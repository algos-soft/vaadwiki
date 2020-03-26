package it.algos.vaadwiki.integration;

import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.modules.preferenza.PreferenzaService;
import it.algos.vaadwiki.ATest;
import it.algos.vaadwiki.download.ElaboraService;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.vaadwiki.service.LibBio;
import it.algos.vaadwiki.service.ParBio;
import it.algos.vaadwiki.upload.UploadService;
import it.algos.wiki.Api;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedHashMap;
import java.util.Map;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 10-set-2019
 * Time: 10:16
 * Assert.assertEquals(previsto, ottenuto);
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TemplateBioIntegrationTest extends ATest {

    private static LinkedHashMap<String, String> mappaNome;

    private static LinkedHashMap<ParBio, String> mappaParBio;

    private static LinkedHashMap<ParBio, String> mappaParBioEstesa;

    private static String titoloBio = "Emma Marrone";

    private static String titoloBio2 = "Flavio Bucci";

//    private static String titoloBio3 = "Giuseppe Frignani";

    private static String titoloBio3 = "Giovanni Dellepiane";

//    private static String titoloBio3 = "Giuseppe Fiori (narratore)";

    @Autowired
    public BioService bioService;

    @Autowired
    public PreferenzaService pref;

    @Autowired
    public Api api;

    @Autowired
    public ElaboraService elaboraService;

    @Autowired
    public UploadService uploadService;

    @Autowired
    protected LibBio libBio;

    @Autowired
    protected AnnoService annoService;

    private String[] listaSorgente;


    @BeforeClass
    public static void setUp() {
        mappaNome = getMappaNome();
        mappaParBio = getMappaParBio();
        mappaParBioEstesa = getMappaParBioEstesa();
    }// end of static method


    private static LinkedHashMap<String, String> getMappaNome() {
        LinkedHashMap<String, String> mappa = new LinkedHashMap<>();

        mappa.put("Nome", "Aldo");
        mappa.put("Cognome", "Brunetta");
        mappa.put("Attività", "Cantante");

        System.out.println("");
        System.out.println("*************Mappa");
        for (Map.Entry<String, String> entry : mappa.entrySet()) {
            System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
        }// end of for cycle
        System.out.println("*************Mappa");

        return mappa;
    }// end of static method


    private static LinkedHashMap<ParBio, String> getMappaParBio() {
        LinkedHashMap<ParBio, String> mappaParBio = new LinkedHashMap<>();

        mappaParBio.put(ParBio.nome, "Francesco");
        mappaParBio.put(ParBio.cognome, "Pope");
        mappaParBio.put(ParBio.nazionalita, "polacco");

        System.out.println("");
        System.out.println("*************MappaParBio");
        for (Map.Entry<ParBio, String> entry : mappaParBio.entrySet()) {
            System.out.println("Key : " + entry.getKey().getTag() + " Value : " + entry.getValue());
        }// end of for cycle
        System.out.println("*************MappaParBio");

        return mappaParBio;
    }// end of static method


    private static LinkedHashMap<ParBio, String> getMappaParBioEstesa() {
        LinkedHashMap<ParBio, String> mappaParBioEstesa = new LinkedHashMap<>();

        mappaParBioEstesa.put(ParBio.titolo, "Onorevole");
        mappaParBioEstesa.put(ParBio.nome, "Marcello");
        mappaParBioEstesa.put(ParBio.cognome, "Giolitti");
        mappaParBioEstesa.put(ParBio.pseudonimo, "mark");
        mappaParBioEstesa.put(ParBio.noteNascita, "Data non conosciuta");
        mappaParBioEstesa.put(ParBio.nazionalita, "russo");
        mappaParBioEstesa.put(ParBio.cittadinanza, "americana");

        System.out.println("");
        System.out.println("*************MappaParBioEstesa");
        for (Map.Entry<ParBio, String> entry : mappaParBioEstesa.entrySet()) {
            System.out.println("Key : " + entry.getKey().getTag() + " Value : " + entry.getValue());
        }// end of for cycle
        System.out.println("*************MappaParBioEstesa");

        return mappaParBioEstesa;
    }// end of static method


    @Test
    public void metodoPerOrdinareTuttiTest() {
        creaRigaTemplate();
        creaRigaTemplate2();
        creaTemplateSenzaGraffe();
        creaTemplate();
        creaTemplateBioSenzaGraffe();
        creaTemplateBio();
        creaTemplateBioEsteso();
        creaTemplateBioEntity();
        creaTemplateMerged();
        creaTemplateMerged2();
        creaTemplateMerged3();
//        uploadBio();
    }// end of single test


    /**
     * Crea la singola riga del template chiave=valore con pipe e ritorno a capo
     *
     * @param parBioText nome della property del template
     * @param value      corrispondente
     *
     * @return riga di testo
     */
    public void creaRigaTemplate() {
        String testoGrezzo = "";
        for (Map.Entry<String, String> entry : mappaNome.entrySet()) {
            testoGrezzo += libBio.creaRigaTemplate(entry.getKey(), entry.getValue());
        }// end of for cycle
        testoGrezzo = testoGrezzo.trim();

        System.out.println("");
        System.out.println("*************creaBioPar");
        System.out.println("Uno. Crea, con 3 chiamate separate, un pacchetto di 3 parametri costruiti da String");
        System.out.println("*************creaBioPar");
        System.out.println(testoGrezzo);
        System.out.println("*************creaBioPar");
        System.out.println("");
    }// end of single test


    /**
     * Crea la singola riga del template chiave=valore con pipe e ritorno a capo
     * Rimanda la metodo base recuperando la stringa dal ParBio
     *
     * @param parBio enumeration da cui estrarre il nome della property
     * @param value  corrispondente
     *
     * @return riga di testo
     */
    public void creaRigaTemplate2() {
        String testoGrezzo = "";
        for (Map.Entry<ParBio, String> entry : mappaParBio.entrySet()) {
            testoGrezzo += libBio.creaRigaTemplate(entry.getKey(), entry.getValue());
        }// end of for cycle
        testoGrezzo = testoGrezzo.trim();

        System.out.println("");
        System.out.println("*************creaBioPar");
        System.out.println("Due. Crea, con 3 chiamate separate, un pacchetto di 3 parametri costruiti col ParBio");
        System.out.println("*************creaBioPar");
        System.out.println(testoGrezzo);
        System.out.println("*************creaBioPar");
        System.out.println("");
    }// end of single test


    /**
     * Crea tutte le righe della mappa ricevuta
     * Usa SOLO i parametri presenti nella mappa
     *
     * @param mappa chiave=valore da costruire
     *
     * @return testo del template senza graffe
     */
    public void creaTemplateSenzaGraffe() {
        String testoGrezzo = libBio.creaTemplateSenzaGraffe(mappaNome);

        System.out.println("");
        System.out.println("*************creaTemplateSenzaGraffe");
        System.out.println("Tre. Crea un pacchetto di righe, costruite da una mappa chiave=valore");
        System.out.println("Tre. Usa SOLO i parametri presenti nella mappa");
        System.out.println("*************creaTemplateSenzaGraffe");
        System.out.println(testoGrezzo);
        System.out.println("*************creaTemplateSenzaGraffe");
        System.out.println("");
    }// end of single test


    /**
     * Crea il template bio della mappa ricevuta
     * Usa SOLO i parametri presenti nella mappa
     *
     * @param mappa chiave=valore da costruire
     *
     * @return testo del template bio comprensivo di graffe di apertura e chiusura
     */
    public void creaTemplate() {
        String testoGrezzo = libBio.creaTemplate(mappaNome);

        System.out.println("");
        System.out.println("*************creaTemplate");
        System.out.println("Quattro. Crea un template bio ridotto, costruito da una mappa chiave=valore");
        System.out.println("Quattro. Usa SOLO i parametri presenti nella mappa");
        System.out.println("*************creaTemplate");
        System.out.println(testoGrezzo);
        System.out.println("*************creaTemplate");
        System.out.println("");
    }// end of single test


    /**
     * Costruisce il template Bio della mappa ricevuta con i parametri (valorizzati) in ingresso
     * Aggiunge (vuoti) i parametri obbligatori mancanti
     *
     * @param mappa chiave=valore da costruire
     *
     * @return testo del template senza graffe
     */
    public void creaTemplateBioSenzaGraffe() {
        String testoGrezzo = libBio.creaTemplateBioSenzaGraffe(mappaParBio);

        System.out.println("");
        System.out.println("*************creaTemplateBioSenzaGraffe");
        System.out.println("Cinque. Crea tutte le righe normali di un template bio, costruite da una mappa chiave=valore");
        System.out.println("Cinque. Aggiunge (vuoti) i parametri obbligatori mancanti");
        System.out.println("*************creaTemplateBioSenzaGraffe");
        System.out.println(testoGrezzo);
        System.out.println("*************creaTemplateBioSenzaGraffe");
        System.out.println("");
    }// end of single test


    /**
     * Costruisce il template Bio della mappa ricevuta con i parametri (valorizzati) in ingresso
     * Aggiunge (vuoti) i parametri obbligatori mancanti
     *
     * @param mappa chiave=valore da costruire
     *
     * @return testo del template bio comprensivo di graffe di apertura e chiusura
     */
    public void creaTemplateBio() {
        String testoGrezzo = libBio.creaTemplateBio(mappaParBio);

        System.out.println("");
        System.out.println("*************creaTemplateBio");
        System.out.println("Sei. Crea un template bio normale, costruito da una mappa chiave=valore");
        System.out.println("Sei. Aggiunge (vuoti) i parametri obbligatori mancanti");
        System.out.println("*************creaTemplateBio");
        System.out.println(testoGrezzo);
        System.out.println("*************creaTemplateBio");
        System.out.println("");
    }// end of single test


    /**
     * Costruisce il template Bio della mappa ricevuta con i parametri (valorizzati) in ingresso
     * Nella mappa ci possono essere anche parametri non obbligatori che vengono mantenuti
     * Aggiunge (vuoti) i parametri obbligatori mancanti
     *
     * @param mappa chiave=valore da costruire
     *
     * @return testo del template bio comprensivo di graffe di apertura e chiusura
     */
    public void creaTemplateBioEsteso() {
        String testoGrezzo = libBio.creaTemplateBio(mappaParBioEstesa);

        System.out.println("");
        System.out.println("*************creaTemplateBioEsteso");
        System.out.println("Sette. Crea un template bio esteso, costruito da una mappa chiave=valore");
        System.out.println("Sette. Nella mappa ci possono essere anche parametri non obbligatori che vengono mantenuti");
        System.out.println("Sette. Aggiunge (vuoti) i parametri obbligatori mancanti");
        System.out.println("*************creaTemplateBioEsteso");
        System.out.println(testoGrezzo);
        System.out.println("*************creaTemplateBioEsteso");
        System.out.println("");
    }// end of single test


    /**
     * Costruisce il template Bio dai dati della entity
     * Aggiunge (vuoti) i parametri obbligatori mancanti
     * NON aggiunge altri parametri non presenti nella entity e non obbligatori
     *
     * @param entity di riferimento
     *
     * @return testo del template bio comprensivo di graffe di apertura e chiusura
     */
    public void creaTemplateBioEntity() {
        Bio bio = api.leggeBio(titoloBio);
        String testoGrezzo = libBio.creaTemplateBio(bio);

        System.out.println("");
        System.out.println("*************creaTemplateBioEntity");
        System.out.println("Otto. Crea un template bio esteso, costruito da una entity della classe Bio");
        System.out.println("Otto. NON aggiunge altri parametri non presenti nella entity e non obbligatori");
        System.out.println("Otto. Aggiunge (vuoti) i parametri obbligatori mancanti");
        System.out.println("*************creaTemplateBioEntity");
        System.out.println(testoGrezzo);
        System.out.println("*************creaTemplateBioEntity");
        System.out.println("");
    }// end of single test


    /**
     * Costruisce il template Bio
     * Merge tra il template del server ed i dati (eventualmente) modificati della entity
     * NON aggiunge altri parametri non presenti sul server e non obbligatori
     *
     * @param entity di riferimento
     *
     * @return testo del template bio comprensivo di graffe di apertura e chiusura
     */
    public void creaTemplateMerged() {
        Bio bio = api.leggeBio(titoloBio);
        String testoGrezzoEntity = libBio.creaTemplateBio(bio);
        String testoGrezzoMerged = libBio.mergeTemplates(bio.tmplBioServer, bio);

        System.out.println("");
        System.out.println("*************creaTemplateMerged");
        System.out.println("Nove. Crea un template bio");
        System.out.println("Nove. Merge tra il template del server ed i dati (eventualmente) modificati della entity");
        System.out.println("Nove. NON aggiunge altri parametri non presenti sul server e non obbligatori");
        System.out.println("*************templateOriginarioDelServer");
        System.out.println(bio.getTmplBioServer());
        System.out.println("*************templateVirtualeInMemoriaConLePropertyDellaEntity");
        System.out.println(testoGrezzoEntity);
        System.out.println("*************templateMerged");
        System.out.println(testoGrezzoMerged);
        System.out.println("*************creaTemplateMerged");
        System.out.println("");
    }// end of single test


    /**
     * Costruisce il template Bio
     * Merge tra il template del server ed i dati (eventualmente) modificati della entity
     * NON aggiunge altri parametri non presenti sul server e non obbligatori
     *
     * @param entity di riferimento
     *
     * @return testo del template bio comprensivo di graffe di apertura e chiusura
     */
    public void creaTemplateMerged2() {
        Bio bio = api.leggeBio(titoloBio2);
        bio.setLuogoNato("Napoli"); //modificato
        bio.setLuogoMorto("Palermo"); //aggiunto
        bio.setAnnoMorte(annoService.findByKeyUnica("1948"));
        String testoGrezzoEntity = libBio.creaTemplateBio(bio);
        String testoGrezzoMerged = libBio.mergeTemplates(bio.tmplBioServer, bio);

        System.out.println("");
        System.out.println("*************creaTemplateMerged");
        System.out.println("Dieci. Crea un template bio");
        System.out.println("Dieci. Merge tra il template del server ed i dati (eventualmente) modificati della entity");
        System.out.println("Dieci. NON aggiunge altri parametri non presenti sul server e non obbligatori");
        System.out.println("*************templateOriginarioDelServer");
        System.out.println(bio.getTmplBioServer());
        System.out.println("*************propertyModificata");
        System.out.println("LuogoNascita -> Napoli (modificato)");
        System.out.println("LuogoMorte -> Palermo (aggiunto)");
        System.out.println("AnnoMorte -> 1948 (aggiunto)");
        System.out.println("*************templateVirtualeInMemoriaConLePropertyDellaEntity");
        System.out.println(testoGrezzoEntity);
        System.out.println("*************templateMerged");
        System.out.println(testoGrezzoMerged);
        System.out.println("*************creaTemplateMerged");
        System.out.println("");
    }// end of single test


    /**
     * Costruisce il template Bio
     * Merge tra il template del server ed i dati (eventualmente) modificati della entity
     * NON aggiunge altri parametri non presenti sul server e non obbligatori
     *
     * @param entity di riferimento
     *
     * @return testo del template bio comprensivo di graffe di apertura e chiusura
     */
    public void creaTemplateMerged3() {
        Bio bio = api.leggeBio(titoloBio3);
        bio.setSesso("M"); //aggiunto
        String testoGrezzoEntity = libBio.creaTemplateBio(bio);
        String testoGrezzoMerged = libBio.mergeTemplates(bio.tmplBioServer, bio);

        System.out.println("");
        System.out.println("*************creaTemplateMerged");
        System.out.println("Undici. Crea un template bio");
        System.out.println("Undici. Merge tra il template del server ed i dati (eventualmente) modificati della entity");
        System.out.println("Undici. NON aggiunge altri parametri non presenti sul server e non obbligatori");
        System.out.println("*************templateOriginarioDelServer");
        System.out.println(bio.getTmplBioServer());
        System.out.println("*************propertyModificata");
        System.out.println("Sesso -> M (aggiunto)");
        System.out.println("*************templateVirtualeInMemoriaConLePropertyDellaEntity");
        System.out.println(testoGrezzoEntity);
        System.out.println("*************templateMerged");
        System.out.println(testoGrezzoMerged);
        System.out.println("*************creaTemplateMerged");
        System.out.println("");
    }// end of single test


    /**
     * Elabora un valore valido <br>
     * Non serve la entity Bio <br>
     * Con perdita di informazioni <br>
     * NON deve essere usato per sostituire tout-court il valore del template ma per elaborarlo <br>
     * Eventuali parti terminali inutili vengono scartate ma devono essere conservate a parte per il template <br>
     *
     * @param value valore in ingresso da elaborare
     *
     * @return valore finale valido
     */
    @Test
    public void fix() {
        fixNome();
        fixCognome();
        fixSesso();
        fixLuogoNascita();
        fixGiornoMeseNascita();
        fixAnnoNascita();
        fixLuogoMorte();
        fixGiornoMeseMorte();
        fixAnnoMorte();
        fixAttivita();
        fixNazionalita();
    }// end of single test


    private void fixNome() {
        previsto = "Mario";
        listaSorgente = new String[]{"[[Mario]]", "[Mario]", "Mario<ref>Pippoz</ref>", "Mario?", "Mario ?", "Mario{{template:pippoz}}"};

        System.out.println("");
        System.out.println("****nome****");
        for (String sorgente : listaSorgente) {
            stampaPar(ParBio.nome, sorgente, previsto);
        }// end of for cycle

        stampaParInterrogativoNo(ParBio.nome);
    }// end of single test


    private void fixCognome() {
        previsto = "Rossi";
        listaSorgente = new String[]{"[[Rossi]]", "[Rossi]", "Rossi<ref>Pippoz</ref>", "Rossi?", "Rossi ?", "Rossi{{template:pippoz}}"};

        System.out.println("");
        System.out.println("****cognome****");
        for (String sorgente : listaSorgente) {
            stampaPar(ParBio.cognome, sorgente, previsto);
        }// end of for cycle

        stampaParInterrogativoNo(ParBio.cognome);
    }// end of single test


    private void fixSesso() {
        previsto = "M";
        listaSorgente = new String[]{"m", "M?", "m?", "maschio", "uomo", "Uomo", "Maschio"};
        System.out.println("");
        System.out.println("****sesso****");
        for (String sorgente : listaSorgente) {
            stampaPar(ParBio.sesso, sorgente, previsto);
        }// end of for cycle

        previsto = "F";
        listaSorgente = new String[]{"f", "F?", "f?", "femmina", "donna", "Femmina", "Donna"};
        System.out.println("");
        for (String sorgente : listaSorgente) {
            stampaPar(ParBio.sesso, sorgente, previsto);
        }// end of for cycle

        previsto = VUOTA;
        listaSorgente = new String[]{"trans", "incerto", "?", "non si sa", "dubbio"};
        System.out.println("");
        for (String sorgente : listaSorgente) {
            stampaPar(ParBio.sesso, sorgente, previsto);
        }// end of for cycle

        stampaParVuoto(ParBio.sesso);
    }// end of single test


    private void fixLuogoNascita() {
        previsto = "Palermo";
        listaSorgente = new String[]{"[[Palermo]]", "[Palermo]", "Palermo<ref>Pippoz</ref>", "Palermo?", "Palermo ?", "Palermo{{template:pippoz}}"};

        System.out.println("");
        System.out.println("****luogoNascita****");
        for (String sorgente : listaSorgente) {
            stampaPar(ParBio.luogoNascita, sorgente, previsto);
        }// end of for cycle

        stampaParInterrogativoNo(ParBio.luogoNascita);
    }// end of single test


    private void fixGiornoMeseNascita() {
        previsto = "2 febbraio";
        System.out.println("");
        System.out.println("****giornoMeseNascita****");

        listaSorgente = new String[]{"2febbraio", "[[2 febbraio]]", "2 Febbraio", "[2 Febbraio]", "2  febbraio", "2 febbraio?", "2 febbraio ?", "2 febbraio circa", "2 febbraio, pippoz"};
        for (String sorgente : listaSorgente) {
            stampaPar(ParBio.giornoMeseNascita, sorgente, previsto);
        }// end of for cycle

        listaSorgente = new String[]{"2-febbraio", "[[2-febbraio]]", "[[2-Febbraio]]", "2-Febbraio", "2/febbraio", "2/Febbraio", "2-febbraio?"};
        for (String sorgente : listaSorgente) {
            stampaPar(ParBio.giornoMeseNascita, sorgente, previsto);
        }// end of for cycle

        previsto = VUOTA;
        listaSorgente = new String[]{"2marzolino", "[[2 treno]]", "[[2tebbraio]]", "2 Pebbraio", "febbraio2", "febbraio 3"};
        System.out.println("");
        for (String sorgente : listaSorgente) {
            stampaPar(ParBio.giornoMeseNascita, sorgente, previsto);
        }// end of for cycle

        stampaParInterrogativoSi(ParBio.giornoMeseNascita);
    }// end of single test


    private void fixAnnoNascita() {
        previsto = "1847";
        listaSorgente = new String[]{"[[1847]]", "1847 <ref>Pippoz>", "1847?", "1847 ?", "1847, pippoz", "1847,pippoz", "[[1847]]?", "1847 circa", "[[1847]] circa"};

        System.out.println("");
        System.out.println("****annoNascita****");
        for (String sorgente : listaSorgente) {
            stampaPar(ParBio.annoNascita, sorgente, previsto);
        }// end of for cycle

        stampaParInterrogativoSi(ParBio.annoNascita);
    }// end of single test


    private void fixLuogoMorte() {
        previsto = "Barletta";
        listaSorgente = new String[]{"[[Barletta]]", "[Barletta]", "Barletta<ref>Pippoz</ref>", "Barletta?", "Barletta ?", "Barletta{{template:pippoz}}"};

        System.out.println("");
        System.out.println("****luogoMorte****");
        for (String sorgente : listaSorgente) {
            stampaPar(ParBio.luogoMorte, sorgente, previsto);
        }// end of for cycle

        stampaParInterrogativoNo(ParBio.luogoMorte);
    }// end of single test


    private void fixGiornoMeseMorte() {
        previsto = "7 aprile";
        System.out.println("");
        System.out.println("****giornoMeseMorte****");

        listaSorgente = new String[]{"7aprile", "[[7 aprile]]", "7 Aprile", "[7 Aprile]", "7  aprile", "7 aprile?", "7 aprile ?", "7 aprile circa", "7 aprile, pippoz"};
        for (String sorgente : listaSorgente) {
            stampaPar(ParBio.giornoMeseMorte, sorgente, previsto);
        }// end of for cycle

        listaSorgente = new String[]{"7-aprile", "[[7-aprile]]", "[[7-Aprile]]", "7-Aprile", "7/aprile", "7/Aprile", "7-aprile?"};
        for (String sorgente : listaSorgente) {
            stampaPar(ParBio.giornoMeseMorte, sorgente, previsto);
        }// end of for cycle

        previsto = VUOTA;
        listaSorgente = new String[]{"7marzolino", "[[7 treno]]", "[[7tebbraio]]", "7 Pebbraio", "aprile7", "aprile 7"};
        System.out.println("");
        for (String sorgente : listaSorgente) {
            stampaPar(ParBio.giornoMeseMorte, sorgente, previsto);
        }// end of for cycle

        stampaParInterrogativoSi(ParBio.giornoMeseMorte);
    }// end of single test


    private void fixAnnoMorte() {
        previsto = "1451";
        listaSorgente = new String[]{"[[1451]]", "1451 <ref>Pippoz>", "1451?", "1451 ?", "1451, pippoz", "1451,pippoz", "[[1451]]?", "1451 circa", "[[1451]] circa"};

        System.out.println("");
        System.out.println("****annoMorte****");
        for (String sorgente : listaSorgente) {
            stampaPar(ParBio.annoMorte, sorgente, previsto);
        }// end of for cycle

        stampaParInterrogativoSi(ParBio.annoMorte);
    }// end of single test


    private void fixAttivita() {
        previsto = "attore";
        listaSorgente = new String[]{" attore", " attore<ref>Pippox>", "attore?", "attore{{pert}}"};

        System.out.println("");
        System.out.println("****attivita****");
        for (String sorgente : listaSorgente) {
            stampaPar(ParBio.attivita, sorgente, previsto);
        }// end of for cycle
        stampaParInterrogativoNo(ParBio.attivita);

        System.out.println("");
        System.out.println("****attivita2****");
        for (String sorgente : listaSorgente) {
            stampaPar(ParBio.attivita2, sorgente, previsto);
        }// end of for cycle
        stampaParInterrogativoNo(ParBio.attivita2);

        System.out.println("");
        System.out.println("****attivita3****");
        for (String sorgente : listaSorgente) {
            stampaPar(ParBio.attivita3, sorgente, previsto);
        }// end of for cycle
        stampaParInterrogativoNo(ParBio.attivita3);
    }// end of single test


    private void fixNazionalita() {
        previsto = "francese";
        listaSorgente = new String[]{" francese", " francese<ref>Pippox>", "francese?", "francese{{forse}}"};

        System.out.println("");
        System.out.println("****nazionalita****");
        for (String sorgente : listaSorgente) {
            stampaPar(ParBio.nazionalita, sorgente, previsto);
        }// end of for cycle
        stampaParInterrogativoNo(ParBio.nazionalita);
    }// end of single test


    private void stampaPar(ParBio par, String sorgente, String previsto) {
        ottenuto = par.fix(sorgente, libBio);
        Assert.assertEquals(previsto, ottenuto);
        if (ottenuto.equals(VUOTA)) {
            ottenuto = "'vuoto'";
        }// end of if cycle
        System.out.println(sorgente + " diventa " + ottenuto);
    }// end of method


    /**
     * Per ogni parametro controlla che il valore vuoto venga gestito <br>
     */
    private void stampaParVuoto(ParBio par) {
        ottenuto = par.fix(VUOTA, libBio);
        Assert.assertEquals(VUOTA, ottenuto);
        System.out.println("controllata validità del parametro vuoto in ingresso");
    }// end of method


    /**
     * Per ogni parametro controlla che il valore vuoto venga gestito <br>
     * Se il valore è un punto interrogativo, rimane un punto interrogativo <br>
     */
    private void stampaParInterrogativoSi(ParBio par) {
        String previsto = LibBio.INTERROGATIVO;
        stampaParVuoto(par);

        sorgente = LibBio.INTERROGATIVO;
        ottenuto = par.fix(sorgente, libBio);
        Assert.assertEquals(previsto, ottenuto);
        System.out.println("il punto interrogativo (ammesso per il parametro " + par.getTag() + ") rimane punto interrogativo");
    }// end of method


    /**
     * Per ogni parametro controlla che il valore vuoto venga gestito <br>
     * Se il valore è un punto interrogativo, diventa un valore vuoto (il punto interrogativo non è ammesso) <br>
     */
    private void stampaParInterrogativoNo(ParBio par) {
        String previsto = VUOTA;
        stampaParVuoto(par);

        sorgente = LibBio.INTERROGATIVO;
        ottenuto = par.fix(sorgente, libBio);
        Assert.assertEquals(previsto, ottenuto);
        System.out.println("il punto interrogativo (non ammesso per il parametro " + par.getTag() + ") diventa 'vuoto'");
    }// end of method


    /**
     * Carica sul server wiki la entity indicata
     * <p>
     * 1) Recupera la entity dal mongoDB (parametri eventualmente modificati dal programma)
     * 2) Scarica la voce dal server (senza modificare il template)
     * 4) Esegue un merge (ragionato) tra il template del server e la entity
     * 5) Sostituisce il templateMerged al testoServerNew nel testo della voce
     * 6) Upload del testo
     *
     * @param wikiTitle della pagina wiki (obbligatorio, unico)
     */
    public void uploadBio() {
        //--controllare che il flag di preferenze FlowCost.USA_DEBUG sia true
        uploadService.uploadBio(titoloBio3);
    }// end of single test

}// end of class
