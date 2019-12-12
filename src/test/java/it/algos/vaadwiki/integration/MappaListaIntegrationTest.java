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

    @Autowired
    private ApplicationContext appContext;

    private String testo;

    private String annoText = "1824";

    private String giornoText = "3 marzo";

    //    private String nomeText = "Rita";
    private String nomeText = "Andrea";

    private Anno annoEntity;

    private Giorno giornoEntity;

    private Nome nomeEntity;

    private Cognome cognomeEntity;

    private String parVuoto = "Senza giorno di nascita";

    private String parAttivita = "Senza attività specificata";

    private ListaGiornoNato listaGiorno;

    private ListaAnnoNato listaAnno;

    private ListaNomi listaNome;

    private ListaCognomi listaCognomi;

    private List<WrapDidascalia> listaDidascalie;

    private EADidascalia typeGiorno = EADidascalia.giornoNato;

    private EADidascalia typeAnno = EADidascalia.annoNato;

    private EADidascalia typeNome = EADidascalia.listaNomi;

    private MappaLista mappaLista;


    @Before
    public void setUpIniziale() {
        Assert.assertNotNull(text);
        Assert.assertNotNull(giornoService);
        Assert.assertNotNull(annoService);
        Assert.assertNotNull(nomeService);
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

//        anno();
//        giorno();
//        nome();
    }// end of single test


    @Test
    public void anniSenzaParagrafiRigheSingoleTesta() {
        inizia();
        listaDidascalie = listaAnno.listaDidascalie;

        mappaLista = appContext.getBean(MappaLista.class, listaDidascalie, typeAnno, false, false);
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

        mappaLista = appContext.getBean(MappaLista.class,  listaDidascalie, typeAnno, false, true);
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

        mappaLista = appContext.getBean(MappaLista.class, listaDidascalie, typeAnno, true, false);
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

        mappaLista = appContext.getBean(MappaLista.class, listaDidascalie, typeAnno, true, true);
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

        mappaLista = appContext.getBean(MappaLista.class, annoText, listaDidascalie, typeAnno, true, false, true, parVuoto, false, false, false, 0);
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

        mappaLista = appContext.getBean(MappaLista.class, annoText, listaDidascalie, typeAnno, true, true, true, parVuoto, false, false, false, 0);
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

        mappaLista = appContext.getBean(MappaLista.class, annoText, listaDidascalie, typeAnno, true, false, true, parVuoto, true, false, false, 0);
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

        mappaLista = appContext.getBean(MappaLista.class, annoText, listaDidascalie, typeAnno, true, false, true, parVuoto, true, true, false, 0);
        Assert.assertNotNull(mappaLista);
        System.out.println("*************");
        System.out.println("anniConParagrafiRigheRaggruppateTestaConlinkConsize");
        System.out.println("Lista dei " + listaAnno.size + " nati nel " + annoText + " - Con paragrafi, righe raggruppate, vuoto in testa, con link, con size");
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

        mappaLista = appContext.getBean(MappaLista.class, nomeText, listaDidascalie, typeNome, false, false, true, parAttivita, false, false, false, 0);
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

        mappaLista = appContext.getBean(MappaLista.class, nomeText, listaDidascalie, typeNome, false, true, true, parAttivita, false, false, false, 0);
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

        mappaLista = appContext.getBean(MappaLista.class, nomeText, listaDidascalie, typeNome, false, false, true, parAttivita, true, false, false, 0);
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

        mappaLista = appContext.getBean(MappaLista.class, nomeText, listaDidascalie, typeNome, false, false, true, parAttivita, true, true, false, 0);
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
    public void nomiTestaConlinkConsizeConsottopagine() {
        inizia();
        listaDidascalie = listaNome.listaDidascalie;

        mappaLista = appContext.getBean(MappaLista.class, nomeText, listaDidascalie, typeNome, false, false, true, parAttivita, true, true, true, 12);
        Assert.assertNotNull(mappaLista);
        System.out.println("*************");
        System.out.println("nomiTestaConlinkConsizeConsottopagine");
        System.out.println("Lista dei " + listaNome.size + " biografati di nome " + nomeText + " - Con paragrafi, con link, con le dimensioni nel titolo del paragrafo e con sottopagine");
        System.out.println("*************");
        testo = mappaLista.getTesto();
        System.out.println(testo);
        System.out.println("");
    }// end of single test

}// end of class
