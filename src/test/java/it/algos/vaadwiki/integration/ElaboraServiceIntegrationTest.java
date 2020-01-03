package it.algos.vaadwiki.integration;

import it.algos.vaadwiki.ATest;
import it.algos.vaadwiki.download.ElaboraService;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.service.ParBio;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Autowired
    protected ElaboraService service;


    @Before
    public void setUpIniziale() {
        Assert.assertNotNull(text);
        Assert.assertNotNull(service);
    }// end of method


    /**
     * Dal server wiki al bio (non registrato) <br>
     */
    @Test
    public void creaBio() {
        String tmplBioServer = VUOTA;
        Bio entity = service.creaBioMemory(BIO_UNO);
        Assert.assertNotNull(entity);
        tmplBioServer = service.getTmplBioServer(entity);

        System.out.println("*************");
        System.out.println("tmplBioServer");
        System.out.println("*************");
        System.out.println(tmplBioServer);
        System.out.println("");

        stampaParametriMongo(entity);
    }// end of method


    public void stampaParametriMongo(Bio entity) {
        System.out.println("");
        System.out.println("*************");
        System.out.println("stampaParametri");
        System.out.println("*************");

        for (ParBio par : ParBio.values()) {
            System.out.println(par.getTag() + ": " + par.getValue(entity));
        }// end of for cycle

    }// end of method

}// end of class
