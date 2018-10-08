package it.algos.vaadflow.modules.anno;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.modules.secolo.Secolo;
import it.algos.vaadflow.service.AService;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import static it.algos.vaadflow.application.FlowCost.TAG_ANN;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 7-ott-2018 21.01.29 <br>
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
@Qualifier(TAG_ANN)
@Slf4j
@AIScript(sovrascrivibile = false)
public class AnnoService extends AService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public AnnoRepository repository;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
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
     * Crea una entity e la registra <br>
     *
     * @param secolo        di riferimento (obbligatorio)
     * @param ordinamento (obbligatorio, unico)
     * @param titolo      (obbligatorio, unico)
     *
     * @return la entity appena creata
     */
    public Anno crea(Secolo secolo, int ordinamento, String titolo) {
        return (Anno) save(newEntity(secolo, ordinamento, titolo));
    }// end of method


     /**
      * Creazione in memoria di una nuova entity che NON viene salvata
      * Eventuali regolazioni iniziali delle property
      * Senza properties per compatibilità con la superclasse
      *
      * @return la nuova entity appena creata (non salvata)
      */
     @Override
     public Anno newEntity() {
         return newEntity((Secolo) null, 0, "");
     }// end of method


     /**
      * Creazione in memoria di una nuova entity che NON viene salvata <br>
      * Eventuali regolazioni iniziali delle property <br>
      * All properties <br>
      * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok) <br>
      * Utilizza, eventualmente, la newEntity() della superclasse, per le property della superclasse <br>
     *
      * @param secolo        di riferimento (obbligatorio)
      * @param ordinamento (obbligatorio, unico)
      * @param titolo      (obbligatorio, unico)
      *
      * @return la nuova entity appena creata (non salvata)
      */
     public Anno newEntity(Secolo secolo, int ordinamento, String titolo) {
         Anno entity = null;

         entity = findByKeyUnica(titolo);
		if (entity != null) {
			return findByKeyUnica(titolo);
		}// end of if cycle

         entity = Anno.builderAnno()
                 .secolo(secolo)
                 .ordinamento(ordinamento)
                 .titolo(titolo)
                 .build();

         return (Anno)creaIdKeySpecifica(entity);
     }// end of method


    /**
     * Property unica (se esiste).
     */
    public String getPropertyUnica(AEntity entityBean) {
        return text.isValid(((Anno) entityBean).getTitolo()) ? ((Anno) entityBean).getTitolo() : "";
    }// end of method


    /**
     * Operazioni eseguite PRIMA del save <br>
     * Regolazioni automatiche di property <br>
     *
     * @param entityBean da regolare prima del save
     * @param operation  del dialogo (NEW, EDIT)
     *
     * @return the modified entity
     */
    @Override
    public AEntity beforeSave(AEntity entityBean, AViewDialog.Operation operation) {
        Anno entity = (Anno) super.beforeSave(entityBean, operation);

        if (entity.getSecolo() == null || entity.ordinamento == 0 || text.isEmpty(entity.titolo)) {
            entity = null;
            log.error("entity incompleta in AnnoService.beforeSave()");
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


}// end of class