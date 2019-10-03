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
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 10-set-2019
 * Time: 10:16
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
