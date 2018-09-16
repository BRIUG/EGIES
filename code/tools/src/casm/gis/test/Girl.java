package casm.gis.test;

/*
 * Create an instance class for a simple factory
 * 2017-04-27 02:47:32
 */
public class Girl implements Person {

	@Override
	public void talk() {
		System.out.println("The girl is talking.....");
	}

	@Override
	public void run() {
		System.out.println("The girl is running.....");
	}

}
