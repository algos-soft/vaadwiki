package it.algos.vaadflow.ui.dialog;

import com.vaadin.flow.component.dialog.Dialog;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: ven, 12-ott-2018
 * Time: 20:30
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class ADialog extends Dialog implements IADialog {

    /**
     * Opens the given item for editing in the dialog.
     *
     * @param item      The item to edit; it may be an existing or a newly created instance
     * @param operation The operation being performed on the item
     */
    @Override
    public void open(AEntity item, EAOperation operation) {
    }// end of method

    /**
     * Opens the given item for editing in the dialog.
     *
     * @param item      The item to edit; it may be an existing or a newly created instance
     * @param operation The operation being performed on the item
     * @param context   legato alla sessione
     */
    @Override
    public void open(AEntity item, EAOperation operation, AContext context) {
    }// end of method

    /**
     * Opens the given item for editing in the dialog.
     *
     * @param item      The item to edit; it may be an existing or a newly created instance
     * @param operation The operation being performed on the item
     * @param context   legato alla sessione
     * @param title     of the window dialog
     */
    @Override
    public void open(AEntity item, EAOperation operation, AContext context, String title) {
    }// end of method

}// end of class
