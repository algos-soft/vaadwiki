package it.algos.vaadwiki.integration;

import com.mongodb.client.DistinctIterable;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadwiki.ATest;
import it.algos.vaadwiki.enumeration.EADidascalia;
import it.algos.vaadwiki.liste.ListaNomi;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.vaadwiki.modules.nome.NomeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Sat, 01-Jun-2019
 * Time: 17:11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NomeServiceIntegrationTest extends ATest {

    private static String NOME_UNO = "Abdul";
    protected ListaNomi listaNome;

    @Autowired
    public MongoOperations mongoOp;

    @Autowired
    public MongoTemplate mongoTemplate;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected ADateService date;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private NomeService nomeService;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private BioService bioService;

    private int numEntities;

    @Autowired
    private ApplicationContext appContext;


    @Before
    public void setUp() {
        Assert.assertNotNull(appContext);
    }// end of method


    @Test
    public void nonFaNulla() {
    }// end of single test


    //    @Test
    public void check() {
        String tag = " ";
        int cont = 0;
        int soglia = 30;
        int numVoci = 0;
        Query query;
        List<String> nomiSpazio = new ArrayList<>();
        List<String> nomiSenzaSpazio = new ArrayList<>();
        List<String> nomiConMenoDi30Voci = new ArrayList<>();
        List<String> nonValidi = new ArrayList<>();

        DistinctIterable<String> listaNomiDistinti = mongoOp.getCollection("bio").distinct("nome", String.class);
        for (String nome : listaNomiDistinti) {
            cont++;

            if (text.isValid(nome)) {
                query = new Query();
                query.addCriteria(Criteria.where("nome").is(nome));
                numVoci = ((List) mongoOp.find(query, Bio.class)).size();

                if (numVoci >= soglia) {
                    if (nome.contains(tag)) {
                        nomiSpazio.add(nome);
                    } else {
                        nomiSenzaSpazio.add(nome);
                    }// end of if/else cycle
                } else {
                    nomiConMenoDi30Voci.add(nome);
                }// end of if/else cycle
            } else {
                nonValidi.add(nome);
            }// end of if/else cycle

        }// end of for cycle

        System.out.println("Ci sono " + nomiSenzaSpazio.size() + " nomi senza spazi su un totale di " + cont);
        System.out.println("Ci sono " + nomiSpazio.size() + " nomi con spazi vuoti su un totale di " + cont);
        System.out.println("Ci sono " + nonValidi.size() + " nomi non validi su un totale di " + cont);
        System.out.println("Ci sono " + nomiConMenoDi30Voci.size() + " nomi con meno di 30 biografie su un totale di " + cont);
        System.out.println("");
        System.out.println("********");
        System.out.println("Spazi vuoti");
        System.out.println("********");
        for (String stringa : nomiSpazio) {
            System.out.println(stringa);
        }// end of for cycle

    }// end of single test


    public void crea() {
        String message = "";
        long inizio = 0;
        List<String> nomi;
        int cont = 0;
        int numVoci = 0;
        numEntities = nomeService.count();
        List<String> listamemoria = new ArrayList();

        if (numEntities == 0) {
            nomeService.findOrCrea("gac");
        }// end of if cycle

        inizio = System.currentTimeMillis();
        DistinctIterable<String> listaNomiDistinti = mongoOp.getCollection("bio").distinct("nome", String.class);
        for (String nome : listaNomiDistinti) {
            cont++;

            Query query = new Query();
            query.addCriteria(Criteria.where("nome").is(nome));
            numVoci = ((List) mongoOp.find(query, Bio.class)).size();

            if (numVoci >= 10) {
                listamemoria.add(nome);
//                nomeService.findOrCrea(nome, numVoci);
            }// end of if cycle

            if (cont > 1000) {
                System.out.println("Nomi controllati: ");
                break;
            }// end of if cycle

        }// end of for cycle

        System.out.println("Tempo impiegato: " + date.deltaText(inizio));
    }// end of single test


    @Test
    public void cercaNome() {
//        listaNome = appContext.getBean(ListaNomi.class, NOME_UNO);
     List<Bio> listaBio=bioService.findAllByNomeBio(NOME_UNO);

        int a=87;
    }// end of single test

}// end of class
