package ut.com.vasilievaleksey.extgitjiraintegration;

import org.junit.Test;
import com.vasilievaleksey.extgitjiraintegration.api.MyPluginComponent;
import com.vasilievaleksey.extgitjiraintegration.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}