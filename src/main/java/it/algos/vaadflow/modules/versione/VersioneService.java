package it.algos.vaadflow.modules.versione;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAPrefType;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.AService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.TAG_VER;

/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 30-set-2018 16.14.56 <br>
 * <br>
 * Estende la classe astratta AService. Layer di collegamento per la Repository. <br>
 * <br>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Service (ridondante) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@SpringComponent
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_VER)
@Slf4j
@AIScript(sovrascrivibile = false)
public class VersioneService extends AService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    private final static String TAG = ".";

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private PreferenzaService pref;


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    private VersioneRepository repository;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola nella superclasse il modello-dati specifico <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public VersioneService(@Qualifier(TAG_VER) MongoRepository repository) {
        super(repository);
        super.entityClass = Versione.class;
        this.repository = (VersioneRepository) repository;
    }// end of Spring constructor

    /**
     * Crea una entity e la registra <br>
     *
     * @param sigla  del progetto interessato (obbligatorio, un solo carattere) <br>
     * @param titolo della versione (obbligatorio, non unico) <br>
     * @param nome   descrittivo della versione (obbligatorio, unico) <br>
     *
     * @return la entity appena creata
     */
    public Versione crea(String sigla, String titolo, String nome) {
        return (Versione) save(newEntity(sigla, 0, titolo, nome, (LocalDateTime) null));
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Versione newEntity() {
        return newEntity("", 0, "", "", (LocalDateTime) null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok) <br>
     *
     * @param sigla     del progetto interessato (obbligatorio, un solo carattere) <br>
     * @param ordine    di presentazione (obbligatorio con inserimento automatico se è zero)
     * @param titolo    della versione (obbligatorio, non unico) <br>
     * @param nome      descrittivo della versione (obbligatorio, unico) <br>
     * @param timestamp in cui si effettua la modifica della versione (obbligatorio, non unica) <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Versione newEntity(String sigla, int ordine, String titolo, String nome, LocalDateTime timestamp) {
        Versione entity = null;
        int newOrdine = 0;
        String textOrdine;

        entity = Versione.builderVersione()
                .titolo(text.isValid(titolo) ? titolo : null)
                .nome(text.isValid(nome) ? nome : null)
                .timestamp(timestamp != null ? timestamp : LocalDateTime.now())
                .build();

        newOrdine = getNewOrdine(sigla, ordine);
        textOrdine = newOrdine < 10 ? TAG + newOrdine : "" + newOrdine;
        entity.id = sigla + textOrdine;

        return (Versione)creaIdKeySpecifica(entity);
    }// end of method


    /**
     * Ordine di presentazione (obbligatorio, unico per ogni project), <br>
     * Viene calcolato in automatico alla creazione della entity <br>
     * Recupera dal DB il valore massimo pre-esistente della property per lo specifico progetto <br>
     * Incrementa di uno il risultato <br>
     */
    public int getNewOrdine(String sigla, int ordine) {
        int newOrdine = ordine;
        List<Versione> lista = null;
        String idKey = "";

        if (newOrdine == 0) {
            lista = repository.findByIdRegex(sigla);
            if (lista != null && lista.size() > 0) {
                idKey = lista.get(lista.size() - 1).getId();
                idKey = idKey.substring(1);
                idKey = idKey.startsWith(TAG) ? text.levaTesta(idKey, TAG) : idKey;
            }// end of if cycle

            try { // prova ad eseguire il codice
                newOrdine = Integer.decode(idKey);
            } catch (Exception unErrore) { // intercetta l'errore
                log.error(unErrore.toString());
            }// fine del blocco try-catch
        }// end of if cycle

        return newOrdine + 1;
    }// end of method


    /**
     * Retrieves an entity by its id.
     * Codice formato da un carattere, un separatore ed un numero
     */
    public Versione findByCode(String codeUnCarattere, int numProgressivo) {
        if (numProgressivo < 10) {
            return (Versione) findById(codeUnCarattere + TAG + numProgressivo);
        } else {
            return (Versione) findById(codeUnCarattere + numProgressivo);
        }// end of if/else cycle
    }// end of method


    /**
     * Returns all entities of the type <br>
     * <p>
     * Se esiste la property 'ordine', ordinate secondo questa property <br>
     * Altrimenti, se esiste la property 'code', ordinate secondo questa property <br>
     * Altrimenti, se esiste la property 'descrizione', ordinate secondo questa property <br>
     * Altrimenti, ordinate secondo il metodo sovrascritto nella sottoclasse concreta <br>
     * Altrimenti, ordinate in ordine di inserimento nel DB mongo <br>
     *
     * @return all ordered entities
     */
    @Override
    public List<? extends AEntity> findAll() {
        return findAll((Sort) null);
    }// end of method

    /**
     * Crea una entity di Versione e la registra <br>
     * Crea una nuova preferenza (solo se non esistente) <br>
     *
     * @param sigla    del progetto interessato (obbligatorio, un solo carattere) <br>
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     * @param type     della preferenza
     * @param value    di default della preferenza
     */
    public void creaPref(String sigla, String codePref, String descPref, EAPrefType type, Object value) {
        pref.findOrCrea(codePref, descPref, type, value);
        this.crea(sigla, "Preferenze", codePref + ", di default " + value);
    }// end of method


    /**
     * Crea una entity di Versione e la registra <br>
     * Crea una nuova preferenza di tipo string (solo se non esistente) <br>
     *
     * @param sigla    del progetto interessato (obbligatorio, un solo carattere) <br>
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     * @param value    di default della preferenza
     */
    public void creaPrefTxt(String sigla, String codePref, String descPref, String value) {
        creaPref(sigla, codePref, descPref, EAPrefType.string, value);
    }// end of method

    /**
     * Crea una entity di Versione e la registra <br>
     * Crea una nuova preferenza di tipo string (solo se non esistente) <br>
     *
     * @param sigla    del progetto interessato (obbligatorio, un solo carattere) <br>
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     */
    public void creaPrefTxt(String sigla, String codePref, String descPref) {
        creaPrefTxt(sigla, codePref, descPref, "");
    }// end of method


    /**
     * Crea una entity di Versione e la registra <br>
     * Crea una nuova preferenza di tipo bool (solo se non esistente) <br>
     *
     * @param sigla    del progetto interessato (obbligatorio, un solo carattere) <br>
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     * @param value    di default della preferenza
     */
    public void creaPrefBool(String sigla, String codePref, String descPref, boolean value) {
        creaPref(sigla, codePref, descPref, EAPrefType.bool, value);
    }// end of method

    /**
     * Crea una entity di Versione e la registra <br>
     * Crea una nuova preferenza di tipo bool (solo se non esistente) <br>
     *
     * @param sigla    del progetto interessato (obbligatorio, un solo carattere) <br>
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     */
    public void creaPrefBool(String sigla, String codePref, String descPref) {
        creaPrefBool(sigla, codePref, descPref, false);
    }// end of method


    /**
     * Crea una entity di Versione e la registra <br>
     * Crea una nuova preferenza di tipo int (solo se non esistente) <br>
     *
     * @param sigla    del progetto interessato (obbligatorio, un solo carattere) <br>
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     * @param value    di default della preferenza
     */
    public void creaPrefInt(String sigla, String codePref, String descPref, int value) {
        creaPref(sigla, codePref, descPref, EAPrefType.integer, value);
    }// end of method

    /**
     * Crea una entity di Versione e la registra <br>
     * Crea una nuova preferenza di tipo int (solo se non esistente) <br>
     *
     * @param sigla    del progetto interessato (obbligatorio, un solo carattere) <br>
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     */
    public void creaPrefInt(String sigla, String codePref, String descPref) {
        creaPrefInt(sigla, codePref, descPref, 0);
    }// end of method


    /**
     * Crea una entity di Versione e la registra <br>
     * Crea una nuova preferenza di tipo date (solo se non esistente) <br>
     *
     * @param sigla    del progetto interessato (obbligatorio, un solo carattere) <br>
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     * @param value    di default della preferenza
     */
    public void creaPrefDate(String sigla, String codePref, String descPref, LocalDateTime value) {
        creaPref(sigla, codePref, descPref, EAPrefType.date, value);
    }// end of method

    /**
     * Crea una entity di Versione e la registra <br>
     * Crea una nuova preferenza di tipo date (solo se non esistente) <br>
     *
     * @param sigla    del progetto interessato (obbligatorio, un solo carattere) <br>
     * @param codePref key code della preferenza (obbligatoria per Pref)
     * @param descPref dettagliata (obbligatoria per Pref)
     */
    public void creaPrefDate(String sigla, String codePref, String descPref) {
        creaPrefDate(sigla, codePref, descPref, (LocalDateTime) null);
    }// end of method


}// end of class