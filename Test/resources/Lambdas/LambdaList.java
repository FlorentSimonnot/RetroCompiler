public class LambdaList{
    private final List<String> list;

    public LambdaList(){
        list = new ArrayList<>();
    }

    public void add(String s){
        list.add(s);
    }

    public void showList(){
        list.foreach(System.out::println);
    }

    public static void main(String[] args) {

    }

}