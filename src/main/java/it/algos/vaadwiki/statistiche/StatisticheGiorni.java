package it.algos.vaadwiki.statistiche;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadflow.service.AMathService;
import it.algos.vaadwiki.upload.UploadService;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;

import static it.algos.vaadflow.application.FlowCost.A_CAPO;
import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.DURATA_UPLOAD_STATISTICHE_GIORNI;
import static it.algos.vaadwiki.application.WikiCost.LAST_UPLOAD_STATISTICHE_GIORNI;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 19-nov-2019
 * Time: 16:32
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class StatisticheGiorni extends StatisticheAttNaz {

    private static String TITOLO_PAGINA_WIKI = "Progetto:Biografie/Giorni";


    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private GiornoService service;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private UploadService upload;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private AMathService math;


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(StatisticheGiorni.class) <br>
     */
    public StatisticheGiorni() {
    }// end of Spring constructor


    /**
     * Questa classe viene tipicamente costruita con appContext.getBean(StatisticheAttivita.class) <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired di questa classe <br>
     */
    @PostConstruct
    protected void postConstruct() {
        long inizio = System.currentTimeMillis();
        inizia();
        setLastUpload(inizio);
    }// end of method


    /**
     * Preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        super.fixPreferenze();
        super.titoloPagina = TITOLO_PAGINA_WIKI;
        super.codeLastUpload = LAST_UPLOAD_STATISTICHE_GIORNI;
        super.codeDurataUpload = DURATA_UPLOAD_STATISTICHE_GIORNI;
    }// fine del metodo


    /**
     * Recupera la lista
     */
    @Override
    protected void creaLista() {
        lista = service.findAllTitoliOrdered();
    }// end of method


    /**
     * Costruisce la mappa <br>
     */
    protected void creaMappa() {
        mappa = new LinkedHashMap<>();
        MappaStatistiche mappaSingola;
        int numGiornoNato;
        int numGiornoMorto;
        Giorno giorno;

        for (String titolo : this.lista) {
            giorno = service.findByKeyUnica(titolo);
            numGiornoNato = bioService.countByGiornoNascita(giorno);
            numGiornoMorto = bioService.countByGiornoMorte(giorno);

            mappaSingola = new MappaStatistiche(giorno, numGiornoNato, numGiornoMorto);
            mappa.put(titolo, mappaSingola);
        }// end of for cycle
    }// end of method


    /*
     * testo descrittivo prima tabella <br>
     */
    protected String testoPrimaTabella(int numBio) {
        String testo = VUOTA;

        testo += A_CAPO;
        testo += "==Giorni==";
        testo += A_CAPO;
        testo += "Statistiche dei nati e morti per ogni giorno dell'anno.";
        testo += "<ref>Previsto il [[29 febbraio]] per gli [[Anno bisestile|anni bisestili]]</ref>";
        testo += " Vengono prese in considerazione '''solo''' le voci biografiche che hanno valori '''validi e certi''' dei giorni di nascita e morte della persona.";

        return testo;
    }// fine del metodo


    @Override
    protected String colonnePrimaTabella() {
        String testo = "";
        String color = "! style=\"background-color:#CCC;\" |";

        testo += color;
        testo += "'''#'''";
        testo += A_CAPO;

        testo += color;
        testo += "'''giorno'''";
        testo += A_CAPO;

        testo += color;
        testo += "'''nati nel giorno'''";
        testo += "<ref>Il [[template:Bio|template Bio]] della voce biografica deve avere un valore valido al parametro '''giornoMeseNascita'''.</ref>'''";
        testo += A_CAPO;

        testo += color;
        testo += "'''morti nel giorno'''";
        testo += "<ref>Il [[template:Bio|template Bio]] della voce biografica deve avere un valore valido al parametro '''giornoMeseMorte'''.</ref>'''";
        testo += A_CAPO;

        testo += color;
        testo += "'''% nati giorno/anno'''";
        testo += A_CAPO;

        testo += color;
        testo += "'''% morti giorno/anno'''";
        testo += A_CAPO;

        return testo;
    }// fine del metodo


    protected String rigaPrimaTabella(String titolo, int nonUsato) {
        String testo = "";
        String tagDx = "style=\"text-align: right;\" |";
        String sep = "|";
        MappaStatistiche mappaSingola = null;
        String titoloPaginaNati = "";
        String titoloPaginaMorti = "";
        Giorno giorno;

        mappaSingola = mappa.get(titolo);
        if (mappaSingola == null) {
            return VUOTA;
        }// end of if cycle

        giorno = service.findByKeyUnica(titolo);
        titoloPaginaNati = upload.getTitoloGiornoNato(giorno);
        titoloPaginaMorti = upload.getTitoloGiornoMorto(giorno);
        int nati = mappaSingola.getNumGiornoNato();
        int morti = mappaSingola.getNumGiornoMorto();
        int totNati = getTotaleNati();
        int totMorti = getTotaleMorti();

        testo += "|-";
        testo += A_CAPO;
        testo += "|";

        testo += tagDx;
        testo += mappaSingola.getOrdine();

        testo += " || ";
        testo += LibWiki.setQuadre(titolo);

        testo += " || ";
        testo += tagDx;
        testo += LibWiki.setQuadre(titoloPaginaNati + sep + nati);

        testo += " || ";
        testo += tagDx;
        testo += LibWiki.setQuadre(titoloPaginaMorti + sep + morti);

        testo += " || ";
        testo += tagDx;
        testo += math.percentualeDueDecimali(nati, totNati);

        testo += " || ";
        testo += tagDx;
        testo += math.percentualeDueDecimali(morti, totMorti);

        testo += A_CAPO;

        return testo;
    }// fine del metodo


    /*
     * seconda tabella <br>
     */
    protected String tabellaNonUsate(int numBio) {
        return VUOTA;
    }// fine del metodo


    protected int getTotaleNati() {
        int numero = 0;

        for (String key : mappa.keySet()) {
            numero += mappa.get(key).getNumGiornoNato();
        }// end of for cycle

        return numero;
    }// fine del metodo


    protected int getTotaleMorti() {
        int numero = 0;

        for (String key : mappa.keySet()) {
            numero += mappa.get(key).getNumGiornoMorto();
        }// end of for cycle

        return numero;
    }// fine del metodo

}// end of class
