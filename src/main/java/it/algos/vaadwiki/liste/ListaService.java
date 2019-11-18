package it.algos.vaadwiki.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadwiki.application.WikiCost;
import it.algos.vaadwiki.didascalia.EADidascalia;
import it.algos.vaadwiki.didascalia.WrapDidascalia;
import it.algos.vaadwiki.modules.bio.Bio;
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

import java.util.*;

import static it.algos.vaadflow.application.FlowCost.A_CAPO;
import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.AST;
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

//    LinkedHashMap<String, List<String>> mappaChiaveDue;


    /**
     * Costruisce una lista di didascalie (Wrap) che hanno una valore valido per la pagina specifica <br>
     * La lista NON è ordinata <br>
     *
     * @param listaGrezzaBio di persone che hanno una valore valido per la pagina specifica
     *
     * @return lista NON ORDINATA di didascalie (Wrap)
     */
    public ArrayList<WrapDidascalia> creaListaDidascalie(List<Bio> listaGrezzaBio, EADidascalia typeDidascalia) {
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
//        Collections.sort(listaDisordinata);
//        return listaDisordinata;
//
        return ordinaListaDidascalieChiaveTre(listaDisordinata);
    }// fine del metodo


    /**
     * Ordina la lista di didascalie (Wrap) che hanno una valore valido per la pagina specifica <br>
     *
     * @param listaDisordinata di didascalie
     *
     * @return lista di didascalie (Wrap) ordinate per giorno/anno (key) e poi per cognome (value)
     */
    public ArrayList<WrapDidascalia> ordinaListaDidascalieCognomi(ArrayList<WrapDidascalia> listaDisordinata) {
//        Collections.sort(listaDisordinata);
//        return listaDisordinata;
        return ordinaListaDidascalieChiaveTre(listaDisordinata);
    }// fine del metodo


    /**
     * Ordina la lista di didascalie (Wrap) che hanno una valore valido per la pagina specifica <br>
     *
     * @param listaDisordinata di didascalie
     *
     * @return lista di didascalie (Wrap) ordinate per 'chiaveTre'
     */
    public ArrayList<WrapDidascalia> ordinaListaDidascalieChiaveTre(List<WrapDidascalia> listaDisordinata) {
        ArrayList<WrapDidascalia> listaOrdinata = null;
        LinkedHashMap<String, WrapDidascalia> mappa = null;

        if (array.isValid(listaDisordinata)) {
            listaOrdinata = new ArrayList<>();
            mappa = new LinkedHashMap<>();

            for (WrapDidascalia wrap : listaDisordinata) {
                mappa.put(wrap.chiaveTre, wrap);
            }// end of for cycle

            Set<String> listaChiavi = mappa.keySet();
            String[] matrice = listaChiavi.toArray(new String[listaChiavi.size()]);
            List<String> list = Arrays.asList(matrice);
            Collections.sort(list);

            for (String key : list) {
                listaOrdinata.add(mappa.get(key));
            }// end of for cycle

        }// end of if cycle

        return listaOrdinata;
    }// fine del metodo

//    /**
//     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
//     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
//     * Ogni chiave della mappa è una dei giorni/anni in cui suddividere la pagina <br>
//     * La chiave è comprensiva di parentesi quadre per avere il link alla pagina wiki <br>
//     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
//     *
//     * @param listaOrdinata di didascalie (Wrap) ordinate per giorno/anno (key) e poi per cognome (value)
//     *
//     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
//     */
//    public LinkedHashMap<String, List<String>> creaMappaQuadre(List<WrapDidascalia> listaOrdinata) {
//        LinkedHashMap<String, List<String>> mappa = new LinkedHashMap<>();
//        ArrayList<String> lista = null;
//        String chiave;
//
//        for (WrapDidascalia wrap : listaOrdinata) {
//            chiave = wrap.getChiave();
//            chiave = LibWiki.setQuadre(chiave);
//
//            if (mappa.get(chiave) == null) {
//                lista = new ArrayList<String>();
//                mappa.put(chiave, lista);
//            } else {
//                lista = (ArrayList<String>) mappa.get(chiave);
//            }// end of if/else cycle
//            lista.add(wrap.getTestoSenza()); //@todo rimettere
//
//        }// end of for cycle
//
//        return mappa;
//    }// fine del metodo


//    /**
//     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
//     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
//     * Ogni chiave della mappa è una dei giorni/anni in cui suddividere la pagina <br>
//     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
//     * Sovrascritto nella sottoclasse concreta <br>
//     *
//     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
//     *
//     * @listaOrdinata di didascalie (Wrap) ordinate per giorno/anno (key) e poi per cognome (value)
//     */
//    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> creaMappaChiaveUno(ArrayList<WrapDidascalia> listaGrezza) {
//        return creaMappaChiaveUno(listaGrezza, "");
//    }// fine del metodo


