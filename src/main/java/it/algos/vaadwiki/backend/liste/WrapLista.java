package it.algos.vaadwiki.backend.liste;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadwiki.backend.enumeration.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import it.algos.vaadwiki.backend.packages.nazionalita.*;
import it.algos.vaadwiki.backend.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.*;

import javax.annotation.*;
import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 22-feb-2022
 * Time: 09:46
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WrapLista {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected WikiUtility wikiUtility;

    /**
     * The App context.
     */
    @Autowired
    private GenericApplicationContext appContext;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    private TextService text;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    private NazionalitaService nazionalitaService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    private DidascaliaService didascaliaService;

    /**
     * Mappa delle didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave che corrisponde al titolo della pagina/sottoPagina/paragrafo <br>
     * Ogni valore della mappa è costituito da una List <br>
     * Nel caso di AETypeMappa.paragrafo la lista è costituita da didascalie <br>
     */
    private Map<String, List> mappa;

    /**
     * Lista delle biografie che hanno una valore valido per la pagina specifica <br>
     * La lista è ordinata per cognome <br>
     */
    private List<Bio> listaBio;

    /**
     * Lista delle didascalie che hanno una valore valido per la pagina specifica <br>
     * La lista viene creata nel @PostConstruct dell'istanza <br>
     * La lista è ordinata per cognome <br>
     */
    private List<String> listaDidascalie;

    private String wikiTitle;

    private String paginaMadre;

    private AETypeLista typeLista;

    private AETypeMappa typeMappa;

    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     */
    public WrapLista(
            final AETypeLista typeLista,
            final String title,
            final List listaBio) {
        this(typeLista, title, listaBio, AETypeMappa.paginaPrincipale, VUOTA);
    }// end of constructor

    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     *
     * @param typeMappa
     */
    public WrapLista(
            final AETypeLista typeLista,
            final String title,
            final List listaBio,
            final AETypeMappa typeMappa,
            final String paginaMadre) {
        this.typeLista = typeLista;
        this.wikiTitle = typeLista.getPrefix() + title;
        this.listaBio = listaBio;
        this.typeMappa = typeMappa;
        this.paginaMadre = paginaMadre;
    }// end of constructor


    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     */
    @PostConstruct
    public void inizia() {
        this.fixMappaDidascalie();
    }

    /**
     * Costruisce una mappa di didascalie che hanno una valore valido per la pagina specifica <br>
     */
    protected void fixMappaDidascalie() {
        switch (typeLista) {
            case attivita -> fixMappaNazionalita();
        }
    }


    private void fixMappaNazionalita() {
        Map<String, List> mappa = new LinkedHashMap<>();
        String altre = "Titolo provvisorio";
        List<String> lista;
        Set<String> setNazionalita;
        List<String> listaNazionalita;
        String titoloParagrafoNazionalita;
        Nazionalita nazionalita;
        String didascalia;

        if (listaBio == null) {
            return;
        }

        setNazionalita = new LinkedHashSet<>();
        listaDidascalie = new ArrayList<>();
        for (Bio bio : listaBio) {
            if (text.isEmpty(bio.nazionalita)) {
                bio.nazionalita = altre;
            }
            titoloParagrafoNazionalita = bio.nazionalita;
            setNazionalita.add(titoloParagrafoNazionalita);
        }
        listaNazionalita = new ArrayList<>(setNazionalita);
        Collections.sort(listaNazionalita);

        for (String nazionalitaSingolare : listaNazionalita) {
            for (Bio bio : listaBio) {
                if (bio.nazionalita.equals(nazionalitaSingolare)) {
                    try {
                        nazionalita = nazionalitaService.findById(nazionalitaSingolare);
                        titoloParagrafoNazionalita = nazionalita != null ? nazionalita.plurale : "errore";
                        titoloParagrafoNazionalita = text.primaMaiuscola(titoloParagrafoNazionalita);
                        if (mappa.containsKey(titoloParagrafoNazionalita)) {
                            lista = mappa.get(titoloParagrafoNazionalita);
                        }
                        else {
                            lista = new ArrayList<>();
                        }
                        didascalia = didascaliaService.getLista(bio);
                        listaDidascalie.add(didascalia);
                        lista.add(didascalia);
                        mappa.put(titoloParagrafoNazionalita, lista);
                    } catch (AlgosException unErrore) {
                        int a = 87;
                    }
                }
            }
        }
        this.mappa = mappa;
    }

    public List<String> getListaDidascalie() {
        return listaDidascalie;
    }

    public List<Bio> getListaBio() {
        return listaBio;
    }

    public Map<String, List> getMappa() {
        return mappa;
    }


    /**
     * Costruisce un testo completo con titoli dei paragrafi <br>
     */
    public String getTestoConParagrafi() {
        StringBuilder buffer = new StringBuilder();
        List<String> lista;

        if (mappa != null && mappa.size() > 0) {
            for (String key : mappa.keySet()) {
                lista = mappa.get(key);

                if (lista != null && lista.size() > 0) {
                    buffer.append(getTitoloParagrafo(key, lista.size()));

                    for (Object stringa : lista) {
                        buffer.append(ASTERISCO);
                        buffer.append(SPAZIO);
                        buffer.append(stringa);
                        buffer.append(A_CAPO);
                    }
                    buffer.append(A_CAPO);
                }
            }
        }

        return buffer.toString();
    }


    public String getTitoloParagrafo(final String keyParagrafo, int numero) {
        return switch (typeLista) {
            case attivita -> getTitoloParagrafoAttivita(keyParagrafo, numero);
            default -> VUOTA;
        };
    }


    public String getTitoloParagrafoAttivita(final String keyParagrafo, int numero) {
        String keyMaiuscola = text.primaMaiuscola(keyParagrafo);
        String tag = "Progetto:Biografie/Nazionalità/";

        tag += keyMaiuscola;
        tag += PIPE;
        tag += keyMaiuscola;
        tag = text.setDoppieQuadre(tag);

        return wikiUtility.setParagrafo(tag, numero);
    }

    public enum AETypeMappa {
        paginaPrincipale, sottoPagina, paragrafo
    }

}
