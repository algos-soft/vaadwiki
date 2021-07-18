package it.algos.unit;

import com.vaadin.flow.data.provider.*;
import it.algos.test.*;
import it.algos.vaadflow14.backend.service.*;
import org.junit.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 30-apr-2021
 * Time: 08:34
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("UtilityService - Utility varie.")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UtilityServiceTest extends ATest {


    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    UtilityService service;

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpAll() {
        super.setUpStartUp();

        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(service);
        Assertions.assertNotNull(service);
        service.text = text;
        service.array = array;
        service.annotation = annotation;
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
    @DisplayName("1 - getSortField")
    void getSortField() {
        previsto = NAME_NOME;

        sortSpring = Sort.by(Sort.Direction.ASC, NAME_NOME);
        Assert.assertNotNull(sortSpring);

        ottenuto = service.getSortFieldName(sortSpring);
        Assert.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, ottenuto);
    }

    @Test
    @Order(2)
    @DisplayName("2 - getSortDirection")
    void getSortDirection() {
        Sort.Direction direzionePrevista = Sort.Direction.ASC;

        sortSpring = Sort.by(Sort.Direction.ASC, NAME_NOME);
        Assert.assertNotNull(sortSpring);

        direzionePrevista = service.getSortDirection(sortSpring);
        Assert.assertNotNull(direzionePrevista);
        Assert.assertEquals(previsto, ottenuto);
    }


    @Test
    @Order(3)
    @DisplayName("3 - getSortDirection")
    void getSortDirection2() {
        Sort.Direction direzionePrevista = Sort.Direction.ASC;

        sortVaadin = new QuerySortOrder(NAME_NOME, SortDirection.ASCENDING);
        Assert.assertNotNull(sortVaadin);

        direzionePrevista = service.getSortDirection(sortVaadin);
        Assert.assertNotNull(direzionePrevista);
        Assert.assertEquals(previsto, ottenuto);
    }

    @Test
    @Order(4)
    @DisplayName("4 - sortVaadinToSpring")
    void sortVaadinToSpring() {
        previsto = NAME_NOME;
        Sort.Direction previstoDirection;
        Sort.Direction ottenutoDirection;

        previstoDirection = Sort.Direction.ASC;
        sortVaadin = new QuerySortOrder(NAME_NOME, SortDirection.ASCENDING);
        sortSpring = service.sortVaadinToSpring(sortVaadin);
        Assert.assertNotNull(sortSpring);
        ottenuto = service.getSortFieldName(sortSpring);
        Assert.assertEquals(previsto, ottenuto);
        ottenutoDirection = service.getSortDirection(sortSpring);
        Assert.assertEquals(previstoDirection, ottenutoDirection);

        previstoDirection = Sort.Direction.DESC;
        sortVaadin = new QuerySortOrder(NAME_NOME, SortDirection.DESCENDING);
        sortSpring = service.sortVaadinToSpring(sortVaadin);
        Assert.assertNotNull(sortSpring);
        ottenuto = service.getSortFieldName(sortSpring);
        Assert.assertEquals(previsto, ottenuto);
        ottenutoDirection = service.getSortDirection(sortSpring);
        Assert.assertEquals(previstoDirection, ottenutoDirection);
    }


    @Test
    @Order(5)
    @DisplayName("5 - List sortVaadinToSpring")
    void sortVaadinToSpring2() {
        List<QuerySortOrder> sortVaadinList = new ArrayList<>();

        previstoIntero = 1;
        previsto = "_id: ASC";
        sortSpring = service.sortVaadinToSpring((List<QuerySortOrder>) null,null);
        Assert.assertNotNull(sortSpring);
        Assert.assertEquals(previstoIntero, sortSpring.stream().count());
        ottenuto = sortSpring.toString();
        Assert.assertEquals(previsto, ottenuto);

        sortVaadin = new QuerySortOrder(NAME_NOME, SortDirection.ASCENDING);
        sortVaadinList.add(sortVaadin);
        sortVaadin = new QuerySortOrder(NAME_ORDINE, SortDirection.DESCENDING);
        sortVaadinList.add(sortVaadin);
        sortVaadin = new QuerySortOrder(NAME_CODE, SortDirection.ASCENDING);
        sortVaadinList.add(sortVaadin);

        sortSpring = service.sortVaadinToSpring(sortVaadinList,null);
        Assert.assertNotNull(sortSpring);
        Assert.assertEquals(3, sortSpring.stream().count());
    }

    @Test
    @Order(6)
    @DisplayName("6 - sortSpringToVaadin")
    void sortSpringToVaadin() {
        QuerySortOrder ottenutoSortVaadin;
        previsto = NAME_NOME;
        SortDirection previstoDirection;
        SortDirection ottenutoDirection;

        previstoDirection = SortDirection.ASCENDING;
        sortSpring = Sort.by(Sort.Direction.ASC, NAME_NOME);
        ottenutoSortVaadin = service.sortSpringToVaadin(sortSpring);
        Assert.assertNotNull(ottenutoSortVaadin);
        ottenuto = ottenutoSortVaadin.getSorted();
        Assert.assertEquals(previsto, ottenuto);
        ottenutoDirection = ottenutoSortVaadin.getDirection();
        Assert.assertEquals(previstoDirection, ottenutoDirection);

        previstoDirection = SortDirection.DESCENDING;
        sortSpring = Sort.by(Sort.Direction.DESC, NAME_NOME);
        ottenutoSortVaadin = service.sortSpringToVaadin(sortSpring);
        Assert.assertNotNull(ottenutoSortVaadin);
        ottenuto = ottenutoSortVaadin.getSorted();
        Assert.assertEquals(previsto, ottenuto);
        ottenutoDirection = ottenutoSortVaadin.getDirection();
        Assert.assertEquals(previstoDirection, ottenutoDirection);
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