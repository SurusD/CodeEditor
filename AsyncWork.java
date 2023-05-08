package org.surus.lang.tools;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AsyncWork<G, R> {
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	public Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());
    
	public abstract R doInBackground(G... args);

	public abstract void onPostExecute(R result);

	public final void execute(G... args) {
		Callable<R> call = ()->{
			return doInBackground(args);
		};
	    
	}
}