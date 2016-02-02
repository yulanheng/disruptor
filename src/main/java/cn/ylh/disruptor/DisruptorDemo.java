package cn.ylh.disruptor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
public class DisruptorDemo {
	private static int threadNum=5000;
	private static int size=100;
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws InterruptedException {
		ThreadFactory threadFactory=new DisruptorThreadFactory();
		int ringBufferSize =64; // RingBuffer 大小，必须是 2 的 N 次方；
		EventFactory<LongEvent> eventFactory=new LongEventFactory();
		//Disruptor<LongEvent> disruptor=new Disruptor<>(eventFactory, ringBufferSize, threadFactory, ProducerType.MULTI, new YieldingWaitStrategy());
		Disruptor<LongEvent> disruptor=new Disruptor<>(eventFactory, ringBufferSize, threadFactory, ProducerType.MULTI, new BlockingWaitStrategy());
		EventHandler<LongEvent> eventHandler1=new LongEventHandler("eventHandler1");
		EventHandler<LongEvent> eventHandler2=new LongEventHandler("eventHandler2");
		EventHandler<LongEvent> eventHandler3=new LongEventHandler("eventHandler3");
		//disruptor.handleEventsWith(eventHandler1);
		WorkHandler<LongEvent> workHandler1=new LongWorkHandler("workHandler1");
		WorkHandler<LongEvent> workHandler2=new LongWorkHandler("workHandler2");
		WorkHandler<LongEvent>[] workHandlers=new WorkHandler[2];
		for(int i=0;i<2;i++){
			WorkHandler<LongEvent> workHandler=new LongWorkHandler("workHandler"+(i+1));
			workHandlers[i]=workHandler;
		}
		//disruptor.handleEventsWithWorkerPool(workHandlers);
		//设置错误处理,默认FatalExceptionHandler会停止消费，IgnoreExceptionHandler打印错误信息继续消费下一个
		//disruptor.handleExceptionsFor(eventHandler).with(new IgnoreExceptionHandler());
		disruptor.setDefaultExceptionHandler(new IgnoreExceptionHandler());

		RingBuffer<LongEvent> ringBuffer=disruptor.start();
		long bufferSize=disruptor.getBufferSize();
		System.out.println(ringBuffer.get(0));
		System.out.println(ringBuffer.get(0).getValue());
		System.out.println("bufferSize="+bufferSize);
		CountDownLatch cdl=new CountDownLatch(threadNum);
		long start=System.currentTimeMillis();
		for(int j=0;j<threadNum;j++){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					for(long i=0;i<size;i++){
						long seq=ringBuffer.next();
						try {
							LongEvent longEvent=ringBuffer.get(seq);
							longEvent.setValue(i);
						} finally {
							ringBuffer.publish(seq);
						}
					}
					cdl.countDown();
				}
			}).start();
		}
		cdl.await();
		long end=System.currentTimeMillis();
		System.out.println("花费:"+(end-start));
		disruptor.shutdown();
	}
}
