package it.algos.vaadwiki.modules.wiki;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.anno.AnnoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.algos.vaadflow.application.FlowCost.TAG_ANN;
import static it.algos.vaadwiki.application.WikiCost.TAG_WANN;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 24-nov-2019
 * Time: 08:22
 */
@SpringComponent
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_WANN)
@Slf4j
@AIScript(sovrascrivibile = false)
public class WikiAnnoService extends WikiService {

    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public WikiAnnoService(@Qualifier(TAG_ANN) MongoRepository repository) {
        super(repository);
        super.entityClass = Anno.class;
        super.repository = (AnnoRepository) repository;
    }// end of Spring constructor


    /**
     * Returns all entities of the type <br>
     *
     * @return all ordered entities
     */
    @Override
    public List<Anno> findAll() {
        List<Anno> lista = null;
        String sortName = FIELD_NAME_ORDINE;
        Sort sort;

        if (entityClass == null) {
            return null;
        }// end of if cycle

        if (text.isValid(sortName)) {
            sort = new Sort(Sort.Direction.DESC, sortName);
            lista = (List<Anno>) findAll(sort);
        }// end of if cycle

        return lista;
    }// end of method

}// end of class
