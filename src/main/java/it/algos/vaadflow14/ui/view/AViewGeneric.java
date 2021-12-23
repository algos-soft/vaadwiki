package it.algos.vaadflow14.ui.view;

import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: sab, 10-ott-2020
 * Time: 18:16
 * Layer di collegamento tra la classe AView e la sottoclasse generica che NON usa ne AViewList, ne AViewForm <br>
 * Si usa per viste NON legate direttamente ad un package del database <br>
 * Permette di mantenere alcuni automatismi delle altre viste, pur senza usare AGrid e AForm <br>
 */
@SpringComponent
public abstract class AViewGeneric extends AView {

    /**
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     */
    public AViewGeneric() {
        super();
    }// end of Vaadin/@Route constructor


    /**
     * Creazione iniziale (business logic) della view DOPO costruttore, init(), postConstruct() e setParameter() <br>
     * <p>
     * Chiamato da com.vaadin.flow.router.Router tramite l' interfaccia BeforeEnterObserver <br>
     * Chiamato DOPO @PostConstruct e DOPO setParameter() <br>
     * Le property necessarie sono già state regolate <br>
     *
     * @param beforeEnterEvent con la location, ui, navigationTarget, source, ecc
     */
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        //--Costruisce gli oggetti base (placeholder) di questa view
        super.fixLayout();

        this.initView();

        //        //--Costruisce un (eventuale) layout per informazioni aggiuntive come header della view
//        this.fixHeaderLayout();
//
//        //--Costruisce un layout (obbligatorio) per i menu ed i bottoni di comando della view
//        this.fixTopLayout();
//
//        //--body con la Grid oppure il Form o qualsiasi altro componente grafico
//        this.fixBody();
//
//        //--eventuale barra di bottoni in basso
//        this.fixBottomLayout();

        //--aggiunge il footer standard
        this.fixFooterLayout();
    }

}
