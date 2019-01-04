package it.algos.vaadwiki.security;

import it.algos.vaadflow.annotation.AIScript;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 04-gen-2019
 * Time: 15:16
 */
//@EnableWebSecurity
//@Configuration
//@AIScript(sovrascrivibile = true)
public class NoSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().antMatchers("/").permitAll();
    }// end of method

}// end of class
