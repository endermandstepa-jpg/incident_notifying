package com.emergency.alert.config;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

class ApiKeyFilterTest {

    private final ApiKeyFilter filter = new ApiKeyFilter();

    @Test
    void shouldRejectRequestWithoutApiKey() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/events");

        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, new MockFilterChain());

        assertEquals(401, response.getStatus());
    }
}