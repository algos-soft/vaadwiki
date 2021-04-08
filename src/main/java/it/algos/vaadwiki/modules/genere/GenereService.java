package it.algos.vaadwiki.modules.genere;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadwiki.modules.wiki.WikiService;
import it.algos.wiki.LibWiki;
import it.algos.wiki.web.AQueryVoce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.A_CAPO;
import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 15-giu-2019 11.00.02 <br>
 * <br>
 * Business class. Layer di collegamento per la Repository. <br>
 * <br>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 * NOT annotated with @VaadinSessionScope (sbagliato, perché SpringBoot va in loop iniziale) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_GEN)
@Slf4j
@AIScript(sovrascrivibile = false)
public class GenereService extends WikiService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public GenereRepository repository;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public GenereService(@Qualifier(TAG_GEN) MongoRepository repository) {
        super(repository);
        super.entityClass = Genere.class;
        this.repository = (GenereRepository) repository;
        super.titoloModulo = titoloModuloGenere;
        super.codeLastDownload = LAST_DOWNLOAD_GENERE;
        super.durataLastDownload = DURATA_DOWNLOAD_GENERE;
    }// end of Spring constructor


    /**
     * Download completo del modulo da Wiki <br>
     * Cancella tutte le precedenti entities <br>
     * Registra su Mongo DB tutte le occorrenze di attività/nazionalità <br>
     */
    public void download() {
        String tagIni = "{";
        String tagEnd = "}";
        String tagVir = ",";
        String tagUgu = "=";
        String tagApi = "\"";
        String tagM = "\"M\"";
        String tagF = "\"F\"";
        long inizio = System.currentTimeMillis();
        String testo = "";
        testo = text.estrae(testo, tagIni, tagEnd);
        String[] righe = null;
        String[] parti;
        String singolare;
        String testoPlurale;
        String pluraleMaschile;
        String pluraleFemminile;
        String message = "";

        //--legge la pagina wiki
        testo = ((AQueryVoce) appContext.getBean("AQueryVoce", titoloModulo)).urlRequest();
        if (text.isValid(testo)) {
            righe = testo.split(A_CAPO);
        }// end of if cycle

        if (array.isValid(righe)) {
            this.deleteAll();
            for (String riga : righe) {
                singolare = "";
                pluraleMaschile = "";
                pluraleFemminile = "";
                riga = text.levaCoda(riga, tagVir);
                parti = riga.split(tagUgu);
                if (array.isValid(parti) && parti.length == 2) {
                    singolare = parti[0].trim();
                    singolare = text.estrae(singolare, tagApi);
                    testoPlurale = parti[1].trim();

                    if (singolare.equals("arbitro di calcio")) {
                        int a = 87;
                    }// end of if cycle

                    if (testoPlurale.contains(tagM) && testoPlurale.contains(tagF)) {
                        pluraleMaschile = text.estrae(testoPlurale, tagApi);
                        pluraleFemminile = estraeFemminile(testoPlurale);
                    }
                    else {
                        if (testoPlurale.contains(tagM)) {
                            pluraleMaschile = text.estrae(testoPlurale, tagApi);
                        }// end of if cycle
                        if (testoPlurale.contains(tagF)) {
                            pluraleFemminile = text.estrae(testoPlurale, tagApi);
                        }// end of if cycle
                    }// end of if/else cycle

                    this.findOrCrea(singolare, pluraleMaschile, pluraleFemminile);
                }// end of if cycle
            }// end of for cycle

            setLastDownload(inizio);
            if (pref.isBool(FlowCost.USA_DEBUG)) {
                message += "Download modulo ";
                message += entityClass.getSimpleName();
                message += " (";
                message += text.format(righe.length);
                message += " elementi in ";
                message += date.deltaText(inizio);
                message += "), con AQueryVoce, senza login, senza cookies, urlRequest di tipo GET";

                logger.debug(message);
            }// end of if cycle
        }
        else {
            logger.error(entityClass.getSimpleName() + " - Qualcosa non ha funzionato");
        }// end of if/else cycle
    }// end of method


    /**
     * Funziona solo per il format: { "avvocati","M", "avvocate","F"}
     */
    public String estraeFemminile(String testoPlurale) {
        String pluraleFemminile = "";
        String plurale = "";
        String tagIni = "{";
        String tagEnd = "}";
        String tagVir = ",";
        String tagUgu = "=";
        String tagApi = "";
        String tagM = "M";
        String tagF = "F";
        String[] parti;
        String tag = "F";

        // Funziona solo per il format: { "avvocati","M", "avvocate","F"}
        plurale = LibWiki.setNoGraffe(testoPlurale);
        parti = plurale.split(tagVir);
        for (int k = 0; k < parti.length; k++) {
            parti[k] = LibWiki.setNoVirgolette(parti[k]);
        }// end of for cycle

        for (int k = 0; k < parti.length; k++) {
            if (parti[k].equals(tag)) {
                if (k > 0) {
                    pluraleFemminile = parti[k - 1];
                }// end of if cycle
            }// end of if cycle
        }// end of for cycle

        return pluraleFemminile;
    }// end of method


    /**
     * Ricerca di una entity (la crea se non la trova) <br>
     *
     * @param singolare        maschile e femminile (obbligatorio ed unico)
     * @param pluraleMaschile  (obbligatorio NON unico)
     * @param pluraleFemminile (obbligatorio NON unico)
     *
     * @return la entity trovata o appena creata
     */
    public Genere findOrCrea(String singolare, String pluraleMaschile, String pluraleFemminile) {
        Genere entity = findByKeyUnica(singolare);

        if (entity == null) {
            entity = crea(singolare, pluraleMaschile, pluraleFemminile);
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Crea una entity e la registra <br>
     *
     * @param singolare        maschile e femminile (obbligatorio ed unico)
     * @param pluraleMaschile  (obbligatorio NON unico)
     * @param pluraleFemminile (obbligatorio NON unico)
     *
     * @return la entity appena creata
     */
    public Genere crea(String singolare, String pluraleMaschile, String pluraleFemminile) {
        return (Genere) save(newEntity(singolare, pluraleMaschile, pluraleFemminile));
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Genere newEntity() {
        return newEntity("", "", "");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Utilizza, eventualmente, la newEntity() della superclasse, per le property della superclasse <br>
     *
     * @param singolare        maschile e femminile (obbligatorio ed unico)
     * @param pluraleMaschile  (obbligatorio NON unico)
     * @param pluraleFemminile (obbligatorio NON unico)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Genere newEntity(String singolare, String pluraleMaschile, String pluraleFemminile) {
        Genere entity = null;

        entity = findByKeyUnica(singolare);
        if (entity != null) {
            return findByKeyUnica(singolare);
        }// end of if cycle

        entity = new Genere();
        entity.singolare = singolare.equals("") ? null : singolare;
        entity.pluraleMaschile = pluraleMaschile.equals("") ? null : pluraleMaschile;
        entity.pluraleFemminile = pluraleFemminile.equals("") ? null : pluraleFemminile;

        return (Genere) creaIdKeySpecifica(entity);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param singolare maschile e femminile (obbligatorio ed unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Genere findByKeyUnica(String singolare) {
        return repository.findBySingolare(singolare);
    }// end of method


    @Override
    public List<Genere> findAll() {
        return (List<Genere>) super.findAll();
    }// end of method

    /**
     * Recupera tutte le istanze della Entity usando la query della property specifica <br>
     *
     * @param genere maschile e femminile
     *
     * @return all entities
     */
    public List<Genere> findAllByPlurale(Genere genere) {
        List<Genere> lista = new ArrayList<>();
        List<Genere> listaM = null;
        List<Genere> listaF = null;

        if (genere != null) {
            if (text.isValid(genere.pluraleMaschile)) {
                listaM = findAllByPlurale(genere.pluraleMaschile);
            }// end of if cycle
            if (text.isValid(genere.pluraleFemminile)) {
                listaF = findAllByPlurale(genere.pluraleFemminile);
            }// end of if cycle

            if (array.isValid(listaM)) {
                lista.addAll(listaM);
            }// end of if cycle
            if (array.isValid(listaF)) {
                lista.addAll(listaF);
            }// end of if cycle
        }// end of if cycle

        return lista;
    }// end of method


    /**
     * Recupera tutte le istanze della Entity usando la query della property specifica <br>
     *
     * @param plurale maschile o femminile
     *
     * @return all entities
     */
    public List<Genere> findAllByPlurale(String plurale) {
        List<Genere> lista = new ArrayList<>();
        List<Genere> listaM;
        List<Genere> listaF;

        if (text.isValid(plurale)) {
            listaM = findAllByPluraleMaschile(plurale);
            listaF = findAllByPluraleFemminile(plurale);

            lista.addAll(listaM);
            lista.addAll(listaF);
        }// end of if cycle

        return lista;
    }// end of method


    /**
     * Recupera tutte le istanze della Entity usando la query della property specifica <br>
     *
     * @param pluraleMaschile
     *
     * @return all entities
     */
    public List<Genere> findAllByPluraleMaschile(String pluraleMaschile) {
        return repository.findAllByPluraleMaschile(pluraleMaschile);
    }// end of method


    /**
     * Recupera tutte le istanze della Entity usando la query della property specifica <br>
     *
     * @param pluraleFemminile
     *
     * @return all entities
     */
    public List<Genere> findAllByPluraleFemminile(String pluraleFemminile) {
        return repository.findAllByPluraleFemminile(pluraleFemminile);
    }// end of method


    /**
     * Property unica (se esiste).
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        return ((Genere) entityBean).singolare;
    }// end of method


    /**
     * Cancella i nomi esistenti <br>
     * Crea tutti i nomi <br>
     * Controlla che ci siano almeno n voci biografiche per il singolo nome <br>
     * Registra la entity <br>
     * Non registra la entity col nome mancante <br>
     */
    public void crea() {
        //        long inizio = System.currentTimeMillis();
        //        int cont = 0;
        //        System.out.println("Creazione completa nomi delle biografie. Circa 1 minuto.");
        //        deleteAll();
        //
        //        DistinctIterable<String> listaNomiDistinti = mongo.mongoOp.getCollection("bio").distinct("nome", String.class);
        //        for (String nome : listaNomiDistinti) {
        //            cont++;
        //            saveNumVoci(nome);
        //        }// end of for cycle
        //
        //        pref.saveValue(LAST_ELABORA_NOME, LocalDateTime.now());
        //        System.out.println("Creazione completa di " + cont + " nomi. Tempo impiegato: " + date.deltaText(inizio));
    }// end of method


    /**
     * Controlla che ci siano almeno n voci biografiche per il singolo nome <br>
     */
    public void update() {
        //        long inizio = System.currentTimeMillis();
        //        System.out.println("Elaborazione nomi delle biografie. Meno di 1 minuto.");
        //        for (Nome nome : findAll()) {
        //            saveNumVoci(nome);
        //        }// end of for cycle
        //        pref.saveValue(LAST_ELABORA_NOME, LocalDateTime.now());
        //        System.out.println("Elaborazione completa dei nomi. Tempo impiegato: " + date.deltaText(inizio));
    }// end of method


    /**
     * Plurale maschile (se esiste).
     */
    public String getPluraleMaschile(String singolare) {
        String plurale = "";
        Genere genere = findByKeyUnica(singolare);

        if (genere != null) {
            plurale = genere.pluraleMaschile;
        }// end of if cycle

        return plurale;
    }// end of method


    /**
     * Plurale femminile (se esiste).
     */
    public String getPluraleFemminile(String singolare) {
        String plurale = "";
        String tagVir = ",";
        Genere genere = findByKeyUnica(singolare);
        String[] parti;
        String tag = "F";

        if (genere != null) {
            plurale = genere.pluraleFemminile;
        }// end of if cycle

        //        // Funziona solo per il format: { "avvocati","M", "avvocate","F"}
        //        if (plurale.contains(GRAFFA_INI) && plurale.contains(GRAFFA_END)) {
        //            plurale = LibWiki.setNoGraffe(plurale);
        //            parti = plurale.split(tagVir);
        //            for (int k = 0; k < parti.length; k++) {
        //                parti[k]=LibWiki.setNoVirgolette(parti[k]);
        //            }// end of for cycle
        //
        //            for (int k = 0; k < parti.length; k++) {
        //                if (parti[k].equals(tag)) {
        //                    if (k > 0) {
        //                        plurale = parti[k - 1];
        //                    }// end of if cycle
        //                }// end of if cycle
        //            }// end of for cycle
        //        }// end of if cycle

        return plurale;
    }// end of method

}// end of class