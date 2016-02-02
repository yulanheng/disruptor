package cn.ylh.disruptor;
import com.lmax.disruptor.WorkHandler;
public class LongWorkHandler implements WorkHandler<LongEvent>{

	private String name;
	public  LongWorkHandler(String name) {
		this.name=name;
	}
	@Override
	public void onEvent(LongEvent event) throws Exception {
		 //System.out.println(name+"   ValueEvent: " + event.getValue());
		
	}

}
