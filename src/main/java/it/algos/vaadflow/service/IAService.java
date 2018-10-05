package it.algos.vaadflow.service;

import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.ui.dialog.AViewDialog;

import java.util.List;

/**
 * Project vbase
 * Created by Algos
 * User: gac
 * Date: lun, 19-mar-2018
 * Time: 21:08
 */
public interface IAService {


    /**
     * Service iniettato da Spring (@Scope = 'singleton'). Unica per tutta l'applicazione. Usata come libreria.
     */
    public AAnnotationService getAnnotationService();


    /**
     * Service iniettato da Spring (@Scope = 'singleton'). Unica per tutta l'applicazione. Usata come libreria.
     */
    public AFieldService getFieldService();


    /**
     * Returns the number of entities available.
     *
     * @return the number of entities
     */
    public int count();


    /**
     * Returns the number of entities available for the current company
     *
     * @return the number of selected entities
     */
//    public int countByCompany() ;


    /**
     * Returns the number of entities available for the company
     *
     * @return the number of selected entities
     */
//    public int countByCompany(Company company) ;


    /**
     * Ricerca una entity <br>
     * Se non esiste, la crea <br>
     *
     * @param idKey di riferimento (obbligatorio ed unico)
     *
     * @return la entity trovata o appena creata
     */
    public AEntity findOrCrea(String idKey);

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    public AEntity findById(String id);


    /**
     * Returns all entities of the type.
     * <p>
     * Senza filtri
     * Ordinati per ID
     * <p>
     * Methods of this library return Iterable<T>, while the rest of my code expects Collection<T>
     * L'annotation standard di JPA prevede un ritorno di tipo Iterable, mentre noi usiamo List
     * Eseguo qui la conversione, che rimane trasparente al resto del programma
     *
     * @return all entities
     */
    public List<? extends AEntity> findAll();


    /**
     * Fetches the entities whose 'main text property' matches the given filter text.
     * <p>
     * The matching is case insensitive. When passed an empty filter text,
     * the method returns all categories. The returned list is ordered by name.
     * The 'main text property' is different in each entity class and chosen in the specific subclass
     *
     * @param filter the filter text
     *
     * @return the list of matching entities
     */
    public List<? extends AEntity> findFilter(String filter);


    /**
     * Operazioni eseguite PRIMA del save <br>
     * Regolazioni automatiche di property <br>
     *
     * @param entityBean da regolare prima del save
     * @param operation del dialogo (NEW, EDIT)
     *
     * @return the modified entity
     */
    public AEntity beforeSave(AEntity entityBean, AViewDialog.Operation operation) ;


        /**
          * Saves a given entity.
          * Use the returned instance for further operations
          * as the save operation might have changed the entity instance completely.
          *
          * @param entityBean to be saved
          *
          * @return the saved entity
          */
    public AEntity save(AEntity entityBean);


    /**
     * Saves a given entity.
     * Use the returned instance for further operations
     * as the save operation might have changed the entity instance completely.
     *
     * @param oldBean      previus state
     * @param modifiedBean to be saved
     *
     * @return the saved entity
     */
    public AEntity save(AEntity oldBean, AEntity modifiedBean) throws Exception;


    /**
     * Costruisce una lista di nomi delle properties della Grid nell'ordine:
     * 1) Cerca nell'annotation @AIList della Entity e usa quella lista (con o senza ID)
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxService
     *
     * @return lista di nomi di properties
     */
    public List<String> getGridPropertyNamesList();


    /**
     * Costruisce una lista di nomi delle properties del Form nell'ordine:
     * 1) Cerca nell'annotation @AIForm della Entity e usa quella lista (con o senza ID)
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxService
     *
     * @return lista di nomi di properties
     */
    public List<String> getFormPropertyNamesList(AEntity curremtItem);



//    /**
//     * Fields visibili (e ordinati) nel Form
//     * Sovrascrivibile
//     * Il campo key ID normalmente non viene visualizzato
//     * 1) Se questo metodo viene sovrascritto, si utilizza la lista della sottoclasse specifica (con o senza ID)
//     * 2) Se la classe AEntity->@AIForm(fields = ...) prevede una lista specifica, usa quella lista (con o senza ID)
//     * 3) Se non trova AEntity->@AIForm, usa tutti i campi della AEntity (senza ID)
//     * 4) Se trova AEntity->@AIForm(showsID = true), questo viene aggiunto, indipendentemente dalla lista
//     * 5) Vengono visualizzati anche i campi delle superclassi della classe AEntity
//     * Ad esempio: company2 della classe ACompanyEntity
//     *
//     * @return lista di fields visibili nel Form
//     */
//    public List<Field> getFormFields();
//
//
//    /**
//     * Lista di bottoni presenti nella toolbar (footer) della view AList
//     * Legge la enumeration indicata nella @Annotation della AEntity
//     *
//     * @return lista (type) di bottoni visibili nella toolbar della view AList
//     */
////    public List<EATypeButton> getListTypeButtons();
//
//
//    /**
//     * Lista di bottoni presenti nella toolbar (footer) della view AForm
//     * Legge la enumeration indicata nella @Annotation della AEntity
//     *
//     * @return lista (type) di bottoni visibili nella toolbar della view AForm
//     */
////    public List<EATypeButton> getFormTypeButtons();


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public AEntity newEntity();


    /**
     * Opportunità di controllare (per le nuove schede) che la key unica non esista già.
     * Invocato appena prima del save(), solo per una nuova entity
     *
     * @param entityBean nuova da creare
     */
    public boolean isEsisteEntityKeyUnica(AEntity entityBean);


    /**
     * Deletes a given entity.
     *
     * @param entityBean must not be null
     *
     * @return true, se la entity è stata effettivamente cancellata
     *
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    public boolean delete(AEntity entityBean);


    /**
     * Deletes all entities of the collection.
     */
    public boolean deleteAll();

}// end of interface
