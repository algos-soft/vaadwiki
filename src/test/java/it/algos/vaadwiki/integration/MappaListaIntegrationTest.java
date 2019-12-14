package it.algos.vaadwiki.integration;

import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadwiki.ATest;
import it.algos.vaadwiki.didascalia.WrapDidascalia;
import it.algos.vaadwiki.enumeration.EADidascalia;
import it.algos.vaadwiki.liste.*;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.vaadwiki.modules.cognome.Cognome;
import it.algos.vaadwiki.modules.cognome.CognomeService;
import it.algos.vaadwiki.modules.nome.Nome;
import it.algos.vaadwiki.modules.nome.NomeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 11-dic-2019
 * Time: 19:13
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MappaListaIntegrationTest extends ATest {

    @Autowired
    protected BioService bioService;

    @Autowired
    protected GiornoService giornoService;

    @Autowired
    protected AnnoService annoService;

    @Autowired
    protected NomeService nomeService;

    @Autowired
    protected CognomeService cognomeService;

    @Autowired
    protected PreferenzaService prefService;

    @Autowired
    protected ListaService listaService;

    @Autowired
    protected ADateService dateService;

    @Autowired
    private ApplicationContext appContext;

    private String testo;

    private String annoText = "1824";

    private String giornoText = "3 marzo";

    //    private String nomeText = "Rita";
//    private String nomeText = "Andrea";
//    private String nomeText = "Željko";
//    private String nomeText = "Aldo";
    private String nomeText = "Giovanni";

    private String cognomeText = "Smith";

    private Anno annoEntity;

    private Giorno giornoEntity;

    private Nome nomeEntity;

    private Cognome cognomeEntity;

    private String parVuoto = "senza giorno di nascita";

    private String parVuotoGiorno = "senza anno di nascita";

    private String parAttivita = "senza attività specificata";

    private ListaGiornoNato listaGiorno;

    private ListaAnnoNato listaAnno;

    private ListaNomi listaNome;

    private ListaCognomi listaCognome;

    private List<WrapDidascalia> listaDidascalie;

    private EADidascalia typeAnno = EADidascalia.annoNato;

    private EADidascalia typeGiorno = EADidascalia.giornoNato;

    private EADidascalia typeNome = EADidascalia.listaNomi;

    private EADidascalia typeCognome = EADidascalia.listaCognomi;

    private MappaLista mappaLista;

    private TypeLista typeLista;


    @Before
    public void setUpIniziale() {
        Assert.assertNotNull(text);
        Assert.assertNotNull(giornoService);
        Assert.assertNotNull(annoService);
        Assert.assertNotNull(nomeService);
        Assert.assertNotNull(cognomeService);
    }// end of method


    private void inizia() {
        annoEntity = annoService.findByKeyUnica(annoText);
        Assert.assertNotNull(annoEntity);
        listaAnno = appContext.getBean(ListaAnnoNato.class, annoEntity);
        Assert.assertNotNull(listaAnno);

        giornoEntity = giornoService.findByKeyUnica(giornoText);
        Assert.assertNotNull(giornoEntity);
        listaGiorno = appContext.getBean(ListaGiornoNato.class, giornoEntity);
        Assert.assertNotNull(listaGiorno);

        nomeEntity = nomeService.findByKeyUnica(nomeText);
        Assert.assertNotNull(nomeEntity);
        listaNome = appContext.getBean(ListaNomi.class, nomeEntity);
        Assert.assertNotNull(listaNome);

        cognomeEntity = cognomeService.findByKeyUnica(cognomeText);
        Assert.assertNotNull(cognomeEntity);
        listaCognome = appContext.getBean(ListaCognomi.class, cognomeEntity);
        Assert.assertNotNull(listaCognome);
    }// end of single test


    @Test
    public void anniSenzaParagrafiRigheSingoleTesta() {
        inizia();
        listaDidascalie = listaAnno.listaDidascalie;
        typeLista = new TypeLista(false);
        typeLista.usaRigheRaggruppate = false;
        mappaLista = appContext.getBean(MappaLista.class, nomeText, listaDidascalie, typeAnno, typeLista, parVuoto);

        Assert.assertNotNull(mappaLista);
        System.out.println("*************");
        System.out.println("anniSenzaParagrafiRigheSingoleTesta");
        System.out.println("Lista dei " + listaAnno.size + " nati nel " + annoText + " - Senza paragrafi, righe singole, vuoto in testa");
        System.out.println("*************");
        testo = mappaLista.getTesto();
        System.out.println(testo);
        System.out.println("");
    }// end of method


    @Test
    public void anniSenzaParagrafiRigheSingoleCoda() {
        inizia();
        listaDidascalie = listaAnno.listaDidascalie;
        typeLista = new TypeLista(false);
        typeLista.usaRigheRaggruppate = false;
        typeLista.paragrafoVuotoInCoda = true;
        mappaLista = appContext.getBean(MappaLista.class, nomeText, listaDidascalie, typeAnno, typeLista, parVuoto);

        Assert.assertNotNull(mappaLista);
        System.out.println("*************");
        System.out.println("anniSenzaParagrafiRigheSingoleCoda");
        System.out.println("Lista dei " + listaAnno.size + " nati nel " + annoText + " - Senza paragrafi, righe singole, vuoto in coda");
        System.out.println("*************");
        testo = mappaLista.getTesto();
        System.out.println(testo);
        System.out.println("");
    }// end of method


    @Test
    public void anniSenzaParagrafiRigheRaggruppateTesta() {
        inizia();
        listaDidascalie = listaAnno.listaDidascalie;
        typeLista = new TypeLista(false);
        mappaLista = appContext.getBean(MappaLista.class, nomeText, listaDidascalie, typeAnno, typeLista, parVuoto);

        Assert.assertNotNull(mappaLista);
        System.out.println("*************");
        System.out.println("anniSenzaParagrafiRigheRaggruppateTesta");
        System.out.println("Lista dei " + listaAnno.size + " nati nel " + annoText + " - Senza paragrafi, righe raggruppate, vuoto in testa");
        System.out.println("*************");
        testo = mappaLista.getTesto();
        System.out.println(testo);
        System.out.println("");
    }// end of single test


    @Test
    public void anniSenzaParagrafiRigheRaggruppateCoda() {
        inizia();
        listaDidascalie = listaAnno.listaDidascalie;
        typeLista = new TypeLista(false);
        typeLista.paragrafoVuotoInCoda = true;
        mappaLista = appContext.getBean(MappaLista.class, nomeText, listaDidascalie, typeAnno, typeLista, parVuoto);

        Assert.assertNotNull(mappaLista);
        System.out.println("*************");
        System.out.println("anniSenzaParagrafiRigheRaggruppateCoda");
        System.out.println("Lista dei " + listaAnno.size + " nati nel " + annoText + " - Senza paragrafi, righe raggruppate, vuoto in coda");
        System.out.println("*************");
        testo = mappaLista.getTesto();
        System.out.println(testo);
        System.out.println("");
    }// end of single test


    @Test
    public void anniConParagrafiRigheRaggruppateTestaSenzalinkSenzasize() {
        inizia();
        listaDidascalie = listaAnno.listaDidascalie;
        typeLista = new TypeLista(true, true, false, false, false, false);
        mappaLista = appContext.getBean(MappaLista.class, nomeText, listaDidascalie, typeAnno, typeLista, parVuoto);

        Assert.assertNotNull(mappaLista);
        System.out.println("*************");
        System.out.println("anniConParagrafiRigheRaggruppateTestaSenzalinkSenzasize");
        System.out.println("Lista dei " + listaAnno.size + " nati nel " + annoText + " - Con paragrafi, righe raggruppate, vuoto in testa, senza link, senza size");
        System.out.println("*************");
        testo = mappaLista.getTesto();
        System.out.println(testo);
        System.out.println("");
    }// end of method


    @Test
    public void anniConParagrafiRigheRaggruppateCodaSenzalinkSenzasize() {
        inizia();
        listaDidascalie = listaAnno.listaDidascalie;
        typeLista = new TypeLista(true, true, true, false, false, false);
        mappaLista = appContext.getBean(MappaLista.class, nomeText, listaDidascalie, typeAnno, typeLista, parVuoto);

        Assert.assertNotNull(mappaLista);
        System.out.println("*************");
        System.out.println("anniConParagrafiRigheRaggruppateCodaSenzalinkSenzasize");
        System.out.println("Lista dei " + listaAnno.size + " nati nel " + annoText + " - Con paragrafi, righe raggruppate, vuoto in coda, senza link, senza size");
        System.out.println("*************");
        testo = mappaLista.getTesto();
        System.out.println(testo);
        System.out.println("");
    }// end of method


    @Test
    public void anniConParagrafiRigheRaggruppateTestaConlinkSenzasize() {
        inizia();
        listaDidascalie = listaAnno.listaDidascalie;
        typeLista = new TypeLista(true, true, false, true, false, false);
        mappaLista = appContext.getBean(MappaLista.class, nomeText, listaDidascalie, typeAnno, typeLista, parVuoto);

        Assert.assertNotNull(mappaLista);
        System.out.println("*************");
        System.out.println("anniConParagrafiRigheRaggruppateTestaConlinkSenzasize");
        System.out.println("Lista dei " + listaAnno.size + " nati nel " + annoText + " - Con paragrafi, righe raggruppate, vuoto in testa, con link, senza size");
        System.out.println("*************");
        testo = mappaLista.getTesto();
        System.out.println(testo);
        System.out.println("");
    }// end of method


    @Test
    public void anniConParagrafiRigheRaggruppateTestaConlinkConsize() {
        inizia();
        listaDidascalie = listaAnno.listaDidascalie;
        typeLista = new TypeLista(true, true, true, true, true, false);
        mappaLista = appContext.getBean(MappaLista.class, nomeText, listaDidascalie, typeAnno, typeLista, parVuoto);

        Assert.assertNotNull(mappaLista);
        System.out.println("*************");
        System.out.println("anniConParagrafiRigheRaggruppateTestaConlinkConsize");
        System.out.println("Lista dei " + listaAnno.size + " nati nel " + annoText + " - Con paragrafi, righe raggruppate, vuoto in testa, con link, con size");
        System.out.println("*************");
        testo = mappaLista.getTesto();
        System.out.println(testo);
        System.out.println("");
    }// end of method


    @Test
    public void giorniConParagrafiRigheRaggruppateTestaConlinkConsize() {
        inizia();
        listaDidascalie = listaGiorno.listaDidascalie;
        typeLista = new TypeLista(true, true, true, true, true, false);
        mappaLista = appContext.getBean(MappaLista.class, nomeText, listaDidascalie, typeGiorno, typeLista, parVuotoGiorno);

        Assert.assertNotNull(mappaLista);
        System.out.println("*************");
        System.out.println("giorniConParagrafiRigheRaggruppateTestaConlinkConsize");
        System.out.println("Lista dei " + listaGiorno.size + " nati il " + giornoText + " - Con paragrafi, righe raggruppate, vuoto in testa, con link, con size");
        System.out.println("*************");
        testo = mappaLista.getTesto();
        System.out.println(testo);
        System.out.println("");
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
    public void nomiTestaSenzalinkSenzasizeSenzasottopagine() {
        inizia();
        listaDidascalie = listaNome.listaDidascalie;
        typeLista = new TypeLista(true, false, false, false, false, false);
        mappaLista = appContext.getBean(MappaLista.class, nomeText, listaDidascalie, typeNome, typeLista, parAttivita, 12);

        Assert.assertNotNull(mappaLista);
        System.out.println("*************");
        System.out.println("nomiTestaSenzalinkSenzasizeSenzasottopagine");
        System.out.println("Lista dei " + listaNome.size + " biografati di nome " + nomeText + " - Con paragrafi, senza link, senza le dimensioni nel titolo del paragrafo e senza sottopagine");
        System.out.println("*************");
        testo = mappaLista.getTesto();
        System.out.println(testo);
        System.out.println("");
    }// end of single test


    @Test
    public void nomiCodaSenzalinkSenzasizeSenzasottopagine() {
        inizia();
        listaDidascalie = listaNome.listaDidascalie;
        typeLista = new TypeLista(true, false, true, false, false, false);
        mappaLista = appContext.getBean(MappaLista.class, nomeText, listaDidascalie, typeNome, typeLista, parAttivita, 12);

        Assert.assertNotNull(mappaLista);
        System.out.println("*************");
        System.out.println("nomiCodaSenzalinkSenzasizeSenzasottopagine");
        System.out.println("Lista dei " + listaNome.size + " biografati di nome " + nomeText + " - Con paragrafi, senza link, senza le dimensioni nel titolo del paragrafo e senza sottopagine");
        System.out.println("*************");
        testo = mappaLista.getTesto();
        System.out.println(testo);
        System.out.println("");
    }// end of single test


    @Test
    public void nomiTestaConlinkSenzasizeSenzasottopagine() {
        inizia();
        listaDidascalie = listaNome.listaDidascalie;
        typeLista = new TypeLista(true, false, false, true, false, false);
        mappaLista = appContext.getBean(MappaLista.class, nomeText, listaDidascalie, typeNome, typeLista, parAttivita, 12);

        Assert.assertNotNull(mappaLista);
        System.out.println("*************");
        System.out.println("nomiTestaConlinkSenzasizeSenzasottopagine");
        System.out.println("Lista dei " + listaNome.size + " biografati di nome " + nomeText + " - Con paragrafi, con link, senza le dimensioni nel titolo del paragrafo e senza sottopagine");
        System.out.println("*************");
        testo = mappaLista.getTesto();
        System.out.println(testo);
        System.out.println("");
    }// end of single test


    @Test
    public void nomiTestaConlinkConsizeSenzasottopagine() {
        inizia();
        listaDidascalie = listaNome.listaDidascalie;
        typeLista = new TypeLista(true, false, false, true, true, false);
        mappaLista = appContext.getBean(MappaLista.class, nomeText, listaDidascalie, typeNome, typeLista, parAttivita, 12);

        Assert.assertNotNull(mappaLista);
        System.out.println("*************");
        System.out.println("nomiTestaConlinkConsizeSenzasottopagine");
        System.out.println("Lista dei " + listaNome.size + " biografati di nome " + nomeText + " - Con paragrafi, con link, con le dimensioni nel titolo del paragrafo e senza sottopagine");
        System.out.println("*************");
        testo = mappaLista.getTesto();
        System.out.println(testo);
        System.out.println("");
    }// end of single test


    @Test
    public void nomiCodaConlinkConsizeConsottopagine() {
        inizia();
        int dim = 10;
        listaDidascalie = listaNome.listaDidascalie;
        typeLista = new TypeLista(true);
        mappaLista = appContext.getBean(MappaLista.class, nomeText, listaDidascalie, typeNome, typeLista, parAttivita, 12);
        Assert.assertNotNull(mappaLista);
        System.out.println("*************");
        System.out.println("nomiCodaConlinkConsizeConsottopagine");
        System.out.println("Lista dei " + listaNome.size + " biografati di nome " + nomeText + " - Con paragrafi, con link, con le dimensioni nel titolo del paragrafo e con sottopagine");
        System.out.println("*************");
        System.out.println("Ci sono " + mappaLista.getNumParagrafi() + " paragrafi");
        if (dim >= mappaLista.getNumParagrafi()) {
            dim = mappaLista.getNumParagrafi();
        }// end of if cycle
        System.out.println("Primi " + dim + " titoli disordinati");
        System.out.println(mappaLista.getTitoliParagrafiDisordinati().subList(0, dim));
        System.out.println("Primi " + dim + " titoli ordinati");
        System.out.println(mappaLista.getTitoliParagrafiOrdinati().subList(0, dim));
        System.out.println("Primi " + dim + " riferimenti alla pagina professione");
        System.out.println(mappaLista.getTitoliParagrafiPagine().subList(0, dim));
        System.out.println("Primi " + dim + " titoli visibili");
        System.out.println(mappaLista.getTitoliParagrafiVisibili().subList(0, dim));
        System.out.println("Primi " + dim + " titoli linkati");
        System.out.println(mappaLista.getTitoliParagrafiLinkati().subList(0, dim));
        System.out.println("Primi " + dim + " titoli conSize");
        System.out.println(mappaLista.getTitoliParagrafiConSize().subList(0, dim));
        System.out.println("Primi " + dim + " titoli definitivi");
        System.out.println(mappaLista.getTitoliParagrafiDefinitivi().subList(0, dim));
        System.out.println("Dimensione dei paragrafi");
        System.out.println(mappaLista.getDimParagrafi());

        System.out.println("");
        testo = mappaLista.getTesto();
        System.out.println(testo);
        System.out.println("");
    }// end of single test


    @Test
    public void nomiTestaConlinkConsizeConsottopagine() {
        inizia();
        listaDidascalie = listaNome.listaDidascalie;
        typeLista = new TypeLista(true);
        typeLista.paragrafoVuotoInCoda = false;
        mappaLista = appContext.getBean(MappaLista.class, nomeText, listaDidascalie, typeNome, typeLista, parAttivita);
        Assert.assertNotNull(mappaLista);
        System.out.println("*************");
        System.out.println("nomiTestaConlinkConsizeConsottopagine");
        System.out.println("Lista dei " + listaNome.size + " biografati di nome " + nomeText + " - Con paragrafi, con link, con le dimensioni nel titolo del paragrafo e con sottopagine");
        System.out.println("*************");
        System.out.println("");
        testo = mappaLista.getTesto();
        System.out.println(testo);
        System.out.println("");
    }// end of single test


    @Test
    public void cognomiTestaConlinkConsizeConsottopagine() {
        inizia();
        listaDidascalie = listaCognome.listaDidascalie;
        typeLista = new TypeLista(true);
        typeLista.paragrafoVuotoInCoda = false;
        mappaLista = appContext.getBean(MappaLista.class, cognomeText, listaDidascalie, typeCognome, typeLista, parAttivita, 12);
        Assert.assertNotNull(mappaLista);
        System.out.println("*************");
        System.out.println("cognomiTestaConlinkConsizeConsottopagine");
        System.out.println("Lista dei " + listaCognome.size + " biografati di cognome " + cognomeText + " - Con paragrafi, con link, con le dimensioni nel titolo del paragrafo e con sottopagine");
        System.out.println("*************");
        System.out.println("");
        testo = mappaLista.getTesto();
        System.out.println(testo);
        System.out.println("");
    }// end of single test


    @Test
    public void nomiSottoPagine() {
        inizia();
        listaDidascalie = listaNome.listaDidascalie;
        typeLista = new TypeLista(true);
        mappaLista = appContext.getBean(MappaLista.class, nomeText, listaDidascalie, typeNome, typeLista, parAttivita);
        Assert.assertNotNull(mappaLista);
        mappaLista.getTesto();
        System.out.println("*************");
        System.out.println("nomiSottoPagine: Nella lista di " + nomeText + " ci sono " + mappaLista.getNumParagrafi() + " paragrafi di cui " + mappaLista.getSottoPagine().size() + " sono sottopagine");
        System.out.println("*************");
        System.out.println("");
        for (String sottopagina : mappaLista.getSottoPagine().keySet()) {
            System.out.println(sottopagina + " con " + mappaLista.getDimParagrafo(sottopagina) + " voci");
        }// end of for cycle
    }// end of single test

}// end of class
