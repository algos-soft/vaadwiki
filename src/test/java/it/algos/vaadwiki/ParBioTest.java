package it.algos.vaadwiki;

import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.service.AMongoService;
import it.algos.vaadflow.service.AReflectionService;
import it.algos.vaadwiki.download.ElaboraService;
import it.algos.vaadwiki.download.PageService;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.vaadwiki.service.LibBio;
import it.algos.vaadwiki.service.ParBio;
import it.algos.wiki.Api;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 09-set-2019
 * Time: 07:27
 */
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Test per creare un template")
public class ParBioTest extends ATest {

    @InjectMocks
    public PageService pageService;

    @InjectMocks
    public Api api;

    @InjectMocks
    public BioService bioService;

    @InjectMocks
    public ElaboraService elaboraService;

    @InjectMocks
    public AnnoService annoService;

    @InjectMocks
    protected LibBio libBio;
    @InjectMocks
    public AMongoService mongo;
    @InjectMocks
    public AReflectionService reflection;

    private LinkedHashMap<String, String> mappa = getMappaUno();

    private LinkedHashMap<ParBio, String> mappaParBio = getMappaParBio();

    private LinkedHashMap<ParBio, String> mappaParBioEstesa = getMappaParBioEstesa();

    private String titoloBio = "Pasquale Villani";


    @BeforeAll
    public void setUp() {
        super.setUpTest();
        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(libBio);
        MockitoAnnotations.initMocks(pageService);
        MockitoAnnotations.initMocks(api);
        MockitoAnnotations.initMocks(bioService);
        MockitoAnnotations.initMocks(elaboraService);
        MockitoAnnotations.initMocks(annoService);
        MockitoAnnotations.initMocks(mongo);
        MockitoAnnotations.initMocks(reflection);
        pageService.text = text;
        pageService.api = api;
        pageService.bioService = bioService;
        pageService.elaboraService = elaboraService;
        api.text = text;
        api.pageService = pageService;
        bioService.text = text;
        elaboraService.text = text;
        elaboraService.libBio = libBio;
        libBio.annoService = annoService;
        libBio.mongo = mongo;
        mongo.reflection = reflection;
    }// end of method


    private LinkedHashMap<String, String> getMappaUno() {
        LinkedHashMap<String, String> mappa = new LinkedHashMap<>();

        mappa.put("Nome", "Aldo");
        mappa.put("Cognome", "Brunetta");
        mappa.put("Attività", "Cantante");

        System.out.println("");
        System.out.println("*************Mappa");
        for (Map.Entry<String, String> entry : mappa.entrySet()) {
            System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
        }// end of for cycle
        System.out.println("*************Mappa");

        return mappa;
    }// end of single test


    private LinkedHashMap<ParBio, String> getMappaParBio() {
        LinkedHashMap<ParBio, String> mappaParBio = new LinkedHashMap<>();

        mappaParBio.put(ParBio.nome, "Francesco");
        mappaParBio.put(ParBio.cognome, "Pope");
        mappaParBio.put(ParBio.nazionalita, "polacco");

        System.out.println("");
        System.out.println("*************MappaParBio");
        for (Map.Entry<ParBio, String> entry : mappaParBio.entrySet()) {
            System.out.println("Key : " + entry.getKey().getTag() + " Value : " + entry.getValue());
        }// end of for cycle
        System.out.println("*************MappaParBio");

        return mappaParBio;
    }// end of single test


    private LinkedHashMap<ParBio, String> getMappaParBioEstesa() {
        LinkedHashMap<ParBio, String> mappaParBioEstesa = new LinkedHashMap<>();

        mappaParBioEstesa.put(ParBio.titolo, "Onorevole");
        mappaParBioEstesa.put(ParBio.nome, "Marcello");
        mappaParBioEstesa.put(ParBio.cognome, "Giolitti");
        mappaParBioEstesa.put(ParBio.pseudonimo, "mark");
        mappaParBioEstesa.put(ParBio.noteNascita, "Data non conosciuta");
        mappaParBioEstesa.put(ParBio.nazionalita, "russo");
        mappaParBioEstesa.put(ParBio.cittadinanza, "americana");

        System.out.println("");
        System.out.println("*************MappaParBioEstesa");
        for (Map.Entry<ParBio, String> entry : mappaParBioEstesa.entrySet()) {
            System.out.println("Key : " + entry.getKey().getTag() + " Value : " + entry.getValue());
        }// end of for cycle
        System.out.println("*************MappaParBioEstesa");

        return mappaParBioEstesa;
    }// end of single test


    @Test
    /**
     */
    public void creaBioPar() {
        String testoGrezzo = "";
        for (Map.Entry<String, String> entry : mappa.entrySet()) {
            testoGrezzo += libBio.creaBioPar(entry.getKey(), entry.getValue());
        }// end of for cycle
        testoGrezzo = testoGrezzo.trim();

        System.out.println("*************Uno");
        System.out.println("Template uno");
        System.out.println("*************Uno");
        System.out.println(testoGrezzo);
        System.out.println("*************Uno");
    }// end of single test


    @Test
    public void creaBioParAll() {
        String testoGrezzo = libBio.creaBioParAll(mappa);

        System.out.println("*************Due");
        System.out.println("Template due");
        System.out.println("*************Due");
        System.out.println(testoGrezzo);
        System.out.println("*************Due");
    }// end of single test


    @Test
    public void creaBioParTemplate() {
        String testoGrezzo = libBio.creaBioParTemplate(mappa);

        System.out.println("*************Tre");
        System.out.println("Template tre");
        System.out.println("*************Tre");
        System.out.println(testoGrezzo);
        System.out.println("*************Tre");
    }// end of single test


    @Test
    public void creaBioTemplate() {
        String testoGrezzo = libBio.creaBioTemplate(mappaParBio);

        System.out.println("*************Quattro");
        System.out.println("Template quattro");
        System.out.println("*************Quattro");
        System.out.println(testoGrezzo);
        System.out.println("*************Quattro");
    }// end of single test


    @Test
    public void creaBioTemplateEstesa() {
        String testoGrezzo = libBio.creaBioTemplate(mappaParBioEstesa);

        System.out.println("*************Cinque");
        System.out.println("Template cinque");
        System.out.println("*************Cinque");
        System.out.println(testoGrezzo);
        System.out.println("*************Cinque");
    }// end of single test


    @Test
    public void creaTemplate() {
        Bio bio = api.leggeBio(titoloBio);
        bio = elaboraService.esegueNoSave(bio);

        String testoGrezzo = libBio.creaTemplate(bio);

        System.out.println("*************Sei");
        System.out.println("Template sei");
        System.out.println("*************Sei");
        System.out.println(testoGrezzo);
        System.out.println("*************Sei");
    }// end of single test


}// end of class