//    /**
//     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
//     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
//     * Ogni chiave della mappa è una dei giorni/anni in cui suddividere la pagina <br>
//     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
//     * Sovrascritto nella sottoclasse concreta <br>
//     *
//     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
//     *
//     * @listaOrdinata di didascalie (Wrap) ordinate per giorno/anno (key) e poi per cognome (value)
//     */
//    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> creaMappaChiaveUno(ArrayList<WrapDidascalia> listaGrezza, String titoloParagrafoVuoto) {
//        return creaMappaChiaveUno(listaGrezza, titoloParagrafoVuoto, true);
//    }// fine del metodo


    /**
     * Mappa delle didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiaveUno (ordinata) che corrisponde al titolo del paragrafo <br>
     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
     * Ogni valore della mappa è costituito da una ulteriore LinkedHashMap <br>
     * Questa mappa è composta da una chiaveDue e da un ArrayList di didascalie (testo) <br>
     * La chiaveUno è un secolo, un mese, un'attività (a seconda del tipo di didascalia) <br>
     * La chiaveUno è un link a pagina di wikipedia (escluso titoloParagrafoVuoto) con doppie quadre <br>
     * La chiaveDue è un anno, un giorno (a seconda del tipo di didascalia) <br>
     * Le didascalie sono ordinate per cognome <br>
     *
     * @param listaDidascalie da raggruppare
     *
     * @return mappa complessa
     */
    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> creaMappa(List<WrapDidascalia> listaDidascalie, EADidascalia typeDidascalia) {
        return creaMappa(listaDidascalie, "", false, false, false, typeDidascalia);
    }// fine del metodo


    /**
     * Mappa delle didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiaveUno (ordinata) che corrisponde al titolo del paragrafo <br>
     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
     * Ogni valore della mappa è costituito da una ulteriore LinkedHashMap <br>
     * Questa mappa è composta da una chiaveDue e da un ArrayList di didascalie (testo) <br>
     * La chiaveUno è un secolo, un mese, un'attività (a seconda del tipo di didascalia) <br>
     * La chiaveUno è un link a pagina di wikipedia (escluso titoloParagrafoVuoto) con doppie quadre <br>
     * La chiaveDue è un anno, un giorno (a seconda del tipo di didascalia) <br>
     * Le didascalie sono ordinate per cognome <br>
     *
     * @param listaDidascalie      da raggruppare
     * @param titoloParagrafoVuoto titolo da assegnare al paragrafo vuoto
     * @param paragrafoVuotoInCoda posizionamento in coda del paragrafo vuoto
     *
     * @return mappa complessa
     */
    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> creaMappa(
            List<WrapDidascalia> listaDidascalie,
            String titoloParagrafoVuoto,
            boolean paragrafoVuotoInCoda,
            boolean usaLinkAttivita,
            boolean usaOrdineAlfabetico,
            EADidascalia typeDidascalia) {
        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaGenerale;
        LinkedHashMap<String, List<WrapDidascalia>> mappaParagrafi;

        //--creazione paragrafi con titolo='chiaveUno'
        mappaParagrafi = creaParagrafi(listaDidascalie);

        //--ordinamento alfabetico dei paragrafi
        mappaParagrafi = ordinaParagrafi(mappaParagrafi, typeDidascalia);

        //--costruzione del titolo definitivo dei paragrafi
        mappaParagrafi = titoloParagrafi(mappaParagrafi, usaLinkAttivita, titoloParagrafoVuoto);

        //--creazione della mappa interna 'mappaChiaveDue'
        mappaGenerale = creaMappaInterna(mappaParagrafi, usaOrdineAlfabetico);

        //--spostamento in fondo del paragrafo dal titolo vuoto
        this.spostaInFondo(mappaGenerale, paragrafoVuotoInCoda, titoloParagrafoVuoto);

        return mappaGenerale;
    }// fine del metodo


    /**
     * Ordinamento alfabetico dei paragrafi <br>
     */
    public LinkedHashMap<String, List<WrapDidascalia>> ordinaParagrafi(LinkedHashMap<String, List<WrapDidascalia>> mappaParagrafi, EADidascalia typeDidascalia) {
        LinkedHashMap<String, List<WrapDidascalia>> mappaParagrafiOrdinata = mappaParagrafi;

        switch (typeDidascalia) {
            case giornoNato:
            case giornoMorto:
                break;
            case annoNato:
            case annoMorto:
                break;
            case listaNomi:
                break;
            case listaCognomi:
                break;
            default:
                log.warn("Switch - caso non definito");
                break;
        } // end of switch statement

        return mappaParagrafiOrdinata;
    }// fine del metodo


    /**
     * Creazione paragrafi con titolo='chiaveUno' <br>
     */
    public LinkedHashMap<String, List<WrapDidascalia>> creaParagrafi(List<WrapDidascalia> listaDidascalie) {
        String chiaveUno;
        LinkedHashMap<String, List<WrapDidascalia>> mappaParagrafi = new LinkedHashMap<>();
        ArrayList<WrapDidascalia> listaChiaveDue = null;

        for (WrapDidascalia wrap : listaDidascalie) {
            chiaveUno = wrap.chiaveUno;

            if (mappaParagrafi.get(chiaveUno) == null) {
                listaChiaveDue = new ArrayList<WrapDidascalia>();
            } else {
                listaChiaveDue = (ArrayList<WrapDidascalia>) mappaParagrafi.get(chiaveUno);
            }// end of if/else cycle
            listaChiaveDue.add(wrap);
            mappaParagrafi.put(chiaveUno, listaChiaveDue);
        }// end of for cycle

        return mappaParagrafi;
    }// fine del metodo


    /**
     * Costruzione del titolo definitivo dei paragrafi <br>
     * aggiunge il link alla pagina di wikipedia per la professione di riferimento <br>
     * se le didascalie non sono omogenee (non puntano alla stessa pagina di link), non mette il link <br>
     * aggiunge le parentesi quadre <br>
     * se il titolo del paragrafo è vuoto, sostituisce col titolo previsto standard <br>
     */
    public LinkedHashMap<String, List<WrapDidascalia>> titoloParagrafi(LinkedHashMap<String, List<WrapDidascalia>> mappaParagrafi, boolean usaLinkAttivita, String titoloParagrafoVuoto) {
        LinkedHashMap<String, List<WrapDidascalia>> mappaParagrafiTitolo = new LinkedHashMap<>();
        String chiave = "";
        List<String> listaChiavi;
        List<WrapDidascalia> listaWrap;

        if (usaLinkAttivita) {
            for (String key : mappaParagrafi.keySet()) {
                listaChiavi = new ArrayList();
                for (WrapDidascalia wrap : mappaParagrafi.get(key)) {
                    chiave = getTitoloParagrafo(wrap.bio);
                    if (!listaChiavi.contains(chiave)) {
                        listaChiavi.add(chiave);
                    }// end of if cycle
                }// end of for cycle

                if (listaChiavi.size() == 1) {
                    chiave = listaChiavi.get(0);
                } else {
                    chiave = key;
                }// end of if/else cycle

                chiave = text.isValid(chiave) ? chiave : titoloParagrafoVuoto;
                mappaParagrafiTitolo.put(chiave, mappaParagrafi.get(key));
            }// end of for cycle
        } else {
            for (String key : mappaParagrafi.keySet()) {
                if (text.isValid(key)) {
                    mappaParagrafiTitolo.put(key, mappaParagrafi.get(key));
                } else {
                    mappaParagrafiTitolo.put(titoloParagrafoVuoto, mappaParagrafi.get(key));
                }// end of if/else cycle
            }// end of for cycle
        }// end of if/else cycle

        return mappaParagrafiTitolo;
    }// fine del metodo


    /**
     * Creazione della mappa interna 'mappaChiaveDue' <br>
     */
    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> creaMappaInterna(LinkedHashMap<String, List<WrapDidascalia>> mappaParagrafi, boolean usaOrdineAlfabetico) {
        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaGenerale = new LinkedHashMap<>();
        LinkedHashMap<String, List<String>> mappaChiaveDue;

        for (String key : mappaParagrafi.keySet()) {
            mappaChiaveDue = creaMappaInterna(mappaParagrafi.get(key));
            //--ordinamento alfabetico della mappa interna 'mappaChiaveDue'
            if (usaOrdineAlfabetico) {
                mappaChiaveDue = ordinaMappaAlfabetica(mappaChiaveDue);
            }// end of if cycle
            mappaGenerale.put(key, mappaChiaveDue);
        }// end of for cycle

        return mappaGenerale;
    }// fine del metodo


    /**
     * Spostamento (eventuale, dipende dal flag) in fondo del paragrafo senza titolo <br>
     */
    public void spostaInFondo(HashMap<String, LinkedHashMap<String, List<String>>> mappaGenerale, boolean paragrafoVuotoInCoda, String titoloParagrafoVuoto) {
        LinkedHashMap<String, List<String>> mappaChiaveDue;

        if (paragrafoVuotoInCoda) {
            if (mappaGenerale.containsKey(titoloParagrafoVuoto)) {
                mappaChiaveDue = mappaGenerale.get(titoloParagrafoVuoto);
                mappaGenerale.remove(titoloParagrafoVuoto);
                mappaGenerale.put(titoloParagrafoVuoto, mappaChiaveDue);
            }// end of if cycle
        }// end of if cycle
    }// fine del metodo


    /**
     * Costruisce una mappa basata sulla 'chiaveDue' <br>
     * Ordina il contenuto (per cognome) <br>
     * Estrae il testo della discalia <br>
     */
    private LinkedHashMap<String, List<String>> creaMappaInterna(List<WrapDidascalia> listaDidascalieDellaChiaveUno) {
        LinkedHashMap<String, List<String>> mappa = new LinkedHashMap<>();
        LinkedHashMap<String, List<WrapDidascalia>> mappaWrap = new LinkedHashMap<>();
        List<WrapDidascalia> listaWrap = null;
        List<WrapDidascalia> listaWrap2 = null;
        List<String> listaChiaveDue = null;
        List<String> lista = null;
        String chiaveDue;

        //--costruisce una mappa 'disordinata' usando la 'chiaveDue' (A, B, C, ...) (
        for (WrapDidascalia wrap : listaDidascalieDellaChiaveUno) {
            chiaveDue = wrap.chiaveDue;

            if (mappaWrap.get(chiaveDue) == null) {
                listaWrap = new ArrayList<>();
            } else {
                listaWrap = mappaWrap.get(chiaveDue);
            }// end of if/else cycle
            listaWrap.add(wrap);
            mappaWrap.put(chiaveDue, listaWrap);
        }// end of for cycle

        //--ordina il contenuto per 'cognome'
        for (String keyParagrafo : mappaWrap.keySet()) {
            listaWrap2 = mappaWrap.get(keyParagrafo);
            listaWrap2 = orderByCognome(listaWrap2);
            mappaWrap.put(keyParagrafo, listaWrap2);
        }// end of for cycle

        Set<String> listaChiavi = mappa.keySet();
        String[] matrice = listaChiavi.toArray(new String[listaChiavi.size()]);
        List<String> list = Arrays.asList(matrice);
        Collections.sort(list);

        //--estrae il testo della discalia da WrapDidascalia
        for (String keyParagrafo : mappaWrap.keySet()) {
            listaWrap2 = mappaWrap.get(keyParagrafo);
            lista = new ArrayList<>();
            for (WrapDidascalia wrap : listaWrap2) {
                lista.add(wrap.getTestoSenza());
            }// end of for cycle
            mappa.put(keyParagrafo, lista);
        }// end of for cycle

        return mappa;
    }// fine del metodo


    public List<WrapDidascalia> orderByCognome(List<WrapDidascalia> listaIn) {
        List<WrapDidascalia> listaOut = listaIn;
        LinkedHashMap<String, List<WrapDidascalia>> mappa;
        String cognome = "";
        List<WrapDidascalia> listaTmp = null;

        if (array.isValid(listaIn)) {
            listaOut = new ArrayList<>();
            mappa = new LinkedHashMap<>();

            for (WrapDidascalia wrap : listaIn) {
                cognome = wrap.chiaveTre;

                if (mappa.containsKey(cognome)) {
                    listaTmp = mappa.get(cognome);
                } else {
                    listaTmp = new ArrayList<>();
                }// end of if/else cycle
                listaTmp.add(wrap);

                mappa.put(cognome, listaTmp);
            }// end of for cycle

            Set<String> listaChiavi = mappa.keySet();
            String[] matrice = listaChiavi.toArray(new String[listaChiavi.size()]);
            List<String> list = Arrays.asList(matrice);
            Collections.sort(list);

            for (String key : list) {
                for (WrapDidascalia wrap : mappa.get(key)) {
                    listaOut.add(wrap);
                }// end of for cycle
            }// end of for cycle

        }// end of if cycle

        return listaOut;
    }// end of method


    /**
     * Ordina i paragrafi (A, B, C, ...) <br>
     */
    public LinkedHashMap<String, List<String>> ordinaMappaAlfabetica(LinkedHashMap<String, List<String>> mappaDisordinata) {
        LinkedHashMap<String, List<String>> mappaOrdinata = new LinkedHashMap<String, List<String>>();
        List<String> listaKey = new ArrayList();

        for (String key : mappaDisordinata.keySet()) {
            listaKey.add(key);
        }// end of for cycle

        Collections.sort(listaKey);
        for (String orderedKey : listaKey) {
            mappaOrdinata.put(orderedKey, mappaDisordinata.get(orderedKey));
        }// end of for cycle

        return mappaOrdinata;
    }// fine del metodo


    public LinkedHashMap<String, List<String>> ordinaMappaCronologica(LinkedHashMap<String, List<String>> mappaDisordinata) {
        LinkedHashMap<String, List<String>> mappaOrdinata = new LinkedHashMap<String, List<String>>();
        List<Integer> listaKey = new ArrayList();
        List lista;

        for (String key : mappaDisordinata.keySet()) {
            lista = mappaDisordinata.get(key);
        }// end of for cycle

        Collections.sort(listaKey);

        return mappaOrdinata;
    }// fine del metodo

