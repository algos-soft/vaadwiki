package it.algos.vaadflow14.wizard;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.dependency.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.application.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.ui.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import it.algos.vaadflow14.wizard.scripts.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;

import javax.annotation.*;


/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: sab, 6-feb-2021
 * Time: 17:56
 * Utilizzato da VaadFlow14 direttamente, per creare/aggiornare un nuovo progetto esterno <br>
 * Utilizzato dal progetto corrente, per importare/aggiornare il codice da VaadFlow14 <br>
 * Utilizzato dal progetto corrente, per creare/aggiornare nuovi packages forse <br>
 */
@Route(value = FlowCost.TAG_WIZ, layout = MainLayout.class)
@SpringComponent
@UIScope
@PageTitle(FlowCost.TAG_WIZ)
@Qualifier(FlowCost.TAG_WIZ)
@CssImport("./styles/shared-styles.css")
public class Wizard extends VerticalLayout {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
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
    public ALogService logger;


    /**
     * Utilizzato dall'interno di VaadFlow14 per creare un nuovo progetto <br>
     */
    @Autowired
    private WizDialogNewProject dialogNewProject;

    /**
     * Utilizzato dall'interno di VaadFlow14 per aggiornare un progetto esistente <br>
     * Utilizzato dall'interno di un qualsiasi progetto per importare/aggiornare il codice da VaadFlow14 <br>
     */
    @Autowired
    private WizDialogUpdateProject dialogUpdateProject;

    /**
     * Utilizzato dall'interno di VaadFlow14 per aggiornare un progetto esistente <br>
     * Utilizzato dall'interno di un qualsiasi progetto per importare/aggiornare il codice da VaadFlow14 <br>
     */
    @Autowired
    private WizDialogUpdateModulo dialogUpdateModulo;

    /**
     * Utilizzato dall'interno di un qualsiasi progetto per creare nuovi packages <br>
     */
    @Autowired
    private WizDialogNewPackage dialogNewPackage;

    /**
     * Utilizzato dall'interno di un qualsiasi progetto per aggiornare i packages esistenti <br>
     */
    @Autowired
    private WizDialogUpdatePackage dialogUpdatePackage;

    /**
     * Utilizzato dall'interno di un qualsiasi progetto per aggiornare la documentazione delle varie classi standard <br>
     */
    @Autowired
    private WizDialogDocPackages dialogDocPackages;

    /**
     * Utilizzato dall'interno di un qualsiasi progetto per aggiornare la directory wizard di VaadFlow14 <br>
     */
    @Autowired
    private WizDialogFeedbackWizard dialogFeedbackWizard;

    /**
     * Utilizzato dall'interno di VaadFlow14 per creare un nuovo progetto <br>
     */
    @Autowired
    private WizElaboraNewProject elaboraNewProject;

    /**
     * Utilizzato dall'interno di VaadFlow14 per aggiornare un progetto esistente <br>
     * Utilizzato dall'interno di un qualsiasi progetto per importare/aggiornare il codice da VaadFlow14 <br>
     */
    @Autowired
    private WizElaboraUpdateProject elaboraUpdateProject;

    /**
     * Utilizzato dall'interno di VaadFlow14 per aggiornare un progetto esistente <br>
     * Utilizzato dall'interno di un qualsiasi progetto per importare/aggiornare il codice da VaadFlow14 <br>
     */
    @Autowired
    private WizElaboraUpdateModulo elaboraUpdateModulo;


    /**
     * Utilizzato dall'interno di un qualsiasi progetto per creare nuovi packages <br>
     */
    @Autowired
    private WizElaboraNewPackage elaboraNewPackage;

    /**
     * Utilizzato dall'interno di un qualsiasi progetto per aggiornare i packages esistenti <br>
     */
    @Autowired
    private WizElaboraUpdatePackage elaboraUpdatePackage;

    /**
     * Utilizzato dall'interno di un qualsiasi progetto per aggiornare la documentazione delle varie classi standard <br>
     */
    @Autowired
    private WizElaboraDocPackages elaboraDocPackages;


