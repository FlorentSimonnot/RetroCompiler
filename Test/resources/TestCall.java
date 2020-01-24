public class TestCall{
    private final String a;
    private final int b;

    public TestCall(String a, int b){
        this.a = a;
        this.b = b;
    }

    public int test(Double d, double dd, Integer i){
        test2(i, this.a, d, dd);
        return i*2;
    }

    public void test2(int i, String a, double d, Double dd){
        System.out.println("finish " + i + " " + d);
    }

    public static void main(String[] args){
        var testCall = new TestCall("a", 3);
        testCall.test(new Double(3), 3.0, 3);
    }

}