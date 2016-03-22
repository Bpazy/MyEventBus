import org.junit.Assert;
import org.junit.Test;
import src.MyEventBus;

public class MyEventBusTest {
    @Test
    public void testGetDefault() {
        MyEventBus aDefault = MyEventBus.getDefault();
        Assert.assertNotNull(aDefault);
    }
}
