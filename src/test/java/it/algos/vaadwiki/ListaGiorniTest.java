package it.algos.vaadwiki;

import it.algos.vaadwiki.liste.ListaGiorni;
import it.algos.wiki.Api;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 25-gen-2019
 * Time: 16:51
 */
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("listaGiorni")
@DisplayName("Test per l'ordinamento delle liste")
public class ListaGiorniTest extends ATest {


    @InjectMocks
    public Api api;

    @InjectMocks
    public ListaGiorni listaGiorni;


    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(listaGiorni);
    }// end of method


    @Test
    public void comparatore() {
    }// end of single test

}// end of class