//    /**
//     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
//     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
//     * Ogni chiave della mappa è una dei giorni/anni in cui suddividere la pagina <br>
//     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
//     * Sovrascritto nella sottoclasse concreta <br>
//     *
//     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
//     *
//     * @listaOrdinata di didascalie (Wrap) ordinate per giorno/anno (key) e poi per cognome (value)
//     */
//    @Deprecated
//    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> creaMappaChiaveUno(
//            List<WrapDidascalia> listaGrezza,
//            String paragrafoVuoto,
//            boolean paragrafoVuotoInCoda) {
//        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaGenerale = new LinkedHashMap<>();
//        LinkedHashMap<String, List<WrapDidascalia>> mappaParagrafi = new LinkedHashMap<>();
//        LinkedHashMap<String, List<String>> mappaChiaveDue = null;
//        List<WrapDidascalia> listaChiaveDue = null;
//        String chiaveUno;
//        String titoloParagrafo;
//        int size = 0;
////        String paragrafoVuoto = titoloParagrafoVuoto;
//
//        for (WrapDidascalia wrap : listaGrezza) {
//            chiaveUno = text.isValid(wrap.chiaveUno) ? wrap.chiaveUno : paragrafoVuoto;
//            titoloParagrafo = getTitoloParagrafo(wrap.bio, paragrafoVuoto);
//
//            if (mappaParagrafi.get(titoloParagrafo) == null) {
//                listaChiaveDue = new ArrayList<WrapDidascalia>();
//            } else {
//                listaChiaveDue = (ArrayList<WrapDidascalia>) mappaParagrafi.get(titoloParagrafo);
//            }// end of if/else cycle
//            listaChiaveDue.add(wrap);
//            mappaParagrafi.put(titoloParagrafo, listaChiaveDue);
//        }// end of for cycle
//
//        for (String key : mappaParagrafi.keySet()) {
//            titoloParagrafo = key;
//            size = 0;
//            boolean cambia = false;
////            mappaChiaveDue = creaMappaInterna(mappaParagrafi.get(titoloParagrafo));
//
////            if (usaParagrafoSize) {
////                cambia = titoloParagrafo.equals(paragrafoVuoto);
////                size = mappaChiaveDue.get("").size();
////                titoloParagrafo += " <small><small>(" + size + ")</small></small>";
////                paragrafoVuoto = cambia ? titoloParagrafo : paragrafoVuoto;
////            }// end of if cycle
//            mappaGenerale.put(titoloParagrafo, mappaChiaveDue);
//        }// end of for cycle
//
//        if (mappaGenerale.containsKey(paragrafoVuoto)) {
//            mappaChiaveDue = mappaGenerale.get(paragrafoVuoto);
//            mappaGenerale.remove(paragrafoVuoto);
//            mappaGenerale.put(paragrafoVuoto, mappaChiaveDue);
//        }// end of if cycle
//
//        return mappaGenerale;
//    }// fine del metodo


