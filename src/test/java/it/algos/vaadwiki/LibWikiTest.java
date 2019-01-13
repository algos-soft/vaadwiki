package it.algos.vaadwiki;


import it.algos.wiki.LibWiki;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 07-dic-2017
 * Time: 14:23
 */
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("libwiki")
@DisplayName("Test per la libreria LibWiki")
public class LibWikiTest extends ATest {


    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }// end of method


    /**
     * Estrae il testo di un template BIO dal testo completo della voce
     * Esamina il PRIMO template che trova (ce ne dovrebbe essere solo uno)
     * Gli estremi sono COMPRESI
     * <p>
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag finale di chiusura con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     */
    @Test
    public void estraeTmplBioCompresi() {
        sorgente = "Questa prova di {{bio forse funziona}}, ma non è detto";
        previsto = "{{bio forse funziona}}";

        ottenuto = LibWiki.estraeTmplBioCompresi(sorgente);
        assertEquals(previsto, ottenuto);

        sorgente = "Questa prova di {{bio\nforse funziona}}, ma non è detto";
        previsto = "{{bio\nforse funziona}}";
        ottenuto = LibWiki.estraeTmplBioCompresi(sorgente);
        assertEquals(previsto, ottenuto);

        sorgente = "Questa prova di {{bio\tforse funziona}}, ma non è detto";
        previsto = "{{bio\tforse funziona}}";
        ottenuto = LibWiki.estraeTmplBioCompresi(sorgente);
        assertEquals(previsto, ottenuto);

        sorgente = "Questa prova di {{bio\rforse funziona}}, ma non è detto";
        previsto = "{{bio\rforse funziona}}";
        ottenuto = LibWiki.estraeTmplBioCompresi(sorgente);
        assertEquals(previsto, ottenuto);

        sorgente = "Questa prova di {{biox forse funziona}}, ma non è detto";
        previsto = "";
        ottenuto = LibWiki.estraeTmplBioCompresi(sorgente);
        assertEquals(previsto, ottenuto);

        sorgente = "Questa prova di {{biografia forse funziona}}, ma non è detto";
        previsto = "{{biografia forse funziona}}";
        ottenuto = LibWiki.estraeTmplBioCompresi(sorgente);
        assertEquals(previsto, ottenuto);
    }// end of single test

}// end of class
