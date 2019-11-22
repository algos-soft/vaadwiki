package it.algos.vaadwiki.modules.doppinomi;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadwiki.modules.wiki.WikiService;
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
import static it.algos.vaadwiki.application.WikiCost.LAST_DOWNLOAD_DOPPI_NOMI;
import static it.algos.vaadwiki.application.WikiCost.TAG_DOP;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 26-set-2019 15.24.13 <br>
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
 * - la documentazione precedente a questo tag viene SEMPRE riscritta <br>
 * - se occorre preservare delle @Annotation con valori specifici, spostarle DOPO @AIScript <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_DOP)
@Slf4j
@AIScript(sovrascrivibile = false)
public class DoppinomiService extends WikiService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public DoppinomiRepository repository;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public DoppinomiService(@Qualifier(TAG_DOP) MongoRepository repository) {
        super(repository);
        super.entityClass = Doppinomi.class;
        this.repository = (DoppinomiRepository) repository;
        super.codeLastDownload = LAST_DOWNLOAD_DOPPI_NOMI;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova) <br>
     *
     * @param code di riferimento (obbligatorio ed unico)
     *
     * @return la entity trovata o appena creata
     */
    public Doppinomi findOrCrea(String code) {
        Doppinomi entity = findByKeyUnica(code);

        if (entity == null) {
            entity = (Doppinomi) save(newEntity(code));
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Utilizza, eventualmente, la newEntity() della superclasse, per le property della superclasse <br>
     *
     * @param code codice di riferimento (obbligatorio)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Doppinomi newEntity(String code) {
        Doppinomi entity = null;

        entity = findByKeyUnica(code);
        if (entity != null) {
            return entity;
        }// end of if cycle

        entity = Doppinomi.builderDoppinomi()
                .code(text.isValid(code) ? code : null)
                .build();

        return (Doppinomi) creaIdKeySpecifica(entity);
    }// end of method


    /**
     * Costruisce una lista della property 'code' di tutte le entities <br>
     */
    public List<String> findAllCode() {
        List<String> listaCode = new ArrayList();
        List<Doppinomi> listaEntity = (List<Doppinomi>) findAll();

        if (array.isValid(listaEntity)) {
            listaCode = new ArrayList<>();
            for (Doppinomi doppio : listaEntity) {
                listaCode.add(doppio.code);
            }// end of for cycle
        }// end of if cycle

        return listaCode;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param code di riferimento (obbligatorio)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Doppinomi findByKeyUnica(String code) {
        return repository.findByCode(code);
    }// end of method


    /**
     * Download completo del modulo da Wiki <br>
     * Cancella tutte le precedenti entities <br>
     * Registra su Mongo DB tutte le occorrenze di attività/nazionalità <br>
     */
    public void download() {
        String tag = A_CAPO + "\\*";
        long inizio = System.currentTimeMillis();
        String testo = "";
        String[] righe = null;
        String message = "";
        String nome;

        //--legge la pagina wiki
        testo = ((AQueryVoce) appContext.getBean("AQueryVoce", titoloModuloDoppiNomi)).urlRequest();
        if (text.isValid(testo)) {
            righe = testo.split(tag);
        }// end of if cycle

        if (array.isValid(righe)) {
            this.deleteAll();

            //--il primo va eliminato (non pertinente)
            for (int k = 1; k < righe.length; k++) {
                nome = righe[k];

                //--l'ultimo va troncato
                if (k == righe.length - 1) {
                    nome = nome.substring(0, nome.indexOf("\n\n"));
                }// end of if cycle

                this.findOrCrea(nome);
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

                log.debug(message);
                logger.debug(message);
            }// end of if cycle
        } else {
            logger.error(entityClass.getSimpleName() + " - Qualcosa non ha funzionato");
        }// end of if/else cycle
    }// end of method

}// end of class