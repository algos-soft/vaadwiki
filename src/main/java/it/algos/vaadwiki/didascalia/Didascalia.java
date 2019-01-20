package it.algos.vaadwiki.didascalia;

import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 18-gen-2019
 * Time: 19:07
 * Didascalia specializzata per le liste costruibili a partire dal template Bio.
 * Cronologiche (in namespace principale) di nati e morti nel giorno o nell'anno
 * Attività e nazionalità (in Progetto:Biografie).
 * <p>
 * Sovrascritta nelle sottoclassi concrete
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public abstract class Didascalia {

}// end of class
