package it.algos.vaadflow14.ui.home;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.ui.*;

import javax.annotation.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mar, 18-ago-2020
 * Time: 21:30
 */
@Route(value = "", layout = MainLayout.class)
public class HomeView extends VerticalLayout {


    public HomeView() {
        if (FlowVar.usaSecurity) {
            add(new Span("Usa la security"));
        } else {
            add(new Span("Non usa la security"));
        }

        if (FlowVar.usaCompany) {
            add(new Span("Usa diverse company"));
        } else {
            add(new Span("Non usa company"));
        }
        add(new Span("Usa @Route e usa MainLayout.class"));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
    }


    /**
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     */
    @PostConstruct
    protected void postConstruct() {
    }


}
