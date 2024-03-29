package it.algos.vaadwiki;

import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.wrapper.WrapTreStringhe;
import it.algos.vaadwiki.download.ElaboraService;
import it.algos.vaadwiki.download.PageService;
import it.algos.vaadwiki.enumeration.EAGraffe;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.vaadwiki.service.ABioService;
import it.algos.vaadwiki.service.LibBio;
import it.algos.vaadwiki.service.ParBio;
import it.algos.wiki.Api;
import it.algos.wiki.Page;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 21-apr-2020
 * Time: 16:09
 */
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Test per la libreria LibBio")
public class ABioServiceTest extends ATest {

    private static String TITOLO_MURUGUZA = "Pedro Muguruza";

    private static String TITOLO_GUERRA = "Antonio Guerra (politico)";

    private static String TMPL_GUERRA = "{{Bio\n" +
            "|Nome = Antonio\n" +
            "|Cognome = Guerra\n" +
            "|Sesso = M\n" +
            "|LuogoNascita = Afragola\n" +
            "|GiornoMeseNascita = 24 marzo\n" +
            "|AnnoNascita = 1824\n" +
            "|LuogoMorte = Afragola \n" +
            "|GiornoMeseMorte = 20 maggio\n" +
            "|AnnoMorte = 1890\n" +
            "|NoteMorte = <ref name=\"LAF\">{{cita notizia|autore=Domenico Corcione|url=http://www.lafragolanapoli.it/giornale/afragola-sconosciuta-le-lapidi-cittadine/|titolo=Afragola sconosciuta: le lapidi cittadine|giornale=La Fragola Napoli|data=27 marzo 2014}}</ref>\n" +
            "|Attività = politico\n" +
            "|Nazionalità = italiano\n" +
            "|PostNazionalità = , [[Deputati della XII legislatura del Regno d'Italia|deputato della XII legislatura del Regno d'Italia]]\n" +
            "}}";

    @InjectMocks
    public ABioService aBioService;

    @InjectMocks
    public BioService bioService;

    @InjectMocks
    public ATextService text;

    @InjectMocks
    public ElaboraService elaboraService;

    @InjectMocks
    public LibBio libBio;

    @InjectMocks
    private Api api;

    @InjectMocks
    private PageService pageService;

    private HashMap<String, Object> mappa;

    private LinkedHashMap<String, String> linkMappa;