    /**
     * Utilizzato dall'interno di un qualsiasi progetto per aggiornare la directory wizard di VaadFlow14 <br>
     */
    @Autowired
    private WizElaboraFeedbackWizard elaboraFeedbackWizard;


    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    private WizService wizService;


    /**
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     */
    public Wizard() {
        super();
    }// end of Vaadin/@Route constructor


    /**
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le (eventuali) istanze @Autowired <br>
     * Questo metodo viene chiamato subito dopo che il framework ha terminato l' init() implicito <br>
     * del costruttore e PRIMA di qualsiasi altro metodo <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l' ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     */
    @PostConstruct
    protected void postConstruct() {
        initView();
    }


    /**
     * Qui va tutta la logica iniziale della view principale <br>
     * Per adesso regolo SOLO le costanti base <br>
     * Per adesso posso solo selezionare se sono in VaadFlow14 oppure no <br>
     * In base a questo decido quale paragrafo/possibilità mostrare <br>
     */
    protected void initView() {
        this.setMargin(true);
        this.setPadding(false);
        this.setSpacing(false);
        this.titolo();

        wizService.fixVariabiliIniziali();
        wizService.printInfoStart();

        if (AEFlag.isBaseFlow.is()) {
            paragrafoNewProject();
        }

        paragrafoUpdateProject();
        paragrafoUpdateModulo();
        paragrafoNewPackage();
        paragrafoUpdatePackage();
        paragrafoDocPackages();

        if (!AEFlag.isBaseFlow.is()) {
            paragrafoFeedBackWizard();
        }
    }


    public void titolo() {
        H1 titolo = new H1("Gestione Wizard");
        titolo.getElement().getStyle().set("color", "green");
        this.add(titolo);
    }


    public void paragrafoNewProject() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(false);
        layout.setPadding(false);
        layout.setSpacing(false);
        H3 paragrafo = new H3(WizCost.TITOLO_NUOVO_PROGETTO);
        paragrafo.getElement().getStyle().set("color", "blue");

        layout.add(new Label("Crea un nuovo project IntelliJIdea, nella directory 'IdeaProjects'"));
        layout.add(new Label("Seleziona un progetto vuoto tra quelli esistenti. Regola alcuni flags iniziali"));
        layout.add(new Label("Il progetto va poi spostato nella directory 'operativi' e agiunto alla enumeration AEProgetto"));

        Button bottone = new Button("Crea project");
        bottone.getElement().setAttribute("theme", "primary");
        dialogNewProject = appContext.getBean(WizDialogNewProject.class);
        bottone.addClickListener(event -> dialogNewProject.open(this::elaboraNewProject));

