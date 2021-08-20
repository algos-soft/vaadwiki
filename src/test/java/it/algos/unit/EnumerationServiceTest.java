package it.algos.unit;

import static com.helger.commons.mock.CommonsAssert.assertEquals;
import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.service.*;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: dom, 30-ago-2020
 * Time: 10:23
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("EnumerationService - Persistenza nel mongoDB delle enumeration.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EnumerationServiceTest extends ATest {

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private EnumerationService service;

    private String rawValues;

    private String singleValue;

    private List<String> listValues;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        service = enumerationService;
    }


    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();

        rawValues = VUOTA;
        singleValue = VUOTA;
        listValues = null;
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


    @Test
    @Order(1)
    @DisplayName("1 - Crea la preferenza da una serie")
    void fixPreferenzaMongoDB() {
        previsto = "alfa,beta,gamma;beta";

        ottenuto = service.fixPreferenzaMongoDB(rawValues, singleValue);
        assertTrue(textService.isEmpty(ottenuto));

        rawValues = "alfa,beta,gamma";
        singleValue = VUOTA;
        ottenuto = service.fixPreferenzaMongoDB(rawValues, singleValue);
        assertTrue(textService.isEmpty(ottenuto));

        rawValues = VUOTA;
        singleValue = "beta";
        ottenuto = service.fixPreferenzaMongoDB(rawValues, singleValue);
        assertTrue(textService.isEmpty(ottenuto));

        rawValues = "alfa,beta,gamma";
        singleValue = "delta";
        ottenuto = service.fixPreferenzaMongoDB(rawValues, singleValue);
        assertTrue(textService.isEmpty(ottenuto));

        rawValues = "alfa";
        singleValue = "alfa";
        previsto2 = "alfa;alfa";
        ottenuto = service.fixPreferenzaMongoDB(rawValues, singleValue);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto2, ottenuto);

        rawValues = "alfa,beta;gamma";
        singleValue = "delta";
        ottenuto = service.fixPreferenzaMongoDB(rawValues, singleValue);
        assertTrue(textService.isEmpty(ottenuto));

        rawValues = "alfa,beta,gamma";
        singleValue = "beta,gamma";
        ottenuto = service.fixPreferenzaMongoDB(rawValues, singleValue);
        assertTrue(textService.isEmpty(ottenuto));

        rawValues = "alfa,beta,gamma";
        singleValue = "beta;gamma";
        ottenuto = service.fixPreferenzaMongoDB(rawValues, singleValue);
        assertTrue(textService.isEmpty(ottenuto));

        rawValues = "alfa,beta,gamma";
        singleValue = "beta";
        ottenuto = service.fixPreferenzaMongoDB(rawValues, singleValue);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
    }


    @Test
    @Order(2)
    @DisplayName("2 - Crea la preferenza da una lista")
    void fixPreferenzaMongoDB2() {
        previsto = "alfa,beta,gamma;beta";

        ottenuto = service.fixPreferenzaMongoDB(listValues, singleValue);
        assertTrue(textService.isEmpty(ottenuto));

        listValues = arrayService.fromStringa("alfa,beta,gamma");
        singleValue = VUOTA;
        ottenuto = service.fixPreferenzaMongoDB(listValues, singleValue);
        assertTrue(textService.isEmpty(ottenuto));

        listValues = null;
        singleValue = "beta";
        ottenuto = service.fixPreferenzaMongoDB(listValues, singleValue);
        assertTrue(textService.isEmpty(ottenuto));

        listValues = arrayService.fromStringa("alfa,beta,gamma");
        singleValue = "delta";
        ottenuto = service.fixPreferenzaMongoDB(listValues, singleValue);
        assertTrue(textService.isEmpty(ottenuto));

        listValues = arrayService.fromStringa("alfa");
        singleValue = "alfa";
        previsto2 = "alfa;alfa";
        ottenuto = service.fixPreferenzaMongoDB(listValues, singleValue);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto2, ottenuto);

        listValues = arrayService.fromStringa("alfa,beta;gamma");
        singleValue = "delta";
        ottenuto = service.fixPreferenzaMongoDB(listValues, singleValue);
        assertTrue(textService.isEmpty(ottenuto));

        listValues = arrayService.fromStringa("alfa,beta,gamma");
        singleValue = "beta,gamma";
        ottenuto = service.fixPreferenzaMongoDB(listValues, singleValue);
        assertTrue(textService.isEmpty(ottenuto));

        listValues = arrayService.fromStringa("alfa,beta,gamma");
        singleValue = "beta;gamma";
        ottenuto = service.fixPreferenzaMongoDB(listValues, singleValue);
        assertTrue(textService.isEmpty(ottenuto));

        listValues = arrayService.fromStringa("alfa,beta,gamma");
        singleValue = "beta";
        ottenuto = service.fixPreferenzaMongoDB(listValues, singleValue);
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
    }


    @Test
    @Order(3)
    @DisplayName("3 - Matrice dei valori e del valore selezionato")
    void getParti() {
        sorgente = "alfa,beta,gamma;delta";
        previstoMatrice = new String[]{"alfa,beta,gamma", "delta"};
        ottenutoMatrice = service.getParti(sorgente);
        Assertions.assertNotNull(ottenutoMatrice);
        Assert.assertEquals(previstoMatrice, ottenutoMatrice);
        Assert.assertEquals(ottenutoMatrice.length, 2);
        Assert.assertEquals(ottenutoMatrice[0], "alfa,beta,gamma");
        Assert.assertEquals(ottenutoMatrice[1], "delta");

        sorgente = "alfa,beta,gamma";
        previstoMatrice = new String[]{"alfa,beta,gamma"};
        ottenutoMatrice = service.getParti(sorgente);
        Assertions.assertNotNull(ottenutoMatrice);
        Assert.assertEquals(previstoMatrice, ottenutoMatrice);
        Assert.assertEquals(ottenutoMatrice.length, 1);
        Assert.assertEquals(ottenutoMatrice[0], "alfa,beta,gamma");

        sorgente = "alfa";
        previstoMatrice = new String[]{"alfa"};
        ottenutoMatrice = service.getParti(sorgente);
        Assertions.assertNotNull(ottenutoMatrice);
        Assert.assertEquals(previstoMatrice, ottenutoMatrice);
        Assert.assertEquals(ottenutoMatrice.length, 1);
        Assert.assertEquals(ottenutoMatrice[0], "alfa");

        sorgente = "alfa,beta,gamma;delta,epsilon";
        previstoMatrice = new String[]{"alfa,beta,gamma", "delta,epsilon"};
        ottenutoMatrice = service.getParti(sorgente);
        Assertions.assertNotNull(ottenutoMatrice);
        Assert.assertEquals(previstoMatrice, ottenutoMatrice);
        Assert.assertEquals(ottenutoMatrice.length, 2);
        Assert.assertEquals(ottenutoMatrice[0], "alfa,beta,gamma");
        Assert.assertEquals(ottenutoMatrice[1], "delta,epsilon");

        sorgente = "alfa,beta,gamma;delta;epsilon";
        previstoMatrice = new String[]{"alfa,beta,gamma", "delta;epsilon"};
        ottenutoMatrice = service.getParti(sorgente);
        Assertions.assertNotNull(ottenutoMatrice);
        Assert.assertEquals(previstoMatrice, ottenutoMatrice);
        Assert.assertEquals(ottenutoMatrice.length, 2);
        Assert.assertEquals(ottenutoMatrice[0], "alfa,beta,gamma");
        Assert.assertEquals(ottenutoMatrice[1], "delta;epsilon");
    }


    @Test
    @Order(4)
    @DisplayName("4 - Estrae la lista dei valori")
    void getList() {
        previstoArray = new ArrayList<>(Arrays.asList("alfa", "beta", "gamma"));

        sorgente = "alfa,beta,gamma;delta";
        ottenutoArray = service.getList(sorgente);
        Assertions.assertNotNull(ottenutoArray);
        Assert.assertEquals(previstoArray, ottenutoArray);

        sorgente = "alfa,beta,gamma";
        ottenutoArray = service.getList(sorgente);
        Assertions.assertNotNull(ottenutoArray);
        Assert.assertEquals(previstoArray, ottenutoArray);

        sorgente = "alfa,beta,gamma;delta,epsilon";
        ottenutoArray = service.getList(sorgente);
        Assertions.assertNotNull(ottenutoArray);
        Assert.assertEquals(previstoArray, ottenutoArray);

        sorgente = "alfa,beta,gamma;delta;epsilon";
        ottenutoArray = service.getList(sorgente);
        Assertions.assertNotNull(ottenutoArray);
        Assert.assertEquals(previstoArray, ottenutoArray);

        previstoArray = new ArrayList<>(Arrays.asList("alfa"));
        sorgente = "alfa";
        ottenutoArray = service.getList(sorgente);
        Assertions.assertNotNull(ottenutoArray);
        Assert.assertEquals(previstoArray, ottenutoArray);
    }


    @Test
    @Order(5)
    @DisplayName("5 - Estrae il valore previsto")
    void convertToPresentation() {
        sorgente = "alfa,beta,gamma;delta";
        previsto = "delta";
        ottenuto = service.convertToPresentation(sorgente);
        Assertions.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, ottenuto);

        sorgente = "alfa,beta,gamma";
        previsto = VUOTA;
        ottenuto = service.convertToPresentation(sorgente);
        Assertions.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, ottenuto);

        sorgente = "alfa";
        previsto = VUOTA;
        ottenuto = service.convertToPresentation(sorgente);
        Assertions.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, ottenuto);

        sorgente = "alfa,beta,gamma;delta,epsilon";
        previsto = "delta,epsilon";
        ottenuto = service.convertToPresentation(sorgente);
        Assertions.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, ottenuto);

        sorgente = "alfa,beta,gamma;delta;epsilon";
        previsto = "delta;epsilon";
        ottenuto = service.convertToPresentation(sorgente);
        Assertions.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, ottenuto);
    }


    @Test
    @Order(6)
    @DisplayName("6 - Modifica la stringa da memorizzare")
    void convertToModel() {
        String rawValue;
        String newSelectedValue;

        rawValue = VUOTA;
        newSelectedValue = VUOTA;
        previsto = VUOTA;
        ottenuto = service.convertToModel(rawValue, newSelectedValue);
        Assertions.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, ottenuto);

        rawValue = VUOTA;
        newSelectedValue = "veloce";
        previsto = VUOTA;
        ottenuto = service.convertToModel(rawValue, newSelectedValue);
        Assertions.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, ottenuto);

        rawValue = "veloce";
        newSelectedValue = VUOTA;
        previsto = "veloce";
        ottenuto = service.convertToModel(rawValue, newSelectedValue);
        Assertions.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, ottenuto);

        rawValue = "veloce,lento";
        newSelectedValue = VUOTA;
        previsto = "veloce,lento";
        ottenuto = service.convertToModel(rawValue, newSelectedValue);
        Assertions.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, ottenuto);

        rawValue = "veloce,lento;";
        newSelectedValue = VUOTA;
        previsto = "veloce,lento";
        ottenuto = service.convertToModel(rawValue, newSelectedValue);
        Assertions.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, ottenuto);

        rawValue = "veloce,lento";
        newSelectedValue = "lento";
        previsto = "veloce,lento;lento";
        ottenuto = service.convertToModel(rawValue, newSelectedValue);
        Assertions.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, ottenuto);

        rawValue = "veloce,lento;";
        newSelectedValue = "lento";
        previsto = "veloce,lento;lento";
        ottenuto = service.convertToModel(rawValue, newSelectedValue);
        Assertions.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, ottenuto);

        rawValue = "veloce,lento;veloce";
        newSelectedValue = "lento";
        previsto = "veloce,lento;lento";
        ottenuto = service.convertToModel(rawValue, newSelectedValue);
        Assertions.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, ottenuto);

        rawValue = "veloce,lento";
        newSelectedValue = "adagio";
        previsto = "veloce,lento";
        ottenuto = service.convertToModel(rawValue, newSelectedValue);
        Assertions.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, ottenuto);

        rawValue = "veloce,lento;veloce";
        newSelectedValue = "adagio";
        previsto = "veloce,lento;veloce";
        ottenuto = service.convertToModel(rawValue, newSelectedValue);
        Assertions.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, ottenuto);

        rawValue = "veloce,lento;";
        newSelectedValue = "adagio";
        previsto = "veloce,lento";
        ottenuto = service.convertToModel(rawValue, newSelectedValue);
        Assertions.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, ottenuto);
    }

}