//    public void pippo(ArrayList<WrapDidascalia> listaDidascalie, EADidascalia typeDidascalia, String tagParagrafoNullo) {
//        LinkedHashMap<String, HashMap> mappaBio = new LinkedHashMap<String, HashMap>();
//        if (listaDidascalie != null && listaDidascalie.size() > 0) {
//            for (WrapDidascalia wrap : listaDidascalie) {
//                elaboraMappaSingola(mappaBio, wrap, tagParagrafoNullo);
//            }// end of if cycle
//        }// end of for cycle
//    }// fine del metodo


//    /**
//     * Costruisce una mappa di tutte le biografie della pagina, suddivisa in paragrafi
//     * Sovrascritto
//     */
//    @SuppressWarnings("all")
//    @Deprecated
//    protected void elaboraMappaSingola(LinkedHashMap<String, HashMap> mappaBio, WrapDidascalia wrap, String tagParagrafoNullo) {
//        String key = wrap.getChiave();
//        String didascalia;
//        ArrayList<Bio> lista;
//        HashMap<String, Object> mappa;
//        int voci;
//        Bio bio = wrap.bio;
//
//        if (mappaBio.containsKey(key)) {
//            mappa = mappaBio.get(key);
//            lista = (ArrayList<Bio>) mappa.get(KEY_MAP_LISTA);
//            voci = (int) mappa.get(KEY_MAP_VOCI);
//            lista.add(bio);
//            mappa.put(KEY_MAP_VOCI, voci + 1);
//        } else {
//            mappa = new HashMap<>();
//            lista = new ArrayList<>();
//            lista.add(bio);
//            mappa.put(KEY_MAP_PARAGRAFO_TITOLO, key);
//            mappa.put(KEY_MAP_PARAGRAFO_LINK, getTitoloParagrafo(bio, tagParagrafoNullo));
//            mappa.put(KEY_MAP_LISTA, lista);
//            mappa.put(KEY_MAP_SESSO, bio.getSesso());
//            mappa.put(KEY_MAP_VOCI, 1);
//
////            if (usaSortCronologico) {
////            if (text.isValid(bio.getGiornoNato())) {
////                mappa.put(KEY_MAP_ORDINE_GIORNO_NATO, bio.getGiornoNato().getOrdinamento());
////            }// end of if cycle
////            if (text.isValid(bio.getGiornoMorto())) {
////                mappa.put(KEY_MAP_ORDINE_GIORNO_MORTO, bio.getGiornoMorto().getOrdinamento());
////            }// end of if cycle
////            if (text.isValid(bio.getAnnoNato())) {
////                mappa.put(KEY_MAP_ORDINE_ANNO_NATO, bio.getAnnoNato().getOrdinamento());
////            }// end of if cycle
////            if (text.isValid(bio.getAnnoMorto())) {
////                mappa.put(KEY_MAP_ORDINE_ANNO_MORTO, bio.getAnnoMorto().getOrdinamento());
////            }// end of if cycle
////            }// end of if cycle
//
//            mappaBio.put(key, mappa);
//        }// end of if/else cycle
//    }// fine del metodo


    /**
     * Costruisce il titolo del paragrafo
     * <p>
     * Questo deve essere composto da:
     * Professione.pagina
     * Genere.plurale
     */
    @Deprecated
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


    public String getTitoloParagrafo(Bio bio) {
        String titoloParagrafo = "";
        String paginaWiki = VUOTA;
        String linkVisibile = VUOTA;

        if (bio == null) {
            return VUOTA;
        }// end of if cycle

        if (bio.getAttivita() != null) {
            paginaWiki = getProfessioneDaAttivitaSingolare(bio.getAttivita().singolare);
            linkVisibile = getGenereDaAttivitaSingolare(bio.getAttivita().singolare, bio.getSesso());
        }// end of if cycle

        if (text.isValid(paginaWiki) && text.isValid(linkVisibile)) {
            titoloParagrafo = costruisceTitolo(paginaWiki, linkVisibile, "");
        }// end of if cycle

        return titoloParagrafo;
    }// fine del metodo

