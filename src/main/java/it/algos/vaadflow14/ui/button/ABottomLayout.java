package it.algos.vaadflow14.ui.button;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.ui.enumeration.*;
import it.algos.vaadflow14.ui.interfaces.*;
import it.algos.vaadflow14.ui.wrapper.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: mer, 20-mag-2020
 * Time: 07:21
 * <p>
 * Layer per eventuali bottoni sotto la Grid della List <br>
 * <p>
 * I bottoni vengono passati nel wrapper come enumeration standard. <br>
 * Se si usano bottoni non standard vengono inseriti per ultimi <br>
 * I listeners dei bottoni non standard devono essere regolati singolarmente dalla Logic chiamante <br>
 * La classe viene costruita con appContext.getBean(ABottomLayout.class, wrapButtons) in LogicList <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ABottomLayout extends AButtonLayout {


    /**
     * Costruttore base con parametro wrapper di passaggio dati <br>
     * La classe viene costruita con appContext.getBean(xxxLayout.class, wrapButtons) in AEntityService <br>
     *
     * @param wrapButtons wrap di informazioni
     */
    public ABottomLayout(WrapButtons wrapButtons) {
        super(wrapButtons);
    }


    /**
     * Qui va tutta la logica iniziale della view <br>
     * Può essere sovrascritto. Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void initView() {
        super.initView();
        super.rigaUnica = new AHorizontalLayout();
    }


    @Override
    protected void creaAllBottoni() {
        if (listaAEBottoni != null && listaAEBottoni.size() > 0) {
            for (AIButton bottone : listaAEBottoni) {
                this.addBottoneEnum(bottone);
            }
        }

        if (listaBottoniSpecifici != null && listaBottoniSpecifici.size() > 0) {
            for (Button bottone : listaBottoniSpecifici) {
                rigaUnica.add(bottone);
            }
        }
    }


    protected void addBottoneEnum(AIButton aeButton) {
        Button button = getButton(aeButton);
        rigaUnica.add(button);
    }


    @Override
    protected void addAllToView() {
        if (rigaUnica.getComponentCount() > 0) {
            this.add(rigaUnica);
        }
    }

}
