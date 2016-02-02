package cn.ylh.disruptor;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
public class BigPlate {

	private static int size=100;
	private static int produceThreadNum=500;
	private static int  consumeThreadNum=2;
	private static CountDownLatch cdl=new CountDownLatch(produceThreadNum+consumeThreadNum);
	private static AtomicLong count=new AtomicLong();
	
	
	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<Object> blockingQueue=new ArrayBlockingQueue<Object>(64);
		long start=System.currentTimeMillis();
		for(int i = 0; i < produceThreadNum; i++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					for(long j=0;j<size;j++){
						try {
							A a=new A();
							a.setValue(j);
							blockingQueue.put(a);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					cdl.countDown();
					
				}
			}).start();
		}
		for(int i = 0; i < consumeThreadNum; i++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true){
						try {
							if(count.incrementAndGet()>=produceThreadNum*size){
								break;
							}
							blockingQueue.take();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					}
					cdl.countDown();
					
				}
			}).start();
		}
		cdl.await();
		long end=System.currentTimeMillis();
		System.out.println("BlockingQueue一共花费:"+(end-start));
	}
}
class A{
	private long value;

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}
    
	
}