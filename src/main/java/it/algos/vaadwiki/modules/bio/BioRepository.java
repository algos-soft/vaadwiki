package it.algos.vaadwiki.modules.bio;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadwiki.modules.attivita.Attivita;
import it.algos.vaadwiki.modules.nazionalita.Nazionalita;
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

    public List<Bio> findAllByGiornoNascita(Giorno giornoNascita);

    public List<Bio> findAllByGiornoMorte(Giorno giornoMorte);

    public List<Bio> findAllByAnnoNascita(Anno annoNascita);

    public List<Bio> findAllByAnnoMorte(Anno annoMorte);

    public List<Bio> findAllByNome(String nome);

    public List<Bio> findAllByNomeOrderByAttivitaAsc(String nome);

    public List<Bio> findAllByCognome(String cognome);

    public int countAllByAttivita(Attivita attivita);

    public int countAllByAttivita2(Attivita attivita);

    public int countAllByAttivita3(Attivita attivita);

    public int countByAttivitaOrAttivita2OrAttivita3(Attivita attivita, Attivita attivita2, Attivita attivita3);

    public List<Bio> findAllByAttivitaOrderByCognomeAsc(Attivita attivita);

    public List<Bio> findAllByAttivita2(Attivita attivita);

    public List<Bio> findAllByAttivita3(Attivita attivita);

    public List<Bio> findByAttivitaOrAttivita2OrAttivita3OrderByCognomeAsc(Attivita attivita, Attivita attivita2, Attivita attivita3);

    public List<Bio> findAllByNazionalitaOrderByCognomeAsc(Nazionalita nazionalita);


    public int countAllByNazionalita(Nazionalita nazionalita);

    public int countAllByGiornoNascita(Giorno giornoNascita);

    public int countAllByGiornoMorte(Giorno giornoMorte);

    public int countAllByAnnoNascita(Anno annoNascita);

    public int countAllByAnnoMorte(Anno annoMorte);

//    @Query("{'nome':?0}")
//    List<String> findDistinctNome(String nome);
//
//    @Query("{ distinct : 'bio', key : 'nome'}")
//    JSONArray listDistinctNome();
//
//    List<String> findDistinctByNome();

}// end of class