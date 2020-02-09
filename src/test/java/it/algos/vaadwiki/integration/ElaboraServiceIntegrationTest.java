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

import java.util.HashMap;

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
            "|GiornoMeseNascita = 1º settembre\n" +
            "|AnnoNascita = [[1981]]\n" +
            "|LuogoMorte = ?\n" +
            "|GiornoMeseMorte = \n" +
            "|AnnoMorte = \n" +
            "|Attività = modella<ref>Dal 2000</ref>\n" +
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
//    @Test
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
     * EAElabora.ordinaNormaliNoLoss
     * <p>
     * Parte da una entity Bio esistente sul mongoDB <br>
     * Il tmpl del mongoDB è diverso da quello 'previsto' <br>
     * Riordina il template SENZA nessuna modifica dei valori preesistenti <br>
     * Riordina i parametri <br>
     * Aggiunge quelli 'normali' mancanti vuoti (sono 11) <br>
     * Elimina quelli esistenti vuoti, senza valore <br>
     * Registra le modifiche sul server wiki <br>
     */
//    @Test
//    public void ordinaNormaliNoLoss3() {
//        long inizio;
//        List<Bio> lista;
//        Sort sort = new Sort(Sort.Direction.ASC, "_id");
////        List<Bio> lista = mongo.mongoOp.find(new Query().with(PageRequest.of(20, 100, sort)), Bio.class);
//        lista= bioService.findAll();
//        int cont = 0;
//
//        inizio = System.currentTimeMillis();
//        if (lista != null && lista.size() > 0) {
//            for (Bio bio : lista) {
//                if (service.check(bio)) {
////                    service.uploadNormaliNoLoss(bio);
//                    cont++;
//                }// end of if cycle
//            }// end of for cycle
//        }// end of if cycle
//
//        System.out.println("Modificati " + cont + " su " + lista.size() + " in " + date.deltaText(inizio));
//    }// end of single test


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

        tmplBioMerged = service.getMergedNoLoss(tmplBioMongo, tmplBioServer);
        Assert.assertEquals(tmplBioMerged, BIO_MERGED_NO_LOSS);

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

        tmplBioMerged = service.getMergedNoLoss(mappa, BIO_SERVER_MAPPA);
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
//     * Regola questo campo
//     * <p>
//     * Elimina il testo successivo a varii tag (fixPropertyBase)
//     * Elimina il testo se NON contiene una spazio vuoto (tipico della data giorno-mese)
//     * Elimina eventuali DOPPI spazi vuoto (tipico della data tra il giorno ed il mese)
//     * Controlla che il valore esista nella collezione Giorno
//     * Elimina la prima maiuscola del mese
//     *
//     * @param testoGrezzo in entrata da elaborare
//     *
//     * @return testoValido regolato in uscita
//     */
//    @Test
//    public void fixGiornoValido() {
//        sorgente = "3 Marzo";
//        previsto = "3 marzo";
//        ottenuto = libBio.fixGiornoValido(sorgente);
//        Assert.assertEquals(ottenuto, previsto);
//
//        sorgente = "1° Marzo";
//        previsto = "1º marzo";
//        ottenuto = libBio.fixGiornoValido(sorgente);
//        Assert.assertEquals(previsto, ottenuto);
//
//
//    }// end of single test

}// end of class
