package cn.ylh.disruptor;

import java.util.concurrent.ThreadFactory;

public class DisruptorThreadFactory implements ThreadFactory{

	@Override
	public Thread newThread(Runnable r) {
		Thread t=new Thread(r);
		return t;
	}

}
