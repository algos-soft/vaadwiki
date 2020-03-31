package it.algos.vaadwiki.integration;

import it.algos.vaadwiki.ATest;
import it.algos.vaadwiki.modules.attivita.AttivitaService;
import it.algos.vaadwiki.service.ParBio;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 31-mar-2020
 * Time: 16:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AttivitaIntegrationTest extends ATest {

    @Autowired
    protected AttivitaService service;


    @Before
    public void setUpIniziale() {
        Assert.assertNotNull(service);
    }// end of method


    /**
     * Controlla l'esistenza di una Entity usando la query della property specifica (obbligatoria ed unica) <br>
     * Alcune attività ammettono il prefisso 'ex' oppure 'ex-' <br>
     * Nella collection su mongoDB esiste solo la versione 'ex', la 'ex-' è implicita ma valida <bR>
     *
     * @param singolare maschile e femminile (obbligatorio ed unico)
     *
     * @return true se trovata
     */
    @Test
    public void findByKeyUnica() {
        Object[] attivita = {"?", false};
        Object[] attivita2 = {"Pittore", true};
        Object[] attivita3 = {"pittore", true};
        Object[] attivita4 = {"", false};
        Object[] attivita5 = {"Pittore ?", false};
        Object[] attivita6 = {"Pittore<ref>Dal 2000</ref>", false};
        Object[] attivita7 = {"Pittore<ref>Dal 2000</ref>", false};
        Object[] attivita8 = {"calciatore", true};
        Object[] attivita9 = {"ex-calciatore", true};
        Object[] attivita10 = {"ex calciatore", true};
        Object[] attivita11 = {"ex - calciatore", false};
        Object[] attivita12 = {"ex-politico", false};
        Object[] attivita13 = {"ex politico", false};

        List<Object[]> lista = new ArrayList<>();
        lista.add(attivita);
        lista.add(attivita2);
        lista.add(attivita3);
        lista.add(attivita4);
        lista.add(attivita5);
        lista.add(attivita6);
        lista.add(attivita7);
        lista.add(attivita8);
        lista.add(attivita9);
        lista.add(attivita10);
        lista.add(attivita11);
        lista.add(attivita12);
        lista.add(attivita13);

        for (Object[] riga : lista) {
            sorgente = (String) riga[0];
            previstoBooleano = (Boolean) riga[1];
            ottenutoBooleano = service.isEsiste(sorgente);

            Assert.assertEquals(previstoBooleano, ottenutoBooleano);
            System.out.println("Parametro di attività: " + sorgente + " = " + ottenutoBooleano);
        }// end of for cycle

    }// end of single test

}// end of class
