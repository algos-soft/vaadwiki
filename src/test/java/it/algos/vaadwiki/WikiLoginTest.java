package it.algos.vaadwiki;

import it.algos.wiki.request.RequestWikiLogin;
import it.algos.wiki.request.RequestWikiReadPages;
import it.algos.wiki.web.AQueryLogin;
import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.ArrayList;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 27-gen-2019
 * Time: 14:13
 */
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("wikilogin")
@DisplayName("Test per il login al server wiki")
public class WikiLoginTest extends ATest {


    @InjectMocks
    protected AQueryLogin aQueryLogin;


    @BeforeAll
    public void setUp() {
        super.setUpTest();
        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(aQueryLogin);
        aQueryLogin.text = text;
    }// end of method


    @Test
    public void prova() {
//        domain = "https://it.wikipedia.org/w/api.php?action=query&format=json&meta=tokens&type=login";
//        domain = "https://it.wikipedia.org/w/api.php?format=json&formatversion=2&action=login";


        ArrayList<Long> lista = new ArrayList<>();
        lista.add(77500987L);
        lista.add(77330987L);
//String pippo= new AQueryLogin().urlRequest("");
//        aQueryLogin.urlRequest();
    }// end of single test

}// end of class
