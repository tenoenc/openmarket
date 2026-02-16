package com.teno.openmarket.test.support;

import com.teno.openmarket.common.config.JacksonConfig;
import com.teno.openmarket.common.exception.GlobalExceptionHandler;
import com.teno.openmarket.common.filter.MdcLoggingFilter;
import com.teno.openmarket.common.response.GlobalResponseAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@ContextConfiguration(classes = BaseControllerTest.TestApp.class)
@Import({
    GlobalResponseAdvice.class,
    GlobalExceptionHandler.class,
    JacksonConfig.class,
    MdcLoggingFilter.class
})
public abstract class BaseControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @SpringBootApplication(scanBasePackages = "none")
    public static class TestApp {

    }
}