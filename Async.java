package org.surus.lang.tools;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class Async<R, A> {
	public ExecutorService executor = Executors.newSingleThreadExecutor();
	private Future<R> work;

	public abstract R inBackground(A argument);

	public final R execute(A argument){
		Callable<R> callable = ()->{
			return inBackground(argument);
		};
		work = executor.submit(callable);
		try {
		  return getFuture().get();
		} catch (Exception e){
		  return null;
		}
	}
	
	public final boolean isCancelled(){
		return getFuture() == null?true:getFuture().isCancelled();
	}
	
	public final void cancelWork(){
		if (getFuture() != null){
			getFuture().cancel(true);
		}
	}
	
	public final void resume(){
		if (getFuture() != null){
	      getFuture().cancel(false);
		}
	}
	
	public Future<R> getFuture(){
		return work;
	}
	
}