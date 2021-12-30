package it.algos.vaadwiki.backend.packages.bio;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.ui.wrapper.*;
import it.algos.vaadwiki.backend.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.*;
import org.springframework.context.annotation.Scope;

import javax.annotation.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 28-dic-2021
 * Time: 20:45
 * Form di supporto nel tab della view base per mostrare le properties sotto forma di didascalie <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BioFormDidascalie extends AVerticalLayout {

    /**
     * Istanza di una interfaccia <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ApplicationContext appContext;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public WikiBotService wikiBot;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public DidascaliaService didascaliaService;

    private String minWidthForm = "40em";

    private Bio bioBean;


    public BioFormDidascalie(final Bio bioBean) {
        this.bioBean = bioBean;
        this.setWidth(minWidthForm);
    }

    /**
     * Performing the initialization in a constructor is not suggested <br>
     * as the state of the UI is not properly set up when the constructor is invoked. <br>
     * <p>
     * La injection viene fatta da SpringBoot solo alla fine del metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * L'istanza può essere creata con  appContext.getBean(xxxClass.class);  oppure con @Autowired <br>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     */
    @PostConstruct
    protected void postConstruct() {
        this.add(get("Incipit wikipedia"));
        this.add(getIncipit());
        this.add(getSpazio());
        this.add(get("Attività"));
        this.add(getAttivita());
        this.add(getSpazio());
        this.add(get("Nazionalità"));
        this.add(getNazionalita());
        this.add(getSpazio());
    }

    protected Component get(final String value) {
        return new Div(new Text(value));
    }

    protected Component getSpazio() {
        Label emptyLabel2 = new Label(VUOTA);
        emptyLabel2.setHeight("1em");
        return emptyLabel2;
    }

    protected Component getIncipit() {
        String didascalia = didascaliaService.getLista(bioBean);
        return new Div(new Text(didascalia));
    }

    protected Component getAttivita() {
        return new Div(new Text(didascaliaService.getAttivitaNazionalita(bioBean)));
    }


    protected Component getNazionalita() {
        return new Div(new Text(didascaliaService.getAttivitaNazionalita(bioBean)));
    }

}
