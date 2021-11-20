package it.algos.vaadflow14.backend.boot;

import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.core.mapping.event.*;
import org.springframework.validation.*;
import org.springframework.validation.beanvalidation.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mar, 04-ago-2020
 * Time: 14:26
 */
//@Configuration
public class FlowConfiguration {

//    @Bean
//    public ValidatingMongoEventListener validatingMongoEventListener() {
//        return new ValidatingMongoEventListener(validator());
//    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        return validator;
    }
}
