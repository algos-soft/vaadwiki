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
import it.algos.vaadwiki.modules.professione.Professione;
import it.algos.vaadwiki.modules.professione.ProfessioneService;
import it.algos.vaadwiki.service.ABioService;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.*;

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

//            listaDisordinata.sort(new Comparator<WrapDidascalia>() {
//
//                int w1Ord;
//
//                int w2Ord;
//
//
//                @Override
//                public int compare(WrapDidascalia dida1, WrapDidascalia dida2) {
//                    w1Ord = dida1.getOrdine();
//                    w2Ord = dida2.getOrdine();
//
//                    return text.compareInt(w1Ord, w2Ord);
//                }// end of method
//            });//end of lambda expressions and anonymous inner class

            listaDisordinata.sort(new Comparator<WrapDidascalia>() {

                int w1Ord;

                int w2Ord;

                String w1Cog;

                String w2Cog;

                String w1ChiaveUno;

                String w2ChiaveUno;

                int resultOrdine;

                int resultCognomi;


                @Override
                public int compare(WrapDidascalia dida1, WrapDidascalia dida2) {
                    w1Ord = dida1.getOrdine();
                    w2Ord = dida2.getOrdine();
                    w1Cog = dida1.getSottoChiave();
                    w2Cog = dida2.getSottoChiave();
                    w1ChiaveUno = dida1.getChiave();
                    w2ChiaveUno = dida2.getChiave();

                    resultOrdine = text.compareInt(w1Ord, w2Ord);

                    if (resultOrdine == 0) {
//                        return text.compareStr(w1Cog, w2Cog);
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

        for (WrapDidascalia wrap : listaDisordinata) {
            chiave = wrap.getChiave();
            chiave = text.isValid(chiave) ? LibWiki.setQuadre(chiave) : "";

            if (mappa.get(chiave) == null) {
                lista = new ArrayList<String>();
                mappa.put(chiave, lista);
            } else {
                lista = (ArrayList<String>) mappa.get(chiave);
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
    public LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> creaMappaChiaveUno(ArrayList<WrapDidascalia> listaGrezza, String titoloParagrafoVuoto) {
        LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> mappaGenerale = new LinkedHashMap<>();
        LinkedHashMap<String, ArrayList<WrapDidascalia>> mappaParagrafi = new LinkedHashMap<>();
        LinkedHashMap<String, ArrayList<String>> mappaChiaveDue;
        ArrayList<WrapDidascalia> listaChiaveDue = null;
        String chiaveUno;

        for (WrapDidascalia wrap : listaGrezza) {
            chiaveUno = text.isValid(wrap.chiaveUno) ? wrap.chiaveUno : titoloParagrafoVuoto;

            if (mappaParagrafi.get(chiaveUno) == null) {
                listaChiaveDue = new ArrayList<WrapDidascalia>();
            } else {
                listaChiaveDue = (ArrayList<WrapDidascalia>) mappaParagrafi.get(chiaveUno);
            }// end of if/else cycle
            listaChiaveDue.add(wrap);
            mappaParagrafi.put(chiaveUno, listaChiaveDue);
        }// end of for cycle

        for (String key : mappaParagrafi.keySet()) {
            mappaChiaveDue = creaMappaChiaveDue(mappaParagrafi.get(key));
            mappaGenerale.put(key, mappaChiaveDue);
        }// end of for cycle

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
    protected String getTitoloParagrafo(Bio bio, String tagParagrafoNullo) {
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

        if (bio.getWikiTitle().equals("Ferdinando Ughelli")) {
            int a = 87;
        }// end of if cycle

        attivitaSingolare = bio.getAttivita().singolare;
        professione = professioneService.findByKeyUnica(attivitaSingolare);
        genere = genereService.findByKeyUnica(attivitaSingolare);
//        }// end of if cycle

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
     * Ogni chiave della mappa è una dei giorni/anni in cui suddividere la pagina <br>
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
        return listaNomi.mappaSemplice;
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
        return listaCognomi.mappaSemplice;
    }// end of method


    /**
     * Elabora la mappa di didascalie e costruisce il testo della pagina <br>
     */
    public String righeParagrafo(LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> mappaGenerale) {
        String testo = VUOTA;
        int numVociParagrafo;
        HashMap<String, Object> mappa;
        String titoloParagrafo;
        String titoloSottopagina;
        String paginaLinkata;
        String titoloVisibile;
        List<Bio> lista = null;
        LinkedHashMap<String, ArrayList<String>> mappaParagrafi = new LinkedHashMap<>();
        LinkedHashMap<String, ArrayList<String>> mappaChiaveDue;
        ArrayList<WrapDidascalia> listaChiaveDue = null;

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