//    public String getTitoloParagrafo2(Bio bio) {
//        String titoloParagrafo = "";
//        String paginaWiki = VUOTA;
//        String linkVisibile = VUOTA;
//
//        paginaWiki = getProfessioneDaAttivitaSingolare(bio.getAttivita().singolare);
//        linkVisibile = getGenereDaAttivitaSingolare(bio.getAttivita().singolare, bio.getSesso());
//
//        if (text.isValid(paginaWiki) && text.isValid(linkVisibile)) {
//            titoloParagrafo = costruisceTitolo(paginaWiki, linkVisibile, "");
//        }// end of if cycle
//
//        return titoloParagrafo;
//    }// fine del metodo


    public String getTitoloParagrafo(String linkVisibile) {
        String titoloParagrafo = "";
        String paginaWiki = VUOTA;
        Genere genere = null;
        Professione professione = null;

//        genere = genereService.findByKeyUnica(linkVisibile);
        if (genere != null) {
            professione = professioneService.findByKeyUnica(genere.singolare);
        }// end of if cycle
        if (professione != null) {
            paginaWiki = text.primaMaiuscola(professione.getPagina());
        }// end of if cycle

        if (text.isValid(paginaWiki)) {
            titoloParagrafo = costruisceTitolo(paginaWiki, text.primaMaiuscola(linkVisibile), "");
        }// end of if cycle

        return titoloParagrafo;
    }// fine del metodo


    /**
     * Restituisce il titolo della pagina wiki associata all'attività indicata <br>
     *
     * @param attivitaSingolare
     *
     * @return nome della pagina wiki da linkare come riferimento
     */
    public String getProfessioneDaAttivitaSingolare(String attivitaSingolare) {
        String professioneTxt = "";
        Professione professione;

        professione = professioneService.findByKeyUnica(attivitaSingolare.toLowerCase());

        if (professione != null) {
            professioneTxt = professione.getPagina();
        } else {
            professioneTxt = attivitaSingolare;
        }// end of if/else cycle

        if (text.isValid(professioneTxt)) {
            professioneTxt = text.primaMaiuscola(professioneTxt);
        }// end of if cycle

        return professioneTxt;
    }// end of single test


    /**
     * Restituisce il titolo plurale visibile associato all'attività indicata <br>
     *
     * @param attivitaSingolare
     *
     * @return nome della pagina wiki da linkare come riferimento
     */
    public String getGenereDaAttivitaSingolare(String attivitaSingolare, String sesso) {
        String linkVisibile = VUOTA;
        String genereTxt = "";
        Genere genere;

        genere = genereService.findByKeyUnica(attivitaSingolare);

        if (text.isEmpty(sesso)) {
            sesso = "M";
        }// end of if cycle

        if (genere != null) {
            if (sesso.equals("M")) {
                genereTxt = genere.getPluraleMaschile();
            } else {
                if (sesso.equals("F")) {
                    genereTxt = genere.getPluraleFemminile();
                } else {
                    //@todo errore
                }// end of if/else cycle
            }// end of if/else cycle

            if (text.isValid(genereTxt)) {
                linkVisibile = text.primaMaiuscola(genereTxt);
            }// end of if cycle
        }// end of if cycle

        return linkVisibile;
    }// end of single test


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


