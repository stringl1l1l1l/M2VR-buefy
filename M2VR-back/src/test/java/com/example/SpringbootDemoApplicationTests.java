package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootDemoApplicationTests {

    @Test
    void contextLoads() throws Exception {
     MinioTests.testMinio2();
    }
}
