package it.algos.wiki;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by gac on 04 ago 2015.
 * .
 */
public abstract class VaadWiki {

    private static final String TAG_INI = "[[";
    private static final String TAG_END = "]]";

    /**
     * Tries to convert an Object in int.
     *
     * @param obj to convert
     * @return the corresponding int
     */
    public static int getInt(Object obj) {
        int intero = 0;

        if (obj == null) {
            return 0;
        }// fine del blocco if

        if (obj instanceof Number) {
            Number number = (Number) obj;
            intero = number.intValue();
            return intero;
        }// fine del blocco if

        if (obj instanceof String) {
            String string = (String) obj;
            try { // prova ad eseguire il codice
                intero = Integer.parseInt(string);
            } catch (Exception unErrore) { // intercetta l'errore
            }// fine del blocco try-catch
            return intero;
        }// fine del blocco if

        return 0;
    }// end of static method

    /**
     * Estrae da un testo una serie di occorrenze comprese tra due estremi
     * Estremi compresi
     *
     * @param testo  da analizzare
     * @param tagIni iniziale
     * @param tagEnd finale
     * @return la lista di valori
     */
    public static ArrayList<String> estrae(String testo, String tagIni, String tagEnd) {
        ArrayList<String> lista = null;
        int posIni = 0;
        int posEnd = 0;
        String parte;

        if (testo != null && testo.contains(tagIni) && testo.contains(tagEnd)) {
            lista = new ArrayList<String>();
            do {
                posIni = testo.indexOf(tagIni, posIni);
                posEnd = testo.indexOf(tagEnd, posIni + tagIni.length());
                parte = testo.substring(posIni, posEnd + tagEnd.length());
                lista.add(parte);
                posIni = testo.indexOf(tagIni, posEnd);
            } while (posIni > -1); // fine del blocco do
        }// fine del blocco if

        return lista;
    }// end of static method

    /**
     * Estrae da un testo una serie di occorrenze comprese tra due estremi
     * Estremi esclusi
     *
     * @param testo  da analizzare
     * @param tagIni iniziale
     * @param tagEnd finale
     * @return la lista di valori
     */
    public static ArrayList<String> estraeEsclusi(String testo, String tagIni, String tagEnd) {
        ArrayList<String> listaEsclusi = null;
        ArrayList<String> listaCompresi = estrae(testo, tagIni, tagEnd);

        if (listaCompresi != null) {
            listaEsclusi = new ArrayList<String>();
            for (String stringa : listaCompresi) {
//                stringa = LibText.levaTesta(stringa, tagIni);
//                stringa = LibText.levaCoda(stringa, tagEnd);
                listaEsclusi.add(stringa);
            } // fine del ciclo for-each
        }// fine del blocco if

        return listaEsclusi;
    }// end of static method

    /**
     * Estrae da una stringa la parte del link (dopo il pipe)
     * Prevede che la stringa inizi e finisca con le doppie quadre
     * Prevede che la stringa contenga il tag pipe
     *
     * @param stringa di testo da analizzare
     * @return stringa con solo il link
     */
    public static String estraeSingoloLink(String stringa) {
        String link = "";
        String tagPipe = "|";

        if (stringa != null) {
            link = stringa.trim();
        }// fine del blocco if

        if (link.startsWith(TAG_INI) && link.endsWith(TAG_END)) {
            if (link.contains(tagPipe)) {
                link = link.substring(link.indexOf(tagPipe) + 1, link.length() - 2);
            } else {
//                link = LibText.levaTesta(link, TAG_INI);
//                link = LibText.levaCoda(link, TAG_END);
            }// fine del blocco if-else
        }// fine del blocco if

        return link;
    }// end of static method

    /**
     * Estrae da un testo una serie di occorrenze di doppie quadre
     * Estremi esclusi
     *
     * @param testo da analizzare
     * @return la lista di valori
     */
    public static ArrayList<String> estraeDoppieQuadre(String testo) {
        ArrayList<String> lista = null;
        ArrayList<String> listaGrezza = estrae(testo, TAG_INI, TAG_END);

        if (listaGrezza != null) {
            lista = new ArrayList<String>();
            for (String stringa : listaGrezza) {
                stringa = estraeSingoloLink(stringa);
                lista.add(stringa);
            } // fine del ciclo for-each
        }// fine del blocco if

        return lista;
    }// end of static method

    /**
     * Estrae da un testo la prima occorrenza di doppie quadre
     * Estremi esclusi
     *
     * @param testo da analizzare
     * @return la lista di valori
     */
    public static String estraeDoppiaQuadra(String testo) {
        String quadra = "";
        ArrayList<String> lista = estraeDoppieQuadre(testo);

        if (lista != null && lista.size() > 0) {
            quadra = lista.get(0);
        }// fine del blocco if

        return quadra;
    }// end of static method

    /**
     * Sostituisce tutte le occorrenze di doppie quadre con il link visibile
     *
     * @param testoIn da elaborare
     * @return testo con i soli link visibili e senza doppie qaudre
     */
    public static String sostituisceLink(String testoIn) {
        String testoOut = testoIn;
        ArrayList<String> listaGrezza = estrae(testoIn, TAG_INI, TAG_END);
        String newStringa;

        if (listaGrezza != null) {
            for (String oldStringa : listaGrezza) {
                newStringa = estraeSingoloLink(oldStringa);
//                testoOut = LibText.sostituisce(testoOut, oldStringa, newStringa);
            } // fine del ciclo for-each
        }// fine del blocco if

        return testoOut;
    }// end of static method

    /**
     * Converte una stringa formatta wiki in timestamp.
     *
     * @param stringa da convertire
     * @return la data corrispondente
     */
    public static Timestamp getWikiTime(String stringa) {
        Date data;
        DateFormat formatter;

        data = new Date();

        GregorianCalendar calendario = new GregorianCalendar(0, 0, 0, 0, 0, 0);
        SimpleDateFormat wikiDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        wikiDateFormat.setCalendar(calendario);

        formatter = wikiDateFormat;
        try { // prova ad eseguire il codice
            data = formatter.parse(stringa);
        } catch (ParseException unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        return new Timestamp(data.getTime());
    } // fine del metodo

}// end of static class
