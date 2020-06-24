package com.example.devconfig.holder;

import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by AMe on 2020-06-23 12:52.
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    public <T> T getBean(Class<T> type) {
        return applicationContext.getBean(type);
    }

    public <T> Map<String, T> getBeans(Class<T> type) {
        return applicationContext.getBeansOfType(type);
    }
}
