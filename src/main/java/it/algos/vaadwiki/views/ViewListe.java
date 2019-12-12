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
import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadflow.service.AArrayService;
import it.algos.vaadflow.service.ADateService;
import it.algos.vaadflow.service.ATextService;
import it.algos.vaadwiki.liste.Lista;
import it.algos.vaadwiki.liste.ListaService;
import it.algos.vaadwiki.liste.ListaSottopagina;
import it.algos.vaadwiki.modules.attivita.Attivita;
import it.algos.vaadwiki.modules.attivita.AttivitaService;
import it.algos.vaadwiki.modules.cognome.Cognome;
import it.algos.vaadwiki.modules.cognome.CognomeService;
import it.algos.vaadwiki.modules.nazionalita.Nazionalita;
import it.algos.vaadwiki.modules.nazionalita.NazionalitaService;
import it.algos.vaadwiki.modules.nome.Nome;
import it.algos.vaadwiki.modules.nome.NomeService;
import it.algos.vaadwiki.upload.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

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

    //--property
    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaComplessa;

    //--property
    public boolean usaParagrafoSize;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected AttivitaService attivitaService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected NazionalitaService nazionalitaService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected CognomeService cognomeService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected NomeService nomeService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected GiornoService giornoService;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile solo dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected AnnoService annoService;

    //--property
    protected Anno anno;

    //--property
    protected Giorno giorno;

    //--property
    protected Nome nome;

    //--property
    protected Cognome cognome;

    //--property
    protected Attivita attivita;

    //--property
    protected Nazionalita nazionalita;

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
    protected AArrayService array;

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

    protected LinkedHashMap<String, List<String>> mappaSemplice;

    //--property
    protected boolean usaSuddivisioneParagrafi;

    //--property
    protected String titoloParagrafoVuoto;

    //--property
    protected boolean paragrafoVuotoInCoda;

    //--property
    protected boolean usaLinkParagrafo;

    //--property
    protected int taglioSottoPagina;

    //--property
    protected boolean usaBodySottopagine;

    protected TextArea area = new TextArea();

    //--property
    protected int numVoci;

    //--property
    protected int numParagrafi;

    protected Lista lista;

    //--property elaborata
    private List<String> titoliParagrafiDisordinati;

    //--property elaborata
    private List<String> titoliParagrafiOrdinati;

    //--property elaborata
    private List<String> titoliParagrafiPagine;

    //--property elaborata
    private List<String> titoliParagrafiVisibili;

    //--property elaborata
    private List<String> titoliParagrafiLinkati;

    //--property elaborata
    private List<String> titoliParagrafiConSize;

    //--property elaborata
    private List<String> titoliParagrafiDefinitivi;

    //--property elaborata
    private List<Integer> numVociParagrafi;


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
        this.numVoci = lista.getNumVoci();
        this.numParagrafi = lista.getNumParagrafi();
        this.titoliParagrafiDisordinati = lista.getTitoliParagrafiDisordinati();
        this.titoliParagrafiOrdinati = lista.getTitoliParagrafiOrdinati();
        this.titoliParagrafiPagine = lista.getTitoliParagrafiPagine();
        this.titoliParagrafiVisibili = lista.getTitoliParagrafiVisibili();
        this.titoliParagrafiLinkati = lista.getTitoliParagrafiLinkati();
        this.titoliParagrafiConSize = lista.getTitoliParagrafiConSize();
        this.titoliParagrafiDefinitivi = lista.getTitoliParagrafiDefinitivi();
        this.usaSuddivisioneParagrafi = lista.usaSuddivisioneParagrafi;
        this.titoloParagrafoVuoto = lista.titoloParagrafoVuoto;
        this.paragrafoVuotoInCoda = lista.paragrafoVuotoInCoda;
        this.usaLinkParagrafo = lista.usaLinkParagrafo;
        this.usaParagrafoSize = lista.usaParagrafoSize;
        this.usaRigheRaggruppate = lista.usaRigheRaggruppate;
        this.usaBodySottopagine = lista.usaSottopagine;
        this.taglioSottoPagina = lista.taglioSottoPagina;
        this.numVociParagrafi = lista.getDimParagrafi();
        this.mappaComplessa = lista.getMappa();
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
        this.add(new Label((usaSuddivisioneParagrafi) ? "Con paragrafi: ce ne sono " + numParagrafi : "Senza paragrafi"));
        if (usaSuddivisioneParagrafi) {
            this.add(new Label("Paragrafi disordinati: " + array.toStringaSpazio(fixVuoto(titoliParagrafiDisordinati))));
            this.add(new Label("Paragrafi ordinati: " + array.toStringaSpazio(fixVuoto(titoliParagrafiOrdinati))));
            this.add(new Label("Paragrafi pagine: " + array.toStringaSpazio(titoliParagrafiPagine)));
            this.add(new Label("Paragrafi visibili: " + array.toStringaSpazio(titoliParagrafiVisibili)));
            this.add(new Label("Paragrafi linkati: " + array.toStringaSpazio(fixVuoto(titoliParagrafiLinkati))));
            this.add(new Label("Paragrafi con dimensione: " + array.toStringaSpazio(titoliParagrafiConSize)));
            this.add(new Label("Paragrafi definitivi: " + array.toStringaSpazio(titoliParagrafiDefinitivi)));
            this.add(new Label("Dimensione dei paragrafi: " + array.toStringa(numVociParagrafi)));
            this.add(new Label("Titolo paragrafo vuoto: " + titoloParagrafoVuoto));
            this.add(new Label("Paragrafo vuoto posizionato " + (paragrafoVuotoInCoda ? "in coda" : "in testa")));
            this.add(new Label(usaLinkParagrafo ? "Usa un wikilink ad una pagina di wikipedia nel titolo del paragrafo" : "Non usa un wikilink nel titolo del paragrafo"));
            this.add(new Label(usaParagrafoSize ? "Usa le dimensioni nel titolo del paragrafo" : "Non usa le dimensioni nel titolo del paragrafo"));
        }// end of if cycle
        this.add(new Label((usaRigheRaggruppate ? "Usa righe raggruppate" : "Usa righe singole")));
        this.add(new Label((usaBodySottopagine ? "Usa sottopagine con taglio a " + taglioSottoPagina : "Non usa sottopagine")));
    }// end of method


    /**
     * Costruisce il corpo della pagina <br>
     */
    protected void creaBody() {
        String testoLista = "";
        ListaSottopagina listaSottopagina;

        if (usaSuddivisioneParagrafi) {
            testoLista = lista.getMappaLista().getTesto();
//            testoLista = creaParagrafi();
        } else {
            testoLista = lista.getMappaLista().getTesto();
        }// end of if/else cycle


//        if (usaBodySottopagine) {
//            listaSottopagina = lista.getSottopagina();
//            testoLista = listaSottopagina.getTesto();
//        } else {
//            testoLista = lista.getMappaLista().getTesto();
//        }// end of if/else cycle

        area.setValue(testoLista);
    }// end of method