//    /**
//     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
//     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
//     * Ogni chiave della mappa è una dei giorni/anni in cui suddividere la pagina <br>
//     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
//     *
//     * @param giorno di riferimento per la lista
//     *
//     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
//     */
//    public LinkedHashMap<String, ArrayList<String>> getMappaGiornoNato(Giorno giorno) {
//        LinkedHashMap<String, ArrayList<String>> mappa = null;
//        ListaGiornoNato listaGiornoNato;
//
//        listaGiornoNato = appContext.getBean(ListaGiornoNato.class, giorno);
//        return listaGiornoNato.mappaSemplice;
//    }// end of method


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
    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> getMappaGiornoNato(Giorno giorno, String titoloParagrafoVuoto, boolean paragrafoVuotoInCoda) {
        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappa = null;
        ListaGiornoNato listaGiornoNato;

        listaGiornoNato = appContext.getBean(ListaGiornoNato.class, giorno, titoloParagrafoVuoto, paragrafoVuotoInCoda);
        return listaGiornoNato.mappa;
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
    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> getMappaGiornoMorto(Giorno giorno, String titoloParagrafoVuoto, boolean paragrafoVuotoInCoda) {
        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappa = null;
        ListaGiornoMorto listaGiornoMorto;

        listaGiornoMorto = appContext.getBean(ListaGiornoMorto.class, giorno, titoloParagrafoVuoto, paragrafoVuotoInCoda);
        return listaGiornoMorto.mappa;
    }// end of method


//    /**
//     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
//     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
//     * Ogni chiave della mappa è una dei giorni/anni in cui suddividere la pagina <br>
//     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
//     *
//     * @param giorno di riferimento per la lista
//     *
//     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
//     */
//    public LinkedHashMap<String, ArrayList<String>> getMappaGiornoMorto(Giorno giorno) {
//        LinkedHashMap<String, ArrayList<String>> mappa = null;
//        ListaGiornoMorto listaGiornoMorto;
//
//        listaGiornoMorto = appContext.getBean(ListaGiornoMorto.class, giorno);
//        return listaGiornoMorto.mappaSemplice;
//    }// end of method


//    /**
//     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
//     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
//     * Ogni chiave della mappa è una dei giorni/anni in cui suddividere la pagina <br>
//     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
//     *
//     * @param anno di riferimento per la lista
//     *
//     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
//     */
//    public LinkedHashMap<String, ArrayList<String>> getMappaAnnoNato(Anno anno) {
//        LinkedHashMap<String, ArrayList<String>> mappa = null;
//        ListaAnnoNato listaAnnoNato;
//
//        listaAnnoNato = appContext.getBean(ListaAnnoNato.class, anno);
//        return listaAnnoNato.mappaSemplice;
//    }// end of method


//    /**
//     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
//     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
//     * Ogni chiave della mappa è una dei giorni/anni in cui suddividere la pagina <br>
//     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
//     *
//     * @param anno di riferimento per la lista
//     *
//     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
//     */
//    public LinkedHashMap<String, ArrayList<String>> getMappaAnnoMorto(Anno anno) {
//        LinkedHashMap<String, ArrayList<String>> mappa = null;
//        ListaAnnoMorto listaAnnoMorto;
//
//        listaAnnoMorto = appContext.getBean(ListaAnnoMorto.class, anno);
//        return listaAnnoMorto.mappaSemplice;
//    }// end of method


//    /**
//     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la property specifica <br>
//     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
//     * Ogni chiave della mappa è una delle attività in cui suddividere la pagina <br>
//     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
//     *
//     * @param nome di riferimento per la lista
//     *
//     * @return mappa ordinata delle didascalie ordinate per nomi (key) e poi per cognome (value)
//     */
//    @Deprecated
//    public LinkedHashMap<String, ArrayList<String>> getMappaNomi(Nome nome) {
//        LinkedHashMap<String, ArrayList<String>> mappa = null;
//        ListaNomi listaNomi;
//
//        listaNomi = appContext.getBean(ListaNomi.class, nome);
//        return listaNomi.mappaSemplice;
//    }// end of method


//    /**
//     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la property specifica <br>
//     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
//     * Ogni chiave della mappa è una delle attività in cui suddividere la pagina <br>
//     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
//     *
//     * @param nomeText di riferimento per la lista
//     *
//     * @return mappa ordinata delle didascalie ordinate per nomi (key) e poi per cognome (value)
//     */
//    @Deprecated
//    public LinkedHashMap<String, ArrayList<String>> getMappaNomi(String nomeText) {
//        LinkedHashMap<String, ArrayList<String>> mappa = null;
//        Nome nomeEntity = nomeService.findByKeyUnica(nomeText);
//
//        if (nomeEntity != null) {
//            mappa = getMappaNomi(nomeEntity);
//        }// end of if cycle
//
//        return mappa;
//    }// end of method


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
    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> getMappaNome(Nome nome) {
        LinkedHashMap<String, List<String>> mappa = null;
        ListaNomi listaNomi;

        listaNomi = appContext.getBean(ListaNomi.class, nome);
        return listaNomi.mappa;
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
    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> getMappaNome(String nomeText) {
        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappa = null;
        Nome nomeEntity = nomeService.findByKeyUnica(nomeText);

        if (nomeEntity != null) {
            mappa = getMappaNome(nomeEntity);
        }// end of if cycle

        return mappa;
    }// end of method


//    /**
//     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la property specifica <br>
//     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
//     * Ogni chiave della mappa è una dei nomi/cognomi in cui suddividere la pagina <br>
//     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br> //@todo Forse
//     *
//     * @param cognome di riferimento per la lista
//     *
//     * @return mappa ordinata delle didascalie ordinate per nomi/cognomi (key) e poi per cognome (value)//@todo Forse
//     */
//    public LinkedHashMap<String, ArrayList<String>> getMappaCognomi(Cognome cognome) {
//        LinkedHashMap<String, ArrayList<String>> mappa = null;
//        ListaCognomi listaCognomi;
//
//        listaCognomi = appContext.getBean(ListaCognomi.class, cognome);
//        return listaCognomi.mappaSemplice;
//    }// end of method


//    /**
//     * Elabora la mappa di didascalie e costruisce il testo della pagina <br>
//     */
//    public String righeSenzaParagrafoOld(LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaGenerale) {
//        StringBuilder testo = new StringBuilder(VUOTA);
//        LinkedHashMap<String, List<String>> mappaInterna;
//        List<String> listaInterna;
//
//        if (mappaGenerale == null) {
//            return VUOTA;
//        }// end of if cycle
//
//        for (Map.Entry<String, LinkedHashMap<String, List<String>>> entry : mappaGenerale.entrySet()) {
//            mappaInterna = entry.getValue();
//            for (Map.Entry<String, List<String>> entry2 : mappaInterna.entrySet()) {
//                listaInterna = entry2.getValue();
//                for (String didascalia : listaInterna) {
//                    testo.append(didascalia);
//                }// end of for cycle
//            }// end of for cycle
//        }// end of for cycle
//
//        return testo.toString();
//    }// end of method


    /**
     * Elabora la mappa di didascalie e costruisce il testo della pagina <br>
     */
    public String righeSenzaParagrafo(LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappa) {
        return righe(mappa, false, false);
    }// end of method


    /**
     * Elabora la mappa di didascalie e costruisce il testo della pagina <br>
     */
    public String righeConParagrafo(LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappa) {
        return righe(mappa, true, false);
    }// end of method


    /**
     * Elabora la mappa di didascalie e costruisce il testo della pagina <br>
     */
    public String righeConParagrafoSize(LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappa) {
        return righe(mappa, true, true);
    }// end of method


    /**
     * Elabora la mappa di didascalie e costruisce il testo della pagina <br>
     * I paragrafi (chiaveUno) possono esserci oppure no <br>
     * Il titolo del paragrafo vuoto arriva già deciso dalla mappa <br>
     * Il posizionamento (testa/coda) del paragrafo vuoto arriva già deciso dalla mappa <br>
     * Gli eventuali link (doppie quadre) nel titolo del paragrafo arrivano già decisi dalla mappa <br>
     * Le dimensioni del paragrafo (se esiste) possono essere indicate (usaParagrafoSize) oppure no <br>
     * Le righe sono sempre raggruppate (chiaveDue) <br>
     */
    private String righe(LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaGenerale, boolean usaParagrafo, boolean usaParagrafoSize) {
        StringBuilder testo = new StringBuilder(VUOTA);
        LinkedHashMap<String, List<String>> mappaParagrafi = new LinkedHashMap<>();
        List<String> listaDidascalie = null;
        String titoloParagrafo = "";
        int size = 0;

        if (mappaGenerale != null) {
            for (String keyUno : mappaGenerale.keySet()) {

                if (usaParagrafo && text.isValid(keyUno)) {
                    titoloParagrafo = keyUno;
                    testo.append(A_CAPO);
                    testo.append(A_CAPO);
                    if (usaParagrafoSize) {
                        size = getMappaSize(mappaGenerale.get(keyUno));
                        titoloParagrafo += " <small><small>(" + size + ")</small></small>";
                    }// end of if cycle
                    testo.append(PARAGRAFO).append(titoloParagrafo).append(PARAGRAFO);
                }// end of if cycle

                mappaParagrafi = mappaGenerale.get(keyUno);

                if (mappaParagrafi != null) {
                    for (String keyDue : mappaParagrafi.keySet()) {
                        listaDidascalie = mappaParagrafi.get(keyDue);

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

        return testo.toString().trim();
    }// fine del metodo


    /**
     * Elabora la mappa di didascalie e costruisce il testo della pagina <br>
     * I paragrafi ci sono sempre <br>
     * Il titolo del paragrafo vuoto arriva già deciso dalla mappa <br>
     * Il posizionamento (testa/coda) del paragrafo vuoto arriva già deciso dalla mappa <br>
     * Gli eventuali link (doppie quadre) nel titolo del paragrafo arrivano già decisi dalla mappa <br>
     * Le dimensioni del paragrafo sono sempre indicate <br>
     * Le righe sono sempre raggruppate (chiaveDue) <br>
     * La soglia di taglio arriva come parametro <br>
     * Il nome della sottopagina viene composto dal titoloPagina passato come parametro più il titolo visibile del paragrafo <br>
     * Se esiste un paragrafo dal titolo titoloParagrafoVuoto, questo rimane come titolo del paragrafo ma la sottoPagina si chiama titoloSottoPaginaVuota <br>
     *
     * @param mappaGenerale          di tutta una pagina che deve implementare le sottopagine per alcuni paragrafi (se ci sono)
     * @param soglia                 di voci biografiche per far scattare la sottopagina
     * @param titoloCompletoPagina   principale da passare alla sottopagina da costruire (UploadSottoPagina)
     * @param soggetto               della lista
     * @param titoloParagrafoVuoto   da controllare
     * @param titoloSottoPaginaVuota al posto del titoloParagrafoVuoto
     */
    public ListaSottopagina sottopagina(LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaGenerale, int soglia, String titoloCompletoPagina, String titoloParagrafoVuoto, String titoloSottoPaginaVuota) {
        ListaSottopagina sottopagina = null;
        StringBuilder builder = new StringBuilder(VUOTA);
        LinkedHashMap<String, List<String>> mappaParagrafi;
        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaSottopagine = new LinkedHashMap<String, LinkedHashMap<String, List<String>>>();
        String titoloParagrafo = "";
        int size = 0;
        String titoloVisibile;
        String titoloSottopagina;

        if (mappaGenerale != null) {
            for (String keyUno : mappaGenerale.keySet()) {

                titoloParagrafo = keyUno;
                builder.append(A_CAPO).append(A_CAPO);
                size = getMappaSize(mappaGenerale.get(keyUno));
                titoloParagrafo += " <small><small>(" + size + ")</small></small>";
                builder.append(PARAGRAFO).append(titoloParagrafo).append(PARAGRAFO);

                mappaParagrafi = mappaGenerale.get(keyUno);

                if (mappaParagrafi != null) {
                    if (size > soglia) {
                        builder.append(A_CAPO);
                        titoloVisibile = estraeVisibile(keyUno);
                        titoloVisibile = titoloVisibile.equals(titoloParagrafoVuoto) ? titoloSottoPaginaVuota : titoloVisibile;
                        titoloSottopagina = titoloCompletoPagina + "/" + titoloVisibile;

                        builder.append("{{Vedi anche|").append(titoloSottopagina).append("}}");
                        mappaSottopagine.put(titoloVisibile, mappaParagrafi);
                    } else {
                        builder.append(contenutoParagrafoNormale(mappaParagrafi));
                    }// end of if/else cycle
                }// end of if cycle
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


        sottopagina = appContext.getBean(ListaSottopagina.class, builder.toString().trim(), mappaSottopagine);
        return sottopagina;
    }// fine del metodo


    public String estraeVisibile(String titoloParagrafo) {
        String titoloVisibile = titoloParagrafo;
        String tag = "|";

        titoloVisibile = LibWiki.setNoQuadre(titoloVisibile);
        if (titoloVisibile.contains(tag)) {
            titoloVisibile = titoloVisibile.substring(titoloVisibile.indexOf(tag) + 1);
        }// end of if cycle

        return titoloVisibile;
    }// fine del metodo


    /**
     * Righe suddivise per paragrafi <br>
     * All'interno dei paragrafi usa righeSemplici <br>
     * Ignora la chiaveDue (prima lettera alfabetica del cognome) che viene utilizzata solo per le sottopagine <br>
     */
    public String contenutoParagrafoNormale(LinkedHashMap<String, List<String>> mappaParagrafo) {
        StringBuilder testo = new StringBuilder(VUOTA);
        List<String> listaDidascalie = null;

        if (mappaParagrafo != null) {
            for (String keyDue : mappaParagrafo.keySet()) {
                listaDidascalie = mappaParagrafo.get(keyDue);
                if (array.isValid(listaDidascalie)) {
                    for (String didascalia : listaDidascalie) {
                        testo.append(A_CAPO).append(AST).append(didascalia);
                    }// end of for cycle
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return testo.toString();
    }// end of method


//    /**
//     * Righe suddivise per paragrafi <br>
//     * Titolo del paragrafo con wikiLink <br>
//     * Titolo del paragrafo con dimensione (secondo flag) <br>
//     * All'interno dei paragrafi usa righeSemplici <br>
//     */
//    public String paragrafoAttivita(LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappa) {
//        StringBuilder testo = new StringBuilder(VUOTA);
//        LinkedHashMap<String, List<String>> mappaParagrafo;
//        boolean usaParagrafoSize = true; //@todo eventualmente potrebbe essere passato come parametro
//        int size = 0;
//        String titoloParagrafo = "";
//
//        if (mappa != null) {
//            for (String keyUno : mappa.keySet()) {
//                titoloParagrafo = keyUno;
//                mappaParagrafo = mappa.get(keyUno);
//
//                if (usaParagrafoSize) {
//                    size = mappaParagrafo.get("").size();
//                    titoloParagrafo += " <small><small>(" + size + ")</small></small>";
//                }// end of if cycle
//
//                testo.append(PARAGRAFO).append(titoloParagrafo).append(PARAGRAFO);
//                testo.append(contenutoParagrafoRaggruppato(mappaParagrafo));
//                testo.append(A_CAPO);
//            }// end of for cycle
//        }// end of if cycle
//
//        return testo.toString();
//    }// end of method


//    /**
//     * Righe suddivise per paragrafi <br>
//     * All'interno dei paragrafi usa righeSemplici <br>
//     */
//    public String paragrafoSottopaginato(LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappa, String rinvio, String sottoTitolo, int soglia) {
//        StringBuilder testo = new StringBuilder(VUOTA);
//        LinkedHashMap<String, List<String>> mappaParagrafo;
//        Object[] arraySet = null;
//        int numSet = 0;
//        int numVoci = 0;
//
//        if (mappa != null) {
//            for (String keyUno : mappa.keySet()) {
//                testo.append(PARAGRAFO).append(keyUno).append(PARAGRAFO);
//                mappaParagrafo = mappa.get(keyUno);
//
//                //--controllo sottoparagrafo
//                arraySet = mappaParagrafo.keySet().toArray();
//                if (arraySet.length == 1) {
//                    numVoci = mappaParagrafo.get(arraySet[0]).size();
//                }// end of if cycle
//
//                if (numVoci > soglia) {
//                    testo.append(A_CAPO);
//                    testo.append("{{Vedi anche|");
//                    testo.append(rinvio);
//                    testo.append("/");
//                    testo.append(sottoTitolo);
//                    testo.append("}}");
//                    testo.append(A_CAPO);
//                    //lancia sottopagina
//                } else {
//                    testo.append(contenutoParagrafoSemplice(mappaParagrafo));
//                }// end of if/else cycle
//
//                testo.append(A_CAPO);
//            }// end of for cycle
//        }// end of if cycle
//
//        return testo.toString();
//    }// end of method


//    /**
//     * Righe suddivise per paragrafi <br>
//     * All'interno dei paragrafi usa righe righeRaggruppate <br>
//     */
//    public String paragrafoBase(LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappa, boolean usaParagrafoSize) {
//        StringBuilder testo = new StringBuilder(VUOTA);
//        LinkedHashMap<String, List<String>> mappaParagrafo;
//        int size = 0;
//        String titoloParagrafo = "";
//
//        if (mappa != null) {
//            for (String keyUno : mappa.keySet()) {
//                titoloParagrafo = keyUno;
//                titoloParagrafo = LibWiki.setQuadre(titoloParagrafo);
//                mappaParagrafo = mappa.get(keyUno);
//
//                if (usaParagrafoSize) {
//                    size = mappaParagrafo.size();
//                    titoloParagrafo += " <small><small>(" + size + ")</small></small>";
//                }// end of if cycle
//
//                testo.append(PARAGRAFO).append(titoloParagrafo).append(PARAGRAFO);
//                testo.append(contenutoParagrafoRaggruppato(mappaParagrafo));
//                testo.append(A_CAPO);
//            }// end of for cycle
//        }// end of if cycle
//
//        return testo.toString();
//    }// end of method


//    /**
//     * Righe suddivise per paragrafi <br>
//     * All'interno dei paragrafi usa righe righeRaggruppate <br>
//     */
//    public String paragrafoSenzaSize(LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappa) {
//        return paragrafoBase(mappa, false);
//    }// end of method


//    /**
//     * Righe suddivise per paragrafi <br>
//     * All'interno dei paragrafi usa righe righeRaggruppate <br>
//     */
//    public String paragrafoConSize(LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappa) {
//        return paragrafoBase(mappa, true);
//    }// end of method


//    /**
//     * Righe suddivise per paragrafi <br>
//     * All'interno dei paragrafi usa righeRaggruppate <br>
//     */
//    public String contenutoParagrafoRaggruppato(LinkedHashMap<String, List<String>> mappaParagrafo) {
//        StringBuilder testo = new StringBuilder(VUOTA);
//        List<String> listaDidascalie = null;
//
//        if (mappaParagrafo != null) {
//            for (String keyDue : mappaParagrafo.keySet()) {
//                listaDidascalie = mappaParagrafo.get(keyDue);
//
//                if (array.isValid(listaDidascalie)) {
//                    if (listaDidascalie.size() == 1) {
//                        testo.append(A_CAPO);
//                        testo.append(AST);
//                        if (text.isValid(keyDue)) {
//                            testo.append(LibWiki.setQuadre(keyDue));
//                            testo.append(WikiCost.TAG_SEP);
//                        }// end of if cycle
//                        testo.append(listaDidascalie.get(0));
//                    } else {
//                        if (text.isValid(keyDue)) {
//                            testo.append(A_CAPO);
//                            testo.append(AST);
//                            testo.append(LibWiki.setQuadre(keyDue));
//                            for (String stringa : listaDidascalie) {
//                                testo.append(A_CAPO);
//                                testo.append(AST);
//                                testo.append(AST);
//                                testo.append(stringa);
//                            }// end of for cycle
//                        } else {
//                            for (String stringa : listaDidascalie) {
//                                testo.append(A_CAPO);
//                                testo.append(AST);
//                                testo.append(stringa);
//                            }// end of for cycle
//                        }// end of if/else cycle
//
//                    }// end of if/else cycle
//
//                }// end of if cycle
//
//            }// end of for cycle
//
//        }// end of if cycle
//        testo.append(A_CAPO);
//
//        return testo.toString();
//    }// end of method


//    /**
//     * Righe singole senza nessun raggruppamento
//     */
//    public String righeSemplici(LinkedHashMap<String, List<String>> mappaDidascalie) {
//        StringBuilder testo = new StringBuilder(VUOTA);
//        List<String> listaDidascalie;
//
//        if (mappaDidascalie != null) {
//            for (String key : mappaDidascalie.keySet()) {
//                listaDidascalie = mappaDidascalie.get(key);
//
//                if (text.isValid(key)) {
//                    for (String didascalia : listaDidascalie) {
//                        testo.append(ASTERISCO).append(key).append(TAG_SEP).append(didascalia).append(A_CAPO);
//                    }// end of for cycle
//                } else {
//                    for (String didascalia : listaDidascalie) {
//                        testo.append(ASTERISCO).append(didascalia).append(A_CAPO);
//                    }// end of for cycle
//                }// end of if/else cycle
//
//            }// end of for cycle
//        }// end of if cycle
//
//        return testo.toString();
//    }// fine del metodo


//    /**
//     * Righe raggruppate per anno/giorno
//     */
//    public String senzaParagrafi(LinkedHashMap<String, List<String>> mappaDidascalie) {
//        StringBuilder testo = new StringBuilder(VUOTA);
//        List<String> listaDidascalie;
//
//        if (mappaDidascalie != null) {
//            for (String key : mappaDidascalie.keySet()) {
//                listaDidascalie = mappaDidascalie.get(key);
//
//                if (listaDidascalie.size() == 1) {
//                    testo.append(ASTERISCO).append(text.isValid(key) ? key + TAG_SEP : SPAZIO).append(listaDidascalie.get(0)).append(A_CAPO);
//                } else {
//                    if (text.isValid(key)) {
//                        testo.append(ASTERISCO).append(key).append(A_CAPO);
//                        for (String didascalia : listaDidascalie) {
//                            testo.append(ASTERISCO + ASTERISCO).append(didascalia).append(A_CAPO);
//                        }// end of if/else cycle
//                    } else {
//                        for (String didascalia : listaDidascalie) {
//                            testo.append(ASTERISCO).append(didascalia).append(A_CAPO);
//                        }// end of if/else cycle
//                    }// end of if/else cycle
//                }// end of for cycle
//            }// end of for cycle
//        }// end of if cycle
//
//        return testo.toString();
//    }// fine del metodo


    @Deprecated
    public int getMappaSize(LinkedHashMap<String, List<String>> mappaGenerale) {
        int numVoci = 0;

        for (List<String> lista : mappaGenerale.values()) {
            numVoci += lista.size();
        }// end of for cycle

        return numVoci;
    }// end of method


//    @Deprecated
//    public int getMappaDueSize(LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> mappaGenerale) {
//        int numVoci = 0;
//
//        for (LinkedHashMap<String, ArrayList<String>> mappaChiaveDue : mappaGenerale.values()) {
//            for (ArrayList<String> lista : mappaChiaveDue.values()) {
//                numVoci += lista.size();
//            }// end of for cycle
//        }// end of for cycle
//
//        return numVoci;
//    }// end of method


//    /**
//     * La mappa delle biografie arriva non ordinata
//     * Occorre spostare in basso il paragrafo vuoto
//     */
//    protected void fixPosizioneParagrafoVuoto(LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> mappaGenerale, String titoloParagrafoVuoto) {
//        LinkedHashMap<String, ArrayList<String>> mappaChiaveDue;
//
//        if (mappaGenerale == null) {
//            return;
//        }// end of if cycle
//
//        if (mappaGenerale.containsKey(titoloParagrafoVuoto)) {
//            mappaChiaveDue = mappaGenerale.get(titoloParagrafoVuoto);
//            mappaGenerale.remove(titoloParagrafoVuoto);
//            mappaGenerale.put(titoloParagrafoVuoto, mappaChiaveDue);
//        }// end of if cycle
//
//    }// fine del metodo

}// end of class
