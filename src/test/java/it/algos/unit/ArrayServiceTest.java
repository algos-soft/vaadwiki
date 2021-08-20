package it.algos.unit;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.html.*;
import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.packages.anagrafica.via.*;
import it.algos.vaadflow14.backend.service.*;
import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;


/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: mer, 29-apr-2020
 * Time: 14:46
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("ArrayService - Gestione degli array.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ArrayServiceTest extends ATest {

    /**
     * The constant ARRAY_STRING.
     */
    protected static final String[] ARRAY_STRING = {"primo", "secondo", "quarto", "quinto", "1Ad", "terzo", "a10"};

    /**
     * The constant LIST_STRING.
     */
    protected final static List<String> LIST_STRING = new ArrayList(Arrays.asList(ARRAY_STRING));


    /**
     * The constant ARRAY_OBJECT.
     */
    protected static final Object[] ARRAY_OBJECT = {new Label("Alfa"), new Button()};

    /**
     * The constant LIST_OBJECT.
     */
    protected static final List<Object> LIST_OBJECT = new ArrayList(Arrays.asList(ARRAY_OBJECT));

    /**
     * The constant ARRAY_LONG.
     */
    protected static final Long[] ARRAY_LONG = {234L, 85L, 151099L, 123500L, 3L, 456772L};

    /**
     * The constant LIST_LONG.
     */
    protected static final ArrayList<Long> LIST_LONG = new ArrayList(Arrays.asList(ARRAY_LONG));

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private ArrayService service;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        service = arrayService;
    }


    /**
     * Qui passa a ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();
    }


    @Test
    @Order(1)
    @DisplayName("1 - isAllValid (since Java 11) array")
    void isAllValid() {
        listaStr = new ArrayList<>();

        ottenutoBooleano = service.isAllValid((List) null);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = service.isAllValid((Map) null);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = service.isAllValid((new ArrayList()));
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = service.isAllValid((listaStr));
        assertFalse(ottenutoBooleano);

        listaStr.add(null);
        ottenutoBooleano = service.isAllValid(listaStr);
        assertFalse(ottenutoBooleano);

        listaStr = new ArrayList<>();
        listaStr.add(VUOTA);
        ottenutoBooleano = service.isAllValid(listaStr);
        assertFalse(ottenutoBooleano);

        listaStr = new ArrayList<>();
        listaStr.add(PIENA);
        ottenutoBooleano = service.isAllValid(listaStr);
        assertTrue(ottenutoBooleano);

        listaStr.add("Mario");
        ottenutoBooleano = service.isAllValid(listaStr);
        assertTrue(ottenutoBooleano);

        listaStr.add(null);
        ottenutoBooleano = service.isAllValid(listaStr);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = service.isAllValid(LIST_STRING);
        assertTrue(ottenutoBooleano);

        ottenutoBooleano = service.isAllValid(LIST_OBJECT);
        assertTrue(ottenutoBooleano);

        ottenutoBooleano = service.isAllValid(LIST_LONG);
        assertTrue(ottenutoBooleano);
    }


    @Test
    @Order(2)
    @DisplayName("2 - isAllValid (since Java 11) matrice")
    void isAllValid2() {
        sorgenteMatrice = null;
        ottenutoBooleano = service.isAllValid((String[]) null);
        assertFalse(ottenutoBooleano);

        sorgenteMatrice = null;
        ottenutoBooleano = service.isAllValid(sorgenteMatrice);
        assertFalse(ottenutoBooleano);

        sorgenteMatrice = new String[]{"Codice", "Regioni"};
        ottenutoBooleano = service.isAllValid(sorgenteMatrice);
        assertTrue(ottenutoBooleano);

        sorgenteMatrice = new String[]{VUOTA, "Regioni"};
        ottenutoBooleano = service.isAllValid(sorgenteMatrice);
        assertFalse(ottenutoBooleano);

        sorgenteMatrice = new String[]{VUOTA};
        ottenutoBooleano = service.isAllValid(sorgenteMatrice);
        assertFalse(ottenutoBooleano);

        sorgenteMatrice = new String[]{"Mario", VUOTA, "Regioni"};
        ottenutoBooleano = service.isAllValid(sorgenteMatrice);
        assertFalse(ottenutoBooleano);

        sorgenteMatrice = new String[]{VUOTA, VUOTA, VUOTA};
        ottenutoBooleano = service.isAllValid(sorgenteMatrice);
        assertFalse(ottenutoBooleano);
    }


    @Test
    @Order(3)
    @DisplayName("3 - isAllValid (since Java 11) map")
    void isAllValid3() {
        mappaSorgente = null;
        ottenutoBooleano = service.isAllValid((LinkedHashMap) null);
        assertFalse(ottenutoBooleano);

        mappaSorgente = null;
        ottenutoBooleano = service.isAllValid(mappaSorgente);
        assertFalse(ottenutoBooleano);

        mappaSorgente = new HashMap();
        ottenutoBooleano = service.isAllValid(mappaSorgente);
        assertFalse(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        ottenutoBooleano = service.isAllValid(mappaSorgente);
        assertFalse(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        mappaSorgente.put("beta", "irrilevante");
        mappaSorgente.put(null, "irrilevante2");
        mappaSorgente.put("delta", "irrilevante3");
        ottenutoBooleano = service.isAllValid(mappaSorgente);
        assertFalse(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        mappaSorgente.put("beta", "irrilevante");
        mappaSorgente.put(VUOTA, "irrilevante2");
        mappaSorgente.put("delta", "irrilevante3");
        ottenutoBooleano = service.isAllValid(mappaSorgente);
        assertFalse(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        mappaSorgente.put("beta", "irrilevante");
        mappaSorgente.put("alfa", null);
        mappaSorgente.put("delta", "irrilevante3");
        ottenutoBooleano = service.isAllValid(mappaSorgente);
        assertTrue(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        mappaSorgente.put("beta", "irrilevante");
        mappaSorgente.put("alfa", VUOTA);
        mappaSorgente.put("delta", "irrilevante3");
        ottenutoBooleano = service.isAllValid(mappaSorgente);
        assertTrue(ottenutoBooleano);
    }

    @Test
    @Order(4)
    @DisplayName("4 - isEmpty (since Java 11) array")
    void isEmpty() {
        listaStr = new ArrayList<>();

        ottenutoBooleano = service.isEmpty((List) null);
        assertTrue(ottenutoBooleano);

        ottenutoBooleano = service.isEmpty((Map) null);
        assertTrue(ottenutoBooleano);

        ottenutoBooleano = service.isEmpty((new ArrayList()));
        assertTrue(ottenutoBooleano);

        ottenutoBooleano = service.isEmpty((listaStr));
        assertTrue(ottenutoBooleano);

        listaStr.add(null);
        ottenutoBooleano = service.isEmpty(listaStr);
        assertTrue(ottenutoBooleano);

        listaStr = new ArrayList<>();
        listaStr.add(VUOTA);
        ottenutoBooleano = service.isEmpty(listaStr);
        assertTrue(ottenutoBooleano);

        listaStr = new ArrayList<>();
        listaStr.add(PIENA);
        ottenutoBooleano = service.isEmpty(listaStr);
        assertFalse(ottenutoBooleano);

        listaStr.add("Mario");
        ottenutoBooleano = service.isEmpty(listaStr);
        assertFalse(ottenutoBooleano);

        listaStr.add(null);
        ottenutoBooleano = service.isEmpty(listaStr);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = service.isEmpty(LIST_STRING);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = service.isEmpty(LIST_OBJECT);
        assertFalse(ottenutoBooleano);

        ottenutoBooleano = service.isEmpty(LIST_LONG);
        assertFalse(ottenutoBooleano);
    }


    @Test
    @Order(5)
    @DisplayName("5 - isEmpty (since Java 11) matrice")
    void isEmpty2() {
        sorgenteMatrice = null;
        ottenutoBooleano = service.isEmpty((String[]) null);
        assertTrue(ottenutoBooleano);

        sorgenteMatrice = null;
        ottenutoBooleano = service.isEmpty(sorgenteMatrice);
        assertTrue(ottenutoBooleano);

        sorgenteMatrice = new String[]{"Codice", "Regioni"};
        ottenutoBooleano = service.isEmpty(sorgenteMatrice);
        assertFalse(ottenutoBooleano);

        sorgenteMatrice = new String[]{VUOTA, "Regioni"};
        ottenutoBooleano = service.isEmpty(sorgenteMatrice);
        assertFalse(ottenutoBooleano);

        sorgenteMatrice = new String[]{VUOTA};
        ottenutoBooleano = service.isEmpty(sorgenteMatrice);
        assertTrue(ottenutoBooleano);

        sorgenteMatrice = new String[]{"Mario", VUOTA, "Regioni"};
        ottenutoBooleano = service.isEmpty(sorgenteMatrice);
        assertFalse(ottenutoBooleano);

        sorgenteMatrice = new String[]{VUOTA, VUOTA, VUOTA};
        ottenutoBooleano = service.isEmpty(sorgenteMatrice);
        assertTrue(ottenutoBooleano);
    }


    @Test
    @Order(6)
    @DisplayName("6 - isEmpty (since Java 11) map")
    void isEmpty3() {
        mappaSorgente = null;
        ottenutoBooleano = service.isEmpty((LinkedHashMap) null);
        assertTrue(ottenutoBooleano);

        mappaSorgente = null;
        ottenutoBooleano = service.isEmpty(mappaSorgente);
        assertTrue(ottenutoBooleano);

        mappaSorgente = new HashMap();
        ottenutoBooleano = service.isEmpty(mappaSorgente);
        assertTrue(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        ottenutoBooleano = service.isEmpty(mappaSorgente);
        assertTrue(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        mappaSorgente.put("", "irrilevante");
        mappaSorgente.put(null, "irrilevante2");
        ottenutoBooleano = service.isEmpty(mappaSorgente);
        assertTrue(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        mappaSorgente.put("beta", "irrilevante");
        mappaSorgente.put(VUOTA, "irrilevante2");
        mappaSorgente.put("delta", "irrilevante3");
        ottenutoBooleano = service.isEmpty(mappaSorgente);
        assertFalse(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        mappaSorgente.put("beta", "irrilevante");
        mappaSorgente.put(null, "irrilevante2");
        mappaSorgente.put("delta", "irrilevante3");
        ottenutoBooleano = service.isEmpty(mappaSorgente);
        assertFalse(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        mappaSorgente.put("", "irrilevante");
        mappaSorgente.put(null, "irrilevante2");
        mappaSorgente.put("delta", "irrilevante3");
        ottenutoBooleano = service.isEmpty(mappaSorgente);
        assertFalse(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        mappaSorgente.put("beta", "irrilevante");
        mappaSorgente.put("alfa", null);
        mappaSorgente.put("delta", "irrilevante3");
        ottenutoBooleano = service.isEmpty(mappaSorgente);
        assertFalse(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        mappaSorgente.put("beta", "irrilevante");
        mappaSorgente.put("alfa", VUOTA);
        mappaSorgente.put("delta", "irrilevante3");
        ottenutoBooleano = service.isEmpty(mappaSorgente);
        assertFalse(ottenutoBooleano);

        mappaSorgente = new LinkedHashMap();
        mappaSorgente.put("beta", null);
        mappaSorgente.put("alfa", VUOTA);
        ottenutoBooleano = service.isEmpty(mappaSorgente);
        assertFalse(ottenutoBooleano);
    }


    @Test
    @Order(7)
    @DisplayName("7 - isMappaSemplificabile (since Java 11)")
    void isMappaSemplificabile() {
        ottenutoBooleano = service.isMappaSemplificabile(null);
        Assertions.assertFalse(ottenutoBooleano);

        Map<String, List<String>> multiParametersMap = null;
        ottenutoBooleano = service.isMappaSemplificabile(multiParametersMap);
        Assertions.assertFalse(ottenutoBooleano);

        multiParametersMap = new HashMap<>();
        ottenutoBooleano = service.isMappaSemplificabile(multiParametersMap);
        Assertions.assertFalse(ottenutoBooleano);

        multiParametersMap = new HashMap<>();
        multiParametersMap.put("uno", LIST_STRING);
        multiParametersMap.put("due", LIST_SHORT_STRING);
        ottenutoBooleano = service.isMappaSemplificabile(multiParametersMap);
        Assertions.assertFalse(ottenutoBooleano);

        multiParametersMap = new HashMap<>();
        multiParametersMap.put("", LIST_SHORT_STRING);
        multiParametersMap.put("due", LIST_SHORT_STRING);
        ottenutoBooleano = service.isMappaSemplificabile(multiParametersMap);
        assertTrue(ottenutoBooleano);

        multiParametersMap = new HashMap<>();
        multiParametersMap.put("uno", LIST_SHORT_STRING);
        multiParametersMap.put("due", LIST_SHORT_STRING);
        ottenutoBooleano = service.isMappaSemplificabile(multiParametersMap);
        assertTrue(ottenutoBooleano);
    }


    @Test
    @Order(8)
    @DisplayName("8 - semplificaMappa (since Java 11)")
    void semplificaMappa() {
        Map<String, List<String>> multiParametersMap = null;

        mappaOttenuta = service.semplificaMappa(null);
        Assertions.assertNull(mappaOttenuta);

        mappaOttenuta = service.semplificaMappa(multiParametersMap);
        Assertions.assertNull(mappaOttenuta);

        multiParametersMap = new HashMap<>();
        mappaOttenuta = service.semplificaMappa(multiParametersMap);
        Assertions.assertNull(mappaOttenuta);

        multiParametersMap = new HashMap<>();
        multiParametersMap.put("uno", LIST_STRING);
        multiParametersMap.put("due", LIST_SHORT_STRING);
        mappaOttenuta = service.semplificaMappa(multiParametersMap);
        Assertions.assertNull(mappaOttenuta);

        multiParametersMap = new HashMap<>();
        multiParametersMap.put("uno", LIST_SHORT_STRING);
        multiParametersMap.put("due", LIST_SHORT_STRING_DUE);
        mappaPrevista = new HashMap<>();
        mappaPrevista.put("uno", CONTENUTO);
        mappaPrevista.put("due", CONTENUTO_DUE);
        mappaOttenuta = service.semplificaMappa(multiParametersMap);
        Assertions.assertNotNull(mappaOttenuta);
        Assertions.assertEquals(mappaPrevista, mappaOttenuta);
    }


    @Test
    @Order(9)
    @DisplayName("9 - creaArraySingolo (since Java 11)")
    void creaArraySingolo() {
        sorgente = "valore";
        previstoArray = new ArrayList<>();

        ottenutoArray = service.creaArraySingolo(null);
        Assertions.assertNotNull(ottenutoArray);
        Assertions.assertEquals(previstoArray, ottenutoArray);

        previstoArray.add(sorgente);
        ottenutoArray = service.creaArraySingolo(sorgente);
        Assertions.assertNotNull(ottenutoArray);
        Assertions.assertEquals(previstoArray, ottenutoArray);
    }

    @Test
    @Order(10)
    @DisplayName("10 - creaMappaSingola (since Java 11)")
    void creaMappaSingola() {
        sorgente = "chiave";
        sorgente2 = "valore";
        mappaPrevista = new HashMap<>();

        mappaOttenuta = service.creaMappaSingola(null, null);
        Assertions.assertNotNull(mappaOttenuta);
        Assertions.assertEquals(mappaPrevista, mappaOttenuta);

        mappaOttenuta = service.creaMappaSingola(VUOTA, VUOTA);
        Assertions.assertNotNull(mappaOttenuta);
        Assertions.assertEquals(mappaPrevista, mappaOttenuta);

        mappaOttenuta = service.creaMappaSingola(VUOTA, null);
        Assertions.assertNotNull(mappaOttenuta);
        Assertions.assertEquals(mappaPrevista, mappaOttenuta);

        mappaOttenuta = service.creaMappaSingola(null, VUOTA);
        Assertions.assertNotNull(mappaOttenuta);
        Assertions.assertEquals(mappaPrevista, mappaOttenuta);

        mappaOttenuta = service.creaMappaSingola(sorgente, VUOTA);
        Assertions.assertNotNull(mappaOttenuta);
        Assertions.assertEquals(mappaPrevista, mappaOttenuta);

        mappaOttenuta = service.creaMappaSingola(VUOTA, sorgente2);
        Assertions.assertNotNull(mappaOttenuta);
        Assertions.assertEquals(mappaPrevista, mappaOttenuta);

        mappaPrevista.put(sorgente, sorgente2);
        mappaOttenuta = service.creaMappaSingola(sorgente, sorgente2);
        Assertions.assertNotNull(mappaOttenuta);
        Assertions.assertEquals(mappaPrevista, mappaOttenuta);
    }


    @Test
    @Order(11)
    @DisplayName("11 - toStringaVirgola (since Java 11)")
    void toStringaVirgola() {
        sorgenteArray = List.of("alfa", "beta", "gamma", "delta");
        previsto = "alfa,beta,gamma,delta";

        ottenuto = service.toStringaVirgola(null);
        assertTrue(textService.isEmpty(ottenuto));

        ottenuto = service.toStringaVirgola(new ArrayList());
        assertTrue(textService.isEmpty(ottenuto));

        ottenuto = service.toStringaVirgola(sorgenteArray);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
    }

    @Test
    @Order(12)
    @DisplayName("12 - toStringaPipe (since Java 11)")
    void toStringaPipe() {
        ottenuto = service.toStringaPipe(null);
        assertTrue(textService.isEmpty(ottenuto));

        ottenuto = service.toStringaPipe(new ArrayList());
        assertTrue(textService.isEmpty(ottenuto));

        sorgenteArray = List.of("alfa", "beta", "gamma", "delta");
        previsto = "alfa|beta|gamma|delta";
        ottenuto = service.toStringaPipe(sorgenteArray);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);

        sorgenteArrayLong = List.of(Long.valueOf(876876), Long.valueOf(793444), Long.valueOf(22223), Long.valueOf(50030044));
        previsto = "876876|793444|22223|50030044";
        ottenuto = service.toStringaPipe(sorgenteArrayLong);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
    }

    @Test
    @Order(13)
    @DisplayName("13 - toStringa (since Java 11)")
    void toStringa() {
        sorgenteArray = List.of("alfa", "beta", "gamma", "delta");
        previsto = "alfa, beta, gamma, delta";

        ottenuto = service.toStringa(null);
        assertTrue(textService.isEmpty(ottenuto));

        ottenuto = service.toStringa(new ArrayList());
        assertTrue(textService.isEmpty(ottenuto));

        ottenuto = service.toStringa(sorgenteArray);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
    }


    @Test
    @Order(14)
    @DisplayName("14 - fromString (since Java 11)")
    void fromString() {
        previstoArray = List.of();
        sorgente = "alfa,beta,gamma,delta";

        ottenutoArray = service.fromStringa(null);
        Assertions.assertNotNull(ottenutoArray);
        Assertions.assertEquals(previstoArray, ottenutoArray);

        ottenutoArray = service.fromStringa(VUOTA);
        Assertions.assertNotNull(ottenutoArray);
        Assertions.assertEquals(previstoArray, ottenutoArray);

        previstoArray = List.of("alfa", "beta", "gamma", "delta");
        ottenutoArray = service.fromStringa(sorgente);
        Assertions.assertNotNull(ottenutoArray);
        Assertions.assertEquals(previstoArray, ottenutoArray);

        sorgente = "alfa, beta, gamma, delta";
        ottenutoArray = service.fromStringa(sorgente);
        Assertions.assertNotNull(ottenutoArray);
        Assertions.assertEquals(previstoArray, ottenutoArray);
    }


    @Test
    @Order(15)
    @DisplayName("15 - getColumnArray")
    void getColumnArray() {
        Grid<Via> grid = new Grid();
        Grid.Column[] colonne = service.getColumnArray(grid);
        Assertions.assertNotNull(colonne);

        //        Grid.Column[] matrix = grid.getColumns().toArray((Grid.Column));
        //        Assertions.assertNotNull(colonne);
    }

    @Test
    @Order(20)
    @DisplayName("20 - check enumeration")
    void checkEnumeration() {

        sorgente = "giorno";
        ottenutoBooleano = AECrono.getValue().contains(sorgente);
        assertTrue(ottenutoBooleano);

        sorgente = "giorno";
        ottenutoBooleano = AEGeografia.getValue().contains(sorgente);
        assertFalse(ottenutoBooleano);

        sorgente = "provincia";
        ottenutoBooleano = AEGeografia.getValue().contains(sorgente);
        assertTrue(ottenutoBooleano);

        sorgente = "provincia";
        ottenutoBooleano = AECrono.getValue().contains(sorgente);
        assertFalse(ottenutoBooleano);

        sorgente = "company";
        ottenutoBooleano = AECrono.getValue().contains(sorgente);
        assertFalse(ottenutoBooleano);
        ottenutoBooleano = AEGeografia.getValue().contains(sorgente);
        assertFalse(ottenutoBooleano);
    }

}