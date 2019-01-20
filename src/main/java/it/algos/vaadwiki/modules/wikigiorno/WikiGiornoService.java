package it.algos.vaadwiki.modules.wikigiorno;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.service.AService;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import static it.algos.vaadwiki.application.WikiCost.TAG_WGIO;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 19-gen-2019 11.33.37 <br>
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
@VaadinSessionScope
@Qualifier(TAG_WGIO)
@Slf4j
@AIScript(sovrascrivibile = false)
public class WikiGiornoService extends AService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public WikiGiornoRepository repository;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public WikiGiornoService(@Qualifier(TAG_WGIO) MongoRepository repository) {
        super(repository);
        super.entityClass = WikiGiorno.class;
        this.repository = (WikiGiornoRepository) repository;
   }// end of Spring constructor

    /**
     * Ricerca di una entity (la crea se non la trova) <br>
     *
     * @param code di riferimento (obbligatorio ed unico)
     *
     * @return la entity trovata o appena creata
     */
    public WikiGiorno findOrCrea(String code) {
        WikiGiorno entity = findByKeyUnica(code);

        if (entity == null) {
            entity = crea(code);
        }// end of if cycle

        return entity;
    }// end of method

    /**
     * Crea una entity e la registra <br>
     *
     * @param code di riferimento (obbligatorio ed unico)
     *
     * @return la entity appena creata
     */
    public WikiGiorno crea(String code) {
         return (WikiGiorno)save(newEntity(0, code, ""));
    }// end of method

     /**
      * Creazione in memoria di una nuova entity che NON viene salvata
      * Eventuali regolazioni iniziali delle property
      * Senza properties per compatibilità con la superclasse
      *
      * @return la nuova entity appena creata (non salvata)
      */
     @Override
     public WikiGiorno newEntity() {
         return newEntity(0, "", "");
     }// end of method


     /**
      * Creazione in memoria di una nuova entity che NON viene salvata <br>
      * Eventuali regolazioni iniziali delle property <br>
      * All properties <br>
      * Utilizza, eventualmente, la newEntity() della superclasse, per le property della superclasse <br>
     *
      * @param ordine      di presentazione (obbligatorio con inserimento automatico se è zero)
	* @param code        codice di riferimento (obbligatorio)
	* @param descrizione (facoltativa, non unica)
      *
      * @return la nuova entity appena creata (non salvata)
      */
     public WikiGiorno newEntity(int ordine, String code, String descrizione) {
         WikiGiorno entity = null;

         entity = findByKeyUnica(code);
		if (entity != null) {
			return findByKeyUnica(code);
		}// end of if cycle
		
         entity = WikiGiorno.builderWikiGiorno()
				.ordine(ordine != 0 ? ordine : this.getNewOrdine())
				.code(text.isValid(code) ? code : null)
				.descrizione(text.isValid(descrizione) ? descrizione : null)
				.build();

         return (WikiGiorno)creaIdKeySpecifica(entity);
     }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param code di riferimento (obbligatorio)
     *
     * @return istanza della Entity, null se non trovata
     */
    public WikiGiorno findByKeyUnica(String code) {
        return repository.findByCode(code);
    }// end of method

    /**
     * Property unica (se esiste) <br>
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        return ((WikiGiorno) entityBean).getCode();
    }// end of method

}// end of class