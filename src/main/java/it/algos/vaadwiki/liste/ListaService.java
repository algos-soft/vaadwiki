package it.algos.vaadwiki.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.cognome.Cognome;
import it.algos.vaadwiki.modules.nome.Nome;
import it.algos.vaadwiki.service.ABioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.*;

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
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Ogni chiave della mappa è una dei nomi/cognomi in cui suddividere la pagina <br>
     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br> //@todo Forse
     *
     * @param nome di riferimento per la lista
     *
     * @return mappa ordinata delle didascalie ordinate per nomi/cognomi (key) e poi per cognome (value)//@todo Forse
     */
    public LinkedHashMap<String, ArrayList<String>> getMappaNomi(Nome nome) {
        LinkedHashMap<String, ArrayList<String>> mappa = null;
        ListaNomi listaNomi;

        listaNomi = appContext.getBean(ListaNomi.class, nome);
        return listaNomi.mappa;
    }// end of method


    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Ogni chiave della mappa è una dei nomi/cognomi in cui suddividere la pagina <br>
     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br> //@todo Forse
     *
     * @param cognome di riferimento per la lista
     *
     * @return mappa ordinata delle didascalie ordinate per nomi/cognomi (key) e poi per cognome (value)//@todo Forse
     */
    public LinkedHashMap<String, ArrayList<String>> getMappaCognomi(Cognome cognome) {
        LinkedHashMap<String, ArrayList<String>> mappa = null;
        ListaCognomi listaCognomi;

        listaCognomi = appContext.getBean(ListaCognomi.class, cognome);
        return listaCognomi.mappa;
    }// end of method


    /**
     * Biografie con paragrafi
     */
    public String righeParagrafo(LinkedHashMap<String, ArrayList<String>> mappaDidascalie) {
        String testo = VUOTA;
        int numVociParagrafo;
        HashMap<String, Object> mappa;
        String titoloParagrafo;
        String titoloSottopagina;
        String paginaLinkata;
        String titoloVisibile;
        List<Bio> lista=null;

//        for (Map.Entry<String, HashMap> mappaTmp : mappaBio.entrySet()) {
//            testo += CostBio.A_CAPO;
//
//            mappa = mappaTmp.getValue();
//
//            if (usaOrdineAlfabeticoParagrafi) {
//                titoloParagrafo = (String) mappa.get(KEY_MAP_PARAGRAFO_TITOLO);
//            } else {
//                titoloParagrafo = (String) mappa.get(KEY_MAP_PARAGRAFO_LINK);
//            }// end of if/else cycle
//
//            titoloVisibile = (String) mappa.get(KEY_MAP_PARAGRAFO_TITOLO);
//            lista = (List<Bio>) mappa.get(KEY_MAP_LISTA);
//            numVociParagrafo = lista.size();
//
////            titoloParagrafo = costruisceTitolo(paginaLinkata, titoloVisibile);
//            if (Pref.getBool(CostBio.USA_NUMERI_PARAGRAFO, false)) {
//                testo += LibWiki.setParagrafo(titoloParagrafo, numVociParagrafo);
//            } else {
//                testo += LibWiki.setParagrafo(titoloParagrafo);
//            }// end of if/else cycle
//
//            testo += CostBio.A_CAPO;
//
//            if (usaSottopagine && numVociParagrafo > maxVociParagrafo) {
//                titoloSottopagina = titoloPagina + "/" + titoloVisibile;
//                testo += "{{Vedi anche|" + titoloSottopagina + "}}";
//                creaSottopagina(mappa);
//            } else {
//                for (Bio bio : lista) {
//                    testo += CostBio.ASTERISCO;
//                    testo += bio.getDidascaliaListe();
//                    testo += CostBio.A_CAPO;
//                }// end of for cycle
//            }// end of if/else cycle
//
//        }// end of for cycle

        return testo;
    }// fine del metodo

    /**
     * Raggruppa le biografie
     */
    public String righeRaggruppate(LinkedHashMap<String, ArrayList<String>> mappaDidascalie) {
        String testo = VUOTA;
        ArrayList<String> listaDidascalie;

        if (mappaDidascalie != null) {
            for (String key : mappaDidascalie.keySet()) {
                listaDidascalie = mappaDidascalie.get(key);

                if (listaDidascalie.size() == 1) {
                    testo += ASTERISCO + (text.isValid(key) ? key + TAG_SEP : SPAZIO) + listaDidascalie.get(0) + A_CAPO;
                } else {
                    if (text.isValid(key)) {
                        testo += ASTERISCO + key + A_CAPO;
                        for (String didascalia : listaDidascalie) {
                            testo += ASTERISCO + ASTERISCO + didascalia + A_CAPO;
                        }// end of if/else cycle
                    } else {
                        for (String didascalia : listaDidascalie) {
                            testo += ASTERISCO + didascalia + A_CAPO;
                        }// end of if/else cycle
                    }// end of if/else cycle
                }// end of for cycle
            }// end of for cycle
        }// end of if cycle

        return testo;
    }// fine del metodo


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
