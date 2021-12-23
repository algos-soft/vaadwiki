package it.algos.vaadflow14.ui.validator;

import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.util.regex.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: dom, 16-ago-2020
 * Time: 19:08
 * https://www.baeldung.com/java-regex-validate-phone-numbers
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class APhoneValidator implements Validator {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public TextService text;


    /**
     * Costruttore con parametri <br>
     * L' istanza viene costruita con appContext.getBean(APhoneValidator.class) <br>
     */
    public APhoneValidator() {
    } // end of SpringBoot constructor


    @Override
    public ValidationResult apply(Object value, ValueContext valueContext) {
        if (!text.isValid(value)) {
            return ValidationResult.ok();
        }

        String patterns = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"

                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"

                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$"

                + "|^(0{1}[1-9]{1,3})[\\s|\\.|\\-]?(\\d{5,})$";
        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher = pattern.matcher((String) value);

        return matcher.matches() ? ValidationResult.ok() : ValidationResult.error("Numero di telefono non valido");
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
