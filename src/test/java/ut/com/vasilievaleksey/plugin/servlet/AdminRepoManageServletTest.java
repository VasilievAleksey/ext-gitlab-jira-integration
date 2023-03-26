package ut.com.vasilievaleksey.plugin.servlet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdminRepoManageServletTest {

    HttpServletRequest mockRequest;
    HttpServletResponse mockResponse;

    @Before
    public void setup() {
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testSomething() {
        String expected = "test";
        when(mockRequest.getParameter(Mockito.anyString())).thenReturn(expected);
        assertEquals(expected,mockRequest.getParameter("some string"));

    }
}
