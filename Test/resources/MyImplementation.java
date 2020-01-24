public class MyImplementation implements MyInterface{

    public void m(){
        System.out.println("BOUH");
    }

    private void test(MyInterface i){
        i.m();
    }

}