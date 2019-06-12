package it.algos.vaadwiki.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadwiki.service.ABioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static it.algos.vaadflow.application.FlowCost.*;
import static it.algos.vaadwiki.didascalia.Didascalia.TAG_SEP;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Mon, 10-Jun-2019
 * Time: 19:50
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class ListaService extends ABioService {


    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Ogni chiave della mappa è una dei giorni/anni in cui suddividere la pagina <br>
     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
     *
     * @param giorno di riferimento per la lista
     *
     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
     */
    public LinkedHashMap<String, ArrayList<String>> getMappaGiornoNato(Giorno giorno) {
        LinkedHashMap<String, ArrayList<String>> mappa = null;
        ListaGiornoNato listaGiornoNato;

        listaGiornoNato = appContext.getBean(ListaGiornoNato.class, giorno);
        return listaGiornoNato.mappa;
    }// end of method


    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Ogni chiave della mappa è una dei giorni/anni in cui suddividere la pagina <br>
     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
     *
     * @param giorno di riferimento per la lista
     *
     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
     */
    public LinkedHashMap<String, ArrayList<String>> getMappaGiornoMorto(Giorno giorno) {
        LinkedHashMap<String, ArrayList<String>> mappa = null;
        ListaGiornoMorto listaGiornoMorto;

        listaGiornoMorto = appContext.getBean(ListaGiornoMorto.class, giorno);
        return listaGiornoMorto.mappa;
    }// end of method


    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Ogni chiave della mappa è una dei giorni/anni in cui suddividere la pagina <br>
     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
     *
     * @param anno di riferimento per la lista
     *
     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
     */
    public LinkedHashMap<String, ArrayList<String>> getMappaAnnoNato(Anno anno) {
        LinkedHashMap<String, ArrayList<String>> mappa = null;
        ListaAnnoNato listaAnnoNato;

        listaAnnoNato = appContext.getBean(ListaAnnoNato.class, anno);
        return listaAnnoNato.mappa;
    }// end of method

    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Ogni chiave della mappa è una dei giorni/anni in cui suddividere la pagina <br>
     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
     *
     * @param anno di riferimento per la lista
     *
     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
     */
    public LinkedHashMap<String, ArrayList<String>> getMappaAnnoMorto(Anno anno) {
        LinkedHashMap<String, ArrayList<String>> mappa = null;
        ListaAnnoMorto listaAnnoMorto;

        listaAnnoMorto = appContext.getBean(ListaAnnoMorto.class, anno);
        return listaAnnoMorto.mappa;
    }// end of method


    /**
     * Nessun raggruppamento
     */
    public String righeSemplici(LinkedHashMap<String, ArrayList<String>> mappaDidascalie) {
        String testo = VUOTA;
        ArrayList<String> listaDidascalie;

        if (mappaDidascalie != null) {
            for (String key : mappaDidascalie.keySet()) {
                listaDidascalie = mappaDidascalie.get(key);

                if (text.isValid(key)) {
                    for (String didascalia : listaDidascalie) {
                        testo += ASTERISCO + key + TAG_SEP + didascalia + A_CAPO;
                    }// end of for cycle
                } else {
                    for (String didascalia : listaDidascalie) {
                        testo += ASTERISCO + didascalia + A_CAPO;
                    }// end of for cycle
                }// end of if/else cycle

            }// end of for cycle
        }// end of if cycle

        return testo;
    }// fine del metodo


    public int getMappaSize(LinkedHashMap<String, ArrayList<String>> mappa) {
        int numVoci = 0;
        ArrayList<String> lista;

            for (Map.Entry<String, ArrayList<String>> elementoDellaMappa : mappa.entrySet()) {
                lista = (ArrayList) elementoDellaMappa.getValue();
                numVoci+=lista.size();
        }// end of for cycle

        return numVoci;
    }// end of method

}// end of class
