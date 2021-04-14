package it.algos.vaadflow14.backend.service;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.wrapper.*;
import org.json.simple.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.net.*;
import java.util.*;


/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: gio, 07-mag-2020
 * Time: 07:49
 * <p>
 * Importazione di pagine  da Wikipedia <br>
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AAnnotationService.class); <br>
 * 3) @Autowired private AWikiService wiki; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AWikiService extends AAbstractService {

    public static final String ENCODE = "UTF-8";

    public static final String PAGINA_ISO_1 = "ISO 3166-1";

    public static final String PAGINA_ISO_2 = "ISO 3166-2:IT";

    public static final String PAGINA_PROVINCE = "Regioni_d'Italia";

    public static final String PAGINA_ISO_1_NUMERICO = "ISO 3166-1 numerico";

    public static final String PAGES = "pages";

    public static final String QUERY = "query";

    public static final String REVISIONS = "revisions";

    public static final String SLOTS = "slots";

    public static final String MAIN = "main";

    public static final String CONTENT = "content";

    private static final String WIKI_URL = "https://it.wikipedia.org/w/api.php?&format=json&formatversion=2&action=query&rvslots=main&prop=info|revisions&rvprop=content|ids|flags|timestamp|user|userid|comment|size&titles=";

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AWebService web;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ATextService text;

    //    /**
    //     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
    //     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
    //     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
    //     */
    //    @Autowired
    //    public RegioneLogic regioneLogic;


    /**
     * Estrae una wikitable da una pagina wiki <br>
     * Restituisce una lista dei valori per ogni riga, esclusa la prima coi titoli <br>
     *
     * @return lista di valori per ogni riga significativa della wikitable
     */
    public List<List<String>> getStati() {
        List<List<String>> listaTable = new ArrayList<>();
        List<List<String>> listaGrezza = null;
        Map<String, String> mappa = getTableStatiNumerico();
        String sep = DOPPIO_PIPE_REGEX;
        String[] parti = null;
        List<String> riga = null;
        String numerico = VUOTA;
        String nome = VUOTA;
        String alfatre = VUOTA;
        String alfadue = VUOTA;
        String locale = VUOTA;

        try {
            listaGrezza = getTable(PAGINA_ISO_1);
        } catch (Exception unErrore) {
        }

        if (listaGrezza != null && listaGrezza.size() > 1) {
            for (List<String> rigaGrezza : listaGrezza) {
                riga = new ArrayList<>();
                if (rigaGrezza.size() == 3 || rigaGrezza.size() == 4) {
                    parti = rigaGrezza.get(2).split(sep);
                    if (parti.length == 4) {
                        nome = fixNomeStato(mappa.get(parti[0].trim()));
                        numerico = parti[0].trim();
                        alfatre = parti[1].trim();
                        alfadue = parti[2].trim();
                        locale = text.setNoQuadre(parti[3].trim());
                        riga.add(nome);
                        riga.add(numerico);
                        riga.add(alfatre);
                        riga.add(alfadue);
                        riga.add(locale);
                        listaTable.add(riga);
                    }
                }
            }
        }

        return listaTable;
    }

    /**
     * Restituisce una lista degli stati <br>
     *
     * @return lista dei nome degli stati
     */
    public List<String> getNomeStati() {
        List<String> lista = new ArrayList<>();
        List<List<String>> listaGrezza = getStati();

        for (List<String> listaRiga : listaGrezza) {
            lista.add(listaRiga.get(0));
        }

        return lista;
    }

    /**
     * Restituisce una lista degli stati <br>
     *
     * @return lista dei nome degli stati
     */
    public List<String> getSiglaStati() {
        List<String> lista = new ArrayList<>();
        List<List<String>> listaGrezza = getStati();

        for (List<String> listaRiga : listaGrezza) {
            lista.add(listaRiga.get(3));
        }

        return lista;
    }

    /**
     * Estrae una wikitable da una pagina wiki <br>
     * Restituisce una lista dei valori per ogni riga, esclusa la prima coi titoli <br>
     *
     * @return lista di valori per ogni riga significativa della wikitable
     */
    public Map<String, String> getTableStatiNumerico() {
        Map<String, String> mappa = new HashMap<>();
        List<List<String>> listaGrezza = null;
        String[] partiRiga = null;
        String sep = DOPPIO_PIPE_REGEX;
        String codice;
        String paese;

        try {
            listaGrezza = getTable(PAGINA_ISO_1_NUMERICO, 1);
        } catch (Exception unErrore) {
        }

        if (listaGrezza != null && listaGrezza.size() > 1) {
            for (List<String> riga : listaGrezza) {
                codice = riga.get(0).trim();
                paese = text.setNoQuadre(riga.get(1)).trim();
                mappa.put(codice, paese);
                //                partiRiga = rigaGrezza.get(0).split(sep);
                //                if (partiRiga.length == 2) {
                //                    codice = partiRiga[0].trim();
                //                    if (codice.startsWith(PIPE)) {
                //                        codice = text.levaTesta(codice, PIPE);
                //                    }
                //                    paese = text.setNoQuadre(partiRiga[1]).trim();
                //                    mappa.put(codice, paese);
                //                }
            }
        }

        return mappa;
    }

    //    /**
    //     * Estrae una wikitable da una pagina wiki <br>
    //     * Restituisce una lista dei valori per ogni riga, esclusa la prima coi titoli <br>
    //     *
    //     * @return lista di valori per ogni riga significativa della wikitable
    //     */
    //    public List<List<String>> getRegioni() {
    //        List<List<String>> listaTable = new ArrayList<>();
    //        List<List<String>> listaGrezza = getTable(PAGINA_ISO_2);
    //        List<String> riga;
    //        String iso;
    //        String nome;
    //        String sigla;
    //
    //        if (listaGrezza != null && listaGrezza.size() == 20) {
    //            for (List<String> rigaGrezza : listaGrezza) {
    //                riga = new ArrayList<>();
    //                iso = rigaGrezza.get(0);
    //                nome = rigaGrezza.get(1);
    //                iso = fixCodice(iso);
    //                nome = fixNomeRegione(nome);
    //                sigla = getSiglaDaNome(nome);//@todo Funzionalità ancora da implementare
    //                riga.add(nome);
    //                riga.add(iso);
    //                riga.add(sigla);
    //                listaTable.add(riga);
    //            }
    //        }
    //
    //        return listaTable;
    //    }

    //    //@todo Funzionalità ancora da implementare
    //    public String getSiglaDaNome(String nome) {
    //        String sigla = VUOTA;
    //
    //        if (text.isValid(nome)) {
    //            switch (nome) {
    //                case "Abruzzo":
    //                    sigla = "ABR";
    //                    break;
    //                case "Basilicata":
    //                    sigla = "BAS";
    //                    break;
    //                case "Calabria":
    //                    sigla = "CAL";
    //                    break;
    //                case "Campania":
    //                    sigla = "CAM";
    //                    break;
    //                case "Emilia-Romagna":
    //                    sigla = "EMR";
    //                    break;
    //                case "Friuli-Venezia Giulia":
    //                    sigla = "FVG";
    //                    break;
    //                case "Lazio":
    //                    sigla = "LAZ";
    //                    break;
    //                case "Liguria":
    //                    sigla = "LIG";
    //                    break;
    //                case "Lombardia":
    //                    sigla = "LOM";
    //                    break;
    //                case "Marche":
    //                    sigla = "MAR";
    //                    break;
    //                case "Molise":
    //                    sigla = "MOL";
    //                    break;
    //                case "Piemonte":
    //                    sigla = "PNM";
    //                    break;
    //                case "Puglia":
    //                    sigla = "PUG";
    //                    break;
    //                case "Sardegna":
    //                    sigla = "SAR";
    //                    break;
    //                case "Sicilia":
    //                    sigla = "SIC";
    //                    break;
    //                case "Toscana":
    //                    sigla = "TOS";
    //                    break;
    //                case "Trentino-Alto Adige":
    //                    sigla = "TAA";
    //                    break;
    //                case "Umbria":
    //                    sigla = "UMB";
    //                    break;
    //                case "Valle d'Aosta":
    //                    sigla = "VAO";
    //                    break;
    //                case "Veneto":
    //                    sigla = "VEN";
    //                    break;
    //                default:
    //                    logger.warn("Switch - caso non definito", this.getClass(), "getSiglaDaNome");
    //                    break;
    //            }
    //
    //        }
    //
    //        return sigla;
    //    }
    //
    //
    //    //@todo Funzionalità ancora da implementare
    //    public String getRegioneDaSigla(String sigla) {
    //        String nome = VUOTA;
    //
    //        if (text.isValid(sigla)) {
    //            switch (sigla) {
    //                case "ABR":
    //                    nome = "Abruzzo";
    //                    break;
    //                case "BAS":
    //                    nome = "Basilicata";
    //                    break;
    //                case "CAL":
    //                    nome = "Calabria";
    //                    break;
    //                case "CAM":
    //                    nome = "Campania";
    //                    break;
    //                case "EMR":
    //                    nome = "Emilia-Romagna";
    //                    break;
    //                case "FVG":
    //                    nome = "Friuli-Venezia Giulia";
    //                    break;
    //                case "LAZ":
    //                    nome = "Lazio";
    //                    break;
    //                case "LIG":
    //                    nome = "Liguria";
    //                    break;
    //                case "LOM":
    //                    nome = "Lombardia";
    //                    break;
    //                case "MAR":
    //                    nome = "Marche";
    //                    break;
    //                case "MOL":
    //                    nome = "Molise";
    //                    break;
    //                case "PNM":
    //                    nome = "Piemonte";
    //                    break;
    //                case "PUG":
    //                    nome = "Puglia";
    //                    break;
    //                case "SAR":
    //                    nome = "Sardegna";
    //                    break;
    //                case "SIC":
    //                    nome = "Sicilia";
    //                    break;
    //                case "TOS":
    //                    nome = "Toscana";
    //                    break;
    //                case "TAA":
    //                    nome = "Trentino-Alto Adige";
    //                    break;
    //                case "UMB":
    //                    nome = "Umbria";
    //                    break;
    //                case "VAO":
    //                    nome = "Valle d'Aosta";
    //                    break;
    //                case "VEN":
    //                    nome = "Veneto";
    //                    break;
    //                default:
    //                    logger.warn("Switch - caso non definito", this.getClass(), "getRegioneDaSigla");
    //                    break;
    //            }
    //
    //        }
    //
    //        return nome;
    //    }

    //    /**
    //     * Estrae una wikitable da una pagina wiki <br>
    //     * Restituisce una lista dei valori per ogni riga, esclusa la prima coi titoli <br>
    //     *
    //     * @return lista di valori per ogni riga significativa della wikitable
    //     */
    //    public List<List<String>> getProvince() {
    //        List<List<String>> listaTable = new ArrayList<>();
    //        List<List<String>> listaGrezza = getTable(PAGINA_PROVINCE, 1, 1);
    //        List<String> listaProvinceDellaRegione;
    //        List<String> riga;
    //        Regione regione;
    //        String siglaRegione;
    //        String nomeRegione;
    //        String testoRiga;
    //
    //        //--nella tabella su wiki c'è una riga in più di riepilogo che non interessa
    //        if (listaGrezza != null && listaGrezza.size() == 21) {
    //            for (List<String> rigaGrezza : listaGrezza.subList(0, 20)) {
    //                //                regione = rigaGrezza.get(1);
    //                siglaRegione = fixRegione(rigaGrezza.get(0));
    //                regione = regioneLogic.findBySigla(siglaRegione);
    //                nomeRegione = regione != null ? regione.getNome() : VUOTA;
    //                testoRiga = rigaGrezza.get(5);
    //                listaProvinceDellaRegione = getListaProvince(testoRiga);
    //                for (String provincia : listaProvinceDellaRegione) {
    //                    riga = new ArrayList<>();
    //                    riga.add(provincia);
    //                    riga.add(provincia);//@todo PROVVISORIO
    //                    riga.add(nomeRegione);
    //                    listaTable.add(riga);
    //                }
    //            }
    //        }
    //
    //        return listaTable;
    //    }

    //    private List<String> getListaProvince(String testoRiga) {
    //        List<String> lista = new ArrayList<>();
    //        String tag = "]]";
    //        String[] parti = testoRiga.split(tag);
    //        int pos = 0;
    //        String provincia = VUOTA;
    //
    //        for (String testo : parti) {
    //            if (testo.contains(PIPE)) {
    //                pos = testo.lastIndexOf(PIPE) + 1;
    //                provincia = testo.substring(pos, testo.length());
    //                if (text.isValid(provincia)) {
    //                    lista.add(provincia.trim());
    //                }
    //            }
    //        }
    //
    //        return lista;
    //    }


    /**
     * Estrae il contenuto del template bandierina indicato <br>
     *
     * @param wikiTitle della pagina wiki
     *
     * @return coppia di valori: sigla e nome
     */
    public WrapDueStringhe getTemplateBandierina(String wikiTitle) {
        WrapDueStringhe wrap = null;
        String tag = "Template:";
        String testoGrezzo = VUOTA;
        String sigla = VUOTA;

        if (text.isEmpty(wikiTitle)) {
            return null;
        }

        wikiTitle = text.setNoDoppieGraffe(wikiTitle);
        sigla = text.levaTestoPrimaDi(wikiTitle, TRATTINO);
        if (sigla.contains(PIPE)) {
            sigla = text.levaCodaDa(sigla, PIPE);
        }

        if (sigla.length() < 2) {
            sigla = "0" + sigla;
        }

        testoGrezzo = legge(tag + wikiTitle);

        if (text.isValid(testoGrezzo)) {
            if (testoGrezzo.startsWith(DOPPIE_GRAFFE_INI)) {
                wrap = estraeBandierinaGraffe(testoGrezzo, sigla);
            }
            else {
                if (testoGrezzo.contains("{{band div|ITA")) {
                    wrap = estraeBandierinaGraffe(testoGrezzo, sigla);
                }
                else {
                    wrap = estraeBandierinaQuadre(testoGrezzo, sigla);
                }
            }
        }

        return wrap;
    }


    /**
     * Restituisce un wrapper di valori: sigla e nome estratti dal template bandierine <br>
     * '{{band div|FRA|Borgogna-Franca Contea}}<noinclude>[[Categoria:Template bandierine regionali francesi|BFC]]</noinclude>' <br>
     *
     * @param testoGrezzo del template wiki
     * @param sigla       della suddivisione richiesta
     *
     * @return wrapper di valori: sigla e nome
     */
    public WrapDueStringhe estraeBandierinaGraffe(String testoGrezzo, String sigla) {
        WrapDueStringhe wrap = null;
        String testoGraffa = VUOTA;
        String[] parti = null;
        String nome = VUOTA;

        if (text.isValid(testoGrezzo)) {
            testoGraffa = estraeGraffa(testoGrezzo);
        }

        if (text.isValid(testoGraffa)) {
            parti = testoGraffa.split(PIPE_REGEX);
        }

        if (parti != null && parti.length >= 3) {
            nome = parti[2];
            wrap = new WrapDueStringhe(sigla, nome);
        }

        return wrap;
    }


    /**
     * Restituisce un wrapper di valori: sigla e nome estratti dal template bandierine <br>
     * '[[File:Blason_Auvergne-Rhône-Alpes.svg|20px]] [[Alvernia-Rodano-Alpi]] <noinclude>[[Categoria:Template bandierine regionali francesi|ARA]]</noinclude>' <br>
     *
     * @param testoGrezzo del template wiki
     * @param sigla       della suddivisione richiesta
     *
     * @return wrapper di valori: sigla e nome
     */
    public WrapDueStringhe estraeBandierinaQuadre(String testoGrezzo, String sigla) {
        WrapDueStringhe wrap = null;
        String testoQuadra = VUOTA;
        String tag = "<noinclude";
        String nome = VUOTA;

        if (text.isValid(testoGrezzo)) {
            testoQuadra = text.levaCodaDa(testoGrezzo, tag);
        }

        //--estrae la seconda quadra
        if (text.isValid(testoQuadra)) {
            testoQuadra = text.levaTestoPrimaDi(testoQuadra, QUADRE_END);
        }

        if (text.isValid(testoQuadra)) {
            nome = text.estrae(testoQuadra, QUADRE_INI, QUADRE_END).trim();
            if (nome.contains(PIPE)) {
                nome = text.levaTestoPrimaDi(nome, PIPE);
            }
            wrap = new WrapDueStringhe(sigla, nome);
        }

        return wrap;
    }


    /**
     * Restituisce una lista di stringhe estratte dai template bandierine <br>
     *
     * @param wikiTitle    della pagina wiki
     * @param posTabella   della wikitable nella pagina se ce ne sono più di una
     * @param rigaIniziale da cui estrarre le righe, scartando la testa della table
     * @param numColonna   da cui estrarre il template-bandierine
     *
     * @return lista di coppia di valori: sigla e nome
     */
    @Deprecated
    public List<WrapDueStringhe> getTemplateList(String wikiTitle, int posTabella, int rigaIniziale, int numColonna) {
        List<String> lista = getColonna(wikiTitle, posTabella, rigaIniziale, numColonna);
        return getTemplateList(lista);
    }


    /**
     * Restituisce una lista di stringhe estratte dai template bandierine <br>
     *
     * @param listaTemplate bandierine
     *
     * @return lista di coppia di valori: sigla e nome
     */
    @Deprecated
    public List<WrapDueStringhe> getTemplateList(List<String> listaTemplate) {
        List<WrapDueStringhe> lista = null;
        WrapDueStringhe wrap;

        if (array.isAllValid(listaTemplate)) {
            lista = new ArrayList<>();
            for (String wikiTitle : listaTemplate) {
                wrap = getTemplateBandierina(wikiTitle);
                if (wrap != null) {
                    lista.add(wrap);
                }
            }
        }

        return lista;
    }

    //    /**
    //     * Restituisce una lista di stringhe estratte dai template bandierine <br>
    //     *
    //     * @param wikiTitle             della pagina wiki
    //     * @param posTabella            della wikitable nella pagina se ce ne sono più di una
    //     * @param rigaIniziale          da cui estrarre le righe, scartando la testa della table
    //     * @param numColonnaBandierine  da cui estrarre il template-bandierine
    //     * @param numColonnaTerzoValore da cui estrarre il valore della terza stringa richiesta
    //     *
    //     * @return lista di tripletta di valori: sigla e nome e divisione amministrativa superiore
    //     */
    //    public List<WrapDueStringhe> getTemplateList(String wikiTitle, int posTabella, int rigaIniziale, int numColonnaBandierine, int numColonnaTerzoValore) {
    //        List<WrapDueStringhe> lista = getDueColonne(wikiTitle, posTabella, rigaIniziale, numColonnaBandierine,numColonnaTerzoValore);
    //        return null;
    //    }


    /**
     * Restituisce una lista di stringhe estratte dai template bandierine <br>
     *
     * @param wikiTitle             della pagina wiki
     * @param posTabella            della wikitable nella pagina se ce ne sono più di una
     * @param rigaIniziale          da cui estrarre le righe, scartando la testa della table
     * @param numColonnaBandierine  da cui estrarre il template-bandierine
     * @param numColonnaTerzoValore da cui estrarre il valore della terza stringa richiesta
     *
     * @return lista di tripletta di valori: sigla e nome e divisione amministrativa superiore
     */
    public List<WrapTreStringhe> getTemplateList(String wikiTitle, int posTabella, int rigaIniziale, int numColonnaBandierine, int numColonnaTerzoValore) {
        List<WrapTreStringhe> listaTre = null;
        WrapDueStringhe wrapBandierina;
        WrapTreStringhe wrapTre;
        List<WrapDueStringhe> listaDue = getDueColonne(wikiTitle, posTabella, rigaIniziale, numColonnaBandierine, numColonnaTerzoValore);

        if (array.isAllValid(listaDue)) {
            listaTre = new ArrayList<>();
            for (WrapDueStringhe wrapDue : listaDue) {
                wrapBandierina = getTemplateBandierina(wrapDue.getPrima());
                if (wrapBandierina != null) {
                    wrapTre = new WrapTreStringhe(wrapBandierina.getPrima(), wrapBandierina.getSeconda(), wrapDue.getSeconda());
                    listaTre.add(wrapTre);
                }
            }
        }

        return listaTre.subList(1, listaTre.size() - 1);
    }


    /**
     * Restituisce una colonna estratta da una wiki table <br>
     *
     * @param wikiTitle    della pagina wiki
     * @param posTabella   della wikitable nella pagina se ce ne sono più di una
     * @param rigaIniziale da cui estrarre le righe, scartando la testa della table
     * @param numColonna   da cui estrarre la cella
     *
     * @return lista di coppia di valori: sigla e nome
     */
    public List<String> getColonna(String wikiTitle, int posTabella, int rigaIniziale, int numColonna) {
        List<String> colonna = null;
        List<List<String>> lista = null;
        String cella = VUOTA;
        String[] parti = null;

        if (text.isEmpty(wikiTitle)) {
            return null;
        }

        try {
            lista = getTable(wikiTitle, posTabella);
        } catch (Exception unErrore) {
        }

        if (array.isAllValid(lista)) {
            colonna = new ArrayList<>();
            for (List<String> riga : lista) {
                if (riga.size() == 1 || (riga.size() == 2 && !riga.get(0).startsWith(GRAFFA_END))) {
                    parti = riga.get(0).split(DOPPIO_PIPE_REGEX);
                    if (parti != null && parti.length >= numColonna) {
                        cella = parti[numColonna - 1];
                    }
                }
                else {
                    if (!riga.get(0).startsWith(ESCLAMATIVO)) {
                        cella = riga.get(numColonna - 1);
                    }
                }
                if (text.isValid(cella)) {
                    cella = cella.trim();
                    cella = text.setNoDoppieGraffe(cella);
                    cella = fixCode(cella);
                    colonna.add(cella);
                }
            }
        }

        return colonna;
    }


    /**
     * Restituisce due colonne sincronizzate da una wiki table <br>
     * Esclude il testo di eventuali note <br>
     *
     * @param wikiTitle     della pagina wiki
     * @param posTabella    della wikitable nella pagina se ce ne sono più di una
     * @param rigaIniziale  da cui estrarre le righe, scartando la testa della table
     * @param numColonnaUno da cui estrarre la cella
     * @param numColonnaDue da cui estrarre la cella
     *
     * @return lista di coppia di valori: sigla e nome
     */
    @Deprecated
    public List<WrapDueStringhe> getDueColonne(String wikiTitle, int posTabella, int rigaIniziale, int numColonnaUno, int numColonnaDue) {
        List<WrapDueStringhe> listaWrap = null;
        WrapDueStringhe wrap;
        List<String> colonna = null;
        List<List<String>> lista = null;
        String cella = VUOTA;
        String[] parti = null;
        String prima;
        String seconda;

        if (text.isEmpty(wikiTitle)) {
            return null;
        }

        try {
            lista = getTable(wikiTitle, posTabella);
        } catch (Exception unErrore) {
        }

        if (array.isAllValid(lista)) {
            listaWrap = new ArrayList<>();
            for (List<String> riga : lista) {
                wrap = null;
                prima = VUOTA;
                seconda = VUOTA;
                if (riga.size() < 2 || (riga.size() == 2 && riga.get(1).startsWith(GRAFFA_END))) {
                    cella = riga.get(0);
                    parti = cella.split(DOPPIO_PIPE_REGEX);
                    if (parti != null && parti.length >= numColonnaDue) {
                        prima = parti[numColonnaUno - 1];
                        seconda = parti[numColonnaDue - 1];
                    }

                }
                else {
                    if (!riga.get(0).startsWith(ESCLAMATIVO)) {
                        prima = riga.get(numColonnaUno - 1);
                        seconda = riga.get(numColonnaDue - 1);
                    }
                }
                if (text.isValid(prima) && text.isValid(seconda)) {
                    prima = text.levaCodaDa(prima, REF);
                    seconda = text.levaCodaDa(seconda, REF);
                    prima = text.setNoDoppieGraffe(prima);
                    seconda = text.setNoDoppieGraffe(seconda);
                    prima = text.setNoQuadre(prima);
                    seconda = text.setNoQuadre(seconda);
                    prima = fixCode(prima);
                    seconda = fixCode(seconda);
                    prima = html.setNoHtmlTag(prima, "kbd");
                    if (prima.contains(PIPE)) {
                        if (prima.contains(DOPPIE_GRAFFE_INI) && prima.contains(DOPPIE_GRAFFE_END)) {
                        }
                        else {
                            prima = text.levaTestoPrimaDi(prima, PIPE);
                        }
                    }
                    if (seconda.contains(QUADRE_INI) && seconda.contains(QUADRE_END)) {
                        seconda = text.estrae(seconda, QUADRE_INI, QUADRE_END);
                    }
                    if (seconda.contains(PIPE)) {
                        if (seconda.contains(DOPPIE_GRAFFE_INI) && seconda.contains(DOPPIE_GRAFFE_END)) {
                        }
                        else {
                            seconda = text.levaTestoPrimaDi(seconda, PIPE);
                        }
                    }
                    wrap = new WrapDueStringhe(prima, seconda);
                }
                if (wrap != null) {
                    listaWrap.add(wrap);
                }
            }
        }

        return listaWrap;
    }


    /**
     * Import delle regioni (tutti gli stati) da una pagina di wikipedia <br>
     *
     * @param wikiTitle della pagina wiki
     *
     * @return lista di wrapper con due stringhe ognuno (sigla, nome)
     */
    public List<WrapDueStringhe> getRegioni(String wikiTitle) throws Exception {
        List<WrapDueStringhe> listaWrap = null;
        List<List<String>> listaTable = null;
        WrapDueStringhe wrap;

        listaTable = getTable(wikiTitle);
        if (listaTable == null || listaTable.size() < 1) {
            listaTable = getTable(wikiTitle, 2);
        }

        if (listaTable != null && listaTable.size() > 0) {
            listaWrap = new ArrayList<>();

            wrap = getWrapTitolo(listaTable.get(0));
            if (wrap != null) {
                listaWrap.add(wrap);
            }

            for (List<String> listaRiga : listaTable.subList(1, listaTable.size())) {
                wrap = getWrapRegione(listaRiga);
                if (wrap != null) {
                    listaWrap.add(wrap);
                }
            }
        }

        return listaWrap;
    }

    /**
     * Import delle province italiane da una pagina di wikipedia <br>
     *
     * @return lista di wrapper con due stringhe ognuno (sigla, nome)
     */
    public List<WrapTreStringhe> getProvince() {
        List<WrapTreStringhe> listaWrap = null;
        String wikiTitle = "Province d'Italia";
        List<List<String>> listaTable = null;
        WrapTreStringhe wrap;

        listaTable = getTable(wikiTitle);

        if (listaTable != null && listaTable.size() > 0) {
            listaWrap = new ArrayList<>();

            for (List<String> listaRiga : listaTable.subList(1, listaTable.size())) {
                wrap = getWrapProvincia(listaRiga);
                if (wrap != null) {
                    listaWrap.add(wrap);
                }
            }
        }

        return listaWrap;
    }

    /**
     * Probabilmente il secondo elemento della lista contiene i titoli <br>
     *
     * @param listaRiga valori di una singola riga di titoli
     *
     * @return wrapper di due stringhe (titoloUno, titoloDue)
     */
    public WrapDueStringhe getWrapTitolo(List<String> listaRiga) {
        WrapDueStringhe wrap = null;
        String titoloUno = VUOTA;
        String titoloDue = VUOTA;

        if (listaRiga != null && listaRiga.size() == 2) {
            titoloUno = listaRiga.get(0).trim();
            titoloDue = listaRiga.get(1).trim();
        }

        if (listaRiga != null && listaRiga.size() == 3) {
            titoloUno = listaRiga.get(1).trim();
            titoloDue = listaRiga.get(2).trim();
        }

        //--Azerbaigian
        if (listaRiga != null && listaRiga.size() == 4) {
            titoloUno = listaRiga.get(0).trim();
            titoloDue = listaRiga.get(1).trim();
        }

        if (text.isValid(titoloUno) && text.isValid(titoloDue)) {
            if (titoloUno.startsWith(ESCLAMATIVO)) {
                titoloUno = text.levaTestoPrimaDi(titoloUno, ESCLAMATIVO);
            }
            titoloUno = titoloUno.trim();

            if (titoloDue.contains(QUADRE_INI) && titoloDue.contains(QUADRE_END)) {
                titoloDue = text.estrae(titoloDue, QUADRE_INI, QUADRE_END);
            }
            if (titoloDue.contains(PIPE)) {
                titoloDue = text.levaTestoPrimaDi(titoloDue, PIPE);
            }
            if (titoloDue.startsWith(ESCLAMATIVO)) {
                titoloDue = text.levaTestoPrimaDi(titoloDue, ESCLAMATIVO);
            }
            titoloDue = titoloDue.trim();

            wrap = new WrapDueStringhe(titoloUno, titoloDue);
        }

        return wrap;
    }


    /**
     * Estrae una coppia di valori significativi da una lista eterogenea <br>
     * Se la lista ha un solo valore, qualcosa non funziona <br>
     * Se la lista ha più di due valori, occorre selezionare i due significativi <br>
     * Sicuramente uno dei due valori contiene la sigla (deve avere un trattino) <br>
     * Uno dei valori deve essere un nome oppure il link alle bandierine (deve avere le doppie graffe) <br>
     * Dalla eventuale bandierina recupero il nome <br>
     *
     * @param listaRiga valori di una singola regione
     *
     * @return wrapper di due stringhe valid (sigla, nome)
     */
    public WrapDueStringhe getWrapRegione(List<String> listaRiga) {
        String sigla = VUOTA;
        String nome = VUOTA;
        WrapDueStringhe wrap = null;

        if (listaRiga.size() < 2) {
            return null;
        }

        if (listaRiga.get(0).contains(TRATTINO)) {
            sigla = listaRiga.get(0);
        }
        else {
            if (listaRiga.get(1).contains(TRATTINO)) {
                sigla = listaRiga.get(1);
            }
            else {
                if (listaRiga.get(0).length() == 1) {
                    sigla = listaRiga.get(0);
                }
            }
        }

        //--finlandia
        if (text.isEmpty(nome) && listaRiga.size() == 4 && !listaRiga.get(1).contains(QUADRE_INI) && !listaRiga.get(1).contains(QUADRE_INI) && listaRiga.get(3).contains(QUADRE_INI) && listaRiga.get(3).contains(QUADRE_END)) {
            nome = listaRiga.get(3);
        }

        //--azerbaigian
        if (text.isEmpty(nome) && listaRiga.size() == 4 && listaRiga.get(3).contains(QUADRE_INI) && listaRiga.get(3).contains(QUADRE_END)) {
            nome = listaRiga.get(1);
        }

        if (text.isEmpty(nome) && listaRiga.get(1).contains(QUADRE_INI) && listaRiga.get(1).contains(QUADRE_END)) {
            nome = listaRiga.get(1);
        }
        else {
            if (listaRiga.size() > 2 && listaRiga.get(2).contains(QUADRE_INI) && listaRiga.get(2).contains(QUADRE_END)) {
                nome = listaRiga.get(2);
            }
        }

        //--solo per Italia (spero)
        if (text.isEmpty(nome) && listaRiga.get(1).contains(DOPPIE_GRAFFE_INI) && listaRiga.get(1).contains(PIPE) && listaRiga.get(1).contains(DOPPIE_GRAFFE_END)) {
            nome = text.estrae(listaRiga.get(1), DOPPIE_GRAFFE_INI, DOPPIE_GRAFFE_END);
            nome = text.estrae(nome, PIPE, PIPE);
        }

        //--template bandierine per recuperare il nome
        if (text.isEmpty(nome)) {
            if (listaRiga.get(1).contains(DOPPIE_GRAFFE_INI) && listaRiga.get(1).contains(DOPPIE_GRAFFE_END)) {
                wrap = getTemplateBandierina(listaRiga.get(1));
            }
            else {
                if (listaRiga.size() > 2 && listaRiga.get(2).contains(DOPPIE_GRAFFE_INI) && listaRiga.get(2).contains(DOPPIE_GRAFFE_END)) {
                    wrap = getTemplateBandierina(listaRiga.get(2));
                }
            }
        }

        if (text.isEmpty(sigla) && text.isValid(nome)) {
            sigla = listaRiga.get(0);
        }

        //--belize
        if (listaRiga.size() == 2 && text.isEmpty(nome) && sigla.equals(listaRiga.get(1))) {
            nome = listaRiga.get(0);
        }

        sigla = sigla.trim();
        sigla = html.setNoHtmlTag(sigla, "kbd");
        sigla = html.setNoHtmlTag(sigla, "code");
        sigla = text.levaCodaDa(sigla, "<ref");

        if (text.isValid(nome)) {
            nome = nome.trim();
            nome = text.estrae(nome, QUADRE_INI, QUADRE_END);
            nome = text.levaTestoPrimaDi(nome, PIPE);
            wrap = new WrapDueStringhe(sigla, nome);
        }
        else {
            if (wrap != null) {
                wrap.setPrima(sigla);
            }
        }

        return wrap;
    }

    /**
     * Estrae una tripletta di valori significativi da una lista eterogenea <br>
     * Se la lista ha un solo valore, qualcosa non funziona <br>
     * Se la lista ha più di due valori, occorre selezionare i due significativi <br>
     * Sicuramente uno dei due valori contiene la sigla (deve avere un trattino) <br>
     * Uno dei valori deve essere un nome oppure il link alle bandierine (deve avere le doppie graffe) <br>
     * Dalla eventuale bandierina recupero il nome <br>
     *
     * @param listaRiga valori di una singola regione
     *
     * @return wrapper di due stringhe valid (sigla, nome)
     */
    public WrapTreStringhe getWrapProvincia(List<String> listaRiga) {
        String sigla = VUOTA;
        String nome = VUOTA;
        String regione = VUOTA;
        WrapTreStringhe wrap = null;
        WrapDueStringhe wrapDue = null;
        String tagVdA = "Valle d'Aosta";

        if (listaRiga.size() < 3) {
            return null;
        }

        sigla = listaRiga.get(0).trim();
        regione = listaRiga.get(2).trim();
        regione = text.estrae(regione, QUADRE_INI, QUADRE_END);

        //--template bandierine per recuperare il nome
        if (sigla.contains(DOPPIE_GRAFFE_INI) && sigla.contains(DOPPIE_GRAFFE_END)) {
            if (regione.equals(tagVdA)) {
                sigla = "AO";
                nome = regione;
            }
            else {
                sigla = text.estraeGraffaDoppia(sigla);
                wrapDue = getTemplateBandierina(sigla);
                if (wrapDue != null) {
                    sigla = wrapDue.getPrima();
                    nome = wrapDue.getSeconda();
                }
            }
        }

        //        sigla = sigla.trim();
        //        sigla = text.setNoHtmlTag(sigla, "kbd");
        //        sigla = text.setNoHtmlTag(sigla, "code");
        //        sigla = text.levaCodaDa(sigla, "<ref");

        //        nome = nome.trim();
        //        nome = text.estrae(nome, QUADRE_INI, QUADRE_END);
        //        nome = text.levaTestoPrimaDi(nome, PIPE);
        //            wrap = new WrapDueStringhe(sigla, nome);

        //        regione = regione.trim();

        wrap = new WrapTreStringhe(sigla, nome, regione);
        return wrap;
    }

    /**
     * Estrae una wikitable da una pagina wiki <br>
     * Restituisce una lista dei valori per ogni riga, esclusa la prima coi titoli <br>
     *
     * @param wikiTitle della pagina wiki
     *
     * @return lista di valori per ogni riga significativa della wikitable
     */
    public List<List<String>> getTable(String wikiTitle) {
        return getTable(wikiTitle, 1);
    }


    /**
     * Estrae una wikitable da una pagina wiki <br>
     * Restituisce una lista di valori per ogni riga valida <br>
     * Restituisce anche la prima lista di titoli <br>
     * Esclude il testo che precede il primo punto ESCLAMATIVO, da scartare <br>
     * Poi estrae i titoli e poi esegue lo spilt per separare le righe valide <br>
     *
     * @param wikiTitle  della pagina wiki
     * @param posTabella della wikitable nella pagina se ce ne sono più di una
     *
     * @return lista di valori per ogni riga significativa della wikitable
     */
    public List<List<String>> getTable(String wikiTitle, int posTabella) {
        List<List<String>> listaTable = new ArrayList<>();
        List<String> listaRiga;
        String[] righeTable = null;
        String testoRigaSingola;
        String[] partiRiga = null;
        String tagTable = "\\|-";
        String testoTable = VUOTA;

        testoTable = leggeTable(wikiTitle, posTabella);

        //--elimina la testa di apertura della table per evitare fuffa
        if (text.isValid(testoTable)) {
            testoTable = text.levaTestoPrimaDi(testoTable, ESCLAMATIVO);
            testoTable = testoTable.trim();
        }

        //--elimina la coda di chiusura della table per evitare che la suddivisione in righe contenga anche la chiusura della table
        if (text.isValid(testoTable)) {
            if (testoTable.endsWith(GRAFFA_END)) {
                testoTable = text.levaCodaDa(testoTable, GRAFFA_END);
            }
            testoTable = testoTable.trim();
            if (testoTable.endsWith(PIPE)) {
                testoTable = text.levaCodaDa(testoTable, PIPE);
            }
            testoTable = testoTable.trim();
        }

        //--dopo aver eliminato la testa della tabella, la coda della tabella ed i titoli, individua le righe valide
        righeTable = testoTable.split(tagTable);

        if (righeTable != null && righeTable.length > 2) {
            for (int k = 0; k < righeTable.length; k++) {
                testoRigaSingola = righeTable[k].trim();
                if (testoRigaSingola.startsWith(ESCLAMATIVO)) {
                    continue;
                }
                partiRiga = getParti(testoRigaSingola);
                if (partiRiga != null && partiRiga.length > 0) {
                    listaRiga = new ArrayList<>();
                    for (String value : partiRiga) {
                        if (text.isValid(value)) {
                            value = value.trim();
                            if (value.startsWith(PIPE)) {
                                value = text.levaTesta(value, PIPE);
                            }
                            value = value.trim();
                            listaRiga.add(value);
                        }
                    }
                    if (!listaRiga.get(0).equals(ESCLAMATIVO)) {
                        listaTable.add(listaRiga);
                    }
                }
            }
        }

        return listaTable;
    }


    private String[] getParti(String testoRigaSingola) {
        String[] partiRiga = null;
        String tagUno = A_CAPO;
        String tagDue = DOPPIO_PIPE_REGEX;
        String tagTre = DOPPIO_ESCLAMATIVO;

        //--primo tentativo
        partiRiga = testoRigaSingola.split(tagUno);

        //--secondo tentativo
        if (partiRiga != null && partiRiga.length == 1) {
            partiRiga = testoRigaSingola.split(tagDue);
        }

        //--terzo tentativo
        if (partiRiga != null && partiRiga.length == 1) {
            partiRiga = testoRigaSingola.split(tagTre);
        }

        return partiRiga;
    }


    /**
     * Legge una wikitable da una pagina wiki <br>
     *
     * @param wikiTitle della pagina wiki
     *
     * @return testo della wikitable
     */
    public String leggeTable(String wikiTitle) {
        try {
            return leggeTable(wikiTitle, 1);
        } catch (Exception unErrore) {
        }
        return VUOTA;
    }


    /**
     * Legge una wikitable da una pagina wiki <br>
     *
     * @param wikiTitle della pagina wiki
     * @param pos       della wikitable nella pagina se ce ne sono più di una
     *
     * @return testo della wikitable
     */
    public String leggeTable(String wikiTitle, int pos) {
        String testoTable = VUOTA;
        String tag1 = "{|class=\"wikitable";
        String tag2 = "{| class=\"wikitable";
        String tag3 = "{|  class=\"wikitable";
        String tag4 = "{|class=\"sortable wikitable";
        String tag5 = "{| class=\"sortable wikitable";
        String tag6 = "{|  class=\"sortable wikitable";
        String tagEnd = "|}\n";
        int posIni = 0;
        int posEnd = 0;
        String testoPagina = legge(wikiTitle);

        if (text.isValid(testoPagina)) {
            if (testoPagina.contains(tag1) || testoPagina.contains(tag2) || testoPagina.contains(tag3) || testoPagina.contains(tag4) || testoPagina.contains(tag5) || testoPagina.contains(tag6)) {
                if (testoPagina.contains(tag1)) {
                    for (int k = 1; k <= pos; k++) {
                        posIni = testoPagina.indexOf(tag1, posIni + tag1.length());
                    }
                }
                if (testoPagina.contains(tag2)) {
                    for (int k = 1; k <= pos; k++) {
                        posIni = testoPagina.indexOf(tag2, posIni + tag2.length());
                    }
                }
                if (testoPagina.contains(tag3)) {
                    for (int k = 1; k <= pos; k++) {
                        posIni = testoPagina.indexOf(tag3, posIni + tag3.length());
                    }
                }
                if (testoPagina.contains(tag4)) {
                    for (int k = 1; k <= pos; k++) {
                        posIni = testoPagina.indexOf(tag4, posIni + tag4.length());
                    }
                }
                posEnd = testoPagina.indexOf(tagEnd, posIni) + tagEnd.length();
                testoTable = testoPagina.substring(posIni, posEnd);
            }
            else {
                //                throw new Exception("La pagina wiki " + wikiTitle + " non contiene nessuna wikitable. AWikiService.leggeTable()");
                //                logger.warn("La pagina wiki " + wikiTitle + " non contiene nessuna wikitable", this.getClass(), "leggeTable");
            }
        }

        return text.isValid(testoTable) ? testoTable.trim() : VUOTA;
    }

    /**
     * Legge un modulo di una pagina wiki <br>
     *
     * @param wikiTitle della pagina wiki
     *
     * @return testo del modulo, comprensivo delle graffe iniziale e finale
     */
    public String leggeModulo(String wikiTitle) {
        String testoModulo = VUOTA;
        String testoPagina = legge(wikiTitle);
        String tag = "return";

        if (text.isValid(testoPagina)) {
            if (testoPagina.contains(tag)) {
                testoModulo = text.levaTestoPrimaDi(testoPagina, tag);
            }
        }

        return text.isValid(testoModulo) ? testoModulo.trim() : VUOTA;
    }


    /**
     * Legge la mappa dei valori di modulo di una pagina wiki <br>
     *
     * @param wikiTitle della pagina wiki
     *
     * @return mappa chiave=valore
     */
    public Map<String, String> leggeMappaModulo(String wikiTitle) {
        Map<String, String> mappa = null;
        String tagRighe = VIRGOLA_CAPO;
        String tagSezioni = UGUALE_SEMPLICE;
        String[] righe = null;
        String[] sezioni = null;
        String key;
        String value;
        String testoModulo = leggeModulo(wikiTitle);

        if (text.isValid(testoModulo)) {
            testoModulo = text.setNoGraffe(testoModulo);
            righe = testoModulo.split(tagRighe);
        }

        if (righe != null && righe.length > 0) {
            mappa = new LinkedHashMap<>();
            for (String riga : righe) {

                sezioni = riga.split(tagSezioni);
                if (sezioni != null && sezioni.length == 2) {
                    key = sezioni[0];

                    key = text.setNoQuadre(key);
                    key = text.setNoDoppiApici(key);
                    value = sezioni[1];
                    value = text.setNoDoppiApici(value);
                    value = text.setNoGraffe(value);
                    mappa.put(key, value);
                }
            }
        }

        return mappa;
    }


    /**
     * Legge dal server wiki <br>
     * Testo in linguaggio wiki <br>
     *
     * @param wikiTitle della pagina wiki
     *
     * @return testo visibile della pagina
     */
    public String legge(String wikiTitle) {
        String testoPagina = VUOTA;
        String webUrl;
        String contenutoCompletoPaginaWebInFormatoJSON;

        try {
            wikiTitle = text.isValid(wikiTitle) ? URLEncoder.encode(wikiTitle, ENCODE) : VUOTA;
        } catch (Exception unErrore) {
            logger.error(unErrore, this.getClass(), "legge");

        }
        webUrl = WIKI_URL + wikiTitle;

        contenutoCompletoPaginaWebInFormatoJSON = text.isValid(webUrl) ? web.leggeWeb(webUrl) : VUOTA;
        testoPagina = text.isValid(contenutoCompletoPaginaWebInFormatoJSON) ? estraeTestoPaginaWiki(contenutoCompletoPaginaWebInFormatoJSON) : VUOTA;

        //        contenutoCompletoPaginaWebInFormatoJSON = web.leggeWeb(webUrl);
        //        testoPagina = estraeTestoPaginaWiki(contenutoCompletoPaginaWebInFormatoJSON);

        return testoPagina;
    }


    /**
     * Recupera la mappa dei valori dal testo JSON di una singola pagina
     * 21 parametri
     * 10 generali
     * 8 revisions
     * 3 slots/main
     *
     * @param singolaPaginaTextJSON in ingresso
     *
     * @return mappa parametri di una pagina
     */
    public String estraeTestoPaginaWiki(String singolaPaginaTextJSON) {
        String testoPagina = VUOTA;
        JSONArray arrayPagine = this.getArrayPagine(singolaPaginaTextJSON);

        if (arrayPagine != null) {
            testoPagina = this.getContent((JSONObject) arrayPagine.get(0));
        }

        return testoPagina;
    }


    /**
     * Recupera un array di pagine dal testo JSON di una pagina action=query
     *
     * @param contenutoCompletoPaginaWebInFormatoJSON in ingresso
     *
     * @return parametri pages
     */
    public JSONArray getArrayPagine(String contenutoCompletoPaginaWebInFormatoJSON) {
        JSONArray arrayQuery = null;
        JSONObject objectQuery = getObjectQuery(contenutoCompletoPaginaWebInFormatoJSON);

        //--recupera i valori dei parametri pages
        if (objectQuery != null && objectQuery.get(PAGES) != null && objectQuery.get(PAGES) instanceof JSONArray) {
            arrayQuery = (JSONArray) objectQuery.get(PAGES);
        }

        return arrayQuery;
    }


    /**
     * Recupera il contenuto testuale dal testo JSON di una singola pagina
     * 21 parametri
     * 10 generali
     * 8 revisions
     * 3 slots/main
     *
     * @param paginaTextJSON in ingresso
     *
     * @return testo della pagina wiki
     */
    public String getContent(JSONObject paginaTextJSON) {
        String textContent = VUOTA;
        JSONArray arrayRevisions;
        JSONObject objectRevisions = null;
        JSONObject objectSlots;
        JSONObject objectMain = null;

        if (paginaTextJSON == null) {
            return null;
        }

        //--parametri revisions
        if (paginaTextJSON.get(REVISIONS) != null && paginaTextJSON.get(REVISIONS) instanceof JSONArray) {
            arrayRevisions = (JSONArray) paginaTextJSON.get(REVISIONS);
            if (arrayRevisions != null && arrayRevisions.size() > 0 && arrayRevisions.get(0) instanceof JSONObject) {
                objectRevisions = (JSONObject) arrayRevisions.get(0);
            }
        }

        //--parametri slots/main -> content
        if (objectRevisions != null && objectRevisions.get(SLOTS) != null && objectRevisions.get(SLOTS) instanceof JSONObject) {
            objectSlots = (JSONObject) objectRevisions.get(SLOTS);
            if (objectSlots.get(MAIN) != null && objectSlots.get(MAIN) instanceof JSONObject) {
                objectMain = (JSONObject) objectSlots.get(MAIN);
            }
        }

        if (objectMain != null && objectMain.get(CONTENT) != null) {
            textContent = (String) objectMain.get(CONTENT);
        }

        return textContent;
    }


    /**
     * Recupera l'oggetto pagina dal testo JSON di una pagina action=query
     *
     * @param contenutoCompletoPaginaWebInFormatoJSON in ingresso
     *
     * @return parametri pages
     */
    public JSONObject getObjectQuery(String contenutoCompletoPaginaWebInFormatoJSON) {
        JSONObject objectQuery = null;
        JSONObject objectAll = (JSONObject) JSONValue.parse(contenutoCompletoPaginaWebInFormatoJSON);

        //--recupera i valori dei parametri pages
        if (objectAll != null && objectAll.get(QUERY) != null && objectAll.get(QUERY) instanceof JSONObject) {
            objectQuery = (JSONObject) objectAll.get(QUERY);
        }

        return objectQuery;
    }


    /**
     * Sorgente completo di una pagina web <br>
     * Testo 'grezzo' <br>
     *
     * @param paginaWiki da leggere
     *
     * @return sorgente
     */
    @Deprecated
    public String getSorgente(String paginaWiki) {
        return web.leggeSorgenteWiki(paginaWiki);
    }


    /**
     * Legge una porzione di testo incolonnato dalla pagina wikipedia <br>
     *
     * @param wikiTitle della pagina wiki
     *
     * @return testo contenuto nelle colonne
     */
    public String leggeColonne(String wikiTitle) {
        String testoIncolonnato = VUOTA;
        String tagIni = "{{Colonne}}";
        String tagEnd = "{{Colonne fine}}";
        int posIni = 0;
        int posEnd = 0;
        String testoPagina = legge(wikiTitle);

        if (text.isValid(testoPagina)) {
            if (testoPagina.contains(tagIni)) {
                posIni = testoPagina.indexOf(tagIni);
                posEnd = testoPagina.indexOf(tagEnd, posIni);
                testoIncolonnato = testoPagina.substring(posIni, posEnd);
            }
        }

        return testoIncolonnato;
    }


    /**
     * Import da una pagina di wikipedia <br>
     *
     * @return lista di wrapper con due stringhe ognuno
     */
    @Deprecated
    public List<WrapDueStringhe> estraeListaDue(String pagina, String titoli, int posUno, int posDue) {
        List<WrapDueStringhe> listaWrap = null;
        List<List<String>> matriceTable = null;
        String[] titoliTable = text.getMatrice(titoli);
        WrapDueStringhe wrapGrezzo;

        matriceTable = web.getMatriceTableWiki(pagina, titoliTable);
        if (matriceTable != null && matriceTable.size() > 0) {
            listaWrap = new ArrayList<>();
            for (List<String> riga : matriceTable) {
                wrapGrezzo = new WrapDueStringhe(riga.get(posUno - 1), posDue > 0 ? riga.get(posDue - 1) : VUOTA);
                listaWrap.add(wrapGrezzo);
            }
        }
        return listaWrap;
    }


    /**
     * Import da una pagina di wikipedia <br>
     *
     * @return lista di wrapper con tre stringhe ognuno
     */
    @Deprecated
    public List<WrapTreStringhe> estraeListaTre(String pagina, String titoli) {
        List<WrapTreStringhe> listaWrap = null;
        LinkedHashMap<String, LinkedHashMap<String, String>> mappaGenerale = null;
        LinkedHashMap<String, String> mappa;
        String[] titoliTable = text.getMatrice(titoli);
        String tagUno = titoliTable[0];
        String tagDue = titoliTable[1];
        String tagTre = titoliTable[2];
        WrapTreStringhe wrapGrezzo;

        mappaGenerale = web.getMappaTableWiki(pagina, titoliTable);
        if (mappaGenerale != null && mappaGenerale.size() > 0) {
            listaWrap = new ArrayList<>();
            for (String elemento : mappaGenerale.keySet()) {
                mappa = mappaGenerale.get(elemento);
                wrapGrezzo = new WrapTreStringhe(mappa.get(tagUno), mappa.get(tagDue), mappa.get(tagTre));
                listaWrap.add(wrapGrezzo);
            }
        }
        return listaWrap;
    }


    public String fixCode(String testoGrezzo) {
        String testoValido = VUOTA;
        String tagIni = "<code>";
        String tagEnd = "</code>";

        if (text.isEmpty(testoGrezzo)) {
            return VUOTA;
        }

        if (!testoGrezzo.contains(tagIni) || !testoGrezzo.contains(tagEnd)) {
            return testoGrezzo;
        }

        testoValido = testoGrezzo.trim();
        if (testoValido.startsWith(tagIni)) {
            testoValido = text.levaTesta(testoValido, tagIni);
            testoValido = text.levaCoda(testoValido, tagEnd);
        }
        else {
            testoValido = text.estrae(testoValido, tagIni, tagEnd);
        }

        return testoValido.trim();
    }


    public String fixNome(String testoGrezzo) {
        String testoValido = VUOTA;
        int posIni = 0;
        int posEnd = 0;

        if (text.isEmpty(testoGrezzo)) {
            return VUOTA;
        }

        testoValido = testoGrezzo.trim();
        posEnd = testoValido.lastIndexOf("</a>");
        if (posEnd > 0) {
            testoValido = testoValido.substring(0, posEnd);
        }// end of if cycle
        posIni = testoValido.lastIndexOf(">") + 1;
        testoValido = testoValido.substring(posIni);

        return testoValido;
    }


    public String fixNomeStato(String testoGrezzo) {
        String testoValido = VUOTA;
        String tag = PIPE;
        int pos = 0;

        if (text.isEmpty(testoGrezzo)) {
            return VUOTA;
        }

        testoValido = testoGrezzo.trim();
        if (testoGrezzo.contains(tag)) {
            pos = testoGrezzo.indexOf(tag) + tag.length();
            testoValido = testoGrezzo.substring(pos);
        }

        return testoValido.trim();
    }

    //    public String fixNomeRegione(String testoGrezzo) {
    //        String testoValido = VUOTA;
    //        String tag = "\\|";
    //        String[] parti;
    //
    //        if (text.isEmpty(testoGrezzo)) {
    //            return VUOTA;
    //        }
    //
    //        testoValido = testoGrezzo.trim();
    //        testoValido = text.setNoGraffe(testoValido);
    //        parti = testoValido.split(tag);
    //
    //        if (parti.length == 3) {
    //            testoValido = parti[1];
    //        }
    //
    //        return testoValido.trim();
    //    }


    public String fixRegione(String testoGrezzo) {
        String testoValido = VUOTA;
        String tag = "-";
        int pos = 0;
        String[] parti = null;

        if (text.isEmpty(testoGrezzo)) {
            return VUOTA;
        }

        if (testoGrezzo.contains(tag)) {
            pos = testoGrezzo.indexOf(tag) + tag.length();
            testoValido = testoGrezzo.substring(pos, pos + 3);
        }

        return testoValido.trim();
    }

    //    public String fixRegione(String testoGrezzo) {
    //        String testoValido = VUOTA;
    //        String[] parti = null;
    //
    //        if (text.isEmpty(testoGrezzo)) {
    //            return VUOTA;
    //        }
    //
    //        if (testoGrezzo.contains(PIPE)) {
    //            parti = testoGrezzo.split(PIPE_REGEX);
    //            if (parti != null && parti.length == 2) {
    //                testoValido = parti[1].trim();
    //                testoValido = text.setNoQuadre(testoValido);
    //            }
    //        }
    //
    //        return testoValido.trim();
    //    }


    /**
     * Import delle regioni da una pagina di wikipedia <br>
     *
     * @return lista di wrapper con due stringhe ognuno (sigla, nome)
     */
    @Deprecated
    public List<WrapDueStringhe> regioni() {
        List<WrapDueStringhe> listaWrap = null;
        List<WrapDueStringhe> listaWrapGrezzo = null;
        WrapDueStringhe wrapValido;
        String titoli = "Codice,Regioni";
        String prima;
        String seconda;

        listaWrapGrezzo = estraeListaDue(PAGINA_ISO_2, titoli, 1, 2);
        if (listaWrapGrezzo != null && listaWrapGrezzo.size() > 0) {
            listaWrap = new ArrayList<>();
            for (WrapDueStringhe wrap : listaWrapGrezzo) {
                prima = wrap.getPrima();
                seconda = wrap.getSeconda();
                prima = fixCode(prima);
                seconda = fixNome(seconda);
                wrapValido = new WrapDueStringhe(prima, seconda);
                listaWrap.add(wrapValido);
            }
        }

        return listaWrap;
    }


    /**
     * Import degli stati da una pagina di wikipedia <br>
     *
     * @return lista di wrapper con tre stringhe ognuno (sigla, nome, regione)
     */
    @Deprecated
    public List<WrapQuattroStringhe> stati() {
        List<WrapQuattroStringhe> listaWrap = null;
        List<WrapQuattroStringhe> listaWrapGrezzo = null;
        WrapQuattroStringhe wrapValido;
        String titoli = "Codice,Regioni";
        String prima;
        String seconda;

        //        listaWrapGrezzo = estraeListaDue(PAGINA, titoli, 1, 2);
        //        if (listaWrapGrezzo != null && listaWrapGrezzo.size() > 0) {
        //            listaWrap = new ArrayList<>();
        //            for (WrapDueStringhe wrap : listaWrapGrezzo) {
        //                prima = wrap.getPrima();
        //                seconda = wrap.getSeconda();
        //                prima = elaboraCodice(prima);
        //                seconda = elaboraNome(seconda);
        //                wrapValido = new WrapDueStringhe(prima, seconda);
        //                listaWrap.add(wrapValido);
        //            }
        //        }

        return listaWrap;
    }


    public String estraeGraffaCon(String testoCompleto) {
        String testoValido = VUOTA;
        int posIni;
        int posEnd;

        if (testoCompleto.contains(DOPPIE_GRAFFE_INI) && testoCompleto.contains(DOPPIE_GRAFFE_END)) {
            posIni = testoCompleto.indexOf(GRAFFA_INI);
            posEnd = testoCompleto.indexOf(DOPPIE_GRAFFE_END) + DOPPIE_GRAFFE_END.length();
            if (posIni >= 0 && posEnd > posIni) {
                testoValido = testoCompleto.substring(posIni, posEnd);
            }
        }

        return testoValido;
    }


    public String estraeGraffa(String testoCompleto) {
        return text.setNoDoppieGraffe(estraeGraffaCon(testoCompleto));
    }

}