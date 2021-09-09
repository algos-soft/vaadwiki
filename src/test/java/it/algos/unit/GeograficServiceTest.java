package it.algos.unit;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.wiki.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: lun, 12-lug-2021
 * Time: 13:31
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("GeograficServiceTest")
@DisplayName("GeograficService - Entity geografiche")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GeograficServiceTest extends ATest {


    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private GeograficService service;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        service = geograficService;
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
    @DisplayName("1 - legge (come user) un template")
    public void getTemplateBandierina() {
        sorgente = "ES-AN";
        treStringhe = service.getTemplateBandierina(sorgente);
        assertNotNull(treStringhe);
        System.out.println(String.format("Il template wiki è: %s", sorgente));
        System.out.println(treStringhe.getPrima() + FORWARD + treStringhe.getSeconda() + SEP + treStringhe.getTerza());

        sorgente = "{{ES-CB}}";
        treStringhe = service.getTemplateBandierina(sorgente);
        assertNotNull(treStringhe);
        System.out.println(VUOTA);
        System.out.println(String.format("Il template wiki è: %s", sorgente));
        System.out.println(treStringhe.getPrima() + FORWARD + treStringhe.getSeconda() + SEP + treStringhe.getTerza());

        sorgente = "Template:ES-AN";
        treStringhe = service.getTemplateBandierina(sorgente);
        assertNotNull(treStringhe);
        System.out.println(VUOTA);
        System.out.println(String.format("Il template wiki è: %s", sorgente));
        System.out.println(treStringhe.getPrima() + FORWARD + treStringhe.getSeconda() + SEP + treStringhe.getTerza());
    }


    @Test
    @Order(2)
    @DisplayName("2 - legge (come user) una lista di template")
    public void getTemplateList() {
        sorgente = "ISO_3166-2:ES";
        sorgenteIntero = 1;
        previstoIntero = 17;
        listaStr = wikiApiService.getColonna(sorgente, sorgenteIntero, 2, 2);
        assertNotNull(listaStr);
        assertEquals(previstoIntero, listaStr.size());
        listaWrapTre = service.getTemplateList(listaStr);
        assertNotNull(listaWrapTre);
        assertEquals(previstoIntero, listaWrapTre.size());
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println(String.format("Template Spagna, Comunità Autonome: %d righe", listaWrapTre.size()));
        System.out.println(String.format("Tempo impiegato per leggere la pagina base e le %d pagine correlate: %s", listaWrapTre.size(), getTime()));
        printWrapTre(listaWrapTre);
    }


    @Test
    @Order(3)
    @DisplayName("3 - legge (come user) una lista di template")
    public void getTemplateList2() {
        sorgente = "ISO_3166-2:ES";
        sorgenteIntero = 2;
        previstoIntero = 50;
        listaStr = wikiApiService.getColonna(sorgente, sorgenteIntero, 2, 2);
        assertNotNull(listaStr);
        assertEquals(previstoIntero, listaStr.size());

        previstoIntero = 43;
        listaWrapTre = service.getTemplateList(listaStr);
        assertNotNull(listaWrapTre);
        assertEquals(previstoIntero, listaWrapTre.size());
        System.out.println(String.format("La pagina wiki è: %s", sorgente));
        System.out.println(String.format("Template Spagna, Province: %d righe", listaWrapTre.size()));
        System.out.println(String.format("Tempo impiegato per leggere la pagina base e le %d pagine correlate: %s", listaWrapTre.size(), getTime()));
        printWrapTre(listaWrapTre);
    }

    //    //    @Test
    //    @Order(8)
    //    @DisplayName("8 - legge le righe delle regioni italiane")
    //    public void getTableRegioni() {
    //        sorgente = "ISO 3166-2:IT";
    //
    //        //--regioni
    //        previstoIntero = 20;
    //        previsto = "<code>IT-65</code>";
    //        previsto2 = "{{bandiera|Abruzzo|nome}}";
    //        try {
    //            listaGrezza = service.getTable(sorgente);
    //        } catch (Exception unErrore) {
    //        }
    //        assertNotNull(listaGrezza);
    //        assertEquals(previstoIntero, listaGrezza.size());
    //        assertEquals(previsto, listaGrezza.get(0).get(0));
    //        assertEquals(previsto2, listaGrezza.get(0).get(1));
    //        System.out.println("Legge le righe di una tabella wiki");
    //        System.out.println(VUOTA);
    //        System.out.println("8 - Regioni: " + listaGrezza.size());
    //        System.out.println("*******");
    //        printList(listaGrezza);
    //    }


    //        @Test
    @Order(9)
    @DisplayName("9 - legge le righe delle province")
    public void getTableProvince() {
        sorgente = "ISO 3166-2:IT";

        //--province
        previstoIntero = 14;
        listaWrapTre = service.getTemplateList(sorgente, 2, 3, 1, 3);
        assertNotNull(listaWrapTre);
        assertEquals(previstoIntero, listaWrapTre.size());
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println("9 - Province: " + listaWrapTre.size());
        printWrapTre(listaWrapTre);
    }

    //    @Test
    //    @Order(10)
    //    @DisplayName("10 - legge le regioni della Francia")
    //    public void getTableFrancia() {
    //        sorgente = "ISO_3166-2:FR";
    //        previstoIntero = 13;
    //        listaWrap = geografic.getTemplateList(sorgente, 1, 2, 2);
    //        assertNotNull(listaWrap);
    //        assertEquals(previstoIntero, listaWrap.size());
    //        System.out.println(VUOTA);
    //        System.out.println("10 - Francia: " + listaWrap.size());
    //        printWrap(listaWrap);
    //
    //        previstoIntero = 3;
    //        listaWrap = service.getDueColonne(sorgente, 3, 2, 2, 4);
    //        assertNotNull(listaWrap);
    //        assertEquals(previstoIntero, listaWrap.size());
    //        System.out.println(VUOTA);
    //        System.out.println("10 - Francia: " + listaWrap.size());
    //        printWrap(listaWrap);
    //
    //        previstoIntero = 9;
    //        listaWrap = service.getDueColonne(sorgente, 4, 2, 1, 3);
    //        assertNotNull(listaWrap);
    //        assertEquals(previstoIntero, listaWrap.size());
    //        System.out.println(VUOTA);
    //        System.out.println("10 - Francia: " + listaWrap.size());
    //        printWrap(listaWrap);
    //    }


    //    @Test
    @Order(11)
    @DisplayName("11 - legge i cantoni della Svizzera")
    public void getTableSvizzera() {
        sorgente = "ISO_3166-2:CH";
        previstoIntero = 26;
        listaWrapTre = service.getTemplateList(sorgente, 1, 2, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("11 - Svizzera: " + listaWrap.size());
        printWrapTre(listaWrapTre);
    }


    //    @Test
    @Order(12)
    @DisplayName("12 - legge i lander della Austria")
    public void getTableAustria() {
        sorgente = "ISO_3166-2:AT";
        previstoIntero = 9;
        listaWrapTre = service.getTemplateList(sorgente, 1, 2, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("12 - Austria: " + listaWrap.size());
        printWrapTre(listaWrapTre);
    }


    //    @Test
    @Order(13)
    @DisplayName("13 - legge i lander della Germania")
    public void getTableGermania() {
        sorgente = "ISO_3166-2:DE";
        previstoIntero = 16;
        listaWrapTre = service.getTemplateList(sorgente, 1, 2, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("13 - Germania: " + listaWrap.size());
        printWrapTre(listaWrapTre);
    }


    //    @Test
    @Order(14)
    @DisplayName("14 - legge le comunità della Spagna")
    public void getTableSpagna() {
        sorgente = "ISO_3166-2:ES";
        previstoIntero = 17;
        listaWrapTre = service.getTemplateList(sorgente, 1, 2, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("14 - Spagna: " + listaWrap.size());
        printWrapTre(listaWrapTre);
    }


    //    @Test
    @Order(15)
    @DisplayName("15 - legge i distretti del Portogallo")
    public void getTablePortogallo() {
        sorgente = "ISO_3166-2:PT";
        previstoIntero = 18;
        listaWrap = wikiApiService.getDueColonne(sorgente, 1, 2, 2, 3);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("15 - Portogallo: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //        @Test
    @Order(16)
    @DisplayName("16 - legge i comuni della Slovenia")
    public void getTableSlovenia() {
        sorgente = "ISO_3166-2:SI";
        previstoIntero = 211;
        listaWrap = wikiApiService.getDueColonne(sorgente, 1, 2, 1, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("16 - Slovenia: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(17)
    @DisplayName("17 - legge i comuni del Belgio")
    public void getTableBelgio() {
        sorgente = "ISO_3166-2:BE";
        previstoIntero = 3;
        listaWrapTre = service.getTemplateList(sorgente, 1, 2, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("17 - Belgio: " + listaWrap.size());
        printWrapTre(listaWrapTre);
    }


    //    @Test
    @Order(18)
    @DisplayName("18 - legge le province dell'Olanda")
    public void getTableOlanda() {
        sorgente = "ISO_3166-2:NL";
        previstoIntero = 12;
        listaWrapTre = service.getTemplateList(sorgente, 1, 2, 3);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("18 - Olanda: " + listaWrap.size());
        printWrapTre(listaWrapTre);
    }


    //    @Test
    @Order(19)
    @DisplayName("19 - legge le province della Croazia")
    public void getTableCroazia() {
        sorgente = "ISO_3166-2:HR";
        previstoIntero = 21;
        listaWrap = wikiApiService.getDueColonne(sorgente, 1, 2, 1, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("19 - Croazia: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(20)
    @DisplayName("20 - legge i distretti della Albania")
    public void getTableAlbania() {
        sorgente = "ISO_3166-2:AL";
        previstoIntero = 36;
        listaWrap = wikiApiService.getDueColonne(sorgente, 1, 1, 1, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("20 - Albania: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(21)
    @DisplayName("21 - legge i distretti della Grecia")
    public void getTableGrecia() {
        sorgente = "ISO_3166-2:GR";

        //--periferie
        previstoIntero = 13;
        listaWrap = wikiApiService.getDueColonne(sorgente, 1, 2, 1, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("21 - Grecia: " + listaWrap.size());
        printWrap(listaWrap);

        //--prefetture
        previstoIntero = 52;
        listaWrap = wikiApiService.getDueColonne(sorgente, 2, 2, 2, 3);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("21 - Grecia: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(22)
    @DisplayName("22 - legge le regioni della Cechia")
    public void getTableCechia() {
        sorgente = "ISO_3166-2:CZ";
        previstoIntero = 14;
        listaWrapTre = service.getTemplateList(sorgente, 1, 2, 3);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("22 - Cechia: " + listaWrap.size());
        printWrapTre(listaWrapTre);
    }


    //    @Test
    @Order(23)
    @DisplayName("23 - legge le regioni della Slovacchia")
    public void getTableSlovacchia() {
        sorgente = "ISO_3166-2:SK";
        previstoIntero = 8;
        listaWrap = wikiApiService.getDueColonne(sorgente, 1, 2, 1, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("23 - Slovacchia: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(24)
    @DisplayName("24 - legge le province della Ungheria")
    public void getTableUngheria() {
        sorgente = "ISO_3166-2:HU";
        previstoIntero = 19;
        listaWrapTre = service.getTemplateList(sorgente, 1, 2, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("24 - Ungheria: " + listaWrap.size());
        printWrapTre(listaWrapTre);
    }


    //    @Test
    @Order(25)
    @DisplayName("25 - legge i distretti della Romania")
    public void getTableRomania() {
        sorgente = "ISO_3166-2:RO";

        //--distretti
        previstoIntero = 41;
        listaWrap = wikiApiService.getDueColonne(sorgente, 1, 2, 2, 3);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("25 - Romania: " + listaWrap.size());
        printWrap(listaWrap);

        //--capitale
        previstoIntero = 1;
        listaWrap = wikiApiService.getDueColonne(sorgente, 2, 2, 2, 3);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("25 - Romania: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(26)
    @DisplayName("26 - legge i distretti della Bulgaria")
    public void getTableBulgaria() {
        sorgente = "ISO_3166-2:BG";
        previstoIntero = 28;
        listaWrap = wikiApiService.getDueColonne(sorgente, 1, 2, 2, 3);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("26 - Bulgaria: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(27)
    @DisplayName("27 - legge i voivodati della Polonia")
    public void getTablePolonia() {
        sorgente = "ISO_3166-2:PL";
        previstoIntero = 16;
        listaWrap = wikiApiService.getDueColonne(sorgente, 1, 2, 2, 3);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("27 - Polonia: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(28)
    @DisplayName("28 - legge le regioni della Danimarca")
    public void getTableDanimarca() {
        sorgente = "ISO_3166-2:DK";
        previstoIntero = 5;
        listaWrap = wikiApiService.getDueColonne(sorgente, 1, 2, 1, 2);
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("28 - Danimarca: " + listaWrap.size());
        printWrap(listaWrap);
    }


    //    @Test
    @Order(29)
    @DisplayName("29 - legge i distretti di Finlandia")
    public void getTableFinlandia() {
        sorgente = "ISO_3166-2:FI";
        previstoIntero = 19 + 1;
        try {
            listaWrap = service.getRegioni(sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("29 - Finlandia: " + (listaWrap.size() - 1) + " + titolo");
        printWrap(listaWrap);
    }

    //    @Test
    @Order(30)
    @DisplayName("30 - legge i distretti di Azerbaigian")
    public void getTableAzerbaigian() {
        sorgente = "ISO_3166-2:AZ";
        previstoIntero = 77 + 1;
        try {
            listaWrap = service.getRegioni(sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("30 - Azerbaigian: " + (listaWrap.size() - 1) + " + titolo");
        printWrap(listaWrap);
    }

    //    @Test
    @Order(31)
    @DisplayName("31 - legge i distretti di Belize")
    public void getTableBelize() {
        sorgente = "ISO_3166-2:BZ";
        previstoIntero = 6 + 1;
        try {
            listaWrap = service.getRegioni(sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("31 - Belize: " + (listaWrap.size() - 1) + " + titolo");
        printWrap(listaWrap);
    }

    //    @Test
    @Order(32)
    @DisplayName("32 - legge i distretti di Guatemala")
    public void getTableGuatemala() {
        sorgente = "ISO_3166-2:GT";
        previstoIntero = 22 + 1;
        try {
            listaWrap = service.getRegioni(sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("32 - Guatemala: " + (listaWrap.size() - 1) + " + titolo");
        printWrap(listaWrap);
    }

    //    @Test
    @Order(33)
    @DisplayName("33 - legge i distretti di Guinea Bissau")
    public void getTableGuinea() {
        sorgente = "ISO_3166-2:GW";
        previstoIntero = 9 + 1;
        try {
            listaWrap = service.getRegioni(sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("33 - Guinea Bissau: " + (listaWrap.size() - 1) + " + titolo");
        printWrap(listaWrap);
    }

    //    @Test
    @Order(34)
    @DisplayName("34 - legge i distretti di Slovenia")
    public void getTableSlovenia2() {
        sorgente = "ISO_3166-2:SI";
        previstoIntero = 211 + 1;
        try {
            listaWrap = service.getRegioni(sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("33 - Guinea Bissau: " + (listaWrap.size() - 1) + " + titolo");
        printWrap(listaWrap);
    }

    //    @Test
    @Order(35)
    @DisplayName("35 - legge i distretti di Kirghizistan")
    public void getTableKirghizistan() {
        sorgente = "ISO_3166-2:KG";
        previstoIntero = 8 + 1;
        try {
            listaWrap = service.getRegioni(sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(listaWrap);
        assertEquals(previstoIntero, listaWrap.size());
        System.out.println(VUOTA);
        System.out.println("33 - Guinea Bissau: " + (listaWrap.size() - 1) + " + titolo");
        printWrap(listaWrap);
    }

    //    @Test
    @Order(40)
    @DisplayName("40 - legge le regioni dei primi 50 stati")
    public void readStati1() {
        List<List<String>> listaStati = service.getStati();
        assertNotNull(listaStati);
        listaStati = listaStati.subList(0, 50);
        readStati(listaStati);
    }

    //    @Test
    @Order(41)
    @DisplayName("41 - legge le regioni degli stati 50-100")
    public void readStati2() {
        List<List<String>> listaStati = service.getStati();
        assertNotNull(listaStati);
        listaStati = listaStati.subList(50, 100);
        readStati(listaStati);
    }

    //    @Test
    @Order(42)
    @DisplayName("42 - legge le regioni degli stati 100-150")
    public void readStati3() {
        List<List<String>> listaStati = service.getStati();
        assertNotNull(listaStati);
        listaStati = listaStati.subList(100, 150);
        readStati(listaStati);
    }

    //    @Test
    @Order(43)
    @DisplayName("43 - legge le regioni degli stati 150-200")
    public void readStati4() {
        List<List<String>> listaStati = service.getStati();
        assertNotNull(listaStati);
        listaStati = listaStati.subList(150, 200);
        readStati(listaStati);
    }

    //    @Test
    @Order(44)
    @DisplayName("44 - legge le regioni degli stati 200-250")
    public void readStati5() {
        List<List<String>> listaStati = service.getStati();
        assertNotNull(listaStati);
        listaStati = listaStati.subList(200, listaStati.size() - 1);
        readStati(listaStati);
    }

    private void readStati(List<List<String>> listaStati) {
        String nome;
        String tag = "ISO 3166-2:";
        List<String> valide = new ArrayList<>();
        List<String> errate = new ArrayList<>();

        System.out.println("Legge le regioni di " + listaStati.size() + " stati (" + AWikiApiService.PAGINA_ISO_1 + ")");
        System.out.println(VUOTA);
        System.out.println("Valide                    Errate");
        System.out.println("********************************");
        for (List<String> lista : listaStati) {
            nome = lista.get(0);
            sorgente = tag + lista.get(3);
            try {
                listaWrap = service.getRegioni(sorgente);
                if (listaWrap != null && listaWrap.size() > 0) {
                    valide.add(nome);
                    System.out.println(nome);
                }
            } catch (Exception unErrore) {
                errate.add(nome);
                System.out.println("                          " + nome);
            }
        }
        System.out.println(VUOTA);
        System.out.println("Stati con regioni valide: " + valide.size());
        System.out.println("Stati con regioni errate: " + errate.size());
    }

    //    @Test
    @Order(45)
    @DisplayName("45 - legge le province italiane")
    public void getTableProvinceItaliane() {
        previstoIntero = 107;

        listaWrapQuattro = service.getProvince();

        assertNotNull(listaWrapQuattro);
        assertEquals(previstoIntero, listaWrapQuattro.size());
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println(VUOTA);
        System.out.println("45 - Province: " + listaWrapQuattro.size());
        printWrapQuattro(listaWrapQuattro);
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
    @AfterAll
    void tearDownAll() {
    }

}