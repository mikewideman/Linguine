package LinGUIne.utilities;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Simple monitor based on java locks and conditions which is AutoCloseable.
 * 
 * @author Kyle Mullins
 */
public class Monitor implements AutoCloseable {

	private Lock monitorLock;
	private Condition monitorCondition;
	
	public Monitor(Lock lock){
		monitorLock = lock;
		monitorCondition = monitorLock.newCondition();
	}
	
	public Monitor(){
		this(new ReentrantLock());
	}
	
	public Monitor enter(){
		monitorLock.lock();
		
		return this;
	}
	
	public void exit(){
		monitorLock.unlock();
	}
	
	public void await() throws InterruptedException{
		monitorCondition.await();
	}
	
	public void signal(){
		monitorCondition.signal();
	}
	
	@Override
	public void close(){
		exit();
	}
}
