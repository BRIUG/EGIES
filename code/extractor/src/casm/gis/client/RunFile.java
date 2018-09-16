package casm.gis.client;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import casm.gis.file.OPFile;
import casm.gis.full.config.InitParams;
import casm.gis.util.StringUtils;

/*
 * Timed scan file
 * 2017-06-04 16:20:35
 */
public class RunFile  implements ServletContextListener{
	private long PERIODTIME = getPeriodTime();
	private String SOURCEPATH = getSourcePath();
	
	protected long getPeriodTime(){
		String pt = StringUtils.getConfigParam(InitParams.PERIOD, "2", InitParams.PROPERTIES_NAME);
		long periodTime = Long.valueOf(pt);
		return periodTime*1000*60;
	}
	
	protected String getSourcePath(){
		return StringUtils.getConfigParam(InitParams.SOURCE_FILEPATH, "", InitParams.PROPERTIES_NAME);
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("destroyed !");
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Timer tim = new Timer();
		TimerTask tTask = new TimerTask(){
			@Override
			public void run() {
//				long fileStart = System.currentTimeMillis();
//				OPFile.saveData(SOURCEPATH);
				OPFile.saveDataSuper(SOURCEPATH);
//				long fileEnd = System.currentTimeMillis();
//				System.out.println("Your abstract file total spent on "+(fileEnd-fileStart)+" ms.");
			}
			
		};
		tim.schedule(tTask, 0, PERIODTIME);
		
		
	}

}
