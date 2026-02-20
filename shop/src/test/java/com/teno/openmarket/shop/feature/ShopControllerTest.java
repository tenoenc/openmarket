package com.teno.openmarket.shop.feature;

import com.teno.openmarket.shop.config.TestShopSecurityConfig;
import com.teno.openmarket.test.support.BaseControllerTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = TestShopSecurityConfig.class)
public abstract class ShopControllerTest extends BaseControllerTest {
}
