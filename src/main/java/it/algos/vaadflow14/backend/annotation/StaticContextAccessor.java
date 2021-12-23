package it.algos.vaadflow14.backend.annotation;


import com.vaadin.flow.spring.annotation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;

import javax.annotation.*;

/**
 * Created by gac on 01/07/17
 */
@SpringComponent
public class StaticContextAccessor {

    private static StaticContextAccessor instance;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void registerInstance() {
//        log.info("Algos - Crea uno StaticContext.");
        instance = this;
    }

    public static <T> T getBean(Class<T> clazz) {
        return instance.applicationContext.getBean(clazz);
    }

}
