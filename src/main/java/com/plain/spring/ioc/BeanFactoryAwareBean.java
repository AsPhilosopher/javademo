package com.plain.spring.ioc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;

public class BeanFactoryAwareBean implements BeanFactoryAware {
    private DefaultListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("setBeanFactory.........................");
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    public void onApplicationEvent() {
        System.out.println("ContextRefreshed...................");

        BeanDefinitionBuilder userBeanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
        userBeanDefinitionBuilder.addPropertyValue("username", "chinfeng");
        userBeanDefinitionBuilder.addPropertyValue("password", "123456");
        beanFactory.registerBeanDefinition("user", userBeanDefinitionBuilder.getRawBeanDefinition());

        /**
         * BeanDefinitionHolder持有一个BeanDefinition，名称，和别名数组。在Spring内部，它用来临时保存BeanDefinition来传递BeanDefinition。
         */
        /*BeanDefinitionHolder beanDefinitionHolder1 = new BeanDefinitionHolder(userBeanDefinitionBuilder.getBeanDefinition(), "user");
        beanFactory.registerBeanDefinition(beanDefinitionHolder1.getBeanName(), beanDefinitionHolder1.getBeanDefinition());*/


        BeanDefinitionBuilder usersBeanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(UserService.class);
        usersBeanDefinitionBuilder.addPropertyReference("user", "user");
        beanFactory.registerBeanDefinition("userService", usersBeanDefinitionBuilder.getRawBeanDefinition());

        /*BeanDefinitionHolder beanDefinitionHolder2 = new BeanDefinitionHolder(usersBeanDefinitionBuilder.getBeanDefinition(), "userService");
        beanFactory.registerBeanDefinition(beanDefinitionHolder2.getBeanName(), beanDefinitionHolder2.getBeanDefinition());*/
    }
}
