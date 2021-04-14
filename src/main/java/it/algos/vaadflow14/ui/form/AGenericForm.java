package it.algos.vaadflow14.ui.form;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.service.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 27-ago-2020
 * Time: 13:52
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AGenericForm extends AForm {

    @Deprecated
    public AGenericForm(AILogic logic, WrapForm wrap) {
        //        super(logic, wrap);
    }

    public AGenericForm(AIService service, AILogic logic, WrapForm wrap) {
        super(service, logic, wrap);
    }


}