//    protected String creaParagrafi() {
//        StringBuilder testoLista = new StringBuilder();
//        LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<String>>>> mappaUno = lista.getMappaNew();
//        LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaDue;
//        LinkedHashMap<String, List<String>> mappaTre;
//        List<String> listaTxt = null;
//
//        for (String chiaveParagrafo : mappaUno.keySet()) {
//            listaTxt = new ArrayList<>();
//            testoLista.append(A_CAPO);
//            testoLista.append(LibBio.setParagrafo(chiaveParagrafo));
//            mappaDue = mappaUno.get(chiaveParagrafo);
//            if (mappaDue != null && mappaDue.size() > 0) {
//                for (String chiaveDue : mappaDue.keySet()) {
//                    mappaTre = mappaDue.get(chiaveDue);
//                    if (mappaTre != null && mappaTre.size() > 0) {
//                        for (String chiaveTre : mappaTre.keySet()) {
//                            listaTxt.addAll(mappaTre.get(chiaveTre));
//                        }// end of for cycle
//                    }// end of if cycle
//                }// end of for cycle
//            }// end of if cycle
//            for (String riga : listaTxt) {
//                testoLista.append(riga);
//                testoLista.append(A_CAPO);
//            }// end of for cycle
//        }// end of for cycle
//
//        return testoLista.toString().trim();
//    }// end of method


    /**
     * Sostituisce una stringa vuota con un testo 'visibile' <br>
     */
    private List<String> fixVuoto(List<String> listaIn) {
        List<String> listaOut = new ArrayList<>();
        String tag = "Vuoto";

        if (listaIn != null) {
            for (String stringa : listaIn) {
                if (stringa.equals(VUOTA)) {
                    listaOut.add(tag);
                } else {
                    listaOut.add(stringa);
                }// end of if/else cycle
            }// end of for cycle
        }// end of if cycle

        return listaOut;
    }// end of method

}// end of class
