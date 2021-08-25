package it.algos.wiki;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import it.algos.vaadwiki.backend.service.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 21-gen-2019
 * Time: 08:39
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValidoWiki")
@DisplayName("DidascaliaService - Elaborazione delle didascalie.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DidascaliaServiceTest extends WTest {

    private static final String DATA_BASE_NAME = "vaadwiki";


    private String wikiTitle = "Adone Asinari";

    private String wikiTitleDue = "Sonia Todd";

    private static String[] PAGINE() {
        return new String[]{PAGINA_UNO, PAGINA_DUE, PAGINA_TRE, PAGINA_QUATTRO, PAGINA_CINQUE, PAGINA_SEI, PAGINA_SETTE};
    }

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private DidascaliaService service;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        service = didascaliaService;
    }


    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();

        didascalia = VUOTA;
        wrap = null;
        bio = null;
    }

    private Giorno creaGiorno(String giornoText) {
        Giorno giorno = null;
        giorno = new Giorno();
        //        giorno.titolo = giornoText;
        return giorno;
    }// end of method

    //    private Anno creaAnno(int annoInt) {
    //        Anno anno = null;
    //        anno = new Anno();
    //        anno.titolo = "" + annoInt;
    //        return anno;
    //    }// end of method

    //    private Attivita creaAttivita(String attivitaText) {
    //        Attivita attivita = null;
    //        attivita = new Attivita();
    //        attivita.singolare = attivitaText;
    //        return attivita;
    //    }// end of method

    //    private Nazionalita creaNazionalita(String nazionalitaText) {
    //        Nazionalita nazionalita = null;
    //        nazionalita = new Nazionalita();
    //        nazionalita.singolare = nazionalitaText;
    //        return nazionalita;
    //    }// end of method


    private Bio creaBio() {
        Bio entity = null;
        //        Giorno giorno = null;
        //        Anno anno = creaAnno(1963);
        //        Attivita attivita = creaAttivita("attrice");
        //        Nazionalita nazionalita = creaNazionalita("australiana");
        //
        //        entity = new Bio();
        //        entity.pageid = 29999;
        //        entity.wikiTitle = wikiTitle;
        //        entity.nome = "Sonia";
        //        entity.cognome = "Todd";
        //        entity.sesso = "F";
        //        entity.luogoNato = "Adelaide";
        //        entity.luogoNatoLink = "Adelaide (Australia)";
        //        entity.giornoNascita = giorno;
        //        entity.annoNascita = anno;
        //        entity.attivita = attivita;
        //        entity.nazionalita = nazionalita;

        return entity;
    }

    @Test
    @Order(1)
    @DisplayName("1 - Nome e cognome semplice")
    void getNomeCognome() {
        System.out.println("1 - Nome e cognome semplice");

        sorgente = "Sigurd Ribbung";
        sorgente2 = "Sigurd";
        sorgente3 = "Ribbung";
        previsto = "[[Sigurd Ribbung]]";
        ottenuto = service.getNomeCognome(sorgente, sorgente2, sorgente3);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        print(sorgente, sorgente2, sorgente3, ottenuto);

        wrap = queryBio.urlRequest(sorgente).getWrap();
        assertNotNull(wrap);
        assertTrue(wrap.isValido());
        bio = bioService.newEntity(wrap);
        bio = elaboraService.esegue(bio);
        ottenuto = service.getNomeCognome(bio);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println("Lo stesso passando da WrapBio e Bio");
        print(bio, ottenuto);
    }

    @Test
    @Order(2)
    @DisplayName("2 - Nome doppio e cognome semplice")
    void getNomeCognome2() {
        System.out.println("2 - Nome doppio e cognome semplice");

        sorgente = "Bernart Arnaut d'Armagnac";
        sorgente2 = "Bernart";
        sorgente3 = "d'Armagnac";
        previsto = "[[Bernart Arnaut d'Armagnac]]";
        ottenuto = service.getNomeCognome(sorgente, sorgente2, sorgente3);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        print(sorgente, sorgente2, sorgente3, ottenuto);
    }

    @Test
    @Order(3)
    @DisplayName("3 - Nome doppio e cognome semplice")
    void getNomeCognome3() {
        System.out.println("3 - Nome doppio e cognome semplice");

        sorgente = "Francesco Maria Pignatelli";
        sorgente2 = "Francesco Maria";
        sorgente3 = "Pignatelli";
        previsto = "[[Francesco Maria Pignatelli]]";
        ottenuto = service.getNomeCognome(sorgente, sorgente2, sorgente3);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        print(sorgente, sorgente2, sorgente3, ottenuto);
    }

    @Test
    @Order(4)
    @DisplayName("4 - Titolo disambiguato")
    void getNomeCognome4() {
        System.out.println("4 - Titolo disambiguato");

        sorgente = "Colin Campbell (generale)";
        sorgente2 = "Colin";
        sorgente3 = "Campbell";
        previsto = "[[Colin Campbell (generale)|Colin Campbell]]";
        ottenuto = service.getNomeCognome(sorgente, sorgente2, sorgente3);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        print(sorgente, sorgente2, sorgente3, ottenuto);
    }

    @ParameterizedTest
    @MethodSource(value = "PAGINE")
    @Order(5)
    @DisplayName("5 - Didascalie varie")
    void testWithStringParameter(String wikiTitle) {
        System.out.println("5 - Didascalie varie");

        sorgente = wikiTitle;
        wrap = queryBio.urlRequest(sorgente).getWrap();
        assertNotNull(wrap);
        assertTrue(wrap.isValido());
        bio = bioService.newEntity(wrap);
        bio = elaboraService.esegue(bio);
        previsto = textService.setDoppieQuadre(bio.wikiTitle);
        ottenuto = service.getNomeCognome(bio);
        ottenuto2 = service.getAttivitaNazionalita(bio);
        assertTrue(textService.isValid(ottenuto));
        System.out.println(VUOTA);
        print(bio, ottenuto, ottenuto2);
    }

    //    @Test
    public void download() {
        System.out.println("*************");
        System.out.println("Tipi possibili di didascalie per " + wikiTitle);
        System.out.println("Senza chiave");
        System.out.println("*************");
        //        for (EADidascalia dida : EADidascalia.values()) {
        //            ottenuto = didascalia.esegue(bio, dida, false);
        //            System.out.println(dida.name() + ": " + ottenuto);
        //        }// end of for cycle
        System.out.println("*************");
        System.out.println("Con chiave");
        System.out.println("*************");
        //        for (EADidascalia dida : EADidascalia.values()) {
        //            //            ottenuto = didascalia.esegue(bio, dida);
        //            System.out.println(dida.name() + ": " + ottenuto);
        //        }// end of for cycle
        System.out.println("*************");

    }// end of single test


    /**
     * Test con uscita sul terminale di Idea
     */
    //    @Test
    public void esegueTestDidascalie() {
        System.out.println("");
        System.out.println("Algos");
        System.out.println("");
        System.out.println("Tipi possibili di discalie");
        System.out.println("Esempio '" + wikiTitle + "'");
        System.out.println("");
        Bio bio = creaBio();
        //        for (EADidascalia type : EADidascalia.values()) {
        //            ottenuto = didascaliaService.getBaseSenza(bio, type);
        //            if (text.isValid(ottenuto)) {
        //                System.out.println(type.name() + ": " + ottenuto);
        //            }
        //            else {
        //                System.out.println(type.name() + ": Manca");
        //            }// end of if/else cycle
        //        }// end of for cycle
        System.out.println("");
    }// end of single test

    //    /**
    //     * Pagina completa con uscita su pagina utente
    //     */
    //    @Test
    //    public void esegueTestUplod() {
    //        didascaliaService.esegue();
    //    }// end of single test


}// end of class
