public class TestClass {
    private MyEvent myEvent;

    public TestClass() {
        MyEventBus.getDefault().register(this);
    }

    @Subscribe
    public void setMyEvent(MyEvent myEvent) {
        this.myEvent = myEvent;
        System.out.println("setMyEvent()被调用了");
    }
}