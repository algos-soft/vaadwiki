package it.algos.vaadflow14.backend.logic;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.packages.company.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.backend.wrapper.*;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.*;

import javax.annotation.*;
import java.time.*;
import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: lun, 21-dic-2020
 * Time: 07:18
 * <p>
 * Layer di collegamento del backend con mongoDB <br>
 * Classe astratta di servizio per la Entity di un package <br>
 * Le sottoclassi concrete sono SCOPE_SINGLETON e non mantengono dati <br>
 * L'unico dato mantenuto nelle sottoclassi concrete:
 * la property final entityClazz <br>
 * Se la sottoclasse xxxService non esiste (non è indispensabile), usa la classe
 * generica EntityService; i metodi esistono ma occorre un cast in uscita <br>
 */
public abstract class AService extends AAbstractService implements AIService {

    /**
     * The Entity Class  (obbligatoria sempre e final)
     */
    protected final Class<? extends AEntity> entityClazz;


    /**
     * Flag di preferenza per specificare la property della entity da usare come ID <br>
     * (public perché usata in un test)
     */
    public String keyPropertyName;


    /**
     * Costruttore senza parametri <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public AService(final Class<? extends AEntity> entityClazz) {
        this.entityClazz = entityClazz;
    }


    /**
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     */
    @PostConstruct
    protected void postConstruct() {
        this.fixPreferenze();
    }


    /**
     * Preferenze usate da questo service <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Puo essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        this.keyPropertyName = annotation.getKeyPropertyName(entityClazz);
    }


    /**
     * Crea e registra una entity solo se non esisteva <br>
     * Deve esistere la keyPropertyName della collezione, in modo da poter creare una nuova entity <br>
     * solo col valore di un parametro da usare anche come keyID <br>
     * Controlla che non esista già una entity con lo stesso keyID <br>
     * Deve esistere il metodo newEntity(keyPropertyValue) con un solo parametro <br>
     *
     * @param keyPropertyValue obbligatorio
     *
     * @return la nuova entity appena creata e salvata
     */
    public AEntity creaIfNotExist(final String keyPropertyValue) {
        return null;
    }

    /**
     * Crea e registra una entityBean solo se non esisteva <br>
     * Controlla che la entityBean sia valida e superi i validators associati <br>
     *
     * @param newEntityBean da registrare
     *
     * @return la nuova entityBean appena creata e salvata, null se non creata o non salvata
     */
    @Override
    public AEntity checkAndSave(final AEntity newEntityBean) {
        AEntity entityBean;
        boolean valido = false;
        String message = VUOTA;

        //--controlla che la newEntityBean non esista già
        if (isEsiste(newEntityBean.id)) {
            return null;
        }

        valido = true;

        if (valido) {
            entityBean = beforeSave(newEntityBean, AEOperation.addNew);
            valido = mongo.insert(entityBean) != null;
            return entityBean;
        }
        else {
            message = "Duplicate key error ";
            message += beanService.getModifiche(newEntityBean);
            logger.warn(message, this.getClass(), "checkAndSave");
            return newEntityBean;
        }
    }

    /**
     * Crea e registra sempre una entityBean <br>
     *
     * @param entityBean da registrare (nuova o esistente)
     *
     * @return la entityBean appena salvata, null se non salvata
     */
    @Override
    public AEntity save(AEntity entityBean) {
        return mongo.save(entityBean);
    }

    /**
     * Check the existence of a single entity. <br>
     *
     * @param keyId chiave identificativa
     *
     * @return true if exist
     */
    public boolean isEsiste(final String keyId) {
        return mongo.isEsiste(entityClazz, keyId);
    }

    /**
     * Crea e registra una entityBean col flag reset=true <br>
     * Esegue SOLO se la entity è sottoclasse di AREntity <br>
     *
     * @param newEntity appena creata, da regolare e da salvare
     *
     * @return true se la entity è stata creata e salvata
     */
    protected boolean creaReset(final AREntity newEntity) {
        newEntity.reset = true;
        return save(newEntity) != null;
    }

    /**
     * Creazione in memoria di una nuova entityBean che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Senza properties per compatibilità con la superclasse <br>
     *
     * @return la nuova entityBean appena creata (non salvata)
     */
    public AEntity newEntity() {
        AEntity newEntityBean = null;

        try {
            newEntityBean = entityClazz.getDeclaredConstructor().newInstance();
        } catch (Exception unErrore) {
            logger.warn(unErrore.toString(), this.getClass(), "newEntity");
        }

        return newEntityBean;
    }


