import org.junit.*;
import src.MyEventBus;

public class MyEventBusTest {
    @Test
    public void testGetDefault() {
        MyEventBus aDefault = MyEventBus.getDefault();
        Assert.assertNotNull(aDefault);
    }
}
