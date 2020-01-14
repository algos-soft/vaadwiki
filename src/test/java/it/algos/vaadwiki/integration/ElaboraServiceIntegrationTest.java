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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

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
            " | GiornoMeseNascita = 1º Settembre\n" +
            "|AnnoNascita = [[1981]]\n" +
            "|AnnoMorte=\n" +
            "|LuogoMorte = ? \n" +
            "|Nazionalità = statunitense ?\n" +
            "|Attività = modella<ref>Dal 2000</ref>\n" +
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

    private static String BIO_ORDINATO = "{{Bio\n" +
            "|Nome = Crystle Danae\n" +
            "|Cognome = [[Stewart]]\n" +
            "|Sesso = F\n" +
            "|LuogoNascita = [[Wilmington]]\n" +
            "|GiornoMeseNascita = 1º Settembre\n" +
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

    private static String BIO_MERGED = "{{Bio\n" +
            "|Nome = Crystle Danae\n" +
            "|Cognome = Stewart\n" +
            "|Sesso = F\n" +
            "|LuogoNascita = Wilmington\n" +
            "|GiornoMeseNascita = 1º settembre\n" +
            "|AnnoNascita = 1981\n" +
            "|LuogoMorte = \n" +
            "|GiornoMeseMorte = \n" +
            "|AnnoMorte = \n" +
            "|Attività = modella\n" +
            "|Nazionalità = statunitense\n" +
            "|PostNazionalità = {{sp}}incoronata [[Miss USA]] [[2008]]<ref>{{cite web\n" +
            "|url= http://www.washingtonpost.com/wp-dyn/content/article/2008/04/12/AR2008041200018.html\n" +
            "|title= Texan Takes Miss {{sp}} USA Crown in Las Vegas\n" +
            "|accessdate= 2008-05-10\n" +
            "|date= 2008-04-12\n" +
            "|publisher= Washington Post\n" +
            "}}</ref>\n" +
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
    public void controlloParametri() {
        System.out.println("");
        System.out.println("*************");
        System.out.println("getCampiNormali" + " - sono " + ParBio.getCampiNormali().size());
        System.out.println("*************");

        for (ParBio par : ParBio.getCampiNormali()) {
            System.out.println(par.getTag());
        }// end of for cycle
    }// end of single test


    /**
     * Merge dei template <br>
     * Costruisce un template con i parametri di tmplBioMongo PIU quelli di tmplBioServer <br>
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

//        tmplBioServer = BIO_SORGENTE;
//        entity = bioService.newEntity(123456789, "NonRilevante", tmplBioServer);
//        entity = service.esegueNoSave(entity);

        tmplBioMerged = service.getTmplMerged(tmplBioMongo, tmplBioServer);
        Assert.assertEquals(tmplBioMerged, BIO_MERGED);

        System.out.println("*************");
        System.out.println("tmplBioMerged");
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
    public void ordinaNormaliNoLoss() {
        String tmplOrdinato = service.ordinaNormaliNoLoss(BIO_SORGENTE);
        Assert.assertEquals(tmplOrdinato, BIO_ORDINATO);

        System.out.println("*************");
        System.out.println("ordinaNormaliNoLoss");
        System.out.println("*************");
        System.out.println(tmplOrdinato);
        System.out.println("");
    }// end of single test


//    /**
//     * EAElabora.ordinaNormaliNoLoss
//     * <p>
//     * Riordina il template SENZA nessuna modifica dei valori preesistenti <br>
//     * Riordina i parametri <br>
//     * Aggiunge quelli 'normali' mancanti vuoti (sono 11) <br>
//     * Elimina quelli esistenti vuoti, senza valore <br>
//     * Registra le modifiche sul mongoDB <br>
//     */
//    @Test
//    public void ordinaNormaliNoLoss2() {
//        Sort sort = new Sort(Sort.Direction.ASC, "_id");
//        List<Bio> lista = mongo.mongoOp.find(new Query().with(PageRequest.of(0, 10, sort)), Bio.class);
//        String oldTmpl;
//
//        System.out.println("*************");
//        System.out.println("ordinaNormaliNoLoss");
//        System.out.println("*************");
//        System.out.println("");
//        for (Bio bio : lista) {
//            oldTmpl = bio.getTmplBioServer();
//            if (service.ordinaNormaliNoLoss(bio)) {
//                System.out.println("*************");
//                System.out.println("prima");
//                System.out.println("*************");
//                System.out.println(oldTmpl);
//                System.out.println("");
//                System.out.println("*************");
//                System.out.println("dopo");
//                System.out.println("*************");
//                System.out.println(bio.getTmplBioServer());
//                System.out.println("");
//            } else {
//                System.out.println("Non modificato - " + bio.getWikiTitle());
//            }// end of if/else cycle
//
//
//        }// end of for cycle
//}// end of single test


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
    @Test
    public void ordinaNormaliNoLoss2() {
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
    }// end of single test

    /**
     * Controlla se il tmpl del mongoDB di tutte le istanze è uguale a quello 'previsto' <br>
     * Se è diverso, lo modifica sul server <br>
     */
    @Test
    public void checkAll() {
        service.checkAll();
    }// end of single test

    }// end of class
