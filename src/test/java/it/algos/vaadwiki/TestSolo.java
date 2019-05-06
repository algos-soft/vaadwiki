package it.algos.vaadwiki;

import it.algos.wiki.web.WLogin;
import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Fri, 03-May-2019
 * Time: 07:12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSolo {
    @Autowired
    private ApplicationContext context;

    @Test
    public void aComponent() {
        Assert.assertTrue(context.getBean(WLogin.class) != null);
    }

}// end of class
