package it.algos.vaadflow.service;

import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.*;

/**
 * Project springvaadin
 * Created by Algos
 * User: gac
 * Date: gio, 07-dic-2017
 * Time: 13:46
 * <p>
 * Classe di Libreria - Utility per la gestione degli array <br>
 * <p>
 * Annotated with @SpringComponent (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * Annotated with @Scope (obbligatorio = 'singleton') <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 * <p>
 * Due possibilità d'uso: <br>
 * 1) l'istanza singleton può essere iniettata da SpringBoot in automatico (se all'interno della catena di @Autowired) <br>
 * 2) l'istanza singleton può essere recuperata col metodo statico getInstance() (se creata con new AArrayService) <br>
 * <p>
 * Testata in AArrayServiceTest
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class AArrayService {

    /**
     * Private final property
     */
    private static final AArrayService INSTANCE = new AArrayService();

    /**
     * Service (@Scope = 'singleton') recuperato come istanza dalla classe <br>
     * The class MUST be an instance of Singleton Class and is created at the time of class loading <br>
     */
    public ATextService text = ATextService.getInstance();

    /**
     * Private constructor to avoid client applications to use constructor
     */
    private AArrayService() {
    }// end of constructor

    /**
     * Gets the unique instance of this Singleton.
     *
     * @return the unique instance of this Singleton
     */
    public static AArrayService getInstance() {
        return INSTANCE;
    }// end of static method

    /**
     * Ordina la lista
     * L'ordinamento funziona SOLO se la lista è omogenea (oggetti della stessa classe)
     *
     * @param listaDisordinata in ingresso
     *
     * @return lista ordinata, null se listaDisordinata è null
     */
    public ArrayList sort(ArrayList listaDisordinata) {
        ArrayList<Object> objList;
        Object[] objArray = listaDisordinata.toArray();

        try { // prova ad eseguire il codice
            Arrays.sort(objArray);
        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch
        objList = fromObj(objArray);

        return objList;
    }// end of method

    /**
     * Controlla la validità dell'array
     * Deve esistere (not null)
     * Deve avere degli elementi (size > 0)
     * Il primo elemento deve essere valido
     *
     * @param array (List) in ingresso da controllare
     *
     * @return vero se l'array soddisfa le condizioni previste
     */
    public boolean isValid(final List array) {
        boolean status = false;

        if (array != null && array.size() > 0) {
            if (array.get(0) != null) {
                if (array.get(0) instanceof String) {
                    status = text.isValid(array.get(0));
                } else {
                    status = true;
                }// end of if/else cycle
            }// end of if cycle
        }// end of if cycle

        return status;
    }// end of method

    /**
     * Controlla la validità dell'array
     * Deve esistere (not null)
     * Deve avere degli elementi (length > 0)
     * Il primo elemento deve essere una stringa valida
     *
     * @param array (String[]) in ingresso da controllare
     *
     * @return vero se l'array soddisfa le condizioni previste
     */
    public boolean isValid(final String[] array) {
        boolean status = false;

        if (array != null && array.length > 0) {
            if (array[0] != null) {
                if (array[0] instanceof String) {
                    status = text.isValid((String) array[0]);
                } else {
                    status = true;
                }// end of if/else cycle
            }// end of if cycle
        }// end of if cycle

        return status;
    }// end of method

    /**
     * Controlla che l'array sia nullo o vuoto
     * Non deve esistere (null)
     * Se esiste, non deve avere elementi (size = 0)
     *
     * @param array (List) in ingresso da controllare
     *
     * @return vero se l'array soddisfa le condizioni previste
     */
    public boolean isEmpty(final List array) {
        return !isValid(array);
    }// end of method

    /**
     * Controlla che l'array sia nullo o vuoto
     * Non deve esistere (null)
     * Se esiste, non deve avere elementi (size = 0)
     *
     * @param array (List) in ingresso da controllare
     *
     * @return vero se l'array soddisfa le condizioni previste
     */
    public boolean isEmpty(final String[] array) {
        return !isValid(array);
    }// end of method

    /**
     * Aggiunge un elemento ad una List (di per se immutabile)
     * Deve esistere (not null)
     *
     * @param arrayIn (List) ingresso da incrementare
     *
     * @return la lista aumentata di un elemento
     */
    public List add(final List arrayIn, Object obj) {
        List arrayOut = null;
        ArrayList lista = null;

        if (this.isValid(arrayIn)) {
            lista = new ArrayList(arrayIn);
            lista.add(obj);
            arrayOut = lista;
        }// end of if cycle

        return arrayOut;
    }// end of method

    /**
     * Somma due array (liste) e restituisce una lista ordinata
     * <p>
     * Almeno uno dei due array in ingresso deve essere non nullo
     * Normalmente si usa di più la somma ordinata
     * I valori negli array sono unici
     * <p>
     * Se entrambi i parametri sono nulli, restituisce un nullo
     * Se uno dei parametri è nullo, restituisce l'altro
     * La lista di valori in uscita è unica (quindi la dimensione può essere minore dalla somma delle due)
     *
     * @param arrayPrimo   - prima lista
     * @param arraySecondo - seconda lista
     *
     * @return arraySomma ordinata
     */
    public ArrayList somma(ArrayList arrayPrimo, ArrayList arraySecondo) {
        ArrayList arraySomma = sommaDisordinata(arrayPrimo, arraySecondo);

        if (arraySomma != null) {
            arraySomma = sort(arraySomma);
        }// fine del blocco if

        return arraySomma;
    }// end of method


    /**
     * Somma due array (liste) e restituisce una lista NON ordinata
     * <p>
     * Almeno uno dei due array in ingresso deve essere non nullo
     * Normalmente si usa di meno la somma disordinata
     * I valori negli array sono unici
     * <p>
     * Se entrambi i parametri sono nulli, restituisce un nullo
     * Se uno dei parametri è nullo, restituisce l'altro
     * La lista di valori in uscita è unica (quindi la dimensione può essere minore dalla somma delle due)
     *
     * @param arrayPrimo   - prima lista
     * @param arraySecondo - seconda lista
     *
     * @return arraySomma disordinata
     */
    public ArrayList sommaDisordinata(ArrayList arrayPrimo, ArrayList arraySecondo) {
        ArrayList arraySomma = null;

        if (arrayPrimo != null && arraySecondo != null) {
            arraySomma = new ArrayList();
            for (Object ogg : arrayPrimo) {
                arraySomma.add(ogg);
            } // fine del ciclo for-each
            for (Object ogg : arraySecondo) {
                arraySomma.add(ogg);
            } // fine del ciclo for-each
            arraySomma = valoriUniciBase(arraySomma, false);
        }// fine del blocco if

        if (arrayPrimo == null) {
            arraySecondo = valoriUniciBase(arraySecondo, false);
            return arraySecondo;
        }// fine del blocco if

        if (arraySecondo == null) {
            arrayPrimo = valoriUniciBase(arrayPrimo, false);
            return arrayPrimo;
        }// fine del blocco if

        return arraySomma;
    }// end of method


    /**
     * Estrae i valori unici da un lista con (eventuali) valori doppi
     * Eventualmente (tag booleano) ordina l'array secondo la classe utilizzata:
     * alfabetico per le stringhe
     * numerico per i numeri
     *
     * @param listaValoriDoppi in ingresso
     * @param ordina           tag per forzare l'ordinamento
     *
     * @return valoriUnici disordinati oppure ordinati, null se listaValoriDoppi è null
     */
    @SuppressWarnings("all")
    private ArrayList valoriUniciBase(ArrayList listaValoriDoppi, boolean ordina) {
        ArrayList listaValoriUniciNonOrdinati = null;
        Set set;

        if (listaValoriDoppi != null) {
            set = new LinkedHashSet((List) listaValoriDoppi);
            listaValoriUniciNonOrdinati = new ArrayList(set);
            if (ordina) {
                return sort(listaValoriUniciNonOrdinati);
            } else {
                return listaValoriUniciNonOrdinati;
            }// fine del blocco if-else
        }// fine del blocco if

        return null;
    }// end of method


    /**
     * Convert a objArray to ArrayList
     *
     * @param objArray to convert
     *
     * @return the corresponding casted ArrayList
     */
    public ArrayList<Object> fromObj(Object[] objArray) {
        ArrayList<Object> objList = new ArrayList<Object>();

        for (Object lungo : objArray) {
            objList.add(lungo);
        } // fine del ciclo for-each

        return objList;
    }// end of method


    /**
     * Costruisce una stringa con i singoli valori divisi da un pipe
     * <p>
     *
     * @param array lista di valori
     *
     * @return stringa con i singoli valori divisi da un separatore
     */
    public String toStringaPipe(List array) {
        return toStringa(array, "|");
    }// end of method


    /**
     * Costruisce una stringa con i singoli valori divisi da un separatore
     * <p>
     *
     * @param array lista di valori
     * @param sep   carattere separatore
     *
     * @return stringa con i singoli valori divisi da un separatore
     */
    public String toStringa(List array, String sep) {
        String testo;
        StringBuilder textBuffer = new StringBuilder();

        for (Object obj : array) {
            textBuffer.append(obj.toString());
            textBuffer.append(sep);
        } // fine del ciclo for-each
        testo = textBuffer.toString();
        testo = text.levaCoda(testo, sep);

        return testo;
    }// end of method


    public ArrayList<Long> fromLong(long[] longArray) {
        ArrayList<Long> longList = new ArrayList();
        long[] var2 = longArray;
        int var3 = longArray.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            Long lungo = var2[var4];
            longList.add(lungo);
        }

        return longList;
    }// end of method

    /**
     * Numero di cicli
     *
     * @param totale da dividere
     * @param blocco divisore
     *
     * @return numero di cicli
     */
    public int numCicli(int totale, int blocco) {
        int cicli = 0;
        int resto;

        if (blocco < 0) {
            return 0;
        }// end of if cycle

        if (blocco == 0) {
            return totale;
        }// end of if cycle

        if (blocco > totale) {
            return 1;
        }// end of if cycle

        cicli = totale / blocco;
        resto = totale % blocco;
        if (resto > 0) {
            cicli++;
        }// end of if cycle

        return cicli;
    }// end of method


    /**
     * Estra un subset dalla lista
     *
     * @param listaTotale   da suddividere
     * @param dimBlocco     di suddivisione
     * @param cicloCorrente attuale - inizia da 1 (non da zero)
     *
     * @return sublista corrente del ciclo
     */
    public List estraeSublista(List listaTotale, int dimBlocco, int cicloCorrente) {
        int posIni = 0;
        int posEnd = 0;

        if (listaTotale == null || listaTotale.size() < 1 || cicloCorrente == 0) {

            if (cicloCorrente == 0) {
                log.warn("Algos - AArrayService.estraeSubLista(), cicloCorrente=0");
            } else {
                log.warn("Algos - AArrayService.estraeSubLista(), listaTotale non valida");
            }// end of if/else cycle

            return null;
        }// end of if cycle

        posIni = dimBlocco * (cicloCorrente - 1);
        posEnd = posIni + dimBlocco;
        posEnd = Math.min(posEnd, listaTotale.size());

        return listaTotale.subList(posIni, posEnd);
    }// end of method


    /**
     * Estra un subset dalla lista
     *
     * @param listaTotale   da suddividere
     * @param dimBlocco     di suddivisione
     * @param cicloCorrente attuale
     *
     * @return sublista corrente del ciclo
     */
    public List<Long> estraeSublistaLong(List<Long> listaTotale, int dimBlocco, int cicloCorrente) {
        return estraeSublista(listaTotale, dimBlocco, cicloCorrente);
    }// end of method


    /**
     * Posizione di un valore nella lista
     *
     * @param lista da esaminare
     * @param value di trovare
     *
     * @return posizione
     */
    public int getPos(List lista, Object value) {
        int pos = 0;

        if (isValid(lista)) {
            for (Object obj : lista) {
                pos++;
                if (obj.equals(value)) {
                    return pos;
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return pos;
    }// end of method

}// end of singleton class
