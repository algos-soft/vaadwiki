package it.algos.vaadwiki.statistiche;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadwiki.enumeration.EAPreferenzaWiki;
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
import static it.algos.vaadwiki.application.WikiCost.DURATA_UPLOAD_STATISTICHE_ANNI;
import static it.algos.vaadwiki.application.WikiCost.LAST_UPLOAD_STATISTICHE_ANNI;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mer, 20-nov-2019
 * Time: 14:05
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class StatisticheAnni extends StatisticheAttNaz {

    private static String TITOLO_PAGINA_WIKI = "Progetto:Biografie/Anni";

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private AnnoService service;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private UploadService upload;


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(StatisticheAnni.class) <br>
     */
    public StatisticheAnni() {
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
        super.codeLastUpload = LAST_UPLOAD_STATISTICHE_ANNI;
        super.codeDurataUpload = DURATA_UPLOAD_STATISTICHE_ANNI;
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
        int numAnnoNato;
        int numAnnoMorto;
        Anno anno;
        int ordine = 1;
        int min = pref.getInt(EAPreferenzaWiki.minNatiMortiAnno);

        for (String titolo : this.lista) {
            anno = service.findByKeyUnica(titolo);
            numAnnoNato = bioService.countByAnnoNascita(anno);
            numAnnoMorto = bioService.countByAnnoMorte(anno);

            if (numAnnoNato > min || numAnnoMorto > min) {
                mappaSingola = new MappaStatistiche(anno, ordine, numAnnoNato, numAnnoMorto);
                mappa.put(titolo, mappaSingola);
                ordine++;
            }// end of if cycle
        }// end of for cycle
    }// end of method


    /*
     * testo descrittivo prima tabella <br>
     */
    protected String testoPrimaTabella(int numBio) {
        String testo = VUOTA;
        int min = pref.getInt(EAPreferenzaWiki.minNatiMortiAnno);

        testo += A_CAPO;
        testo += "==Anni==";
        testo += A_CAPO;
        testo += "Statistiche dei nati e morti per ogni anno .";
        testo += "<ref>Potenzialmente dal [[1000 a.C.]] al [[{{CURRENTYEAR}}]]</ref>";
        testo += " Vengono prese in considerazione '''solo''' le voci biografiche che hanno valori '''validi e certi''' degli anni di nascita e morte della persona. Sono riportati gli anni che hanno un numero di nati o morti maggiore di '''" + min + "'''";

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
        testo += "'''anno'''";
        testo += A_CAPO;

        testo += color;
        testo += "'''nati nell'anno'''";
        testo += "<ref>Il [[template:Bio|template Bio]] della voce biografica deve avere un valore valido al parametro '''annoNascita'''.</ref>'''";
        testo += A_CAPO;

        testo += color;
        testo += "'''morti nell'anno'''";
        testo += "<ref>Il [[template:Bio|template Bio]] della voce biografica deve avere un valore valido al parametro '''annoMorte'''.</ref>'''";
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
        Anno anno;
        int numNati;
        int numMorti;
        String nati;
        String morti;

        mappaSingola = mappa.get(titolo);
        if (mappaSingola == null) {
            return VUOTA;
        }// end of if cycle

        anno = service.findByKeyUnica(titolo);
        titoloPaginaNati = upload.getTitoloAnnoNato(anno);
        titoloPaginaMorti = upload.getTitoloAnnoMorto(anno);

        numNati = mappaSingola.getNumAnnoNato();
        numMorti = mappaSingola.getNumAnnoMorto();

        nati = numNati == 0 ? "0" : LibWiki.setQuadre(titoloPaginaNati + sep + numNati);
        morti = numMorti == 0 ? "0" : LibWiki.setQuadre(titoloPaginaMorti + sep + numMorti);

        testo += "|-";
        testo += A_CAPO;
        testo += "|";

        testo += tagDx;
        testo += mappaSingola.getOrdine();

        testo += " || ";
        testo += tagDx;
        testo += LibWiki.setQuadre(titolo);

        testo += " || ";
        testo += tagDx;
        testo += nati;

        testo += " || ";
        testo += tagDx;
        testo += morti;

        testo += A_CAPO;

        return testo;
    }// fine del metodo


    /*
     * seconda tabella <br>
     */
    protected String tabellaNonUsate(int numBio) {
        return VUOTA;
    }// fine del metodo

}// end of class
