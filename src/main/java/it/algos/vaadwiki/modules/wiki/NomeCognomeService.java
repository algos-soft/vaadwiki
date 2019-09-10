package it.algos.vaadwiki.modules.wiki;

import com.mongodb.client.DistinctIterable;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.service.AService;
import it.algos.vaadwiki.modules.nome.Nome;
import it.algos.vaadwiki.modules.nome.NomeRepository;
import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static it.algos.vaadwiki.application.WikiCost.LAST_ELABORA_NOME;
import static it.algos.vaadwiki.application.WikiCost.TAG_NOM;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 10-set-2019
 * Time: 09:16
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
@AIScript(sovrascrivibile = false)
public class NomeCognomeService extends AService {

    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public NomeCognomeService(@Qualifier(TAG_NOM) MongoRepository repository) {
        super(repository);
    }// end of Spring constructor

    /**
     * Cancella i nomi esistenti <br>
     * Crea tutti i nomi <br>
     * Controlla che ci siano almeno n voci biografiche per il singolo nome <br>
     * Registra la entity <br>
     * Non registra la entity col nome mancante <br>
     */
    public void crea() {
    }// end of method

    /**
     * Controlla che ci siano almeno n voci biografiche per il singolo nome <br>
     */
    public void update() {
    }// end of method

}// end of class
