package com.teno.openmarket.system.feature;

import com.teno.openmarket.system.config.TestSystemSecurityConfig;
import com.teno.openmarket.test.support.BaseControllerTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = TestSystemSecurityConfig.class)
public abstract class SystemControllerTest extends BaseControllerTest {
}
