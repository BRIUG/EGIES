package casm.gis.test;

/*
 * Create a Boy implementation class
 * 2017-04-27 02:44:11
 */
public class Boy implements Person {

	@Override
	public void talk() {
		System.out.println("The boy is talking....");
	}

	@Override
	public void run() {
		System.out.println("The boy is running....");
	}

}
