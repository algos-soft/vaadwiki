package it.algos.vaadwiki.integration;

import it.algos.vaadwiki.ATest;
import it.algos.vaadwiki.modules.attivita.Attivita;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.nazionalita.Nazionalita;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 12-set-2019
 * Time: 22:01
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BioIntegrationTest extends ATest {

    private static List<String> elencoNomiFemminili = new ArrayList<>(Arrays.asList(
            "Paola",
            "Risa",
            "Alicia",
            "Cornelia",
            "Egidia"));


    private static List<String> elencoNomiMaschili = new ArrayList<>(Arrays.asList(
            "João",
            "Francesco",
            "Alric",
            "Ming",
            "Joachim",
            "Max",
            "Ivanko",
            "Nazareno",
            "Diego",
            "Anton",
            "Stephen",
            "Tim",
            "Cornelius",
            "Ottavio",
            "Marcos",
            "Alfredo",
            "Dante",
            "Antonio",
            "Antonino",
            "Marcello",
            "Giulio",
            "Paul",
            "Louis",
            "Angelo",
            "Nino",
            "Iacopo",
            "Daniel",
            "Davide",
            "August",
            "Michele",
            "William",
            "Alessandro",
            "Teodoro",
            "Gianni",
            "Enrico",
            "Manuel",
            "Cesare",
            "Alberto",
            "Orlando",
            "Dario",
            "Franco",
            "Guido",
            "Salvatore",
            "Luigi",
            "Pierluigi",
            "Lorenzo",
            "Albert",
            "Franco",
            "Marco",
            "Renato",
            "Otto",
            "Pietro",
            "Carlo",
            "Gregorio",
            "Amadeo",
            "Anselmo",
            "Sam",
            "Domenico",
            "Giacomo",
            "Claudio"));


    @Autowired
    public MongoOperations mongoOp;

    @Autowired
    public MongoTemplate mongoTemplate;

    private List<Bio> listaMaschi;

    private List<Bio> listaFemmine;

    private List<Bio> listaSenzaNome;

    private List<Bio> listaMista;


    private List<Bio> lista;


    @Before
    public void setUp() {
        Assert.assertNotNull(text);
        Assert.assertNotNull(mongoOp);
        Assert.assertNotNull(mongoTemplate);

        if (lista == null) {
            Query query = new Query(Criteria.where("sesso").exists(false));
            lista = mongoTemplate.find(query, Bio.class, "bio");
        }// end of if cycle

    }// end of method


    @Test
    public void lastCharNazionalita() {
        List<Bio> listaFemmine = new ArrayList<>();
        String tag = "a";

        for (Bio bio : lista) {
//            if (getLastChar(bio.getAttivita()).equals(tag)) {
//                if (!listaFemmine.contains(bio)) {
//                    listaFemmine.add(bio);
//                }// end of if cycle
//            }// end of if cycle
//            if (getLastChar(bio.getAttivita2()).equals(tag)) {
//                if (!listaFemmine.contains(bio)) {
//                    listaFemmine.add(bio);
//                }// end of if cycle
//            }// end of if cycle
//            if (getLastChar(bio.getAttivita3()).equals(tag)) {
//                if (!listaFemmine.contains(bio)) {
//                    listaFemmine.add(bio);
//                }// end of if cycle
//            }// end of if cycle
            if (getLastChar(bio.getNazionalita()).equals(tag)) {
                if (!listaFemmine.contains(bio)) {
                    listaFemmine.add(bio);
                }// end of if cycle
            }// end of if cycle
        }// end of for cycle

        int a = 87;
    }// end of single test


    @Test
    public void listeSicure() {
        String nome;
        listaSenzaNome = new ArrayList<>();
        listaFemmine = new ArrayList<>();
        listaMaschi = new ArrayList<>();
        listaMista = new ArrayList<>();

        for (Bio bio : lista) {
            nome = bio.getNome();
            if (text.isEmpty(nome)) {
                listaSenzaNome.add(bio);
            } else {
                if (elencoNomiFemminili.contains(nome)) {
                    listaFemmine.add(bio);
                } else {
                    if (elencoNomiMaschili.contains(nome)) {
                        listaMaschi.add(bio);
                    } else {
                        listaMista.add(bio);
                    }// end of if/else cycle
                }// end of if/else cycle
            }// end of if/else cycle
        }// end of for cycle

        System.out.println("*************");
        System.out.println("listeSicure");
        System.out.println("*************");
        System.out.println("");
        System.out.println("Senza nome. Ci sono " + listaSenzaNome.size() + " biografie senza nome");
        for (Bio bio : listaSenzaNome) {
            System.out.println(bio.getWikiTitle());
        }// end of for cycle
        System.out.println("");
        System.out.println("Femmine. Ci sono " + listaFemmine.size() + " biografie sicuramente femminili");
        for (Bio bio : listaFemmine) {
            System.out.println(bio.getNome() + " della voce " + bio.getWikiTitle());
        }// end of for cycle
        System.out.println("");
        System.out.println("Maschi. Ci sono " + listaMaschi.size() + " biografie sicuramente maschili");
        for (Bio bio : listaMaschi) {
            System.out.println(bio.getNome() + " della voce " + bio.getWikiTitle());
        }// end of for cycle
        System.out.println("");
        System.out.println("Incerti. Ci sono " + listaMista.size() + " biografie incerte da controllare");
        for (Bio bio : listaMista) {
            System.out.println(bio.getNome() + " della voce " + bio.getWikiTitle());
        }// end of for cycle
        System.out.println("");

        int a = 87;
    }// end of single test


    private String getLastChar(Attivita attivita) {
        return attivita != null ? getLastChar(attivita.singolare) : "";
    }// end of method


    private String getLastChar(Nazionalita nazionalita) {
        return nazionalita != null ? getLastChar(nazionalita.singolare) : "";
    }// end of method


    private String getLastChar(String singolare) {
        String last = "";

        if (text.isValid(singolare)) {
            last = singolare.substring(singolare.length() - 1, singolare.length());
        }// end of if cycle

        return last;
    }// end of method

}// end of class
