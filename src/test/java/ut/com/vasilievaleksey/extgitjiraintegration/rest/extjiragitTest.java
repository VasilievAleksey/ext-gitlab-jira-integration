package ut.com.vasilievaleksey.extgitjiraintegration.rest;

import com.vasilievaleksey.plugin.controller.extjiragit;
import com.vasilievaleksey.plugin.controller.extjiragitModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class extjiragitTest {

    @Before
    public void setup() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void messageIsValid() {
        extjiragit resource = new extjiragit();

        Response response = resource.getMessage();
        final extjiragitModel message = (extjiragitModel) response.getEntity();

        assertEquals("wrong message","Hello World",message.getMessage());
    }
}
