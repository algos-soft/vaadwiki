package it.algos.vaadflow14.backend.service;

import com.vaadin.flow.component.grid.Grid;
import it.algos.vaadflow14.backend.functional.APredicate;
import org.jsoup.internal.StringUtil;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.algos.vaadflow14.backend.application.FlowCost.*;

/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 20-set-2019 18.19.24 <br>
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AAbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AAnnotationService.class); <br>
 * 3) @Autowired private AArrayService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AArrayService extends AAbstractService {


    /**
     * Controlla la validità di un array (lista). <br>
     * Deve esistere (not null) <br>
     * Deve avere degli elementi (size maggiore di 0) <br>
     * Non ci devono essere elementi con valore null <br>
     * Non sono accettate le stringhe vuote -> "" <br>
     *
     * @param array (List) in ingresso da controllare
     *
     * @return vero se l'array soddisfa le condizioni previste
     *
     * @since Java 11
     */
    public boolean isAllValid(final List array) {
        return array != null && array.size() > 0 && array.stream().filter(APredicate.nonValido).count() == 0;
    }


    /**
     * Controlla la validità di una matrice. <br>
     * Deve esistere (not null) <br>
     * Deve avere degli elementi (length maggiore di 0) <br>
     * Non ci devono essere elementi con valore null <br>
     * Non sono accettate le stringhe vuote -> "" <br>
     *
     * @param matrice (String[]) in ingresso da controllare
     *
     * @return vero se la matrice soddisfa le condizioni previste
     *
     * @since Java 11
     */
    public boolean isAllValid(final String[] matrice) {
        return matrice != null && matrice.length > 0 && Arrays.asList(matrice).stream().filter(APredicate.nonValido).count() == 0;
    }


    /**
     * Controlla la validità di una mappa. <br>
     * Deve esistere (not null) <br>
     * Deve avere degli elementi (size maggiore di 0) <br>
     * Non ci devono essere chiavi con valore null <br>
     * Non sono accettate le chiavi vuote -> "" <br>
     *
     * @param mappa (Map) in ingresso da controllare
     *
     * @return vero se la mappa soddisfa le condizioni previste
     *
     * @since Java 11
     */
    public boolean isAllValid(final Map mappa) {
        return mappa != null && mappa.size() > 0 && mappa.keySet().stream().filter(APredicate.nonValido).count() == 0;
    }


    /**
     * Controlla che l'array sia nullo o vuoto <br>
     * Non deve esistere (null) <br>
     * Se esiste, non deve avere elementi (size = 0) <br>
     * Se ci sono elementi devono avere tutti valore null <br>
     *
     * @param array (List) in ingresso da controllare
     *
     * @return vero se l'array soddisfa le condizioni previste
     *
     * @since Java 11
     */
    public boolean isEmpty(final List array) {
        return array == null || array.size() == 0 || array.stream().filter(APredicate.valido).count() == 0;
    }


    /**
     * Controlla che la matrice sia nulla o vuota. <br>
     * Non deve esistere (null) <br>
     * Se esiste, non deve avere elementi (size = 0) <br>
     * Se ci sono elementi devono avere tutti valore null o vuoto -> "" <br>
     *
     * @param matrice (String[]) in ingresso da controllare
     *
     * @return vero se la matrice soddisfa le condizioni previste
     *
     * @since Java 11
     */
    public boolean isEmpty(final String[] matrice) {
        return matrice == null || matrice.length == 0 || Arrays.asList(matrice).stream().filter(APredicate.valido).count() == 0;
    }


    /**
     * Controlla che la mappa sia nulla o vuota. <br>
     * Non deve esistere (null) <br>
     * Se esiste, non deve avere elementi (size = 0) <br>
     * Se ci sono elementi devono avere tutti la chiave null o vuota -> "" <br>
     *
     * @param mappa (Map) in ingresso da controllare
     *
     * @return vero se la mappa soddisfa le condizioni previste
     *
     * @since Java 11
     */
    public boolean isEmpty(final Map mappa) {
        return mappa == null || mappa.size() == 0 || mappa.keySet().stream().filter(APredicate.valido).count() == 0;
    }


    /**
     * route costruisce uan view che implementa l'interfaccia HasUrlParameter <br>
     * e nel metodo setParameter() riceve @OptionalParameter parameters che <br>
     * sono del tipo Map<String, List<String>> <br>
     * Se tutte le keys delle liste hanno un solo valore,
     * si può semplificare in Map<String, String> <br>
     * <p>
     * Semplifica la mappa (se è semplificabile) <br>
     *
     * @param multiParametersMap mappa di liste di stringhe
     *
     * @return mappa semplificata
     */
    public Map<String, String> semplificaMappa(final Map<String, List<String>> multiParametersMap) {
        Map<String, String> mappaSemplice = new HashMap<>();

        if (!isMappaSemplificabile(multiParametersMap)) {
            return null;
        }

        //@todo Funzionalità ancora da implementare in Java11
        //        Object alfa = multiParametersMap
        //                .entrySet()
        //                .stream()
        ////                .filter(n -> n.size() > 1)
        //                .map(getValue -> AFunction.riduce)
        //        .collect(Collectors.toUnmodifiableList());
        ////                .collect(Collectors.toMap(getKey, Map.Entry::getValue);
        //        Object beta = alfa;
        for (Map.Entry<String, List<String>> entry : multiParametersMap.entrySet()) {
            if (entry.getValue().size() == 1) {
                mappaSemplice.put(entry.getKey(), entry.getValue().get(0));
            }
            else {
                //@todo Linea di codice provvisoriamente commentata e DA RIMETTERE
                //                log.error("Qualcosa non ha funzionato");
            }
        }

        return mappaSemplice;
    }

    /**
     * Controlla se una mappa può essere semplificata. <br>
     * <p>
     * Esamina una mappa del tipo Map<String, List<String>> e se tutte <br>
     * le liste hanno un solo valore, semplifica in Map<String, String> <br>
     * <p>
     * La mappa deve essere valida <br>
     * Deve esistere (not null) <br>
     * Deve avere degli elementi (size maggiore di 0) <br>
     * Ogni lista deve avere un solo valore <br>
     *
     * @param multiParametersMap mappa di liste di stringhe
     *
     * @return true se è semplificabile sostituendo le liste con un singole valore
     *
     * @since Java 11
     */
    public boolean isMappaSemplificabile(Map<String, List<String>> multiParametersMap) {
        return multiParametersMap != null && multiParametersMap.size() > 0 && multiParametersMap.values().stream().filter(n -> n.size() > 1).count() == 0;
    }


    /**
     * Convert una matrice (Object[]) in una array (List<Object>) <br>
     *
     * @param matrice to convert
     *
     * @return the corresponding ist<Object>
     *
     * @since Java 9
     */
    @Deprecated(since = "Java 9")
    public List<Object> fromObj(final Object[] matrice) {
        return List.of(matrice);
    }


    /**
     * Convert una matrice (String[]) in una array (List<String>) <br>
     *
     * @param matrice to convert
     *
     * @return the corresponding ist<String>
     *
     * @since Java 9
     */
    @Deprecated(since = "Java 9")
    public List<String> fromStr(final String[] matrice) {
        return List.of(matrice);
    }


    /**
     * Crea un array di stringhe con singolo valore <br>
     *
     * @param singoloValore da inserire
     *
     * @return array di un solo elemento oppure vuoto
     *
     * @since Java 9
     */
    public List<String> creaArraySingolo(final String singoloValore) {
        return text.isValid(singoloValore) ? List.of(singoloValore) : List.of();
    }


    /**
     * Crea una mappa con una singola coppia chiave-valore <br>
     * Entrambe le stringhe devono essere valide <br>
     *
     * @param key   della mappa
     * @param value della mappa
     *
     * @return mappa chiave-valore con un solo elemento
     *
     * @since Java 9
     */
    public Map<String, String> creaMappaSingola(final String key, final String value) {
        return (text.isValid(key) && text.isValid(value)) ? Map.of(key, value) : Map.of();
    }


    /**
     * Crea una stringa con i valori dell'array separati da virgola + spazio <br>
     *
     * @param array lista di valori
     *
     * @return stringa con i singoli valori separati da virgola + spazio
     *
     * @since Java 9
     */
    public String toStringa(final List array) {
        return toStringaBase(array, VIRGOLA_SPAZIO);
    }

    /**
     * Crea una stringa con i valori dell'array separati da virgola <br>
     *
     * @param array lista di valori
     *
     * @return stringa con i singoli valori separati da virgola
     *
     * @since Java 9
     */
    public String toStringaVirgola(final List array) {
        return toStringaBase(array, VIRGOLA);
    }

    /**
     * Costruisce una stringa con i singoli valori separati da un pipe <br>
     *
     * @param array lista di valori
     *
     * @return stringa con i singoli valori separati da pipe
     */
    public String toStringaPipe(final List array) {
        return toStringaBase(array, PIPE);
    }


    /**
     * Crea una stringa con i valori dell'array separati da un separatore <br>
     *
     * @param array lista di valori
     * @param sep   caratteri separatore
     *
     * @return stringa con i singoli valori separati da separatore
     *
     * @since Java 9
     */
    private String toStringaBase(final List array, final String sep) {
        return isAllValid(array) ? String.join(sep, array) : null;
    }


    /**
     * Costruisce una array da una stringa di valori separati da virgola <br>
     *
     * @param testo da esaminare
     *
     * @return array list di un solo elemento
     *
     * @since Java 9
     */
    public List<String> fromStringa(String testo) {
        if (StringUtil.isBlank(testo)) {
            return List.of();
        }
        else {
            return Arrays.asList(testo.split(VIRGOLA))
                    .stream()
                    .map(n -> n.trim())
                    .collect(Collectors.toList());
        }
    }


    /**
     * Ordina la lista <br>
     * L'ordinamento funziona SOLO se la lista è omogenea (oggetti della stessa classe) <br>
     *
     * @param listaDisordinata in ingresso
     *
     * @return lista ordinata, null se listaDisordinata è null
     */
    public List sort(List listaDisordinata) {
        List<Object> objList;
        Object[] objArray = listaDisordinata.toArray();

        try {
            Arrays.sort(objArray);
        } catch (Exception unErrore) {
        }
        objList = fromObj(objArray);

        return objList;
    }


    /**
     * Costruisce una matrice di colonne della grid <br>
     *
     * @param grid da esaminare
     *
     * @return matrice
     */
    @Deprecated
    public Grid.Column[] getColumnArray(Grid grid) {
        int k=0;
        List<Grid.Column> lista = grid.getColumns();
        Grid.Column[] matrice = new Grid.Column[lista.size()];

        for (Grid.Column column : lista) {
            matrice[k] = column;
            k++;
        }

        return matrice;
    }

}