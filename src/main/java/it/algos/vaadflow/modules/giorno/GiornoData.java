package it.algos.vaadflow.modules.giorno;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.backend.data.AData;
import it.algos.vaadflow.modules.mese.Mese;
import it.algos.vaadflow.modules.mese.MeseService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.IAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.HashMap;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 07-ott-2018
 * Time: 21:23
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class GiornoData extends AData {


    @Autowired
    private ADateService dateService;

    @Autowired
    private MeseService meseService;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param service di collegamento per la Repository
     */
    @Autowired
    public GiornoData(@Qualifier(TAG_GIO) IAService service) {
        super(Giorno.class, service);
    }// end of Spring constructor



    /**
     * Creazione della collezione
     */
    /**
     * Crea 366 records per tutti i giorni dell'anno (compreso bisestile)
     * <p>
     */
    public int creaAll() {
        int num = 0;
        ArrayList<HashMap> lista;
        String titolo;
        int bisestile;
        Mese mese ;

        //costruisce i 366 records
        lista = dateService.getAllGiorni();
        for (HashMap mappaGiorno : lista) {
            titolo = (String) mappaGiorno.get(KEY_MAPPA_GIORNI_TITOLO);
            bisestile = (int) mappaGiorno.get(KEY_MAPPA_GIORNI_BISESTILE);
            mese = meseService.findByKeyUnica((String) mappaGiorno.get(KEY_MAPPA_GIORNI_MESE_TESTO));
            ((GiornoService)service).crea(mese, bisestile, titolo);
        }// end of for cycle

        return num;
    }// end of method

}// end of class
