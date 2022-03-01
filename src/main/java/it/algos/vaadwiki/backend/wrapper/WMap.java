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
    private LinkedHashMap<String, LinkedHashMap<String, List<String>>> mappaLivelloDue;

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
        mappaBioLivelloUno = creaLivello(listaBio);

        if (livello == 1) {
            finalizzaPrimoLivello();
        }
    }

    private void creaSecondoLivello() {
        mappaBioLivelloUno = creaLivello(listaBio);

        if (livello == 1) {
            finalizzaPrimoLivello();
        }
    }

    private LinkedHashMap<String, List<Bio>> creaLivello(final List<Bio> listaBio) {
        LinkedHashMap<String, List<Bio>> mappaBio = new LinkedHashMap<>();
        Set<String> setChiavi = new LinkedHashSet<>();
        List<String> listaChiavi;
        String keyBio;
        String message;

        //--ricerca delle chiavi
        for (Bio bio : listaBio) {
            keyBio = switch (type) {
                case attivita -> mappaNazionalita.get(bio.nazionalita);
                default -> VUOTA;
            };
            if (text.isValid(bio.nazionalita)) {
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
            keyBio = switch (type) {
                case attivita -> mappaNazionalita.get(bio.nazionalita);
                default -> VUOTA;
            };

            if (mappaBio.containsKey(keyBio)) {
                mappaBio.get(keyBio).add(bio);
            }
            else {
                message = String.format("La mappa non prevede la chiave %s", keyBio);
                logger.warn(message, this.getClass(), "creaLivello");
                mappaBio.get(ALTRE).add(bio);
            }
        }
        return mappaBio;
    }

    protected void creaSecondoLivelloLetteraInizialeCognome() {
        List<Bio> lista;
        WMap mappa = null;
        Set<String> setPrimoCarattere = null;
        List<String> listaPrimoCarattere;
        String primoCarattere;
        String message;
        mappaLivelloUno = new LinkedHashMap<>();

        //--ricerca delle chiavi
        for (Object key : mappaBioLivelloUno.keySet()) {
            lista = mappaBioLivelloUno.get(key);
            setPrimoCarattere = new LinkedHashSet<>();
            for (Bio bio : lista) {
                primoCarattere = bio.cognome.substring(0, 1);
                setPrimoCarattere.add(primoCarattere);
            }

            //--ordinamento delle chiavi
            listaPrimoCarattere = new ArrayList<>(setPrimoCarattere);
            Collections.sort(listaPrimoCarattere);

            //--creazione della mappa vuota prima dell'inserimento dei singoli valori
            for (String primoCar : listaPrimoCarattere) {
                mappaLivelloUno.put(primoCar, new ArrayList());
            }
            mappaLivelloUno.put(ALTRE, new ArrayList());

            //--riempimento della mappa con la lista dei singoli valori per ogni chiave
            for (Bio bio : lista) {
                primoCarattere = bio.cognome.substring(0, 1);
                if (mappaLivelloUno.containsKey(primoCarattere)) {
                    //                    mappaLivelloUno.get(primoCarattere).add(bio);
                }
                else {
                    message = String.format("La mappa non prevede il primo carattere %s", primoCarattere);
                    logger.error(message, this.getClass(), "creaSecondoLivelloLetteraInizialeCognome");
                }
            }
            //            if (mappa.get(ALTRE) == null) {
            //                mappa.remove(ALTRE);
            //            }
            //
            //            mappaBio.put(key, mappa);
        }
    }

    private void finalizzaPrimoLivello() {
        List<Bio> lista;
        List<String> didascalie;
        String didascalia;

        if (mappaBioLivelloUno != null && mappaBioLivelloUno.size() > 0) {
            mappaLivelloUno = new LinkedHashMap<>();

            for (String paragrafo : mappaBioLivelloUno.keySet()) {
                lista = mappaBioLivelloUno.get(paragrafo);

                didascalie = new ArrayList<>();
                for (Bio bio : lista) {
                    didascalia = didascaliaService.getLista(bio);
                    didascalie.add(didascalia);
                }
                paragrafo = text.primaMaiuscola(paragrafo);
                mappaLivelloUno.put(paragrafo, didascalie);
                this.listaDidascalie.addAll(didascalie);
            }
        }
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

    public List<String> getListaDidascalie() {
        return listaDidascalie;
    }

}
