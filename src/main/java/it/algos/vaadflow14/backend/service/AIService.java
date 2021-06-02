package it.algos.vaadflow14.backend.service;

import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mar, 22-dic-2020
 * Time: 10:53
 * Interfaccia di collegamento del backend con mongoDB <br>
 * Contiene le API per fornire funzionalità di accesso a mongoDB <br>
 * La superclasse di implementazione astratta è AService <br>
 */
public interface AIService {


    /**
     * Crea e registra una entityBean solo se non esisteva <br>
     * Deve esistere la keyPropertyName della collezione, in modo da poter creare una nuova entityBean
     * solo col valore di un parametro da usare anche come keyID <br>
     * Controlla che non esista già una entityBean con lo stesso keyID <br>
     * Deve esistere il metodo newEntity(keyPropertyValue) con un solo parametro <br>
     *
     * @param keyPropertyValue obbligatorio
     *
     * @return la nuova entityBean appena creata e salvata
     */
    AEntity creaIfNotExist(final String keyPropertyValue);


    /**
     * Crea e registra una entityBean solo se non esisteva <br>
     * Controlla che la entityBean sia valida e superi i validators associati <br>
     *
     * @param newEntityBean da registrare
     *
     * @return la nuova entityBean appena creata e salvata, null se non creata o non salvata
     */
    AEntity checkAndSave(final AEntity newEntityBean);


    /**
     * Crea e registra sempre una entityBean <br>
     *
     * @param entityBean da registrare (nuova o esistente)
     *
     * @return la entityBean appena salvata, null se non salvata
     */
    AEntity save(final AEntity entityBean);


    /**
     * Check the existence of a single entity <br>
     *
     * @param keyId chiave identificativa
     *
     * @return true if exist
     */
    boolean isEsiste(final String keyId);


    /**
     * Creazione in memoria di una nuova entityBean che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Senza properties per compatibilità con la superclasse <br>
     *
     * @return la nuova entityBean appena creata (non salvata)
     */
    AEntity newEntity();

    /**
     * Ordine di presentazione (facoltativo) <br>
     * Viene calcolato in automatico alla creazione della entity <br>
     * Recupera dal DB il valore massimo pre-esistente della property <br>
     * Incrementa di uno il risultato <br>
     *
     * @return ordine di presentazione per la nuova entity
     */
    int getNewOrdine();

    /**
     * Regola la chiave se esiste il campo keyPropertyName <br>
     * Se la company è nulla, la recupera dal login <br>
     * Se la company è ancora nulla, la entity viene creata comunque
     * ma verrà controllata ancora nel metodo beforeSave() <br>
     *
     * @param newEntityBean to be checked
     *
     * @return the checked entityBean
     */
    AEntity fixKey(final AEntity newEntityBean);


    /**
     * Operazioni eseguite PRIMA di save o di insert <br>
     * Regolazioni automatiche di property <br>
     * Controllo della validità delle properties obbligatorie <br>
     * Controllo per la presenza della company se FlowVar.usaCompany=true <br>
     * Controlla se la entityBean registra le date di creazione e modifica <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @param entityBean da regolare prima del save
     * @param operation  del dialogo (NEW, Edit)
     *
     * @return the modified entity
     */
    AEntity beforeSave(final AEntity entityBean, final AEOperation operation);


    /**
     * Retrieves an entityBean by its id <br>
     *
     * @param keyID must not be {@literal null}.
     *
     * @return the entityBean with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    AEntity findById(final String keyID);


    /**
     * Retrieves an entityBean by its keyProperty <br>
     *
     * @param keyPropertyValue must not be {@literal null}.
     *
     * @return the entityBean with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    AEntity findByKey(final String keyPropertyValue);


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
    boolean delete();


    /**
     * Deletes all entities of the collection. <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @return true se la collection è stata cancellata
     */
    boolean deleteAll();

    /**
     * Creazione o ricreazione di alcuni dati iniziali standard <br>
     * Invocato in fase di 'startup' <br>
     * <p>
     * 1) deve esistere lo specifico metodo sovrascritto <br>
     * 2) deve essere valida la entityClazz <br>
     * 3) deve esistere la collezione su mongoDB <br>
     * 4) la collezione viene svuotata <br>
     * 5) vengono mantenuti eventuali records inseriti manualmente <br>
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
    AIResult bootReset();

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
    AIResult reset() ;

        /**
         * The Entity Class  (obbligatoria sempre e final)
         */
    Class<? extends AEntity> getEntityClazz();


    /**
     * Esegue un azione di download, specifica del programma/package in corso <br>
     * Deve essere sovrascritto <br>
     *
     * @return true se l'azione è stata eseguita
     */
    boolean download();

}

