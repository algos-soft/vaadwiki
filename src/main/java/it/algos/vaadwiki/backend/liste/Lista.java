package it.algos.vaadwiki.backend.liste;

import it.algos.vaadwiki.backend.packages.bio.*;

import javax.annotation.*;
import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 04-gen-2022
 * Time: 18:43
 * <p>
 * Classe specializzata nella creazione di liste. <br>
 * <p>
 * Liste cronologiche (in namespace principale) di nati e morti nel giorno o nell'anno <br>
 * Liste di nomi e cognomi (in namespace principale). <br>
 * Liste di attività e nazionalità (in Progetto:Biografie). <br>
 * <p>
 * Sovrascritta nelle sottoclassi concrete <br>
 * Not annotated with @SpringComponent (sbagliato) perché è una classe astratta <br>
 */
public abstract class Lista {

    /**
     * Mappa delle didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiaveUno (ordinata) che corrisponde al titolo del paragrafo <br>
     * La visualizzazione dei paragrafi può anche essere esclusa, ma questi sono comunque presenti <br>
     * Ogni valore della mappa è costituito da una ulteriore LinkedHashMap <br>
     * Questa mappa è composta da una chiaveDue e da una ulteriore LinkedHashMap <br>
     * Questa mappa è composta da una chiaveTre e da un ArrayList di didascalie (testo) <br>
     * La chiaveUno è un secolo, un mese, un'attività (a seconda del tipo di didascalia) <br>
     * La chiaveUno è un link a pagina di wikipedia (escluso titoloParagrafoVuoto) con doppie quadre <br>
     * La chiaveDue è una lettera alfabetica <br>
     * La chiaveTre è una doppia lettera alfabetica <br>
     */
    public Map<String, List> mappa;

    protected List<Bio> listaBiografie;

    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     * Se hanno la stessa firma, chiama prima @PostConstruct della sottoclasse <br>
     * Se hanno firme diverse, chiama prima @PostConstruct della superclasse <br>
     */
    @PostConstruct
    public void inizia() {
        this.fixPreferenze();
        this.creaListaBiografie();
    }

    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
    }

    /**
     * Costruisce una lista di biografie che hanno una valore valido per la pagina specifica <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void creaListaBiografie() {
        this.listaBiografie = getListaBio();
    }


    /**
     * Costruisce una lista di biografie che hanno una valore valido per la pagina specifica <br>
     * Deve essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @return lista delle istanze di Bio che usano questo istanza nella property appropriata <br>
     */
    protected List<Bio> getListaBio() {
        return new ArrayList<>();
    }

    /**
     * Costruisce una lista di didascalie che hanno una valore valido per la pagina specifica <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void creaMappaDidascalie() {
        //--Crea la lista grezza delle voci biografiche
        this.mappa = new LinkedHashMap<>();

        //--Crea una lista di didascalie specifiche
        //        listaDidascalie = listaService.creaListaDidascalie(listaGrezzaBio, typeDidascalia);

    }

}
