package com.plain.spring.ioc;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class TestMain {
    ApplicationContext applicationContext = new GenericXmlApplicationContext("application.xml");

    @Test
    public void test() {
        BeanFactoryAwareBean beanFactoryAwareBean = (BeanFactoryAwareBean) applicationContext.getBean("beanFactoryAwareBean");
        beanFactoryAwareBean.onApplicationEvent();
        User user = (User) applicationContext.getBean("user");
        UserService userService = (UserService) applicationContext.getBean("userService");
        System.out.println(user);
        System.out.println(userService);
    }
}
