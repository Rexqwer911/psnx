package com.rexqwer.psnx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
class PsnxApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
    public void hashFormdataParameters200() {
        
        MainService mainService = mock(MainService.class);
        MainController mainController = new MainController(mainService);
        ReflectionTestUtils.setField(mainController, "tkn", "testToken");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("test", "value");

        ResponseEntity<MainController.ResponseDTO> response = mainController.hashFormdataParameters(request, Optional.of("testToken"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MainController.ResponseStatus.success, response.getBody().getStatus());
    }

    @Test
    public void hashFormdataParameters403() {

        MainService mainService = mock(MainService.class);
        MainController mainController = new MainController(mainService);
        ReflectionTestUtils.setField(mainController, "tkn", "testToken");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("test", "value");

        ResponseEntity<MainController.ResponseDTO> response = mainController.hashFormdataParameters(request, Optional.of("wrongToken"));

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(MainController.ResponseStatus.fail, response.getBody().getStatus());
    }

    @Test
    public void hashFormdataParameters400() {
        MainService mainService = mock(MainService.class);
        MainController mainController = new MainController(mainService);
        ReflectionTestUtils.setField(mainController, "tkn", "testToken");

        MockHttpServletRequest request = new MockHttpServletRequest();

        ResponseEntity<MainController.ResponseDTO> response = mainController.hashFormdataParameters(request, Optional.of("testToken"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MainController.ResponseStatus.fail, response.getBody().getStatus());
    }
}
