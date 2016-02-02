package cn.ylh.disruptor;

import com.lmax.disruptor.EventHandler;

public class LongEventHandler implements EventHandler<LongEvent>{

	private String name;
	public  LongEventHandler(String name) {
		this.name=name;
	}
	@Override
	public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
		/* if(sequence==66){
			throw new Exception("出错");
		 }else{
			 System.out.println(name+":  Sequence: " + sequence + "   ValueEvent: " + event.getValue());
		 }*/
		
	}

}
