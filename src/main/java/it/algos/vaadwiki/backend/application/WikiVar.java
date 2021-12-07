package it.algos.vaadwiki.backend.application;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 07-dic-2021
 * Time: 09:07
 * <p>
 * Classe statica (astratta) per le variabili specifiche dell'applicazione <br>
 * Le variabili (static) sono uniche per tutta l'applicazione <br>
 * Il valore delle variabili è unico per tutta l'applicazione, ma può essere modificato <br>
 * The compiler automatically initializes class fields to their default values before setting <br>
 * them with any initialization values, so there is no need to explicitly set a field to its <br>
 * default value. <br>
 * Further, under the logic that cleaner code is better code, it's considered poor style to do so. <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WikiVar {

    /**
     * Regola la categoria di riferimento per le Biografie <br>
     * Di norma (a regime) uguale a BioBot <br>
     * Può essere regolata diversamente a fini di sviluppo (test e debug) <br>
     * Deve essere regolata in backend.boot.WikiBoot.fixVariabili() del progetto corrente <br>
     */
    public static String categoriaBio;

}
