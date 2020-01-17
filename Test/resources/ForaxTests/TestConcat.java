import java.net.URI;
import java.net.URISyntaxException;

class TestConcat {
String a = "A";
int lo = 3;

  public String toString() throws IllegalArgumentException{
	   StringBuilder sb = new StringBuilder("");
	double val2 = 4.0;
	sb.append("lo ").append(5).append(val2);
	sb.append(" la");
	return sb.toString();
}

	private int test33(int i){
		System.out.println(lo+" * "+a+" = "+i );
		return i+1;
	}

	private String test33P(String s){
		String c = s.concat("a").concat("b");
		System.out.println(lo+" * "+a+" = "+3L+" / "+3.0 + " " + new TestConcat().toString() + " " + c.concat("c"));
		return c;
	}

  private static void testConcat() throws URISyntaxException {
    URI uri = new URI("http://www.u-pem.fr");
    int value = 5;
    String s = "uri " + uri + " value " + value + ".";
    System.out.println(s.toString());
  }

  public String getA(){
	int b = 3;
	return lo + " * " + a;
}

private static String testConcatStringBuilder(){
   StringBuilder sb = new StringBuilder("record[");
	sb.append("]");	
	String res = sb.toString();
return res;
}
  
  private static void testConcat2() {
    long val1 = 5L;
    double val2 = 4.0;
    int val3 = 3;
    String s = val1 + " " + val2 + " " + val3;
    System.out.println(s);
  }
  
  private static void testConcat3() {
    String text = "hello";
    int value = 5;
    String s = text + "" + text;
    System.out.println(s);
  }

  public static void main(String[] args) throws URISyntaxException {
    testConcat();
    testConcat2();
    testConcat3();
TestConcat obj = new TestConcat();
	String res = obj.a;
	System.out.println(obj.getA());
	System.out.println(obj.test33(33));
System.out.println(obj.test33P("Siiis"));
	
  }
}
