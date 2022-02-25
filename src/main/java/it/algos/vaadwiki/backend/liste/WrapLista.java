package it.algos.vaadwiki.backend.liste;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
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

    public static final String ALTRE = "Altre...";

    public static final String VEDI = "{{Vedi anche|";

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected WikiUtility wikiUtility;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected ALogService logger;

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

    private String titoloGrezzo;

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
            final String titoloGrezzo,
            final List listaBio) {
        this(typeLista, titoloGrezzo, listaBio, AETypeMappa.paginaPrincipale, VUOTA);
    }// end of constructor

    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     *
     * @param typeMappa
     */
    public WrapLista(
            final AETypeLista typeLista,
            final String titoloGrezzo,
            final List listaBio,
            final AETypeMappa typeMappa,
            final String paginaMadre) {
        this.typeLista = typeLista;
        this.titoloGrezzo = titoloGrezzo;
        this.listaBio = listaBio;
        this.typeMappa = typeMappa;
        this.paginaMadre = paginaMadre;
    }// end of constructor


    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     * Se hanno la stessa firma, chiama prima @PostConstruct della sottoclasse <br>
     * Se hanno firme diverse, chiama prima @PostConstruct della superclasse <br>
     */
    @PostConstruct
    public void inizia() {
        this.regolazioniIniziali();
        this.fixMappaDidascalie();
    }

    /**
     * Regolazioni iniziali <br>
     */
    private void regolazioniIniziali() {
        this.wikiTitle = typeLista.getPrefix() + text.primaMaiuscola(titoloGrezzo);
    }

    /**
     * Costruisce una mappa di didascalie che hanno una valore valido per la pagina specifica <br>
     */
    private void fixMappaDidascalie() {
        switch (typeLista) {
            case attivita -> fixMappaNazionalita();
        }
    }


    private void fixMappaNazionalita() {
        mappa = new LinkedHashMap<>();
        LinkedHashMap<String, List> mappaBio = new LinkedHashMap<>();
        String message = VUOTA;
        List<Bio> lista;
        Set<String> setNazionalita;
        List<String> listaNazionalita;
        List<String> didascalie;
        String nazionalitaPlurale;
        String didascalia;
        LinkedHashMap<String, String> mappaNazionalita = nazionalitaService.getMappa();

        if (listaBio == null) {
            return;
        }

        setNazionalita = new LinkedHashSet<>();
        listaDidascalie = new ArrayList<>();
        for (Bio bio : listaBio) {
            if (text.isValid(bio.nazionalita)) {
                nazionalitaPlurale = mappaNazionalita.get(bio.nazionalita);
                setNazionalita.add(nazionalitaPlurale);
            }
        }
        listaNazionalita = new ArrayList<>(setNazionalita);
        Collections.sort(listaNazionalita);

        for (String nazPlurale : listaNazionalita) {
            mappaBio.put(nazPlurale, new ArrayList());
        }
        mappaBio.put(ALTRE, new ArrayList());

        for (Bio bio : listaBio) {
            if (text.isValid(bio.nazionalita)) {
                nazionalitaPlurale = mappaNazionalita.get(bio.nazionalita);
                if (mappaBio.containsKey(nazionalitaPlurale)) {
                    mappaBio.get(nazionalitaPlurale).add(bio);
                }
                else {
                    message = String.format("La mappa non prevede la nazionalità %s", nazionalitaPlurale);
                    logger.warn(message, this.getClass(), "fixMappaNazionalita");
                }
            }
            else {

                mappaBio.get(ALTRE).add(bio);
            }
        }

        if (mappaBio != null && mappaBio.size() > 0) {
            for (String paragrafo : mappaBio.keySet()) {
                lista = mappaBio.get(paragrafo);

                didascalie = new ArrayList<>();
                for (Bio bio : lista) {
                    didascalia = didascaliaService.getLista(bio);
                    didascalie.add(didascalia);
                    listaDidascalie.add(didascalia);
                }
                paragrafo = text.primaMaiuscola(paragrafo);
                mappa.put(paragrafo, didascalie);

                //                    didascalie = new ArrayList<>();
                //                    for (Bio bio : lista) {
                //                        didascalia = didascaliaService.getLista(bio);
                //                        didascalie.add(didascalia);
                //                        listaDidascalie.add(didascalia);
                //                    }
                //                    paragrafo = text.primaMaiuscola(paragrafo);
                //
                //                    mappa.put(paragrafo, Arrays.asList(String.format("%s%s%s%s%s",VEDI,wikiTitle,SLASH,paragrafo,DOPPIE_GRAFFE_END)));
                //                }
                //                else {
                //                }
            }
        }
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

                    for (String stringa : lista) {
                        if (stringa.startsWith(DOPPIE_GRAFFE_INI)) {
                            buffer.append(stringa);
                        }
                        else {
                            buffer.append(ASTERISCO);
                            buffer.append(SPAZIO);
                            buffer.append(stringa);
                        }
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
        String titolo;

        tag += keyMaiuscola;
        tag += PIPE;
        tag += keyMaiuscola;
        tag = text.setDoppieQuadre(tag);

        titolo = keyMaiuscola.equals(ALTRE) ? ALTRE : tag;
        return wikiUtility.setParagrafo(titolo, numero);
    }

    public enum AETypeMappa {
        paginaPrincipale, sottoPagina, paragrafo
    }

}
