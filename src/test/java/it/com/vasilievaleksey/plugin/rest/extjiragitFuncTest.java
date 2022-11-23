package it.com.vasilievaleksey.plugin.rest;

import com.vasilievaleksey.plugin.controller.extjiragitModel;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class extjiragitFuncTest {

    @Before
    public void setup() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void messageIsValid() {

        String baseUrl = System.getProperty("baseurl");
        String resourceUrl = baseUrl + "/rest/extjiragit/1.0/message";

        RestClient client = new RestClient();
        Resource resource = client.resource(resourceUrl);

        extjiragitModel message = resource.get(extjiragitModel.class);

        assertEquals("wrong message","Hello World",message.getMessage());
    }
}
