package it.algos.unit;

import it.algos.test.*;
import static it.algos.vaadflow14.backend.application.FlowCost.NAME_VAADFLOW;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import static it.algos.vaadflow14.wizard.scripts.WizCost.*;
import it.algos.vaadflow14.wizard.scripts.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.*;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 08-apr-2021
 * Time: 22:17
 * <p>
 * Unit test di una classe di servizio <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("testAllValido")
@DisplayName("Test di controllo sulle costanti AEWizCost, in parte tramite WizService")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WizCostTest extends ATest {

    /**
     * Classe principale di riferimento <br>
     */
    @InjectMocks
    WizService service;

    @InjectMocks
    WizCostServiceInjector injector;

    private List<AEWizCost> listaWiz;

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

        MockitoAnnotations.initMocks(injector);
        Assertions.assertNotNull(injector);
        injector.file = fileService;
        injector.text = textService;
        injector.logger = loggerService;
        injector.postConstruct();

        service.text = textService;
        service.array = arrayService;
        service.file = fileService;
    }


    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();
        listaWiz = null;
        AEWizCost.reset();
    }


    @Test
    @Order(1)
    @DisplayName("1 - Enumeration AEWizValue")
    void AEWizValue() {
        String message = VUOTA;
        int pos = 1;
        System.out.println("Enumeration AEWizValue");
        System.out.println(VUOTA);
        for (AEWizValue wiz : AEWizValue.values()) {
            message = pos + ") ";
            message += wiz.getTag();
            message += SEP;
            message += wiz.getDescrizione();
            System.out.println(message);
            pos++;
        }
    }

    @Test
    @Order(2)
    @DisplayName("2 - Enumeration AEWizUso")
    void AEWizUso() {
        String message = VUOTA;
        int pos = 1;
        System.out.println("Enumeration AEWizUso");
        System.out.println(VUOTA);
        for (AEWizUso wiz : AEWizUso.values()) {
            message = pos + ") ";
            message += wiz.getTag();
            message += SEP;
            message += wiz.getDescrizione();
            System.out.println(message);
            pos++;
        }
    }

    @Test
    @Order(3)
    @DisplayName("3 - Enumeration AEWizCopy")
    void AEWizCopy() {
        String message = VUOTA;
        int pos = 1;
        System.out.println("Enumeration AEWizCopy");
        System.out.println(VUOTA);
        for (AEWizCopy wiz : AEWizCopy.values()) {
            message = pos + ") ";
            message += wiz.getTag();
            message += SEP;
            message += wiz.getDescrizione();
            message += SEP;
            message += "Copia: " + wiz.getType().toString();
            System.out.println(message);
            pos++;
        }
    }


    @Test
    @Order(4)
    @DisplayName("4 - Tutte le costanti AEWizCost")
    void getAll33() {
        listaWiz = service.getAll();
        printAll("Enumeration completa con tutti i valori di AEWizCost (costanti, calcolati, inseriti, derivati)", listaWiz);
    }


    @Test
    @Order(5)
    @DisplayName("5 - service.getWizCostanti")
    void getWizCostanti() {
        listaWiz = service.getWizValueCostanti();
        print(AEWizValue.costante.getDescrizione(), listaWiz);
    }


    @Test
    @Order(6)
    @DisplayName("6 - service.getWizCalcolati")
    void getWizCalcolati() {
        service.fixAEWizCost();
        listaWiz = service.getWizValueCalcolati();
        print(AEWizValue.calcolato.getDescrizione(), listaWiz);
    }


    @Test
    @Order(7)
    @DisplayName("7 - service.getWizInseriti")
    void getWizInseriti() {
        listaWiz = service.getWizValueInseriti();
        print(AEWizValue.inserito.getDescrizione(), listaWiz);
    }

    @Test
    @Order(8)
    @DisplayName("8 - service.getWizDerivati")
    void getWizDerivati() {
        listaWiz = service.getWizValueDerivati();
        print(AEWizValue.derivato.getDescrizione(), listaWiz);
    }

    @Test
    @Order(9)
    @DisplayName("9 - service.getWizUsoNullo")
    void getWizUsoNullo() {
        listaWiz = service.getWizUsoNullo();
        print(AEWizUso.nullo.getDescrizione(), listaWiz);
    }

    @Test
    @Order(10)
    @DisplayName("10 - service.getWizUsoProject")
    void getWizUsoProject() {
        listaWiz = service.getWizUsoProject();
        print(AEWizUso.flagProject.getDescrizione(), listaWiz);
    }

    @Test
    @Order(11)
    @DisplayName("11 - service.getWizUsoPackage")
    void getWizUsoPackage() {
        listaWiz = service.getWizUsoPackage();
        print(AEWizUso.flagPackages.getDescrizione(), listaWiz);
    }


    @Test
    @Order(12)
    @DisplayName("12 - All tipo AETypeFile.nome")
    void getNome() {
        listaWiz = service.getNome();
        print(AEWizCopy.nome.name(), listaWiz);
    }

    @Test
    @Order(13)
    @DisplayName("13 - All tipo AETypeFile.file")
    void getFile() {
        listaWiz = service.getFile();
        print(AEWizCopy.file.name(), listaWiz);
    }

    @Test
    @Order(14)
    @DisplayName("14 - All tipo AETypeFile.source")
    void getSource() {
        listaWiz = service.getSource();
        print(AEWizCopy.source.name(), listaWiz);
    }

    @Test
    @Order(15)
    @DisplayName("15 - All tipo AETypeFile.dir")
    void getDir() {
        listaWiz = service.getDir();
        print(AEWizCopy.dir.name(), listaWiz);
    }

    @Test
    @Order(16)
    @DisplayName("16 - All tipo AETypeFile.path")
    void getPath() {
        listaWiz = service.getPath();
        print(AEWizCopy.path.name(), listaWiz);
    }


    @Test
    @Order(17)
    @DisplayName("17 - getValide")
    void getValorizzate() {
        listaWiz = service.getHannoValore();
        print("Costanti che hanno un valore valido tra quelle che dovrebbero averlo", listaWiz);
    }


    @Test
    @Order(18)
    @DisplayName("18 - getVuote")
    void getVuote() {
        listaWiz = service.getVuote();
        print("Costanti a cui manca un valore indispensabile", listaWiz);
    }

    @Test
    @Order(19)
    @DisplayName("19 - reset")
    void reset() {
        previstoBooleano = false;
        previsto = VALORE_MANCANTE;
        ottenutoBooleano = AEWizCost.nameCurrentProjectUpper.isValida();
        ottenuto = AEWizCost.nameCurrentProjectUpper.getValue();
        assertEquals(previsto, ottenuto);
        assertEquals(previstoBooleano, ottenutoBooleano);

        previsto = NAME_VAADFLOW;
        FlowVar.projectNameUpper = previsto;
        service.fixAEWizCost();

        previstoBooleano = true;
        ottenutoBooleano = AEWizCost.nameCurrentProjectUpper.isValida();
        ottenuto = AEWizCost.nameCurrentProjectUpper.getValue();
        assertEquals(previsto, ottenuto);
        assertEquals(previstoBooleano, ottenutoBooleano);

        AEWizCost.reset();

        previstoBooleano = false;
        previsto = VALORE_MANCANTE;
        ottenutoBooleano = AEWizCost.nameCurrentProjectUpper.isValida();
        ottenuto = AEWizCost.nameCurrentProjectUpper.getValue();
        assertEquals(previsto, ottenuto);
        assertEquals(previstoBooleano, ottenutoBooleano);
    }


    @Test
    @Order(20)
    @DisplayName("20 - getNecessitanoValore")
    void getNecessitanoValore() {
        listaWiz = service.getNecessitanoInserimentoValore();
        print("Costanti che hanno bisogno di un valore in runtime", listaWiz);
    }


    private void printAll(String titolo, List<AEWizCost> lista) {
        String serve = " (serve valore) ";
        String nonServe = " (non serve valore) ";
        String sep = DUE_PUNTI_SPAZIO;
        String sep2 = UGUALE_SPAZIATO;
        String sep3 = SEP;
        String riga;

        System.out.print(titolo);
        System.out.println(" (" + lista.size() + ")");
        System.out.println(VUOTA);
        if (arrayService.isAllValid(lista)) {
            for (AEWizCost wiz : lista) {
                riga = VUOTA;
                riga += wiz.getWizValue();
                riga += sep3;
                riga += wiz.name();
                riga += sep;
                riga += wiz.getDescrizione();
                riga += sep2 + wiz.get();

                System.out.println(riga);
            }
        }
    }


    private void print(String titolo, List<AEWizCost> lista) {
        String serve = " (serve valore) ";
        String nonServe = " (non serve valore) ";
        String sep = DUE_PUNTI_SPAZIO;
        String sep2 = UGUALE_SPAZIATO;
        String sep3 = SEP;
        String riga;

        System.out.print(titolo);
        System.out.println(" (" + lista.size() + ")");
        System.out.println(VUOTA);
        if (arrayService.isAllValid(lista)) {
            for (AEWizCost wiz : lista) {
                riga = VUOTA;
                riga += wiz.name();
                riga += sep;
                riga += wiz.getDescrizione();
                riga += sep2 + wiz.get();

                System.out.println(riga);
            }
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