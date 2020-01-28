public class TestConcat{

	private static String concat1(){
		return "The result is ";
	}

	private static String concat2(){
		return " 20/20";
	}


	public static void main(String[] args){
		var a = 2; 
		var b = "TEST ";
		//System.out.println(a + " 1s " + b + 5);
		System.out.println(concat1() + " and the winner is " + concat2());
	}

}
