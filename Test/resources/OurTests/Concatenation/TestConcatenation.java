import java.util.List;
import java.util.ArrayList;

public class TestConcatenation{
    private final String s;
    private final int i;
    private final double d;
    private final Double dd;
    private final char c;
    private final Inner in;
    private final List<Inner> inners;
    private final byte byt;

    static class Inner{
        private final int[] array;
        private final String ss;
        private final float f;

        public Inner(int[] array, String ss, float f) {
            this.array = array;
            this.ss = ss;
            this.f = f;
        }

        public String toString(){
            return array.length + " and " + ss;
        }

    }

    public TestConcatenation(String s, int i, double d, Double dd, char c, Inner inner, byte byt) {
        this.s = s;
        this.i = i;
        this.d = d;
        this.dd = dd;
        this.c = c;
        this.in = inner;
        this.inners = new ArrayList<>();
        this.byt = byt;
    }

    public void addInner(Inner in){
        inners.add(in);
    }

    public String getS(){return s;}

    public String getIS(){return s + " * " + i;}

    public String testConcat(){
        var sj = new StringBuilder("");
        for(var i = 0; i < inners.size(); i++){
            sj.append("\n").append(inners.get(i));
        }
        return c + " " + getIS() + " --- " + getS() + " and " + d + " and inner is " + in + " or " + sj + " * " + byt + " | " + in.f;
    }

    public static void main(String[] args) {
        byte byt = 100;
        var test = new TestConcatenation("Salut", 3, 23.0, new Double(23), 'c', new TestConcatenation.Inner(new int[]{1, 2}, "Blabla", 3f), byt);
        test.addInner(new TestConcatenation.Inner(new int[]{3, 4}, "blibli", 10f));
        test.addInner(new TestConcatenation.Inner(new int[]{5, 6}, "Bloblo", 1f));
        System.out.println(test.testConcat());
    }
}