package it.algos.vaadwiki.backend.application;

import com.vaadin.flow.spring.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

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

    /**
     * Regola il simbolo grafico da preporre alle date di nascita o, in alternativa, alle località di nascita <br>
     * Mantenuto nelle preferenze con la caratteristica di necessitare del riavvio <br>
     * Viene letto ogni volta dalle preferenze all'avvio del programma e rimane poi immutabile <br>
     * Deve essere regolata in backend.boot.WikiBoot.fixVariabili() del progetto corrente <br>
     */
    public static String simboloNato;

    /**
     * Regola il simbolo grafico da preporre alle date di nascita o, in alternativa, alle località di nascita <br>
     * Mantenuto nelle preferenze con la caratteristica di necessitare del riavvio <br>
     * Viene letto ogni volta dalle preferenze all'avvio del programma e rimane poi immutabile <br>
     * Deve essere regolata in backend.boot.WikiBoot.fixVariabili() del progetto corrente <br>
     */
    public static String simboloMorto;

}
