package it.algos.vaadwiki.modules.bio;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

import static it.algos.vaadwiki.application.WikiCost.TAG_BIO;


/**
 * Project vaadbio2 <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Date: 11-ago-2018 17.19.29 <br>
 * <br>
 * Estende la l'interaccia MongoRepository col casting alla Entity relativa di questa repository <br>
 * <br>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Scope (obbligatorio = 'singleton') <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_BIO)
@AIScript(sovrascrivibile = false)
public interface BioRepository extends MongoRepository<Bio, String> {


    public Bio findByPageid(long pageid);

    public Bio findByWikiTitle(String wikiTitle);

    public List<Bio> findAllByOrderByWikiTitleAsc();

    public List<Bio> findTop50ByOrderByWikiTitleAsc();

    public List<Bio> findAllByGiornoNato(String giornoNato);

    public List<Bio> findAllByGiornoMorto(String giornoMorto);

    public List<Bio> findAllByAnnoNato(String annoNato);

    public List<Bio> findAllByAnnoMorto(String annoMorto);

    public List<Bio> findAllByNome(String nome);

    public List<Bio> findAllByCognome(String cognome);

//    @Query("{'nome':?0}")
//    List<String> findDistinctNome(String nome);
//
//    @Query("{ distinct : 'bio', key : 'nome'}")
//    JSONArray listDistinctNome();
//
//    List<String> findDistinctByNome();

}// end of class