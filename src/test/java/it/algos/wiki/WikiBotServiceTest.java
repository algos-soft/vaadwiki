package it.algos.wiki;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadwiki.backend.enumeration.*;
import it.algos.vaadwiki.backend.service.*;
import it.algos.vaadwiki.wiki.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 10-mag-2021
 * Time: 14:07
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValidoWiki")
@DisplayName("WikiBotService - Accesso alle pagine wiki.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WikiBotServiceTest extends WTest {


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private WikiBotService service;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        service = wikiBotService;
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

    @ParameterizedTest
    @MethodSource(value = "PAGINE")
    @Order(1)
    @DisplayName("1 - Controllo e risultato per una pagina")
    //--titolo
    //--pagina valida
    public void isEsisteResult(final String wikiSimplePageCategoryTitle, final boolean paginaValida) {
        System.out.println("1 - Controllo e risultato per una pagina e/o categoria.");
        System.out.println("Si collega come (anonymous)");

        ottenutoRisultato = service.isEsisteResult(wikiSimplePageCategoryTitle);
        assertNotNull(ottenutoRisultato);

        assertEquals(paginaValida, ottenutoRisultato.isValido());
        assertNotEquals(paginaValida, ottenutoRisultato.isErrato());
        assertEquals(paginaValida, textService.isValid(ottenutoRisultato.getValidMessage()));
        assertEquals(paginaValida, textService.isValid(ottenutoRisultato.getResponse()));
        printResult(ottenutoRisultato);

    }


    @ParameterizedTest
    @MethodSource(value = "PAGINE_DUE")
    @Order(2)
    @DisplayName("2 - Esistenza di una pagina")
    //--titolo
    //--pagina o categoria esistente
    public void isEsiste(final String wikiSimplePageCategoryTitle, final boolean paginaCategoriaValida) {
        System.out.println("2 - Esistenza di una pagina e/o categoria.");
        System.out.println("Si collega come (anonymous)");

        ottenutoBooleano = service.isEsiste(wikiSimplePageCategoryTitle);
        assertEquals(paginaCategoriaValida, ottenutoBooleano);
        printEsiste(wikiSimplePageCategoryTitle, ottenutoBooleano);
    }


    @ParameterizedTest
    @MethodSource(value = "CATEGORIE")
    @Order(3)
    @DisplayName("3 - Informazioni generali della categoria")
    //--titolo categoria
    //--categoria esistente
    //--numero di pagine
    //--risultatoEsatto
    public void getInfoCategoria(final String wikiSimpleCategoryTitle, final boolean categoriaEsistente, final int numPagine, final boolean risultatoEsatto) {
        System.out.println("3 - Informazioni generali della categoria.");
        System.out.println("Legge (come anonymous) le info della categoria");
        inizio = System.currentTimeMillis();

        ottenutoRisultato = service.getInfoCategoria(wikiSimpleCategoryTitle);
        assertNotNull(ottenutoRisultato);
        assertEquals(categoriaEsistente, ottenutoRisultato.isValido());
        assertNotEquals(categoriaEsistente, ottenutoRisultato.isErrato());
        assertEquals(categoriaEsistente, textService.isValid(ottenutoRisultato.getValidMessage()));

        printResult(ottenutoRisultato, numPagine, risultatoEsatto);
    }


    @ParameterizedTest
    @MethodSource(value = "CATEGORIE")
    @Order(4)
    @DisplayName("4 - Numero di pagine della categoria")
    //--titolo categoria
    //--categoria esistente
    //--numero di pagine
    //--risultatoEsatto
    public void getTotaleCategoria(final String wikiSimpleCategoryTitle, final boolean categoriaEsistente, final int numPagine, final boolean risultatoEsatto) {
        System.out.println("4 - numero di pagine della categoria.");
        System.out.println("Legge (come anonymous) il numero di pagina della categoria");
        inizio = System.currentTimeMillis();

        ottenutoIntero = service.getTotaleCategoria(wikiSimpleCategoryTitle);
        System.out.println(VUOTA);
        System.out.println(String.format("%s", getEsatto(true, wikiSimpleCategoryTitle, numPagine, ottenutoIntero, risultatoEsatto)));
        System.out.println(String.format("Tempo impiegato per leggere le info della categoria '%s': %s", wikiSimpleCategoryTitle, getTime()));
    }

    @Test
    @Order(5)
    @DisplayName("5 - Accesso alla categoria")
    public void getTotaleCategoria2() {
        System.out.println("5 - Accesso alla categoria.");
        System.out.println(VUOTA);

        sorgente = CAT_INESISTENTE;
        System.out.println("Accesso come anonymous alla categoria");
        ottenutoRisultato = service.getResultCat(sorgente, AETypeUser.anonymous);
        //        assertFalse(ottenutoRisultato.isValido());
        //        assertTrue(ottenutoRisultato.getErrorMessage().startsWith(NO_CAT));
        //        System.out.println(ottenutoRisultato.getErrorMessage());
        printResult(ottenutoRisultato);

        sorgente = CAT_1167;
        ottenutoRisultato = service.getResultCat(sorgente, AETypeUser.anonymous);
        printResult(ottenutoRisultato);
    }

    @ParameterizedTest
    @MethodSource(value = "CATEGORIE_TYPE")
    @Order(6)
    @DisplayName("6 - Legge una lista di pageid di una categoria wiki")
    //--senza specificare il type di user, in automatico mette anonymous
    //--esegue internamente tutti i cicli necessari, ognuno di 500 pagine
    //--
    //--type di user=anonymous
    //--esegue internamente tutti i cicli necessari, ognuno di 500 pagine
    //--
    //--type di user=user
    //--non essendo collegato va in errore
    //--
    //--type di user=bot
    //--non essendo collegato va in errore
    //--
    //--senza specificare il type di user, in automatico mette anonymous
    //--esegue internamente tutti i cicli necessari, ognuno di 500 pagine
    //--
    //--
    //--titolo categoria
    //--categoria esistente (per l'userType specificato)
    //--AETypeUser userType
    //--numero di pagine
    public void getLongCat(final String wikiSimpleCategoryTitle, final boolean categoriaEsistente, final AETypeUser userType, final int numPagine) {
        System.out.println("6 - Legge una lista di pageid di una categoria wiki");
        System.out.println(VUOTA);
        int size;
        inizio = System.currentTimeMillis();
        String type = userType != null ? userType.toString() : "(null)";

        ottenutoArrayLong = service.getLongCat(wikiSimpleCategoryTitle, userType);
        size = ottenutoArrayLong != null ? ottenutoArrayLong.size() : 0;
        assertEquals(categoriaEsistente, size != 0);
        System.out.println(String.format("La categoria '%s' contiene %d pageIds recuperati (come %s) in %s", wikiSimpleCategoryTitle, size, type, getTime()));
    }


    @ParameterizedTest
    @MethodSource(value = "CATEGORIE")
    @Order(7)
    @DisplayName("7 - Legge una pagina da una categoria wiki")
    //--titolo categoria
    //--categoria esistente
    //--numero di pagine
    //--risultatoEsatto
    //--offset
    public void leggeSingolaPaginaDaCategoria(final String wikiSimpleCategoryTitle, final boolean categoriaEsistente, final int numPagine, final boolean risultatoEsatto, final int offset) {
        System.out.println("7 - Legge una pagina da una categoria wiki");
        System.out.println(VUOTA);
        int size;
        String wikiTitle;

        ottenutoArray = service.getTitleCat(wikiSimpleCategoryTitle);
        size = ottenutoArray != null ? ottenutoArray.size() : 0;
        assertEquals(categoriaEsistente, size != 0);

        if (ottenutoArray == null) {
            System.out.println("Nessun risultato");
            return;
        }
        if (ottenutoArray.size() < 1) {
            System.out.println("Nessun risultato");
            return;
        }

        wikiTitle = ottenutoArray.get(offset);
        ottenutoRisultato = service.isEsisteResult(wikiTitle);
        assertNotNull(ottenutoRisultato);
        System.out.println(String.format("WikiCat: %s", wikiSimpleCategoryTitle));
        System.out.println(String.format("PageId: %s", ottenutoRisultato.getLongValue()));
        System.out.println(String.format("WikiPage: %s", wikiTitle));
        System.out.println(String.format("WikiText: %s", getMax(ottenutoRisultato.getWikiText())));
        System.out.println(String.format("WikiBio: %s", getMax(ottenutoRisultato.getWikiBio())));
    }

    //    @Test
    @Order(1)
    @DisplayName("1 - legge (come user) un template")
    public void leggePage() {
        sorgente = "Guido Rossi";
        previsto = "{{Bio";
        WrapPage wrap;

        wrap = service.leggePage(sorgente);
        assertEquals(AETypePage.testoConTmpl, wrap.getType());
        assertTrue(wrap.getTmpl().startsWith(previsto));

        System.out.println("1 - Legge il template Bio della pagina.");
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println("Legge (come user) una SINGOLA pagina dal server wiki");
        System.out.println("La pagina viene richiesta dal TITLE");
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println("Faccio vedere solo l'inizio, perché troppo lungo");
        System.out.println("Sorgente restituito in formato visibile/leggibile");

        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", cicli, getTime()));
        //        this.printWrapCat(wrap); @todo RIMETTERE
    }

    //    @Test
    @Order(2)
    @DisplayName("2 - legge una serie di wrapper di dati con una API action=query di Mediawiki")
    public void leggePages() {
        sorgente = "8956310|132555|134246|133958|8978579";
        List<WrapPage> wrapLista;
        previstoIntero = 5;

        wrapLista = service.leggePages(sorgente);
        assertNotNull(wrapLista);
        assertEquals(previstoIntero, wrapLista.size());

        System.out.println("2 - legge un wrapper di dati con una API action=query di Mediawiki");
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println("Legge (come user) una SERIE di pagine dal server wiki");
        System.out.println("Le pagine vengono richiesta dal PAGEIDs");
        System.out.println(String.format("Le pagine wiki sono: %s", sorgente));
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", previstoIntero, getTime()));

        System.out.println(VUOTA);
        System.out.println("Pagine recuperate:");
        for (WrapPage wrap : wrapLista) {
            //            this.printWrap(wrap); @todo RIMETTERE
        }
    }

    //    @Test
    @Order(3)
    @DisplayName("3 - Recupera (come user) 'lastModifica' di una serie di pageid")
    public void fixPages() {
        sorgente = "8956310|132555|134246|133958|8978579";
        List<MiniWrap> wrapLista;
        previstoIntero = 5;

        wrapLista = service.fixPages(null);
        assertNull(wrapLista);

        wrapLista = service.fixPages(VUOTA);
        assertNull(wrapLista);

        wrapLista = service.fixPages(sorgente);
        assertNotNull(wrapLista);
        assertEquals(previstoIntero, wrapLista.size());

        System.out.println("3 - Recupera (come user) 'lastModifica' di una serie di pageid");
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println("Recupera dalla urlRequest  pageid e timestamp");
        System.out.println(String.format("Le pagine wiki sono: %s", sorgente));
        System.out.println(String.format("Tempo impiegato per leggere %d pagine: %s", previstoIntero, getTime()));

        System.out.println(VUOTA);
        System.out.println("Pagine recuperate:");
        for (MiniWrap wrap : wrapLista) {
            System.out.print(wrap.getPageid());
            System.out.print(SEP);
            System.out.println(wrap.getLastModifica());
        }
    }


    //    @Test
    @Order(4)
    @DisplayName("4 - Recupera (come user) 'lastModifica' di una categoria")
    public void fixPages2() {
        List<MiniWrap> wrapLista;

        sorgente = CAT_1435;
        previstoIntero = 33;
        sorgente2 = service.getPageidsCat(sorgente);
        assertTrue(textService.isValid(sorgente2));

        wrapLista = service.fixPages(sorgente2);
        assertNotNull(wrapLista);
        assertEquals(previstoIntero, wrapLista.size());

        System.out.println("4 - Recupera (come user) 'lastModifica' di una categoria");
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println("Recupera dalla urlRequest 'pageid' e 'timestamp'");
        System.out.println(String.format("Tempo impiegato per leggere la categoria: %s", getTime()));

        System.out.println(VUOTA);
        System.out.println(String.format("La categoria è: %s", sorgente));
        System.out.println(String.format("Le pagine wiki recuperate sono: %s", wrapLista.size()));
        for (MiniWrap wrap : wrapLista) {
            System.out.print(wrap.getPageid());
            System.out.print(SEP);
            System.out.println(wrap.getLastModifica());
        }

        sorgente = CAT_1435;
        previstoIntero = 33;
        wrapLista = service.getMiniWrap(sorgente);
        assertNotNull(wrapLista);
        assertEquals(previstoIntero, wrapLista.size());

        System.out.println(VUOTA);
        System.out.println(String.format("Le pagine wiki sono: %s", wrapLista.size()));
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println(String.format("Tempo impiegato per leggere la categoria '%s' e controllare il 'timestamp' di %d pagine: %s", sorgente, previstoIntero, getTime()));

        sorgente = CAT_1591;
        previstoIntero = 67;
        wrapLista = service.getMiniWrap(sorgente);
        assertNotNull(wrapLista);
        assertEquals(previstoIntero, wrapLista.size());

        System.out.println(VUOTA);
        System.out.println(String.format("Le pagine wiki sono: %s", wrapLista.size()));
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println(String.format("Tempo impiegato per leggere la categoria '%s' e controllare il 'timestamp' di %d pagine: %s", sorgente, previstoIntero, getTime()));

        sorgente = CAT_1713;
        previstoIntero = 104;
        wrapLista = service.getMiniWrap(sorgente);
        assertNotNull(wrapLista);
        assertEquals(previstoIntero, wrapLista.size());

        System.out.println(VUOTA);
        System.out.println(String.format("Le pagine wiki sono: %s", wrapLista.size()));
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println(String.format("Tempo impiegato per leggere la categoria '%s' e controllare il 'timestamp' di %d pagine: %s", sorgente, previstoIntero, getTime()));

        sorgente = CAT_1935;
        previstoIntero = 1987;
        wrapLista = service.getMiniWrap(sorgente);
        assertNotNull(wrapLista);
        assertEquals(previstoIntero, wrapLista.size());

        System.out.println(VUOTA);
        System.out.println(String.format("Le pagine wiki sono: %s", wrapLista.size()));
        System.out.println("Usa una API con action=query SENZA bisogno di loggarsi");
        System.out.println(String.format("Tempo impiegato per leggere la categoria '%s' e controllare il 'timestamp' di %d pagine: %s", sorgente, previstoIntero, getTime()));
    }

    //    @Test
    @Order(5)
    @DisplayName("5 - ciclo di download")
    public void leggePage2() {
        WrapPage wrap = null;
        int cont = 0;
        List<String> lista = service.getTitleCat(CAT_1167);
        assertNotNull(lista);

        System.out.println(String.format("Trovate %s pagine", lista.size()));
        for (String wikiTitle : lista) {
            wrap = service.leggePage(wikiTitle);
            cont++;
            if (wrap.isValida()) {
                System.out.println(String.format("%s%s%s è ok", cont, SEP, wrap.getTitle()));
            }
            else {
                System.out.println(String.format("%s%sAlla pagina '%s' manca il tmpl Bio", cont, SEP, wrap.getTitle()));
            }
        }
    }

    //    @Test
    @Order(6)
    @DisplayName("6 - ciclo di download")
    public void leggePage3() {
        List<WrapCat> listaWrapDiControlloDelPageid = null;
        List<Long> listaPageIdsCategoria = null;
        List<MiniWrap> listaMiniWrap = null;
        List<WrapPage> listaWrapPage = null;
        List<Long> listaPageIdsDaLeggere = null;
        String stringPageIds = VUOTA;

        //--solo per controllo del titolo nel test. Normalmente non serve
        listaWrapDiControlloDelPageid = service.getWrapCat(CAT_1167);
        assertNotNull(listaWrapDiControlloDelPageid);
        System.out.println(String.format("Lista di %d WrapCat con i pageIds per controllo", listaWrapDiControlloDelPageid.size()));
        System.out.println("Solo per controllo del titolo nel test. Normalmente non serve");
        System.out.println(VUOTA);
        printWrapCat(listaWrapDiControlloDelPageid);

        //--A - Parte dalla lista di tutti i (long)pageIds della categoria
        //--nel caso reale sono circa mezzo milione
        listaPageIdsCategoria = service.getLongCat(CAT_1167);
        assertNotNull(listaPageIdsCategoria);
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println("A - Parte dalla lista di tutti i (long) pageIds della categoria");
        System.out.println("Nel caso reale sono circa mezzo milione");
        System.out.println(String.format("Lista di %d pageIds (tutte quelle della categoria)", listaPageIdsCategoria.size()));
        printLong(listaPageIdsCategoria);

        //--B - Usa tutta la lista di pageIds e si recupera una lista (stessa lunghezza) di miniWrap
        listaMiniWrap = service.getMiniWrap(CAT_1167, listaPageIdsCategoria);
        assertNotNull(listaMiniWrap);
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println("B - Usa tutta la lista di pageIds e recupera una lista (stessa lunghezza) di miniWrap");
        System.out.println(String.format("Lista di %d miniWrap corrispondente alla list di %d long (l'ordine potrebbe essere diverso)", listaMiniWrap.size(), listaPageIdsCategoria.size()));
        printMiniWrap(listaMiniWrap);

        //--C - Elabora la lista di miniWrap e costruisce una lista di pageIds da leggere
        //--Vengono usati quelli che hanno un miniWrap.pageid senza corrispondente bio.pageid nel mongoDb
        //--Vengono usati quelli che hanno miniWrap.lastModifica maggiore di bio.lastModifica
        //--questi controlli vengono saltati in questa testUnit
        //--dalla lista risultante di MiniWrap, si costruisce una lista di pageIds da leggere
        //--si costruisce una lista di WrapPage valide
        listaPageIdsDaLeggere = service.elaboraMiniWrap(listaMiniWrap);
        listaWrapPage = service.leggePages(listaPageIdsDaLeggere);
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println("C - Elabora la lista di miniWrap e costruisce una lista di pageIds da leggere");
        System.out.println("Vengono usati quelli che hanno un miniWrap.pageid senza corrispondente bio.pageid nel mongoDb");
        System.out.println("Vengono usati quelli che hanno miniWrap.lastModifica maggiore di bio.lastModifica");
        System.out.println("questi controlli vengono saltati in questa testUnit");
        System.out.println("dalla elaborazione della lista di miniWrap, risulta una lista di pageIds da leggere");
        System.out.println(String.format("Lista originaria di %d miniWrap", listaMiniWrap.size()));
        System.out.println(String.format("Lista elaborata di %d pageIds", listaPageIdsDaLeggere.size()));
        System.out.println(String.format("Lista scaricata di %d wrapPage valide (con tmplBio)", listaWrapPage.size()));
        printWrapPage(listaWrapPage);
    }

    private void printEsiste(String wikiTitle, boolean esiste) {
        String status = esiste ? "esiste" : "non esiste";
        System.out.println(VUOTA);
        System.out.println(String.format("La pagina/categoria '%s'%s%s", wikiTitle, FORWARD, status));
    }




    private void printResult(final AIResult result) {
        printResultBase(result);
        System.out.println(String.format("Value: %d", result.getIntValue()));
        System.out.println(String.format("Tempo impiegato per leggere la pagina: %s", getTime()));
    }


    private void printResult(final AIResult result, final int numPagine, final boolean risultatoEsatto) {
        printResultBase(result);
        String message = getEsatto(result.isValido(), result.getWebTitle(), numPagine, result.getIntValue(), risultatoEsatto);
        System.out.println(String.format("Value: %s", message));
        System.out.println(String.format("Tempo impiegato per leggere la pagina: %s", getTime()));
    }


    private String getEsatto(final boolean risultatoValido, final String catTitle, final int numPaginePreviste, final int numPagineOttenute, final boolean risultatoEsatto) {
        String message;

        if (risultatoValido) {
            if (risultatoEsatto) {
                if (numPagineOttenute == numPaginePreviste) {
                    message = String.format("Nella categoria '%s' ci sono esattamente le %d pagine previste", catTitle, numPagineOttenute);
                }
                else {
                    message = String.format("Nella categoria '%s' ci sono %d pagine che NON sono le %d previste", catTitle, numPagineOttenute, numPaginePreviste);
                }
            }
            else {
                if (numPagineOttenute == numPaginePreviste) {
                    message = String.format("Nella categoria '%s' ci sono le %d pagine previste", catTitle, numPagineOttenute);
                }
                else {
                    message = String.format("Nella categoria '%s' ci sono %d pagine che sono circa le %d previste", catTitle, numPagineOttenute, numPaginePreviste);
                }
            }
        }
        else {
            message = String.format("La categoria '%s' non esiste", catTitle);
        }

        return message;
    }




    private void printWrapCat(List<WrapCat> wrapLista) {
        int pos = 0;
        for (WrapCat wrap : wrapLista) {
            pos++;
            System.out.print(pos);
            System.out.print(") ");
            System.out.print(wrap.getPageid());
            System.out.print(SEP);
            System.out.println(wrap.getTitle());
        }
    }

    private void printLong(List<Long> listaPageIds) {
        int pos = 0;
        for (long lungo : listaPageIds) {
            pos++;
            System.out.print(pos);
            System.out.print(") ");
            System.out.println(lungo);
        }
    }


    private void printMiniWrap(List<MiniWrap> listaMiniWrap) {
        int pos = 0;
        for (MiniWrap wrap : listaMiniWrap) {
            pos++;
            System.out.print(pos);
            System.out.print(") ");
            System.out.print(wrap.getPageid());
            System.out.print(SEP);
            System.out.println(dateService.get(wrap.getLastModifica()));
        }
    }

    private void printWrapPage(List<WrapPage> listaWrapPage) {
        int pos = 0;
        for (WrapPage wrap : listaWrapPage) {
            pos++;
            System.out.print(pos);
            System.out.print(") ");
            System.out.print(wrap.getPageid());
            System.out.print(SEP);
            System.out.println(wrap.getTitle());
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
    @AfterAll
    void tearDownAll() {
    }

}