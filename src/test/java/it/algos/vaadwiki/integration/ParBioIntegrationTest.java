package it.algos.vaadwiki.integration;

import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.service.AMongoService;
import it.algos.vaadflow.service.AReflectionService;
import it.algos.vaadwiki.ATest;
import it.algos.vaadwiki.download.ElaboraService;
import it.algos.vaadwiki.download.PageService;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.vaadwiki.service.LibBio;
import it.algos.vaadwiki.service.ParBio;
import it.algos.wiki.Api;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedHashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 10-set-2019
 * Time: 06:04
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ParBioIntegrationTest extends ATest {

    @Autowired
    public PageService pageService;

    @Autowired
    public Api api;

    @Autowired
    public BioService bioService;

    @Autowired
    public ElaboraService elaboraService;

    @Autowired
    public AnnoService annoService;

    @Autowired
    protected LibBio libBio;

    @Autowired
    public AMongoService mongo;

    @Autowired
    public AReflectionService reflection;

    private LinkedHashMap<String, String> mappa = getMappaUno();

    private LinkedHashMap<ParBio, String> mappaParBio = getMappaParBio();

    private LinkedHashMap<ParBio, String> mappaParBioEstesa = getMappaParBioEstesa();

    private String titoloBio = "Pasquale Villani";

    @Before
    public void setUp() {
        Assert.assertNotNull(pageService);
        Assert.assertNotNull(api);
        Assert.assertNotNull(bioService);
        Assert.assertNotNull(elaboraService);
        Assert.assertNotNull(annoService);
        Assert.assertNotNull(libBio);
        Assert.assertNotNull(mongo);
        Assert.assertNotNull(reflection);
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

}// end of class
