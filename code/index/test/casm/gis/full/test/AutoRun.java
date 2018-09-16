package casm.gis.full.test;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/*
 * Automatically create an index test method
 * Ability to timed indexed test methods
 * 2017-05-12 23:03:18
 */
public class AutoRun implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("destroy!");
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		Timer tim = new Timer();
		TimerTask tTask = new TimerTask() {
			
			@Override
			public void run() {
				System.out.println("It's work!");
			}
		};
		tim.schedule(tTask, 0,200);
	}

}
