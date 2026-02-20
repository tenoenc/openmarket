package com.teno.openmarket.user.feature;

import com.teno.openmarket.test.support.BaseControllerTest;
import com.teno.openmarket.test.support.WithMockUserId;
import com.teno.openmarket.user.config.TestUserSecurityConfig;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = TestUserSecurityConfig.class)
public abstract class UserControllerTest extends BaseControllerTest {
}
