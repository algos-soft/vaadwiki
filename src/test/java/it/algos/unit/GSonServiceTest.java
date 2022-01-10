package it.algos.unit;

import com.mongodb.*;
import com.mongodb.client.*;
import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.packages.anagrafica.via.*;
import it.algos.vaadflow14.backend.packages.crono.anno.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadflow14.backend.service.*;
import org.bson.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.io.*;
import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mar, 17-ago-2021
 * Time: 19:49
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("Gson service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GsonServiceTest extends MongoTest {

    private static final String DATA_BASE_NAME = "vaadflow14";

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    private GsonService service;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        service = gSonService;
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
    @DisplayName("1 - countGraffe")
    void countGraffe() {
        sorgente = "Forse domani ]] me ne vado [[ davvero [[ e forse";
        previstoIntero = 0;
        ottenutoIntero = service.countGraffe(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "Forse domani ]] me ne vado { davvero [[ e forse { no { mercoledì";
        previstoIntero = -1;
        ottenutoIntero = service.countGraffe(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "Forse domani ]] me ne [ vado { davvero [[ e [forse } no } mercoledì}}";
        previstoIntero = -1;
        ottenutoIntero = service.countGraffe(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "Forse {domani ]] me ne [ vado davvero [[ e [forse no } mercoledì";
        previstoIntero = 1;
        ottenutoIntero = service.countGraffe(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "Forse {domani ]] me ne [ vado { davvero [[ e [forse } no } mercoledì";
        previstoIntero = 2;
        ottenutoIntero = service.countGraffe(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);
    }

    @Test
    @Order(2)
    @DisplayName("2 - estraeGraffa")
    void estraeGraffa() {
        sorgente = VUOTA;
        ottenuto = service.estraeGraffa(sorgente);
        assertTrue(textService.isEmpty(ottenuto));

        sorgente = "{\"_id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"reset\":true,\"_class\":\"giorno\"}";
        ottenuto = service.estraeGraffa(sorgente);
        assertTrue(textService.isEmpty(ottenuto));

        sorgente = "{\"_id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"mese\":{\"id\":\"gennaio\",\"collectionName\":\"mese\"},\"reset\":true,\"_class\":\"giorno\"}";
        previsto = "\"mese\":{\"id\":\"gennaio\",\"collectionName\":\"mese\"}";
        ottenuto = service.estraeGraffa(sorgente);
        assertEquals(previsto, ottenuto);
    }


    @Test
    @Order(3)
    @DisplayName("3 - eliminaGraffa")
    void eliminaGraffa() {
        sorgente = VUOTA;
        ottenuto = service.eliminaGraffa(sorgente);
        assertTrue(textService.isEmpty(ottenuto));

        sorgente = "{\"_id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"reset\":true,\"_class\":\"giorno\"}";
        previsto = "{\"_id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"reset\":true,\"_class\":\"giorno\"}";
        ottenuto = service.eliminaGraffa(sorgente);
        assertEquals(previsto, ottenuto);

        sorgente = "{\"_id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"mese\":{\"_id\":\"gennaio\",\"collectionName\":\"mese\"},\"reset\":true,\"_class\":\"giorno\"}";
        previsto = "{\"_id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"reset\":true,\"_class\":\"giorno\"}";
        ottenuto = service.eliminaGraffa(sorgente);
        assertEquals(previsto, ottenuto);
    }


    @Test
    @Order(4)
    @DisplayName("4 - estraeGraffe (nessuna)")
    void estraeGraffe() {
        System.out.println("4 - estraeGraffe (nessuna)");

        System.out.println(VUOTA);
        System.out.println("sorgente nullo, array nullo");
        sorgente = VUOTA;
        ottenutoArray = service.estraeGraffe(sorgente);
        assertNull(ottenutoArray);

        System.out.println(VUOTA);
        System.out.println("array di un solo elemento col testo originale completo");
        sorgente = "{\"_id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"reset\":true,\"_class\":\"giorno\"}";
        previstoIntero = 1;
        previstoArray = arrayService.creaArraySingolo(sorgente);
        ottenutoArray = service.estraeGraffe(sorgente);
        assertEquals(previstoIntero, ottenutoArray.size());
        assertEquals(previstoArray, ottenutoArray);
        print(ottenutoArray);
    }


    @Test
    @Order(5)
    @DisplayName("5 - estraeGraffe (una)")
    void estraeGraffe1() {
        System.out.println("5 - estraeGraffe (una)");
        System.out.println(VUOTA);
        System.out.println("array di due elementi col testo senza graffe nel primo e il contenuto interno della graffa nel secondo");

        sorgente = "{\"_id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"mese\":{\"_id\":\"gennaio\",\"collectionName\":\"mese\"},\"reset\":true,\"_class\":\"giorno\"}";
        previstoIntero = 2;
        previstoArray = new ArrayList<>();
        previstoArray.add("{\"_id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"reset\":true,\"_class\":\"giorno\"}");
        previstoArray.add("\"mese\":{\"_id\":\"gennaio\",\"collectionName\":\"mese\"}");
        ottenutoArray = service.estraeGraffe(sorgente);
        assertEquals(previstoIntero, ottenutoArray.size());
        assertEquals(previstoArray, ottenutoArray);
        print(ottenutoArray);
    }

    @Test
    @Order(6)
    @DisplayName("6 - estraeGraffe (due)")
    void estraeGraffe2() {
        System.out.println("6 - estraeGraffe (due)");
        System.out.println(VUOTA);
        System.out.println("array di tre elementi col testo senza graffe nel primo e i contenuti interni delle due graffe nel secondo e nel terzo");

        sorgente = "{\"_id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"mese\":{\"_id\":\"gennaio\",\"collectionName\":\"mese\"},\"reset\":true,\"anno\":{\"_id\":\"1876\",\"collectionName\":\"anno\"},\"_class\":\"giorno\"}";
        previstoIntero = 3;
        previstoArray = new ArrayList<>();
        previstoArray.add("{\"_id\":\"5gennaio\",\"ordine\":5,\"titolo\":\"5 gennaio\",\"reset\":true,\"_class\":\"giorno\"}");
        previstoArray.add("\"mese\":{\"_id\":\"gennaio\",\"collectionName\":\"mese\"}");
        previstoArray.add("\"anno\":{\"_id\":\"1876\",\"collectionName\":\"anno\"}");
        ottenutoArray = service.estraeGraffe(sorgente);
        assertEquals(previstoIntero, ottenutoArray.size());
        assertEquals(previstoArray, ottenutoArray);
        print(ottenutoArray);
    }


    @Test
    @Order(7)
    @DisplayName("7 - crea una entityBean da un testo jSon di mongoDB")
    void crea() {
        System.out.println("7 - crea una entityBean da un testo jSon di mongoDB");
        System.out.println(VUOTA);
        FlowVar.typeSerializing = AETypeSerializing.gson;
        String mongoToString = VUOTA;
        String entityToString;
        AEntity entityFromMongoString;
        AEntity entityFromEntityString;

        sorgente = "piazza";
        clazz = Via.class;

        try {
            entityBean = mongoService.find(clazz, sorgente);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        assertNotNull(entityBean);

        try {
            mongoToString = service.mongoToString(clazz, sorgente);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        entityToString = service.entityToString(entityBean);

        System.out.println(VUOTA);
        System.out.println(String.format("mongoToString: %s", mongoToString));
        System.out.println(String.format("entityToString: %s", entityToString));
        System.out.println(VUOTA);

        //        entityFromMongoString = service.stringToEntity(clazz, mongoToString);
        //        entityFromEntityString = service.stringToEntity(clazz, entityToString);
        //        assertNotNull(entityFromMongoString);
        //        assertNotNull(entityFromEntityString);

        sorgente = "5gennaio";
        clazz = Giorno.class;
        try {
            ottenuto = service.mongoToString(clazz, sorgente);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        try {
            entityBean = service.stringToEntity(clazz, ottenuto);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        assertNotNull(entityBean);
        System.out.println(VUOTA);
        System.out.println(String.format("Creazione di un bean di classe %s", clazz.getSimpleName()));
        System.out.println(entityBean);

        sorgente = "1786";
        clazz = Anno.class;
        try {
            ottenuto = service.mongoToString(clazz, sorgente);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        try {
            entityBean = service.stringToEntity(clazz, ottenuto);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        assertNotNull(entityBean);
        System.out.println(VUOTA);
        System.out.println(String.format("Creazione di un bean di classe %s", clazz.getSimpleName()));
        System.out.println(entityBean);

        sorgente = "5gennaio";
        clazz = Giorno.class;
        try {
            entityBean = mongoService.find(clazz, sorgente);
        } catch (Exception unErrore) {
        }
        ottenuto = service.entityToString(entityBean);

        try {
            entityBean = service.stringToEntity(clazz, ottenuto);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        assertNotNull(entityBean);
        System.out.println(VUOTA);
        System.out.println(String.format("Creazione di un bean di classe %s", clazz.getSimpleName()));
        System.out.println(entityBean);
    }

    @ParameterizedTest
    @MethodSource(value = "CLAZZ_KEY_ID")
    @Order(8)
    @DisplayName("8 - Crea una entityBean (da mongoDB) tramite keyId con gson")
    /*
      8 - Crea una entityBean (da mongoDB) tramite keyId con gson
    */
    void creaIdGson(final Class clazz, final Serializable keyPropertyValue) {
        FlowVar.typeSerializing = AETypeSerializing.gson;
        try {
            entityBean = service.creaId(clazz, keyPropertyValue);
        } catch (AlgosException unErrore) {
            printError(unErrore);
            return;
        }
        printEntityBeanFromClazz((String) keyPropertyValue, clazz, entityBean);
    }

    @ParameterizedTest
    @MethodSource(value = "CLAZZ_KEY_ID")
    @Order(9)
    @DisplayName("9 - Crea una entityBean (da mongoDB) tramite keyId con spring")
    /*
      9 - Crea una entityBean (da mongoDB) tramite keyId con spring
    */
    void creaIdSpring(final Class clazz, final Serializable keyPropertyValue) {
        FlowVar.typeSerializing = AETypeSerializing.spring;
        try {
            entityBean = service.creaId(clazz, keyPropertyValue);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        printEntityBeanFromClazz((String) keyPropertyValue, clazz, entityBean);
    }

    @ParameterizedTest
    @MethodSource(value = "CLAZZ_PROPERTY")
    @Order(10)
    @DisplayName("10 - Crea una entityBean (da mongoDB) tramite property=value con gson")
    /*
      10 - Crea una entityBean (da mongoDB) tramite tramite property=value con gson
    */
    void creaPropertyGson(final Class clazz, final String propertyName, final Serializable propertyValue, final int previstoIntero) {
        FlowVar.typeSerializing = AETypeSerializing.gson;
        try {
            entityBean = service.creaProperty(clazz, propertyName, propertyValue);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        printEntityBeanFromProperty(clazz, propertyName, propertyValue, entityBean, previstoIntero);
    }


    @ParameterizedTest
    @MethodSource(value = "CLAZZ_PROPERTY")
    @Order(11)
    @DisplayName("11 - Crea una entityBean (da mongoDB) tramite property=value con spring")
    /*
      11 - Crea una entityBean (da mongoDB) tramite tramite property=value con spring
    */
    void creaPropertySpring(final Class clazz, final String propertyName, final Serializable propertyValue, final int previstoIntero) {
        FlowVar.typeSerializing = AETypeSerializing.spring;
        try {
            entityBean = service.creaProperty(clazz, propertyName, propertyValue);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        printEntityBeanFromProperty(clazz, propertyName, propertyValue, entityBean, previstoIntero);
    }


    @Test
    @Order(11)
    @DisplayName("11 - Java object to JSON string")
    void writeValueAsString() {
        System.out.println("11 - Java object to JSON string");

        sorgente = "piazzale";
        clazz = Via.class;
        previsto = "{\"_id\":\"piazzale\",\"ordine\":6,\"nome\":\"piazzale\",\"reset\":true,\"_class\":\"via\"}";
        try {
            entityBean = mongoService.find(clazz, sorgente);
        } catch (Exception unErrore) {
        }
        ottenuto = service.entityToString(entityBean);
        assertTrue(textService.isValid(ottenuto));
        //        assertEquals(previsto, ottenuto);
        System.out.println(ottenuto);

        sorgente = "8marzo";
        clazz = Giorno.class;
        previsto = "{\"_id\":\"8marzo\",\"ordine\":68,\"titolo\":\"8 marzo\",\"mese\":{\"id\":\"marzo\",\"collectionName\":\"mese\"},\"reset\":true,\"_class\":\"giorno\"}";
        try {
            entityBean = mongoService.find(clazz, sorgente);
        } catch (Exception unErrore) {
        }
        ottenuto = service.entityToString(entityBean);
        assertTrue(textService.isValid(ottenuto));
        //        assertEquals(previsto, ottenuto);
        System.out.println(ottenuto);

        sorgente = "23 ottobre";
        clazz = Giorno.class;
        previsto = "{\"_id\":\"23ottobre\",\"ordine\":297,\"titolo\":\"23 ottobre\",\"mese\":{\"id\":\"ottobre\",\"collectionName\":\"mese\"},\"reset\":true,\"_class\":\"giorno\"}";
        try {
            entityBean = mongoService.find(clazz, sorgente);
        } catch (AlgosException unErrore) {
        }
        ottenuto = service.entityToString(entityBean);
        assertTrue(textService.isValid(ottenuto));
        //        assertEquals(previsto, ottenuto);
        System.out.println(ottenuto);
    }

    @Test
    @Order(15)
    @DisplayName("15 - creazione di un testo jSon da mongoDb")
    void legge() {
        System.out.println("15 - creazione di un testo jSon da mongoDb");

        sorgente = "8marzo";
        clazz = Giorno.class;
        previsto = "{\"_id\":\"8marzo\",\"ordine\":68,\"titolo\":\"8 marzo\",\"mese\":{\"id\":\"marzo\",\"collectionName\":\"mese\"},\"inizio\":67,\"fine\":298,\"reset\":true,\"_class\":\"giorno\"}";
        try {
            ottenuto = service.mongoToString(clazz, sorgente);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println(ottenuto);

        sorgente = "23 ottobre";
        clazz = Giorno.class;
        previsto = "{\"_id\":\"23ottobre\",\"ordine\":297,\"titolo\":\"23 ottobre\",\"mese\":{\"id\":\"ottobre\",\"collectionName\":\"mese\"},\"inizio\":296,\"fine\":69,\"reset\":true,\"_class\":\"giorno\"}";
        try {
            ottenuto = service.mongoToString(clazz, sorgente);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        assertTrue(textService.isValid(ottenuto));
        assertEquals(previsto, ottenuto);
        System.out.println(ottenuto);

        sorgente = "quartiere";
        clazz = Via.class;
        previsto = "{\"_id\":\"quartiere\",\"ordine\":11,\"nome\":\"quartiere\",\"reset\":true,\"creazione\":";
        try {
            ottenuto = service.mongoToString(clazz, sorgente);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        assertTrue(textService.isValid(ottenuto));
        assertTrue(ottenuto.startsWith(previsto));
        System.out.println(ottenuto);
    }


    @Test
    @Order(20)
    @DisplayName("20 - legge doc from mongo")
    void leggeDoc() {
        clazz = Via.class;
        sorgente = "quartiere";
        doc = getDoc(clazz, sorgente);
        System.out.println(String.format("Documento ricavato da %s.%s", clazz.getSimpleName(), sorgente));
        printDoc(doc);

//        clazz = Delta.class;
//        sorgente = "uno";
//        doc = getDoc(clazz, sorgente);
//        System.out.println(String.format("Documento ricavato da %s.%s", clazz.getSimpleName(), sorgente));
//        printDoc(doc);
    }


    @Test
    @Order(21)
    @DisplayName("21 - crea entity from doc")
    void creaEntity() {
        clazz = Via.class;
        sorgente = "quartiere";
        doc = getDoc(clazz, sorgente);
        entityBean = service.creaOld(doc, clazz);
        printDoc(doc);
        printMappa(entityBean);

//        clazz = Delta.class;
//        sorgente = "uno";
//        doc = getDoc(clazz, sorgente);
//        entityBean = service.creaOld(doc, clazz);
//        printDoc(doc);
//        printMappa(entityBean);
    }

    @Test
    @Order(31)
    @DisplayName("31 - crea doc from entity")
    void finalexx() {
    }

    @Test
    @Order(37)
    @DisplayName("37 - prove")
    void finale() {
        sorgente = "quartiere";
        clazz = Via.class;

        System.out.println("37 - From mongoDB to string passando da Doc");
        try {
            ottenuto = service.mongoToString(clazz, sorgente);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        System.out.println(ottenuto);

        System.out.println(VUOTA);
        System.out.println("16 - Crea una entity col jsonString appena ottenuto");
        try {
            entityBean = service.stringToEntity(clazz, ottenuto);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        System.out.println(entityBean);

        System.out.println(VUOTA);
        System.out.println("16 - From entityBean to string ");
        try {
            entityBean = mongoService.find(clazz, sorgente);
        } catch (Exception unErrore) {
        }
        ottenuto = service.entityToString(entityBean);
        System.out.println(ottenuto);
        System.out.println("16 - Crea una entity col jsonString appena ottenuto");
        try {
            entityBean = service.stringToEntity(clazz, ottenuto);
        } catch (AlgosException unErrore) {
            printError(unErrore);
        }
        System.out.println(entityBean);
    }

    //    @Test
    //    @Order(98)
    //    @DisplayName("98 - timestamp versus UTC Datetime")
    //    void saveDate() {
    //        Delta delta = Delta.builderDelta().build();
    //        delta.id = "tre";
    //        delta.code = "topolino";
    //        delta.immagine="";
    //        delta.uno = LocalDateTime.now();
    //        delta.quattro = Timestamp.valueOf(LocalDateTime.now());
    //        String jSonText;
    //        Class<? extends AEntity> entityClazz = delta.getClass();
    //        MongoCollection<Document> collection = mongoService.getCollection(entityClazz);
    //
    //        jSonText = service.entityToString(delta);
    //        jSonText = jSonText.replace(FIELD_NAME_ID_SENZA, FIELD_NAME_ID_CON);
    //        doc = textService.isValid(jSonText) ? Document.parse(jSonText) : null;
    //        printDoc(doc);
    //        try {
    //            if (doc!=null) {
    //                collection.insertOne(doc);
    //            }
    //        } catch (Exception unErrore) {
    //            System.out.println(unErrore);
    //        }
    //
    //    }

    private Document getDoc(Class clazz, String sorgente) {
        Document doc = null;
        MongoCollection<Document> collection;
        collection = mongoService.getCollection(clazz);

        if (collection != null) {
            objectQuery = new BasicDBObject();
            objectQuery.put(FIELD_NAME_ID_CON, sorgente);
            doc = collection.find(objectQuery).first();
        }

        return doc;
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