package it.algos.vaadwiki.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadwiki.application.WikiCost;
import it.algos.vaadwiki.didascalia.EADidascalia;
import it.algos.vaadwiki.didascalia.WrapDidascalia;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.cognome.Cognome;
import it.algos.vaadwiki.modules.genere.Genere;
import it.algos.vaadwiki.modules.genere.GenereService;
import it.algos.vaadwiki.modules.nome.Nome;
import it.algos.vaadwiki.modules.nome.NomeService;
import it.algos.vaadwiki.modules.professione.Professione;
import it.algos.vaadwiki.modules.professione.ProfessioneService;
import it.algos.vaadwiki.service.ABioService;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static it.algos.vaadflow.application.FlowCost.*;
import static it.algos.vaadwiki.application.WikiCost.*;
import static it.algos.vaadwiki.didascalia.Didascalia.TAG_SEP;
import static it.algos.wiki.LibWiki.PARAGRAFO;

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
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ProfessioneService professioneService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected GenereService genereService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected NomeService nomeService;


    /**
     * Costruisce una lista di didascalie (Wrap) che hanno una valore valido per la pagina specifica <br>
     * La lista NON è ordinata <br>
     *
     * @param listaGrezzaBio di persone che hanno una valore valido per la pagina specifica
     *
     * @return lista NON ORDINATA di didascalie (Wrap)
     */
    public ArrayList<WrapDidascalia> creaListaDidascalie(ArrayList<Bio> listaGrezzaBio, EADidascalia typeDidascalia) {
        ArrayList<WrapDidascalia> listaDidascalie = new ArrayList<WrapDidascalia>();
        WrapDidascalia wrap = null;

        for (Bio bio : listaGrezzaBio) {
            try { // prova ad eseguire il codice
                wrap = appContext.getBean(WrapDidascalia.class, bio, typeDidascalia);
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch

            if (wrap != null) {
                listaDidascalie.add(wrap);
            }// end of if cycle
        }// end of for cycle

        return listaDidascalie;
    }// fine del metodo


    /**
     * Ordina la lista di didascalie (Wrap) che hanno una valore valido per la pagina specifica <br>
     *
     * @param listaDisordinata di didascalie
     *
     * @return lista di didascalie (Wrap) ordinate per giorno/anno (key) e poi per cognome (value)
     */
    public ArrayList<WrapDidascalia> ordinaListaDidascalie(ArrayList<WrapDidascalia> listaDisordinata) {
        ArrayList<WrapDidascalia> listaOrdinata = listaDisordinata;
        if (listaDisordinata != null) {

            listaDisordinata.sort(new Comparator<WrapDidascalia>() {

                int w1Ord;

                int w2Ord;

                String w1ChiaveUno;

                String w2ChiaveUno;

                int resultOrdine;


                @Override
                public int compare(WrapDidascalia dida1, WrapDidascalia dida2) {
                    w1Ord = dida1.getOrdine();
                    w2Ord = dida2.getOrdine();
                    w1ChiaveUno = dida1.getChiave();
                    w2ChiaveUno = dida2.getChiave();

                    resultOrdine = text.compareInt(w1Ord, w2Ord);

                    if (resultOrdine == 0) {
                        return text.compareStr(w1ChiaveUno, w2ChiaveUno);
                    } else {
                        return resultOrdine;
                    }// end of if/else cycle

                }// end of method
            });//end of lambda expressions and anonymous inner class
        }// end of if cycle

        return listaOrdinata;
    }// fine del metodo


    /**
     * Ordina la lista di didascalie (Wrap) che hanno una valore valido per la pagina specifica <br>
     *
     * @param listaDisordinata di didascalie
     *
     * @return lista di didascalie (Wrap) ordinate per giorno/anno (key) e poi per cognome (value)
     */
    public ArrayList<WrapDidascalia> ordinaListaDidascalieNomi(ArrayList<WrapDidascalia> listaDisordinata) {
        ArrayList<WrapDidascalia> listaOrdinata = listaDisordinata;
        if (listaDisordinata != null) {

            listaDisordinata.sort(new Comparator<WrapDidascalia>() {

                String w1ChiaveUno;

                String w2ChiaveUno;


                @Override
                public int compare(WrapDidascalia dida1, WrapDidascalia dida2) {
                    w1ChiaveUno = dida1.getChiave();
                    w2ChiaveUno = dida2.getChiave();
                    return text.compareStr(w1ChiaveUno, w2ChiaveUno);
                }// end of method
            });//end of lambda expressions and anonymous inner class
        }// end of if cycle

        return listaOrdinata;
    }// fine del metodo


    /**
     * Ordina la lista di didascalie (Wrap) che hanno una valore valido per la pagina specifica <br>
     *
     * @param listaDisordinata di didascalie
     *
     * @return lista di didascalie (Wrap) ordinate per giorno/anno (key) e poi per cognome (value)
     */
    public ArrayList<WrapDidascalia> ordinaListaDidascalieCognomi(ArrayList<WrapDidascalia> listaDisordinata) {
        ArrayList<WrapDidascalia> listaOrdinata = new ArrayList<>();
        LinkedHashMap<String, ArrayList<WrapDidascalia>> mappa = new LinkedHashMap<>();
        ArrayList<String> listaKey = new ArrayList<>();
        ArrayList<String> listaKeyOrdinata = new ArrayList<>();
        String key = "";
        ArrayList<WrapDidascalia> listaWrap;

        if (listaDisordinata != null) {
            for (WrapDidascalia wrap : listaDisordinata) {
                key = wrap.chiaveUno;
                if (!listaKey.contains(key)) {
                    listaKey.add(key);
                }// end of if cycle

                if (mappa.containsKey(key)) {
                    listaWrap = mappa.get(key);
                } else {
                    listaWrap = new ArrayList<>();
                }// end of if/else cycle
                listaWrap.add(wrap);
                mappa.put(key, listaWrap);
            }// end of for cycle

            listaKeyOrdinata = array.sort(listaKey);
            for (String keyOrdinata : listaKeyOrdinata) {
                listaWrap = mappa.get(keyOrdinata);
                for (WrapDidascalia wrap : listaWrap) {
                    listaOrdinata.add(wrap);
                }// end of for cycle
            }// end of for cycle
        }// end of if cycle

        return listaOrdinata;
    }// fine del metodo


    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Ogni chiave della mappa è una dei giorni/anni in cui suddividere la pagina <br>
     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
     * Sovrascritto nella sottoclasse concreta <br>
     *
     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
     *
     * @listaOrdinata di didascalie (Wrap) ordinate per giorno/anno (key) e poi per cognome (value)
     */
    public LinkedHashMap<String, ArrayList<String>> creaMappa(ArrayList<WrapDidascalia> listaDisordinata) {
        LinkedHashMap<String, ArrayList<String>> mappa = new LinkedHashMap<>();
        ArrayList<String> lista = null;
        String chiave;
        String titoloParagrafo = "";

        for (WrapDidascalia wrap : listaDisordinata) {
            chiave = wrap.getChiave();
            titoloParagrafo = getTitoloParagrafo(wrap.bio, "");

            if (mappa.get(titoloParagrafo) == null) {
                lista = new ArrayList<String>();
                mappa.put(titoloParagrafo, lista);
            } else {
                lista = (ArrayList<String>) mappa.get(titoloParagrafo);
            }// end of if/else cycle
            lista.add(wrap.getTestoSenza()); //@todo rimettere

        }// end of for cycle

        return mappa;
    }// fine del metodo


    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Ogni chiave della mappa è una dei giorni/anni in cui suddividere la pagina <br>
     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
     * Sovrascritto nella sottoclasse concreta <br>
     *
     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
     *
     * @listaOrdinata di didascalie (Wrap) ordinate per giorno/anno (key) e poi per cognome (value)
     */
    public LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> creaMappaChiaveUno(ArrayList<WrapDidascalia> listaGrezza) {
        return creaMappaChiaveUno(listaGrezza, "");
    }// fine del metodo


    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Ogni chiave della mappa è una dei giorni/anni in cui suddividere la pagina <br>
     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
     * Sovrascritto nella sottoclasse concreta <br>
     *
     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
     *
     * @listaOrdinata di didascalie (Wrap) ordinate per giorno/anno (key) e poi per cognome (value)
     */
    public LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> creaMappaChiaveUno(ArrayList<WrapDidascalia> listaGrezza, String titoloParagrafoVuoto) {
        return creaMappaChiaveUno(listaGrezza, titoloParagrafoVuoto, true);
    }// fine del metodo


    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Ogni chiave della mappa è una dei giorni/anni in cui suddividere la pagina <br>
     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
     * Sovrascritto nella sottoclasse concreta <br>
     *
     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
     *
     * @listaOrdinata di didascalie (Wrap) ordinate per giorno/anno (key) e poi per cognome (value)
     */
    public LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> creaMappaChiaveUno(
            ArrayList<WrapDidascalia> listaGrezza,
            String paragrafoVuoto,
            boolean paragrafoVuotoInCoda) {
        LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> mappaGenerale = new LinkedHashMap<>();
        LinkedHashMap<String, ArrayList<WrapDidascalia>> mappaParagrafi = new LinkedHashMap<>();
        LinkedHashMap<String, ArrayList<String>> mappaChiaveDue;
        ArrayList<WrapDidascalia> listaChiaveDue = null;
        String chiaveUno;
        String titoloParagrafo;
        int size = 0;
//        String paragrafoVuoto = titoloParagrafoVuoto;

        for (WrapDidascalia wrap : listaGrezza) {
            chiaveUno = text.isValid(wrap.chiaveUno) ? wrap.chiaveUno : paragrafoVuoto;
            titoloParagrafo = getTitoloParagrafo(wrap.bio, paragrafoVuoto);

            if (mappaParagrafi.get(titoloParagrafo) == null) {
                listaChiaveDue = new ArrayList<WrapDidascalia>();
            } else {
                listaChiaveDue = (ArrayList<WrapDidascalia>) mappaParagrafi.get(titoloParagrafo);
            }// end of if/else cycle
            listaChiaveDue.add(wrap);
            mappaParagrafi.put(titoloParagrafo, listaChiaveDue);
        }// end of for cycle

        for (String key : mappaParagrafi.keySet()) {
            titoloParagrafo = key;
            size = 0;
            boolean cambia = false;
            mappaChiaveDue = creaMappaChiaveDue(mappaParagrafi.get(titoloParagrafo));

//            if (usaParagrafoSize) {
//                cambia = titoloParagrafo.equals(paragrafoVuoto);
//                size = mappaChiaveDue.get("").size();
//                titoloParagrafo += " <small><small>(" + size + ")</small></small>";
//                paragrafoVuoto = cambia ? titoloParagrafo : paragrafoVuoto;
//            }// end of if cycle
            mappaGenerale.put(titoloParagrafo, mappaChiaveDue);
        }// end of for cycle

        if (mappaGenerale.containsKey(paragrafoVuoto)) {
            mappaChiaveDue = mappaGenerale.get(paragrafoVuoto);
            mappaGenerale.remove(paragrafoVuoto);
            mappaGenerale.put(paragrafoVuoto, mappaChiaveDue);
        }// end of if cycle

        return mappaGenerale;
    }// fine del metodo


    public LinkedHashMap<String, ArrayList<String>> creaMappaChiaveDue(ArrayList<WrapDidascalia> listaDidascalieDellaChiaveUno) {
        LinkedHashMap<String, ArrayList<String>> mappa = new LinkedHashMap<>();
        ArrayList<String> lista = null;
        String chiaveDue;
        String chiaveTre;

        for (WrapDidascalia wrap : listaDidascalieDellaChiaveUno) {
            chiaveDue = wrap.chiaveDue;

            if (mappa.get(chiaveDue) == null) {
                lista = new ArrayList<String>();
            } else {
                lista = (ArrayList<String>) mappa.get(chiaveDue);
            }// end of if/else cycle
            lista.add(wrap.getTestoSenza());
            mappa.put(chiaveDue, lista);
        }// end of for cycle

        return mappa;
    }// fine del metodo

//    public void pippo(ArrayList<WrapDidascalia> listaDidascalie, EADidascalia typeDidascalia, String tagParagrafoNullo) {
//        LinkedHashMap<String, HashMap> mappaBio = new LinkedHashMap<String, HashMap>();
//        if (listaDidascalie != null && listaDidascalie.size() > 0) {
//            for (WrapDidascalia wrap : listaDidascalie) {
//                elaboraMappaSingola(mappaBio, wrap, tagParagrafoNullo);
//            }// end of if cycle
//        }// end of for cycle
//    }// fine del metodo


    /**
     * Costruisce una mappa di tutte le biografie della pagina, suddivisa in paragrafi
     * Sovrascritto
     */
    @SuppressWarnings("all")
    protected void elaboraMappaSingola(LinkedHashMap<String, HashMap> mappaBio, WrapDidascalia wrap, String tagParagrafoNullo) {
        String key = wrap.getChiave();
        String didascalia;
        ArrayList<Bio> lista;
        HashMap<String, Object> mappa;
        int voci;
        Bio bio = wrap.bio;

        if (mappaBio.containsKey(key)) {
            mappa = mappaBio.get(key);
            lista = (ArrayList<Bio>) mappa.get(KEY_MAP_LISTA);
            voci = (int) mappa.get(KEY_MAP_VOCI);
            lista.add(bio);
            mappa.put(KEY_MAP_VOCI, voci + 1);
        } else {
            mappa = new HashMap<>();
            lista = new ArrayList<>();
            lista.add(bio);
            mappa.put(KEY_MAP_PARAGRAFO_TITOLO, key);
            mappa.put(KEY_MAP_PARAGRAFO_LINK, getTitoloParagrafo(bio, tagParagrafoNullo));
            mappa.put(KEY_MAP_LISTA, lista);
            mappa.put(KEY_MAP_SESSO, bio.getSesso());
            mappa.put(KEY_MAP_VOCI, 1);

//            if (usaSortCronologico) {
//            if (text.isValid(bio.getGiornoNato())) {
//                mappa.put(KEY_MAP_ORDINE_GIORNO_NATO, bio.getGiornoNato().getOrdinamento());
//            }// end of if cycle
//            if (text.isValid(bio.getGiornoMorto())) {
//                mappa.put(KEY_MAP_ORDINE_GIORNO_MORTO, bio.getGiornoMorto().getOrdinamento());
//            }// end of if cycle
//            if (text.isValid(bio.getAnnoNato())) {
//                mappa.put(KEY_MAP_ORDINE_ANNO_NATO, bio.getAnnoNato().getOrdinamento());
//            }// end of if cycle
//            if (text.isValid(bio.getAnnoMorto())) {
//                mappa.put(KEY_MAP_ORDINE_ANNO_MORTO, bio.getAnnoMorto().getOrdinamento());
//            }// end of if cycle
//            }// end of if cycle

            mappaBio.put(key, mappa);
        }// end of if/else cycle
    }// fine del metodo


    /**
     * Costruisce il titolo del paragrafo
     * <p>
     * Questo deve essere composto da:
     * Professione.pagina
     * Genere.plurale
     */
    public String getTitoloParagrafo(Bio bio, String tagParagrafoNullo) {
        String titoloParagrafo = tagParagrafoNullo;
        Professione professione = null;
        String professioneTxt;
        String paginaWiki = VUOTA;
        Genere genere = null;
        String genereTxt = "";
        String linkVisibile = VUOTA;
        String attivitaSingolare = VUOTA;

        if (bio == null) {
            return VUOTA;
        }// end of if cycle
        if (bio.getAttivita() == null) {
            return titoloParagrafo;
        }// end of if cycle

        attivitaSingolare = bio.getAttivita().singolare;
        professione = professioneService.findByKeyUnica(attivitaSingolare);
        genere = genereService.findByKeyUnica(attivitaSingolare);

        if (professione != null) {
            professioneTxt = professione.getPagina();
        } else {
            professioneTxt = attivitaSingolare;
        }// end of if/else cycle
        if (!professioneTxt.equals(VUOTA)) {
            paginaWiki = text.primaMaiuscola(professioneTxt);
        }// end of if cycle

        if (genere != null) {
            if (bio.getSesso().equals("M")) {
                genereTxt = genere.getPluraleMaschile();
            } else {
                if (bio.getSesso().equals("F")) {
                    genereTxt = genere.getPluraleFemminile();
                } else {
                    //@todo errore
                }// end of if/else cycle
            }// end of if/else cycle

            if (text.isValid(genereTxt)) {
                linkVisibile = text.primaMaiuscola(genereTxt);
            }// end of if cycle
        }// end of if cycle

        if (text.isValid(paginaWiki) && text.isValid(linkVisibile)) {
            titoloParagrafo = costruisceTitolo(paginaWiki, linkVisibile, tagParagrafoNullo);
        }// end of if cycle

        return titoloParagrafo;
    }// fine del metodo


    /**
     * Costruisce il titolo
     * Controlla se il titolo visibile (link) non esiste già
     * Se esiste, sostituisce la pagina (prima parte del titolo) con quella già esistente
     */
    protected String costruisceTitolo(String paginaWiki, String linkVisibile, String tagParagrafoNullo) {
        String titoloParagrafo = LibWiki.setLink(paginaWiki, linkVisibile);
        String link;

        if (linkVisibile.equals(tagParagrafoNullo)) {
            return linkVisibile;
        }// end of if cycle

//        for (String keyCompleta : mappaBio.keySet()) {
//            link = keyCompleta.substring(keyCompleta.indexOf("|") + 1);
//            link = LibWiki.setNoQuadre(link);
//            if (link.equals(linkVisibile)) {
//                titoloParagrafo = keyCompleta;
//                break;
//            }// end of if cycle
//        }// end of for cycle

//        if (usaTitoloParagrafoConLink) {
//            titoloParagrafo = LibBio.fixLink(titoloParagrafo);
//        }// end of if/else cycle

        return titoloParagrafo;
    }// fine del metodo


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
        return listaGiornoNato.mappaSemplice;
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
    public LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> getMappaGiornoNatoNew(Giorno giorno, String titoloParagrafoVuoto, boolean paragrafoVuotoInCoda) {
        LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> mappa = null;
        ListaGiornoNato listaGiornoNato;

        listaGiornoNato = appContext.getBean(ListaGiornoNato.class, giorno, titoloParagrafoVuoto, paragrafoVuotoInCoda);
        return listaGiornoNato.mappaComplessa;
    }// end of method


    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Ogni chiave della mappa è una dei giorni in cui suddividere la pagina <br>
     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
     *
     * @param giorno di riferimento per la lista
     *
     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
     */
    public LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> getMappaGiornoMortoNew(Giorno giorno, String titoloParagrafoVuoto, boolean paragrafoVuotoInCoda) {
        LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> mappa = null;
        ListaGiornoMorto listaGiornoMorto;

        listaGiornoMorto = appContext.getBean(ListaGiornoMorto.class, giorno, titoloParagrafoVuoto, paragrafoVuotoInCoda);
        return listaGiornoMorto.mappaComplessa;
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
        return listaGiornoMorto.mappaSemplice;
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
        return listaAnnoNato.mappaSemplice;
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
        return listaAnnoMorto.mappaSemplice;
    }// end of method


    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la property specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Ogni chiave della mappa è una delle attività in cui suddividere la pagina <br>
     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
     *
     * @param nome di riferimento per la lista
     *
     * @return mappa ordinata delle didascalie ordinate per nomi (key) e poi per cognome (value)
     */
    @Deprecated
    public LinkedHashMap<String, ArrayList<String>> getMappaNomi(Nome nome) {
        LinkedHashMap<String, ArrayList<String>> mappa = null;
        ListaNomi listaNomi;

        listaNomi = appContext.getBean(ListaNomi.class, nome);
        return listaNomi.mappaSemplice;
    }// end of method


    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la property specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Ogni chiave della mappa è una delle attività in cui suddividere la pagina <br>
     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
     *
     * @param nomeText di riferimento per la lista
     *
     * @return mappa ordinata delle didascalie ordinate per nomi (key) e poi per cognome (value)
     */
    @Deprecated
    public LinkedHashMap<String, ArrayList<String>> getMappaNomi(String nomeText) {
        LinkedHashMap<String, ArrayList<String>> mappa = null;
        Nome nomeEntity = nomeService.findByKeyUnica(nomeText);

        if (nomeEntity != null) {
            mappa = getMappaNomi(nomeEntity);
        }// end of if cycle

        return mappa;
    }// end of method


    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la property specifica <br>
     * La mappa è composta da una chiave (ordinata) e da una LinkedHashMap <br>
     * Ogni chiave della mappa è una delle attività in cui suddividere la pagina <br>
     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
     *
     * @param nome di riferimento per la lista
     *
     * @return mappa ordinata delle didascalie ordinate per nomi (key) e poi per cognome (value)
     */
    public LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> getMappaNome(Nome nome) {
        LinkedHashMap<String, ArrayList<String>> mappa = null;
        ListaNomi listaNomi;

        listaNomi = appContext.getBean(ListaNomi.class, nome);
        return listaNomi.mappaComplessa;
    }// end of method


    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la property specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Ogni chiave della mappa è una delle attività in cui suddividere la pagina <br>
     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br> //@todo Forse
     *
     * @param nomeText di riferimento per la lista
     *
     * @return mappa ordinata delle didascalie ordinate per nomi (key) e poi per cognome (value)//@todo Forse
     */
    public LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> getMappaNome(String nomeText) {
        LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> mappa = null;
        Nome nomeEntity = nomeService.findByKeyUnica(nomeText);

        if (nomeEntity != null) {
            mappa = getMappaNome(nomeEntity);
        }// end of if cycle

        return mappa;
    }// end of method


    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la property specifica <br>
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
        return listaCognomi.mappaSemplice;
    }// end of method


    /**
     * Elabora la mappa di didascalie e costruisce il testo della pagina <br>
     */
    public String righeParagrafo(LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> mappaGenerale) {
        String testo = VUOTA;
        int numVociParagrafo;
//        HashMap<String, Object> mappa;
//        String titoloParagrafo;
//        String titoloSottopagina;
//        String paginaLinkata;
//        String titoloVisibile;
//        List<Bio> lista = null;
        LinkedHashMap<String, ArrayList<String>> mappaParagrafi = new LinkedHashMap<>();
//        LinkedHashMap<String, ArrayList<String>> mappaChiaveDue;
//        ArrayList<WrapDidascalia> listaChiaveDue = null;

        ArrayList<String> listaDidascalie = null;

        if (mappaGenerale != null) {
            for (String keyUno : mappaGenerale.keySet()) {
                testo += A_CAPO;
                testo += PARAGRAFO + keyUno + PARAGRAFO;

                mappaParagrafi = mappaGenerale.get(keyUno);

                if (mappaParagrafi != null) {
                    for (String keyDue : mappaParagrafi.keySet()) {
                        listaDidascalie = mappaParagrafi.get(keyDue);

                        if (array.isValid(listaDidascalie)) {
                            if (listaDidascalie.size() == 1) {
                                testo += A_CAPO;
                                testo += AST;
                                if (text.isValid(keyDue)) {
                                    testo += LibWiki.setQuadre(keyDue);
                                    testo += WikiCost.TAG_SEP;
                                }// end of if cycle
                                testo += listaDidascalie.get(0);
                            } else {
                                if (text.isValid(keyDue)) {
                                    testo += A_CAPO;
                                    testo += AST;
                                    testo += LibWiki.setQuadre(keyDue);
                                    for (String stringa : listaDidascalie) {
                                        testo += A_CAPO;
                                        testo += AST;
                                        testo += AST;
                                        testo += stringa;
                                    }// end of for cycle
                                } else {
                                    for (String stringa : listaDidascalie) {
                                        testo += A_CAPO;
                                        testo += AST;
                                        testo += stringa;
                                    }// end of for cycle
                                }// end of if/else cycle

                            }// end of if/else cycle

                        }// end of if cycle

                    }// end of for cycle

                }// end of if cycle
                testo += A_CAPO;
            }// end of for cycle
        }// end of if cycle


//        for (Map.Entry<String, HashMap> mappaTmp : mappaDidascalie.entrySet()) {
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
     * Righe suddivise per paragrafi <br>
     * Titolo del paragrafo con wikiLink <br>
     * Titolo del paragrafo con dimensione (secondo flag) <br>
     * All'interno dei paragrafi usa righeSemplici <br>
     */
    public String paragrafoAttivita(LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> mappa) {
        StringBuilder testo = new StringBuilder(VUOTA);
        LinkedHashMap<String, ArrayList<String>> mappaParagrafo;
        boolean usaParagrafoSize = true; //@todo eventualmente potrebbe essere passato come parametro
        int size = 0;
        String titoloParagrafo = "";

        if (mappa != null) {
            for (String keyUno : mappa.keySet()) {
                titoloParagrafo = keyUno;
                mappaParagrafo = mappa.get(keyUno);

                if (usaParagrafoSize) {
                    size = mappaParagrafo.get("").size();
                    titoloParagrafo += " <small><small>(" + size + ")</small></small>";
                }// end of if cycle

                testo.append(PARAGRAFO).append(titoloParagrafo).append(PARAGRAFO);
                testo.append(contenutoParagrafoRaggruppato(mappaParagrafo));
                testo.append(A_CAPO);
            }// end of for cycle
        }// end of if cycle

        return testo.toString();
    }// end of method


    /**
     * Righe suddivise per paragrafi <br>
     * All'interno dei paragrafi usa righeSemplici <br>
     */
    public String paragrafoSottopaginato(LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> mappa, String rinvio, String sottoTitolo, int soglia) {
        StringBuilder testo = new StringBuilder(VUOTA);
        LinkedHashMap<String, ArrayList<String>> mappaParagrafo;
        Object[] arraySet = null;
        int numSet = 0;
        int numVoci = 0;

        if (mappa != null) {
            for (String keyUno : mappa.keySet()) {
                testo.append(PARAGRAFO).append(keyUno).append(PARAGRAFO);
                mappaParagrafo = mappa.get(keyUno);

                //--controllo sottoparagrafo
                arraySet = mappaParagrafo.keySet().toArray();
                if (arraySet.length == 1) {
                    numVoci = mappaParagrafo.get(arraySet[0]).size();
                }// end of if cycle

                if (numVoci > soglia) {
                    testo.append(A_CAPO);
                    testo.append("{{Vedi anche|");
                    testo.append(rinvio);
                    testo.append("/");
                    testo.append(sottoTitolo);
                    testo.append("}}");
                    testo.append(A_CAPO);
                    //lancia sottopagina
                } else {
                    testo.append(contenutoParagrafoSemplice(mappaParagrafo));
                }// end of if/else cycle

                testo.append(A_CAPO);
            }// end of for cycle
        }// end of if cycle

        return testo.toString();
    }// end of method


    /**
     * Righe suddivise per paragrafi <br>
     * All'interno dei paragrafi usa righe righeRaggruppate <br>
     */
    public String paragrafoConRigheRaggruppate(LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> mappa) {
        StringBuilder testo = new StringBuilder(VUOTA);
        LinkedHashMap<String, ArrayList<String>> mappaParagrafo;
        boolean usaParagrafoSize = true; //@todo eventualmente potrebbe essere passato come parametro
        int size = 0;
        String titoloParagrafo = "";

        if (mappa != null) {
            for (String keyUno : mappa.keySet()) {
                titoloParagrafo = keyUno;
                titoloParagrafo = LibWiki.setQuadre(titoloParagrafo);
                mappaParagrafo = mappa.get(keyUno);

                if (usaParagrafoSize) {
                    size = mappaParagrafo.size();
                    titoloParagrafo += " <small><small>(" + size + ")</small></small>";
                }// end of if cycle

                testo.append(PARAGRAFO).append(titoloParagrafo).append(PARAGRAFO);
                testo.append(contenutoParagrafoRaggruppato(mappaParagrafo));
                testo.append(A_CAPO);
            }// end of for cycle
        }// end of if cycle

        return testo.toString();
    }// end of method


    /**
     * Righe suddivise per paragrafi <br>
     * All'interno dei paragrafi usa righeSemplici <br>
     */
    public String contenutoParagrafoSemplice(LinkedHashMap<String, ArrayList<String>> mappaParagrafo) {
        StringBuilder testo = new StringBuilder(VUOTA);
        ArrayList<String> listaDidascalie = null;

        if (mappaParagrafo != null) {
            for (String keyDue : mappaParagrafo.keySet()) {
                listaDidascalie = mappaParagrafo.get(keyDue);

                if (array.isValid(listaDidascalie)) {
                    if (listaDidascalie.size() == 1) {
                        testo.append(A_CAPO);
                        testo.append(AST);
                        if (text.isValid(keyDue)) {
                            testo.append(LibWiki.setQuadre(keyDue));
                            testo.append(WikiCost.TAG_SEP);
                        }// end of if cycle
                        testo.append(listaDidascalie.get(0));
                    } else {
                        if (text.isValid(keyDue)) {
                            testo.append(A_CAPO);
                            testo.append(AST);
                            testo.append(LibWiki.setQuadre(keyDue));
                            for (String stringa : listaDidascalie) {
                                testo.append(A_CAPO);
                                testo.append(AST);
                                testo.append(AST);
                                testo.append(stringa);
                            }// end of for cycle
                        } else {
                            for (String stringa : listaDidascalie) {
                                testo.append(A_CAPO);
                                testo.append(AST);
                                testo.append(stringa);
                            }// end of for cycle
                        }// end of if/else cycle

                    }// end of if/else cycle

                }// end of if cycle

            }// end of for cycle

        }// end of if cycle
        testo.append(A_CAPO);

        return testo.toString();
    }// end of method


    /**
     * Righe suddivise per paragrafi <br>
     * All'interno dei paragrafi usa righeRaggruppate <br>
     */
    public String contenutoParagrafoRaggruppato(LinkedHashMap<String, ArrayList<String>> mappaParagrafo) {
        StringBuilder testo = new StringBuilder(VUOTA);
        ArrayList<String> listaDidascalie = null;

        if (mappaParagrafo != null) {
            for (String keyDue : mappaParagrafo.keySet()) {
                listaDidascalie = mappaParagrafo.get(keyDue);

                if (array.isValid(listaDidascalie)) {
                    if (listaDidascalie.size() == 1) {
                        testo.append(A_CAPO);
                        testo.append(AST);
                        if (text.isValid(keyDue)) {
                            testo.append(LibWiki.setQuadre(keyDue));
                            testo.append(WikiCost.TAG_SEP);
                        }// end of if cycle
                        testo.append(listaDidascalie.get(0));
                    } else {
                        if (text.isValid(keyDue)) {
                            testo.append(A_CAPO);
                            testo.append(AST);
                            testo.append(LibWiki.setQuadre(keyDue));
                            for (String stringa : listaDidascalie) {
                                testo.append(A_CAPO);
                                testo.append(AST);
                                testo.append(AST);
                                testo.append(stringa);
                            }// end of for cycle
                        } else {
                            for (String stringa : listaDidascalie) {
                                testo.append(A_CAPO);
                                testo.append(AST);
                                testo.append(stringa);
                            }// end of for cycle
                        }// end of if/else cycle

                    }// end of if/else cycle

                }// end of if cycle

            }// end of for cycle

        }// end of if cycle
        testo.append(A_CAPO);

        return testo.toString();
    }// end of method


    /**
     * Righe singole senza nessun raggruppamento
     */
    public String righeSemplici(LinkedHashMap<String, ArrayList<String>> mappaDidascalie) {
        StringBuilder testo = new StringBuilder(VUOTA);
        ArrayList<String> listaDidascalie;

        if (mappaDidascalie != null) {
            for (String key : mappaDidascalie.keySet()) {
                listaDidascalie = mappaDidascalie.get(key);

                if (text.isValid(key)) {
                    for (String didascalia : listaDidascalie) {
                        testo.append(ASTERISCO).append(key).append(TAG_SEP).append(didascalia).append(A_CAPO);
                    }// end of for cycle
                } else {
                    for (String didascalia : listaDidascalie) {
                        testo.append(ASTERISCO).append(didascalia).append(A_CAPO);
                    }// end of for cycle
                }// end of if/else cycle

            }// end of for cycle
        }// end of if cycle

        return testo.toString();
    }// fine del metodo


    /**
     * Righe raggruppate per anno/giorno
     */
    public String righeRaggruppate(LinkedHashMap<String, ArrayList<String>> mappaDidascalie) {
        StringBuilder testo = new StringBuilder(VUOTA);
        ArrayList<String> listaDidascalie;

        if (mappaDidascalie != null) {
            for (String key : mappaDidascalie.keySet()) {
                listaDidascalie = mappaDidascalie.get(key);

                if (listaDidascalie.size() == 1) {
                    testo.append(ASTERISCO).append(text.isValid(key) ? key + TAG_SEP : SPAZIO).append(listaDidascalie.get(0)).append(A_CAPO);
                } else {
                    if (text.isValid(key)) {
                        testo.append(ASTERISCO).append(key).append(A_CAPO);
                        for (String didascalia : listaDidascalie) {
                            testo.append(ASTERISCO + ASTERISCO).append(didascalia).append(A_CAPO);
                        }// end of if/else cycle
                    } else {
                        for (String didascalia : listaDidascalie) {
                            testo.append(ASTERISCO).append(didascalia).append(A_CAPO);
                        }// end of if/else cycle
                    }// end of if/else cycle
                }// end of for cycle
            }// end of for cycle
        }// end of if cycle

        return testo.toString();
    }// fine del metodo


    public int getMappaSize(LinkedHashMap<String, ArrayList<String>> mappaGenerale) {
        int numVoci = 0;

        for (ArrayList<String> lista : mappaGenerale.values()) {
            numVoci += lista.size();
        }// end of for cycle

        return numVoci;
    }// end of method


    public int getMappaDueSize(LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> mappaGenerale) {
        int numVoci = 0;

        for (LinkedHashMap<String, ArrayList<String>> mappaChiaveDue : mappaGenerale.values()) {
            for (ArrayList<String> lista : mappaChiaveDue.values()) {
                numVoci += lista.size();
            }// end of for cycle
        }// end of for cycle

        return numVoci;
    }// end of method


    /**
     * La mappa delle biografie arriva non ordinata
     * Occorre spostare in basso il paragrafo vuoto
     */
    protected void fixPosizioneParagrafoVuoto(LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> mappaGenerale, String titoloParagrafoVuoto) {
        LinkedHashMap<String, ArrayList<String>> mappaChiaveDue;

        if (mappaGenerale == null) {
            return;
        }// end of if cycle

        if (mappaGenerale.containsKey(titoloParagrafoVuoto)) {
            mappaChiaveDue = mappaGenerale.get(titoloParagrafoVuoto);
            mappaGenerale.remove(titoloParagrafoVuoto);
            mappaGenerale.put(titoloParagrafoVuoto, mappaChiaveDue);
        }// end of if cycle

    }// fine del metodo

}// end of class
