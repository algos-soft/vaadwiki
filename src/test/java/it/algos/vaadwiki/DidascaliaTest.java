package it.algos.vaadwiki;

import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.AReflectionService;
import it.algos.vaadwiki.didascalia.*;
import it.algos.vaadwiki.download.ElaboraService;
import it.algos.vaadwiki.download.PageService;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.vaadwiki.didascalia.DidascaliaService;
import it.algos.vaadwiki.service.LibBio;
import it.algos.wiki.Api;
import lombok.extern.slf4j.Slf4j;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 21-gen-2019
 * Time: 08:39
 */

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@DataMongoTest
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = WikiApplication.class)

//@ExtendWith(MockitoExtension.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@Tag("didascalia")
//@DisplayName("Test per le didascalie")
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Test semplice per le didascalie")
public class DidascaliaTest extends ATest {


    @InjectMocks
    public Api api;


//    @InjectMocks
//    public Didascalia didascalia;

    @InjectMocks
    public DidascaliaService didascaliaService;

    @InjectMocks
    public DidascaliaGiornoNato giornoNato;

    @InjectMocks
    public DidascaliaAnnoNato annoNato;

    @InjectMocks
    public DidascaliaGiornoMorto giornoMorto;

    @InjectMocks
    public DidascaliaAnnoMorto annoMorto;

    @InjectMocks
    public DidascaliaListe standard;

    @InjectMocks
    public DidascaliaBiografie completa;

    @InjectMocks
    public AnnoService annoService;

    @InjectMocks
    public LibBio libBio;

    @InjectMocks
    public GiornoService giorno;

    @InjectMocks
    protected PageService pageService;

    @InjectMocks
    protected BioService bioService;

    @InjectMocks
    protected ElaboraService elaboraService;

    @InjectMocks
    protected AReflectionService reflection;

    @InjectMocks
    protected PreferenzaService pref;

    @InjectMocks
    protected ADateService date;

    private String wikiTitle = "Adone Asinari";

    private String textPage;

    private Bio bio;


    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(api);
        MockitoAnnotations.initMocks(pageService);
        MockitoAnnotations.initMocks(bioService);
        MockitoAnnotations.initMocks(elaboraService);
        MockitoAnnotations.initMocks(annoService);
        MockitoAnnotations.initMocks(libBio);
        MockitoAnnotations.initMocks(giorno);
        MockitoAnnotations.initMocks(reflection);
//        MockitoAnnotations.initMocks(mongoOperations);
//        MockitoAnnotations.initMocks(mongoService);
        MockitoAnnotations.initMocks(text);
//        MockitoAnnotations.initMocks(didascalia);
        MockitoAnnotations.initMocks(giornoNato);
        MockitoAnnotations.initMocks(annoNato);
        MockitoAnnotations.initMocks(giornoMorto);
        MockitoAnnotations.initMocks(annoMorto);
        MockitoAnnotations.initMocks(standard);
        MockitoAnnotations.initMocks(completa);
        MockitoAnnotations.initMocks(didascaliaService);
        MockitoAnnotations.initMocks(pref);
        MockitoAnnotations.initMocks(date);
        api.pageService = pageService;
        api.text = text;
        pageService.api = api;
        pageService.text = text;
        pageService.bioService = bioService;
        pageService.elaboraService = elaboraService;
        elaboraService.text = text;
        elaboraService.libBio = libBio;
        libBio.giorno = giorno;
//        libBio.mongo = mongoService;
//        mongoService.mongoOp = mongoOperations;
//        mongoService.reflection = reflection;
//        mongoService.text = text;
//        didascalia.text = text;
        giornoNato.annoService = annoService;
        giornoNato.text = text;
        annoNato.text = text;
        giornoMorto.text = text;
        annoMorto.text = text;
        standard.text = text;
        completa.text = text;
//        didascalia.didascaliaCompleta = completa;
//        didascalia.didascaliaGiornoNato = giornoNato;
//        didascalia.didascaliaAnnoNato = annoNato;
//        didascalia.didascaliaGiornoMorto = giornoMorto;
//        didascalia.didascaliaAnnoMorto = annoMorto;
//        didascalia.didascaliaStandard = standard;
        bio = api.leggeBio(wikiTitle);
        didascaliaService.wikiTitle = wikiTitle;
        didascaliaService.pref = pref;
        didascaliaService.date = date;
        didascaliaService.api = api;
        didascaliaService.text = text;
    }// end of method


    @Test
    public void download() {
        System.out.println("*************");
        System.out.println("Tipi possibili di didascalie per " + wikiTitle);
        System.out.println("Senza chiave");
        System.out.println("*************");
        for (EADidascalia dida : EADidascalia.values()) {
//            ottenuto = didascalia.esegue(bio, dida, false);
            System.out.println(dida.name() + ": " + ottenuto);
        }// end of for cycle
        System.out.println("*************");
        System.out.println("Con chiave");
        System.out.println("*************");
        for (EADidascalia dida : EADidascalia.values()) {
//            ottenuto = didascalia.esegue(bio, dida);
            System.out.println(dida.name() + ": " + ottenuto);
        }// end of for cycle
        System.out.println("*************");

    }// end of single test


    /**
     * Test con uscita sul terminale di Idea
     */
    @Test
    public void esegueTestDidascalie() {
        System.out.println("");
        System.out.println("Algos");
        System.out.println("");
        System.out.println("Tipi possibili di discalie");
        System.out.println("Esempio '" + wikiTitle + "'");
        System.out.println("");
        Bio bio = api.leggeBio(wikiTitle);
        for (EADidascalia dida : EADidascalia.values()) {
//            System.out.println(dida.name() + ": " + didascalia.esegue(bio, dida));
        }// end of for cycle
        System.out.println("");
    }// end of single test


//    /**
//     * Pagina completa con uscita su pagina utente
//     */
//    @Test
//    public void esegueTestUplod() {
//        didascaliaService.esegue();
//    }// end of single test

}// end of class