        this.add(paragrafo);
        layout.add(bottone);
        this.add(layout);
    }


    private void elaboraNewProject() {
        dialogNewProject.close();
        elaboraNewProject.esegue();
    }


    public void paragrafoUpdateProject() {
        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout bottoni = new HorizontalLayout();
        layout.setMargin(false);
        layout.setPadding(false);
        layout.setSpacing(false);
        H3 paragrafo;
        Label label;

        if (AEFlag.isBaseFlow.is()) {
            paragrafo = new H3(WizCost.TITOLO_MODIFICA_PROGETTO);
            label = new Label("Seleziona un progetto dalla enumeration AEProgetto. Regola alcuni flags per le modifiche");
        }
        else {
            paragrafo = new H3(String.format("Modifica del modulo %s del progetto %s", AEWizCost.nameVaadFlow14Lower.get(), AEWizCost.nameCurrentProjectUpper.get()));
            label = new Label("Update del modulo base di questo progetto. Regola alcuni flags per le modifiche");
        }
        paragrafo.getElement().getStyle().set("color", "blue");

        Button bottone = new Button(String.format("Update modulo %s", AEWizCost.nameVaadFlow14Lower.get()));
        bottone.getElement().setAttribute("theme", "primary");
        bottone.addClickListener(event -> dialogUpdateProject.open(this::elaboraUpdateProject));
        bottoni.add(bottone);

        this.add(paragrafo);
        layout.add(label, bottoni);
        this.add(layout);
    }

    private void elaboraUpdateProject() {
        elaboraUpdateProject.esegue();
        dialogUpdateProject.close();
    }

    public void paragrafoUpdateModulo() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(false);
        layout.setPadding(false);
        layout.setSpacing(false);
        H3 paragrafo;
        Label label;

        if (AEFlag.isBaseFlow.is()) {
            paragrafo = new H3(WizCost.TITOLO_MODIFICA_MODULO);
            label = new Label("Seleziona un modulo dalla enumeration AEProgetto. Regola alcuni flags per le modifiche");
        }
        else {
            paragrafo = new H3(String.format("Modifica del modulo %s del progetto %s", AEWizCost.nameCurrentProjectModulo.get(), AEWizCost.nameCurrentProjectUpper.get()));
            label = new Label("Update del modulo corrente di questo progetto. Regola alcuni flags per le modifiche");
        }
        paragrafo.getElement().getStyle().set("color", "blue");

        Button bottone = new Button(String.format("Update modulo %s", AEWizCost.nameCurrentProjectModulo.get()));
        bottone.getElement().setAttribute("theme", "primary");
        bottone.addClickListener(event -> dialogUpdateModulo.open(this::elaboraUpdateModulo));

        this.add(paragrafo);
        layout.add(label, bottone);
        this.add(layout);
    }

    private void elaboraUpdateModulo() {
        elaboraUpdateModulo.esegue();
        dialogUpdateModulo.close();
    }


    public void paragrafoNewPackage() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(false);
        layout.setPadding(false);
        layout.setSpacing(false);
        H3 paragrafo;
        Label label;

        if (AEFlag.isBaseFlow.is()) {
            paragrafo = new H3(WizCost.TITOLO_NEW_PACKAGE);
        }
        else {
            paragrafo = new H3(String.format("Nuovo package per il modulo %s", AEWizCost.nameCurrentProjectModulo.get()));
        }
        paragrafo.getElement().getStyle().set("color", "blue");
        label = new Label("Creazione di un nuovo package. Regola alcuni flags di possibili opzioni");

        if (AEFlag.isBaseFlow.is()) {
            Button bottone = new Button(String.format("New package %s", AEWizCost.nameVaadFlow14Lower.get()));
            bottone.getElement().setAttribute("theme", "primary");
            bottone.addClickListener(event -> dialogNewPackage.open(this::elaboraNewPackage));

            Button bottone2 = new Button(String.format("New package %s", AEWizCost.nameCurrentProjectModulo.get()));
            bottone2.getElement().setAttribute("theme", "primary");
            bottone2.addClickListener(event -> dialogNewPackage.open(this::elaboraNewPackage));

            layout.add(label, new HorizontalLayout(bottone, bottone2));
        }
        else {
            Button bottone = new Button(String.format("New package %s", AEWizCost.nameCurrentProjectModulo.get()));
            bottone.getElement().setAttribute("theme", "primary");
            bottone.addClickListener(event -> dialogNewPackage.open(this::elaboraNewPackage));

            layout.add(label, bottone);
        }

        this.add(paragrafo);
        this.add(layout);
    }


    private void elaboraNewPackage() {
        elaboraNewPackage.esegue();
        dialogNewPackage.close();
    }


    public void paragrafoUpdatePackage() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(false);
        layout.setPadding(false);
        layout.setSpacing(false);
        H3 paragrafo;
        Label label;
        Label label2;

        if (AEFlag.isBaseFlow.is()) {
            paragrafo = new H3(WizCost.TITOLO_UPDATE_PACKAGE);
        }
        else {
            paragrafo = new H3(String.format("Modifica package del modulo %s", AEWizCost.nameCurrentProjectModulo.get()));
        }
        paragrafo.getElement().getStyle().set("color", "blue");
        label = new Label("Update di un package esistente");
        label2 = new Label("Seleziona il package dalla lista di quelli esistenti. Regola alcuni flags di possibili opzioni");

        if (AEFlag.isBaseFlow.is()) {
            Button bottone = new Button("Update package " + AEWizCost.nameVaadFlow14Lower.get());
            bottone.getElement().setAttribute("theme", "primary");
            bottone.addClickListener(event -> dialogNewPackage.open(this::elaboraUpdatePackage));

            Button bottone2 = new Button("Update package " + AEWizCost.nameCurrentProjectModulo.get());
            bottone2.getElement().setAttribute("theme", "primary");
            bottone2.addClickListener(event -> dialogNewPackage.open(this::elaboraUpdatePackage));

            layout.add(label, label2, new HorizontalLayout(bottone, bottone2));
        }
        else {
            Button bottone = new Button(String.format("Update package %s", AEWizCost.nameCurrentProjectModulo.get()));
            bottone.getElement().setAttribute("theme", "primary");
            bottone.addClickListener(event -> dialogUpdatePackage.open(this::elaboraUpdatePackage));

            layout.add(label, label2, bottone);
        }

        this.add(paragrafo);
        this.add(layout);
    }

    private void elaboraUpdatePackage() {
        elaboraUpdatePackage.esegue();
        dialogUpdatePackage.close();
    }

    public void paragrafoDocPackages() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(false);
        layout.setPadding(false);
        layout.setSpacing(false);
