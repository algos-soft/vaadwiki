package it.algos.vaadwiki.modules.categoria;

import java.util.List;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import static it.algos.vaadwiki.application.WikiCost.TAG_CAT;

/**
 * Project vaadwiki <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 5-ott-2018 12.05.13 <br>
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
@Qualifier(TAG_CAT)
@AIScript(sovrascrivibile = false)
public interface CategoriaRepository extends MongoRepository<Categoria, String> {



	public Categoria findByPageid(long pageid);
	public Categoria findByTitle(String title);

	public List<Categoria> findTop100ByOrderByTitleAsc();
	public List<Categoria> findTop1000ByOrderByTitleAsc();
	public List<Categoria> findTop10000ByOrderByTitleAsc();
	public List<Categoria> findTop100000ByOrderByTitleAsc();
	public List<Categoria> findAllByOrderByTitleAsc();

}// end of class