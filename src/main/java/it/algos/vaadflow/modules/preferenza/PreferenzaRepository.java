package it.algos.vaadflow.modules.preferenza;

import java.util.List;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import static it.algos.vaadflow.application.FlowCost.TAG_PRE;

/**
 * Project vaadflow <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 30-set-2018 16.14.56 <br>
 * <br>
 * Estende la l'interaccia MongoRepository col casting alla Entity relativa di questa repository <br>
 * <br>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_PRE)
@AIScript(sovrascrivibile = false)
public interface PreferenzaRepository extends MongoRepository<Preferenza, String> {

	public Preferenza findByCode(String code);

	public List<Preferenza> findAllByOrderByCodeAsc();

	public Preferenza findByDescrizione(String descrizione);

	public List<Preferenza> findAllByOrderByDescrizioneAsc();

	public Preferenza findByOrdine(int ordine);

	public List<Preferenza> findAllByOrderByOrdineAsc();

	public List<Preferenza> findTop1AllByOrderByOrdineDesc();

}// end of class