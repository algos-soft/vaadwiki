package it.algos.unit;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.mongodb.client.*;
import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.packages.anagrafica.via.*;
import it.algos.vaadflow14.backend.packages.company.*;
import it.algos.vaadflow14.backend.packages.crono.anno.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadflow14.backend.service.*;
import org.bson.conversions.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;

import java.text.*;
import java.time.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 30-apr-2021
 * Time: 07:51
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("Mongo Service (senza mongoOp)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MongoServiceTest extends ATest {

    protected static final String COLLEZIONE_INESISTENTE = "pomeriggio";

    protected static final String COLLEZIONE_VUOTA = "utente";

    protected static final String COLLEZIONE_VALIDA = "giorno";

    private static final String DATA_BASE_NAME = "vaadflow14";

    /**
     * Classe principale di riferimento <br>
     * Gia 'costruita' nella superclasse <br>
     */
    protected AIMongoService service;

    protected MongoCollection collection;

    protected Bson bSon;


    private static String[] COLLEZIONI() {
        return new String[]{"pomeriggio", "alfa", "via"};
    }

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        //--reindirizzo l'istanza della superclasse
        service = mongoService;
    }


    /**
     * Qui passa a ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();

        collection = null;
        bSon = null;
    }


    @Test
    @Order(13)
    @DisplayName("13 - Save base (gson) di una entity")
    void save() {
        System.out.println("13 - Save base (gson) di una entity");
        FlowVar.typeSerializing = AETypeSerializing.gson;
        Company company = null;
        Company companyReborn = null;

        //--costruisco una entityBean
        sorgente = "doppia";
        sorgente2 = "Porta Valori Associato";
        company = companyService.newEntity(sorgente, sorgente2, VUOTA, VUOTA);
                company.setCreazione(LocalDateTime.now());
        assertNotNull(company);

        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
            mapper.setDateFormat(format);
            json = mapper.writeValueAsString(company);
            System.out.println(json);
        } catch (JsonProcessingException unErrore) {
            System.out.println(unErrore);
        }

        //        collection.insertOne(Document.parse(json));

        //        --salvo la entityBean
        try {
            //            ((MongoService) service).save(company);
            companyService.save(company);
        } catch (AMongoException unErrore) {
//            System.out.println(unErrore);
            loggerService.info(unErrore.getMessage());
        }
    }

    //    @Test
    @Order(13)
    @DisplayName("13 - Trova singola entity by id")
    void findById() {
        System.out.println("13 - Trova singola entity by id");

        //            sorgente = "104";
        //            clazz = Anno.class;
        //            entityBean = service.findById(clazz, sorgente);
        //            assertNotNull(entityBean);
        //            System.out.println(VUOTA);
        //            System.out.println(String.format("Recupero di un bean di classe %s", clazz.getSimpleName()));
        //            System.out.println(entityBean);
        //
        //            sorgente = "via";
        //            clazz = Via.class;
        //            entityBean = service.findById(clazz, sorgente);
        //            assertNotNull(entityBean);
        //            System.out.println(VUOTA);
        //            System.out.println(String.format("Recupero di un bean di classe %s", clazz.getSimpleName()));
        //            System.out.println(entityBean);

        sorgente = "terzo";
        clazz = Company.class;
        try {
            entityBean = service.findById(clazz, sorgente);
        } catch (Exception unErrore) {
        }
        assertNotNull(entityBean);
        System.out.println(VUOTA);
        System.out.println(String.format("Recupero di un bean di classe %s", clazz.getSimpleName()));
        System.out.println(entityBean);
    }


    //    @Test
    @Order(6)
    @DisplayName("6 - Trova singola entity by key")
    void findByKey() {
        System.out.println("6 - Trova singola entity by key");

        clazz = Giorno.class;
        sorgente = "titolo";
        sorgente2 = "4 novembre";
        try {
            entityBean = service.findByProperty(clazz, sorgente, sorgente2);
        } catch (AMongoException unErrore) {
        }
        assertNotNull(entityBean);
        System.out.println(VUOTA);
        System.out.println(String.format("EntityBean di classe %s recuperato dal valore '%s' della property '%s'", clazz.getSimpleName(), sorgente2, sorgente));
        System.out.println(entityBean);
    }


    //    @Test
    @Order(7)
    @DisplayName("7 - Save di una entity")
    void save2() {
        System.out.println("7 - Save di una entity");
        int originario;
        int daModificare;
        int modificato;
        int finale;
        String jsonInString;

        //--leggo una entityBean e memorizzo una property
        clazz = Via.class;
        sorgente = "corte";
        try {
            entityBean = service.findByKey(clazz, sorgente);
        } catch (AMongoException unErrore) {
        }
        assertNotNull(entityBean);
        originario = ((Via) entityBean).getOrdine();
        System.out.println(VUOTA);
        System.out.println(String.format("Nella entity originale [%s] il valore di 'ordine' è [%s]", sorgente, originario));

        //--modifico la entityBean
        daModificare = mathService.random();
        ((Via) entityBean).setOrdine(daModificare);

        //--registro la entityBean modificata
        try {
            //            jsonInString = gSonService.legge(entityBean);
            //            System.out.println(String.format("Stringa in formato json -> %s", jsonInString));
            ((MongoService) service).save(entityBean);
        } catch (AMongoException unErrore) {
            System.out.println(unErrore);
        }

        //--ri-leggo la entityBean (dal vecchio id) controllo la property per vedere se è stata modificata e registrata
        try {
            entityBean = service.findById(clazz, entityBean.getId());
        } catch (Exception unErrore) {
        }
        modificato = ((Via) entityBean).getOrdine();
        assertEquals(daModificare, modificato);
        System.out.println(VUOTA);
        System.out.println(String.format("Nella entity modificata [%s] il valore di 'ordine' è [%s]", sorgente, modificato));

        //--ri-modifico la entityBean
        ((Via) entityBean).setOrdine(originario);

        //--ri-registro la entityBean come in origine
        try {
            ((MongoService) service).save(entityBean);
        } catch (AMongoException unErrore) {
            System.out.println(unErrore);
        }

        //--ri-leggo la entityBean e ri-controllo la property
        try {
            entityBean = service.findByKey(clazz, sorgente);
        } catch (AMongoException unErrore) {
        }
        assertNotNull(entityBean);
        finale = ((Via) entityBean).getOrdine();
        assertEquals(originario, finale);
        System.out.println(VUOTA);
        System.out.println(String.format("Nella entity ricostruita [%s] il valore di 'ordine' è [%s], uguale al valore originario [%s]", sorgente, finale, originario));

    }

    //    @Test
    @Order(8)
    @DisplayName("8 - Trova l'ordine successivo")
    void sss() {
        System.out.println("8 - Trova l'ordine successivo\"");

        clazz = Via.class;
        sorgente = "ordine";
        try {
            entityBean = service.findByKey(clazz, sorgente);
        } catch (AMongoException unErrore) {
        }

        //--il database mongoDB potrebbe anche essere vuoto
        if (service.isExistsCollection(clazz.getSimpleName().toLowerCase())) {
            try {
                ottenutoIntero = service.getNewOrder(clazz, sorgente);
            } catch (AMongoException unErrore) {
            }
            System.out.println(String.format("Successivo ordine %d", ottenutoIntero));
        }
        else {
            System.out.println("Il database 'via' è vuoto");
        }
    }

    //    @Test
    @Order(5)
    @DisplayName("5 - Lista di tutte le entities")
    void fetch() {
        System.out.println("5 - Lista di tutte le entities");

        sorgenteClasse = Via.class;
        previstoIntero = 26;
        inizio = System.currentTimeMillis();
        try {
            listaBean = ((MongoService) service).fetch(sorgenteClasse);
        } catch (Exception unErrore) {
            loggerService.error(unErrore, this.getClass(), "fetch");
        }
        assertNotNull(listaBean);
        assertEquals(previstoIntero, listaBean.size());
        System.out.println(String.format("Nella collezione '%s' ci sono %s entities recuperate in %s", sorgenteClasse.getSimpleName(), textService.format(listaBean.size()), dateService.deltaTextEsatto(inizio)));

        sorgenteClasse = Giorno.class;
        previstoIntero = 366;
        inizio = System.currentTimeMillis();
        try {
            listaBean = ((MongoService) service).fetch(sorgenteClasse);
        } catch (AQueryException unErrore) {
            loggerService.error(unErrore, this.getClass(), "fetch");
        } catch (AMongoException unErrore) {
            loggerService.error(unErrore, this.getClass(), "fetch");
        }
        assertNotNull(listaBean);
        assertEquals(previstoIntero, listaBean.size());
        System.out.println(String.format("Nella collezione '%s' ci sono %s entities recuperate in %s", sorgenteClasse.getSimpleName(), textService.format(listaBean.size()), dateService.deltaTextEsatto(inizio)));

        sorgenteClasse = Anno.class;
        previstoIntero = 3030;
        inizio = System.currentTimeMillis();
        try {
            listaBean = ((MongoService) service).fetch(sorgenteClasse);
        } catch (AQueryException unErrore) {
            loggerService.error(unErrore, this.getClass(), "fetch");
        } catch (AMongoException unErrore) {
            loggerService.error(unErrore, this.getClass(), "fetch");
        }
        assertNotNull(listaBean);
        assertEquals(previstoIntero, listaBean.size());
        System.out.println(String.format("Nella collezione '%s' ci sono %s entities recuperate in %s", sorgenteClasse.getSimpleName(), textService.format(listaBean.size()), dateService.deltaTextEsatto(inizio)));
    }

    //    @ParameterizedTest
    //    @MethodSource(value = "COLLEZIONI")
    //    @Order(2)
    //    @DisplayName("2 - Stato delle collezioni")
    //    void testWithStringParameterOld(String collectionName) {
    //        System.out.println("2 - Stato delle collezioni");
    //
    //        sorgente = collectionName;
    //        ottenutoBooleano = service.isExists(sorgente);
    //        assertFalse(ottenutoBooleano);
    //    }


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