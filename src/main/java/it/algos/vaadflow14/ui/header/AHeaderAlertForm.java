package it.algos.vaadflow14.ui.header;

import com.vaadin.flow.component.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.enumeration.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 12-mar-2021
 * Time: 19:56
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AHeaderAlertForm extends AHeaderAlert {


    /**
     * Costruttore base con parametro <br>
     * Non usa @Autowired perché l' istanza viene creata con appContext.getBean(AHeaderSpanForm.class, spanList) <br>
     */
    public AHeaderAlertForm(final List<Component> listaAlert) {
        super(listaAlert);
    }


    @Override
    protected void initView() {
        Component comp;

        if (listaAlert != null && listaAlert.size() > 0) {
            comp = listaAlert.get(0);
            comp.getElement().getStyle().set(AETypeColor.verde.getTag(), AETypeColor.verde.get());
            comp.getElement().getStyle().set(AETypeWeight.bold.getTag(), AETypeWeight.bold.get());
            //            comp.getElement().getStyle().set(AETypeSize.smaller.getTag(), AETypeSize.smaller.get());
            this.add(comp);

            for (int k = 1; k < listaAlert.size(); k++) {
                this.add(listaAlert.get(k));
            }
        }
    }

}