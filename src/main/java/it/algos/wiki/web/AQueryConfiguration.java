package it.algos.wiki.web;

import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 30-gen-2019
 * Time: 09:46
 */
//@SpringComponent
//@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
//@Configuration
public class AQueryConfiguration {

    @Bean
    @Scope("prototype")
    public AQueryHTTP foo(String urlDomain) {
        return new AQueryHTTP(urlDomain);
    }// end of method

}// end of class