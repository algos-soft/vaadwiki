package it.algos.vaadwiki.backend.didascalia;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.backend.enumeration.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 14-ago-2021
 * Time: 19:27
 */
public abstract class Didascalia {
    protected EDidascalia type;

    protected Bio bio;


    public Didascalia() {
    }// end of constructor


    public Didascalia(Bio bio, EDidascalia type) {
        this.bio = bio;
        this.type = type;
        this.fixPreferenze();
    }// end of constructor

    /**
     * Preferenze usate da questa 'didascalia' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
    }

}
