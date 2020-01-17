import java.util.Objects;
import java.util.Arrays; 
import java.lang.Float;
import java.lang.Double;
import java.lang.StringBuilder;

record RecordClassTestEquals(int lo, int hi, String a, double d, boolean bool, float f, long l, byte b, int[] array) {
  public RecordClassTestEquals {
    if (lo > hi)  /* referring here to the implicit constructor parameters */
      throw new IllegalArgumentException(String.format("(%d,%d)", lo, hi));
  }

	@Override 
	public boolean equals(Object obj){
		if(!(obj instanceof RecordClassTestEquals)){
			return false;
		}
		var rec = (RecordClassTestEquals) obj; 
		return lo == rec.lo && hi == rec.hi && a.equals(rec.a) &&
			 d == rec.d && bool == rec.bool && f == rec.f && l == rec.l && b == rec.b && Arrays.equals(array, rec.array);
	}

	@Override
	public String toString(){
		var sb = new StringBuilder();
		sb.append(lo).append(hi).append(d).append(bool).append(f).append(l);
		byte[] bytes = new byte[1];
		bytes[0] = b; 
		sb.append(bytes).append(array);
		return sb.toString();
	}


	@Override 
	public int hashCode(){
		var hash = 7;
		hash = 31 * hash + lo; 
		hash = 31 * hash + hi; 
		hash = 31 * hash + Objects.hashCode(a);
		hash = 31 * hash + (int) (Double.doubleToLongBits(d) ^ Double.doubleToLongBits(d) >>> 32);
		hash = 31 * hash + (bool ? 0 : 1);
		hash = 31 * hash + (int) (Float.floatToIntBits(f));
		hash = 31 * hash + (int) (l ^ l >>> 32);
		hash = 31 * hash + b;
		hash = 31 * hash + Arrays.hashCode(array);
		return hash;
	}

	public static void main(String[] args){
		byte b = 100;
		var sb = new StringBuilder();
		var r1 = new RecordClassTestEquals(5, 6, "a", 5.0, true, 3.0f, 3L, b, new int[]{1, 2, 3});
		var r2 = new RecordClassTestEquals(4, 5, "a", 5.0, false, 3.0f, 4L, b, new int[]{1, 2, 3});			
		System.out.println(r1.equals(r1));
		System.out.println(r1.equals(r2));
		System.out.println(r1);
		byte[] bytes = new byte[1];
		bytes[0] = b;
		var s = new String(bytes);
		sb.append(s);
	}

}
