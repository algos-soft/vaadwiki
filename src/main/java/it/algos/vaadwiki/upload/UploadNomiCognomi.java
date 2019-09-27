package it.algos.vaadwiki.upload;

import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import static it.algos.vaadwiki.application.WikiCost.USA_FORCETOC_NOMI;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 27-set-2019
 * Time: 07:56
 */
public abstract class UploadNomiCognomi extends Upload{

    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.usaBodySottopagine = true;
    }// end of method

}// end of class
