package it.algos.vaadflow14.backend.wrapper;

import com.vaadin.flow.component.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.ui.enumeration.*;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: ven, 19-giu-2020
 * Time: 11:35
 */
public class WrapEvento {

    private AEAction azione;

    private AEntity entityBean;

    private String searchFieldValue;

    private AbstractField.ComponentValueChangeEvent evento;


    public WrapEvento(AEAction azione) {
        this.azione = azione;
    }


    public WrapEvento(AEAction azione, AEntity entityBean) {
        this.azione = azione;
        this.entityBean = entityBean;
    }


    @Deprecated
    public WrapEvento(AEAction azione, AbstractField.ComponentValueChangeEvent evento) {
        this.azione = azione;
        this.evento = evento;
    }


    public WrapEvento(AEAction azione, AEntity entityBean, String searchFieldValue) {
        this.azione = azione;
        this.entityBean = entityBean;
        this.searchFieldValue = searchFieldValue;
    }


    public AEAction getAzione() {
        return azione;
    }


    public AEntity getEntityBean() {
        return entityBean;
    }


    public String getSearchFieldValue() {
        return searchFieldValue;
    }

}
