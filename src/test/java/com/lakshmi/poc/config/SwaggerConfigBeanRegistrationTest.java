package com.lakshmi.poc.config;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class SwaggerConfigBeanRegistrationTest {
    private ApplicationContextRunner contextRunner;

    @Before
    public void setup() {
        contextRunner = new ApplicationContextRunner()
                .withUserConfiguration(SwaggerConfig.class);
    }

    @Test
    public void testSwaggerConfigWhenPropertyNotSet() {
        contextRunner.run(context -> assertAll(
                () -> assertThat(context).hasSingleBean(SwaggerConfig.class)
        ));
    }

    @Test
    public void testSwaggerConfigWhenPropertySetToTrue() {
        contextRunner
                .withPropertyValues("swagger.enabled=true")
                .run(context -> assertAll(
                () -> assertThat(context).hasSingleBean(SwaggerConfig.class)
        ));
    }

    @Test
    public void testSwaggerConfigWhenPropertySetToFalse() {
        contextRunner
                .withPropertyValues("swagger.enabled=false")
                .run(context -> assertAll(
                        () -> assertThat(context).doesNotHaveBean(SwaggerConfig.class)
                ));
    }

    @Test
    public void testSwaggerBuildVersion() {
        contextRunner
                .withPropertyValues("swagger.info.build.version=test-version")
                .run(context -> assertAll(
                        () -> assertEquals("test-version", context.getBean(SwaggerConfig.class).getVersion())
                ));
    }

}