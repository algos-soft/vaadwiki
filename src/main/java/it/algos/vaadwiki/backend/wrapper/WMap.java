package it.algos.vaadwiki.backend.wrapper;

import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.service.*;
import static it.algos.vaadwiki.backend.application.WikiCost.*;
import it.algos.vaadwiki.backend.enumeration.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import it.algos.vaadwiki.backend.packages.nazionalita.*;
import it.algos.vaadwiki.backend.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

import javax.annotation.*;
import java.util.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 04-gen-2022
 * Time: 11:33
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WMap {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public TextService text;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public NazionalitaService nazionalitaService;

    public String titolo;

    public String chiave;

    public List<Bio> listaBio;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected DidascaliaService didascaliaService;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    protected ALogService logger;

    private AETypeLista type;

    private int livello;

    private List<String> listaDidascalie;

    //--property elaborata
    private LinkedHashMap<String, List<Bio>> mappaBioLivelloUno;

    //--property elaborata
    private LinkedHashMap<String, LinkedHashMap<String, List<Bio>>> mappaBioLivelloDue;

    //--property elaborata
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<Bio>>>> mappaBioLivelloTre;

    //--property elaborata
    private LinkedHashMap<String, List<String>> mappaLivelloUno;

    //--property elaborata
    private Map<String, Map<String, List<String>>> mappaLivelloDue;

    //--property elaborata
    private LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, List<String>>>> mappaLivelloTre;

    private LinkedHashMap<String, String> mappaNazionalita;

    public WMap(final AETypeLista type, final List<Bio> listaBio) {
        this.type = type;
        this.livello = type.getLivello();
        this.listaBio = listaBio;
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
        this.mappaNazionalita = nazionalitaService.getMappa();
        this.listaDidascalie = new ArrayList<>();
    }

    public void creaLivelli() {
        LinkedHashMap<String, List<Bio>> mappa;
        mappaBioLivelloUno = creaLivello(listaBio, 1);
        finalizzaPrimoLivello();

        switch (livello) {
            case 2 -> {
                mappaBioLivelloDue = new LinkedHashMap<>();

                for (String key : mappaBioLivelloUno.keySet()) {
                    mappa = creaLivello(mappaBioLivelloUno.get(key), 2);
                    mappaBioLivelloDue.put(key, mappa);
                }
                finalizzaSecondoLivello();
            }
            default -> {}
        }

    }


    private LinkedHashMap<String, List<Bio>> creaLivello(final List<Bio> listaBio, final int livello) {
        LinkedHashMap<String, List<Bio>> mappaBio = new LinkedHashMap<>();
        Set<String> setChiavi = new LinkedHashSet<>();
        List<String> listaChiavi;
        String keyBio;
        String message;

        //--ricerca delle chiavi
        for (Bio bio : listaBio) {
            keyBio = getChiave(bio, livello);

            if (text.isValid(keyBio)) {
                setChiavi.add(keyBio);
            }
        }

        //--ordinamento delle chiavi
        listaChiavi = new ArrayList<>(setChiavi);
        Collections.sort(listaChiavi);

        //--creazione della mappa vuota prima dell'inserimento dei singoli valori
        for (String keyMappa : listaChiavi) {
            mappaBio.put(keyMappa, new ArrayList());
        }
        mappaBio.put(ALTRE, new ArrayList());

        //--riempimento della mappa con la lista dei singoli valori per ogni chiave
        for (Bio bio : listaBio) {
            keyBio = getChiave(bio, livello);

            if (mappaBio.containsKey(keyBio)) {
                mappaBio.get(keyBio).add(bio);
            }
            else {
                message = String.format("La mappa non prevede la chiave %s", keyBio);
                logger.warn(message, this.getClass(), "creaLivello");
                mappaBio.get(ALTRE).add(bio);
            }
        }

        if (mappaBio.get(ALTRE).size() == 0) {
            mappaBio.remove(ALTRE);
        }

        return mappaBio;
    }

    protected String getChiave(Bio bio, final int livello) {
        String keyBio;
        String keyBioInt;

        keyBio = switch (livello) {
            case 1 -> {
                keyBioInt = switch (type) {
                    case attivita -> text.primaMaiuscola(mappaNazionalita.get(bio.nazionalita));
                    default -> VUOTA;
                };
                yield keyBioInt;
            }
            case 2 -> {
                keyBioInt = switch (type) {
                    case attivita -> bio.cognome != null ? bio.cognome.substring(0, 1) : bio.wikiTitle.substring(0, 1);
                    default -> VUOTA;
                };
                yield keyBioInt;
            }
            default -> VUOTA;
        };

        return keyBio;
    }


    private void finalizzaPrimoLivello() {
        mappaLivelloUno = new LinkedHashMap<>();
        List<Bio> listaBio;
        List<String> listaDidascalie;

        if (mappaBioLivelloUno != null && mappaBioLivelloUno.size() > 0) {
            for (String key : mappaBioLivelloUno.keySet()) {
                listaBio = mappaBioLivelloUno.get(key);
                listaDidascalie = fixLista(listaBio);
                mappaLivelloUno.put(key, listaDidascalie);
                this.listaDidascalie.addAll(listaDidascalie);
            }
        }
    }


    private void finalizzaSecondoLivello() {
        mappaLivelloDue = new LinkedHashMap<>();
        this.listaDidascalie = new ArrayList<>();
        LinkedHashMap<String, List<Bio>> mappaBio;
        LinkedHashMap<String, List<String>> mappaDidascalie;
        List<Bio> listaBio;
        List<String> listaDidascalie;

        if (mappaBioLivelloDue != null && mappaBioLivelloDue.size() > 0) {
            for (String paragrafo : mappaBioLivelloDue.keySet()) {
                mappaBio = mappaBioLivelloDue.get(paragrafo);
                if (mappaBio != null && mappaBio.size() > 0) {
                    mappaDidascalie = new LinkedHashMap<>();

                    for (String key : mappaBio.keySet()) {
                        listaBio = mappaBio.get(key);
                        listaDidascalie = fixLista(listaBio);
                        mappaDidascalie.put(key, listaDidascalie);
                        this.listaDidascalie.addAll(listaDidascalie);
                    }
                    mappaLivelloDue.put(paragrafo, mappaDidascalie);
                }
            }
        }
    }

    private List<String> fixLista(final List<Bio> listaBio) {
        List<String> listaDidascalie = new ArrayList<>();
        String didascalia;

        for (Bio bio : listaBio) {
            didascalia = didascaliaService.getLista(bio);
            listaDidascalie.add(didascalia);
        }

        return listaDidascalie;
    }


    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getChiave() {
        return chiave;
    }

    public void setChiave(String chiave) {
        this.chiave = chiave;
    }

    public LinkedHashMap<String, List<String>> getMappaLivelloUno() {
        return mappaLivelloUno;
    }

    public Map<String, Map<String, List<String>>> getMappaLivelloDue() {
        return mappaLivelloDue;
    }

    public List<String> getListaDidascalie() {
        return listaDidascalie;
    }

}
