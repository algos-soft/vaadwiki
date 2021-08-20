package it.algos.vaadwiki.backend.packages.bio;

import com.vaadin.flow.component.formlayout.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.ui.fields.*;
import it.algos.vaadwiki.backend.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.*;
import org.springframework.context.annotation.Scope;

import javax.annotation.*;
import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 17-giu-2021
 * Time: 21:39
 * Form di supporto nel tab della view base per mostrare le properties sotto forma di mappe <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BioFormMaps extends FormLayout {

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

    private String minWidthForm = "40em";

    private Bio bioBean;


    public BioFormMaps(final Bio bioBean) {
        this.bioBean = bioBean;
        this.setResponsiveSteps(new FormLayout.ResponsiveStep(minWidthForm, 1));
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
        Map<String, String> mappa = wikiBot.getMappaDownload(bioBean.tmplBio);
        AField<Map<String, String>> field = appContext.getBean(AMappaField.class);
        field.setValue(mappa);
        this.add(field);
    }

}
