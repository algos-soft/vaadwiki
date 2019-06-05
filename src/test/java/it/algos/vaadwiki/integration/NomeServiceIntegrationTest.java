package it.algos.vaadwiki.integration;

import com.mongodb.client.DistinctIterable;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadwiki.ATest;
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
    public void legge() {
        String message = "";
        long inizio = 0;
        List<String> nomi;
        int cont = 0;
        int numVoci = 0;
        numEntities = nomeService.count();

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

            if (numVoci>=10) {
                nomeService.findOrCrea(nome,numVoci);
            }// end of if cycle

            if (cont > 37000) {
                System.out.println("Nomi controllati: ");
                break;
            }// end of if cycle

        }// end of for cycle

        System.out.println("Tempo impiegato: " + date.deltaText(inizio));
    }// end of method

}// end of class
