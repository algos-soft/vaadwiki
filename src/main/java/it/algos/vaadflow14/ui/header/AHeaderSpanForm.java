package it.algos.vaadflow14.ui.header;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.enumeration.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: ven, 12-mar-2021
 * Time: 19:56
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AHeaderSpanForm extends AHeaderSpan {


    /**
     * Costruttore base con parametro <br>
     * Non usa @Autowired perché l' istanza viene creata con appContext.getBean(AHeaderSpanForm.class, message) <br>
     */
    public AHeaderSpanForm(final String message) {
        super();
        super.message = message;
    }

    @Override
    protected void initView() {
        Span span;

        if (text.isValid(message)) {
            span = new Span(message);
            span.setText(message);
            span.getElement().getStyle().set(AETypeColor.verde.getTag(), AETypeColor.verde.get());
            span.getElement().getStyle().set(AETypeWeight.bold.getTag(), AETypeWeight.bold.get());
            span.getElement().getStyle().set(AETypeSize.smaller.getTag(), AETypeSize.smaller.get());
            this.add(span);
        }
    }

}