package it.algos.test;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.packages.crono.anno.*;
import it.algos.vaadflow14.backend.packages.crono.giorno.*;
import it.algos.vaadwiki.backend.packages.attivita.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import it.algos.vaadwiki.backend.packages.nazionalita.*;
import it.algos.vaadwiki.backend.service.*;
import it.algos.vaadwiki.wiki.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 16-ago-2021
 * Time: 14:45
 */
public abstract class WTest extends ATest {

    protected static final String PAGINA_UNO = "Roman Protasevič";

    protected static final String PAGINA_DUE = "Gaetano Anzalone";

    protected static final String PAGINA_TRE = "Bernart Arnaut d'Armagnac";

    protected static final String PAGINA_QUATTRO = "Francesco Maria Pignatelli";

    protected static final String PAGINA_CINQUE = "Colin Campbell (generale)";

    protected static final String PAGINA_SEI = "Edwin Hall";

    protected static final String PAGINA_SETTE = "Louis Winslow Austin";

    protected static final String PAGINA_DISAMBIGUA = "Rossi";

    protected static final String PAGINA_REDIRECT = "Regno di Napoli (1805-1815)";

    protected String didascalia;

    protected WrapBio wrap;

    protected Bio bio;


    @InjectMocks
    protected GiornoService giornoService;

    @InjectMocks
    protected AnnoService annoService;

    @InjectMocks
    protected AttivitaService attivitaService;

    @InjectMocks
    protected NazionalitaService nazionalitaService;

    @InjectMocks
    protected DidascaliaService didascaliaService;

    @InjectMocks
    protected WikiBotService wikiBotService;

    @InjectMocks
    protected BioUtility bioUtilityService;

    @InjectMocks
    protected BioService bioService;

    @InjectMocks
    protected ElaboraService elaboraService;

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpIniziale() {
        super.setUpStartUp();

        initMocks();
        fixRiferimentiIncrociati();
    }


    /**
     * Inizializzazione dei service
     * Devono essere tutti 'mockati' prima di iniettare i riferimenti incrociati <br>
     */
    protected void initMocks() {
        MockitoAnnotations.initMocks(giornoService);
        Assertions.assertNotNull(giornoService);

        MockitoAnnotations.initMocks(annoService);
        Assertions.assertNotNull(annoService);

        MockitoAnnotations.initMocks(attivitaService);
        Assertions.assertNotNull(attivitaService);

        MockitoAnnotations.initMocks(nazionalitaService);
        Assertions.assertNotNull(nazionalitaService);

        MockitoAnnotations.initMocks(didascaliaService);
        Assertions.assertNotNull(didascaliaService);

        MockitoAnnotations.initMocks(wikiBotService);
        Assertions.assertNotNull(wikiBotService);

        MockitoAnnotations.initMocks(bioUtilityService);
        Assertions.assertNotNull(bioUtilityService);

        MockitoAnnotations.initMocks(bioService);
        Assertions.assertNotNull(bioService);

        MockitoAnnotations.initMocks(elaboraService);
        Assertions.assertNotNull(elaboraService);
    }

    /**
     * Regola tutti riferimenti incrociati <br>
     * Deve essere fatto dopo aver costruito le referenze 'mockate' <br>
     * Nelle sottoclassi di testi devono essere regolati i riferimenti dei service specifici <br>
     */
    protected void fixRiferimentiIncrociati() {
    }

    protected void print(final Bio bio, final String nomeCognome, final String attivitaNazionalita) {
        System.out.println(VUOTA);
        System.out.println(String.format("Titolo effettivo della voce: %s", sorgente));
        System.out.println(String.format("NomeCognome: %s", nomeCognome));
        System.out.println(String.format("AttivitaNazionalita: %s", attivitaNazionalita));
    }

    protected void print(final Bio bio, final String ottenuto) {
        print(bio.wikiTitle, bio.nome, bio.cognome, ottenuto);
    }

    protected void print(final String sorgente, final String sorgente2, final String sorgente3, final String ottenuto) {
        System.out.println(VUOTA);
        System.out.println(String.format("Titolo effettivo della voce: %s", sorgente));
        System.out.println(String.format("Nome: %s", sorgente2));
        System.out.println(String.format("Cognome: %s", sorgente3));
        System.out.println(String.format("Didascalia: %s", ottenuto));
    }

    protected void printWrapBio(WrapBio wrap) {
        System.out.println("WrapBio");
        System.out.println(VUOTA);
        System.out.println("Wrap valido: " + wrap.isValido());
        System.out.println("Titolo:" + SPAZIO + wrap.getTitle());
        System.out.println("PageId:" + SPAZIO + wrap.getPageid());
        System.out.println("Type:" + SPAZIO + wrap.getType());
        System.out.println("Timestamp:" + SPAZIO + wrap.getTime());
        System.out.println("Template:" + SPAZIO + wrap.getTemplBio());
    }

}
