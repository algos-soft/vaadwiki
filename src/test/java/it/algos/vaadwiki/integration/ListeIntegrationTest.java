package it.algos.vaadwiki.integration;

import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadwiki.ATest;
import it.algos.vaadwiki.application.WikiCost;
import it.algos.vaadwiki.didascalia.EADidascalia;
import it.algos.vaadwiki.didascalia.WrapDidascalia;
import it.algos.vaadwiki.liste.ListaAnnoNato;
import it.algos.vaadwiki.liste.ListaGiornoNato;
import it.algos.vaadwiki.liste.ListaNomi;
import it.algos.vaadwiki.liste.ListaService;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.vaadwiki.modules.nome.Nome;
import it.algos.vaadwiki.modules.nome.NomeService;
import it.algos.wiki.LibWiki;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 15-set-2019
 * Time: 08:19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ListeIntegrationTest extends ATest {

    private static String NOME_BIO_UNO = "Ron Clarke";

    private static boolean USA_PARAGRAFI_GIORNI;

    private static String TAG_PARAGRAFO_VUOTO_GIORNI_NASCITA;

    private static boolean IS_PARAGRAFO_VUOTO_GIORNI_IN_CODA;

    private static boolean USA_RIGHE_RAGGRUPPATE_GIORNI;

    private static boolean USA_PARAGRAFO_SIZE_GIORNI;

    private static boolean USA_PARAGRAFI_ANNI;

    private static String TAG_PARAGRAFO_VUOTO_ANNI_NASCITA;

    private static boolean IS_PARAGRAFO_VUOTO_ANNI_IN_CODA;

    private static boolean USA_RIGHE_RAGGRUPPATE_ANNI;

    private static boolean USA_PARAGRAFO_SIZE_ANNI;

    private static String TAG_PARAGRAFO_VUOTO_NOMI_COGNOMI;

    private static boolean IS_PARAGRAFO_VUOTO_NOMI_IN_CODA;

    private static boolean USA_PARAGRAFO_SIZE_NOMI;


    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappa;

    protected List<Bio> listaBio;

    protected List<WrapDidascalia> listaWrap;

    protected List<String> listaDidascalieText;

    @Autowired
    protected BioService bioService;

    @Autowired
    protected NomeService nomeService;

    @Autowired
    protected GiornoService giornoService;

    @Autowired
    protected AnnoService annoService;

    @Autowired
    protected PreferenzaService prefService;

    @Autowired
    protected ListaService listaService;

    @Autowired
    protected ADateService dateService;

    protected Anno annoEntity;

    protected Giorno giornoEntity;

    protected Nome nomeEntity;

    protected ListaNomi listaNome;

    protected ListaGiornoNato listaGiorno;

    protected ListaAnnoNato listaAnno;

    protected String testo;

    @Autowired
    private ApplicationContext appContext;

    private String annoText = "1924";

    private String giornoText = "3 marzo";

    private String nomeTextCorto = "Rita";

    private String nomeTextLungo = "Giovanni";

    private int posIni = 35;

    private int posEnd = 45;


    @Before
    public void setUpIniziale() {
        Assert.assertNotNull(text);
        Assert.assertNotNull(giornoService);
        Assert.assertNotNull(nomeService);
    }// end of method


    public void testUnico() {
        annoEntity = annoService.findByKeyUnica(annoText);
        Assert.assertNotNull(annoEntity);
        listaAnno = appContext.getBean(ListaAnnoNato.class, annoEntity);
        Assert.assertNotNull(listaAnno);

        giornoEntity = giornoService.findByKeyUnica(giornoText);
        Assert.assertNotNull(giornoEntity);
        listaGiorno = appContext.getBean(ListaGiornoNato.class, giornoEntity);
        Assert.assertNotNull(listaGiorno);

        nomeEntity = nomeService.findByKeyUnica(nomeTextCorto);
        Assert.assertNotNull(nomeEntity);
        listaNome = appContext.getBean(ListaNomi.class, nomeEntity);
        Assert.assertNotNull(listaNome);

//        anno();
//        giorno();
//        nome();
    }// end of single test


    //    @Test
    public void titoloParagrafo() {
        Bio bio = bioService.findByKeyUnica(NOME_BIO_UNO);
        ottenuto = listaService.getTitoloParagrafo(bio, "pippoz");
    }// end of single test


    //    @Test
    public void timingTitoloParagrafo() {
        long inizio;
        ArrayList<Bio> listaGrezzaBio = bioService.findAllByNome(nomeTextLungo);
        ArrayList<WrapDidascalia> listaDidascalie = listaService.creaListaDidascalie(listaGrezzaBio, EADidascalia.listaNomi);

        inizio = System.currentTimeMillis();
        mappa = listaService.creaMappa(listaDidascalie);
        System.out.println("Mappa complessa tempo impiegato: " + dateService.deltaText(inizio));
    }// end of single test


    private ListaGiornoNato getGiorno() {
        giornoEntity = giornoService.findByKeyUnica(giornoText);
        Assert.assertNotNull(giornoEntity);
        listaGiorno = appContext.getBean(ListaGiornoNato.class, giornoEntity);
        Assert.assertNotNull(listaGiorno);

        return listaGiorno;
    }// end of method


    private ListaGiornoNato fixGiornoSenzaParagrafi() {
        previstoBooleano = false;
        USA_PARAGRAFI_GIORNI = prefService.isBool(WikiCost.USA_PARAGRAFI_GIORNI);
        prefService.saveValue(WikiCost.USA_PARAGRAFI_GIORNI, previstoBooleano);
        ottenutoBooleano = prefService.isBool(WikiCost.USA_PARAGRAFI_GIORNI);
        Assert.assertEquals(ottenutoBooleano, previstoBooleano);

        previstoBooleano = true;
        USA_RIGHE_RAGGRUPPATE_GIORNI = prefService.isBool(WikiCost.USA_RIGHE_RAGGRUPPATE_GIORNI);
        prefService.saveValue(WikiCost.USA_RIGHE_RAGGRUPPATE_GIORNI, previstoBooleano);
        ottenutoBooleano = prefService.isBool(WikiCost.USA_RIGHE_RAGGRUPPATE_GIORNI);
        Assert.assertEquals(ottenutoBooleano, previstoBooleano);

        TAG_PARAGRAFO_VUOTO_GIORNI_NASCITA = prefService.getStr(WikiCost.TAG_PARAGRAFO_VUOTO_GIORNI_NASCITA);
        IS_PARAGRAFO_VUOTO_GIORNI_IN_CODA = prefService.isBool(WikiCost.IS_PARAGRAFO_VUOTO_GIORNI_IN_CODA);
        USA_PARAGRAFO_SIZE_GIORNI = prefService.isBool(WikiCost.USA_PARAGRAFO_SIZE_GIORNI);

        return getGiorno();
    }// end of method


    private ListaGiornoNato fixGiornoConParagrafi() {
        previstoBooleano = true;
        USA_PARAGRAFI_GIORNI = prefService.isBool(WikiCost.USA_PARAGRAFI_GIORNI);
        prefService.saveValue(WikiCost.USA_PARAGRAFI_GIORNI, previstoBooleano);
        ottenutoBooleano = prefService.isBool(WikiCost.USA_PARAGRAFI_GIORNI);
        Assert.assertEquals(ottenutoBooleano, previstoBooleano);

        previstoBooleano = true;
        USA_RIGHE_RAGGRUPPATE_GIORNI = prefService.isBool(WikiCost.USA_RIGHE_RAGGRUPPATE_GIORNI);
        prefService.saveValue(WikiCost.USA_RIGHE_RAGGRUPPATE_GIORNI, previstoBooleano);
        ottenutoBooleano = prefService.isBool(WikiCost.USA_RIGHE_RAGGRUPPATE_GIORNI);
        Assert.assertEquals(ottenutoBooleano, previstoBooleano);

        previsto = "Senza anno di nascita conosciuto";
        TAG_PARAGRAFO_VUOTO_GIORNI_NASCITA = prefService.getStr(WikiCost.TAG_PARAGRAFO_VUOTO_GIORNI_NASCITA);
        prefService.saveValue(WikiCost.TAG_PARAGRAFO_VUOTO_GIORNI_NASCITA, previsto);
        ottenuto = prefService.getStr(WikiCost.TAG_PARAGRAFO_VUOTO_GIORNI_NASCITA);
        Assert.assertEquals(ottenuto, previsto);

        previstoBooleano = false;
        IS_PARAGRAFO_VUOTO_GIORNI_IN_CODA = prefService.isBool(WikiCost.IS_PARAGRAFO_VUOTO_GIORNI_IN_CODA);
        prefService.saveValue(WikiCost.IS_PARAGRAFO_VUOTO_GIORNI_IN_CODA, previstoBooleano);
        ottenutoBooleano = prefService.isBool(WikiCost.IS_PARAGRAFO_VUOTO_GIORNI_IN_CODA);
        Assert.assertEquals(ottenutoBooleano, previstoBooleano);

        previstoBooleano = true;
        USA_PARAGRAFO_SIZE_GIORNI = prefService.isBool(WikiCost.USA_PARAGRAFO_SIZE_GIORNI);
        prefService.saveValue(WikiCost.USA_PARAGRAFO_SIZE_GIORNI, previstoBooleano);
        ottenutoBooleano = prefService.isBool(WikiCost.USA_PARAGRAFO_SIZE_GIORNI);
        Assert.assertEquals(ottenutoBooleano, previstoBooleano);

        return getGiorno();
    }// end of method


    private void resetPreferenzeGiorno() {
        prefService.saveValue(WikiCost.USA_PARAGRAFI_GIORNI, USA_PARAGRAFI_GIORNI);
        prefService.saveValue(WikiCost.USA_RIGHE_RAGGRUPPATE_GIORNI, USA_RIGHE_RAGGRUPPATE_GIORNI);
        prefService.saveValue(WikiCost.TAG_PARAGRAFO_VUOTO_GIORNI_NASCITA, TAG_PARAGRAFO_VUOTO_GIORNI_NASCITA);
        prefService.saveValue(WikiCost.IS_PARAGRAFO_VUOTO_GIORNI_IN_CODA, IS_PARAGRAFO_VUOTO_GIORNI_IN_CODA);
        prefService.saveValue(WikiCost.USA_PARAGRAFO_SIZE_GIORNI, USA_PARAGRAFO_SIZE_GIORNI);
    }// end of method


    /**
     * Lista dei giorni.
     * Può usare i paragrafi oppure no.
     * Senza paragrafi e con righe raggruppate (le righe non raggruppate non vengono usate anche se teoricamente possibili).
     * <p>
     * Regola le preferenze da usare nella Lista.
     * I valori attuali del mongoDB in produzione vengono alterati.
     * Se tutto va bene, vengono ripristinati al termine del test.
     * In caso di uscita dal test per errore, vanno controllati.
     */
    @Test
    public void listaGiorniSenzaParagrafi() {
        listaGiorno = fixGiornoSenzaParagrafi();

        mappa = listaGiorno.getMappa();
        Assert.assertNotNull(mappa);

        System.out.println("*************");
        System.out.println("listaService.righeSenzaParagrafo");
        System.out.println("Lista dei primi " + listaGiorno.size + " nati il giorno " + giornoText + " - Senza paragrafi e con righe raggruppate (le righe non raggruppate non vengono usate anche se teoricamente possibili)");
        System.out.println("*************");
        testo = listaService.righeSenzaParagrafo(mappa);
        System.out.println(testo);
        System.out.println("");

        resetPreferenzeGiorno();
    }// end of single test


    /**
     * Lista dei giorni.
     * Può usare i paragrafi oppure no.
     * Test con paragrafi
     * Test con titolo del paragrafo senza anno di nascita = "Pippoz"
     * Test col paragrafo senza titolo in coda
     * Test con righe raggruppate (le righe non raggruppate non vengono usate anche se teoricamente possibili).
     * Test senza dimensioni del paragrafo indicate
     * <p>
     * Regola le preferenze da usare nella Lista.
     * I valori attuali del mongoDB in produzione vengono alterati.
     * Se tutto va bene, vengono ripristinati al termine del test.
     * In caso di uscita dal test per errore, vanno controllati.
     */
    @Test
    public void listaGiorniConParagrafi() {
        listaGiorno = fixGiornoConParagrafi();

        mappa = listaGiorno.getMappa();
        Assert.assertNotNull(mappa);

        System.out.println("*************");
        System.out.println("listaService.righeConParagrafo");
        System.out.println("Lista dei primi " + listaGiorno.size + " nati il giorno " + giornoText + " - Con paragrafi, righe raggruppate, titolo vuoto 'Pippoz' in coda");
        System.out.println("*************");
        testo = listaService.righeConParagrafo(mappa);
        System.out.println(testo);
        System.out.println("");

        resetPreferenzeGiorno();
    }// end of single test


    /**
     * Lista dei giorni.
     * Può usare i paragrafi oppure no.
     * Test con paragrafi
     * Test con titolo del paragrafo senza anno di nascita = "Pippoz"
     * Test col paragrafo senza titolo in coda
     * Test con righe raggruppate (le righe non raggruppate non vengono usate anche se teoricamente possibili).
     * Test con le dimensioni del paragrafo indicate
     * <p>
     * Regola le preferenze da usare nella Lista.
     * I valori attuali del mongoDB in produzione vengono alterati.
     * Se tutto va bene, vengono ripristinati al termine del test.
     * In caso di uscita dal test per errore, vanno controllati.
     */
    @Test
    public void listaGiorniConParagrafiSize() {
        listaGiorno = fixGiornoConParagrafi();

        mappa = listaGiorno.getMappa();
        Assert.assertNotNull(mappa);

        System.out.println("*************");
        System.out.println("listaService.righeConParagrafoSize");
        System.out.println("Lista dei primi " + listaGiorno.size + " nati il giorno " + giornoText + " - Con paragrafi, righe raggruppate, titolo vuoto 'Pippoz' in coda, dimensioni del paragrafo");
        System.out.println("*************");
        testo = listaService.righeConParagrafoSize(mappa);
        System.out.println(testo);
        System.out.println("");

        resetPreferenzeGiorno();
    }// end of single test


    /**
     * Lista dei giorni attiva da preferenze della lista.
     * Senza paragrafi e con righe raggruppate (le righe non raggruppate non vengono usate anche se teoricamente possibili).
     * <p>
     * Regola le preferenze da usare nella Lista.
     * I valori attuali del mongoDB in produzione vengono alterati.
     * Se tutto va bene, vengono ripristinati al termine del test.
     * In caso di uscita dal test per errore, vanno controllati.
     */
    @Test
    public void listaGiorniInUso() {
        listaGiorno = fixGiornoConParagrafi();

        System.out.println("*************");
        System.out.println("listaGiorno.getTesto");
        System.out.println("Lista dei primi " + listaGiorno.size + " nati il giorno " + giornoText + " come da preferenze correnti");
        System.out.println("*************");
        testo = listaGiorno.getTesto();
        System.out.println(testo);
        System.out.println("");

        resetPreferenzeGiorno();
    }// end of single test


    private ListaAnnoNato getAnno() {
        annoEntity = annoService.findByKeyUnica(annoText);
        Assert.assertNotNull(annoEntity);
        listaAnno = appContext.getBean(ListaAnnoNato.class, annoEntity);
        Assert.assertNotNull(listaAnno);

        return listaAnno;
    }// end of method


    private ListaAnnoNato fixAnnoSenzaParagrafi() {
        previstoBooleano = false;
        USA_PARAGRAFI_ANNI = prefService.isBool(WikiCost.USA_PARAGRAFI_ANNI);
        prefService.saveValue(WikiCost.USA_PARAGRAFI_ANNI, previstoBooleano);
        ottenutoBooleano = prefService.isBool(WikiCost.USA_PARAGRAFI_ANNI);
        Assert.assertEquals(ottenutoBooleano, previstoBooleano);

        previstoBooleano = true;
        USA_RIGHE_RAGGRUPPATE_ANNI = prefService.isBool(WikiCost.USA_RIGHE_RAGGRUPPATE_ANNI);
        prefService.saveValue(WikiCost.USA_RIGHE_RAGGRUPPATE_ANNI, previstoBooleano);
        ottenutoBooleano = prefService.isBool(WikiCost.USA_RIGHE_RAGGRUPPATE_ANNI);
        Assert.assertEquals(ottenutoBooleano, previstoBooleano);

        TAG_PARAGRAFO_VUOTO_ANNI_NASCITA = prefService.getStr(WikiCost.TAG_PARAGRAFO_VUOTO_ANNI_NASCITA);
        IS_PARAGRAFO_VUOTO_ANNI_IN_CODA = prefService.isBool(WikiCost.IS_PARAGRAFO_VUOTO_ANNI_IN_CODA);

        return getAnno();
    }// end of method


    private ListaAnnoNato fixAnnoConParagrafi() {
        previstoBooleano = false;
        USA_PARAGRAFI_ANNI = prefService.isBool(WikiCost.USA_PARAGRAFI_ANNI);
        prefService.saveValue(WikiCost.USA_PARAGRAFI_ANNI, previstoBooleano);
        ottenutoBooleano = prefService.isBool(WikiCost.USA_PARAGRAFI_ANNI);
        Assert.assertEquals(ottenutoBooleano, previstoBooleano);

        previstoBooleano = true;
        USA_RIGHE_RAGGRUPPATE_ANNI = prefService.isBool(WikiCost.USA_RIGHE_RAGGRUPPATE_ANNI);
        prefService.saveValue(WikiCost.USA_RIGHE_RAGGRUPPATE_ANNI, previstoBooleano);
        ottenutoBooleano = prefService.isBool(WikiCost.USA_RIGHE_RAGGRUPPATE_ANNI);
        Assert.assertEquals(ottenutoBooleano, previstoBooleano);

        previsto = "Diabolik";
        TAG_PARAGRAFO_VUOTO_ANNI_NASCITA = prefService.getStr(WikiCost.TAG_PARAGRAFO_VUOTO_ANNI_NASCITA);
        prefService.saveValue(WikiCost.TAG_PARAGRAFO_VUOTO_ANNI_NASCITA, previsto);
        ottenuto = prefService.getStr(WikiCost.TAG_PARAGRAFO_VUOTO_ANNI_NASCITA);
        Assert.assertEquals(ottenuto, previsto);

        previstoBooleano = false;
        IS_PARAGRAFO_VUOTO_ANNI_IN_CODA = prefService.isBool(WikiCost.IS_PARAGRAFO_VUOTO_ANNI_IN_CODA);
        prefService.saveValue(WikiCost.IS_PARAGRAFO_VUOTO_ANNI_IN_CODA, previstoBooleano);
        ottenutoBooleano = prefService.isBool(WikiCost.IS_PARAGRAFO_VUOTO_ANNI_IN_CODA);
        Assert.assertEquals(ottenutoBooleano, previstoBooleano);

        return getAnno();
    }// end of method


    private void resetPreferenzeAnno() {
        prefService.saveValue(WikiCost.USA_PARAGRAFI_ANNI, USA_PARAGRAFI_ANNI);
        prefService.saveValue(WikiCost.USA_RIGHE_RAGGRUPPATE_ANNI, USA_RIGHE_RAGGRUPPATE_ANNI);
        prefService.saveValue(WikiCost.TAG_PARAGRAFO_VUOTO_ANNI_NASCITA, TAG_PARAGRAFO_VUOTO_ANNI_NASCITA);
        prefService.saveValue(WikiCost.IS_PARAGRAFO_VUOTO_ANNI_IN_CODA, IS_PARAGRAFO_VUOTO_ANNI_IN_CODA);
    }// end of method


    /**
     * Lista degli anni.
     * Può usare i paragrafi oppure no.
     * Senza paragrafi e con righe raggruppate (le righe non raggruppate non vengono usate anche se teoricamente possibili).
     * <p>
     * Regola le preferenze da usare nella Lista.
     * I valori attuali del mongoDB in produzione vengono alterati.
     * Se tutto va bene, vengono ripristinati al termine del test.
     * In caso di uscita dal test per errore, vanno controllati.
     */
//    @Test
    public void listaAnniSenzaParagrafi() {
        listaAnno = fixAnnoSenzaParagrafi();

//        mappaSemplice = listaAnno.mappaSemplice;
//        Assert.assertNotNull(mappaSemplice);
        mappa = listaAnno.getMappa();
        Assert.assertNull(mappa);

        System.out.println("*************");
        System.out.println("listaService.senzaParagrafi");
        System.out.println("Lista dei primi " + listaAnno.size + " nati nell'anno " + annoText + " - Senza paragrafi e con righe raggruppate (le righe non raggruppate non vengono usate anche se teoricamente possibili)");
        System.out.println("*************");
        testo = listaService.righeSenzaParagrafo(mappa);
        System.out.println(testo);
        System.out.println("");

        resetPreferenzeGiorno();
    }// end of single test


    /**
     * Lista degli anni.
     * Può usare i paragrafi oppure no.
     * Test con paragrafi
     * Test con titolo del paragrafo senza anno di nascita = "Pippoz"
     * Test col paragrafo senza titolo in coda
     * Test con righe raggruppate (le righe non raggruppate non vengono usate anche se teoricamente possibili).
     * <p>
     * Regola le preferenze da usare nella Lista.
     * I valori attuali del mongoDB in produzione vengono alterati.
     * Se tutto va bene, vengono ripristinati al termine del test.
     * In caso di uscita dal test per errore, vanno controllati.
     */
//    @Test
    public void listaAnniConParagrafi() {
        listaGiorno = fixGiornoConParagrafi();

//        mappaSemplice = listaAnno.mappaSemplice;
//        Assert.assertNotNull(mappaSemplice);
//        mappaComplessa = listaAnno.mappaComplessa;
        Assert.assertNull(mappa);

        System.out.println("*************");
        System.out.println("listaService.paragrafoSenzaSize");
        System.out.println("Lista dei primi " + listaAnno.size + " nati nell'anno " + annoText + " - Con paragrafi, righe raggruppate, titolo vuoto 'Diabolik' in coda");
        System.out.println("*************");
        testo = listaService.righeSenzaParagrafo(mappa);
        System.out.println(testo);
        System.out.println("");

        resetPreferenzeGiorno();
    }// end of single test

//        public void anno() {
//        //--costruisco qui la mappa semplice perché listaAnno ha la preferenza usaSuddivisioneParagrafi=true
//        //--se cambio la preferenza nel mongoDb, devo cambiare anche qui
//        ArrayList<Bio> listaGrezzaBio = bioService.findAllByAnnoNascita(annoEntity);
//        ArrayList<WrapDidascalia> listaDidascalie = listaService.creaListaDidascalie(listaGrezzaBio, EADidascalia.annoNato);
//        mappaSemplice = listaService.creaMappaQuadre(listaDidascalie);
//        //--end
//        Assert.assertNotNull(mappaSemplice);
//        mappaComplessa = listaAnno.mappaComplessa;
//        Assert.assertNotNull(mappaComplessa);
//
//        System.out.println("*************");
//        System.out.println("Mappa semplice anno - Righe semplici (escluso per gli anni)");
//        System.out.println("*************");
//        testo = listaService.righeSemplici(mappaSemplice);
//        System.out.println(testo);
//        System.out.println("");
//
//        System.out.println("*************");
//        System.out.println("Mappa semplice anno - Righe raggruppate (opzione valida per gli anni)");
//        System.out.println("*************");
//        testo = listaService.senzaParagrafi(mappaSemplice);
//        System.out.println(testo);
//        System.out.println("");
//
//        System.out.println("*************");
//        System.out.println("Mappa complessa anno - Paragrafi e righe raggruppate (opzione valida per gli anni)");
//        System.out.println("*************");
//        testo = listaService.paragrafoSenzaSize(mappaComplessa);
//        System.out.println(testo);
//        System.out.println("");
//    }// end of single test


    public ListaNomi getNome() {
        nomeEntity = nomeService.findByKeyUnica(nomeTextCorto);
        Assert.assertNotNull(nomeEntity);
        listaNome = appContext.getBean(ListaNomi.class, nomeEntity);
        Assert.assertNotNull(listaNome);

        return listaNome;
    }// end of method


    private ListaNomi fixNomi() {
        previsto = "Pippoz";
        TAG_PARAGRAFO_VUOTO_NOMI_COGNOMI = prefService.getStr(WikiCost.TAG_PARAGRAFO_VUOTO_NOMI_COGNOMI);
        prefService.saveValue(WikiCost.TAG_PARAGRAFO_VUOTO_NOMI_COGNOMI, previsto);
        ottenuto = prefService.getStr(WikiCost.TAG_PARAGRAFO_VUOTO_NOMI_COGNOMI);
        Assert.assertEquals(ottenuto, previsto);

        previstoBooleano = true;
        IS_PARAGRAFO_VUOTO_NOMI_IN_CODA = prefService.isBool(WikiCost.IS_PARAGRAFO_VUOTO_NOMI_IN_CODA);
        prefService.saveValue(WikiCost.IS_PARAGRAFO_VUOTO_NOMI_IN_CODA, previstoBooleano);
        ottenutoBooleano = prefService.isBool(WikiCost.IS_PARAGRAFO_VUOTO_NOMI_IN_CODA);
        Assert.assertEquals(ottenutoBooleano, previstoBooleano);

        previstoBooleano = true;
        USA_PARAGRAFO_SIZE_NOMI = prefService.isBool(WikiCost.USA_PARAGRAFO_SIZE_NOMI);
        prefService.saveValue(WikiCost.USA_PARAGRAFO_SIZE_NOMI, previstoBooleano);
        ottenutoBooleano = prefService.isBool(WikiCost.USA_PARAGRAFO_SIZE_NOMI);
        Assert.assertEquals(ottenutoBooleano, previstoBooleano);

        return getNome();
    }// end of method


    private void resetPreferenzeNome() {
        prefService.saveValue(WikiCost.TAG_PARAGRAFO_VUOTO_NOMI_COGNOMI, TAG_PARAGRAFO_VUOTO_NOMI_COGNOMI);
        prefService.saveValue(WikiCost.IS_PARAGRAFO_VUOTO_NOMI_IN_CODA, IS_PARAGRAFO_VUOTO_NOMI_IN_CODA);
        prefService.saveValue(WikiCost.USA_PARAGRAFO_SIZE_NOMI, USA_PARAGRAFO_SIZE_NOMI);
    }// end of method


    /**
     * Lista dei nomi.
     * Può usare i paragrafi oppure no.
     * Test con paragrafi
     * Test con titolo del paragrafo senza attività = "Pippoz"
     * Test col paragrafo senza titolo in coda
     * Test con righe raggruppate (le righe non raggruppate non vengono usate anche se teoricamente possibili).
     * Test con le dimensioni del paragrafo indicate
     * <p>
     * Regola le preferenze da usare nella Lista.
     * I valori attuali del mongoDB in produzione vengono alterati.
     * Se tutto va bene, vengono ripristinati al termine del test.
     * In caso di uscita dal test per errore, vanno controllati.
     */
    @Test
    public void listaNomi() {
        listaNome = fixNomi();

        mappa = listaNome.getMappa();
        Assert.assertNotNull(mappa);

        System.out.println("*************");
        System.out.println("listaService.righeConParagrafoSize");
        System.out.println("Lista dei " + listaNome.size + " biografati di nome " + nomeTextCorto + " - Con paragrafi e con le dimensioni nel titolo del paragrafo");
        System.out.println("*************");
        testo = listaService.righeConParagrafoSize(mappa);
        System.out.println(testo);
        System.out.println("");

        resetPreferenzeNome();
    }// end of single test


    /**
     * Lista dei nomi attiva da preferenze della lista.
     * Con paragrafi e con le dimensioni nel titolo.
     * <p>
     * Regola le preferenze da usare nella Lista.
     * I valori attuali del mongoDB in produzione vengono alterati.
     * Se tutto va bene, vengono ripristinati al termine del test.
     * In caso di uscita dal test per errore, vanno controllati.
     */
    @Test
    public void listaNomiInUso() {
        listaNome = fixNomi();

        System.out.println("*************");
        System.out.println("listaNome.getTesto");
        System.out.println("Lista dei " + listaNome.size + " biografati di nome " + nomeTextCorto + " come da preferenze correnti");
        System.out.println("*************");
        testo = listaNome.getTesto();
        System.out.println(testo);
        System.out.println("");

        resetPreferenzeNome();
    }// end of single test


    /**
     * Costruisce il titolo del paragrafo
     * <p>
     * Questo deve essere composto da:
     * Professione.pagina
     * Genere.plurale
     */
    @Test
    public void getProfessioneDaAttivita() {
        previsto = "Attivismo";

        sorgente = "attivista";
        ottenuto = listaService.getProfessioneDaAttivitaSingolare(sorgente);
        Assert.assertEquals(ottenuto, previsto);

        sorgente = "Attivista";
        ottenuto = listaService.getProfessioneDaAttivitaSingolare(sorgente);
        Assert.assertEquals(ottenuto, previsto);

        previsto = "Accademico";
        sorgente = "Accademica";
        ottenuto = listaService.getProfessioneDaAttivitaSingolare(sorgente);
        Assert.assertEquals(ottenuto, previsto);
        sorgente = "Accademico";
        ottenuto = listaService.getProfessioneDaAttivitaSingolare(sorgente);
//        Assert.assertEquals(ottenuto, previsto);
//        sorgente = "Professore universitario";
//        ottenuto = listaService.getProfessioneDaAttivitaSingolare(sorgente);
//        Assert.assertEquals(ottenuto, previsto);
//        sorgente = "Professoressa universitaria";
//        ottenuto = listaService.getProfessioneDaAttivitaSingolare(sorgente);
//        Assert.assertEquals(ottenuto, previsto);
//        sorgente = "accademica";
//        ottenuto = listaService.getProfessioneDaAttivitaSingolare(sorgente);
//        Assert.assertEquals(ottenuto, previsto);
//        sorgente = "accademico";
//        ottenuto = listaService.getProfessioneDaAttivitaSingolare(sorgente);
//        Assert.assertEquals(ottenuto, previsto);
//        sorgente = "professore universitario";
//        ottenuto = listaService.getProfessioneDaAttivitaSingolare(sorgente);
//        Assert.assertEquals(ottenuto, previsto);
//        sorgente = "professoressa universitaria";
//        ottenuto = listaService.getProfessioneDaAttivitaSingolare(sorgente);
//        Assert.assertEquals(ottenuto, previsto);

    }// end of single test


    @Test
    public void ordineMappa() {
        listaNome = getNome();
        String tag = "|";
        String titolo;

        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappa;
        mappa = listaNome.getMappa();
        Assert.assertNotNull(mappa);

        System.out.println("*************");
        System.out.println("Titoli visibili dei paragrafi che DEVONO essere in ordine alfabetico");
        System.out.println("*************");
        for (String key : mappa.keySet()) {
            titolo = key;
            titolo = LibWiki.setNoQuadre(titolo);
            titolo = titolo.substring(titolo.indexOf(tag) + 1, titolo.length());
            System.out.println(titolo);
        }// end of for cycle
    }// end of single test


    /**
     * Ordina la lista di didascalie (Wrap) che hanno una valore valido per la pagina specifica <br>
     *
     * @param listaDisordinata di didascalie
     *
     * @return lista di didascalie (Wrap) ordinate per giorno/anno (key) e poi per cognome (value)
     */
    @Test
    public void ordinaDidascalieNomi() {
        ArrayList<Bio> listaGrezzaBio = bioService.findAllByNome(nomeTextCorto);
        ArrayList<WrapDidascalia> listaDidascalieNonOrdinate = listaService.creaListaDidascalie(listaGrezzaBio, EADidascalia.listaNomi);
        ArrayList<WrapDidascalia> listaDidascalieOrdinate;

        System.out.println("*************");
        System.out.println("Lista DISORDINATA di didascalie");
        System.out.println("*************");
        for (WrapDidascalia wrap : listaDidascalieNonOrdinate) {
            System.out.println(wrap.getChiave());
        }// end of for cycle

        listaDidascalieOrdinate = listaService.ordinaDidascalieNomi(listaDidascalieNonOrdinate);

        System.out.println("*************");
        System.out.println("Lista ORDINATA di didascalie");
        System.out.println("*************");
        for (WrapDidascalia wrap : listaDidascalieOrdinate) {
            System.out.println(wrap.getChiave());
        }// end of for cycle

    }// end of single test

}// end of class
