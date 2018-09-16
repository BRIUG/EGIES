package casm.gis.full.client;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import casm.gis.util.StringUtils;
import casm.gis.full.config.InitParams;
import casm.gis.full.index.IndexServer;

/*
 * Used to create an index of operations
 * Implement automatic index creation
 * 2017-05-13 16:56:18
 */
public class OpIndex implements ServletContextListener{
	
	/*
	 * Get the interval to re-do the index again
	 * 2017-05-13 17:03:20
	 */
	private long PERIODTIME = getPeriodTime();
	
	protected long getPeriodTime(){
		String pt = StringUtils.getConfigParam(InitParams.INDEX_PERIOD, "2", InitParams.SEARCH_PROPERTIES);
		long periodTime = Long.valueOf(pt);
		return periodTime*60000;
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("destroy!");
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		Timer tim = new Timer();
		TimerTask tTask = new TimerTask() {
			IndexServer indexServer = (IndexServer) IndexServer.getService();
			
			@Override
			public void run() {
				long indexStart = System.currentTimeMillis();
				indexServer.createIndex();
				long indexEnd = System.currentTimeMillis();
				System.out.println("Your index total spent on "+(indexEnd - indexStart)+" ms.");
			}
		};
		tim.schedule(tTask, 0,PERIODTIME);
	}
}
