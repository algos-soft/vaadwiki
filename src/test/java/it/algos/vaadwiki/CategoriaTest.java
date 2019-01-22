package it.algos.vaadwiki;

import it.algos.vaadflow.application.StaticContextAccessor;
import it.algos.vaadwiki.application.WikiCost;
import it.algos.vaadwiki.modules.categoria.Categoria;
import it.algos.vaadwiki.modules.categoria.CategoriaService;
import it.algos.wiki.Api;
import it.algos.wiki.WikiLogin;
import it.algos.wiki.WrapCat;
import it.algos.wiki.request.RequestWikiCat;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 20-gen-2019
 * Time: 20:48
 */
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("cat")
@DisplayName("Test per la collezione Categoria")
public class CategoriaTest extends ATest {


    @InjectMocks
    public Api api;

    @InjectMocks
    public CategoriaService categoriaService;

//    @InjectMocks
//    protected WikiLogin wikiLogin;

    @InjectMocks
    public RequestWikiCat requestCat;


//    private String titleCategoria = "Attori statunitensi";
//private String titleCategoria = "Nati nel 1985";
//private String titleCategoria = "Nati nel 1942";
//private String titleCategoria = "Nati nel 1435";
//private String titleCategoria = "Nati nel 1715";
//private String titleCategoria = "Nati nel 1815";
//private String titleCategoria = "Nati nel 1865";
//private String titleCategoria = "Nati nel 1885";
    private String titleCategoria = "Nati nel 1895";

//    private int previstoSizeAttoriStatunitensi = 7843;
//private int previstoSizeAttoriStatunitensi = 4616;
//private int previstoSizeAttoriStatunitensi = 2175;
//private int previstoSizeAttoriStatunitensi = 27;
//private int previstoSizeAttoriStatunitensi = 93;
//private int previstoSizeAttoriStatunitensi = 297;
//private int previstoSizeAttoriStatunitensi = 518;
//private int previstoSizeAttoriStatunitensi = 935;
    private int previstoSizeAttoriStatunitensi = 1057;

    private String textPage;


    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(api);
        MockitoAnnotations.initMocks(categoriaService);
        MockitoAnnotations.initMocks(requestCat);
        requestCat.wikiLogin=new WikiLogin("Gacbot@Gacbot", "tftgv0vhl16c0qnmfdqide3jqdp1i5m7");
        requestCat.inizia();
    }// end of method


    @Test
    public void download() {
        ArrayList<WrapCat> listaWrap = null;
        previstoIntero = previstoSizeAttoriStatunitensi;
        ArrayList<Categoria> listaCat;
        requestCat.esegue(titleCategoria);
        listaWrap = requestCat.getListaWrapCat();

        assertNotNull(listaWrap);
        ottenutoIntero = listaWrap.size();
        assertEquals(previstoIntero, ottenutoIntero);
    }// end of single test

}// end of class