    private Page page;


    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(aBioService);
        assertNotNull(aBioService);
        MockitoAnnotations.initMocks(bioService);
        assertNotNull(bioService);
        MockitoAnnotations.initMocks(libBio);
        assertNotNull(libBio);
        MockitoAnnotations.initMocks(elaboraService);
        assertNotNull(elaboraService);
        MockitoAnnotations.initMocks(api);
        assertNotNull(api);
        MockitoAnnotations.initMocks(pageService);
        assertNotNull(pageService);
        MockitoAnnotations.initMocks(text);
        assertNotNull(text);
        aBioService.text = text;
        libBio.text = text;
        elaboraService.text = text;
        elaboraService.libBio = libBio;
        pageService.text = text;
        pageService.api = api;
        pageService.bioService = bioService;
        pageService.elaboraService = elaboraService;
        api.text = text;
        bioService.text = text;
    }// end of method


    /**
     * Numero di occorrenze di un tag in un testo. <br>
     * Il tag non viene trimmato ed è sensibile agli spazi prima e dopo
     * NON si usano le regex <br>
     *
     * @param testoDaSpazzolare di riferimento
     * @param tag               da cercare
     *
     * @return numero di occorrenze - zero se non ce ne sono
     */
    @Test
    public void getNumTag() {
        contenuto = "Quante questo doppie{{ forse /ci sono} in {questo /testo";

        sorgente = VUOTA;
        previstoIntero = 0;
        ottenutoIntero = aBioService.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "forse";
        previstoIntero = 1;
        ottenutoIntero = aBioService.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "questo";
        previstoIntero = 2;
        ottenutoIntero = aBioService.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "/";
        previstoIntero = 2;
        ottenutoIntero = aBioService.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "}";
        previstoIntero = 1;
        ottenutoIntero = aBioService.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "{";
        previstoIntero = 3;
        ottenutoIntero = aBioService.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "{{";
        previstoIntero = 1;
        ottenutoIntero = aBioService.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = GRAFFA_INI;
        previstoIntero = 3;
        ottenutoIntero = aBioService.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = DOPPIE_GRAFFE_INI;
        previstoIntero = 1;
        ottenutoIntero = aBioService.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = GRAFFA_END;
        previstoIntero = 1;
        ottenutoIntero = aBioService.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = GRAFFA_END_REGEX;
        previstoIntero = 0;
        ottenutoIntero = aBioService.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = GRAFFA_INI_REGEX;
        previstoIntero = 0;
        ottenutoIntero = aBioService.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);
    }// end of single test


    /**
     * Restituisce il numero di occorrenze di una coppia di graffe iniziali nel testo. <br>
     * Le graffe possono essere poszionate in qualsiasi punto del testo <br>
     *
     * @param testoDaSpazzolare di riferimento
     *
     * @return numero di occorrenze - zero se non ce ne sono
     */
    @Test
    public void getNumGraffeIni() {
        sorgente = "Quante {questo doppie forse /ci sono} in {questo /testo";
        previstoIntero = 0;
        ottenutoIntero = aBioService.getNumGraffeIni(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "Quante {{questo doppie forse /ci sono} in {questo /testo";
        previstoIntero = 1;
        ottenutoIntero = aBioService.getNumGraffeIni(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "Quante {{questo doppie forse /ci sono{} in {questo /testo";
        previstoIntero = 1;
        ottenutoIntero = aBioService.getNumGraffeIni(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "Quante {{questo doppie forse /ci sono{{adesso}} in {questo /testo";
        previstoIntero = 2;
        ottenutoIntero = aBioService.getNumGraffeIni(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);
    }// end of single test


    /**
     * Restituisce il numero di occorrenze di una coppia di graffe di chiusura nel testo. <br>
     * Le graffe possono essere poszionate in qualsiasi punto del testo <br>
     *
     * @param testoDaSpazzolare di riferimento
     *
     * @return numero di occorrenze - zero se non ce ne sono
     */
    @Test
    public void getNumGraffeEnd() {
        sorgente = "Quante {questo doppie forse /ci sono} in {questo /testo";
        previstoIntero = 0;
        ottenutoIntero = aBioService.getNumGraffeEnd(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "Quante {{questo doppie forse /ci sono}} in {questo} /testo";
        previstoIntero = 1;
        ottenutoIntero = aBioService.getNumGraffeEnd(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "Quante {{questo doppie forse /ci sono{} in {questo /testo";
        previstoIntero = 0;
        ottenutoIntero = aBioService.getNumGraffeEnd(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "Quante questo}} doppie forse /ci sono{{adesso}} in {questo /testo";
        previstoIntero = 2;
        ottenutoIntero = aBioService.getNumGraffeEnd(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);
    }// end of single test


    /**
     * Controlla che le occorrenze del tag iniziale e di quello finale si pareggino all'interno del testo. <br>
     * Ordine ed annidamento NON considerato
     *
     * @param testoDaSpazzolare di riferimento
     * @param tagIni            tag iniziale
     * @param tagEnd            tag finale
     *
     * @return vero se il numero di tagIni è uguale al numero di tagEnd
     */
    @Test
    public void isPariTag() {
        String tagIni = VUOTA;
        String tagEnd = VUOTA;
        String prova = "xyz";
        String pipe = "|";

        sorgente = "Testo < xyz con | {{diverse}} possibilità}} ancora | da > sviluppare";

        ottenutoBooleano = aBioService.isPariTag(VUOTA, tagIni, tagEnd);
        assertFalse(ottenutoBooleano);

        tagIni = prova;
        tagEnd = prova;
        ottenutoBooleano = aBioService.isPariTag(sorgente, tagIni, tagEnd);
        assertFalse(ottenutoBooleano);

        tagIni = pipe;
        tagEnd = pipe;
        ottenutoBooleano = aBioService.isPariTag(sorgente, tagIni, tagEnd);
        assertTrue(ottenutoBooleano);

        tagIni = DOPPIE_GRAFFE_INI;
        tagEnd = DOPPIE_GRAFFE_END;
        ottenutoBooleano = aBioService.isPariTag(sorgente, tagIni, tagEnd);
        assertFalse(ottenutoBooleano);

        tagIni = "<";
        tagEnd = ">";
        ottenutoBooleano = aBioService.isPariTag(sorgente, tagIni, tagEnd);
        assertTrue(ottenutoBooleano);

        sorgente = "Testo < xyz con | {{diverse}} possibilità}} {{ancora | da > sviluppare";
        tagIni = DOPPIE_GRAFFE_INI;
        tagEnd = DOPPIE_GRAFFE_END;
        ottenutoBooleano = aBioService.isPariTag(sorgente, tagIni, tagEnd);
        assertTrue(ottenutoBooleano);
    }// end of single test


    /**
     * Controlla che le graffe di apertura 'pareggino' con le graffe di chiusura'. <br>
     * Ordine ed annidamento NON considerato <br>
     *
     * @param testoDaSpazzolare di riferimento
     *
     * @return vero se il numero di graffe di apertura è uguale al numero di graffe di chiusura
     */
    @Test
    public void isPariGraffe() {
        sorgente = "Testo < xyz con | diverse possibilità ancora | da > sviluppare";
        ottenutoBooleano = aBioService.isPariGraffe(sorgente);
        assertTrue(ottenutoBooleano);

        sorgente = "Testo < xyz con | {{diverse possibilità ancora | da > sviluppare";
        ottenutoBooleano = aBioService.isPariGraffe(sorgente);
        assertFalse(ottenutoBooleano);

        sorgente = "Testo < xyz con | {diverse} possibilità ancora | da > sviluppare";
        ottenutoBooleano = aBioService.isPariGraffe(sorgente);
        assertTrue(ottenutoBooleano);

        sorgente = "Testo < xyz con | {{diverse}} possibilità}} ancora | da > sviluppare";
        ottenutoBooleano = aBioService.isPariGraffe(sorgente);
        assertFalse(ottenutoBooleano);

        sorgente = "Testo < xyz con | {{diverse}} possibilità}} {{ancora | da > sviluppare";
        ottenutoBooleano = aBioService.isPariGraffe(sorgente);
        assertTrue(ottenutoBooleano);
    }// end of single test


    /**
     * Controlla le graffe interne al testo
     * <p>
     * Casi da controllare (all'interno delle graffe principali, già eliminate); i primi 7 non sono validi:
     * 1-......                         (manca)
     * 2-...{{...                       (mezza apertura)
     * 3-...{{...{{...}}...             (mezza apertura)
     * 4-...{{...}}...{{...             (mezza apertura)
     * 5-...}}...                       (mezza chiusura)
     * 6-...}}...{{...}}...             (mezza chiusura)
     * 7-...{{...}}...}}...             (mezza chiusura)
     * 8-...{{..}}...                   (singola)
     * 9-...{{}}...                     (vuota)
     * 10-{{..}}...                     (iniziale)
     * 11-...{{..}}                     (terminale)
     * 12-...{{..{{...}}...}}...        (interna)
     * 13-...{{..}}...{{...}}...        (doppie)
     * 14-...{{..}}..{{..}}..{{...}}... (tre o più)
     * 15-...{{..}}..|..{{..}}...       (due in punti diversi)
     * 16-...{{...|...}}...             (pipe interno)
     * 17-...{{...|...|...}}...         (doppio pipe)
     * 18-...{{..|...}}..|..{{..}}...   (due pipe in due graffe)
     * <p>
     * Se una o più graffe valide esistono, restituisce:
     * <p>
     * keyMapGraffeEsistono = se esistono                                       (boolean)
     * keyMapGraffeType = type della/della graffe trovate                       (EAGraffe)
     * keyMapGraffeNumero = quante ce ne sono                                   (integer)
     * keyMapGraffeTestoPrecedente = testo che precede la prima graffa o testo originale se non ce ne sono
     * keyMapGraffeListaWrapper = lista di WrapTreStringhe coi seguenti dati:   (list<WrapTreStringhe>)
     * - prima = valore del contenuto delle graffe                              (stringa)
     * - seconda = nome del parametro interessato                               (stringa)
     * - terza = valore completo del parametro che le contiene                  (stringa)
     *
     * @param testoDaSpazzolare di riferimento
     *
     * @return mappa col risultato
     */
    @Test
    public void checkGraffe() {
        List<WrapTreStringhe> listaWrap;
        sorgente = VUOTA;
        mappa = aBioService.checkGraffe(sorgente);
        assertNotNull(mappa);
        assertEquals(5, mappa.size());
        assertFalse((boolean) mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        assertEquals(0, mappa.get(KEY_MAP_GRAFFE_NUMERO));
        assertEquals(EAGraffe.manca, mappa.get(KEY_MAP_GRAFFE_TYPE));

        sorgente = "Qui non ci sono graffe";
        mappa = aBioService.checkGraffe(sorgente);
        assertNotNull(mappa);
        assertEquals(5, mappa.size());
        assertFalse((boolean) mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        assertEquals(0, mappa.get(KEY_MAP_GRAFFE_NUMERO));
        assertEquals(EAGraffe.manca, mappa.get(KEY_MAP_GRAFFE_TYPE));

        sorgente = "Qui {{esiste solo l'apertura di una graffa. Senza chiusura.";
        mappa = aBioService.checkGraffe(sorgente);
        assertNotNull(mappa);
        assertEquals(5, mappa.size());
        assertFalse((boolean) mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        assertEquals(0, mappa.get(KEY_MAP_GRAFFE_NUMERO));
        assertEquals(EAGraffe.mezzaInizio, mappa.get(KEY_MAP_GRAFFE_TYPE));

        sorgente = "Qui {{esiste l'apertura di una {{graffa}}. Dispari.";
        mappa = aBioService.checkGraffe(sorgente);
        assertNotNull(mappa);
        assertEquals(5, mappa.size());
        assertFalse((boolean) mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        assertEquals(0, mappa.get(KEY_MAP_GRAFFE_NUMERO));
        assertEquals(EAGraffe.mezzaInizio, mappa.get(KEY_MAP_GRAFFE_TYPE));

        sorgente = "Qui {{esiste}} l'apertura di una {{graffa. Dispari.";
        mappa = aBioService.checkGraffe(sorgente);
        assertNotNull(mappa);
        assertEquals(5, mappa.size());
        assertFalse((boolean) mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        assertEquals(0, mappa.get(KEY_MAP_GRAFFE_NUMERO));
        assertEquals(EAGraffe.mezzaInizio, mappa.get(KEY_MAP_GRAFFE_TYPE));

        sorgente = "Qui esiste}} solo la chiusura di una graffa. Senza apertura.";
        mappa = aBioService.checkGraffe(sorgente);
        assertNotNull(mappa);
        assertEquals(5, mappa.size());
        assertFalse((boolean) mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        assertEquals(0, mappa.get(KEY_MAP_GRAFFE_NUMERO));
        assertEquals(EAGraffe.mezzaFine, mappa.get(KEY_MAP_GRAFFE_TYPE));

        sorgente = "Qui {{esiste}} una graffa. In mezzo al testo.";
        mappa = aBioService.checkGraffe(sorgente);
        assertNotNull(mappa);
        assertEquals(5, mappa.size());
        assertTrue((boolean) mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        assertEquals(1, mappa.get(KEY_MAP_GRAFFE_NUMERO));
        assertEquals(EAGraffe.singola, mappa.get(KEY_MAP_GRAFFE_TYPE));
        listaWrap = (List<WrapTreStringhe>) mappa.get(KEY_MAP_GRAFFE_LISTA_WRAPPER);
        ottenuto = array.isValid(listaWrap) ? listaWrap.get(0).getPrima() : VUOTA;
        assertEquals("esiste", ottenuto);

        sorgente = "Qui {{}} una graffa. In mezzo al testo.";
        mappa = aBioService.checkGraffe(sorgente);
        assertNotNull(mappa);
        assertEquals(5, mappa.size());
        assertTrue((boolean) mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        assertEquals(1, mappa.get(KEY_MAP_GRAFFE_NUMERO));
        assertEquals(EAGraffe.vuota, mappa.get(KEY_MAP_GRAFFE_TYPE));
        listaWrap = (List<WrapTreStringhe>) mappa.get(KEY_MAP_GRAFFE_LISTA_WRAPPER);
        ottenuto = array.isValid(listaWrap) ? listaWrap.get(0).getPrima() : VUOTA;
        assertEquals(VUOTA, ottenuto);

        sorgente = "{{Qui}} esiste una graffa. Inizio testo.";
        mappa = aBioService.checkGraffe(sorgente);
        assertNotNull(mappa);
        assertEquals(5, mappa.size());
        assertTrue((boolean) mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        assertEquals(1, mappa.get(KEY_MAP_GRAFFE_NUMERO));
        assertEquals(EAGraffe.iniziale, mappa.get(KEY_MAP_GRAFFE_TYPE));
        listaWrap = (List<WrapTreStringhe>) mappa.get(KEY_MAP_GRAFFE_LISTA_WRAPPER);
        ottenuto = array.isValid(listaWrap) ? listaWrap.get(0).getPrima() : VUOTA;
        assertEquals("Qui", ottenuto);

        sorgente = "Qui esiste una graffa. Fine {{testo}}";
        mappa = aBioService.checkGraffe(sorgente);
        assertNotNull(mappa);
        assertEquals(5, mappa.size());
        assertTrue((boolean) mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        assertEquals(1, mappa.get(KEY_MAP_GRAFFE_NUMERO));
        assertEquals(EAGraffe.finale, mappa.get(KEY_MAP_GRAFFE_TYPE));
        listaWrap = (List<WrapTreStringhe>) mappa.get(KEY_MAP_GRAFFE_LISTA_WRAPPER);
        ottenuto = array.isValid(listaWrap) ? listaWrap.get(0).getPrima() : VUOTA;
        assertEquals("testo", ottenuto);

        sorgente = "Qui {{esistono}} due graffe. Fine {{seconda}}";
        mappa = aBioService.checkGraffe(sorgente);
        assertNotNull(mappa);
        assertEquals(5, mappa.size());
        assertTrue((boolean) mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        assertEquals(2, mappa.get(KEY_MAP_GRAFFE_NUMERO));
        assertEquals(EAGraffe.doppie, mappa.get(KEY_MAP_GRAFFE_TYPE));
        listaWrap = (List<WrapTreStringhe>) mappa.get(KEY_MAP_GRAFFE_LISTA_WRAPPER);
        ottenuto = array.isValid(listaWrap) ? listaWrap.get(0).getPrima() : VUOTA;
        assertEquals("esistono", ottenuto);
        ottenuto = array.isValid(listaWrap) ? listaWrap.get(1).getPrima() : VUOTA;
        assertEquals("seconda", ottenuto);

        sorgente = "Qui {{esistono}} {{[[tre]]}} graffe. Fine {{terza}}";
        mappa = aBioService.checkGraffe(sorgente);
        assertNotNull(mappa);
        assertEquals(5, mappa.size());
        assertTrue((boolean) mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        assertEquals(3, mappa.get(KEY_MAP_GRAFFE_NUMERO));
        assertEquals(EAGraffe.triple, mappa.get(KEY_MAP_GRAFFE_TYPE));
        listaWrap = (List<WrapTreStringhe>) mappa.get(KEY_MAP_GRAFFE_LISTA_WRAPPER);
        ottenuto = array.isValid(listaWrap) ? listaWrap.get(0).getPrima() : VUOTA;
        assertEquals("esistono", ottenuto);
        ottenuto = array.isValid(listaWrap) ? listaWrap.get(1).getPrima() : VUOTA;
        assertEquals("[[tre]]", ottenuto);
        ottenuto = array.isValid(listaWrap) ? listaWrap.get(2).getPrima() : VUOTA;
        assertEquals("terza", ottenuto);
    }// end of single test


    /**
     * Spostare da LibBio a ABioService
     */
//    @Test
    public  void getMappaDownload() {
        String valorePropertyTmplBioServer = TMPL_GUERRA;
        linkMappa = libBio.getMappaDownload(valorePropertyTmplBioServer);

        Assert.assertNotNull(linkMappa);
        Assert.assertEquals("Antonio", linkMappa.get(ParBio.nome.getTag()));
        Assert.assertEquals("Guerra", linkMappa.get(ParBio.cognome.getTag()));
        Assert.assertEquals("M", linkMappa.get(ParBio.sesso.getTag()));
        Assert.assertEquals("Afragola", linkMappa.get(ParBio.luogoNascita.getTag()));
        Assert.assertEquals("24 marzo", linkMappa.get(ParBio.giornoMeseNascita.getTag()));
        Assert.assertEquals("1824", linkMappa.get(ParBio.annoNascita.getTag()));
        Assert.assertEquals("Afragola", linkMappa.get(ParBio.luogoMorte.getTag()));
        Assert.assertEquals("20 maggio", linkMappa.get(ParBio.giornoMeseMorte.getTag()));
        Assert.assertEquals("1890", linkMappa.get(ParBio.annoMorte.getTag()));
        Assert.assertEquals("politico", linkMappa.get(ParBio.attivita.getTag()));
        Assert.assertEquals("italiano", linkMappa.get(ParBio.nazionalita.getTag()));

        stampaMappa(valorePropertyTmplBioServer, linkMappa);

        Assert.assertEquals(VUOTA, linkMappa.get(ParBio.titolo.getTag()));
        String noteMorte = "<ref name=\"LAF\">{{cita notizia|autore=Domenico Corcione|url=http://www.lafragolanapoli.it/giornale/afragola-sconosciuta-le-lapidi-cittadine/|titolo=Afragola sconosciuta: le lapidi cittadine|giornale=La Fragola Napoli|data=27 marzo 2014}}</ref>";
        Assert.assertEquals(noteMorte, linkMappa.get(ParBio.noteMorte.getTag()));
    }// end of single test


    /**
     * Spostare da LibBio a ABioService
     */
    @Test
    public void getMappaDownload2() {
        Bio entity = null;

        page = api.leggePage(TITOLO_MURUGUZA);
        Assert.assertNotNull(page);

//        entity = pageService.creaBio(page);
//        Assert.assertNotNull(page);
//        stampaParametriMongo(entity);
    }// end of single test


    private void stampaParametriMongo(Bio entity) {
        System.out.println("");
        System.out.println("*************");
        System.out.println("stampa tutti i parametri" + " - sono " + ParBio.values().length);
        System.out.println("*************");

        for (ParBio par : ParBio.values()) {
            System.out.println(par.getTag() + ": " + par.getValue(entity));
        }// end of for cycle

        System.out.println("");
        System.out.println("*************");
        System.out.println("stampa i parametri presenti");
        System.out.println("*************");

        for (ParBio par : ParBio.values()) {
            if (text.isValid(par.getValue(entity))) {
                System.out.println(par.getTag() + ": " + par.getValue(entity));
            }// end of if cycle
        }// end of for cycle
    }// end of method
    private void stampaMappa(String tmplBioServer, LinkedHashMap<String, String> mappa) {
        System.out.println("");
        System.out.println("*************");
        System.out.println("stampa mappa" + " - sono " + mappa.size());
        System.out.println("*************");

        System.out.println("Template:");
        System.out.println(tmplBioServer);
        System.out.println("");

        for (String key : mappa.keySet()) {
            System.out.println(key + ": " + mappa.get(key));
        }// end of for cycle
    }// end of method

}// end of test class
