package it.algos.vaadwiki.integration;

import it.algos.vaadwiki.ATest;
import it.algos.vaadwiki.download.ElaboraService;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.vaadwiki.service.LibBio;
import it.algos.vaadwiki.service.ParBio;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 02-gen-2020
 * Time: 20:04
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


    @Autowired
    protected LibBio libBio;

    @Autowired
    protected BioService bioService;

    @Autowired
    protected ElaboraService service;


    @Before
    public void setUpIniziale() {
        Assert.assertNotNull(text);
        Assert.assertNotNull(service);
        Assert.assertNotNull(libBio);
        Assert.assertNotNull(bioService);
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
    }// end of method


    /**
     * Riordina il template SENZA nessuna modifica dei valori preesistenti <br>
     * Riordina i parametri <br>
     * Aggiunge quelli 'normali' mancanti vuoti (sono 11) <br>
     * Elimina quelli esistenti vuoti, senza valore <br>
     */
    @Test
    public void riordina() {
        String tmplOrdinato = service.riordina(BIO_SORGENTE);
        Assert.assertEquals(tmplOrdinato, BIO_ORDINATO);

        System.out.println("*************");
        System.out.println("tmplOrdinato");
        System.out.println("*************");
        System.out.println(tmplOrdinato);
        System.out.println("");
    }// end of method

}// end of class
