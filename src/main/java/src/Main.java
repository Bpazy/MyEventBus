package src;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException {
        new TestClass();
        MyEventBus.getDefault().post(new MyEvent());
    }
}
