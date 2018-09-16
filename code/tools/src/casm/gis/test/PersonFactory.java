package casm.gis.test;

/*
 * Create a simple engineering class
 * 2017-04-27 02:56:41
 */
public class PersonFactory {

	public static Person createPerson(String type){
		Person person = null;
		if("boy".equals(type)){
			person = new Boy();
			
		}else if("girl".equals(type)){
			person = new Girl();
		}
		return person;
	}
	
	public static void main(String[] args) {
		Person person = createPerson("boy");
		person.talk();
		person.run();
		System.out.println("===============================");
		Person person2 = createPerson("girl");
		person2.talk();
		person2.run();
	}
}
