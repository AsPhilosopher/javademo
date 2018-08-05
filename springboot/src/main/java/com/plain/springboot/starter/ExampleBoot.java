package com.plain.springboot.starter;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ExampleAutoConfigure.class)
@Documented
public @interface ExampleBoot {
}
