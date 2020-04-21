package it.algos.vaadwiki;

import it.algos.vaadflow.service.ATextService;
import it.algos.vaadflow.wrapper.WrapTreStringhe;
import it.algos.vaadwiki.enumeration.EAGraffe;
import it.algos.vaadwiki.service.ABioService;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 21-apr-2020
 * Time: 16:09
 */
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Test per la libreria LibBio")
public class ABioServiceTest extends ATest {


    @InjectMocks
    public ABioService service;

    @InjectMocks
    public ATextService text;

    private HashMap<String, Object> mappa;


    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        MockitoAnnotations.initMocks(service);
        assertNotNull(service);
        MockitoAnnotations.initMocks(text);
        assertNotNull(text);
        service.text = text;
    }// end of method


    /**
     * Numero di occorrenze di un tag in un testo. <br>
     * Il tag non viene trimmato ed è sensibile agli spazi prima e dopo
     * NON si usano le regex <br>
     *
     * @param testoDaSpazzolare di riferimento
     * @param tag               da cercare
     *
     * @return numero di occorrenze - zero se non ce ne sono
     */
    @Test
    public void getNumTag() {
        contenuto = "Quante questo doppie{{ forse /ci sono} in {questo /testo";

        sorgente = VUOTA;
        previstoIntero = 0;
        ottenutoIntero = service.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "forse";
        previstoIntero = 1;
        ottenutoIntero = service.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "questo";
        previstoIntero = 2;
        ottenutoIntero = service.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "/";
        previstoIntero = 2;
        ottenutoIntero = service.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "}";
        previstoIntero = 1;
        ottenutoIntero = service.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "{";
        previstoIntero = 3;
        ottenutoIntero = service.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "{{";
        previstoIntero = 1;
        ottenutoIntero = service.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = GRAFFA_INI;
        previstoIntero = 3;
        ottenutoIntero = service.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = DOPPIE_GRAFFE_INI;
        previstoIntero = 1;
        ottenutoIntero = service.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = GRAFFA_END;
        previstoIntero = 1;
        ottenutoIntero = service.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = GRAFFA_END_REGEX;
        previstoIntero = 0;
        ottenutoIntero = service.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = GRAFFA_INI_REGEX;
        previstoIntero = 0;
        ottenutoIntero = service.getNumTag(contenuto, sorgente);
        assertEquals(previstoIntero, ottenutoIntero);
    }// end of single test


    /**
     * Restituisce il numero di occorrenze di una coppia di graffe iniziali nel testo. <br>
     * Le graffe possono essere poszionate in qualsiasi punto del testo <br>
     *
     * @param testoDaSpazzolare di riferimento
     *
     * @return numero di occorrenze - zero se non ce ne sono
     */
    @Test
    public void getNumGraffeIni() {
        sorgente = "Quante {questo doppie forse /ci sono} in {questo /testo";
        previstoIntero = 0;
        ottenutoIntero = service.getNumGraffeIni(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "Quante {{questo doppie forse /ci sono} in {questo /testo";
        previstoIntero = 1;
        ottenutoIntero = service.getNumGraffeIni(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "Quante {{questo doppie forse /ci sono{} in {questo /testo";
        previstoIntero = 1;
        ottenutoIntero = service.getNumGraffeIni(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "Quante {{questo doppie forse /ci sono{{adesso}} in {questo /testo";
        previstoIntero = 2;
        ottenutoIntero = service.getNumGraffeIni(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);
    }// end of single test


    /**
     * Restituisce il numero di occorrenze di una coppia di graffe di chiusura nel testo. <br>
     * Le graffe possono essere poszionate in qualsiasi punto del testo <br>
     *
     * @param testoDaSpazzolare di riferimento
     *
     * @return numero di occorrenze - zero se non ce ne sono
     */
    @Test
    public void getNumGraffeEnd() {
        sorgente = "Quante {questo doppie forse /ci sono} in {questo /testo";
        previstoIntero = 0;
        ottenutoIntero = service.getNumGraffeEnd(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "Quante {{questo doppie forse /ci sono}} in {questo} /testo";
        previstoIntero = 1;
        ottenutoIntero = service.getNumGraffeEnd(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "Quante {{questo doppie forse /ci sono{} in {questo /testo";
        previstoIntero = 0;
        ottenutoIntero = service.getNumGraffeEnd(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);

        sorgente = "Quante questo}} doppie forse /ci sono{{adesso}} in {questo /testo";
        previstoIntero = 2;
        ottenutoIntero = service.getNumGraffeEnd(sorgente);
        assertEquals(previstoIntero, ottenutoIntero);
    }// end of single test


    /**
     * Controlla che le occorrenze del tag iniziale e di quello finale si pareggino all'interno del testo. <br>
     * Ordine ed annidamento NON considerato
     *
     * @param testoDaSpazzolare di riferimento
     * @param tagIni            tag iniziale
     * @param tagEnd            tag finale
     *
     * @return vero se il numero di tagIni è uguale al numero di tagEnd
     */
    @Test
    public void isPariTag() {
        String tagIni = VUOTA;
        String tagEnd = VUOTA;
        String prova = "xyz";
        String pipe = "|";

        sorgente = "Testo < xyz con | {{diverse}} possibilità}} ancora | da > sviluppare";

        ottenutoBooleano = service.isPariTag(VUOTA, tagIni, tagEnd);
        assertFalse(ottenutoBooleano);

        tagIni = prova;
        tagEnd = prova;
        ottenutoBooleano = service.isPariTag(sorgente, tagIni, tagEnd);
        assertFalse(ottenutoBooleano);

        tagIni = pipe;
        tagEnd = pipe;
        ottenutoBooleano = service.isPariTag(sorgente, tagIni, tagEnd);
        assertTrue(ottenutoBooleano);

        tagIni = DOPPIE_GRAFFE_INI;
        tagEnd = DOPPIE_GRAFFE_END;
        ottenutoBooleano = service.isPariTag(sorgente, tagIni, tagEnd);
        assertFalse(ottenutoBooleano);

        tagIni = "<";
        tagEnd = ">";
        ottenutoBooleano = service.isPariTag(sorgente, tagIni, tagEnd);
        assertTrue(ottenutoBooleano);

        sorgente = "Testo < xyz con | {{diverse}} possibilità}} {{ancora | da > sviluppare";
        tagIni = DOPPIE_GRAFFE_INI;
        tagEnd = DOPPIE_GRAFFE_END;
        ottenutoBooleano = service.isPariTag(sorgente, tagIni, tagEnd);
        assertTrue(ottenutoBooleano);
    }// end of single test


    /**
     * Controlla che le graffe di apertura 'pareggino' con le graffe di chiusura'. <br>
     * Ordine ed annidamento NON considerato <br>
     *
     * @param testoDaSpazzolare di riferimento
     *
     * @return vero se il numero di graffe di apertura è uguale al numero di graffe di chiusura
     */
    public void isPariGraffe() {
        sorgente = "Testo < xyz con | diverse possibilità ancora | da > sviluppare";
        ottenutoBooleano = service.isPariGraffe(sorgente);
        assertFalse(ottenutoBooleano);

        sorgente = "Testo < xyz con | {{diverse possibilità ancora | da > sviluppare";
        ottenutoBooleano = service.isPariGraffe(sorgente);
        assertFalse(ottenutoBooleano);

        sorgente = "Testo < xyz con | {diverse} possibilità ancora | da > sviluppare";
        ottenutoBooleano = service.isPariGraffe(sorgente);
        assertFalse(ottenutoBooleano);

        sorgente = "Testo < xyz con | {{diverse}} possibilità}} ancora | da > sviluppare";
        ottenutoBooleano = service.isPariGraffe(sorgente);
        assertFalse(ottenutoBooleano);

        sorgente = "Testo < xyz con | {{diverse}} possibilità}} {{ancora | da > sviluppare";
        ottenutoBooleano = service.isPariGraffe(sorgente);
        assertTrue(ottenutoBooleano);
    }// end of single test


    /**
     * Controlla le graffe interne al testo
     * <p>
     * Casi da controllare (all'interno delle graffe principali, già eliminate):
     * 1-......                         (manca)
     * 2-...{{..}}...                   (singola)
     * 3-...{{}}...                     (vuota)
     * 4-{{..}}...                      (iniziale)
     * 5-...{{..}}                      (terminale)
     * 6-...{{..{{...}}...}}...         (interna)
     * 7-...{{..}}...{{...}}...         (doppie)
     * 8-...{{..}}..{{..}}..{{...}}...  (tre o più)
     * 9-...{{..}}..|..{{..}}...        (due in punti diversi)
     * 10-...{{...|...}}...             (pipe interno)
     * 11-...{{...|...|...}}...         (doppio pipe)
     * 12-...{{..|...}}..|..{{..}}...   (due pipe in due graffe)
     * 13-...{{....                     (singola apertura)
     * 14-...}}....                     (singola chiusura)
     * <p>
     * Se una o più graffe esistono, restituisce:
     * <p>
     * keyMapGraffeEsistono = se esistono                                       (boolean)
     * keyMapGraffeType = type della/della graffe trovate                       (EAGraffe)
     * keyMapGraffeNumero = quante ce ne sono                                   (integer)
     * keyMapGraffeTestoPrecedente = testo che precede la prima graffa o testo originale se non ce ne sono
     * keyMapGraffeListaWrapper = lista di WrapTreStringhe coi seguenti dati:   (list<WrapTreStringhe>)
     * - prima = valore del contenuto delle graffe                              (stringa)
     * - seconda = nome del parametro interessato                               (stringa)
     * - terza = valore completo del parametro che le contiene                  (stringa)
     *
     * @param testoDaSpazzolare di riferimento
     *
     * @return mappa col risultato
     */
    @Test
    public void checkGraffe() {
        List<WrapTreStringhe> listaWrap;
        sorgente = VUOTA;
        mappa = service.checkGraffe(sorgente);
        assertNotNull(mappa);
        assertEquals(5, mappa.size());
        assertFalse((boolean) mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        assertEquals(0, mappa.get(KEY_MAP_GRAFFE_NUMERO));
        assertEquals(EAGraffe.manca, mappa.get(KEY_MAP_GRAFFE_TYPE));

        sorgente = "Qui non ci sono graffe";
        mappa = service.checkGraffe(sorgente);
        assertNotNull(mappa);
        assertEquals(5, mappa.size());
        assertFalse((boolean) mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        assertEquals(0, mappa.get(KEY_MAP_GRAFFE_NUMERO));
        assertEquals(EAGraffe.manca, mappa.get(KEY_MAP_GRAFFE_TYPE));

        sorgente = "Qui {{esiste solo l'apertura di una graffa. Senza chiusura.";
        mappa = service.checkGraffe(sorgente);
        assertNotNull(mappa);
        assertEquals(5, mappa.size());
        assertFalse((boolean) mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        assertEquals(0, mappa.get(KEY_MAP_GRAFFE_NUMERO));
        assertEquals(EAGraffe.mezzaInizio, mappa.get(KEY_MAP_GRAFFE_TYPE));

        sorgente = "Qui {{esiste l'apertura di una {{graffa}}. Dispari.";
        mappa = service.checkGraffe(sorgente);
        assertNotNull(mappa);
        assertEquals(5, mappa.size());
        assertFalse((boolean) mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        assertEquals(0, mappa.get(KEY_MAP_GRAFFE_NUMERO));
        assertEquals(EAGraffe.mezzaInizio, mappa.get(KEY_MAP_GRAFFE_TYPE));

        sorgente = "Qui {{esiste}} l'apertura di una {{graffa. Dispari.";
        mappa = service.checkGraffe(sorgente);
        assertNotNull(mappa);
        assertEquals(5, mappa.size());
        assertFalse((boolean) mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        assertEquals(0, mappa.get(KEY_MAP_GRAFFE_NUMERO));
        assertEquals(EAGraffe.mezzaInizio, mappa.get(KEY_MAP_GRAFFE_TYPE));

        sorgente = "Qui esiste}} solo la chiusura di una graffa. Senza apertura.";
        mappa = service.checkGraffe(sorgente);
        assertNotNull(mappa);
        assertEquals(5, mappa.size());
        assertFalse((boolean) mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        assertEquals(0, mappa.get(KEY_MAP_GRAFFE_NUMERO));
        assertEquals(EAGraffe.mezzaFine, mappa.get(KEY_MAP_GRAFFE_TYPE));

        sorgente = "Qui {{esiste}} una graffa. In mezzo al testo.";
        mappa = service.checkGraffe(sorgente);
        assertNotNull(mappa);
        assertEquals(5, mappa.size());
        assertTrue((boolean) mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        assertEquals(1, mappa.get(KEY_MAP_GRAFFE_NUMERO));
        assertEquals(EAGraffe.singola, mappa.get(KEY_MAP_GRAFFE_TYPE));
        listaWrap = (List<WrapTreStringhe>) mappa.get(KEY_MAP_GRAFFE_LISTA_WRAPPER);
        ottenuto = array.isValid(listaWrap) ? listaWrap.get(0).getPrima() : VUOTA;
        assertEquals("esiste", ottenuto);

        sorgente = "Qui {{}} una graffa. In mezzo al testo.";
        mappa = service.checkGraffe(sorgente);
        assertNotNull(mappa);
        assertEquals(5, mappa.size());
        assertTrue((boolean) mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        assertEquals(1, mappa.get(KEY_MAP_GRAFFE_NUMERO));
        assertEquals(EAGraffe.vuota, mappa.get(KEY_MAP_GRAFFE_TYPE));
        listaWrap = (List<WrapTreStringhe>) mappa.get(KEY_MAP_GRAFFE_LISTA_WRAPPER);
        ottenuto = array.isValid(listaWrap) ? listaWrap.get(0).getPrima() : VUOTA;
        assertEquals(VUOTA, ottenuto);

        sorgente = "{{Qui}} esiste una graffa. Inizio testo.";
        mappa = service.checkGraffe(sorgente);
        assertNotNull(mappa);
        assertEquals(5, mappa.size());
        assertTrue((boolean) mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        assertEquals(1, mappa.get(KEY_MAP_GRAFFE_NUMERO));
        assertEquals(EAGraffe.iniziale, mappa.get(KEY_MAP_GRAFFE_TYPE));
        listaWrap = (List<WrapTreStringhe>) mappa.get(KEY_MAP_GRAFFE_LISTA_WRAPPER);
        ottenuto = array.isValid(listaWrap) ? listaWrap.get(0).getPrima() : VUOTA;
        assertEquals("Qui", ottenuto);

        sorgente = "Qui esiste una graffa. Fine {{testo}}";
        mappa = service.checkGraffe(sorgente);
        assertNotNull(mappa);
        assertEquals(5, mappa.size());
        assertTrue((boolean) mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        assertEquals(1, mappa.get(KEY_MAP_GRAFFE_NUMERO));
        assertEquals(EAGraffe.finale, mappa.get(KEY_MAP_GRAFFE_TYPE));
        listaWrap = (List<WrapTreStringhe>) mappa.get(KEY_MAP_GRAFFE_LISTA_WRAPPER);
        ottenuto = array.isValid(listaWrap) ? listaWrap.get(0).getPrima() : VUOTA;
        assertEquals("testo", ottenuto);

        sorgente = "Qui {{esistono}} due graffe. Fine {{testo}}";
        mappa = service.checkGraffe(sorgente);
        assertNotNull(mappa);
        assertEquals(5, mappa.size());
        assertTrue((boolean) mappa.get(KEY_MAP_GRAFFE_ESISTONO));
        assertEquals(2, mappa.get(KEY_MAP_GRAFFE_NUMERO));
        assertEquals(EAGraffe.doppie, mappa.get(KEY_MAP_GRAFFE_TYPE));
        listaWrap = (List<WrapTreStringhe>) mappa.get(KEY_MAP_GRAFFE_LISTA_WRAPPER);
        ottenuto = array.isValid(listaWrap) ? listaWrap.get(0).getPrima() : VUOTA;
        assertEquals("esistono", ottenuto);

    }// end of single test

}// end of test class