//        H3 paragrafo = new H3(WizCost.TITOLO_DOC_PACKAGES);
        H3 paragrafo;
        Label label;
        Label label2;

        if (AEFlag.isBaseFlow.is()) {
            paragrafo = new H3(WizCost.TITOLO_DOC_PACKAGES);
        }
        else {
            paragrafo = new H3(String.format("Documentazione del modulo %s", AEWizCost.nameCurrentProjectModulo.get()));
        }
        paragrafo.getElement().getStyle().set("color", "blue");
        label = new Label("Documentazione delle classi standard dei packages esistenti nel modulo");
        label2 = new Label("Seleziona quali classi standard modificare. Si applica a tutti i packages.");

        if (AEFlag.isBaseFlow.is()) {
            Button bottone = new Button("Doc packages " + AEWizCost.nameVaadFlow14Lower.get());
            bottone.getElement().setAttribute("theme", "primary");
            bottone.addClickListener(event -> dialogNewPackage.open(this::elaboraDocPackages));

            Button bottone2 = new Button("Doc packages " + AEWizCost.nameCurrentProjectModulo.get());
            bottone2.getElement().setAttribute("theme", "primary");
            bottone2.addClickListener(event -> dialogNewPackage.open(this::elaboraDocPackages));

            layout.add(label, label2, new HorizontalLayout(bottone, bottone2));
        }
        else {
            Button bottone = new Button(String.format("Doc package %s", AEWizCost.nameCurrentProjectModulo.get()));
            bottone.getElement().setAttribute("theme", "primary");
            bottone.addClickListener(event -> dialogDocPackages.open(this::elaboraDocPackages));

            layout.add(label, label2, bottone);
        }

        this.add(paragrafo);
        this.add(layout);
    }


    private void elaboraDocPackages() {
        elaboraDocPackages.esegue();
        dialogDocPackages.close();
    }


    public void paragrafoFeedBackWizard() {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(false);
        layout.setPadding(false);
        layout.setSpacing(false);
        H3 paragrafo = new H3(WizCost.TITOLO_FEEDBACK_PROGETTO);
        paragrafo.getElement().getStyle().set("color", "blue");
        this.add(paragrafo);

        layout.add(new Label("Ricopia su Vaadflow14 la directory wizard di questo progetto"));

        Button bottone = new Button(String.format("Send back to %s", AEWizCost.nameVaadFlow14Upper.get()));
        bottone.getElement().setAttribute("theme", "primary");
        bottone.addClickListener(event -> dialogFeedbackWizard.open(this::elaboraFeedbackWizard));

        layout.add(bottone);
        this.add(layout);
        this.add(new H2());
    }


    private void elaboraFeedbackWizard() {
        elaboraFeedbackWizard.esegue();
        dialogFeedbackWizard.close();
    }

}