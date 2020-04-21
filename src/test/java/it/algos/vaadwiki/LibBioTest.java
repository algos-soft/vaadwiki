package it.algos.vaadwiki;

import it.algos.vaadwiki.service.LibBio;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.LinkedHashMap;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 21-apr-2020
 * Time: 13:21
 */
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Test per la libreria LibBio")
public class LibBioTest extends ATest {


    private String wikiTitle = "Pietro Ponzo";

    private String textPage;


//    @BeforeAll
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        MockitoAnnotations.initMocks(libBio);
//    }// end of method


    /**
     * Controlla le graffe interne al testo
     * <p>
     * Casi da controllare (all'interno delle graffe principali, già eliminate):
     * 1-...{{..}}...                   (singola)
     * 2-...{{}}...                     (vuota)
     * 3-{{..}}...                      (iniziale)
     * 3-...{{..}}                      (terminale)
     * 4-...{{..{{...}}...}}...         (interna)
     * 5-...{{..}}...{{...}}...         (doppie)
     * 6-...{{..}}..{{..}}..{{...}}...  (tre o più)
     * 7-...{{..}}..|..{{..}}...        (due in punti diversi)
     * 8-...{{...|...}}...              (pipe interno)
     * 8-...{{...|...|...}}...          (doppio pipe)
     * 7-...{{..|...}}..|..{{..}}...    (due pipe in due graffe)
     * 9-...{{....                      (singola apertura)
     * 10-...}}....                     (singola chiusura)
     * <p>
     * Se una o più graffe esistono, restituisce:
     * <p>
     * keyMapGraffeEsistono = se esistono                                       (boolean)
     * keyMapGraffeNumero = quante ce ne sono                                   (integer)
     * keyMapGraffeTestoPrecedente = testo che precede la prima graffa o testo originale se non ce ne sono
     * keyMapGraffeListaWrapper = lista di WrapTreStringhe coi seguenti dati:   (list<WrapTreStringhe>)
     * - prima = valore del contenuto delle graffe                              (stringa)
     * - seconda = nome del parametro interessato                               (stringa)
     * - terza = valore completo del parametro che le contiene                  (stringa)
     *
     * @param testoTemplate da analizzare
     *
     * @return mappa col risultato
     */
    @Test
    public void checkGraffe() {
        LinkedHashMap mappa;

        sorgente = "mariolino senza {{ graffe";
        mappa = LibBio.checkGraffe(sorgente);
        printMappa(sorgente, mappa);


        sorgente = "mariolino con {{ graffe}}";
        mappa = LibBio.checkGraffe(sorgente);
        printMappa(sorgente, mappa);

        sorgente = "mariolino {{con}} doppie {{ graffe}}";
        mappa = LibBio.checkGraffe(sorgente);
        printMappa(sorgente, mappa);

    }// end of single test


    private void printMappa(String sorgente, LinkedHashMap mappa) {
        assertNotNull(mappa);

//        System.out.println(sorgente);
        System.out.println(VUOTA);

        System.out.println(KEY_MAP_GRAFFE_ESISTONO + " - " + mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        System.out.println(KEY_MAP_GRAFFE_NUMERO + " - " + mappa.get(KEY_MAP_GRAFFE_NUMERO));
        System.out.println(KEY_MAP_GRAFFE_TESTO_PRECEDENTE + " - " + mappa.get(KEY_MAP_GRAFFE_TESTO_PRECEDENTE));
        System.out.println(KEY_MAP_GRAFFE_VALORE_CONTENUTO + " - " + mappa.get(KEY_MAP_GRAFFE_VALORE_CONTENUTO));
        System.out.println(KEY_MAP_GRAFFE_NOME_PARAMETRO + " - " + mappa.get(KEY_MAP_GRAFFE_NOME_PARAMETRO));
        System.out.println(KEY_MAP_GRAFFE_VALORE_PARAMETRO + " - " + mappa.get(KEY_MAP_GRAFFE_VALORE_PARAMETRO));
    }// end of single test

}// end of class


