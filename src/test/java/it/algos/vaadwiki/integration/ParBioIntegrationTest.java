package it.algos.vaadwiki.integration;

import it.algos.vaadwiki.ATest;
import it.algos.vaadwiki.service.LibBio;
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
 * Date: ven, 31-gen-2020
 * Time: 09:38
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ParBioIntegrationTest extends ATest {


    @Autowired
    protected LibBio libBio;


    @Before
    public void setUpIniziale() {
        Assert.assertNotNull(text);
        Assert.assertNotNull(libBio);
    }// end of method


    /**
     * ELIMINA gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Restituisce un valore GREZZO che deve essere ancora elaborato <br>
     * Eventuali parti terminali inutili vengono scartate ma devono essere conservate a parte per il template <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     *
     * @param valoreOriginarioDelServer in entrata da elaborare
     *
     * @return valore grezzo troncato del parametro
     */
    @Test
    public void troncaParteFinale() {
        ParBio parBio;
        String testoOriginale;
        Object[] nome = {ParBio.nome, "Crystle Danae?", "Crystle Danae"};
        Object[] nomei = {ParBio.nome, "?", ""};
        Object[] cognome = {ParBio.cognome, "[[Stewart]]", "[[Stewart]]"};
        Object[] cognomei = {ParBio.cognome, "?", ""};
        Object[] sesso = {ParBio.sesso, "m", "m"};
        Object[] luogoNascita = {ParBio.luogoNascita, "[Wilmington]", "[Wilmington]"};
        Object[] luogoNascitai = {ParBio.luogoNascita, "?", "?"};
        Object[] luogoNascita2 = {ParBio.luogoNascita, "[Wilmington]?", "[Wilmington]"};
        Object[] giornoMeseNascita = {ParBio.giornoMeseNascita, "1 Settembre", "1 Settembre"};
        Object[] giornoMeseNascitax = {ParBio.giornoMeseNascita, "1 Brumaio", "1 Brumaio"};
        Object[] annoNascita = {ParBio.annoNascita, "[[1981]]{{forse}}", "[[1981]]"};
        Object[] annoNascitai = {ParBio.annoNascita, "?", "?"};
        Object[] annoNascita2 = {ParBio.annoNascita, "[[1981]]?", "[[1981]]"};
        Object[] luogoMorte = {ParBio.luogoMorte, "?", "?"};
        Object[] annoMorte = {ParBio.annoMorte, "?", "?"};
        Object[] annoMortex = {ParBio.annoMorte, "[[2345]]", "[[2345]]"};
        Object[] attivita = {ParBio.attivita, "modella<ref>Dal 2000</ref>", "modella"};
        Object[] attivitai = {ParBio.attivita, "?", ""};
        Object[] attivita2 = {ParBio.attivita2, "Pittore<ref>Dal 2000</ref>", "Pittore"};
        Object[] attivita3 = {ParBio.attivita2, "Pittore ?", "Pittore"};
        Object[] attivitax = {ParBio.attivita2, "Paninaro<ref>Dal 2000</ref>", "Paninaro"};
        Object[] nazionalita = {ParBio.nazionalita, "Statunitense ?", "Statunitense"};
        Object[] nazionalitai = {ParBio.nazionalita, "?", ""};
        Object[] nazionalitax = {ParBio.nazionalita, "Sarmatese{{forse}}", "Sarmatese"};

        List<Object[]> lista = new ArrayList<>();
        lista.add(nome);
        lista.add(nomei);
        lista.add(cognome);
        lista.add(sesso);
        lista.add(cognomei);
        lista.add(luogoNascita);
        lista.add(luogoNascitai);
        lista.add(luogoNascita2);
        lista.add(giornoMeseNascita);
        lista.add(giornoMeseNascitax);
        lista.add(annoNascita);
        lista.add(annoNascitai);
        lista.add(annoNascita2);
        lista.add(luogoMorte);
        lista.add(annoMorte);
        lista.add(annoMortex);
        lista.add(attivita);
        lista.add(attivitai);
        lista.add(attivita2);
        lista.add(attivita3);
        lista.add(attivitax);
        lista.add(nazionalita);
        lista.add(nazionalitai);
        lista.add(nazionalitax);

        for (Object[] riga : lista) {
            parBio = (ParBio) riga[0];
            testoOriginale = (String) riga[1];
            previsto = (String) riga[2];
            ottenuto = parBio.troncaParteFinale(testoOriginale);

            Assert.assertEquals(previsto, ottenuto);
            System.out.println("Parametro " + parBio.getTag().toLowerCase() + " troncato correttamente. Valore grezzo: " + ottenuto);

        }// end of for cycle
    }// end of single test


    /**
     * Restituisce un valore valido del parametro <br>
     * ELIMINA gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
     *
     * @param valoreOriginarioDelServer in entrata da elaborare
     *
     * @return valore finale valido del parametro
     */
    @Test
    public void estraeParteValida() {
        ParBio parBio;
        String testoOriginale;
        Object[] nome = {ParBio.nome, "Crystle Danae?", "Crystle Danae"};
        Object[] nomei = {ParBio.nome, "?", ""};
        Object[] cognome = {ParBio.cognome, "[[Stewart]]", "Stewart"};
        Object[] cognomei = {ParBio.cognome, "?", ""};
        Object[] sesso = {ParBio.sesso, "m", "M"};
        Object[] luogoNascita = {ParBio.luogoNascita, "[Wilmington]", "Wilmington"};
        Object[] luogoNascitai = {ParBio.luogoNascita, "?", "?"};
        Object[] luogoNascita2 = {ParBio.luogoNascita, "[Wilmington]?", "Wilmington"};
        Object[] giornoMeseNascita = {ParBio.giornoMeseNascita, "1 Settembre", "1º settembre"};
        Object[] giornoMeseNascitax = {ParBio.giornoMeseNascita, "1 Brumaio", "1º brumaio"};
        Object[] annoNascita = {ParBio.annoNascita, "[[1981]]{{forse}}", "1981"};
        Object[] annoNascitai = {ParBio.annoNascita, "?", "?"};
        Object[] annoNascita2 = {ParBio.annoNascita, "[[1981]]?", "1981"};
        Object[] luogoMorte = {ParBio.luogoMorte, "?", "?"};
        Object[] annoMorte = {ParBio.annoMorte, "?", "?"};
        Object[] annoMortex = {ParBio.annoMorte, "[[2345]]", "2345"};
        Object[] attivita = {ParBio.attivita, "modella<ref>Dal 2000</ref>", "modella"};
        Object[] attivitai = {ParBio.attivita, "?", ""};
        Object[] attivita2 = {ParBio.attivita2, "Pittore<ref>Dal 2000</ref>", "pittore"};
        Object[] attivita3 = {ParBio.attivita2, "Pittore ?", "pittore"};
        Object[] attivitax = {ParBio.attivita2, "Paninaro<ref>Dal 2000</ref>", "paninaro"};
        Object[] nazionalita = {ParBio.nazionalita, "Statunitense ?", "statunitense"};
        Object[] nazionalitai = {ParBio.nazionalita, "?", ""};
        Object[] nazionalitax = {ParBio.nazionalita, "Sarmatese{{forse}}", "sarmatese"};

        List<Object[]> lista = new ArrayList<>();
        lista.add(nome);
        lista.add(nomei);
        lista.add(cognome);
        lista.add(sesso);
        lista.add(cognomei);
        lista.add(luogoNascita);
        lista.add(luogoNascitai);
        lista.add(luogoNascita2);
        lista.add(giornoMeseNascita);
        lista.add(giornoMeseNascitax);
        lista.add(annoNascita);
        lista.add(annoNascitai);
        lista.add(annoNascita2);
        lista.add(luogoMorte);
        lista.add(annoMorte);
        lista.add(annoMortex);
        lista.add(attivita);
        lista.add(attivitai);
        lista.add(attivita2);
        lista.add(attivita3);
        lista.add(attivitax);
        lista.add(nazionalita);
        lista.add(nazionalitai);
        lista.add(nazionalitax);

        for (Object[] riga : lista) {
            parBio = (ParBio) riga[0];
            testoOriginale = (String) riga[1];
            previsto = (String) riga[2];
            ottenuto = parBio.estraeParteSignificativa(testoOriginale);

            Assert.assertEquals(previsto, ottenuto);
            System.out.println("Parametro " + parBio.getTag().toLowerCase() + " elaborato correttamente. Valore valido: " + ottenuto);

        }// end of for cycle
    }// end of single test


    /**
     * Restituisce un valore valido del parametro <br>
     * ELIMINA gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Eventuali parti terminali inutili vengono scartate ma devono essere conservate a parte per il template <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     * CONTROLLA la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
     * Se manca la corrispondenza, restituisce VUOTA <br>
     * <p>
     * La differenza con estraeParteValida() riguarda solo i parametri Giorno, Anno, Attivita, Nazionalita <br>
     *
     * @param valoreOriginarioDelServer in entrata da elaborare
     *
     * @return valore finale valido del parametro
     */
    @Test
    public void estraeParametroValido() {
        ParBio parBio;
        String testoOriginale;
        Object[] nome = {ParBio.nome, "Crystle Danae?", "Crystle Danae"};
        Object[] nomei = {ParBio.nome, "?", ""};
        Object[] cognome = {ParBio.cognome, "[[Stewart]]", "Stewart"};
        Object[] cognomei = {ParBio.cognome, "?", ""};
        Object[] sesso = {ParBio.sesso, "m", "M"};
        Object[] luogoNascita = {ParBio.luogoNascita, "[Wilmington]", "Wilmington"};
        Object[] luogoNascitai = {ParBio.luogoNascita, "?", "?"};
        Object[] luogoNascita2 = {ParBio.luogoNascita, "[Wilmington]?", "Wilmington"};
        Object[] giornoMeseNascita = {ParBio.giornoMeseNascita, "1 Settembre", "1º settembre"};
        Object[] giornoMeseNascitax = {ParBio.giornoMeseNascita, "1 Brumaio", ""};
        Object[] annoNascita = {ParBio.annoNascita, "[[1981]]{{forse}}", "1981"};
        Object[] annoNascitai = {ParBio.annoNascita, "?", "?"};
        Object[] annoNascita2 = {ParBio.annoNascita, "[[1981]]?", "1981"};
        Object[] luogoMorte = {ParBio.luogoMorte, "?", "?"};
        Object[] annoMorte = {ParBio.annoMorte, "?", "?"};
        Object[] annoMortex = {ParBio.annoMorte, "[[2345]]", ""};
        Object[] attivita = {ParBio.attivita, "modella<ref>Dal 2000</ref>", "modella"};
        Object[] attivitai = {ParBio.attivita, "?", ""};
        Object[] attivita2 = {ParBio.attivita2, "Pittore<ref>Dal 2000</ref>", "pittore"};
        Object[] attivita3 = {ParBio.attivita2, "Pittore ?", "pittore"};
        Object[] attivitax = {ParBio.attivita2, "Paninaro<ref>Dal 2000</ref>", ""};
        Object[] nazionalita = {ParBio.nazionalita, "Statunitense ?", "statunitense"};
        Object[] nazionalitai = {ParBio.nazionalita, "?", ""};
        Object[] nazionalitax = {ParBio.nazionalita, "Sarmatese{{forse}}", ""};

        List<Object[]> lista = new ArrayList<>();
        lista.add(nome);
        lista.add(nomei);
        lista.add(cognome);
        lista.add(sesso);
        lista.add(cognomei);
        lista.add(luogoNascita);
        lista.add(luogoNascitai);
        lista.add(luogoNascita2);
        lista.add(giornoMeseNascita);
        lista.add(giornoMeseNascitax);
        lista.add(annoNascita);
        lista.add(annoNascitai);
        lista.add(annoNascita2);
        lista.add(luogoMorte);
        lista.add(annoMorte);
        lista.add(annoMortex);
        lista.add(attivita);
        lista.add(attivitai);
        lista.add(attivita2);
        lista.add(attivita3);
        lista.add(attivitax);
        lista.add(nazionalita);
        lista.add(nazionalitai);
        lista.add(nazionalitax);

        for (Object[] riga : lista) {
            parBio = (ParBio) riga[0];
            testoOriginale = (String) riga[1];
            previsto = (String) riga[2];
            ottenuto = parBio.estraeParametroValido(testoOriginale, libBio);

            Assert.assertEquals(previsto, ottenuto);
            System.out.println("Parametro " + parBio.getTag().toLowerCase() + " elaborato correttamente. Valore valido: " + ottenuto);

        }// end of for cycle
    }// end of single test


    /**
     * Restituisce un valore valido del parametro <br>
     * MANTIENE gli eventuali contenuti IN CODA che vengono reinseriti dopo aver elaborato il valore valido del parametro <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     * Usato per Upload sul server
     *
     * @param valoreOriginarioDelServer in entrata da elaborare
     *
     * @return valore finale valido completo del parametro
     */
    @Test
    public void sostituisceParteValida() {
        ParBio parBio;
        String testoOriginale;
        Object[] nome = {ParBio.nome, "Crystle Danae?", "Crystle Danae"};
        Object[] nomei = {ParBio.nome, "?", ""};
        Object[] cognome = {ParBio.cognome, "[[Stewart]]", "Stewart"};
        Object[] cognomei = {ParBio.cognome, "?", ""};
        Object[] sesso = {ParBio.sesso, "m?", "M"};
        Object[] luogoNascita = {ParBio.luogoNascita, "[Wilmington]", "Wilmington"};
        Object[] luogoNascitai = {ParBio.luogoNascita, "?", "?"};
        Object[] luogoNascita2 = {ParBio.luogoNascita, "[Wilmington]?", "Wilmington"};
        Object[] giornoMeseNascita = {ParBio.giornoMeseNascita, "1 Settembre", "1º settembre"};
        Object[] annoNascita = {ParBio.annoNascita, "[[1981]]{{forse}}", "1981{{forse}}"};
        Object[] annoNascitai = {ParBio.annoNascita, "?", "?"};
        Object[] annoNascita2 = {ParBio.annoNascita, "[[1981]]?", "1981"};
        Object[] luogoMorte = {ParBio.luogoMorte, "?", "?"};
        Object[] annoMorte = {ParBio.annoMorte, "?", "?"};
        Object[] attivita = {ParBio.attivita, "Modella<ref>Dal 2000</ref>", "modella<ref>Dal 2000</ref>"};
        Object[] attivitai = {ParBio.attivita, "?", ""};
        Object[] attivita2 = {ParBio.attivita2, "Pittore<ref>Dal 2000</ref>", "pittore<ref>Dal 2000</ref>"};
        Object[] attivita3 = {ParBio.attivita2, "Pittore ?", "pittore"};
        Object[] nazionalita = {ParBio.nazionalita, "Statunitense ?", "statunitense"};
        Object[] nazionalitai = {ParBio.nazionalita, "?", ""};

        List<Object[]> lista = new ArrayList<>();
        lista.add(nome);
        lista.add(nomei);
        lista.add(cognome);
        lista.add(cognomei);
        lista.add(sesso);
        lista.add(luogoNascita);
        lista.add(luogoNascitai);
        lista.add(luogoNascita2);
        lista.add(giornoMeseNascita);
        lista.add(annoNascita);
        lista.add(annoNascitai);
        lista.add(annoNascita2);
        lista.add(luogoMorte);
        lista.add(annoMorte);
        lista.add(attivita);
        lista.add(attivitai);
        lista.add(attivita2);
        lista.add(attivita3);
        lista.add(nazionalita);
        lista.add(nazionalitai);

        for (Object[] riga : lista) {
            parBio = (ParBio) riga[0];
            testoOriginale = (String) riga[1];
            previsto = (String) riga[2];
            ottenuto = parBio.sostituisceParteValida(testoOriginale);
            Assert.assertEquals(previsto, ottenuto);
            System.out.println("Parametro " + parBio.getTag().toLowerCase() + " elaborato correttamente. Valore spedito sul server: " + ottenuto);
        }// end of for cycle
    }// end of single test


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
//    @Test
    public void fixGiornoValido() {
        //--senza spazio
        previsto = "12 ottobre";
        sorgente = "12ottobre";
        ottenuto = LibBio.fixGiornoValido(sorgente);
        Assert.assertEquals(ottenuto, previsto);

        //--triplo spazio
        sorgente = "12   ottobre";
        ottenuto = LibBio.fixGiornoValido(sorgente);
        Assert.assertEquals(ottenuto, previsto);

        //--doppio spazio
        sorgente = "12  ottobre";
        ottenuto = LibBio.fixGiornoValido(sorgente);
        Assert.assertEquals(ottenuto, previsto);

        //--spazio prima
        sorgente = " 12 ottobre";
        ottenuto = LibBio.fixGiornoValido(sorgente);
        Assert.assertEquals(ottenuto, previsto);

        //--spazio dopo
        sorgente = "12 ottobre ";
        ottenuto = LibBio.fixGiornoValido(sorgente);
        Assert.assertEquals(ottenuto, previsto);

        //--maiuscola
        sorgente = "12 Ottobre ";
        ottenuto = LibBio.fixGiornoValido(sorgente);
        Assert.assertEquals(ottenuto, previsto);

        previsto = "1º marzo";

        //--maiuscola
        sorgente = "1º Marzo";
        ottenuto = LibBio.fixGiornoValido(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        //--grado e non ordinale
        sorgente = "1° Marzo";
        ottenuto = LibBio.fixGiornoValido(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        //--grado e non ordinale
        previsto = "1º aprile";
        sorgente = "1° aprile";
        ottenuto = LibBio.fixGiornoValido(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        //--grado e non ordinale
        previsto = "1º ottobre";
        sorgente = "1° Ottobre";
        ottenuto = LibBio.fixGiornoValido(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        //--grado e non ordinale
        previsto = "1º luglio";
        sorgente = "1° luglio";
        ottenuto = LibBio.fixGiornoValido(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        //--numero e non ordinale
        previsto = "1º luglio";
        sorgente = "1 luglio";
        ottenuto = LibBio.fixGiornoValido(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        //--numero e non ordinale
        previsto = "1º ottobre";
        sorgente = "1 Ottobre";
        ottenuto = LibBio.fixGiornoValido(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        //--numero e non ordinale
        previsto = "1º aprile";
        sorgente = "1 aprile";
        ottenuto = LibBio.fixGiornoValido(sorgente);
        Assert.assertEquals(previsto, ottenuto);
    }// end of single test


    @Test
    public void separaMese() {
        previsto = "8 settembre";

        sorgente = "8settembre";
        ottenuto = LibBio.separaMese(sorgente);
        Assert.assertEquals(previsto, ottenuto);
        ottenuto = LibBio.fixGiornoValido(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        sorgente = " 8settembre";
        ottenuto = LibBio.separaMese(sorgente);
        Assert.assertEquals(previsto, ottenuto);
        ottenuto = LibBio.fixGiornoValido(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        previsto = "12 aprile";
        sorgente = "12aprile";
        ottenuto = LibBio.separaMese(sorgente);
        Assert.assertEquals(previsto, ottenuto);
        ottenuto = LibBio.fixGiornoValido(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        sorgente = "12 aprile";
        ottenuto = LibBio.separaMese(sorgente);
        Assert.assertEquals(previsto, ottenuto);
        ottenuto = LibBio.fixGiornoValido(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        previsto = "27 ottobre";

        sorgente = "27ottobre";
        ottenuto = LibBio.separaMese(sorgente);
        Assert.assertEquals(previsto, ottenuto);
        ottenuto = LibBio.fixGiornoValido(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        sorgente = "27-ottobre";
        ottenuto = LibBio.separaMese(sorgente);
        Assert.assertEquals(previsto, ottenuto);
        ottenuto = LibBio.fixGiornoValido(sorgente);
        Assert.assertEquals(previsto, ottenuto);

        sorgente = "27/ottobre";
        ottenuto = LibBio.separaMese(sorgente);
        Assert.assertEquals(previsto, ottenuto);
        ottenuto = LibBio.fixGiornoValido(sorgente);
        Assert.assertEquals(previsto, ottenuto);
    }// end of single test


//    /**
//     * Regola questa property <br>
//     * <p>
//     * Elimina il testo successivo a varii tag (fixPropertyBase) <br>
//     * Elimina il testo se NON contiene una spazio vuoto (tipico della data giorno-mese) <br>
//     * Elimina eventuali TRIPLI spazi vuoti (tipico della data tra il giorno ed il mese) <br>
//     * Elimina eventuali DOPPI spazi vuoti (tipico della data tra il giorno ed il mese) <br>
//     * Forza a minuscolo il primo carattere del mese <br>
//     * Forza a ordinale un eventuale primo giorno del mese scritto come numero o come grado <br>
//     * Controlla che il valore esista nella collezione Giorno <br>
//     *
//     * @param testoGrezzo in entrata da elaborare
//     *
//     * @return testoValido regolato in uscita
//     */
//    @Test
//    public void fixGiornoValido2() {
//        ParBio par = ParBio.giornoMeseNascita;
//
//        //--senza spazio
//        previsto = "12 ottobre";
//        sorgente = "12ottobre";
//        ottenuto = par.fix(sorgente, libBio);
//        Assert.assertEquals(ottenuto, previsto);
//
//        //--triplo spazio
//        sorgente = "12   ottobre";
//        ottenuto = par.fix(sorgente, libBio);
//        Assert.assertEquals(ottenuto, previsto);
//
//        //--doppio spazio
//        sorgente = "12  ottobre";
//        ottenuto = par.fix(sorgente, libBio);
//        Assert.assertEquals(ottenuto, previsto);
//
//        //--spazio prima
//        sorgente = " 12 ottobre";
//        ottenuto = par.fix(sorgente, libBio);
//        Assert.assertEquals(ottenuto, previsto);
//
//        //--spazio dopo
//        sorgente = "12 ottobre ";
//        ottenuto = par.fix(sorgente, libBio);
//        Assert.assertEquals(ottenuto, previsto);
//
//        //--maiuscola
//        sorgente = "12 Ottobre ";
//        ottenuto = par.fix(sorgente, libBio);
//        Assert.assertEquals(ottenuto, previsto);
//
//        previsto = "1º marzo";
//
//        //--maiuscola
//        sorgente = "1º Marzo";
//        ottenuto = par.fix(sorgente, libBio);
//        Assert.assertEquals(previsto, ottenuto);
//
//        //--grado e non ordinale
//        sorgente = "1° Marzo";
//        ottenuto = par.fix(sorgente, libBio);
//        Assert.assertEquals(previsto, ottenuto);
//
//        //--grado e non ordinale
//        previsto = "1º aprile";
//        sorgente = "1° aprile";
//        ottenuto = par.fix(sorgente, libBio);
//        Assert.assertEquals(previsto, ottenuto);
//
//        //--grado e non ordinale
//        previsto = "1º ottobre";
//        sorgente = "1° Ottobre";
//        ottenuto = par.fix(sorgente, libBio);
//        Assert.assertEquals(previsto, ottenuto);
//
//        //--grado e non ordinale
//        previsto = "1º luglio";
//        sorgente = "1° luglio";
//        ottenuto = par.fix(sorgente, libBio);
//        Assert.assertEquals(previsto, ottenuto);
//
//        //--numero e non ordinale
//        previsto = "1º luglio";
//        sorgente = "1 luglio";
//        ottenuto = par.fix(sorgente, libBio);
//        Assert.assertEquals(previsto, ottenuto);
//
//        //--numero e non ordinale
//        previsto = "1º ottobre";
//        sorgente = "1 Ottobre";
//        ottenuto = par.fix(sorgente, libBio);
//        Assert.assertEquals(previsto, ottenuto);
//
//        //--numero e non ordinale
//        previsto = "1º aprile";
//        sorgente = "1 aprile";
//        ottenuto = par.fix(sorgente, libBio);
//        Assert.assertEquals(previsto, ottenuto);
//    }// end of single test


}// end of class
