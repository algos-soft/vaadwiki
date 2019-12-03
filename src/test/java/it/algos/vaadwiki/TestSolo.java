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

    @Test
    public void aComponent2() {
        Assert.assertTrue(context.getBean(WLogin.class) != null);
        String[] tests = {
                "AAA123",
                "ABCDEFGH123",
                "XXXX123",
                "XYZ123ABC",
                "123123",
                "X123",
                "123",
        };
        for (String test : tests) {
            System.out.println(test + " " +test.matches(".+123"));
        }
    }

    @Test
    public void aComponent3() {
        Assert.assertTrue(context.getBean(WLogin.class) != null);
        String[] tests = {
                "AAA,123",
                ",ABCDEFGH123",
                "XXXX123,",
                "XY,Z123ABC",
                "1231,23",
                "X1,,23",
                "1,2,3,",
        };
        for (String test : tests) {
            System.out.println(test + " " +test.matches(".*,.*"));
        }
    }

}// end of class
