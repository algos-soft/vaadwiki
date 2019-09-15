package it.algos.vaadwiki.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.HasUrlParameter;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwiki.liste.Lista;
import it.algos.vaadwiki.liste.ListaService;
import it.algos.vaadwiki.upload.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Wed, 29-May-2019
 * Time: 22:34
 * <p>
 * Classe astratta per la visualizzazione di una lista di prova di biografie di un particolare giorno/anno <br>
 * Viene invocata da WikiGiornoViewList e da WikiAnnoViewList <br>
 * Eliminato header e footer della pagina definitiva su wiki <br>
 * Due sottoclassi (concrete) per i Nati e per i Morti dei giorni <br>
 * Due sottoclassi (concrete) per i Nati e per i Morti degli anni <br>
 * Implementa l'interfaccia HasUrlParameter invece di BeforeEnterObserver <br>
 * e quindi il metodo setParameter(BeforeEvent event, ...) invece di beforeEnter(BeforeEnterEvent beforeEnterEvent <br>
 */
public abstract class ViewListe extends VerticalLayout implements HasUrlParameter<String> {


    //--property
    public boolean usaRigheRaggruppate;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ApplicationContext appContext;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected UploadService uploadService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ListaService listaService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ATextService text;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected PreferenzaService pref;

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected ADateService date;

    protected LinkedHashMap<String, ArrayList<String>> mappaSemplice;
    //--property
    public LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> mappaComplessa;

    //--property
    protected boolean usaSuddivisioneParagrafi;

    //--property
    protected String titoloParagrafoVuoto;

    //--property
    protected boolean paragrafoVuotoInCoda;

    protected TextArea area = new TextArea();

    //--property
    protected int numVoci;

    protected Lista lista;


    /**
     * Costruisce il testo con tutte le didascalie relative al giorno/anno/nome/cognome considerato <br>
     * Presenta le righe secondo uno dei possibili metodi di raggruppamento <br>
     * Deve essere sovrascritto nella sottoclassse concreta <br>
     * Dopo DEVE invocare il metodo della superclasse <br>
     */
    protected void inizia() {
        this.setSpacing(false);
        area.setSizeFull();

        //--Regola le preferenze
        fixPreferenze();

        //--Ritorno alla view chiamante con un bottone nel top
        this.add(backButton());

        //--Mostra le preferenze utilizzate
        this.addInfoTitolo();

        //--Costruisce il corpo della pagina
        this.creaBody();

        this.add(area);

        //--Ritorno alla view chiamante con un bottone nel bottom
        this.add(backButton());
    }// end of method


    /**
     * Regola le preferenze <br>
     */
    protected void fixPreferenze() {
        this.numVoci = lista.size;
        this.usaSuddivisioneParagrafi = lista.usaSuddivisioneParagrafi;
        this.titoloParagrafoVuoto = lista.titoloParagrafoVuoto;
        this.paragrafoVuotoInCoda = lista.paragrafoVuotoInCoda;
        this.usaRigheRaggruppate = lista.usaRigheRaggruppate;
        this.mappaSemplice = lista.mappaSemplice;
        this.mappaComplessa = lista.mappaComplessa;
    }// end of method


    /**
     * Ritorno alla view chiamante <br>
     */
    protected Component backButton() {
        Button backButton = new Button("Back", new Icon(VaadinIcon.ARROW_LEFT));
        backButton.getElement().setAttribute("theme", "primary");
        backButton.addClassName("view-toolbar__button");
        backButton.addClickListener(e -> UI.getCurrent().getPage().getHistory().back());
        return backButton;
    }// end of method


    /**
     * Costruisce il titolo della pagina <br>
     * Deve essere sovrascritto nella sottoclassse concreta <br>
     * Dopo DEVE invocare il metodo della superclasse <br>
     */
    protected void addInfoTitolo() {
        this.add(new Label((usaSuddivisioneParagrafi) ? "Con paragrafi" : "Senza paragrafi"));
        if (usaSuddivisioneParagrafi) {
            this.add(new Label("Titolo paragrafo vuoto: " + titoloParagrafoVuoto));
            this.add(new Label("Paragrafo vuoto posizionato " + (paragrafoVuotoInCoda ? "in coda" : "in testa")));
        }// end of if cycle
        this.add(new Label((usaRigheRaggruppate ? "Usa righe raggruppate" : "Usa righe singole")));
    }// end of method


    /**
     * Costruisce il corpo della pagina <br>
     */
    protected void creaBody() {
        String testo = "";

        if (usaSuddivisioneParagrafi) {
            if (usaRigheRaggruppate) {
                testo = listaService.paragrafoConRigheRaggruppate(mappaComplessa);
            } else {
                testo = listaService.paragrafoConRigheSemplici(mappaComplessa);
            }// end of if/else cycle
        } else {
            if (usaRigheRaggruppate) {
                testo = listaService.righeRaggruppate(mappaSemplice);
            } else {
                testo = listaService.righeSemplici(mappaSemplice);
            }// end of if/else cycle
        }// end of if/else cycle

        area.setValue(testo);
    }// end of method


}// end of class
