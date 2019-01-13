package it.algos.vaadflow.modules.anno;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.secolo.EASecolo;
import it.algos.vaadflow.modules.secolo.Secolo;
import it.algos.vaadflow.modules.secolo.SecoloService;
import it.algos.vaadflow.service.AService;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.algos.vaadflow.application.FlowCost.TAG_ANN;
import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 26-ott-2018 9.59.58 <br>
 * <p>
 * Business class. Layer di collegamento per la Repository. <br>
 * <p>
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
@Qualifier(TAG_ANN)
@Slf4j
@AIScript(sovrascrivibile = false)
public class AnnoService extends AService {


    /**
     * Costanti usate nell'ordinamento delle categorie
     */
    public static final int ANNO_INIZIALE = 2000;

    public static final int ANTE_CRISTO = 1000;

    public static final int DOPO_CRISTO = 2030;

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br> Spring costruisce
     * una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br> Qui si una una
     * interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public AnnoRepository repository;


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    private SecoloService secoloService;


    /**
     * Costruttore @Autowired <br> Si usa un @Qualifier(), per avere la sottoclasse specifica <br> Si usa una costante
     * statica, per essere sicuri di scrivere sempre uguali i riferimenti <br> Regola il modello-dati specifico e lo
     * passa al costruttore della superclasse <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public AnnoService(@Qualifier(TAG_ANN) MongoRepository repository) {
        super(repository);
        super.entityClass = Anno.class;
        this.repository = (AnnoRepository) repository;
    }// end of Spring constructor


    /**
     * Crea una entity solo se non esisteva <br>
     *
     * @param titolo (obbligatorio, unico)
     * @param secolo di riferimento (obbligatorio)
     * @param ordine (obbligatorio, unico)
     *
     * @return true se la entity è stata creata
     */
    public boolean creaIfNotExist(String titolo, Secolo secolo, int ordine) {
        boolean creata = false;

        if (isMancaByKeyUnica(titolo)) {
            AEntity entity = save(newEntity(titolo, secolo, ordine));
            creata = entity != null;
        }// end of if cycle

        return creata;
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Senza properties per compatibilità con la superclasse <br>
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Anno newEntity() {
        return newEntity("", (Secolo) null, 0);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     *
     * @param titolo (obbligatorio, unico)
     * @param secolo di riferimento (obbligatorio)
     * @param ordine (obbligatorio, unico)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Anno newEntity(String titolo, Secolo secolo, int ordine) {
        return Anno.builderAnno()
                .titolo(text.isValid(titolo) ? titolo : null)
                .secolo(secolo)
                .ordine(ordine)
                .build();
    }// end of method


    /**
     * Property unica (se esiste).
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        return ((Anno) entityBean).getTitolo();
    }// end of method


    /**
     * Operazioni eseguite PRIMA del save <br>
     * Regolazioni automatiche di property <br>
     * Controllo della validità delle properties obbligatorie <br>
     *
     * @param entityBean da regolare prima del save
     * @param operation  del dialogo (NEW, Edit)
     *
     * @return the modified entity
     */
    @Override
    public AEntity beforeSave(AEntity entityBean, EAOperation operation) {
        Anno entity = (Anno) super.beforeSave(entityBean, operation);

        if (text.isEmpty(entity.titolo) || entity.getSecolo() == null || entity.ordine == 0) {
            entity = null;
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param titolo (obbligatorio, unico)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Anno findByKeyUnica(String titolo) {
        return repository.findByTitolo(titolo);
    }// end of method

    /**
     * Controlla l'esistenza di una Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param titolo (obbligatorio, unico)
     *
     * @return true se trovata
     */
    public boolean isEsiste(String titolo) {
        return findByKeyUnica(titolo) != null;
    }// end of method

    /**
     * Returns all entities of the type <br>
     * <p>
     * Se esiste la property 'ordine', ordinate secondo questa property <br> Altrimenti, se esiste la property 'code',
     * ordinate secondo questa property <br> Altrimenti, se esiste la property 'descrizione', ordinate secondo questa
     * property <br> Altrimenti, ordinate secondo il metodo sovrascritto nella sottoclasse concreta <br> Altrimenti,
     * ordinate in ordine di inserimento nel DB mongo <br>
     *
     * @return all ordered entities
     */
    @Override
    public List<Anno> findAll() {
        return repository.findTop100ByOrderByOrdine();
    }// end of method


    /**
     * Creazione di alcuni dati demo iniziali <br> Viene invocato alla creazione del programma e dal bottone Reset della
     * lista (solo per il developer) <br> La collezione viene svuotata <br> I dati possono essere presi da una
     * Enumeration o creati direttamemte <br> Deve essere sovrascritto - Invocare PRIMA il metodo della superclasse
     *
     * @return numero di elementi creato
     */
    @Override
    public int reset() {
        int numRec = super.reset();
        int ordine;
        String titolo;
        EASecolo secoloEnum;
        Secolo secolo;
        String titoloSecolo;

        //costruisce gli anni prima di cristo dal 1000
        for (int k = ANTE_CRISTO; k > 0; k--) {
            ordine = ANNO_INIZIALE - k;
            titolo = k + EASecolo.TAG_AC;
            secoloEnum = EASecolo.getSecoloAC(k);
            titoloSecolo = secoloEnum.getTitolo();
            secolo = secoloService.findByKeyUnica(titoloSecolo);
            if (ordine != ANNO_INIZIALE) {
                numRec = creaIfNotExist(titolo, secolo, ordine) ? numRec + 1 : numRec;
            }// end of if cycle
        }// end of for cycle

        //costruisce gli anni dopo cristo fino al 2030
        for (int k = 1; k <= DOPO_CRISTO; k++) {
            ordine = k + ANNO_INIZIALE;
            titolo = k + VUOTA;
            secoloEnum = EASecolo.getSecoloDC(k);
            titoloSecolo = secoloEnum.getTitolo();
            secolo = secoloService.findByKeyUnica(titoloSecolo);
            if (ordine != ANNO_INIZIALE) {
                numRec = creaIfNotExist(titolo, secolo, ordine) ? numRec + 1 : numRec;
            }// end of if cycle
        }// end of for cycle

        return numRec;
    }// end of method


}// end of class