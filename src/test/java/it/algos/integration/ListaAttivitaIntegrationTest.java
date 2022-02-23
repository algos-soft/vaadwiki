package it.algos.integration;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadwiki.*;
import it.algos.vaadwiki.backend.liste.*;
import it.algos.vaadwiki.backend.packages.attivita.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import it.algos.vaadwiki.backend.service.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.context.support.*;
import org.springframework.data.mongodb.core.*;
import org.springframework.test.context.junit.jupiter.*;

import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 05-gen-2022
 * Time: 07:54
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {WikiApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllIntegration")
@DisplayName("Test per le liste di Attività")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ListaAttivitaIntegrationTest extends WTest {

    /**
     * The Service.
     */
    @Autowired
    public MongoService mongoService;

    /**
     * Inietta da Spring
     */
    @Autowired
    public MongoTemplate mongoOp;

    @Autowired
    public WikiUtility wikiUtility;

    /**
     * The App context.
     */
    @Autowired
    protected GenericApplicationContext appContext;

    private List<Bio> listaBio = null;

    private List<String> listaDidascalie = null;

    private Map<String, List> mappaUno = null;

    private ListaAttivita istanza;

    private Attivita attivita;


    private Map<String, List> mappaAttivita;


    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        MockitoAnnotations.initMocks(this);

        MockitoAnnotations.initMocks(mongoService);
        Assertions.assertNotNull(mongoService);

        MockitoAnnotations.initMocks(mongoOp);
        Assertions.assertNotNull(mongoOp);

        FlowVar.typeSerializing = AETypeSerializing.spring;
    }

    /**
     * Regola tutti riferimenti incrociati <br>
     * Deve essere fatto dopo aver costruito le referenze 'mockate' <br>
     * Nelle sottoclassi di testi devono essere regolati i riferimenti dei service specifici <br>
     */
    protected void wFixRiferimentiIncrociati() {
        super.wFixRiferimentiIncrociati();

        mongoService.mongoOp = mongoOp;
        bioService.mongo = mongoService;

        attivitaService.mongo = mongoService;
        mongoService.mongoOp = mongoOp;
        bioService.mongo = mongoService;
        attivitaService.array = arrayService;
    }

    /**
     * Crea alcune istanze specifiche di ogni test <br>
     */
    @Override
    protected void wCreaEntity() {
        super.wCreaEntity();
    }

    /**
     * Qui passa a ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();

        istanza = null;
        attivita = null;
        listaBio = null;
        listaDidascalie = null;
        mappaAttivita = null;
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA")
    @Order(1)
    @DisplayName("1 - Crea una istanza listaAttivita da Attivita ")
        //--attivita
        //--AETypeAttivita
    void creaIstanzaAttivita(final Attivita attivita, final ListaAttivita.AETypeAttivita type) {
        System.out.println(String.format("1 - Crea una istanza listaAttivita da Attivita di '%s'", attivita.toString()));
        System.out.println("Per costruire una istanza listaAttivita, normalmente uso una entity Attivita senza altri parametri");
        System.out.println("Per costruire una istanza listaAttivita, posso usare una entity Attivita col parametro aggiuntivo ListaAttivita.AETypeAttivita");
        System.out.println("Per costruire una istanza listaAttivita, posso usare un nome attivita singolare o plurale");

        if (attivita == null) {
            System.out.println("Nessun attività indicata");
            return;
        }

        if (type == ListaAttivita.AETypeAttivita.singolare) {
            istanza = appContext.getBean(ListaAttivita.class, attivita, ListaAttivita.AETypeAttivita.singolare);
            assertNotNull(istanza);
            assertTrue(istanza.getNumeroAttivitaSingoleConLoStessoPlurale() == 1);
            assertTrue(istanza.getBioSize() == istanza.getNumDidascalie());
            System.out.println(VUOTA);
            System.out.println(String.format("Lista costruita con una sola attività '%s'", attivita.singolare));
            System.out.println(String.format("La lista è composta di %d biografie", istanza.getBioSize()));
        }

        if (type == ListaAttivita.AETypeAttivita.plurale) {
            istanza = appContext.getBean(ListaAttivita.class, attivita);
            assertNotNull(istanza);
            assertTrue(istanza.getBioSize() == istanza.getNumDidascalie());
            assertTrue(istanza.getNumeroAttivitaSingoleConLoStessoPlurale() > 0);
            this.printAttivitaIstanza(attivita, istanza);
        }
    }

    @ParameterizedTest
    @MethodSource(value = "ATTIVITA")
    @Order(2)
    @DisplayName("2 - Crea una istanza listaAttivita da nomeAttivitaSingolare o nomeAttivitaPlurale")
        //--attivita
        //--AETypeAttivita
    void creaIstanzaNomeAttivita(final Attivita attivita, final ListaAttivita.AETypeAttivita type) {
        System.out.println(String.format("2 - Crea una istanza listaAttivita da nomeAttivitaSingolare o nomeAttivitaPlurale di '%s'", attivita.toString()));
        String nomeAttivita = VUOTA;

        if (attivita == null) {
            System.out.println("Nessun attività indicata");
            return;
        }

        if (type == ListaAttivita.AETypeAttivita.singolare) {
            nomeAttivita = attivita.singolare;
            istanza = appContext.getBean(ListaAttivita.class, nomeAttivita, type);
            assertNotNull(istanza);
            assertTrue(istanza.getBioSize() == istanza.getNumDidascalie());
            assertTrue(istanza.getNumeroAttivitaSingoleConLoStessoPlurale() == 1);
            System.out.println(VUOTA);
            System.out.println(String.format("Lista costruita da nomeAttivitaSingolare con una sola attività '%s'", attivita.singolare));
            System.out.println(String.format("La lista è composta di %d biografie", istanza.getBioSize()));
        }

        if (type == ListaAttivita.AETypeAttivita.plurale) {
            nomeAttivita = attivita.plurale;
            istanza = appContext.getBean(ListaAttivita.class, nomeAttivita, type);
            assertNotNull(istanza);
            assertTrue(istanza.getBioSize() == istanza.getNumDidascalie());
            assertTrue(istanza.getNumeroAttivitaSingoleConLoStessoPlurale() > 0);
            this.printAttivitaIstanza(attivita, istanza);
        }
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA")
    @Order(3)
    @DisplayName("3 - Crea una lista di biografie per l'attività singolare e plurale")
        //--attivita
        //--AETypeAttivita
    void creaListaBio(final Attivita attivita, final ListaAttivita.AETypeAttivita type) {
        if (attivita == null) {
            System.out.println("Nessun attività indicata");
            return;
        }

        if (type == ListaAttivita.AETypeAttivita.singolare) {
            System.out.println(String.format("3 - Crea una lista di biografie per l'attività (singolare) '%s'", attivita.singolare));
            listaBio = appContext.getBean(ListaAttivita.class, attivita, type).getListaBio();
            assertNotNull(listaBio);
            System.out.println(VUOTA);
            System.out.println(String.format("Lista costruita con una sola attività (singolare) '%s'", attivita.singolare));
            printListaBio(listaBio);
        }

        if (type == ListaAttivita.AETypeAttivita.plurale) {
            System.out.println(String.format("3 - Crea una lista di biografie per l'attività (plurale) '%s'", attivita.plurale));
            // si può omettere il type perché il costruttore di ListaAttivita inserisce di default ListaAttivita.AETypeAttivita.plurale
            listaBio = appContext.getBean(ListaAttivita.class, attivita).getListaBio();
            assertNotNull(listaBio);
            System.out.println(VUOTA);
            System.out.println(String.format("Lista costruita con tutte le attività associate a '%s'", attivita.plurale));
            printListaBio(listaBio);
        }
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA")
    @Order(4)
    @DisplayName("4 - Crea una lista di didascalie complete per l'attività singolare e plurale")
        //--attivita
        //--AETypeAttivita
    void creaListaDidascalie(final Attivita attivita, final ListaAttivita.AETypeAttivita type) {
        if (attivita == null) {
            System.out.println("Nessun attività indicata");
            return;
        }

        if (type == ListaAttivita.AETypeAttivita.singolare) {
            System.out.println(String.format("4 - Crea una lista di didascalie complete per l'attività (singolare) '%s'", attivita.singolare));
            listaDidascalie = appContext.getBean(ListaAttivita.class, attivita, type).getListaDidascalie();
            assertNotNull(listaDidascalie);
            System.out.println(VUOTA);
            System.out.println(String.format("Lista di didascalie complete per una sola attività (singolare) '%s'", attivita.singolare));
            printListaDidascalie(listaDidascalie);
        }
        if (type == ListaAttivita.AETypeAttivita.plurale) {
            System.out.println(String.format("4 - Crea una lista di didascalie complete per l'attività (plurale) '%s'", attivita.plurale));
            // si può omettere il type perché il costruttore di ListaAttivita inserisce di default ListaAttivita.AETypeAttivita.plurale
            listaDidascalie = appContext.getBean(ListaAttivita.class, attivita).getListaDidascalie();
            assertNotNull(listaDidascalie);
            System.out.println(VUOTA);
            System.out.println(String.format("Lista costruita con tutte le attività associate a '%s'", attivita.plurale));
            printListaDidascalie(listaDidascalie);
        }
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA")
    @Order(5)
    @DisplayName("5 - Crea una mappa con paragrafi per l'attività singolare e plurale")
        //--attivita
        //--AETypeAttivita
    void creaMappa(final Attivita attivita, final ListaAttivita.AETypeAttivita type) {
        if (attivita == null) {
            System.out.println("Nessun attività indicata");
            return;
        }

        if (type == ListaAttivita.AETypeAttivita.singolare) {
            System.out.println(String.format("4 - Crea una mappa con paragrafi per l'attività (singolare) '%s'", attivita.singolare));
            mappaAttivita = appContext.getBean(ListaAttivita.class, attivita, type).getMappa();
            assertNotNull(mappaAttivita);
            System.out.println(VUOTA);
            System.out.println(String.format("Mappa con paragrafi per l'attività (singolare) '%s'", attivita.singolare));
            printParagrafi(mappaAttivita);
        }

        if (type == ListaAttivita.AETypeAttivita.plurale) {
            System.out.println(String.format("4 - Crea una mappa con paragrafi per l'attività (plurale) '%s'", attivita.plurale));
            // si può omettere il type perché il costruttore di ListaAttivita inserisce di default ListaAttivita.AETypeAttivita.plurale
            mappaAttivita = appContext.getBean(ListaAttivita.class, attivita).getMappa();
            assertNotNull(mappaAttivita);
            System.out.println(VUOTA);
            System.out.println(String.format("Mappa con paragrafi per l'attività (plurale) '%s'", attivita.plurale));
            printParagrafi(mappaAttivita);
        }
    }


    @ParameterizedTest
    @MethodSource(value = "ATTIVITA")
    @Order(6)
    @DisplayName("6 - Crea un testo con paragrafi per l'attività singolare e plurale")
        //--attivita
        //--AETypeAttivita
    void creaTesto(final Attivita attivita, final ListaAttivita.AETypeAttivita type) {
        if (attivita == null) {
            System.out.println("Nessun attività indicata");
            return;
        }

        if (type == ListaAttivita.AETypeAttivita.singolare) {
            System.out.println(String.format("4 - Crea un testo con paragrafi per l'attività (singolare) '%s'", attivita.singolare));
            ottenuto = appContext.getBean(ListaAttivita.class, attivita, type).getTestoConParagrafi();
            System.out.println(VUOTA);
            System.out.println(String.format("Testo con paragrafi per l'attività (singolare) '%s'", attivita.singolare));
            System.out.println(VUOTA);
            System.out.println(ottenuto);
        }

        if (type == ListaAttivita.AETypeAttivita.plurale) {
            System.out.println(String.format("4 - Crea un testo con paragrafi per l'attività (plurale) '%s'", attivita.plurale));
            // si può omettere il type perché il costruttore di ListaAttivita inserisce di default ListaAttivita.AETypeAttivita.plurale
            ottenuto = appContext.getBean(ListaAttivita.class, attivita).getTestoConParagrafi();
            System.out.println(VUOTA);
            System.out.println(String.format("Testo con paragrafi per l'attività (plurale) '%s'", attivita.plurale));
            System.out.println(VUOTA);
            System.out.println(ottenuto);
        }
    }


    private void printListaBio(final List<Bio> listaBio) {
        System.out.println(VUOTA);
        if (listaBio != null) {
            if (listaBio.size() > 0) {
                System.out.println(String.format("Ci sono %d biografie", listaBio.size()));
                System.out.println("===================");
                for (Bio bio : listaBio) {
                    System.out.println(String.format("'%s'", bio.getWikiTitle()));
                }
            }
            else {
                System.out.println("L'array listaBio esiste ma è vuoto");
            }
        }
        else {
            System.out.println("Non esiste l'array listaBio");
        }
    }


    private void printListaDidascalie(final List<String> listaDidascalie) {
        System.out.println(VUOTA);
        if (listaDidascalie != null) {
            if (listaDidascalie.size() > 0) {
                System.out.println(String.format("Ci sono %d didascalie", listaDidascalie.size()));
                System.out.println("====================");
                for (String singola : listaDidascalie) {
                    System.out.println(String.format("* %s", singola));
                }
            }
            else {
                System.out.println("L'array listaDidascalie esiste ma è vuota");
            }
        }
        else {
            System.out.println("Non esiste l'array listaDidascalie");
        }
    }


    private void printParagrafi(final Map<String, List> mappa) {
        List lista;
        String asterisco = "*";

        System.out.println(VUOTA);
        if (arrayService.isAllValid(mappa)) {
            for (String key : mappa.keySet()) {
                lista = mappa.get(key);

                if (arrayService.isAllValid(lista)) {
                    System.out.print(wikiUtility.setParagrafo(key));
                    for (Object stringa : lista) {
                        System.out.print(asterisco);
                        System.out.print(SPAZIO);
                        System.out.println(stringa);
                    }
                    System.out.print(A_CAPO);
                }
            }
        }
    }


    void printAttivitaIstanza(Attivita attivita, ListaAttivita istanza) {
        int k = 0;
        System.out.println(VUOTA);
        System.out.println(String.format("Lista costruita con diverse attività singolari dell'attività plurale '%s'", attivita.plurale));
        System.out.println(String.format("La lista è composta di %d biografie", istanza.getBioSize()));
        System.out.println(VUOTA);
        System.out.println(String.format("Le singole attività di '%s' sono %d", attivita.plurale, istanza.getNumeroAttivitaSingoleConLoStessoPlurale()));
        for (String singola : istanza.getListaNomiAttivitaSingole()) {
            k++;
            System.out.print(k);
            System.out.print(PARENTESI_TONDA_END);
            System.out.print(SPAZIO);
            System.out.println(singola);
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