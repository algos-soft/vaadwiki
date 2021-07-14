package it.algos.unit;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.wrapper.*;
import it.algos.vaadflow14.wiki.*;
import it.algos.vaadwiki.wiki.*;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vaadin.flow.component.textfield.TextField;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 12-lug-2021
 * Time: 21:59
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("WrapCatTest")
@DisplayName("Test di unit")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WrapCatTest extends ATest {


    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    WrapCat istanza;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpAll() {
        super.setUpStartUp();

        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(istanza);
        Assertions.assertNotNull(istanza);
    }


    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();
    }

    @Test
    @Order(1)
    @DisplayName("Primo test")
    void getLabelHost() {
    }

    //    @Test
    //    @Order(21)
    //    @DisplayName("21 - legge una lista di WrapCat (come user) di una categoria")
    //    public void leggeCategoria() {
    //        List<WrapCat> lista;
    //        System.out.println("21 - legge una lista di WrapCat (come user) di una categoria");
    //
    //        sorgente = CAT_INESISTENTE;
    //        previstoIntero = 0;
    //        lista = service.getWrapCat(sorgente);
    //        assertNotNull(lista);
    //        assertEquals(lista.size(), previstoIntero);
    //        System.out.println(VUOTA);
    //        System.out.println(String.format("Categoria: %s", sorgente));
    //        System.out.println(String.format("Nessuna pagina"));
    //
    //        sorgente = CAT_1435;
    //        previstoIntero = 33;
    //        inizio = System.currentTimeMillis();
    //        lista = service.getWrapCat(sorgente);
    //        assertNotNull(lista);
    //        assertEquals(lista.size(), previstoIntero);
    //        System.out.println(VUOTA);
    //        System.out.println(String.format("Categoria: %s", sorgente));
    //        System.out.println(String.format("Ce ne sono %s", lista.size()));
    //        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));
    //        printCat(lista);
    //
    //        sorgente = CAT_1935;
    //        previstoIntero = TOT_CAT_1935;
    //        inizio = System.currentTimeMillis();
    //        lista = service.getWrapCat(sorgente);
    //        assertNotNull(lista);
    //        //        assertEquals(lista.size(), previstoIntero);
    //        System.out.println(VUOTA);
    //        System.out.println(String.format("Categoria: %s", sorgente));
    //        System.out.println(String.format("Ce ne sono %s", lista.size()));
    //        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));
    //        System.out.println("Non faccio vedere le pagine perché sono troppe");
    //    }

    //    @Test
    //    @Order(22)
    //    @DisplayName("22 - WrapCat (come user) pages/subcat/files di una categoria")
    //    public void leggeCategoria2() {
    //        List<WrapCat> lista;
    //        AECatType typeCat;
    //        System.out.println("22 - legge (come user) pages/subcat/files di una categoria");
    //
    //        sorgente = CAT_ROMANI;
    //
    //        typeCat = AECatType.file;
    //        previstoIntero = 0;
    //        inizio = System.currentTimeMillis();
    //        lista = service.getWrapCat(sorgente, typeCat);
    //        assertNotNull(lista);
    //        assertEquals(lista.size(), previstoIntero);
    //        System.out.println(VUOTA);
    //        System.out.println(String.format("Categoria: %s Ricerca di: %s", sorgente, typeCat.getTag()));
    //        System.out.println(String.format("Non ce ne sono"));
    //
    //        typeCat = AECatType.subcat;
    //        previstoIntero = 60;
    //        inizio = System.currentTimeMillis();
    //        lista = service.getWrapCat(sorgente, typeCat);
    //        assertNotNull(lista);
    //        assertEquals(lista.size(), previstoIntero);
    //        System.out.println(VUOTA);
    //        System.out.println(String.format("Categoria: %s Ricerca di: %s", sorgente, typeCat.getTag()));
    //        System.out.println(String.format("Ce ne sono %s", lista.size()));
    //        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));
    //
    //        typeCat = AECatType.page;
    //        previstoIntero = 78;
    //        inizio = System.currentTimeMillis();
    //        lista = service.getWrapCat(sorgente, typeCat);
    //        assertNotNull(lista);
    //        assertEquals(lista.size(), previstoIntero);
    //        System.out.println(VUOTA);
    //        System.out.println(String.format("Categoria: %s Ricerca di: %s", sorgente, typeCat.getTag()));
    //        System.out.println(String.format("Ce ne sono %s", lista.size()));
    //        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));
    //
    //        previstoIntero = 78;
    //        inizio = System.currentTimeMillis();
    //        lista = service.getWrapCat(sorgente);
    //        assertNotNull(lista);
    //        assertEquals(lista.size(), previstoIntero);
    //        System.out.println(VUOTA);
    //        System.out.println(String.format("Categoria: %s Ricerca generica che automaticamente cerca solo %s", sorgente, typeCat.getTag()));
    //        System.out.println(String.format("Ce ne sono %s", lista.size()));
    //        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));
    //
    //        typeCat = AECatType.all;
    //        previstoIntero = 138;
    //        inizio = System.currentTimeMillis();
    //        lista = service.getWrapCat(sorgente, typeCat);
    //        assertNotNull(lista);
    //        assertEquals(lista.size(), previstoIntero);
    //        System.out.println(VUOTA);
    //        System.out.println(String.format("Categoria: %s Ricerca di: %s", sorgente, typeCat.getTag()));
    //        System.out.println(String.format("Ce ne sono %s", lista.size()));
    //        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));
    //    }

    //    @Test
    //    @Order(23)
    //    @DisplayName("23 - legge una lista di pageid (come user) di una categoria")
    //    public void getLongCat() {
    //        List<Long> lista;
    //        System.out.println("23 - legge una lista di pageid (come user) di una categoria");
    //
    //        sorgente = CAT_INESISTENTE;
    //        previstoIntero = 0;
    //        lista = service.getLongCat(sorgente);
    //        assertNotNull(lista);
    //        assertEquals(lista.size(), previstoIntero);
    //        System.out.println(VUOTA);
    //        System.out.println(String.format("Categoria: %s", sorgente));
    //        System.out.println(String.format("Nessuna pagina"));
    //
    //        sorgente = CAT_1435;
    //        previstoIntero = 33;
    //        inizio = System.currentTimeMillis();
    //        lista = service.getLongCat(sorgente);
    //        assertNotNull(lista);
    //        assertEquals(previstoIntero, lista.size());
    //        System.out.println(VUOTA);
    //        System.out.println(String.format("Categoria: %s", sorgente));
    //        System.out.println(String.format("Ce ne sono %s", lista.size()));
    //        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));
    //        printLong(lista);
    //
    //        sorgente = CAT_1935;
    //        previstoIntero = TOT_CAT_1935;
    //        inizio = System.currentTimeMillis();
    //        lista = service.getLongCat(sorgente);
    //        assertNotNull(lista);
    //        //        assertEquals(previstoIntero, lista.size());
    //        System.out.println(VUOTA);
    //        System.out.println(String.format("Categoria: %s", sorgente));
    //        System.out.println(String.format("Ce ne sono %s", lista.size()));
    //        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));
    //        System.out.println("Non faccio vedere le pagine perché sono troppe");
    //
    //        sorgente = CAT_ROMA;
    //        inizio = System.currentTimeMillis();
    //        lista = service.getLongCat(sorgente);
    //        assertNotNull(lista);
    //        System.out.println(VUOTA);
    //        System.out.println(String.format("Categoria: %s", sorgente));
    //        System.out.println(String.format("Ce ne sono %s", lista.size()));
    //        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));
    //        System.out.println("Non faccio vedere le pagine perché sono troppe");
    //    }

    //    @Test
    //    @Order(24)
    //    @DisplayName("24 - legge una stringa di pageid (come user) di una categoria")
    //    public void getLongCat2() {
    //        String striscia;
    //        System.out.println("24 - legge una stringa di pageid (come user) di una categoria");
    //
    //        sorgente = CAT_INESISTENTE;
    //        previsto = VUOTA;
    //        striscia = service.getPageidsCat(sorgente);
    //        assertFalse(text.isValid(striscia));
    //        assertEquals(previsto, ottenuto);
    //        System.out.println(VUOTA);
    //        System.out.println(String.format("Categoria: %s", sorgente));
    //        System.out.println(String.format("Nessuna pagina"));
    //
    //        sorgente = CAT_1435;
    //        inizio = System.currentTimeMillis();
    //        striscia = service.getPageidsCat(sorgente);
    //        assertTrue(text.isValid(striscia));
    //        System.out.println(VUOTA);
    //        System.out.println(String.format("Categoria: %s", sorgente));
    //        System.out.println(String.format("Ce ne sono alcune"));
    //        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));
    //        System.out.println(striscia);
    //
    //        //        sorgente = CAT_1935;
    //        //        previstoIntero = 1987;
    //        //        inizio = System.currentTimeMillis();
    //        //        lista = service.getLongCat(sorgente);
    //        //        assertNotNull(lista);
    //        //        assertEquals(lista.size(), previstoIntero);
    //        //        System.out.println(VUOTA);
    //        //        System.out.println(String.format("Categoria: %s", sorgente));
    //        //        System.out.println(String.format("Ce ne sono %s", lista.size()));
    //        //        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));
    //        //        System.out.println("Non faccio vedere le pagine perché sono troppe");
    //    }

    //    @Test
    //    @Order(25)
    //    @DisplayName("25 - legge titles (come user) una categoria")
    //    public void getTitleCat() {
    //        List<String> lista;
    //        System.out.println("25 - legge titles (come user) una categoria");
    //
    //        sorgente = CAT_INESISTENTE;
    //        previstoIntero = 0;
    //        lista = service.getTitleCat(sorgente);
    //        assertNotNull(lista);
    //        assertEquals(lista.size(), previstoIntero);
    //        System.out.println(VUOTA);
    //        System.out.println(String.format("Categoria: %s", sorgente));
    //        System.out.println(String.format("Nessuna pagina"));
    //
    //        sorgente = CAT_1435;
    //        previstoIntero = 33;
    //        inizio = System.currentTimeMillis();
    //        lista = service.getTitleCat(sorgente);
    //        assertNotNull(lista);
    //        assertEquals(lista.size(), previstoIntero);
    //        System.out.println(VUOTA);
    //        System.out.println(String.format("Categoria: %s", sorgente));
    //        System.out.println(String.format("Ce ne sono %s", lista.size()));
    //        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));
    //        printTitle(lista);
    //
    //        sorgente = CAT_1935;
    //        previstoIntero = TOT_CAT_1935;
    //        inizio = System.currentTimeMillis();
    //        lista = service.getTitleCat(sorgente);
    //        assertNotNull(lista);
    //        //        assertEquals(lista.size(), previstoIntero);
    //        System.out.println(VUOTA);
    //        System.out.println(String.format("Categoria: %s", sorgente));
    //        System.out.println(String.format("Ce ne sono %s", lista.size()));
    //        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));
    //        System.out.println("Non faccio vedere le pagine perché sono troppe");
    //    }

    //    @Test
    //    @Order(26)
    //    @DisplayName("26 - legge il numero totale di pagine di una categoria")
    //    public void getTotaleCategoria() {
    //        System.out.println("26 - legge il numero totale di pagine di una categoria");
    //
    //        sorgente = CAT_INESISTENTE;
    //        previstoIntero = 0;
    //        ottenutoIntero = service.getTotaleCategoria(sorgente);
    //        assertEquals(ottenutoIntero, previstoIntero);
    //        System.out.println(VUOTA);
    //        System.out.println(String.format("La categoria: '%s' non contiene nessuna pagina", sorgente));
    //
    //        sorgente = CAT_1435;
    //        previstoIntero = 33;
    //        ottenutoIntero = service.getTotaleCategoria(sorgente);
    //        //        assertEquals(ottenutoIntero, previstoIntero);
    //        System.out.println(VUOTA);
    //        System.out.println(String.format("La categoria: '%s' contiene %d pagine", sorgente, ottenutoIntero));
    //
    //        sorgente = CAT_1935;
    //        previstoIntero = TOT_CAT_1935;
    //        ottenutoIntero = service.getTotaleCategoria(sorgente);
    //        //        assertEquals(ottenutoIntero, previstoIntero);
    //        System.out.println(VUOTA);
    //        System.out.println(String.format("La categoria: '%s' contiene %d pagine", sorgente, ottenutoIntero));
    //    }



    private void printCat(List<WrapCat> lista) {
        for (WrapCat wrap : lista) {
            System.out.print(wrap.getPageid());
            System.out.print(SEP);
            System.out.println(wrap.getTitle());
        }
    }

    private void printWrap(List<WrapDueStringhe> listaWrap) {
        System.out.println("********");
        if (array.isAllValid(listaWrap)) {
            for (WrapDueStringhe wrap : listaWrap) {
                System.out.println(wrap.getPrima() + SEP + wrap.getSeconda());
            }
        }
    }



    private void printMappaPar(Map<String, Object> mappa) {
        System.out.println("11 - crea una mappa da un singolo oggetto BJSON");
        System.out.println(VUOTA);
        for (String key : mappa.keySet()) {
            System.out.print(key);
            System.out.print(SEP);
            System.out.println(mappa.get(key));
        }
    }


    private void printWikiPage(WikiPage wikiPage) {
        System.out.println("WikiPage");
        System.out.println(VUOTA);
        System.out.println("pageid" + SEP + wikiPage.getPageid());
        System.out.println("ns" + SEP + wikiPage.getNs());
        System.out.println("title" + SEP + wikiPage.getTitle());
        System.out.println("pagelanguage" + SEP + wikiPage.getPagelanguage());
        System.out.println("pagelanguagehtmlcode" + SEP + wikiPage.getPagelanguagehtmlcode());
        System.out.println("pagelanguagedir" + SEP + wikiPage.getPagelanguagedir());
        System.out.println("touched" + SEP + wikiPage.getTouched());
        System.out.println("length" + SEP + wikiPage.getLength());
        System.out.println("revid" + SEP + wikiPage.getRevid());
        System.out.println("parentid" + SEP + wikiPage.getParentid());
        System.out.println("user" + SEP + wikiPage.getUser());
        System.out.println("userid" + SEP + wikiPage.getUserid());
        System.out.println("timestamp" + SEP + wikiPage.getTimestamp());
        System.out.println("size" + SEP + wikiPage.getSize());
        System.out.println("comment" + SEP + wikiPage.getComment());
        System.out.println("content" + SEP + wikiPage.getContent());
    }


    private void printLong(List<Long> lista) {
        for (Long pageid : lista) {
            System.out.println(pageid);
        }
    }

    private void printTitle(List<String> lista) {
        for (String title : lista) {
            System.out.println(title);
        }
    }

    /**
     * Qui passa al termine di ogni singolo test <br>
     */
    @AfterEach
    void tearDown() {
    }


    /**
     * Qui passa una volta sola, chiamato alla fine di tutti i tests <br>
     */
    @AfterEach
    void tearDownAll() {
    }

}