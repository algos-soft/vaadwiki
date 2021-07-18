package it.algos.vaadflow14.ui.list;

import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.router.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.*;
import it.algos.vaadflow14.ui.view.*;
import org.springframework.beans.factory.annotation.*;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: mar, 28-apr-2020
 * Time: 19:00
 * Superclasse per visualizzare la Grid <br>
 */
@Route(value = ROUTE_NAME_GENERIC_LIST+"xyz", layout = MainLayout.class)
public class AViewList extends AView {

    @Autowired
    private DataProviderService dataService;

    /**
     * Costruttore @Autowired <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Nella sottoclasse concreta si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Nella sottoclasse concreta si usa una costante statica, per scrivere sempre uguali i riferimenti <br>
     * Passa nella superclasse anche la entityClazz che viene definita qui (specifica di questo modulo) <br>
     */
    public AViewList() {
    }// end of Vaadin/@Route constructor




    /**
     * Costruisce il 'corpo' centrale della view <br>
     * <p>
     * Differenziato tra AViewList e AViewForm <br>
     * Costruisce un' istanza dedicata <br>
     * Inserisce l' istanza (grafica) in bodyPlacehorder della view <br>
     */
    @Override
    public void fixBodyLayout() {
//        AGrid grid = entityLogic.getBodyGridLayout();

        if (bodyPlaceholder != null) {
            bodyPlaceholder.add(creaGrid());
        }
        bodyPlaceholder.setHeight("100%");

        this.add(bodyPlaceholder);
        setHeight("100%");
//        creaGrid();
    }


    private Grid creaGrid() {
//        AGrid grid = entityLogic.getBodyGridLayout();
//        DataProvider dataProvider = dataService.creaDataProvider(entityClazz);
//        grid.getGrid().setDataProvider(dataProvider);
//        grid.getGrid().setHeight("100%");
//        return grid.getGrid();
        return null;
    }

//    /**
//     * Costruisce un layout per i bottoni di comando in footerPlacehorder della view <br>
//     * <p>
//     * Chiamato da AView.initView() <br>
//     * Tipicamente usato SOLO nel Form <br>
//     * Nell' implementazione standard di default presenta solo il bottone 'New' <br>
//     * Recupera dal service specifico i menu/bottoni previsti <br>
//     * Costruisce un' istanza dedicata con i bottoni <br>
//     * Inserisce l' istanza (grafica) in bottomPlacehorder della view <br>
//     */
//    @Override
//    protected void fixBottomLayout() {
//    }

}
