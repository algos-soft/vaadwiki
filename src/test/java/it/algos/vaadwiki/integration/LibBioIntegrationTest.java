package it.algos.vaadwiki.integration;

import it.algos.vaadwiki.ATest;
import it.algos.vaadwiki.service.LibBio;
import it.algos.wiki.Api;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static it.algos.vaadflow.application.FlowCost.SPAZIO;
import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 31-gen-2020
 * Time: 09:38
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LibBioIntegrationTest extends ATest {


    @Autowired
    protected LibBio libBio;


    @Before
    public void setUpIniziale() {
        Assert.assertNotNull(text);
        Assert.assertNotNull(libBio);
    }// end of method


    /**
     * Regola questa property <br>
     * <p>
     * Elimina il testo successivo a varii tag (fixPropertyBase) <br>
     * Elimina il testo se NON contiene una spazio vuoto (tipico della data giorno-mese) <br>
     * Elimina eventuali TRIPLI spazi vuoti (tipico della data tra il giorno ed il mese) <br>
     * Elimina eventuali DOPPI spazi vuoti (tipico della data tra il giorno ed il mese) <br>
     * Forza a minuscolo il primo carattere del mese <br>
     * Forza a ordinale un eventuale primo giorno del mese scritto come numero o come grado <br>
     * Controlla che il valore esista nella collezione Giorno <br>
     *
     * @param testoGrezzo in entrata da elaborare
     *
     * @return testoValido regolato in uscita
     */
    @Test
    public void fixGiornoValido() {
        //--senza spazio
        previsto = "12 ottobre";
        sorgente = "12ottobre";
        ottenuto = libBio.fixGiornoValido(sorgente);
        Assert.assertEquals(ottenuto, previsto);

        //--triplo spazio
        sorgente = "12   ottobre";
        ottenuto = libBio.fixGiornoValido(sorgente);
        Assert.assertEquals(ottenuto, previsto);

        //--doppio spazio
        sorgente = "12  ottobre";
        ottenuto = libBio.fixGiornoValido(sorgente);
        Assert.assertEquals(ottenuto, previsto);

        //--spazio prima
        sorgente = " 12 ottobre";
        ottenuto = libBio.fixGiornoValido(sorgente);
        Assert.assertEquals(ottenuto, previsto);

        //--spazio dopo
        sorgente = "12 ottobre ";
        ottenuto = libBio.fixGiornoValido(sorgente);
        Assert.assertEquals(ottenuto, previsto);

        //--maiuscola
        sorgente = "12 Ottobre ";
        ottenuto = libBio.fixGiornoValido(sorgente);
        Assert.assertEquals(ottenuto, previsto);

        previsto = "1º marzo";

        //--maiuscola
        sorgente = "1º Marzo";
        ottenuto = libBio.fixGiornoValido(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        //--grado e non ordinale
        sorgente = "1° Marzo";
        ottenuto = libBio.fixGiornoValido(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        //--grado e non ordinale
        previsto = "1º aprile";
        sorgente = "1° aprile";
        ottenuto = libBio.fixGiornoValido(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        //--grado e non ordinale
        previsto = "1º ottobre";
        sorgente = "1° Ottobre";
        ottenuto = libBio.fixGiornoValido(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        //--grado e non ordinale
        previsto = "1º luglio";
        sorgente = "1° luglio";
        ottenuto = libBio.fixGiornoValido(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        //--numero e non ordinale
        previsto = "1º luglio";
        sorgente = "1 luglio";
        ottenuto = libBio.fixGiornoValido(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        //--numero e non ordinale
        previsto = "1º ottobre";
        sorgente = "1 Ottobre";
        ottenuto = libBio.fixGiornoValido(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        //--numero e non ordinale
        previsto = "1º aprile";
        sorgente = "1 aprile";
        ottenuto = libBio.fixGiornoValido(sorgente);
        Assert.assertEquals(previsto, ottenuto);
    }// end of single test


    @Test
    public void separaMese() {
        previsto = "8 settembre";

        sorgente = "8settembre";
        ottenuto = libBio.separaMese(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        sorgente = " 8settembre";
        ottenuto = libBio.separaMese(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        previsto = "12 aprile";
        sorgente = "12aprile";
        ottenuto = libBio.separaMese(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        sorgente = "12 aprile";
        ottenuto = libBio.separaMese(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        previsto = "27 ottobre";

        sorgente = "27ottobre";
        ottenuto = libBio.separaMese(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        sorgente = "27-ottobre";
        ottenuto = libBio.separaMese(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        sorgente = "27/ottobre";
        ottenuto = libBio.separaMese(sorgente);
        Assert.assertEquals(previsto, ottenuto);
    }// end of single test

}// end of class
