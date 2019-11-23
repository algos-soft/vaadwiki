package it.algos.vaadwiki.modules.wiki;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.giorno.GiornoRepository;
import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import static it.algos.vaadflow.application.FlowCost.TAG_GIO;
import static it.algos.vaadwiki.application.WikiCost.TAG_PRO;
import static it.algos.vaadwiki.application.WikiCost.TAG_WGIO;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 22-nov-2019
 * Time: 17:36
 */
@SpringComponent
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_WGIO)
@Slf4j
@AIScript(sovrascrivibile = false)
public class WikiGiornoService extends WikiService {


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public WikiGiornoService(@Qualifier(TAG_GIO) MongoRepository repository) {
        super(repository);
        super.entityClass = Giorno.class;
        this.repository = (GiornoRepository) repository;
    }// end of Spring constructor

}// end of class
