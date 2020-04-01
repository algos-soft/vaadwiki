package it.algos.vaadwiki.integration;

import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.AMongoService;
import it.algos.vaadwiki.ATest;
import it.algos.vaadwiki.download.ElaboraService;
import it.algos.vaadwiki.download.PageService;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.vaadwiki.service.LibBio;
import it.algos.vaadwiki.service.ParBio;
import it.algos.vaadwiki.upload.UploadService;
import it.algos.wiki.Api;
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
import java.util.HashMap;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 02-gen-2020
 * Time: 20:04
 * <p>
 * Elaborazione:
 * da mongoDB elaborazione EAElabora.ordinaNormaliNoLoss
 * parte dal tmpl e lo riordina (aggiunge normali mancanti ed elimina quelli vuoti) SENZA modificare i valori o i parametri presenti nel mongoDB
 * da mongoDB elaborazione EAElabora.parametriRipuliti
 * parte dal tmpl e lo riordina (aggiunge normali mancanti ed elimina quelli vuoti) SENZA modificare i valori o i parametri presenti nel mongoDB
 * da mongoDB elaborazione EAElabora.parametriModificati
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ElaboraServiceIntegrationTest extends ATest {

    private static String BIO_UNO = "Utente:Gac/Bio/Complessa3";

    private static String BIO_SORGENTE = "{{Bio\n" +
            "    |Nome = Crystle Danae\n" +
            "|Cognome = [[Stewart]]\n" +
            "|GiornoMeseMorte=\n" +
            "    |PostCognome = \n" +
            "|Sesso = F\n" +
            " | LuogoNascita = [[Wilmington]]\n" +
            " | GiornoMeseNascita = 1 Settembre\n" +
            "|AnnoNascita = [[1981]]\n" +
            "|AnnoMorte=\n" +
            "|LuogoMorte = ? \n" +
            "|Nazionalità = statunitense ?\n" +
            "|Attività = Modella<ref>Dal 2000</ref>\n" +
            "|Attività2 = \n" +
            "|PostNazionalità = {{sp}}incoronata [[Miss USA]] [[2008]]<ref>{{cite web\n" +
            "|url= http://www.washingtonpost.com/wp-dyn/content/article/2008/04/12/AR2008041200018.html\n" +
            "|title= Texan Takes Miss {{sp}} USA Crown in Las Vegas\n" +
            "|accessdate= 2008-05-10\n" +
            "|date= 2008-04-12\n" +
            "|publisher= Washington Post\n" +
            "}}</ref>\n" +
            "|Immagine=\n" +
            "|Didascalia=\n" +
            "}}";

    private static String BIO_MERGED_NO_LOSS = "{{Bio\n" +
            "|Nome = Crystle Danae\n" +
            "|Cognome = [[Stewart]]\n" +
            "|Sesso = F\n" +
            "|LuogoNascita = [[Wilmington]]\n" +
            "|GiornoMeseNascita = 1 Settembre\n" +
            "|AnnoNascita = [[1981]]\n" +
            "|LuogoMorte = ?\n" +
            "|GiornoMeseMorte = \n" +
            "|AnnoMorte = \n" +
            "|Attività = Modella<ref>Dal 2000</ref>\n" +
            "|Nazionalità = statunitense ?\n" +
            "|PostNazionalità = {{sp}}incoronata [[Miss USA]] [[2008]]<ref>{{cite web\n" +
            "|url= http://www.washingtonpost.com/wp-dyn/content/article/2008/04/12/AR2008041200018.html\n" +
            "|title= Texan Takes Miss {{sp}} USA Crown in Las Vegas\n" +
            "|accessdate= 2008-05-10\n" +
            "|date= 2008-04-12\n" +
            "|publisher= Washington Post\n" +
            "}}</ref>\n" +
            "}}";

    private static String BIO_MERGED_LOSS = "{{Bio\n" +
            "|Nome = Crystle Danae\n" +
            "|Cognome = Stewart\n" +
            "|Sesso = F\n" +
            "|LuogoNascita = Wilmington\n" +
            "|GiornoMeseNascita = 1º settembre\n" +
            "|AnnoNascita = 1981\n" +
            "|LuogoMorte = ?\n" +
            "|GiornoMeseMorte = \n" +
            "|AnnoMorte = \n" +
            "|Attività = modella<ref>Dal 2000</ref>\n" +
            "|Nazionalità = statunitense\n" +
            "|PostNazionalità = {{sp}}incoronata [[Miss USA]] [[2008]]<ref>{{cite web\n" +
            "|url= http://www.washingtonpost.com/wp-dyn/content/article/2008/04/12/AR2008041200018.html\n" +
            "|title= Texan Takes Miss {{sp}} USA Crown in Las Vegas\n" +
            "|accessdate= 2008-05-10\n" +
            "|date= 2008-04-12\n" +
            "|publisher= Washington Post\n" +
            "}}</ref>\n" +
            "}}";

    private static String BIO_MINIMO = "{{Bio\n" +
            "|Nome = \n" +
            "|Cognome = \n" +
            "|Sesso = M\n" +
            "|LuogoNascita = \n" +
            "|GiornoMeseNascita = \n" +
            "|AnnoNascita = \n" +
            "|LuogoMorte = \n" +
            "|GiornoMeseMorte = \n" +
            "|AnnoMorte = \n" +
            "|Attività = \n" +
            "|Nazionalità = \n" +
            "}}";

    private static String BIO_NORMALE = "{{Bio\n" +
            "|Nome = Mario\n" +
            "|Cognome = Rossi\n" +
            "|Sesso = M\n" +
            "|LuogoNascita = Milano\n" +
            "|GiornoMeseNascita = 3 marzo\n" +
            "|AnnoNascita = 1963\n" +
            "|LuogoMorte = \n" +
            "|GiornoMeseMorte = \n" +
            "|AnnoMorte = \n" +
            "|Attività = politico\n" +
            "|Nazionalità = francese\n" +
            "}}";

    private static String BIO_SERVER_MAPPA = "{{Bio\n" +
            "|Titolo = Sir\n" +
            "|Nome = Aldo<ref>Vedi note</ref>\n" +
            "|Cognome = Rossi<ref>Vedi note</ref>\n" +
            "|PostNazionalità = \n" +
            "|LuogoNascita = Rozzano\n" +
            "|LuogoNascitaLink = Milano\n" +
            "|GiornoMeseNascita = 3 marzo\n" +
            "|AnnoNascita = 1963\n" +
            "|Nazionalità = francese\n" +
            "|LuogoMorte = Milano, Italia\n" +
            "|GiornoMeseMorte = \n" +
            "|Categorie = No\n" +
            "|Attività = attore<ref>Vedi note</ref>\n" +
            "|Immagine = \n" +
            "|Didascalia = francese\n" +
            "}}";

    private static String BIO_MERGED_MAPPA = "{{Bio\n" +
            "|Titolo = Sir\n" +
            "|Nome = Mario<ref>Vedi note</ref>\n" +
            "|Cognome = Rossi<ref>Vedi note</ref>\n" +
            "|Sesso = M\n" +
            "|LuogoNascita = Affori\n" +
            "|LuogoNascitaLink = Milano\n" +
            "|GiornoMeseNascita = 3 marzo\n" +
            "|AnnoNascita = 1963\n" +
            "|LuogoMorte = Milano\n" +
            "|GiornoMeseMorte = \n" +
            "|AnnoMorte = \n" +
            "|Attività = politico<ref>Vedi note</ref>\n" +
            "|Nazionalità = francese\n" +
            "|Categorie = No\n" +
            "|Didascalia = francese\n" +
            "}}";

    private static String BIO_ERRORE_MERGED = "{{Bio\n" +
            "|Nome = Charles Edward Maurice\n" +
            "|Cognome = Spencer\n" +
            "|PostCognomeVirgola = '''IX Conte Spencer''' <small>DL </small>\n" +
            "|Sesso = M\n" +
            "|LuogoNascita = Althorp\n" +
            "|GiornoMeseNascita = 20 maggio\n" +
            "|AnnoNascita = 1964\n" +
            "|LuogoMorte = \n" +
            "|GiornoMeseMorte = \n" +
            "|AnnoMorte = \n" +
            "|Attività = nobile\n" +
            "|Nazionalità = inglese\n" +
            "|Categorie = no\n" +
            "|FineIncipit = designato con il [[Titoli di cortesia nel Regno Unito|Titolo di cortesia]] '''Visconte Althorp''' tra il [[1975]] ed il [[1992]], è un [[Paria del Regno Unito|pari]] [[Britannici|britannico]] e fratello di [[Diana Spencer|Diana, Principessa di Galles]]. È un autore, giornalista della carta stampata e televisiva\n" +
            "}}";

    private static String BIO_GUADAGNINO_SORGENTE = "{{Bio\n" +
            "|Nome              = Francesco\n" +
            "|Cognome           = Guadagnino\n" +
            "|Sesso             = M\n" +
            "|LuogoNascita      = Canicattì\n" +
            "|GiornoMeseNascita = 26 dicembre\n" +
            "|AnnoNascita       = 1755\n" +
            "|LuogoMorte        = Canicattì\n" +
            "|GiornoMeseMorte   = 12 maggio \n" +
            "|AnnoMorte         = 1829\n" +
            "|Epoca             = 1700\n" +
            "|Attività          = pittore\n" +
            "|Nazionalità       = italiano\n" +
            "|PostNazionalità   = , attivo in [[Sicilia]] a partire dall'ultimo quarto del [[XVIII secolo]] e il primo quarto del successivo \n" +
            "}}";

    private static String BIO_GUADAGNINO_SORGENTE_BRUTTO = "{{Bio\n" +
            "|Nome              = Francesco\n" +
            "|Cognome           = Guadagnino\n" +
            "|Sesso             = M\n" +
            "|LuogoNascita      = Canicattì?\n" +
            "|Attività          = Pittore\n" +
            "|GiornoMeseNascita = 26 dicembre\n" +
            "|AnnoNascita       = 1755\n" +
            "|LuogoMorte        = Canicattì ?\n" +
            "|GiornoMeseMorte   = 12 maggio \n" +
            "|AnnoMorte         = [[1829]]\n" +
            "|Epoca             = 1700\n" +
            "|Nazionalità       = italiano\n" +
            "|PostNazionalità   = , attivo in [[Sicilia]] a partire dall'ultimo quarto del [[XVIII secolo]] e il primo quarto del successivo \n" +
            "}}";

    private static String BIO_GUADAGNINO_FINALE = "{{Bio\n" +
            "|Nome = Francesco\n" +
            "|Cognome = Guadagnino\n" +
            "|Sesso = M\n" +
            "|LuogoNascita = Canicattì\n" +
            "|GiornoMeseNascita = 26 dicembre\n" +
            "|AnnoNascita = 1755\n" +
            "|LuogoMorte = Canicattì\n" +
            "|GiornoMeseMorte = 12 maggio\n" +
            "|AnnoMorte = 1829\n" +
            "|Epoca = 1700\n" +
            "|Attività = pittore\n" +
            "|Nazionalità = italiano\n" +
            "|PostNazionalità = , attivo in [[Sicilia]] a partire dall'ultimo quarto del [[XVIII secolo]] e il primo quarto del successivo\n" +
            "}}";

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    public ADateService date;

    @Autowired
    protected LibBio libBio;

    @Autowired
    protected BioService bioService;

    @Autowired
    protected ElaboraService service;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected AMongoService mongo;

    @Autowired
    protected ApplicationContext appContext;

    @Autowired
    protected UploadService uploadService;

    @Autowired
    protected PageService pageService;

    @Autowired
    protected Api api;


    @Before
    public void setUpIniziale() {
        Assert.assertNotNull(text);
        Assert.assertNotNull(service);
        Assert.assertNotNull(libBio);
        Assert.assertNotNull(bioService);
        Assert.assertNotNull(mongo);
        Assert.assertNotNull(appContext);
        Assert.assertNotNull(api);
        Assert.assertNotNull(pageService);
        Assert.assertNotNull(date);
    }// end of method


    /**
     * Dal server wiki al bio (non registrato) <br>
     */
    @Test
    public void getTmplBioServer() {
        String tmplBioServer = VUOTA;
        HashMap<String, String> mappa;
        Bio entity = null;

        tmplBioServer = BIO_SORGENTE;
        entity = bioService.newEntity(123456789, "NonRilevante", tmplBioServer);
        entity = service.esegueNoSave(entity);

        System.out.println("*************");
        System.out.println("tmplBioServer");
        System.out.println("*************");
        System.out.println(tmplBioServer);
        System.out.println("");

        Assert.assertEquals(entity.nome, "Crystle Danae");
        Assert.assertEquals(entity.cognome, "Stewart");
        Assert.assertEquals(entity.sesso, "F");
        Assert.assertEquals(entity.luogoNato, "Wilmington");
        Assert.assertEquals(entity.giornoNascita.titolo, "1º settembre");
        Assert.assertEquals(entity.annoNascita.titolo, "1981");
        Assert.assertEquals(entity.attivita.singolare, "modella");
        Assert.assertEquals(entity.nazionalita.singolare, "statunitense");

        stampaParametriMongo(entity);
    }// end of single test


    public void stampaParametriMongo(Bio entity) {
        System.out.println("");
        System.out.println("*************");
        System.out.println("stampaParametri" + " - sono " + ParBio.values().length);
        System.out.println("*************");

        for (ParBio par : ParBio.values()) {
            System.out.println(par.getTag() + ": " + par.getValue(entity));
        }// end of for cycle

    }// end of method


    /**
     * Dalla entity mongoDB al tmplBioMongo <br>
     * Costruisce un template con SOLO i parametri gestiti <br>
     */
    @Test
    public void getTmplBioMongo() {
        String tmplBioMongo = VUOTA;
        Bio entity = service.creaBioMemory(BIO_UNO);
        Assert.assertNotNull(entity);
        tmplBioMongo = service.getTmplBioMongo(entity);

        System.out.println("*************");
        System.out.println("tmplBioMongo");
        System.out.println("*************");
        System.out.println(tmplBioMongo);
        System.out.println("");
    }// end of single test


    @Test
    public void getCampiValidi() {
        System.out.println("");
        System.out.println("*************");
        System.out.println("getCampiValidi" + " - sono " + ParBio.getCampiValidi().size());
        System.out.println("*************");

        for (ParBio par : ParBio.getCampiValidi()) {
            System.out.println(par.getTag());
        }// end of for cycle
    }// end of single test


    @Test
    public void getCampiSignificativi() {
        System.out.println("");
        System.out.println("*************");
        System.out.println("getCampiSignificativi" + " - sono " + ParBio.getCampiSignificativi().size());
        System.out.println("*************");

        for (ParBio par : ParBio.getCampiSignificativi()) {
            System.out.println(par.getTag());
        }// end of for cycle
    }// end of single test


    @Test
    public void getCampiNormali() {
        System.out.println("");
        System.out.println("*************");
        System.out.println("getCampiNormali" + " - sono " + ParBio.getCampiNormali().size());
        System.out.println("*************");

        for (ParBio par : ParBio.getCampiNormali()) {
            System.out.println(par.getTag());
        }// end of for cycle
    }// end of single test


    /**
     * EAElabora.ordinaNormaliNoLoss
     * <p>
     * Riordina il template SENZA nessuna modifica dei valori preesistenti <br>
     * Riordina i parametri <br>
     * Aggiunge quelli 'normali' mancanti vuoti (sono 11) <br>
     * Elimina quelli esistenti vuoti, senza valore <br>
     */
    @Test
    public void ordinaNormaliNoLoss() {
        String tmplOrdinato = service.ordinaNormaliNoLoss(BIO_SORGENTE);
        Assert.assertEquals(tmplOrdinato, BIO_MERGED_NO_LOSS);

        System.out.println("*************");
        System.out.println("ordinaNormaliNoLoss");
        System.out.println("*************");
        System.out.println(tmplOrdinato);
        System.out.println("");
    }// end of single test


    /**
     * Controlla se il tmpl del mongoDB di tutte le istanze è uguale a quello 'previsto' <br>
     * Se è diverso, lo modifica sul server <br>
     */
    @Test
    public void checkAll() {
//        service.checkAll(); @todo provvisoriamente disabilitato perché leggermente lungo
    }// end of single test


    /**
     * Costruisce un template da una mappa di parametri <br>
     */
    @Test
    public void getTmpl() {
        mappa = new HashMap<>();
        previsto = BIO_MINIMO;
        ottenuto = service.getTmpl(mappa);
        Assert.assertEquals(ottenuto, previsto);
    }// end of single test


    /**
     * Costruisce un template da una mappa di parametri <br>
     * ATTENZIONE - Non è merged con eventuali parametri non normali <br>
     */
    @Test
    public void getTmpl2() {
        mappa = new HashMap<>();
        previsto = BIO_NORMALE;
        mappa.put(ParBio.cognome.getTag(), "Rossi");
        mappa.put(ParBio.giornoMeseNascita.getTag(), "3 marzo");
        mappa.put(ParBio.attivita.getTag(), "politico");
        mappa.put(ParBio.nazionalita.getTag(), "francese");
        mappa.put(ParBio.annoNascita.getTag(), "1963");
        mappa.put(ParBio.nome.getTag(), "Mario");
        mappa.put(ParBio.luogoNascita.getTag(), "Milano");

        ottenuto = service.getTmpl(mappa);
        Assert.assertEquals(ottenuto, previsto);

        System.out.println("*************");
        System.out.println("getTmpl");
        System.out.println("*************");
        System.out.println(ottenuto);
        System.out.println("");
    }// end of single test


    @Test
    public void estraeParteValida() {
        ParBio parBio;
        String testoOriginale;
        String parteValidaNuova;
        Object[] nome = {ParBio.nome, "Crystle Danae", "Crystle Danae", "Crystle Danae"};
        Object[] cognome = {ParBio.cognome, "[[Stewart]]", "Stewart", "Stewart"};
        Object[] luogoNascita = {ParBio.luogoNascita, "[[Wilmington]]", "Wilmington", "Wilmington"};
        Object[] giornoMeseNascita = {ParBio.giornoMeseNascita, "1 Settembre", "1º settembre", "1º settembre"};
        Object[] annoNascita = {ParBio.annoNascita, "[[1981]]", "1981", "1981"};
        Object[] luogoMorte = {ParBio.luogoMorte, "?", "?", "?"};
        Object[] attivita = {ParBio.attivita, "modella<ref>Dal 2000</ref>", "modella", "modella"};
        Object[] attivita2 = {ParBio.attivita2, "Pittore<ref>Dal 2000</ref>", "pittore", "pittore"};
        Object[] nazionalita = {ParBio.nazionalita, "statunitense ?", "statunitense", "statunitense"};

        List<Object[]> lista = new ArrayList<>();
        lista.add(nome);
        lista.add(cognome);
        lista.add(luogoNascita);
        lista.add(giornoMeseNascita);
        lista.add(annoNascita);
        lista.add(luogoMorte);
        lista.add(attivita);
        lista.add(attivita2);
        lista.add(nazionalita);

        for (Object[] riga : lista) {
            parBio = (ParBio) riga[0];
            testoOriginale = (String) riga[1];
            parteValidaNuova = (String) riga[2];
            previsto = (String) riga[3];
            ottenuto = service.estraeValore(parBio, testoOriginale);
            Assert.assertEquals(previsto, ottenuto);
            System.out.println("Parametro " + parBio.getTag().toLowerCase() + " elaborato correttamente. Valore valido: " + ottenuto);
        }// end of for cycle
    }// end of single test


    /**
     * 0) nome del parametro
     * 1) valore originario preso dal server wiki
     * 2) valore valido elaborato dal programma e preso da mongoDB
     * 3) valore finale da reinserire sul server
     */
    @Test
    public void sostituisceParteValidaFacile() {
        ParBio parBio;
        String testoOriginale;
        String parteValidaNuova;
        Object[] nome = {ParBio.nome, "Crystle Danae", "Crystle Danae", "Crystle Danae"};
        Object[] cognome = {ParBio.cognome, "[[Stewart]]", "Stewart", "Stewart"};
        Object[] luogoNascita = {ParBio.luogoNascita, "[[Wilmington]]", "Wilmington", "Wilmington"};
        Object[] giornoMeseNascita = {ParBio.giornoMeseNascita, "1 Settembre", "1º settembre", "1º settembre"};
        Object[] annoNascita = {ParBio.annoNascita, "[[1981]]", "1981", "1981"};
        Object[] luogoMorte = {ParBio.luogoMorte, "?", "?", "?"};
        Object[] attivita = {ParBio.attivita, "modella<ref>Dal 2000</ref>", "modella", "modella<ref>Dal 2000</ref>"};
        Object[] attivita2 = {ParBio.attivita2, "Pittore<ref>Dal 2000</ref>", "pittore", "pittore<ref>Dal 2000</ref>"};
        Object[] nazionalita = {ParBio.nazionalita, "statunitense ?", "statunitense", "statunitense"};

        List<Object[]> lista = new ArrayList<>();
        lista.add(nome);
        lista.add(cognome);
        lista.add(luogoNascita);
        lista.add(giornoMeseNascita);
        lista.add(annoNascita);
        lista.add(luogoMorte);
        lista.add(attivita);
        lista.add(attivita2);
        lista.add(nazionalita);

        for (Object[] riga : lista) {
            parBio = (ParBio) riga[0];
            testoOriginale = (String) riga[1];
            parteValidaNuova = (String) riga[2];
            previsto = (String) riga[3];
            ottenuto = service.sostituisceParteValida(parBio, testoOriginale, parteValidaNuova);
            Assert.assertEquals(previsto, ottenuto);
            System.out.println("Parametro " + parBio.getTag().toLowerCase() + " elaborato correttamente. Valore spedito sul server: " + ottenuto);
        }// end of for cycle
    }// end of single test


    /**
     * 0) nome del parametro
     * 1) valore originario preso dal server wiki
     * 2) valore valido elaborato dal programma e preso da mongoDB
     * 3) valore finale da reinserire sul server
     */
//    @Test
    public void sostituisceParteValidaDifficile() {
        ParBio parBio;
        String testoOriginale;
        String parteValidaNuova;
        Object[] nome = {ParBio.nome, "Crystle Danae", "Crystle Danae", "Crystle Danae"};
        Object[] cognome = {ParBio.cognome, "[[Stewart]]", "Stewart", "Stewart"};
        Object[] luogoNascita = {ParBio.luogoNascita, "[[Wilmington]]<ref>Pippoz</ref>", "Wilmington", "Wilmington<ref>Pippoz</ref>"};
        Object[] giornoMeseNascita = {ParBio.giornoMeseNascita, "1 Settembre", "1º settembre", "1º settembre"};
        Object[] annoNascita = {ParBio.annoNascita, "[[1981]]{{forse}}", "1981", "1981{{forse}}"};
        Object[] luogoMorte = {ParBio.luogoMorte, "?", "", "?"};
        Object[] attivita = {ParBio.attivita, "Modella<ref>Dal 2000</ref>", "modella", "modella<ref>Dal 2000</ref>"};
        Object[] nazionalita = {ParBio.nazionalita, "statunitense ?", "statunitense", "statunitense"};

        List<Object[]> lista = new ArrayList<>();
        lista.add(nome);
        lista.add(cognome);
        lista.add(luogoNascita);
        lista.add(giornoMeseNascita);
        lista.add(annoNascita);
        lista.add(luogoMorte);
        lista.add(attivita);
        lista.add(nazionalita);

        for (Object[] riga : lista) {
            parBio = (ParBio) riga[0];
            testoOriginale = (String) riga[1];
            parteValidaNuova = (String) riga[2];
            previsto = (String) riga[3];
            ottenuto = service.sostituisceParteValida(parBio, testoOriginale, parteValidaNuova);
            Assert.assertEquals(previsto, ottenuto);
            System.out.println("Parametro " + parBio.getTag().toLowerCase() + " elaborato correttamente. Valore spedito sul server: " + ottenuto);
        }// end of for cycle
    }// end of single test


    /**
     * EAElabora.ordinaNormaliNoLoss
     * <p>
     * Riordina il template SENZA nessuna modifica dei valori preesistenti <br>
     * Riordina i parametri <br>
     * Aggiunge quelli 'normali' mancanti vuoti (sono 11) <br>
     * Elimina quelli esistenti vuoti, senza valore <br>
     */
    @Test
    public void ordinaNormaliNoLoss2() {
        String title = "Charles Spencer, IX conte Spencer";
        String templServer = api.leggeTmplBio(title);

        String tmplOrdinato = service.ordinaNormaliNoLoss(templServer);
        Assert.assertEquals(tmplOrdinato, templServer);

        System.out.println("*************");
        System.out.println("ordinaNormaliNoLoss2");
        System.out.println("*************");
        System.out.println(tmplOrdinato);
        System.out.println("");
    }// end of single test


//    /**
//     * EAElabora.ordinaNormaliNoLoss
//     * <p>
//     * Riordina il template CON modifiche ai valori preesistenti <br>
//     * Riordina i parametri <br>
//     * Aggiunge quelli 'normali' mancanti vuoti (sono 11) <br>
//     * Elimina quelli esistenti vuoti, senza valore <br>
//     * Modifica i parametri secondo le regole base (minuscole, 1° del mese, parentesi quadre) <br>
//     */
//    @Test
//    public void ordinaNormaliWithLoss() {
//        String tmplOrdinato = service.ordinaNormaliNoLoss(BIO_GUADAGNINO_SORGENTE_BRUTTO);
//        Assert.assertEquals(BIO_GUADAGNINO_FINALE,tmplOrdinato);
//
//        System.out.println("*************");
//        System.out.println("ordinaNormaliNoLoss - Guadagnino brutto");
//        System.out.println("*************");
//        System.out.println(tmplOrdinato);
//        System.out.println("");
//    }// end of single test


    /**
     * Merge di un template con i parametri di mappaMongo PIU quelli di mappaServer <br>
     * <p>
     * Se esiste un parametro 'normale' di mappaMongo, lo usa <br>
     * Se non esiste un parametro 'normale' di mappaMongo ma esiste di mappaServer, lo usa <br>
     * Se non esiste un parametro 'normale' ne di mappaMongo ne di mappaServer, usa un stringa di valore VUOTA <br>
     * Non esistono parametri 'extra' di mappaMongo <br>
     * Se esiste un parametro 'extra' di mappaServer, lo usa <br>
     * Se non esiste un parametro 'extra' di mappaServer, non inserisce la riga  <br>
     */
    @Test
    public void getTmplMerged() {
        String tmplBioMerged = VUOTA;
        String tmplBioMongo = VUOTA;
        String tmplBioServer = VUOTA;
        Bio entity = null;

        entity = service.creaBioMemory(BIO_UNO);
        Assert.assertNotNull(entity);
        tmplBioMongo = service.getTmplBioMongo(entity);
        tmplBioServer = service.getTmplBioServer(entity);

        tmplBioMerged = service.getMerged(tmplBioMongo, tmplBioServer);
        Assert.assertEquals(BIO_MERGED_LOSS, tmplBioMerged);

        System.out.println("*************");
        System.out.println("tmplBioMerged");
        System.out.println("*************");
        System.out.println(tmplBioMerged);
        System.out.println("");
    }// end of single test


    /**
     * Merge di una mappa di parametri PIU i parametri del tmplBioServer <br>
     * <p>
     * Se esiste un parametro 'normale' di mappaMongo, lo usa <br>
     * Se non esiste un parametro 'normale' di mappaMongo ma esiste di mappaServer, lo usa <br>
     * Se non esiste un parametro 'normale' ne di mappaMongo ne di mappaServer, usa un stringa di valore VUOTA <br>
     * Non esistono parametri 'extra' di mappaMongo <br>
     * Se esiste un parametro 'extra' di mappaServer, lo usa <br>
     * Se non esiste un parametro 'extra' di mappaServer, non inserisce la riga  <br>
     */
    @Test
    public void getTmplMerged2() {
        String tmplBioMerged = VUOTA;
        String tmplBioServer = VUOTA;
        mappa = new HashMap<>();
        previsto = BIO_NORMALE;
        mappa.put(ParBio.giornoMeseNascita.getTag(), "3 marzo");
        mappa.put(ParBio.attivita.getTag(), "politico");
        mappa.put(ParBio.annoNascita.getTag(), "1963");
        mappa.put(ParBio.nome.getTag(), "Mario");
        mappa.put(ParBio.luogoNascita.getTag(), "Affori");

        tmplBioMerged = service.getMerged(mappa, BIO_SERVER_MAPPA);
        Assert.assertEquals(tmplBioMerged, BIO_MERGED_MAPPA);

        System.out.println("*************");
        System.out.println("tmplBioServerPreesistente");
        System.out.println("*************");
        System.out.println(BIO_SERVER_MAPPA);
        System.out.println("");
        System.out.println("*************");
        System.out.println("tmplBioMergedMappa");
        System.out.println("*************");
        System.out.println(tmplBioMerged);
        System.out.println("");
    }// end of single test

}// end of class