    /**
     * Operazioni eseguite PRIMA di save o di insert <br>
     * Regolazioni automatiche di property <br>
     * Controllo della validità delle properties obbligatorie <br>
     * Controllo per la presenza della company se FlowVar.usaCompany=true <br>
     * Controlla se la entity registra le date di creazione e modifica <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @param entityBean da regolare prima del save
     * @param operation  del dialogo (NEW, Edit)
     *
     * @return the modified entity
     */
    public AEntity beforeSave(final AEntity entityBean, final AEOperation operation) {
        AEntity entityBeanWithID;
        Company company;

        entityBeanWithID = fixKey(entityBean);

        if (FlowVar.usaCompany && entityBeanWithID instanceof ACEntity) {
            company = ((ACEntity) entityBeanWithID).company;
            company = company != null ? company : vaadinService.getCompany();
            if (company == null) {
                return null;
            }
            else {
                ((ACEntity) entityBeanWithID).company = company;
            }
        }

        if (annotation.usaTimeStamp(entityClazz)) {
            if (operation == AEOperation.addNew) {
                entityBeanWithID.creazione = LocalDateTime.now();
            }
            if (operation != AEOperation.showOnly) {
                if (beanService.isModificata(entityBeanWithID)) {
                    entityBeanWithID.modifica = LocalDateTime.now();
                }
            }
        }

        return entityBeanWithID;
    }

    /**
     * Fetches all entities of the type <br>
     * <p>
     * Ordinate secondo l'annotation @AIView(sortProperty) della entityClazz <br>
     * Ordinate secondo la property 'ordine', se esiste <br>
     * Ordinate secondo la property 'code', se esiste <br>
     * Ordinate secondo la property 'descrizione', se esiste <br>
     * Altrimenti, ordinate in ordine di inserimento nel DB mongo <br>
     * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     *
     * @return all ordered entities
     */
    public List<? extends AEntity> fetch() {
        List<? extends AEntity> lista = null;
        Sort sort;

        if (entityClazz == null) {
            return null;
        }

        //--Ordinate secondo l'annotation @AIView(sortProperty) della entityClazz
        sort = annotation.getSortSpring(entityClazz);

        //--Ordinate secondo la property 'ordine', se esiste
        if (sort == null) {
            if (reflection.isEsiste(entityClazz, FIELD_NAME_ORDINE)) {
                sort = Sort.by(Sort.Direction.ASC, FIELD_NAME_ORDINE);
            }
        }

        //--Ordinate secondo la property 'code', se esiste
        if (sort == null) {
            if (reflection.isEsiste(entityClazz, FIELD_NAME_CODE)) {
                sort = Sort.by(Sort.Direction.ASC, FIELD_NAME_CODE);
            }
        }

        //--Ordinate secondo la property 'descrizione', se esiste
        if (sort == null) {
            if (reflection.isEsiste(entityClazz, FIELD_NAME_DESCRIZIONE)) {
                sort = Sort.by(Sort.Direction.ASC, FIELD_NAME_DESCRIZIONE);
            }
        }

        if (sort != null) {
            lista = mongo.findAll(entityClazz, sort);
        }
        else {
            lista = mongo.findAll(entityClazz, (Sort) null);
        }

        return lista;
    }

    /**
     * Regola la chiave se esiste il campo keyPropertyName. <br>
     * Se la company è nulla, la recupera dal login <br>
     * Se la company è ancora nulla, la entity viene creata comunque
     * ma verrà controllata ancora nel metodo beforeSave() <br>
     *
     * @param newEntityBean to be checked
     *
     * @return the checked entity
     */
    public AEntity fixKey(final AEntity newEntityBean) {
        String keyPropertyValue;
        Company company;

        if (text.isEmpty(newEntityBean.id)) {
            if (text.isValid(keyPropertyName)) {
                keyPropertyValue = reflection.getPropertyValueStr(newEntityBean, keyPropertyName);
                if (text.isValid(keyPropertyValue)) {
                    keyPropertyValue = text.levaSpazi(keyPropertyValue);
                    newEntityBean.id = keyPropertyValue.toLowerCase();
                }
            }
        }

        if (newEntityBean instanceof ACEntity) {
            company = vaadinService.getCompany();
            ((ACEntity) newEntityBean).company = company;
        }

        return newEntityBean;
    }

