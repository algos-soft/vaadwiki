package it.algos.vaadflow14.ui.form;

import com.vaadin.flow.router.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.ui.*;
import it.algos.vaadflow14.ui.view.*;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: mer, 13-mag-2020
 * Time: 18:45
 */
@Route(value = "old", layout = MainLayout.class)
public class AViewForm extends AView {


    /**
     *
     */
    protected void fixEntityBean() {
        String keyID;

        keyID = routeParameter.get(KEY_BEAN_ENTITY) != null ? routeParameter.get(KEY_BEAN_ENTITY) : VUOTA;
        if (text.isEmpty(keyID) || keyID.equals(KEY_NULL)) {
            entityBean = entityService.newEntity();
        } else {
            entityBean = entityService.findById(keyID);
        }
    }


    /**
     * Costruisce un layout per i bottoni di comando in topPlacehorder della view <br>
     * <p>
     * Chiamato da AView.initView() <br>
     * Tipicamente usato SOLO nella List <br>
     * Nell'implementazione standard di default presenta solo il bottone 'New' <br>
     * Recupera dal service specifico i menu/bottoni previsti <br>
     * Costruisce un'istanza dedicata con i bottoni <br>
     * Inserisce l'istanza (grafica) in topPlacehorder della view <br>
     */
    @Override
    protected void fixTopLayout() {
    }


    /**
     * Costruisce il 'corpo' centrale della view <br>
     * <p>
     * Differenziato tra AViewList e AViewForm <br>
     * Costruisce un'istanza dedicata <br>
     * Inserisce l'istanza (grafica) in bodyPlacehorder della view <br>
     */
    @Override
    public void fixBodyLayout() {
        AForm form;

        if (entityClazz == null) {
            logger.error("Manca entityClazz", this.getClass(), "fixBody");
        }

        if (entityLogic == null) {
            logger.error("Manca entityLogic", this.getClass(), "fixBody");
        }

        if (entityBean != null) {
//            form = entityLogic.getBodyFormLayout(entityBean); //@todo Linea di codice provvisoriamente commentata e DA RIMETTERE
        } else {
            logger.warn("Manca entityBean", this.getClass(), "fixBody");
//            form = entityLogic.getBodyFormLayout(entityLogic.newEntity()); //@todo Linea di codice provvisoriamente commentata e DA RIMETTERE
        }

         //@todo Linea di codice provvisoriamente commentata e DA RIMETTERE
//        if (bodyPlaceholder != null && form != null) {
//            bodyPlaceholder.add(form);
//        }

        this.add(bodyPlaceholder);
    }


//    /**
//     * Costruisce un layout per i bottoni di comando in footerPlacehorder della view <br>
//     * <p>
//     * Chiamato da AView.initView() <br>
//     * Tipicamente usato SOLO nel Form <br>
//     * Nell'implementazione standard di default presenta solo il bottone 'New' <br>
//     * Recupera dal service specifico i menu/bottoni previsti <br>
//     * Costruisce un'istanza dedicata con i bottoni <br>
//     * Inserisce l'istanza (grafica) in bottomPlacehorder della view <br>
//     */
//    @Override
//    protected void fixBottomLayout() {
//        ABottomLayout bottomLayout = null;
//
//        if (entityLogic != null) {
//            bottomLayout = entityLogic.getBottomLayout(operationForm);
//        }
//
//        if (bottomPlaceholder != null && bottomLayout != null) {
//            bottomPlaceholder.add(bottomLayout);
//        }
//
//        this.add(bottomPlaceholder);
//    }

}
