package it.algos.integration;

import com.mongodb.client.*;
import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.packages.crono.mese.*;
import it.algos.vaadflow14.backend.packages.crono.secolo.*;
import it.algos.vaadflow14.backend.packages.preferenza.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.backend.wrapper.*;
import it.algos.vaadwiki.*;
import org.apache.poi.sl.usermodel.*;
import org.bson.conversions.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.*;
import org.springframework.test.context.junit.jupiter.*;

import java.io.*;
import java.util.*;


/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: ven, 17-lug-2020
 * Time: 06:27
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {WikiApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Mongo Service Integration (con mongoOp)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MongoServiceIntegrationTest extends MongoTest {

    private static int PREVISTO_ESEMPIO_INCROCIATO;

    private static Class<? extends AEntity> CLASSE_ESEMPIO_INCROCIATO = MESE_ENTITY_CLASS;

    /**
     * Inietta da Spring
     */
    @Autowired
    public MongoTemplate mongoOp;

    protected MongoCollection collection;

    protected Bson bSon;

    /**
     * The Service.
     */
    @Autowired
    MongoService service;

    @InjectMocks
    MeseService meseService;

    @InjectMocks
    SecoloService secoloService;

    private Mese meseUno;

    private Mese meseDue;

    private AETypeSerializing oldType;


    /**
     * Qui passa una volta sola <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        MockitoAnnotations.initMocks(this);

        MockitoAnnotations.initMocks(service);
        Assertions.assertNotNull(service);

        MockitoAnnotations.initMocks(meseService);
        Assertions.assertNotNull(meseService);

        MockitoAnnotations.initMocks(secoloService);
        Assertions.assertNotNull(secoloService);

        MockitoAnnotations.initMocks(service.mongoOp);
        Assertions.assertNotNull(service.mongoOp);

        //        this.cancellazioneEntitiesProvvisorie();
        //        this.creazioneInizialeEntitiesProvvisorie();
        //        oldType = FlowVar.typeSerializing;

        companyService.mongo = service;
        //        service.mongoOp = mongoOp;
    }


    @BeforeEach
    void setUpEach() {
        super.setUp();

        collection = null;
        bSon = null;
    }


    @Test
    @Order(1)
    @DisplayName("1 - Stato del database")
    void status() {
        System.out.println("1- Stato del database");
        MongoDatabase dataBase;

        ottenuto = service.getDatabaseName();
        assertTrue(textService.isValid(ottenuto));
        System.out.println(VUOTA);
        System.out.println(String.format("Nome del dataBase corrente: [%s]", ottenuto));

        dataBase = service.getDataBase();
        assertNotNull(dataBase);
        System.out.println(VUOTA);
        System.out.println(String.format("DataBase corrente: [%s]", dataBase));

        listaStr = service.getCollezioni();
        assertNotNull(listaStr);
        System.out.println(VUOTA);
        System.out.println(String.format("Collezioni esistenti: %s", listaStr));
    }


    @ParameterizedTest
    @MethodSource(value = "CLAZZ")
    @NullSource
    @Order(2)
    @DisplayName("2 - Collezioni del database")
    /*
      Controlla l'esistenza della collezione (dall'elenco di tutte le condizioni esistenti nel mongoDB)
      Recupera la collezione
      Controlla l'esistenza della collezione (dal numero di entities presenti)
      Controlla se la collezione è vuota (dal numero di entities presenti)
     */
    void collectionClazz(Class clazz) {
        System.out.println("2 - Collezioni del database");
        String message = String.format("Clazz%s%s", FORWARD, getSimpleName(clazz));
        System.out.println(message);
        System.out.println(VUOTA);

        try {
            ottenutoBooleano = service.isExistsCollection(clazz);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        printCollection(clazz, ottenutoBooleano);
        collection = service.getCollection(clazz);
        printCollection(clazz, collection);
        try {
            ottenutoBooleano = service.isValidCollection(clazz);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        printCollectionValida(clazz, ottenutoBooleano);
        try {
            ottenutoBooleano = !service.isValidCollection(clazz);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        printCollectionVuota(clazz, ottenutoBooleano);

        System.out.println(VUOTA);
    }


    @ParameterizedTest
    @MethodSource(value = "CLAZZ")
    @Order(25)
    @DisplayName("25 - Count totale (gson/spring) per clazz")
    /*
      metodo semplice per l'intera collection
      rimanda al metodo base collection.countDocuments();
      non usa ne gson ne spring
     */
        //--clazz
        //--previstoIntero
        //--risultatoEsatto
    void countAllClazz(final Class clazz, final int previstoIntero, final boolean risultatoEsatto) {
        System.out.println("25 - Count totale (gson/spring) per clazz");
        String message = String.format("Count totale di %s", getSimpleName(clazz));
        System.out.println(message);

        ottenutoIntero = 0;
        try {
            ottenutoIntero = service.count(clazz);
            System.out.println(String.format("Risultato %s %d", UGUALE_SEMPLICE, ottenutoIntero));
            System.out.println(VUOTA);
            printCount(clazz, previstoIntero, ottenutoIntero, risultatoEsatto);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }

        if (flagRisultatiEsattiObbligatori && risultatoEsatto) {
            assertEquals(previstoIntero, ottenutoIntero);
        }
    }


    @ParameterizedTest
    @MethodSource(value = "CLAZZ_KEY_ID")
    @Order(26)
    @DisplayName("26 - Count filtrato (gson) (keyValue)")
        //--clazz
        //--keyValue
        //--doc e/o entityBean valida
    void countKeyGson(final Class clazz, final Serializable keyValue, final boolean entityBeanValida) {
        System.out.println("26 - Count filtrato (gson) (keyValue)");
        FlowVar.typeSerializing = AETypeSerializing.gson;
        countKey(clazz, keyValue, entityBeanValida, "filter");
    }

    @ParameterizedTest
    @MethodSource(value = "CLAZZ_KEY_ID")
    @Order(27)
    @DisplayName("27 - Count filtrato (spring) (keyValue)")
        //--clazz
        //--keyValue
        //--doc e/o entityBean valida
    void countKeySpring(final Class clazz, final Serializable keyValue, final boolean entityBeanValida) {
        System.out.println("27 - Count filtrato (spring) (keyValue)");
        FlowVar.typeSerializing = AETypeSerializing.spring;
        countKey(clazz, keyValue, entityBeanValida, "query");
    }

    private void countKey(final Class clazz, final Serializable keyValue, final boolean entityBeanValida, final String tag) {
        String message = String.format("Count filtrato di %s", getSimpleName(clazz));
        System.out.println(message);
        String keyValueVideo = getPropertyVideo(keyValue);
        message = String.format("%s%s%s=%s", textService.primaMaiuscola(tag), FORWARD, FIELD_ID, keyValueVideo);
        System.out.println(message);

        ottenutoIntero = 0;
        try {
            ottenutoIntero = service.count(clazz, keyValue);
            System.out.println(String.format("Risultato %s %d", UGUALE_SEMPLICE, ottenutoIntero));
            System.out.println(VUOTA);
            //            printCount(clazz, propertyName, propertyValueVideo, previstoIntero, ottenutoIntero);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }

        assertTrue(entityBeanValida ? ottenutoIntero == 1 : ottenutoIntero == 0);
        System.out.println(VUOTA);
    }


    @ParameterizedTest
    @MethodSource(value = "CLAZZ_KEY_ID")
    @Order(28)
    @DisplayName("28 - Esistenza (gson) (keyValue)")
        //--clazz
        //--keyValue
        //--doc e/o entityBean valida
    void isEsisteGson(final Class clazz, final Serializable keyValue, final boolean entityBeanValida) {
        System.out.println("28 - Esistenza (gson) (keyValue)");
        FlowVar.typeSerializing = AETypeSerializing.gson;
        isEsiste(clazz, keyValue, entityBeanValida, "filter");
    }

    @ParameterizedTest
    @MethodSource(value = "CLAZZ_KEY_ID")
    @Order(29)
    @DisplayName("29 - Esistenza (spring) (keyValue)")
        //--clazz
        //--keyValue
        //--doc e/o entityBean valida
    void isEsisteSpring(final Class clazz, final Serializable keyValue, final boolean entityBeanValida) {
        System.out.println("29 - Esistenza (spring) (keyValue)");
        FlowVar.typeSerializing = AETypeSerializing.spring;
        isEsiste(clazz, keyValue, entityBeanValida, "query");
    }


    private void isEsiste(final Class clazz, final Serializable keyValue, final boolean entityBeanValida, final String tag) {
        String message = String.format("Esistenza di una entity nella entityClazz %s", getSimpleName(clazz));
        System.out.println(message);
        String esisteText = VUOTA;
        String keyValueLower = (String) keyValue;
        String propertyValueVideo = getPropertyVideo(keyValue);
        message = String.format("%s%s%s=%s", textService.primaMaiuscola(tag), FORWARD, FIELD_ID, propertyValueVideo);
        System.out.println(message);
        boolean usaKeyIdMinuscolaCaseInsensitive = false;
        try {
            usaKeyIdMinuscolaCaseInsensitive = clazz != null && annotationService.usaKeyIdMinuscolaCaseInsensitive(clazz);
        } catch (AlgosException unErrore) {
            //--non serve
        }
        if (usaKeyIdMinuscolaCaseInsensitive && textService.isValid(keyValueLower)) {
            keyValueLower = keyValueLower.toLowerCase();
        }

        ottenutoIntero = 0;
        try {
            ottenutoBooleano = service.isEsiste(clazz, keyValue);
            assertEquals(entityBeanValida, ottenutoBooleano);
            System.out.println(VUOTA);

            if (ottenutoBooleano) {
                esisteText = "esiste";
            }
            else {
                esisteText = "NON esiste";
            }

            if (usaKeyIdMinuscolaCaseInsensitive) {
                if (ottenutoBooleano) {
                    if (keyValueLower.equals((String) keyValue)) {
                        System.out.println(String.format("Nella classe %s %s una entityBean con %s%s%s", getSimpleName(clazz), esisteText, FIELD_ID, UGUALE_SEMPLICE, keyValueLower));
                    }
                    else {
                        System.out.println(String.format("Nella classe %s %s una entityBean con %s%s%s", getSimpleName(clazz), "NON esiste", FIELD_ID, UGUALE_SEMPLICE, keyValue));
                        System.out.println(String.format("Tuttavia la classe %s usa 'usaKeyIdMinuscolaCaseInsensitive' e quindi", getSimpleName(clazz), "NON esiste", FIELD_ID, UGUALE_SEMPLICE, keyValueLower));
                        System.out.println(String.format("Nella classe %s %s una entityBean con %s%s%s", getSimpleName(clazz), "esiste", FIELD_ID, UGUALE_SEMPLICE, keyValueLower));
                    }
                }
                else {
                    System.out.println(String.format("Nella classe %s %s una entityBean con %s%s%s", getSimpleName(clazz), esisteText, FIELD_ID, UGUALE_SEMPLICE, keyValueLower));
                }
            }
            else {
                System.out.println(String.format("Nella classe %s %s una entityBean con %s%s%s", getSimpleName(clazz), esisteText, FIELD_ID, UGUALE_SEMPLICE, keyValueLower));
            }

        } catch (AlgosException unErrore) {
            printError(unErrore);
        }

    }

    @ParameterizedTest
    @MethodSource(value = "CLAZZ_PROPERTY")
    @Order(32)
    @DisplayName("32 - Count filtrato (gson) (propertyName, propertyValue)")
    void countPropertyGson(final Class clazz, final String propertyName, final Serializable propertyValue, final int previstoIntero) {
        System.out.println("32 - Count filtrato (gson) (propertyName, propertyValue)");
        FlowVar.typeSerializing = AETypeSerializing.gson;
        countProperty(clazz, propertyName, propertyValue, previstoIntero, "filter");
    }

    @ParameterizedTest
    @MethodSource(value = "CLAZZ_PROPERTY")
    @Order(33)
    @DisplayName("33 - Count filtrato (spring) (propertyName, propertyValue)")
    void countPropertySpring(final Class clazz, final String propertyName, final Serializable propertyValue, final int previstoIntero) {
        System.out.println("33 - Count filtrato (spring) (propertyName, propertyValue)");
        FlowVar.typeSerializing = AETypeSerializing.spring;
        countProperty(clazz, propertyName, propertyValue, previstoIntero, "query");
    }

    private void countProperty(final Class clazz, final String propertyName, final Serializable propertyValue, final int previstoIntero, final String tag) {
        String message = String.format("Count filtrato di %s", getSimpleName(clazz));
        System.out.println(message);
        String propertyValueVideo = getPropertyVideo(propertyValue);
        message = String.format("%s%s%s=%s", textService.primaMaiuscola(tag), FORWARD, propertyName, propertyValueVideo);
        System.out.println(message);

        ottenutoIntero = 0;
        try {
            ottenutoIntero = service.count(clazz, propertyName, propertyValue);
            System.out.println(String.format("Risultato %s %d", UGUALE_SEMPLICE, ottenutoIntero));
            System.out.println(VUOTA);
            printCount(clazz, propertyName, propertyValueVideo, previstoIntero, ottenutoIntero);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        if (flagRisultatiEsattiObbligatori) {
            assertEquals(previstoIntero, ottenutoIntero);
        }
        System.out.println(VUOTA);
    }


    @ParameterizedTest
    @MethodSource(value = "CLAZZ_FILTER")
    @Order(34)
    @DisplayName("34 - Count filtrato (gson) (WrapFiltro)")
    void countWrapFiltroGson(final Class clazz, final AETypeFilter filter, final String propertyName, final Serializable propertyValue, final int previstoIntero) {
        System.out.println("34 - Count filtrato (gson) (WrapFiltro)");
        FlowVar.typeSerializing = AETypeSerializing.gson;
        countWrapFiltro(clazz, filter, propertyName, propertyValue, previstoIntero, "filter");
    }


    @ParameterizedTest
    @MethodSource(value = "CLAZZ_FILTER")
    @Order(35)
    @DisplayName("35 - Count filtrato (spring) (WrapFiltro)")
    void countWrapFiltroSpring(final Class clazz, final AETypeFilter filter, final String propertyName, final Serializable propertyValue, final int previstoIntero) {
        System.out.println("35 - Count filtrato (spring) (WrapFiltro)");
        FlowVar.typeSerializing = AETypeSerializing.spring;
        countWrapFiltro(clazz, filter, propertyName, propertyValue, previstoIntero, "query");
    }


    private void countWrapFiltro(final Class clazz, AETypeFilter filter, final String propertyName, final Serializable propertyValue, final int previstoIntero, final String tag) {
        String message = String.format("Count filtrato di %s", getSimpleName(clazz));
        System.out.println(message);
        String propertyValueVideo = getPropertyVideo(propertyValue);
        message = String.format("%s%s%s", textService.primaMaiuscola(tag), FORWARD, filter != null ? filter.getOperazione(propertyName, propertyValueVideo) : "null");
        System.out.println(message);

        String propertyField;
        ottenutoIntero = 0;

        try {
            wrapFiltri.regola(clazz, filter, propertyName, propertyValue);
            propertyField = textService.levaCoda(propertyName, FIELD_NAME_ID_LINK);
            filter = wrapFiltri.getMappaFiltri().get(propertyField).getType();
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }

        if (wrapFiltri != null) {
            try {
                ottenutoIntero = service.count(clazz, wrapFiltri);
                System.out.println(String.format("Risultato = %d", ottenutoIntero));
                System.out.println(VUOTA);
            } catch (AlgosException unErrore) {
                printError(unErrore);
            }
            System.out.println(VUOTA);
            printWrapFiltro(clazz, filter, propertyName, propertyValueVideo, previstoIntero, ottenutoIntero);
        }
        assertEquals(previstoIntero, ottenutoIntero);
    }

    @ParameterizedTest
    @MethodSource(value = "CLAZZ_FILTER")
    @Order(36)
    @DisplayName("36 - Count filtrato (gson) (AFiltro)")
    void countAFiltroGson(final Class clazz, final AETypeFilter filter, final String propertyName, final Serializable propertyValue, final int previstoIntero) {
        System.out.println("36 - Count filtrato (gson) (AFiltro)");
        FlowVar.typeSerializing = AETypeSerializing.gson;
        countAFiltro(clazz, filter, propertyName, propertyValue, previstoIntero, "filter");
    }

    @ParameterizedTest
    @MethodSource(value = "CLAZZ_FILTER")
    @Order(37)
    @DisplayName("37 - Count filtrato (spring) (AFiltro)")
    void countAFiltroSpring(final Class clazz, final AETypeFilter filter, final String propertyName, final Serializable propertyValue, final int previstoIntero) {
        System.out.println("37 - Count filtrato (spring) (AFiltro)");
        FlowVar.typeSerializing = AETypeSerializing.spring;
        countAFiltro(clazz, filter, propertyName, propertyValue, previstoIntero, "query");
    }

    private void countAFiltro(final Class clazz, AETypeFilter filter, final String propertyName, final Serializable propertyValue, final int previstoIntero, final String tag) {
        String message = String.format("Count filtrato di %s", getSimpleName(clazz));
        System.out.println(message);
        String propertyValueVideo = getPropertyVideo(propertyValue);
        message = String.format("%s%s%s", textService.primaMaiuscola(tag), FORWARD, filter != null ? filter.getOperazione(propertyName, propertyValueVideo) : "null");
        System.out.println(message);
        mappaFiltri = new HashMap<>();
        ottenutoIntero = 0;

        try {
            filtro = creaFiltro(clazz, filter, propertyName, propertyValue);
            mappaFiltri.put(propertyName, filtro);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }

        if (mappaFiltri.size() > 0) {
            try {
                ottenutoIntero = service.count(clazz, mappaFiltri);
                System.out.println(String.format("Risultato = %d", ottenutoIntero));
                System.out.println(VUOTA);
            } catch (AlgosException unErrore) {
                printError(unErrore);
            }
            printWrapFiltro(clazz, filter, propertyName, propertyValueVideo, previstoIntero, ottenutoIntero);
            assertEquals(previstoIntero, ottenutoIntero);
        }
    }


    @Test
    @Order(38)
    @DisplayName("38 - Count filtrato (gson) (Map<String, AFiltro>)")
    void countMappaFiltroGson() {
        System.out.println("38 - Count filtrato (gson) (Map<String, AFiltro)");
        FlowVar.typeSerializing = AETypeSerializing.gson;
        countMappaFiltro("filter");
    }

    @Test
    @Order(39)
    @DisplayName("39 - Count filtrato (spring) (Map<String, AFiltro>)")
    void countMappaFiltroSpring() {
        System.out.println("39 - Count filtrato (spring) (Map<String, AFiltro)");
        FlowVar.typeSerializing = AETypeSerializing.spring;
        countMappaFiltro("query");
    }


    private void countMappaFiltro(final String tag) {
        Class clazz = CLASSE_ESEMPIO_INCROCIATO;
        String message = String.format("Count filtrato di %s", getSimpleName(clazz));
        System.out.println(message);
        AETypeFilter filter;
        String propertyName;
        Serializable propertyValue;
        String propertyValueVideo;
        String filterText = VUOTA;
        String sep = " + ";
        mappaFiltri = getEsempioMappaFiltro();

        if (mappaFiltri == null || mappaFiltri.size() == 0) {
            message = String.format("Nella entityClass %s non ho trovato nessuna entities col filtro indicato", clazz.getSimpleName());
            System.out.println(message);
            return;
        }

        for (String key : mappaFiltri.keySet()) {
            filter = mappaFiltri.get(key).getType();
            propertyName = mappaFiltri.get(key).getPropertyField();
            propertyValue = (Serializable) mappaFiltri.get(key).getPropertyValue();
            propertyValueVideo = getPropertyVideo(propertyValue);
            filterText += filter.getOperazione(propertyName, propertyValueVideo);
            filterText += sep;
        }
        filterText = textService.levaCoda(filterText, sep);
        message = String.format("%s%s%s", textService.primaMaiuscola(tag), FORWARD, filterText);
        System.out.println(message);
        ottenutoIntero = 0;

        if (mappaFiltri.size() > 0) {
            try {
                ottenutoIntero = service.count(clazz, mappaFiltri);
                System.out.println(String.format("Risultato = %d", ottenutoIntero));
                System.out.println(VUOTA);
            } catch (AlgosException unErrore) {
                printError(unErrore);
            }

            if (PREVISTO_ESEMPIO_INCROCIATO == ottenutoIntero) {
                message = String.format("Nella entityClass %s ho trovato %d entities con %s", getSimpleName(clazz), ottenutoIntero, filterText);
            }
            else {
                if (ottenutoIntero == 0) {
                    message = String.format("Nella entityClass %s non ho trovato nessuna entities col filtro indicato", getSimpleName(clazz));
                }
                else {
                    message = String.format("Nella entityClass %s ho trovato %d entities con %s che NON sono le %d previste", getSimpleName(clazz), ottenutoIntero, filterText, PREVISTO_ESEMPIO_INCROCIATO);
                }
            }
            System.out.println(message);
            assertEquals(PREVISTO_ESEMPIO_INCROCIATO, ottenutoIntero);
        }

    }

    @ParameterizedTest
    @MethodSource(value = "CLAZZ")
    @Order(40)
    @DisplayName("40 - Fetch completo (gson) di una classe")
    void fetchAllGson(final Class clazz, final int previstoIntero, final boolean risultatoEsatto) {
        System.out.println("40 - Fetch completo (gson) di una classe");
        FlowVar.typeSerializing = AETypeSerializing.gson;
        fetchAll(clazz, previstoIntero, risultatoEsatto);
    }

    @ParameterizedTest
    @MethodSource(value = "CLAZZ")
    @Order(41)
    @DisplayName("41 - Fetch completo (spring) di una classe")
    void fetchAllSpring(final Class clazz, final int previstoIntero, final boolean risultatoEsatto) {
        System.out.println("41 - Fetch completo (spring) di una classe");
        FlowVar.typeSerializing = AETypeSerializing.spring;
        fetchAll(clazz, previstoIntero, risultatoEsatto);
    }

    private void fetchAll(final Class clazz, final int previstoIntero, final boolean risultatoEsatto) {
        String message = String.format("Fetch completo di %s", getSimpleName(clazz));
        System.out.println(message);

        ottenutoIntero = 0;
        try {
            ottenutoIntero = service.count(clazz);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }

        try {
            listaBean = service.fetch(clazz);
            System.out.println(String.format("Risultato count %s %d", UGUALE_SEMPLICE, ottenutoIntero));
            System.out.println(String.format("Risultato fetch %s %d", UGUALE_SEMPLICE, listaBean.size()));
            System.out.println(VUOTA);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        if (listaBean != null) {
            if (ottenutoIntero == listaBean.size()) {
                printWrapFiltro(clazz, previstoIntero, listaBean, risultatoEsatto);
            }
            else {
                message = String.format("Qualcosa non quadra perché il fetch() ha recuperato %d entities mentre avrebbero dovuto essere %d secondo il count()", listaBean.size(), ottenutoIntero);
                System.out.println(message);
                assertEquals(previstoIntero, listaBean.size());
            }
        }
        else {
            if (previstoIntero != 0) {
                System.out.println("Qualcosa non quadra perché erano previste entities che non sono state trovate");
            }
        }
    }


    @ParameterizedTest
    @MethodSource(value = "CLAZZ_PROPERTY")
    @Order(42)
    @DisplayName("42 - Fetch filtrato (gson) (propertyName, propertyValue)")
    void fetchPropertyGson(final Class clazz, final String propertyName, final Serializable propertyValue, final int previstoIntero) {
        System.out.println("42 - Fetch filtrato (gson) (propertyName, propertyValue)");
        FlowVar.typeSerializing = AETypeSerializing.gson;
        fetchProperty(clazz, propertyName, propertyValue, previstoIntero, "filter");
    }

    @ParameterizedTest
    @MethodSource(value = "CLAZZ_PROPERTY")
    @Order(43)
    @DisplayName("43 - Fetch filtrato (spring) (propertyName, propertyValue)")
    void fetchPropertySpring(final Class clazz, final String propertyName, final Serializable propertyValue, final int previstoIntero) {
        System.out.println("43 - Fetch filtrato (spring) (propertyName, propertyValue)");
        FlowVar.typeSerializing = AETypeSerializing.spring;
        fetchProperty(clazz, propertyName, propertyValue, previstoIntero, "query");
    }


    private void fetchProperty(final Class clazz, final String propertyName, final Serializable propertyValue, final int previstoIntero, final String tag) {
        String message = String.format("Fetch filtrato di %s", getSimpleName(clazz));
        System.out.println(message);
        String propertyValueVideo = getPropertyVideo(propertyValue);
        message = String.format("%s%s%s=%s", textService.primaMaiuscola(tag), FORWARD, propertyName, propertyValueVideo);
        System.out.println(message);

        ottenutoIntero = 0;
        try {
            ottenutoIntero = service.count(clazz, propertyName, propertyValue);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }

        try {
            listaBean = service.fetch(clazz, propertyName, propertyValue);
            System.out.println(String.format("Risultato count %s %d", UGUALE_SEMPLICE, ottenutoIntero));
            System.out.println(String.format("Risultato fetch %s %d", UGUALE_SEMPLICE, listaBean.size()));
            System.out.println(VUOTA);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        if (listaBean != null) {
            if (ottenutoIntero == listaBean.size()) {
                printWrapFiltro(clazz, propertyName, propertyValueVideo, previstoIntero, listaBean);
            }
            else {
                message = String.format("Qualcosa non quadra perché il fetch() ha recuperato %d entities mentre avrebbero dovuto essere %d secondo il count()", listaBean.size(), ottenutoIntero);
                System.out.println(message);
                assertEquals(previstoIntero, listaBean.size());
            }
        }
        else {
            if (previstoIntero != 0) {
                System.out.println("Qualcosa non quadra perché erano previste entities che non sono state trovate");
            }
        }
    }

    @ParameterizedTest
    @MethodSource(value = "CLAZZ_FILTER")
    @Order(44)
    @DisplayName("44 - Fetch filtrato (gson) (WrapFiltro)")
    void fetchWrapFiltroGson(final Class clazz, final AETypeFilter filter, final String propertyName, final Serializable propertyValue, final int previstoIntero) {
        System.out.println("44 - Fetch filtrato (gson) (WrapFiltro)");
        FlowVar.typeSerializing = AETypeSerializing.gson;
        fetchWrapFiltro(clazz, filter, propertyName, propertyValue, previstoIntero, "filter");
    }


    @ParameterizedTest
    @MethodSource(value = "CLAZZ_FILTER")
    @Order(45)
    @DisplayName("45 - Fetch filtrato (spring) (WrapFiltro)")
    void fetchWrapFiltroSpring(final Class clazz, final AETypeFilter filter, final String propertyName, final Serializable propertyValue, final int previstoIntero) {
        System.out.println("45 - Fetch filtrato (spring) (WrapFiltro)");
        FlowVar.typeSerializing = AETypeSerializing.spring;
        fetchWrapFiltro(clazz, filter, propertyName, propertyValue, previstoIntero, "query");
    }


    void fetchWrapFiltro(final Class clazz, AETypeFilter filter, final String propertyName, final Serializable propertyValue, final int previstoIntero, final String tag) {
        String message = String.format("Fetch filtrato di %s", getSimpleName(clazz));
        System.out.println(message);
        String propertyValueVideo = getPropertyVideo(propertyValue);
        message = String.format("%s%s%s", textService.primaMaiuscola(tag), FORWARD, filter != null ? filter.getOperazione(propertyName, propertyValueVideo) : "null");
        System.out.println(message);
        String propertyField;
        boolean prosegui = true;

        try {
            wrapFiltri.regola(clazz, filter, propertyName, propertyValue);
            propertyField = textService.levaCoda(propertyName, FIELD_NAME_ID_LINK);
            filter = wrapFiltri.getMappaFiltri().get(propertyField).getType();
            message = String.format("%s%s%s", textService.primaMaiuscola(tag), FORWARD, filter.getOperazione(propertyName, propertyValueVideo));
            System.out.println(message);
        } catch (AlgosException unErrore) {
            printError(unErrore);
            prosegui = false;
        }

        if (prosegui) {
            ottenutoIntero = 0;
            try {
                ottenutoIntero = service.count(clazz, wrapFiltri);
            } catch (AlgosException unErrore) {
            }

            try {
                listaBean = service.fetch(clazz, wrapFiltri);
                System.out.println(String.format("Risultato count %s %d", UGUALE_SEMPLICE, ottenutoIntero));
                System.out.println(String.format("Risultato fetch %s %d", UGUALE_SEMPLICE, listaBean.size()));
                System.out.println(VUOTA);
            } catch (AlgosException unErrore) {
                printError(unErrore);
            }
            if (listaBean != null) {
                if (ottenutoIntero == listaBean.size()) {
                    printWrapFiltro(clazz, previstoIntero, listaBean, true);
                }
                else {
                    message = String.format("Qualcosa non quadra perché il fetch() ha recuperato %d entities mentre avrebbero dovuto essere %d secondo il count()", listaBean.size(), ottenutoIntero);
                    System.out.println(message);
                    assertEquals(previstoIntero, listaBean.size());
                }
            }
            else {
                if (previstoIntero != 0) {
                    System.out.println("Qualcosa non quadra perché erano previste entities che non sono state trovate");
                }
            }
        }
    }

    @ParameterizedTest
    @MethodSource(value = "CLAZZ_FILTER")
    @Order(46)
    @DisplayName("46 - Fetch filtrato (gson) (AFiltro)")
    void fetchAFiltroGson(final Class clazz, final AETypeFilter filter, final String propertyName, final Serializable propertyValue, final int previstoIntero) {
        System.out.println("46 - Fetch filtrato (gson) (AFiltro)");
        FlowVar.typeSerializing = AETypeSerializing.gson;
        fetchAFiltro(clazz, filter, propertyName, propertyValue, previstoIntero);
    }

    @ParameterizedTest
    @MethodSource(value = "CLAZZ_FILTER")
    @Order(47)
    @DisplayName("47 - Fetch filtrato (spring) (AFiltro)")
    void fetchAFiltroSpring(final Class clazz, final AETypeFilter filter, final String propertyName, final Serializable propertyValue, final int previstoIntero) {
        System.out.println("47 - Fetch filtrato (spring) (AFiltro)");
        FlowVar.typeSerializing = AETypeSerializing.spring;
        fetchAFiltro(clazz, filter, propertyName, propertyValue, previstoIntero);
    }


    private void fetchAFiltro(final Class clazz, AETypeFilter filter, final String propertyName, final Serializable propertyValue, final int previstoIntero) {
        String message = String.format("Fetch filtrato di %s", getSimpleName(clazz));
        System.out.println(message);
        String propertyValueVideo = getPropertyVideo(propertyValue);
        message = String.format("%s%s%s", textService.primaMaiuscola(tag), FORWARD, filter != null ? filter.getOperazione(propertyName, propertyValueVideo) : "null");
        System.out.println(message);
        mappaFiltri = new HashMap<>();

        try {
            filtro = creaFiltro(clazz, filter, propertyName, propertyValue);
            mappaFiltri.put(propertyName, filtro);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }

        if (mappaFiltri != null && mappaFiltri.size() == 1) {
            fetchMappaFiltro(clazz, mappaFiltri, previstoIntero);
        }
        else {
            if (clazz != null) {
                message = String.format("Nella entityClass %s non ho trovato nessuna entities col filtro indicato", getSimpleName(clazz));
            }
            else {
                message = String.format("Manca la entityClass");
            }
            System.out.println(message);
        }
    }


    @Test
    @Order(48)
    @DisplayName("48 - Fetch filtrato (gson) (Map<String, AFiltro>)")
    void fetchMappaFiltroGson() {
        System.out.println("48 - Fetch filtrato (gson) (Map<String, AFiltro)");
        FlowVar.typeSerializing = AETypeSerializing.gson;
        fetchMappaFiltroIncrociato();
    }

    @Test
    @Order(49)
    @DisplayName("49 - Fetch filtrato (spring) (Map<String, AFiltro>)")
    void fetchMappaFiltroSpring() {
        System.out.println("49 - Fetch filtrato (spring) (Map<String, AFiltro)");
        FlowVar.typeSerializing = AETypeSerializing.spring;
        fetchMappaFiltroIncrociato();
    }

    private void fetchMappaFiltroIncrociato() {
        String message;
        Class clazz = CLASSE_ESEMPIO_INCROCIATO;
        mappaFiltri = getEsempioMappaFiltro();

        if (mappaFiltri != null && mappaFiltri.size() > 0) {
            fetchMappaFiltro(clazz, mappaFiltri, PREVISTO_ESEMPIO_INCROCIATO);
        }
        else {
            message = String.format("Nella entityClass %s non ho trovato nessuna entities col filtro indicato", getSimpleName(clazz));
            System.out.println(message);
        }
    }

    private void fetchMappaFiltro(final Class clazz, final Map<String, AFiltro> mappaFiltri, final int previstoIntero) {
        String message = String.format("Count filtrato di %s", getSimpleName(clazz));
        System.out.println(message);
        AETypeFilter filter;
        String propertyName;
        Serializable propertyValue;
        String propertyValueVideo;
        String filterText = VUOTA;
        String sep = " + ";

        for (String key : mappaFiltri.keySet()) {
            filter = mappaFiltri.get(key).getType();
            propertyName = mappaFiltri.get(key).getPropertyField();
            propertyValue = (Serializable) mappaFiltri.get(key).getPropertyValue();
            propertyValueVideo = getPropertyVideo(propertyValue);
            filterText += filter.getOperazione(propertyName, propertyValueVideo);
            filterText += sep;
        }
        filterText = textService.levaCoda(filterText, sep);
        message = String.format("%s%s%s", textService.primaMaiuscola(tag), FORWARD, filterText);
        System.out.println(message);

        ottenutoIntero = 0;
        try {
            ottenutoIntero = service.count(clazz, mappaFiltri);
        } catch (AlgosException unErrore) {
        }

        try {
            listaBean = service.fetch(clazz, mappaFiltri);
            System.out.println(String.format("Risultato count %s %d", UGUALE_SEMPLICE, ottenutoIntero));
            System.out.println(String.format("Risultato fetch %s %d", UGUALE_SEMPLICE, listaBean.size()));
            System.out.println(VUOTA);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        if (listaBean != null) {
            if (ottenutoIntero == listaBean.size()) {
                printWrapFiltro(clazz, previstoIntero, listaBean, true);
            }
            else {
                message = String.format("Qualcosa non quadra perché il fetch() ha recuperato %d entities mentre avrebbero dovuto essere %d secondo il count()", listaBean.size(), ottenutoIntero);
                System.out.println(message);
                assertEquals(previstoIntero, listaBean.size());
            }
        }
        else {
            if (previstoIntero != 0) {
                System.out.println("Qualcosa non quadra perché erano previste entities che non sono state trovate");
            }
        }
    }

    @ParameterizedTest
    @MethodSource(value = "CLAZZ_OFFSET")
    @Order(50)
    @DisplayName("50 - Fetch offsetLimit (gson)")
        //--clazz
        //--previstoIntero
        //--risultatoEsatto
        //--offset (eventuale)
        //--limit (eventuale)
    void fetchOffsetLimitGson(final Class clazz, final int previstoIntero, final boolean risultatoEsatto, final int offset, final int limit) {
        System.out.println("50 - Fetch offsetLimit (gson)");
        FlowVar.typeSerializing = AETypeSerializing.gson;
        fetchOffsetLimit(clazz, offset, limit);
    }


    @ParameterizedTest
    @MethodSource(value = "CLAZZ_OFFSET")
    @Order(51)
    @DisplayName("51 - Fetch offsetLimit (spring)")
        //--clazz
        //--previstoIntero
        //--risultatoEsatto
        //--offset (eventuale)
        //--limit (eventuale)
    void fetchOffsetLimitSpring(final Class clazz, final int previstoIntero, final boolean risultatoEsatto, final int offset, final int limit) {
        System.out.println("51 - Fetch offsetLimit (spring)");
        FlowVar.typeSerializing = AETypeSerializing.spring;
        fetchOffsetLimit(clazz, offset, limit);
    }

    private void fetchOffsetLimit(final Class clazz, final int offset, final int limit) {
        String message;
        message = String.format("Fetch con offset e limit di %s", getSimpleName(clazz));
        System.out.println(message);
        System.out.println(String.format("Offset %s %d", UGUALE_SEMPLICE, offset));
        System.out.println(String.format("Limit %s %d", UGUALE_SEMPLICE, limit));

        try {
            listaBean = service.fetch(clazz, null, offset, limit);
            printLimit(clazz, limit, listaBean);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
    }


    @ParameterizedTest
    @MethodSource(value = "CLAZZ_SORT")
    @Order(52)
    @DisplayName("52 - Fetch filtrato e ordinato (spring) (propertyName, propertyValue)")
        //--clazz
        //--propertyName
        //--propertyValue
        //--previstoIntero
        //--sortSpring
    void fetchSortSpring(final Class clazz, final String propertyName, final Serializable propertyValue, final int previstoIntero, final Sort sort) {
        System.out.println("52 - Fetch filtrato e ordinato (spring) (propertyName, propertyValue)");
        FlowVar.typeSerializing = AETypeSerializing.spring;

        if (clazz == null) {
            System.out.println("La entityClazz è nulla");
            return;
        }
        if (textService.isEmpty(propertyName)) {
            System.out.println("La propertyName è nulla");
            return;
        }

        try {
            listaBean = service.fetch(clazz, propertyName, propertyValue, sort);
            printLista(listaBean);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
    }


    @ParameterizedTest
    @MethodSource(value = "CLAZZ_KEY_ID")
    @Order(60)
    @DisplayName("60 - find (gSon)")
        //--clazz
        //--propertyValue
        //--doc e/o entityBean valida
    void findGson(final Class clazz, final Serializable propertyValue, final boolean risultatoEsatto) {
        System.out.println("60 - find (gSon)");
        FlowVar.typeSerializing = AETypeSerializing.gson;
        find(clazz, propertyValue, risultatoEsatto, "filter");
    }

    @ParameterizedTest
    @MethodSource(value = "CLAZZ_KEY_ID")
    @Order(61)
    @DisplayName("61 - find (spring)")
        //--clazz
        //--propertyValue
        //--doc e/o entityBean valida
    void findSpring(final Class clazz, final Serializable propertyValue, final boolean entityValida) {
        System.out.println("61 - find (spring)");
        FlowVar.typeSerializing = AETypeSerializing.spring;
        find(clazz, propertyValue, entityValida, "query");
    }

    void find(final Class clazz, final Serializable propertyValue, final boolean entityValida, final String tag) {
        String message;
        String key = "keyID";
        message = String.format("Ricerca di una entity nella classe %s tramite %s", getSimpleName(clazz), key);
        System.out.println(message);
        String propertyValueVideo = getPropertyVideo(propertyValue);
        message = String.format("%s%s%s=%s", textService.primaMaiuscola(tag), FORWARD, key, propertyValueVideo);
        System.out.println(message);

        entityBean = null;
        try {
            entityBean = service.find(clazz, propertyValue);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }

        System.out.println(VUOTA);
        if (entityValida) {
            Assertions.assertNotNull(entityBean);
            if (clazz.isAssignableFrom(Preferenza.class)) {
                ((Preferenza) entityBean).value = new byte[0];
            }
            printEntityBeanFromKeyId(clazz, propertyValue, entityBean, 0);
        }
        else {
            Assertions.assertNull(entityBean);
            if (clazz != null) {
                System.out.println(String.format("Nella classe %s non esiste una entity con keyID=%s", clazz.getSimpleName(), propertyValue));
            }
        }
    }


    private Map<String, AFiltro> getEsempioMappaFiltro() {
        Map<String, AFiltro> mappa = new HashMap<>();
        AFiltro filtro;
        String message;
        //--clazz
        //--typeFilter
        //--propertyName
        //--propertyValue
        //--previstoIntero
        Class clazz = null;
        AETypeFilter filter;
        String propertyName;
        Serializable propertyValue;
        int previstoIntero;
        PREVISTO_ESEMPIO_INCROCIATO = 2;
        Object[] parti;

        for (Object obj : CLAZZ_FILTER().toArray()) {
            if (obj instanceof Arguments) {
                parti = ((Arguments) obj).get();
                clazz = (Class) parti[0];
                filter = (AETypeFilter) parti[1];
                propertyName = (String) parti[2];
                propertyValue = (Serializable) parti[3];
                previstoIntero = (Integer) parti[4];

                if (parti[0] instanceof Class && (Class) parti[0] == CLASSE_ESEMPIO_INCROCIATO) {

                    //esempio di 'uguale'
                    if (previstoIntero == 7) {
                        try {
                            filtro = creaFiltro(clazz, filter, propertyName, propertyValue);
                            mappa.put(propertyName, filtro);
                        } catch (AlgosException unErrore) {
                            printError(unErrore);
                        }
                    }

                    //esempio di 'inizia'
                    if (previstoIntero == 2) {
                        try {
                            filtro = creaFiltro(clazz, filter, propertyName, propertyValue);
                            mappa.put(propertyName, filtro);
                        } catch (AlgosException unErrore) {
                            printError(unErrore);
                        }
                    }
                }
            }
        }

        if (mappa != null && mappa.size() > 0) {
            return mappa;
        }
        else {
            message = String.format("Nella entityClass %s non ho trovato nessuna entities col filtro indicato", clazz.getSimpleName());
            System.out.println(message);
            return null;
        }
    }

    @Test
    @Order(70)
    @DisplayName("70 - Fetch one (gson)")
    void fetchOneGson() {
        System.out.println("70 - Fetch one (gson)");
        FlowVar.typeSerializing = AETypeSerializing.gson;
        fetchOne();
    }

    @Test
    @Order(71)
    @DisplayName("71 - Fetch one (spring)")
    void fetchOneSpring() {
        System.out.println("71 - Fetch one (spring)");
        FlowVar.typeSerializing = AETypeSerializing.spring;
        fetchOne();
    }

    void fetchOne() {
        clazz = Mese.class;
        previstoIntero = 12;
        int offset = 0;
        int limit = 0;
        System.out.println(VUOTA);

        try {
            listaBean = service.fetch(clazz);
            assertNotNull(listaBean);
            ottenutoIntero = listaBean.size();
            System.out.println(String.format("Nella collezione %s ho trovato %d entities", clazz.getSimpleName(), ottenutoIntero));
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }

        previstoIntero = 12;
        try {
            listaBean = service.fetch(clazz, null, offset, limit);
            assertNotNull(listaBean);
            ottenutoIntero = listaBean.size();
            System.out.println(String.format("Nella collezione %s ho trovato %d entities con offset=%d e limit=%d", clazz.getSimpleName(), ottenutoIntero, offset, limit));
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }

        limit = 1;
        previstoIntero = 1;
        try {
            listaBean = service.fetch(clazz, null, offset, limit);
            assertNotNull(listaBean);
            ottenutoIntero = listaBean.size();
            System.out.println(String.format("Nella collezione %s ho trovato %d entities con offset=%d e limit=%d", clazz.getSimpleName(), ottenutoIntero, offset, limit));
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }

        limit = 2;
        previstoIntero = 2;
        try {
            listaBean = service.fetch(clazz, null, offset, limit);
            assertNotNull(listaBean);
            ottenutoIntero = listaBean.size();
            System.out.println(String.format("Nella collezione %s ho trovato %d entities con offset=%d e limit=%d", clazz.getSimpleName(), ottenutoIntero, offset, limit));
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }

        offset = 4;
        limit = 2;
        previstoIntero = 2;
        try {
            listaBean = service.fetch(clazz, null, offset, limit);
            assertNotNull(listaBean);
            ottenutoIntero = listaBean.size();
            System.out.println(String.format("Nella collezione %s ho trovato %d entities con offset=%d e limit=%d", clazz.getSimpleName(), ottenutoIntero, offset, limit));
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
    }

    //    @Test
    //    @Order(13)
    //    @DisplayName("13 - Save base di una entity")
    //    void save() {
    //        System.out.println("13 - Save base di una entity");
    //        Company company = null;
    //        Company companyReborn = null;
    //
    //        //--costruisco una entityBean
    //        sorgente = "rot";
    //        sorgente2 = "Porta Valori Associato";
    //        company = companyService.newEntity(sorgente, sorgente2, VUOTA, VUOTA);
    //        //        company.setCreazione(LocalDateTime.now());
    //        assertNotNull(company);
    //
    //        ObjectMapper mapper = new ObjectMapper();
    //        String json;
    //        try {
    //            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
    //            mapper.setDateFormat(format);
    //            json = mapper.writeValueAsString(company);
    //            System.out.println(json);
    //        } catch (JsonProcessingException unErrore) {
    //            System.out.println(unErrore);
    //        }
    //
    //        //        collection.insertOne(Document.parse(json));
    //
    //        //        --salvo la entityBean
    //        try {
    //            //            ((MongoService) service).save(company);
    //            companyService.save(company);
    //        } catch (AMongoException unErrore) {
    //            System.out.println(unErrore);
    //        }
    //    }
    //

    //
    //    //    @Test
    //    @Order(12)
    //    @DisplayName("12 - findId")
    //    void findId() {
    //        System.out.println("find rimanda a findById");
    //        System.out.println(VUOTA);
    //
    //        try {
    //            entityBean = service.find(Mese.class, "brumaio");
    //        } catch (Exception unErrore) {
    //        }
    //        Assert.assertNull(entityBean);
    //
    //        previsto = "ottobre";
    //        try {
    //            entityBean = service.find(Mese.class, "ottobre");
    //        } catch (Exception unErrore) {
    //        }
    //        Assert.assertNotNull(entityBean);
    //        Assert.assertEquals(((Mese) entityBean).mese, previsto);
    //        System.out.println(entityBean);
    //    }
    //
    //
    //    //    @Test
    //    @Order(13)
    //    @DisplayName("13 - findById")
    //    void findById() {
    //        System.out.println("singola entity recuperata da keyID");
    //        System.out.println(VUOTA);
    //
    //        entityBean = service.findByIdOld(Mese.class, "brumaio");
    //        Assert.assertNull(entityBean);
    //
    //        previsto = "ottobre";
    //        entityBean = service.findByIdOld(Mese.class, "ottobre");
    //        Assert.assertNotNull(entityBean);
    //        Assert.assertEquals(((Mese) entityBean).mese, previsto);
    //        System.out.println(entityBean);
    //    }
    //
    //
    //    //    @Test
    //    @Order(14)
    //    @DisplayName("14 - findByKey")
    //    void findByKey() {
    //        System.out.println("singola entity recuperata da keyID");
    //        System.out.println(VUOTA);
    //
    //        entityBean = service.findByKeyOld(Mese.class, "brumaio");
    //        Assert.assertNull(entityBean);
    //
    //        previsto = "ottobre";
    //        entityBean = service.findByKeyOld(Mese.class, "ottobre");
    //        Assert.assertNotNull(entityBean);
    //        Assert.assertEquals(((Mese) entityBean).mese, previsto);
    //        System.out.println(entityBean);
    //    }
    //
    //

    //
    //    //    @Test
    //    //    @Order(15)
    //    //    @DisplayName("findEntity")
    //    //    void findEntity() {
    //    //        System.out.println("singola entity");
    //    //        entityBean = service.find((AEntity) null);
    //    //
    //    //        roleQuattro = roleLogic.newEntity("Zeta");
    //    //        roleQuattro.ordine = 88;
    //    //        roleQuattro.id = "mario";
    //    //        Assert.assertNotNull(roleQuattro);
    //    //        entityBean = service.find(roleQuattro);
    //    //        Assert.assertNull(entityBean);
    //    //
    //    //        previsto = "febbraio";
    //    //        query = new Query();
    //    //        query.addCriteria(Criteria.where("giorni").is(28));
    //    //        entityBean = service.findOneUnique(Mese.class, query);
    //    //        Assert.assertNotNull(entityBean);
    //    //        Assert.assertEquals(previsto, ((Mese) entityBean).getNome());
    //    //
    //    //        Mese mese = (Mese) service.find(entityBean);
    //    //        Assert.assertNotNull(mese);
    //    //        Assert.assertEquals(previsto, mese.getNome());
    //    //    }
    //
    //
    //    //    @Test
    ////    @Order(17)
    ////    @DisplayName("17 - getNewOrder")
    ////    void getNewOrder() {
    ////        System.out.println("recupera ordinamento progressivo");
    ////
    ////        sorgenteIntero = service.count(Mese.class);
    ////        previstoIntero = sorgenteIntero + 1;
    ////
    ////        ottenutoIntero = meseService.getNewOrdine();
    ////        Assert.assertEquals(previstoIntero, ottenutoIntero);
    ////    }
    //
    //
    //    //    @Test
    ////    @Order(18)
    ////    @DisplayName("18 - isEsiste")
    ////    void isEsiste() {
    ////        System.out.println("17 - controlla se esiste");
    ////
    ////        try {
    ////            ottenutoBooleano = service.isEsiste((AEntity) null);
    ////        } catch (Exception unErrore) {
    ////        }
    ////        Assert.assertFalse(ottenutoBooleano);
    ////
    ////        previsto = "febbraio";
    ////        query = new Query();
    ////        query.addCriteria(Criteria.where("giorni").is(28));
    ////        entityBean = service.findOneUnique(Mese.class, query);
    ////        Assert.assertNotNull(entityBean);
    ////
    ////        try {
    ////            ottenutoBooleano = service.isEsiste(entityBean);
    ////        } catch (Exception unErrore) {
    ////        }
    ////        Assert.assertTrue(ottenutoBooleano);
    ////
    ////        try {
    ////            ottenutoBooleano = service.isEsiste(Mese.class, "brumaio");
    ////        } catch (Exception unErrore) {
    ////        }
    ////        Assert.assertFalse(ottenutoBooleano);
    ////
    ////        try {
    ////            ottenutoBooleano = service.isEsiste(Mese.class, "marzo");
    ////        } catch (Exception unErrore) {
    ////        }
    ////        Assert.assertTrue(ottenutoBooleano);
    ////    }
    //
    //

    //
    //    //    @Test
    ////    @Order(20)
    ////    @DisplayName("20 - getCollection")
    ////    void pippoz() {
    ////        Gson gson;
    ////        String json;
    ////        ObjectMapper mapper = new ObjectMapper();
    ////        int offset = 0;
    ////        int limit = 12;
    ////        Class<? extends AEntity> clazz = Mese.class;
    ////        AEntity entity;
    ////        String clazzName = clazz.getSimpleName().toLowerCase();
    ////        List<AEntity> lista = new ArrayList();
    ////        Collection<Document> documents = ((MongoService) mongoService).mongoOp.getCollection(clazzName).find().skip(offset).limit(limit).into(new ArrayList());
    ////
    ////        for (Document doc : documents) {
    ////            try {
    ////                gson = new Gson();
    ////                json = gson.toJson(doc);
    ////                System.out.println(json);
    ////                json = json.replace("_id", "id");
    ////                json = json.replace("_class", "note");
    ////                System.out.println(json);
    ////                System.out.println(VUOTA);
    ////                entity = mapper.readValue(json, clazz);
    ////                lista.add(entity);
    ////            } catch (Exception unErrore) {
    ////                System.out.println(unErrore);
    ////            }
    ////        }
    ////
    ////        if (lista != null && lista.size() > 0) {
    ////            for (AEntity bean : lista) {
    ////                System.out.println(bean.id);
    ////            }
    ////        }
    ////
    ////    }
    //
    //    //    @Test
    ////    @Order(21)
    ////    @DisplayName("21 - execute")
    ////    void execute() {
    ////        String jsonCommand = "db.getCollection('secolo').find({}, {\"_id\":0,\"ordine\": 1})";
    ////        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/" + "vaadflow14");
    ////        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
    ////                .applyConnectionString(connectionString)
    ////                .build();
    ////        MongoClient mongoClient = MongoClients.create(mongoClientSettings);
    ////        MongoDatabase database = mongoClient.getDatabase("vaadflow14");
    ////        MongoCollection collection = database.getCollection("mese");
    ////
    ////        //        BasicDBObject command = new BasicDBObject("find", "mese");
    ////        //        Document alfa = mongoOp.executeCommand(String.valueOf(command));
    ////        //        String gamma = alfa.getString("cursor");
    ////        //        ObjectId beta = alfa.getObjectId("cursor");
    ////        int a = 87;
    ////        //        String jsonCommand = "db.getCollection('secolo').find({}, {\"_id\":0,\"ordine\": 1})";
    ////        //        Object alfga = mongo.mongoOp.executeCommand(jsonCommand);
    ////
    ////    }
    //
    //
    //    //    @Test
    //    @Order(22)
    //    @DisplayName("22 - find next")
    //    void findNext() {
    //        String valuePropertyID = "australia";
    //        previsto = "austria";
    //        Stato statoOttenuto = (Stato) service.findNext(Stato.class, valuePropertyID);
    //        Assert.assertNotNull(statoOttenuto);
    //        assertEquals(previsto, statoOttenuto.id);
    //    }
    //
    //
    //    //    @Test
    //    @Order(23)
    //    @DisplayName("23 - find next ordered")
    //    void findNext2() {
    //        Stato statoOttenuto = null;
    //        String sortProperty;
    //        int sortIndex;
    //        String valueProperty;
    //
    //        sortProperty = "stato";
    //        valueProperty = "Australia";
    //        previsto = "Austria";
    //        statoOttenuto = (Stato) service.findNext(Stato.class, sortProperty, valueProperty);
    //        Assert.assertNotNull(statoOttenuto);
    //        assertEquals(previsto, statoOttenuto.stato);
    //
    //        sortProperty = "ordine";
    //        sortIndex = 40;
    //        previsto = "Azerbaigian";
    //        statoOttenuto = (Stato) service.findNext(Stato.class, sortProperty, sortIndex);
    //        Assert.assertNotNull(statoOttenuto);
    //        assertEquals(previsto, statoOttenuto.stato);
    //    }
    //
    //
    //    //    @Test
    //    @Order(24)
    //    @DisplayName("24 - find previous")
    //    void findPrevious() {
    //        String valuePropertyID = "burkinafaso";
    //        previsto = "bulgaria";
    //        Stato statoOttenuto = (Stato) service.findPrevious(Stato.class, valuePropertyID);
    //        Assert.assertNotNull(statoOttenuto);
    //        assertEquals(previsto, statoOttenuto.id);
    //    }
    //
    //    //    @Test
    //    @Order(25)
    //    @DisplayName("25 - find previous ordered")
    //    void findPrevious2() {
    //        Stato statoOttenuto = null;
    //        String sortProperty;
    //        int sortIndex;
    //        String valueProperty;
    //
    //        sortProperty = "stato";
    //        valueProperty = "Burkina Faso";
    //        previsto = "Bulgaria";
    //        statoOttenuto = (Stato) service.findPrevious(Stato.class, sortProperty, valueProperty);
    //        Assert.assertNotNull(statoOttenuto);
    //        assertEquals(previsto, statoOttenuto.stato);
    //
    //        sortProperty = "ordine";
    //        sortIndex = 57;
    //        previsto = "Brunei";
    //        statoOttenuto = (Stato) service.findPrevious(Stato.class, sortProperty, sortIndex);
    //        Assert.assertNotNull(statoOttenuto);
    //        assertEquals(previsto, statoOttenuto.stato);
    //    }
    //
    //    //    @Test
    //    @Order(26)
    //    @DisplayName("26 - next and previous ordered")
    //    void findOrdered() {
    //        Via viaOttenuta = null;
    //        String sortProperty;
    //        int sortIndex;
    //        String valueProperty;
    //
    //        valueProperty = "largo";
    //        previsto = "lungomare";
    //        viaOttenuta = (Via) service.findNext(Via.class, valueProperty);
    //        Assert.assertNotNull(viaOttenuta);
    //        assertEquals(previsto, viaOttenuta.nome);
    //
    //        sortProperty = "ordine";
    //        valueProperty = "largo";
    //        sortIndex = 2;
    //        previsto = "corso";
    //        viaOttenuta = (Via) service.findNext(Via.class, sortProperty, sortIndex);
    //        Assert.assertNotNull(viaOttenuta);
    //        assertEquals(previsto, viaOttenuta.nome);
    //
    //        sortProperty = "nome";
    //        valueProperty = "largo";
    //        previsto = "lungomare";
    //        viaOttenuta = (Via) service.findNext(Via.class, sortProperty, valueProperty);
    //        Assert.assertNotNull(viaOttenuta);
    //        assertEquals(previsto, viaOttenuta.nome);
    //    }
    //
    //    //    @Test
    //    //    @Order(27)
    //    //    @DisplayName("27 - findSetQuery")
    //    //    void findSetQuery() {
    //    //        int offset = 0;
    //    //        int limit = 12;
    //    //        List<Anno> listaAnni;
    //    //        Class<? extends AEntity> clazz = Anno.class;
    //    //        BasicDBObject query = new BasicDBObject("ordine", new BasicDBObject("$lt", 3000));
    //    //
    //    //        previstoIntero = limit;
    //    //        listaAnni = mongo.findSet(clazz, offset, limit, query, null);
    //    //        Assert.assertNotNull(listaAnni);
    //    //        Assert.assertEquals(previstoIntero, listaAnni.size());
    //    //        System.out.println(VUOTA);
    //    //        System.out.println("Anni (12) minori di 3000");
    //    //        for (Anno anno : listaAnni) {
    //    //            System.out.print(anno.anno + SEP + anno.ordine);
    //    //            System.out.println();
    //    //        }
    //    //
    //    //        BasicDBObject query2 = new BasicDBObject("ordine", new BasicDBObject("$gt", 3000));
    //    //        previstoIntero = limit;
    //    //        listaAnni = mongo.findSet(clazz, offset, limit, query2, null);
    //    //        Assert.assertNotNull(listaAnni);
    //    //        Assert.assertEquals(previstoIntero, listaAnni.size());
    //    //        System.out.println(VUOTA);
    //    //        System.out.println("Anni (12) maggiori di 3000");
    //    //        for (Anno anno : listaAnni) {
    //    //            System.out.print(anno.anno + SEP + anno.ordine);
    //    //            System.out.println();
    //    //        }
    //    //
    //    //        previstoIntero = limit;
    //    //        listaAnni = mongo.findSet(clazz, offset, limit, query, null);
    //    //        Assert.assertNotNull(listaAnni);
    //    //        Assert.assertEquals(previstoIntero, listaAnni.size());
    //    //        System.out.println(VUOTA);
    //    //        System.out.println("Anni (12) minori di 3000");
    //    //        for (Anno anno : listaAnni) {
    //    //            System.out.print(anno.anno + SEP + anno.ordine);
    //    //            System.out.println();
    //    //        }
    //    //    }
    //    //
    //    //    @Test
    //    //    @Order(28)
    //    //    @DisplayName("28 - findSetQuery2")
    //    //    void findSetQuery2() {
    //    //        int offset = 0;
    //    //        int limit = 2000;
    //    //        List<Via> listaVia;
    //    //        Sort.Direction sortDirection;
    //    //        String sortProperty = "nome";
    //    //        Class<? extends AEntity> clazz = Via.class;
    //    //        int totRec = service.count(clazz);
    //    //        Document regexQuery;
    //    //        BasicDBObject sort;
    //    //        previsto = "banchi";
    //    //        previsto2 = "vicolo";
    //    //        previsto3 = "via";
    //    //
    //    //        regexQuery = new Document();
    //    //        regexQuery.append("$regex", "^" + Pattern.quote("p") + ".*");
    //    //        BasicDBObject query2 = new BasicDBObject(sortProperty, regexQuery);
    //    //        listaVia = mongo.findSet(clazz, offset, limit, query2, null);
    //    //        assertNotNull(listaVia);
    //    //        printVia(listaVia, "Via iniziano con 'p'");
    //    //
    //    //        regexQuery = new Document();
    //    //        regexQuery.append("$regex", "^" + Pattern.quote("v") + ".*");
    //    //        BasicDBObject query = new BasicDBObject(sortProperty, regexQuery);
    //    //        listaVia = mongo.findSet(clazz, offset, limit, query, null);
    //    //        assertNotNull(listaVia);
    //    //        printVia(listaVia, "Via iniziano con 'v'");
    //    //
    //    //        sort = new BasicDBObject(sortProperty, 1);
    //    //        listaVia = mongo.findSet(clazz, offset, limit, query, sort);
    //    //        assertNotNull(listaVia);
    //    //        printVia(listaVia, "Via iniziano con 'v' ascendenti");
    //    //
    //    //        sort = new BasicDBObject(sortProperty, -1);
    //    //        listaVia = mongo.findSet(clazz, offset, limit, query, sort);
    //    //        assertNotNull(listaVia);
    //    //        printVia(listaVia, "Via iniziano con 'v' discendenti");
    //    //
    //    //        sort = new BasicDBObject(sortProperty, 1);
    //    //        listaVia = mongo.findSet(clazz, offset, limit, null, sort);
    //    //        assertNotNull(listaVia);
    //    //        assertEquals(totRec, listaVia.size());
    //    //        assertEquals(previsto, listaVia.get(0).nome);
    //    //        assertEquals(previsto2, listaVia.get(listaVia.size() - 1).nome);
    //    //        printVia(listaVia, "Via tutte ascendenti - query nulla");
    //    //
    //    //        sort = new BasicDBObject(sortProperty, -1);
    //    //        listaVia = mongo.findSet(clazz, offset, limit, null, sort);
    //    //        assertNotNull(listaVia);
    //    //        assertEquals(totRec, listaVia.size());
    //    //        assertEquals(previsto2, listaVia.get(0).nome);
    //    //        assertEquals(previsto, listaVia.get(listaVia.size() - 1).nome);
    //    //        printVia(listaVia, "Via tutte discendenti - query nulla");
    //    //
    //    //        listaVia = mongo.findSet(clazz, offset, limit, null, null);
    //    //        assertNotNull(listaVia);
    //    //        assertEquals(totRec, listaVia.size());
    //    //        assertEquals(previsto3, listaVia.get(0).nome);
    //    //        printVia(listaVia, "Via tutte ascendenti- query e sort nulli");
    //    //
    //    //        listaVia = mongo.findSet(clazz, offset, limit);
    //    //        assertNotNull(listaVia);
    //    //        assertEquals(totRec, listaVia.size());
    //    //        assertEquals(previsto3, listaVia.get(0).nome);
    //    //        printVia(listaVia, "Via tutte ascendenti- parametri query e sort non presenti");
    //    //    }
    //
    //
    //    //    @Test
    //    @Order(94)
    //    @DisplayName("insertConKey")
    //    void insertConKey() {
    //        //        System.out.println("inserimento di una nuova entity con keyID - controlla le properties uniche");
    //        //        roleQuattro = roleLogic.newEntity("Zeta");
    //        //        roleQuattro.id = "mario";
    //        //        roleQuattro.ordine = 88;
    //        //        Assert.assertNotNull(roleQuattro);
    //        //        roleCinque = (Role) roleLogic.insert(roleQuattro);
    //        //        Assert.assertNotNull(roleCinque);
    //        //        System.out.println(roleCinque);
    //        //
    //        //        roleQuattro = roleLogic.newEntity("Pippo");
    //        //        roleQuattro.id = "francesco";
    //        //        roleQuattro.ordine = 88;
    //        //        Assert.assertNotNull(roleQuattro);
    //        //        roleCinque = (Role) roleLogic.insert(roleQuattro);
    //        //        Assert.assertNull(roleCinque);
    //        //        service.delete(roleQuattro);
    //    }
    //
    //
    //    //    @Test
    //    @Order(95)
    //    @DisplayName("save")
    //    void save2() {
    //        //        System.out.println("modifica di una entity esistente");
    //        //        roleUno.ordine = 345;
    //        //        roleQuattro = (Role) service.save(roleUno);
    //        //        Assert.assertNotNull(roleQuattro);
    //        //
    //        //        roleCinque = roleLogic.newEntity("finestra");
    //        //        roleCinque.ordine = 87;
    //        //        roleQuattro = (Role) service.save(roleCinque);
    //        //        Assert.assertNotNull(roleQuattro);
    //        //
    //        //        roleCinque = roleLogic.newEntity("porta");
    //        //        roleCinque.ordine = 18;
    //        //        roleQuattro = (Role) service.save(roleCinque);
    //        //        Assert.assertNull(roleQuattro);
    //    }
    //
    //
    //    //    @Test
    //    @Order(99)
    //    @DisplayName("insert")
    //    void insert() {
    //        //        System.out.println("inserimento di una nuova entity senza keyID - controlla le properties uniche");
    //        //        roleQuattro = roleLogic.newEntity("Zeta");
    //        //        roleQuattro.ordine = 88;
    //        //        Assert.assertNotNull(roleQuattro);
    //        //        roleCinque = (Role) roleLogic.insert(roleQuattro);
    //        //        Assert.assertNotNull(roleCinque);
    //        //        System.out.println(roleCinque);
    //        //
    //        //        roleQuattro = roleLogic.newEntity("Pippo");
    //        //        roleQuattro.ordine = 88;
    //        //        Assert.assertNotNull(roleQuattro);
    //        //        roleCinque = (Role) roleLogic.insert(roleQuattro);
    //        //        Assert.assertNull(roleCinque);
    //        //        service.delete(Role.class, new Query(Criteria.where("code").is("Zeta")));
    //    }
    //
    //
    //    //    @Test
    ////    @Order(173)
    ////    @DisplayName("173 - tempiCount")
    ////    void tempiCount() {
    ////        long numRec = 0;
    ////        Query query = new Query();
    ////        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/" + "vaadflow14");
    ////        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
    ////                .applyConnectionString(connectionString)
    ////                .build();
    ////        MongoClient mongoClient = MongoClients.create(mongoClientSettings);
    ////        MongoDatabase database = mongoClient.getDatabase("vaadflow14");
    ////        MongoCollection collection = database.getCollection("mese");
    ////
    ////        long inizio = System.currentTimeMillis();
    ////        for (int k = 0; k < 1000; k++) {
    ////            service.count(Mese.class);
    ////        }
    ////        long fine = System.currentTimeMillis();
    ////        System.out.println("tempo count mongoService: " + (fine - inizio));
    ////
    ////        inizio = System.currentTimeMillis();
    ////        for (int k = 0; k < 1000; k++) {
    ////            //            mongoOp.count(new Query(), Mese.class);
    ////        }
    ////        fine = System.currentTimeMillis();
    ////        System.out.println("tempo count mongoOP: " + (fine - inizio));
    ////
    ////        inizio = System.currentTimeMillis();
    ////        for (int k = 0; k < 1000; k++) {
    ////            //            mongoOp.count(query, Mese.class);
    ////        }
    ////        fine = System.currentTimeMillis();
    ////        System.out.println("tempo count mongoOP con query: " + (fine - inizio));
    ////
    ////        collection.countDocuments();
    ////        inizio = System.currentTimeMillis();
    ////        for (int k = 0; k < 1000; k++) {
    ////            collection.countDocuments();
    ////        }
    ////        fine = System.currentTimeMillis();
    ////        System.out.println("tempo count mongoClient: " + (fine - inizio));
    ////
    ////        inizio = System.currentTimeMillis();
    ////        for (int k = 0; k < 1000; k++) {
    ////            numRec = service.count(Mese.class);
    ////        }
    ////        fine = System.currentTimeMillis();
    ////        System.out.println("tempo count mongoService new: " + (fine - inizio) + " per " + numRec + " elementi");
    ////    }
    //
    //
    //    //    @Test
    //    @Order(174)
    //    @DisplayName("174 - tempiFindAll")
    //    void tempiFindAll() {
    //        Query query = new Query();
    //        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/" + "vaadflow14");
    //        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
    //                .applyConnectionString(connectionString)
    //                .build();
    //        MongoClient mongoClient = MongoClients.create(mongoClientSettings);
    //        MongoDatabase database = mongoClient.getDatabase("vaadflow14");
    //        MongoCollection collection = database.getCollection("mese");
    //
    //        long inizio = System.currentTimeMillis();
    //        for (int k = 0; k < 1000; k++) {
    //            service.find(Mese.class);
    //        }
    //        long fine = System.currentTimeMillis();
    //        System.out.println("tempo findAll mongoService: " + (fine - inizio));
    //
    //        inizio = System.currentTimeMillis();
    //        for (int k = 0; k < 1000; k++) {
    //            //            mongoOp.find(new Query(), Mese.class);
    //        }
    //        fine = System.currentTimeMillis();
    //        System.out.println("tempo findAll mongoOP: " + (fine - inizio));
    //
    //        inizio = System.currentTimeMillis();
    //        for (int k = 0; k < 1000; k++) {
    //            //            mongoOp.find(query, Mese.class);
    //        }
    //        fine = System.currentTimeMillis();
    //        System.out.println("tempo findAll mongoOP con query : " + (fine - inizio));
    //
    //        inizio = System.currentTimeMillis();
    //        for (int k = 0; k < 1000; k++) {
    //            collection.find().first();
    //        }
    //        fine = System.currentTimeMillis();
    //        System.out.println("tempo findAll mongoClient: " + (fine - inizio));
    //    }
    //
    //
    //    //    @Test
    //    @Order(175)
    //    @DisplayName("174 - tempiFindAll con esclusione di property")
    //    void tempiFindAllExclude() {
    //        int cicli;
    //        Query query = new Query();
    //
    //        cicli = 100;
    //        long inizio = System.currentTimeMillis();
    //        for (int k = 0; k < cicli; k++) {
    //            listaBean = service.findAll(Stato.class);
    //        }
    //        long fine = System.currentTimeMillis();
    //        Assert.assertNotNull(listaBean);
    //        Assert.assertTrue(listaBean.size() == 249);
    //        System.out.println(VUOTA);
    //        System.out.println("tempo findAll Stato completo per " + cicli + " cicli: " + (fine - inizio));
    //
    //        query.fields().exclude("bandiera");
    //        inizio = System.currentTimeMillis();
    //        for (int k = 0; k < cicli; k++) {
    //            listaBean = service.findAll(Stato.class, query);
    //        }
    //        fine = System.currentTimeMillis();
    //        Assert.assertNotNull(listaBean);
    //        Assert.assertTrue(listaBean.size() == 249);
    //        System.out.println("tempo findAll Stato senza bandiere per " + cicli + " cicli: " + (fine - inizio));
    //
    //        cicli = 10;
    //        inizio = System.currentTimeMillis();
    //        for (int k = 0; k < cicli; k++) {
    //            listaBean = service.findAll(Anno.class);
    //        }
    //        fine = System.currentTimeMillis();
    //        Assert.assertNotNull(listaBean);
    //        Assert.assertTrue(listaBean.size() == 3030);
    //        System.out.println("tempo findAll Anno completo per " + cicli + " cicli: " + (fine - inizio));
    //
    //        query = new Query();
    //        query.fields().include("bisestile");
    //        inizio = System.currentTimeMillis();
    //        for (int k = 0; k < cicli; k++) {
    //            listaBean = service.findAll(Anno.class, query);
    //        }
    //        fine = System.currentTimeMillis();
    //        Assert.assertNotNull(listaBean);
    //        Assert.assertTrue(listaBean.size() == 3030);
    //        System.out.println("tempo findAll Anno con solo property 'bisestile' per " + cicli + " cicli: " + (fine - inizio));
    //    }
    //
    //
    //    /**
    //     * Qui passa al termine di ogni singolo test <br>
    //     */
    //    @AfterEach
    //    void tearDown() {
    //    }
    //
    //
    //    /**
    //     * Qui passa una volta sola, chiamato alla fine di tutti i tests <br>
    //     */
    //    @AfterAll
    //    void tearDownAll() {
    //        cancellazioneEntitiesProvvisorie();
    //    }
    //
    //
    //    private void printLista(List<AEntity> lista) {
    //        printLista(lista, VUOTA);
    //    }
    //
    //    private void print(List<AEntity> lista, String titolo) {
    //        System.out.println(titolo);
    //        for (AEntity bean : lista) {
    //            System.out.print(bean);
    //            System.out.println();
    //        }
    //    }
    //
    //
    //    private void printLista(List<AEntity> lista, String message) {
    //        int cont = 0;
    //
    //        if (lista != null) {
    //            if (textService.isValid(message)) {
    //                System.out.println(VUOTA);
    //                System.out.println(message);
    //            }
    //
    //            for (AEntity entityBean : lista) {
    //                System.out.print(Integer.toString(++cont) + SEP + entityBean);
    //                if (entityBean instanceof Mese) {
    //                    System.out.print(SEP + ((Mese) entityBean).ordine + SEP + ((Mese) entityBean).mese);
    //                }
    //                System.out.println();
    //            }
    //        }
    //    }
    //
    //
    //    /**
    //     * Creazioni di servizio per essere sicuri che ci siano tutti i files/directories richiesti <br>
    //     */
    //    private void creazioneInizialeEntitiesProvvisorie() {
    //        //        roleUno = roleLogic.newEntity("Alfa");
    //        //        roleUno.ordine = 17;
    //        //        service.insert(roleUno);
    //        //
    //        //        roleDue = roleLogic.newEntity("Beta");
    //        //        roleDue.ordine = 18;
    //        //        service.insert(roleDue);
    //        //
    //        //        roleTre = roleLogic.newEntity("Gamma");
    //        //        roleTre.ordine = 19;
    //        //        service.insert(roleTre);
    //    }

    private AFiltro creaFiltro(final Class clazz, final AETypeFilter filter, final String propertyName, final Serializable propertyValue) throws AlgosException {
        AFiltro filtro = null;

        filtro = new AFiltro(clazz, filter, propertyName, propertyValue);
        filtro.text = textService;
        filtro.annotation = annotationService;
        filtro.reflection = reflectionService;
        filtro.regola();

        return filtro;
    }

    private String getSimpleName(final Class clazz) {
        return clazz != null ? clazz.getSimpleName() : "(manca la classe)";
    }

}