package it.algos.vaadwiki.modules.wikigiorno;

import java.util.List;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import it.algos.vaadflow.annotation.AIScript;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import static it.algos.vaadwiki.application.WikiCost.TAG_WGIO;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 19-gen-2019 11.33.37 <br>
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
@Qualifier(TAG_WGIO)
@AIScript(sovrascrivibile = false)
public interface WikiGiornoRepository extends MongoRepository<WikiGiorno, String> {

    public WikiGiorno findByCode(String code);

	public List<WikiGiorno> findAllByOrderByOrdineAsc();

}// end of class