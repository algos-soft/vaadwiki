package it.algos.vaadflow14.ui.header;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.spring.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import java.util.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mar, 16-feb-2021
 * Time: 11:30
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AHeaderSpanList extends AHeaderSpan {


    /**
     * Costruttore base con parametro <br>
     * Non usa @Autowired perché l' istanza viene creata con appContext.getBean(AHeaderSpanList.class, spanList) <br>
     */
    public AHeaderSpanList(List<Span> spanList) {
        super();
        super.spanList = spanList;
    }


    @Override
    protected void initView() {
        if (spanList != null && spanList.size() > 0) {
            for (Span span : spanList) {
                this.add(span);
            }
        }
    }

}
