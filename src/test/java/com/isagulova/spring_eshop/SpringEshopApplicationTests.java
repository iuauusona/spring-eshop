package com.isagulova.spring_eshop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class SpringEshopApplicationTests {

    @Autowired
    ApplicationContext context;

	@Test
	void contextLoads() {
        Assertions.assertNotNull(context);
	}

}
