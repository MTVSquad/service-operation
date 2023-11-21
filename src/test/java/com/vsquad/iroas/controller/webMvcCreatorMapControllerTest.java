package com.vsquad.iroas.controller;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

//@ContextConfiguration(classes = {TestConfig.class})
public class webMvcCreatorMapControllerTest {

    private MockMvc MockMvc;

    AutoCloseable openMocks;

    @BeforeEach
    public void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        MockMvc = MockMvcBuilders.standaloneSetup(CreatorMapController.class).build();
    }
}
