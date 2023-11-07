package pl.polsl.informationtheory.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext.context = applicationContext;
    }

    public static <T extends Object> T getBean(Class<T> beanClass) {
       return context.getBean(beanClass);
    }

    public static Resource getResource(String location) {
        return context.getResource(location);
    }
}
