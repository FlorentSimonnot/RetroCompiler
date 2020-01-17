public class ToStringClassTest{
	String a = "A"; 
	String b = "b"; 
	String c = "Cc";

	public String toString(){
		StringBuilder sb = new StringBuilder("Record[");
		sb.append(a).append(b).append(c);
		return sb.toString();
	}

	public static void main(String[] args){
		ToStringClassTest t = new ToStringClassTest();
		System.out.println(t);
	}



}
