package it.algos.unit;

import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;
import com.google.gson.JsonDeserializer;
import com.google.gson.*;
import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.*;
import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.packages.anagrafica.via.*;
import it.algos.vaadflow14.backend.packages.crono.anno.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadflow14.backend.packages.crono.mese.*;
import it.algos.vaadflow14.backend.packages.crono.secolo.*;
import it.algos.vaadflow14.backend.packages.geografica.regione.*;
import it.algos.vaadflow14.backend.packages.preferenza.*;
import it.algos.vaadflow14.backend.packages.utility.versione.*;
import org.bson.*;
import static org.bson.codecs.configuration.CodecRegistries.*;
import org.bson.codecs.configuration.*;
import org.bson.codecs.pojo.*;
import org.junit.*;
import static org.junit.Assert.assertNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.mockito.*;

import java.lang.reflect.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mer, 21-ott-2020
 * Time: 11:31
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("MongoServiceTest")
@DisplayName("Test di unit")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GSonTest extends ATest {

    private LocalDateTime dataUno = LocalDateTime.now();

    private Gson gSon;

    private MongoClient mongoClient = null;

    private MongoDatabase database = null;

    private String valueID;

    private int offset;

    private int limit;

    private Document doc;

    private Class clazzVia = Via.class;

    private Class clazzAnno = Anno.class;

    private Class clazzGiorno = Giorno.class;

    private Class clazzVersione = Versione.class;

    private Class clazzRegione = Regione.class;

    private MongoCollection collection;

    private MongoCollection collectionVia ;

    private MongoCollection collectionAnno ;

    private MongoCollection collectionGiorno ;

    private MongoCollection collectionVersione;

    private MongoCollection collectionRegione ;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpAll() {
        super.setUpStartUp();

        MockitoAnnotations.initMocks(this);

        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/" + "vaadin14");
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        mongoClient = MongoClients.create(mongoClientSettings);

        database = mongoClient.getDatabase("vaadflow14");
        gSonService.reflection = reflection;
        gSonService.annotation = annotation;
        gSonService.mongo = mongo;
        gSonService.date = date;
        date.text = text;

         collectionVia = database.getCollection(clazzVia.getSimpleName().toLowerCase());
         collectionAnno = database.getCollection(clazzAnno.getSimpleName().toLowerCase());
         collectionGiorno = database.getCollection(clazzGiorno.getSimpleName().toLowerCase());
         collectionVersione = database.getCollection(clazzVersione.getSimpleName().toLowerCase());
         collectionRegione = database.getCollection(clazzRegione.getSimpleName().toLowerCase());

        System.out.println("Fine del setup di mongo");
        System.out.println(VUOTA);
    }


    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();

        gSon = new Gson();
        offset = 0;
        limit = 0;
        collection = null;
        doc = null;
        valueID = VUOTA;
    }


    @Test
    @Order(1)
    @DisplayName("1 - countGraffe")
    void countGraffe() {
        previstoIntero = 0;
        sorgente = "{\"_id\": \"via\", \"ordine\": 1, \"nome\": \"via\", \"_class\": \"via\"}";
        ottenutoIntero = gSonService.countGraffe(sorgente);
        Assert.assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "\"_id\": \"via\", \"ordine\": 1, \"nome\": \"via\", \"_class\": \"via\"";
        ottenutoIntero = gSonService.countGraffe(sorgente);
        Assert.assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "_id\": \"via\", \"ordine\": 1, \"nome\": \"via\", \"_class\": \"via";
        ottenutoIntero = gSonService.countGraffe(sorgente);
        Assert.assertEquals(previstoIntero, ottenutoIntero);

        previstoIntero = 1;
        sorgente = "{\"id\":\"1971\",\"ordine\":3971,\"anno\":\"1971\",\"bisestile\":false,\"secolo\":{\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"_class\":\"anno\"}";
        ottenutoIntero = gSonService.countGraffe(sorgente);
        Assert.assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "\"id\":\"1971\",\"ordine\":3971,\"anno\":\"1971\",\"bisestile\":false,\"secolo\":{\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"_class\":\"anno\"";
        ottenutoIntero = gSonService.countGraffe(sorgente);
        Assert.assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "id\":\"1971\",\"ordine\":3971,\"anno\":\"1971\",\"bisestile\":false,\"secolo\":{\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"_class\":\"anno";
        ottenutoIntero = gSonService.countGraffe(sorgente);
        Assert.assertEquals(previstoIntero, ottenutoIntero);

        previstoIntero = 0;
        sorgente = "{\"id\":\"rest\",\"code\":\"rest\",\"giorno\":\"Dec 31, 2020, 12:00:00 AM\",\"descrizione\":\"pippoz\",\"_class\":\"versione\"}";
        ottenutoIntero = gSonService.countGraffe(sorgente);
        Assert.assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "\"id\":\"rest\",\"code\":\"rest\",\"giorno\":\"Dec 31, 2020, 12:00:00 AM\",\"descrizione\":\"pippoz\",\"_class\":\"versione\"";
        ottenutoIntero = gSonService.countGraffe(sorgente);
        Assert.assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "id\":\"rest\",\"code\":\"rest\",\"giorno\":\"Dec 31, 2020, 12:00:00 AM\",\"descrizione\":\"pippoz\",\"_class\":\"versione";
        ottenutoIntero = gSonService.countGraffe(sorgente);
        Assert.assertEquals(previstoIntero, ottenutoIntero);

        previstoIntero = -1;
        sorgente = "{\"id\":\"1971\",\"secolo\":\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"ordine\":3971,\"anno\":\"1971\",\"bisestile\":false,\"secolo\":{\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"_class\":\"anno\"}";
        ottenutoIntero = gSonService.countGraffe(sorgente);
        Assert.assertEquals(previstoIntero, ottenutoIntero);

        previstoIntero = 2;
        sorgente = "{\"id\":\"1971\",\"secolo\":{\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"ordine\":3971,\"anno\":\"1971\",\"bisestile\":false,\"secolo\":{\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"_class\":\"anno\"}";
        ottenutoIntero = gSonService.countGraffe(sorgente);
        Assert.assertEquals(previstoIntero, ottenutoIntero);
    }


    @Test
    @Order(2)
    @DisplayName("2 - isEsistonoDBRef")
    void isEsistonoDBRef() {
        doc = (Document) collectionVia.find().first();
        ottenutoBooleano = gSonService.isEsistonoDBRef(doc);
        Assert.assertFalse(ottenutoBooleano);

        doc = (Document) collectionAnno.find().first();
        ottenutoBooleano = gSonService.isEsistonoDBRef(doc);
        Assert.assertTrue(ottenutoBooleano);

        doc = (Document) collectionVersione.find().first();
        ottenutoBooleano = gSonService.isEsistonoDBRef(doc);
        Assert.assertFalse(ottenutoBooleano);

    }


    @Test
    @Order(3)
    @DisplayName("3 - estraeGraffa")
    void estraeGraffa() {
        sorgente = "{\"_id\": \"via\", \"ordine\": 1, \"nome\": \"via\", \"_class\": \"via\"}";
        ottenuto = gSonService.estraeGraffa(sorgente);
        Assert.assertNull(ottenuto);

        sorgente = "\"_id\": \"via\", \"ordine\": 1, \"nome\": \"via\", \"_class\": \"via\"";
        ottenuto = gSonService.estraeGraffa(sorgente);
        Assert.assertNull(ottenuto);

        sorgente = "_id\": \"via\", \"ordine\": 1, \"nome\": \"via\", \"_class\": \"via";
        ottenuto = gSonService.estraeGraffa(sorgente);
        Assert.assertNull(ottenuto);

        previsto = "secolo\":{\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"}";
        sorgente = "{\"id\":\"1971\",\"ordine\":3971,\"anno\":\"1971\",\"bisestile\":false,\"secolo\":{\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"_class\":\"anno\"}";
        ottenuto = gSonService.estraeGraffa(sorgente);
        Assert.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, ottenuto);

        sorgente = "\"id\":\"1971\",\"ordine\":3971,\"anno\":\"1971\",\"bisestile\":false,\"secolo\":{\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"_class\":\"anno\"";
        ottenuto = gSonService.estraeGraffa(sorgente);
        Assert.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, ottenuto);

        sorgente = "id\":\"1971\",\"ordine\":3971,\"anno\":\"1971\",\"bisestile\":false,\"secolo\":{\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"_class\":\"anno";
        ottenuto = gSonService.estraeGraffa(sorgente);
        Assert.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, ottenuto);

        sorgente = "{\"id\":\"rest\",\"code\":\"rest\",\"giorno\":\"Dec 31, 2020, 12:00:00 AM\",\"descrizione\":\"pippoz\",\"_class\":\"versione\"}";
        ottenuto = gSonService.estraeGraffa(sorgente);
        Assert.assertNull(ottenuto);

        sorgente = "\"id\":\"rest\",\"code\":\"rest\",\"giorno\":\"Dec 31, 2020, 12:00:00 AM\",\"descrizione\":\"pippoz\",\"_class\":\"versione\"";
        ottenuto = gSonService.estraeGraffa(sorgente);
        Assert.assertNull(ottenuto);

        sorgente = "id\":\"rest\",\"code\":\"rest\",\"giorno\":\"Dec 31, 2020, 12:00:00 AM\",\"descrizione\":\"pippoz\",\"_class\":\"versione";
        ottenuto = gSonService.estraeGraffa(sorgente);
        Assert.assertNull(ottenuto);

        sorgente = "{\"id\":\"1971\",\"secolo\":\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"ordine\":3971,\"anno\":\"1971\",\"bisestile\":false,\"secolo\":{\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"_class\":\"anno\"}";
        ottenuto = gSonService.estraeGraffa(sorgente);
        Assert.assertNull(ottenuto);

        previsto = "secolo\":{\"id\":\"xsecolo\",\"collectionName\":\"secolo\"}";
        sorgente = "{\"id\":\"1971\",\"secolo\":{\"id\":\"xsecolo\",\"collectionName\":\"secolo\"},\"ordine\":3971,\"anno\":\"1971\",\"bisestile\":false,\"secolo\":{\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"_class\":\"anno\"}";
        ottenuto = gSonService.estraeGraffa(sorgente);
        Assert.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, ottenuto);
    }


    @Test
    @Order(4)
    @DisplayName("4 - estraeGraffe")
    void estraeGraffe() {
        sorgente = "{\"_id\": \"via\", \"ordine\": 1, \"nome\": \"via\", \"_class\": \"via\"}";
        ottenutoArray = gSonService.estraeGraffe(sorgente);
        Assert.assertNull(ottenutoArray);

        sorgente = "\"_id\": \"via\", \"ordine\": 1, \"nome\": \"via\", \"_class\": \"via\"";
        ottenutoArray = gSonService.estraeGraffe(sorgente);
        Assert.assertNull(ottenutoArray);

        sorgente = "_id\": \"via\", \"ordine\": 1, \"nome\": \"via\", \"_class\": \"via";
        Assert.assertNull(ottenutoArray);

        previstoIntero = 2;
        previstoArray = new ArrayList<>();
        previstoArray.add("id\":\"1971\",\"ordine\":3971,\"anno\":\"1971\",\"bisestile\":false,\"_class\":\"anno");
        previstoArray.add("secolo\":{\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"}");
        sorgente = "{\"id\":\"1971\",\"ordine\":3971,\"anno\":\"1971\",\"bisestile\":false,\"secolo\":{\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"_class\":\"anno\"}";
        ottenutoArray = gSonService.estraeGraffe(sorgente);
        Assert.assertNotNull(ottenutoArray);
        Assert.assertEquals(previstoIntero, ottenutoArray.size());
        Assert.assertEquals(previstoArray, ottenutoArray);
        printVuota(ottenutoArray);

        sorgente = "\"id\":\"1971\",\"ordine\":3971,\"anno\":\"1971\",\"bisestile\":false,\"secolo\":{\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"_class\":\"anno\"";
        ottenutoArray = gSonService.estraeGraffe(sorgente);
        Assert.assertNotNull(ottenutoArray);
        Assert.assertEquals(previstoIntero, ottenutoArray.size());

        sorgente = "id\":\"1971\",\"ordine\":3971,\"anno\":\"1971\",\"bisestile\":false,\"secolo\":{\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"_class\":\"anno";
        ottenutoArray = gSonService.estraeGraffe(sorgente);
        Assert.assertNotNull(ottenutoArray);
        Assert.assertEquals(previstoIntero, ottenutoArray.size());

        sorgente = "{\"id\":\"rest\",\"code\":\"rest\",\"giorno\":\"Dec 31, 2020, 12:00:00 AM\",\"descrizione\":\"pippoz\",\"_class\":\"versione\"}";
        ottenutoArray = gSonService.estraeGraffe(sorgente);
        Assert.assertNull(ottenutoArray);

        sorgente = "\"id\":\"rest\",\"code\":\"rest\",\"giorno\":\"Dec 31, 2020, 12:00:00 AM\",\"descrizione\":\"pippoz\",\"_class\":\"versione\"";
        ottenutoArray = gSonService.estraeGraffe(sorgente);
        Assert.assertNull(ottenutoArray);

        sorgente = "id\":\"rest\",\"code\":\"rest\",\"giorno\":\"Dec 31, 2020, 12:00:00 AM\",\"descrizione\":\"pippoz\",\"_class\":\"versione";
        ottenutoArray = gSonService.estraeGraffe(sorgente);
        Assert.assertNull(ottenutoArray);

        sorgente = "{\"id\":\"1971\",\"secolo\":\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"ordine\":3971,\"anno\":\"1971\",\"bisestile\":false,\"secolo\":{\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"_class\":\"anno\"}";
        ottenutoArray = gSonService.estraeGraffe(sorgente);
        Assert.assertNull(ottenutoArray);

        previstoIntero = 2;
        sorgente = "{\"id\":\"1971\",\"secolo\":{\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"ordine\":3971,\"anno\":\"1971\",\"bisestile\":false,\"secolo\":{\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"_class\":\"anno\"}";
        ottenutoArray = gSonService.estraeGraffe(sorgente);
        Assert.assertNotNull(ottenutoArray);
        Assert.assertEquals(previstoIntero, ottenutoArray.size());
        Assert.assertEquals(previstoArray, ottenutoArray);
        printVuota(ottenutoArray);

        sorgente = "{\"id\":\"mario\",\"code\":\"mariolino\",\"secolo\":{\"testo\":\"manca\"},\"descrizione\":\"esiste\",\"mese\":{\"code\":\"mariolino\",\"testo\":\"manca\"},\"code\":\"mariolino\"}";
        previstoIntero = 3;
        previstoArray = new ArrayList<>();
        previstoArray.add("id\":\"mario\",\"code\":\"mariolino\",\"descrizione\":\"esiste\",\"code\":\"mariolino");
        previstoArray.add("secolo\":{\"testo\":\"manca\"}");
        previstoArray.add("mese\":{\"code\":\"mariolino\",\"testo\":\"manca\"}");
        ottenutoArray = gSonService.estraeGraffe(sorgente);
        Assert.assertNotNull(ottenutoArray);
        Assert.assertEquals(previstoIntero, ottenutoArray.size());
        Assert.assertEquals(previstoArray, ottenutoArray);
        printVuota(ottenutoArray);
    }


    @Test
    @Order(5)
    @DisplayName("5 - fixStringa")
    void fixStringa() {
        sorgente = "{\"_id\":\"1971\",\"secolo\":\"_id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"ordine\":3971,\"anno\":\"1971\",\"bisestile\":false,\"secolo\":{\"_id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"_class\":\"anno\"}";
        previsto = "{\"id\":\"1971\",\"secolo\":\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"ordine\":3971,\"anno\":\"1971\",\"bisestile\":false,\"secolo\":{\"id\":\"xxsecolo\",\"collectionName\":\"secolo\"},\"class\":\"anno\"}";

        ottenuto = gSonService.fixStringa(sorgente);
        Assert.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, ottenuto);
    }


    @Test
    @Order(6)
    @DisplayName("6 - fixDoc")
    void fixDoc() {
        doc = (Document) collectionVia.find().first();

        previsto = "{\"id\":\"via\",\"ordine\":1,\"nome\":\"via\",\"class\":\"via\"}";
        ottenuto = gSonService.fixDoc(doc);
        Assert.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, ottenuto);
    }


    @Test
    @Order(7)
    @DisplayName("7 - creaNoDbRef - via - first")
    void creaNoDbRef() {
        previsto = "via";
        doc = (Document) collectionVia.find().first();
        entityBean = gSonService.creaNoDbRef(doc, clazzVia);

        Assert.assertNotNull(entityBean);
        Assert.assertEquals(previsto, entityBean.id);
    }

    @Test
    @Order(8)
    @DisplayName("8 - addAllDBRef - anno - first")
    void addAllDBRef() {
        previsto = "1000a.c.";
        previsto2 = "xsecoloa.c.";

        doc = (Document) collectionAnno.find().first();
        entityBean = gSonService.creaNoDbRef(doc, clazzAnno);
        entityBean = gSonService.addAllDBRef(doc, entityBean);

        Assert.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, entityBean.id);
        Assert.assertEquals(previsto2, ((Anno) entityBean).secolo.id);
    }

    @Test
    @Order(9)
    @DisplayName("9 - creaNoDbRef and time - versione - first")
    void creaNoDbRef2() {
        previsto = "setup";

        doc = (Document) collectionVersione.find().first();
        entityBean = gSonService.creaNoDbRef(doc, clazzVersione);

        Assert.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, entityBean.id);
    }

    @Test
    @Order(10)
    @DisplayName("10 - crea - via - first")
    void creaViaFirst() {
        previsto = "via";

        doc = (Document) collectionVia.find().first();
        entityBean = gSonService.crea(doc, clazzVia);

        Assert.assertNotNull(entityBean);
        Assert.assertEquals(previsto, entityBean.id);
    }

    @Test
    @Order(11)
    @DisplayName("11 - crea - anno - first")
    void creaAnnoFirst() {
        previsto = "1000a.c.";
        previsto2 = "xsecoloa.c.";

        doc = (Document) collectionAnno.find().first();
        entityBean = gSonService.crea(doc, clazzAnno);

        Assert.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, entityBean.id);
        Assert.assertEquals(previsto2, ((Anno) entityBean).secolo.id);
    }

    @Test
    @Order(12)
    @DisplayName("12 - crea - versione - first")
    void creaVersioneFirst() {
        previsto = "setup";

        doc = (Document) collectionVersione.find().first();
        entityBean = gSonService.crea(doc, clazzVersione);

        Assert.assertNotNull(ottenuto);
        Assert.assertEquals(previsto, entityBean.id);
    }


    @Test
    @Order(13)
    @DisplayName("13 - deserializeLocalDateTime")
    void deserializeLocalDateTime() {
        for (AEMese aeMese : AEMese.values()) {
            entityBean = gSonService.crea(clazzGiorno, aeMese.getNome());

            Assert.assertNotNull(entityBean);
            Assert.assertEquals(aeMese.getNome(), entityBean.id);
            ottenutoDataTime = entityBean.creazione;
            System.out.println(date.getDataOrarioCompleta(ottenutoDataTime));
        }
    }

    @Test
    @Order(14)
    @DisplayName("14 - deserializeLocalDate")
    void deserializeLocalDate() {
        for (AEMese aeMese : AEMese.values()) {
            entityBean = gSonService.crea(clazzGiorno, aeMese.getNome());

            Assert.assertNotNull(entityBean);
            Assert.assertEquals(aeMese.getNome(), entityBean.id);
            ottenutoDataTime = entityBean.creazione;
            System.out.println(date.getCompleta(ottenutoData));
        }
    }

    @Test
    @Order(15)
    @DisplayName("15 - deserializeLocalTime")
    void deserializeLocalTime() {
        for (AEMese aeMese : AEMese.values()) {
            entityBean = gSonService.crea(clazzGiorno, aeMese.getNome());

            Assert.assertNotNull(entityBean);
            Assert.assertEquals(aeMese.getNome(), entityBean.id);
            ottenutoDataTime = entityBean.creazione;
            System.out.println(date.getOrario(ottenutoOrario));
        }
    }


    @Test
    @Order(16)
    @DisplayName("16 - crea - via")
    void creaVia() {
        valueID = "via";
        previsto = valueID;

        objectQuery = new BasicDBObject();
        objectQuery.put("_id", valueID);
        doc = (Document) collectionVia.find(objectQuery).first();
        entityBean = gSonService.crea(doc, clazzVia);

        Assert.assertNotNull(entityBean);
        Assert.assertEquals(previsto, entityBean.id);

        valueID = "calle";
        previsto = valueID;
        entityBean = gSonService.crea(clazzVia, valueID);

        Assert.assertNotNull(entityBean);
        Assert.assertEquals(previsto, entityBean.id);

        valueID = "nonEsiste";
        entityBean = gSonService.crea(clazzVia, valueID);
        Assert.assertNull(entityBean);
    }

    @Test
    @Order(17)
    @DisplayName("17 - crea - anno")
    void creaAnno() {
        valueID = "308";
        previsto = valueID;
        previsto2 = "ivsecolo";

        entityBean = gSonService.crea(clazzAnno, valueID);

        Assert.assertNotNull(entityBean);
        Assert.assertEquals(previsto, entityBean.id);
        Assert.assertEquals(previsto2, ((Anno) entityBean).secolo.id);

        valueID = "nonEsiste";
        entityBean = gSonService.crea(clazzAnno, valueID);
        Assert.assertNull(entityBean);
    }

    @Test
    @Order(18)
    @DisplayName("18 - crea - versione")
    void creaVersione() {
        valueID = "setup";
        previsto = valueID;

        entityBean = gSonService.crea(clazzVersione, valueID);

        Assert.assertNotNull(entityBean);
        Assert.assertEquals(previsto, entityBean.id);
        ottenutoData = ((Versione) entityBean).giorno;
        Assert.assertNotNull(ottenutoData);
        System.out.println(date.getCompleta(ottenutoData));

        valueID = "nonEsiste";
        entityBean = gSonService.crea(clazzVersione, valueID);
        Assert.assertNull(entityBean);
    }

    @Test
    @Order(19)
    @DisplayName("19 - crea - delta")
    void creaDelta() {
        for (AEMese aeMese : AEMese.values()) {
            entityBean = gSonService.crea(clazzGiorno, aeMese.getNome());

            Assert.assertNotNull(entityBean);
            Assert.assertEquals(aeMese.getNome(), entityBean.id);
            System.out.println(entityBean);
        }

        valueID = "nonEsiste";
        entityBean = gSonService.crea(clazzGiorno, valueID);
        Assert.assertNull(entityBean);
    }

    //    @Test
    @Order(12)
    @DisplayName("12 - creaViaID")
    void creaViaID() {
        clazz = clazzVia;
        sorgente = "rione";
        previsto = sorgente;

        //        entityBean = gSonService.creaID(clazz, sorgente);

        //        Assert.assertNotNull(ottenuto);
        //        Assert.assertEquals(previsto, entityBean.id);
    }


    //    @Test
    @Order(41)
    @DisplayName("41 - Crea entity Via")
    void creaViaOld() {
        Via via;
        Class clazz = Via.class;
        String clazzName = clazz.getSimpleName().toLowerCase();

        //        collectionVia = database.getCollection(clazzName);
        doc = (Document) collectionVia.find().first();

        via = (Via) gSonService.crea(doc, clazz);
        Assert.assertNotNull(via.id);
        System.out.println(VUOTA);
        System.out.println(via.id);
    }

    //    @Test
    @Order(42)
    @DisplayName("42 - Crea entity Via from collection")
    void creaVia2() {
        Via via;
        Class clazz = Via.class;
        String clazzName = clazz.getSimpleName().toLowerCase();
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        com.mongodb.client.MongoClient mongoClient = MongoClients.create(clientSettings);
        MongoDatabase db = mongoClient.getDatabase("vaadflow14");

        MongoCollection collection = db.getCollection(clazzName, clazz);
        Object alfa = collection.find().first();
        via = (Via) collection.find().first();

        //        via = (Via) gSonService.crea(doc, clazz);
        Assert.assertNotNull(via.id);
        System.out.println(VUOTA);
        System.out.println(via.id);

        clazz = Anno.class;
        clazzName = clazz.getSimpleName().toLowerCase();
        collection = db.getCollection(clazzName, clazz);
        Anno anno = (Anno) collection.find().first();
        int a = 87;

    }

    //    @Test
    @Order(51)
    @DisplayName("51 - Crea entity Anno")
    void creaAnnoOld() {
        Anno anno;
        Class clazz = Anno.class;
        String clazzName = clazz.getSimpleName().toLowerCase();

        //        collection = database.getCollection(clazzName);
        doc = (Document) collectionAnno.find().first();

        anno = (Anno) gSonService.crea(doc, clazz);
        Assert.assertNotNull(anno.id);
        Assert.assertNotNull(anno.secolo.id);
        Assert.assertNotNull(anno.secolo.secolo);
        System.out.println(VUOTA);
        System.out.println(anno.id);
        System.out.println(anno.secolo);
        System.out.println(anno.secolo.secolo);
    }

    //        @Test
    @Order(1)
    @DisplayName("1 - Student instance")
    void studentInstance() {

        //Create a studente instance
        Student student = new Student();
        student.setAge(10);
        student.setNome("Mahesh");

        //map Student object to JSON content
        String jsonString = gSonService.fixDoc(student);
        System.out.println(VUOTA);
        System.out.println("Student object to JSON content");
        System.out.println(jsonString);

        //map JSON content to Student object
        Student student1 = gSon.fromJson(jsonString, Student.class);
        System.out.println(VUOTA);
        System.out.println("JSON content to Student object");
        System.out.println(student1);
    }


    //    @Test
    @Order(2)
    @DisplayName("2 - Mese instance")
    void meseInstance() {
        Class<? extends AEntity> clazz = Mese.class;
        String clazzName = clazz.getSimpleName().toLowerCase();

        //Create an instance
        Mese mesePre = Mese.builderMese().mese("marzo").giorni(30).giorniBisestile(30).sigla("mar").build();

        //map Mese object to JSON content
        String jsonString = gSonService.fixDoc(mesePre);
        System.out.println(VUOTA);
        System.out.println("Mese object to JSON content");
        System.out.println(jsonString);

        //map JSON content to Mese object
        AEntity mesePost = gSon.fromJson(jsonString, clazz);
        System.out.println(VUOTA);
        System.out.println("JSON content to Mese object");
        System.out.println(mesePost);

        Assert.assertEquals(mesePre, mesePost);
    }


    //    @Test
    @Order(3)
    @DisplayName("3 - Inner class")
    void innerClass() {
        Student student = new Student();
        student.setRollNo(1);
        Student.Name name = student.new Name();

        name.firstName = "Mahesh";
        name.lastName = "Kumar";
        student.setName(name);

        String jsonString = gSonService.fixDoc(student);
        System.out.println(jsonString);
        student = gSon.fromJson(jsonString, Student.class);

        System.out.println("Roll No: " + student.getRollNo());
        System.out.println("First Name: " + student.getName().firstName);
        System.out.println("Last Name: " + student.getName().lastName);

        String nameString = gSonService.fixDoc(name);
        System.out.println(VUOTA);
        System.out.println(nameString);

        name = gSon.fromJson(nameString, Student.Name.class);
        System.out.println(name.getClass());
        System.out.println("First Name: " + name.firstName);
        System.out.println("Last Name: " + name.lastName);
    }


    //    @Test
    @Order(4)
    @DisplayName("4 - Anno instance")
    void annoInstance() {
        Class<? extends AEntity> clazz = Anno.class;
        String clazzName = clazz.getSimpleName().toLowerCase();

        //Create an instance
        Secolo secolo = Secolo.builderSecolo().ordine(4).secolo("XX secolo").anteCristo(false).inizio(34).fine(87).build();
        secolo.id = "mario";
        Anno annoPre = Anno.builderAnno().ordine(3).anno("1874").bisestile(false).secolo(secolo).build();
        annoPre.id = "francesco";

        //map Anno object to JSON content
        String jsonString = gSonService.fixDoc(annoPre);
        System.out.println(VUOTA);
        System.out.println("Anno object to JSON content");
        System.out.println(jsonString);

        //map JSON content to Anno object
        AEntity annoPost = gSon.fromJson(jsonString, clazz);
        System.out.println(VUOTA);
        System.out.println("JSON content to Anno object");
        System.out.println(annoPost);

        Assert.assertEquals(annoPre, annoPost);
    }


    //    @Test
    @Order(5)
    @DisplayName("5 - estraeGraffe")
    void estraeGraffe2() {
        String jsonString;
        String dbRefString;
        Long propertiesNumber;
        AEntity entity;
        String clazzName = "via";
        String clazzName2 = "anno";
        Class clazz = Secolo.class;
        previstoIntero = 1;
        //        collection = database.getCollection(clazzName);
        propertiesNumber = collectionVia.countDocuments();
        assertNotNull(propertiesNumber);
        System.out.println(VUOTA);
        System.out.println("Nella collezione 'Via' ci sono " + propertiesNumber + " elementi");

        Document doc = (Document) collectionVia.find().first();
        jsonString = doc.toJson();
        listaStr = gSonService.estraeGraffe(jsonString);
        assertNull(listaStr);

        previstoIntero = 2;
        //        collection = database.getCollection(clazzName2);
        propertiesNumber = collectionAnno.countDocuments();
        assertNotNull(propertiesNumber);
        System.out.println(VUOTA);
        System.out.println("Nella collezione 'Anno' ci sono " + propertiesNumber + " elementi");
    }


    //    @Test
    @Order(6)
    @DisplayName("6 - Anno from real database")
    void execute() {
        offset = 2970;
        limit = 1;
        String jsonString;
        Class clazz = Anno.class;
        AEntity entity;
        List<Field> listaRef;
        Anno anno = null;
        String clazzName = clazz.getSimpleName().toLowerCase();

        //        collection = database.getCollection(clazzName);
        Collection<Document> documents = collectionAnno.find().skip(offset).limit(limit).into(new ArrayList());
        listaRef = annotation.getDBRefFields(clazz);

        for (Document doc : documents) {
            jsonString = gSonService.fixDoc(doc);
            entity = (AEntity) gSon.fromJson(jsonString, clazz);
            assertNotNull(entity.id);

            //            Map mappa = gSonService.toMap(doc);

            if (entity instanceof Anno) {
                anno = (Anno) entity;
            }
            JsonElement element = gSon.toJsonTree(doc);
            JsonObject obj = element.getAsJsonObject();
            JsonElement sec = obj.get("secolo");
            JsonObject obj2 = sec.getAsJsonObject();
            JsonElement sec2 = obj2.get("id");
            String value = sec2.getAsString();
            if (listaRef != null && listaRef.size() > 0) {
                for (Field field : listaRef) {
                    clazzName = field.getName();
                    collectionAnno = database.getCollection(clazzName);
                }
            }

            Assert.assertEquals("1971", anno.anno);
            System.out.println(VUOTA);
            System.out.println("JSON content to Anno object");
            System.out.println(anno.anno);
        }
    }

    //    @Test
    @Order(7)
    @DisplayName("7 - Versione from real database")
    void execute2() {
        offset = 0;
        limit = 1;
        String jsonString;
        Class clazz = Versione.class;
        AEntity entity;
        List<Field> listaRef;
        Versione versione = null;
        String clazzName = clazz.getSimpleName().toLowerCase();

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {

            @Override
            public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                Instant instant = Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
                return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            }
        }).create();

        //        collection = database.getCollection(clazzName);
        Collection<Document> documents = collectionVersione.find().skip(offset).limit(limit).into(new ArrayList());
        listaRef = annotation.getDBRefFields(clazz);

        for (Document doc : documents) {
            jsonString = gSonService.fixDoc(doc);

            try {
                //                Map mappa = gSonService.toMap(doc);
                Object obj99 = gSon.fromJson(jsonString, clazz);
            } catch (JsonSyntaxException exception) {
                logger.error(exception, this.getClass(), "nomeDelMetodo");
            }

            entity = (AEntity) gSon.fromJson(jsonString, clazz);
            assertNotNull(entity.id);

            if (entity instanceof Versione) {
                versione = (Versione) entity;
            }
            JsonElement element = gSon.toJsonTree(doc);
            JsonObject obj = element.getAsJsonObject();
            JsonElement sec = obj.get("secolo");
            JsonObject obj2 = sec.getAsJsonObject();
            JsonElement sec2 = obj2.get("id");
            String value = sec2.getAsString();
            if (listaRef != null && listaRef.size() > 0) {
                for (Field field : listaRef) {
                    clazzName = field.getName();
                    collectionVia = database.getCollection(clazzName);
                }
            }

            Assert.assertEquals("rest", versione.code);
            System.out.println(VUOTA);
            System.out.println("JSON content to Versione object");
            System.out.println(versione.code);
            //            Assert.assertEquals("XX secolo", anno.secolo.secolo);
            //            System.out.println(anno.secolo.secolo);
        }
    }

    //    @Test
    @Order(8)
    @DisplayName("8 - Preferenza instance string")
    void preferenzaInstance() {
        Class<? extends AEntity> clazz = Preferenza.class;

        //Create an instance
        Preferenza prefAnte = Preferenza.builderPreferenza()

                .code("alfa")

                .descrizione("Controllo entrate")

                .type(AETypePref.string)

                .value(AETypePref.string.objectToBytes("Mario"))

                .build();

        //map Preferenza object to JSON content
        String jsonString = gSonService.fixDoc(prefAnte);
        System.out.println(VUOTA);
        System.out.println("Preferenza object to JSON content");
        System.out.println(jsonString);

        //map JSON content to Preferenza object
        Preferenza prefPost = (Preferenza) gSon.fromJson(jsonString, clazz);
        System.out.println(VUOTA);
        System.out.println("JSON content to Preferenza object");
        System.out.println("code: " + prefPost.code);
        System.out.println("descrizione: " + prefPost.descrizione);
        System.out.println("type: " + prefPost.type);
        System.out.println("value: " + AETypePref.string.bytesToObject(prefPost.value));

        Assert.assertEquals(prefAnte, prefPost);
    }


    //    @Test
    @Order(9)
    @DisplayName("9 - Preferenza instance int")
    void preferenzaInstance2() {
        Class<? extends AEntity> clazz = Preferenza.class;

        //Create an instance
        Preferenza prefAnte = Preferenza.builderPreferenza()

                .code("alfa")

                .descrizione("Controllo entrate")

                .type(AETypePref.integer)

                .value(AETypePref.integer.objectToBytes(24))

                .build();

        //map Preferenza object to JSON content
        String jsonString = gSonService.fixDoc(prefAnte);
        System.out.println(VUOTA);
        System.out.println("Preferenza object to JSON content");
        System.out.println(jsonString);

        //map JSON content to Preferenza object
        Preferenza prefPost = (Preferenza) gSon.fromJson(jsonString, clazz);
        System.out.println(VUOTA);
        System.out.println("JSON content to Preferenza object");
        System.out.println("code: " + prefPost.code);
        System.out.println("descrizione: " + prefPost.descrizione);
        System.out.println("type: " + prefPost.type);
        System.out.println("value: " + AETypePref.integer.bytesToObject(prefPost.value));

        Assert.assertEquals(prefAnte, prefPost);
    }


    //    @Test
    @Order(10)
    @DisplayName("10 - Preferenza from real database")
    void executes() {
        offset = 6;
        limit = 1;
        String jsonString;
        Class clazz = Preferenza.class;
        AEntity entity;
        String clazzName = "preferenza";
        String[] parti = null;
        Object[] documents;
        Preferenza pref = null;

        collection = database.getCollection(clazzName);
        //        documents = collection.find().skip(offset).limit(limit).into(new ArrayList()).toArray();
        //        doc = (Document) documents[0];
        doc = (Document) collection.find().first();
        Assert.assertNotNull(doc);

        //        Map mappa = gSonService.toMap(doc);

        pref = (Preferenza) gSon.fromJson(doc.toJson(), clazz);
        Assert.assertNotNull(pref);

        //        Gson gson = new Gson();
        //        Preferenza mongoObj = gson.fromJson(doc.toJson(), Preferenza.class);
        //        Assert.assertNotNull(mongoObj);
    }


    //    @Test
    @Order(12)
    @DisplayName("12 - Crea entity Versione")
    void creaVersioneOld() {
        Versione versione;
        Class clazz = Versione.class;
        String clazzName = clazz.getSimpleName().toLowerCase();

        collection = database.getCollection(clazzName);
        doc = (Document) collection.find().first();
        versione = (Versione) gSonService.crea(doc, clazz);

        sorgente = "Dec 16, 2020, 12:00:00 AM";
        String pattern = "MMM d, yyy, HH:mm:ss 'AM'";
        LocalDateTime dt = LocalDateTime.parse(sorgente);
        System.out.println(VUOTA);
        System.out.println(dt);
        System.out.println(VUOTA);
        dataUno = LocalDateTime.of(2020, 12, 16, 12, 0, 0);
        ottenuto = date.get(dataUno, pattern);
        System.out.println(VUOTA);
        System.out.println(ottenuto);
        System.out.println(VUOTA);
        sorgente = "dic 16, 2020, 12:00:00 AM";
        LocalDateTime localDateTime = LocalDateTime.parse(sorgente, DateTimeFormatter.ofPattern(pattern));
        System.out.println(localDateTime);

        ObjectMapper objectMapper = new ObjectMapper();
        String stringaJSON = gSon.toJson(doc);
        stringaJSON = stringaJSON.replace("_id", "id");

        try {
            Map<String, Object> jsonMap = objectMapper.readValue(stringaJSON, new TypeReference<Map<String, Object>>() {

            });
            int a = 87;
        } catch (Exception unErrore) {
            logger.error(unErrore, this.getClass(), "nomeDelMetodo");
        }

        try {
            versione = objectMapper.readValue(stringaJSON, Versione.class);
        } catch (Exception unErrore) {
            logger.error(unErrore, this.getClass(), "nomeDelMetodo");
        }

        versione = (Versione) gSonService.crea(doc, clazz);
        Assert.assertNotNull(versione.id);
        Assert.assertNotNull(versione.giorno);
        System.out.println(VUOTA);
        System.out.println(versione.id);
        System.out.println(versione.giorno);
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


    class Student {

        private String nome;

        private Name name;

        private int rollNo;

        private int age;


        public Student() {
        }


        public int getRollNo() {
            return rollNo;
        }


        public void setRollNo(int rollNo) {
            this.rollNo = rollNo;
        }


        public Name getName() {
            return name;
        }


        public void setName(Name name) {
            this.name = name;
        }


        public int getAge() {
            return age;
        }


        public void setAge(int age) {
            this.age = age;
        }


        public String getNome() {
            return nome;
        }


        public void setNome(String nome) {
            this.nome = nome;
        }


        public String toString() {
            return "Student [nome:" + nome + ", age:" + age + "]";
        }


        class Name {

            public String firstName;

            public String lastName;

        }

    }

}