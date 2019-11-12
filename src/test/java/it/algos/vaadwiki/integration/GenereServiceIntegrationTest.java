package it.algos.vaadwiki.integration;

import it.algos.vaadwiki.ATest;
import it.algos.vaadwiki.modules.genere.Genere;
import it.algos.vaadwiki.modules.genere.GenereService;
import it.algos.vaadwiki.modules.professione.Professione;
import it.algos.vaadwiki.modules.professione.ProfessioneService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 12-nov-2019
 * Time: 14:39
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GenereServiceIntegrationTest extends ATest {


    @Autowired
    protected GenereService genereService;

    @Autowired
    protected ProfessioneService professioneService;

    private List<Genere> listaGenereAll;

    private List<Genere> listaGenere;


    @Before
    public void setUpIniziale() {
        Assert.assertNotNull(genereService);
    }// end of method


    /**
     * Recupera tutte le istanze della Entity usando la query della property specifica <br>
     *
     * @param plurale maschile o femminile
     *
     * @return all entities
     */
    @Test
    public void findAllByPlurale() {
        sorgente = "allenatori di calcio";
        listaGenere = genereService.findAllByPlurale(sorgente);
        Assert.assertNotNull(listaGenere);
        System.out.println("");
        System.out.println("*************");
        System.out.println("Lista generi associati a '" + sorgente + "'");
        System.out.println("*************");
        for (Genere genere : listaGenere) {
            System.out.println(genere.getSingolare());
        }// end of for cycle

        sorgente = "danzatori";
        listaGenere = genereService.findAllByPlurale(sorgente);
        Assert.assertNotNull(listaGenere);
        System.out.println("");
        System.out.println("*************");
        System.out.println("Lista generi associati a '" + sorgente + "'");
        System.out.println("*************");
        for (Genere genere : listaGenere) {
            System.out.println(genere.getSingolare());
        }// end of for cycle

        listaGenereAll = (List<Genere>) genereService.findAll();
        System.out.println("");
        System.out.println("*************");
        System.out.println("Plurale - singolari");
        System.out.println("*************");
        for (Genere genere : listaGenereAll) {
            listaGenere = genereService.findAllByPlurale(genere);
            String M = text.isValid(genere.pluraleMaschile) ? genere.pluraleMaschile + "(M)" : "";
            String F = text.isValid(genere.pluraleFemminile) ? genere.pluraleFemminile + "(F)" : "";
            String sep = (text.isValid(M) && text.isValid(F)) ? " - " : "";
            Professione prof;
            String riga="";
            String pag="";

            for (Genere genere2 : listaGenere) {
                prof= professioneService.findByKeyUnica(genere.singolare);
                if (prof!=null) {
                    pag=prof.getPagina();
                }// end of if cycle
                riga=riga+","+genere2.singolare+"("+pag+")";
            }// end of for cycle

            System.out.println(M + sep + F + " =-> " + riga);
        }// end of for cycle


    }// end of single test

}// end of class
