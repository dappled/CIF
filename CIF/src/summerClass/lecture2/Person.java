package summerClass.lecture2;

public class Person {

	protected int _age;
	protected String _name;
	
	public Person( String name, int age ) {
		_name = name;
		_age = age;
	}

	public String getName() {
		return _name;
	}
	
	public int getAge() {
		return _age;
	}
	
	public boolean equals( Person anotherPerson ) {
		return ( anotherPerson.getName().equals( _name ) && anotherPerson.getAge() == _age );
	}
	
	// Why no definition of compareTo? No obvious comparison
	
}