    /**
     * Retrieves an entity by its id.
     *
     * @param keyID must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    public AEntity findById(final String keyID) {
        return mongo.findById(entityClazz, keyID);
    }


    /**
     * Retrieves an entity by its keyProperty.
     *
     * @param keyPropertyValue must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    public AEntity findByKey(final String keyPropertyValue) {
        if (text.isValid(keyPropertyName)) {
            return mongo.findOneUnique(entityClazz, keyPropertyName, keyPropertyValue);
        }
        else {
            return findById(keyPropertyValue);
        }
    }

    /**
     * Cancella la collection <br>
     * Se usaCompany=false, cancella la intera collection <br>
     * Se usaCompany=true, cancella usando la company corrente come filtro <br>
     * Se non trova la company corrente NON cancella <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @return false se non esiste la company o non ha cancellato
     * ....... true se la collection è stata cancellata (tutta o filtrata)
     */
    @Override
    @Deprecated
    public boolean delete() {
        if (FlowVar.usaCompany) {
            return false;
        }
        else {
            return deleteAll();
        }
    }

    /**
     * Deletes all entities of the collection. <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @return true se la collection è stata cancellata
     */
    @Override
    public boolean deleteAll() {

        if (mongo.isExists(annotation.getCollectionName(entityClazz))) {
            mongo.mongoOp.remove(new Query(), entityClazz);
            return true;
        }
        else {
            return true;
        }
    }

    //    @Deprecated
    //    public AIResult fixPostResetOnly(final AETypeReset type, final int numRec) {
    //        String collectionName;
    //        String message;
    //
    //        if (entityClazz == null) {
    //            return AResult.errato("Manca la entityClazz nella businessService specifica");
    //        }
    //
    //        collectionName = annotation.getCollectionName(entityClazz);
    //        if (mongo.isValid(entityClazz)) {
    //            message = String.format("La collezione %s era vuota e sono stati inseriti %d elementi %s", collectionName, numRec, type.get());
    //            return AResult.valido(message);
    //        }
    //        else {
    //            message = String.format("Non è stato possibile creare la collezione %s", collectionName);
    //            return AResult.errato(message);
    //        }
    //    }

    /**
     * Creazione o ricreazione di alcuni dati iniziali standard <br>
     * Invocato dal bottone Reset di alcune liste <br>
     * <p>
     * I dati possono essere: <br>
     * 1) recuperati da una Enumeration interna <br>
     * 2) letti da un file CSV esterno <br>
     * 3) letti da Wikipedia <br>
     * 4) creati direttamente <br>
     * DEVE essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @return wrapper col risultato ed eventuale messaggio di errore
     */
    @Override
    public AIResult reset() {
        Query query = new Query();

        if (entityClazz == null) {
            return AResult.errato("Manca la entityClazz nella businessService specifica");
        }

        //--cancella le entities preesistenti
        //--solo quelle create da reset
        query.addCriteria(Criteria.where(FIELD_NAME_RESET).is(true));
        mongo.delete(entityClazz, query);

        return AResult.valido();
    }

    /**
     * Creazione o ricreazione di alcuni dati iniziali standard <br>
     * Invocato dal bottone Reset di alcuni form <br>
     * <p>
     * I dati possono essere: <br>
     * 1) recuperati da una Enumeration interna <br>
     * 2) letti da un file CSV esterno <br>
     * 3) letti da Wikipedia <br>
     * 4) creati direttamente <br>
     * DEVE essere sovrascritto, SENZA invocare prima il metodo della superclasse <br>
     *
     * @param entityBean di cui ricreare le condizioni
     *
     * @return wrapper col risultato ed eventuale messaggio di errore
     */
    @Override
    public AIResult resetForm(AEntity entityBean) {
        String message = String.format("Manca il metodo resetForm nella classe specifica %s", this.getClass().getSimpleName());
        logger.log(AETypeLog.reset, message);
        return AResult.errato(message);
    }

    /**
     * Ordine di presentazione (facoltativo) <br>
     * Viene calcolato in automatico alla creazione della entity <br>
     * Recupera dal DB il valore massimo pre-esistente della property <br>
     * Incrementa di uno il risultato <br>
     *
     * @return ordine di presentazione per la nuova entity
     */
    @Override
    public int getNewOrdine() {
        return mongo.getNewOrder(entityClazz, FIELD_ORDINE);
    }

    /**
     * The Entity Class  (obbligatoria sempre e final)
     */
    @Override
    public Class<? extends AEntity> getEntityClazz() {
        return entityClazz;
    }

    /**
     * Esegue un azione di download, specifica del programma/package in corso <br>
     * Deve essere sovrascritto <br>
     *
     * @return true se l'azione è stata eseguita
     */
    @Override
    public boolean download() {
        return false;
    }

}
