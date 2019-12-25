package it.algos.vaadwiki.modules.attivita;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

import static it.algos.vaadwiki.application.WikiCost.TAG_ATT;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 6-ott-2019 18.00.03 <br>
 * <br>
 * Estende la l'interaccia MongoRepository col casting alla Entity relativa di questa repository <br>
 * <br>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 * - la documentazione precedente a questo tag viene SEMPRE riscritta <br>
 * - se occorre preservare delle @Annotation con valori specifici, spostarle DOPO @AIScript <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_ATT)
@AIScript(sovrascrivibile = false)
public interface AttivitaRepository extends MongoRepository<Attivita, String> {

    public Attivita findBySingolare(String singolare);

    public List<Attivita> findAllByOrderBySingolareAsc();

    public List<Attivita> findAllByOrderByPluraleAsc();

    public List<Attivita> findAllByPlurale(String plurale);

    @Query("{ state : 'ACTIVE' }")
    public List<Attivita> findPage(Pageable pageable);

    @Query("{ distinct : 'plurale'}")
    public JSONArray listDistinctPlurali();

}// end of class