package it.algos.vaadflow14.ui.validator;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow14.backend.service.ATextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 11-set-2020
 * Time: 20:45
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ACapValidator implements Validator {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ATextService text;


    /**
     * Costruttore con parametri <br>
     * L' istanza viene costruita con appContext.getBean(ACapValidator.class) <br>
     */
    public ACapValidator() {
    } // end of SpringBoot constructor


    @Override
    public ValidationResult apply(Object value, ValueContext valueContext) {
        if (!text.isValid(value)) {
            return ValidationResult.ok();
        }

        String patterns = "^[0-9]{5}$";
        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher = pattern.matcher((String) value);

        return matcher.matches() ? ValidationResult.ok() : ValidationResult.error("Cap non valido (5 numeri)");
    }


    /**
     * Applies this function to the given arguments.
     *
     * @param o  the first function argument
     * @param o2 the second function argument
     *
     * @return the function result
     */
    @Override
    public Object apply(Object o, Object o2) {
        return null;
    }

}
