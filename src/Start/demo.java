package Start;


import java.lang.reflect.Method;

public class demo {
	public static void main(String[] args) {
		A a = new A();
		Method[] methods = a.getClass().getMethods();
		for (Method method : methods) {
			try {
				method.invoke(a,null);
			} catch (Exception ex) {

			}
		}
	}


}

class A{
	public void test1(){
		System.out.println(1);
	}

	public void test2(){
		System.out.println(2);
	}

	public void test3(){
		System.out.println(3);
	}
}