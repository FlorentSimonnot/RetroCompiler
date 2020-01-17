import java.util.ArrayList;

public class Test{

	class Inner{
		private int b = 5;
		private int c = 7; 
		private int d = 7;
		private String s = "Bouh";
		private boolean bool = true;
		private ArrayList<String> list = new ArrayList<>();
		private double dd = .0;
		private long l = 1;

		public void setB(int b){
			this.b = b;
		}

		public void getB(){
			System.out.println(b);
		}

		public void setArray(ArrayList<String> array){this.list = array;}

	}

	class InnerTwo{
		private int b = 5;
		private int c = 7;
		private int d = 7;
		private String s = "Bouh";
		private boolean bool = true;
		private ArrayList<String> list = new ArrayList<>();
		private double dd = .0;
		private long l = 1;

		public void setB(int b){
			this.b = b;
		}

		public void getB(){
			System.out.println(b);
		}

		public void setArray(ArrayList<String> array){this.list = array;}
	}


	public void print(){
		Inner in = new Inner();
		in.b = in.d; 
		System.out.println(in.b + in.c);
		in.list = new ArrayList<>();
		System.out.println(in.list.size());
		in.bool = false;
		in.l = 3;
		System.out.println(in.bool);
		InnerTwo in2 = new InnerTwo();
		in2.b = in2.d;
		System.out.println(in2.dd);
	}

	public static void main(String[] args){
		Inner cl = new Test().new Inner();
		Test t = new Test();
		t.print();
	}

}
