package it.algos.vaadwiki.integration;

import it.algos.vaadflow.application.FlowCost;
import it.algos.vaadwiki.ATest;
import it.algos.vaadwiki.download.ElaboraService;
import it.algos.vaadwiki.download.PageService;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.vaadwiki.service.LibBio;
import it.algos.vaadwiki.service.ParBio;
import it.algos.wiki.Api;
import it.algos.wiki.Page;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 02-apr-2020
 * Time: 18:50
 * Classe di test per raggruppare tutti i test del template Bio
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BioIntegrationTest extends ATest {

    private final static String TITOLO = "Utente:Biobot/7";

    private final static String TITOLO_DUE = "Antonio Guerra (politico)";

    private final static String TITOLO_TRE = "Pedro Muguruza";

    private static String BIO_UNO = "{{Bio\n" +
            "|Nome = Rudolf\n" +
            "|Cognome = Kolisch\n" +
            "|Sesso = M\n" +
            "|LuogoNascita = Klamm am Semmering\n" +
            "|GiornoMeseNascita = 20 luglio\n" +
            "|AnnoNascita = 1896\n" +
            "|LuogoMorte = Watertown\n" +
            "|LuogoMorteLink = Watertown (Massachusetts)\n" +
            "|GiornoMeseMorte = 1º agosto\n" +
            "|AnnoMorte = 1978\n" +
            "|Epoca = 1900\n" +
            "|Epoca2 = 2000\n" +
            "|Attività = Violinista\n" +
            "|Nazionalità = austriaco\n" +
            "|NazionalitàNaturalizzato = statunitense\n" +
            "}}";

    private static String BIO_DUE = "{{Bio\n" +
            "    |Nome = Crystle Danae\n" +
            "|Cognome = [[Stewart]]\n" +
            "|GiornoMeseMorte=\n" +
            "    |PostCognome = \n" +
            "|Sesso = F\n" +
            " | LuogoNascita = [[Wilmington]]\n" +
            " | GiornoMeseNascita = 1 Settembre\n" +
            "|AnnoNascita = [[1981]]\n" +
            "|AnnoMorte=\n" +
            "|LuogoMorte = ? \n" +
            "|Nazionalità = statunitense ?\n" +
            "|Attività = Modella<ref>Dal 2000</ref>\n" +
            "|Attività2 = \n" +
            "|PostNazionalità = {{sp}}incoronata [[Miss USA]] [[2008]]<ref>{{cite web\n" +
            "|url= http://www.washingtonpost.com/wp-dyn/content/article/2008/04/12/AR2008041200018.html\n" +
            "|title= Texan Takes Miss {{sp}} USA Crown in Las Vegas\n" +
            "|accessdate= 2008-05-10\n" +
            "|date= 2008-04-12\n" +
            "|publisher= Washington Post\n" +
            "}}</ref>\n" +
            "|Immagine=\n" +
            "|Didascalia=\n" +
            "}}";

    @Autowired
    protected LibBio libBio;

    @Autowired
    private Api api;

    @Autowired
    private BioService bioService;

    @Autowired
    private PageService pageService;

    @Autowired
    private ElaboraService elaboraService;

    private Page page;


    @Before
    public void setUpIniziale() {
        Assert.assertNotNull(text);
        Assert.assertNotNull(libBio);
        Assert.assertNotNull(api);
        Assert.assertNotNull(bioService);
    }// end of method


    private Bio getBio(String titolo) {
        Bio bio;

        page = api.leggePage(titolo);
        Assert.assertNotNull(page);

        bio = pageService.creaBio(page);
        Assert.assertNotNull(page);

        return bio;
    }// end of single test


    private Bio getBio() {
        return getBio(TITOLO);
    }// end of single test


    /**
     * Crea una entity Bio partendo da una Page <br>
     * La entity NON viene salvata <br>
     *
     * @param page scaricata dal server wiki
     *
     * @return entity Bio
     */
    @Test
    public void creaBio() {
        Bio bio;

        page = api.leggePage(TITOLO);
        Assert.assertNotNull(page);

        bio = pageService.creaBio(page);
        Assert.assertNotNull(page);

        previsto = BIO_UNO;
        ottenuto = bio.tmplBioServer;
        Assert.assertEquals(previsto, ottenuto);
    }// end of single test


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Properties <br>
     * <p>
     * La entity viene creata con le SOLE seguenti property valide:
     * id
     * pageid
     * wikiTitle
     * tmplBioServer
     * lastLettura
     *
     * @param page scaricata dal server wiki
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Test
    public void newEntity() {
        Bio bio;

        page = api.leggePage(TITOLO);
        Assert.assertNotNull(page);

        bio = bioService.newEntity(page);
        Assert.assertNotNull(bio);
        Assert.assertNotNull(bio.tmplBioServer);

        Assert.assertEquals(TITOLO, bio.wikiTitle);
        Assert.assertNull(bio.nome);
        Assert.assertNull(bio.cognome);
        Assert.assertEquals(FlowCost.START_DATE_TIME, bio.lastModifica);

    }// end of single test


    /**
     * Nell'ordine:
     * 0) type del parametro (enumeration) <br>
     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' risultante <br>
     * 2) valore elaborato valido (minuscole, quadre, ecc.), restituibile al server non necessariamente registrabile sul mongoDB <br>
     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
     * 5) eventuale testo preesistente sul mongoDB <br>
     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>
     */
    private List<Object[]> getAll() {
        List<Object[]> lista = new ArrayList<>();

        Object[] listaParametroNome = {ParBio.nome, getListaParametroNome()};
        lista.add(listaParametroNome);

        Object[] listaParametroCognome = {ParBio.cognome, getListaParametroCognome()};
        lista.add(listaParametroCognome);

        Object[] listaParametroSesso = {ParBio.sesso, getListaParametroSesso()};
        lista.add(listaParametroSesso);

        Object[] listaParametroLuogoNascita = {ParBio.luogoNascita, getListaParametroLuogoNascita()};
        lista.add(listaParametroLuogoNascita);

        Object[] listaParametroGiornoMeseNascita = {ParBio.giornoMeseNascita, getListaParametroGiornoMeseNascita()};
        lista.add(listaParametroGiornoMeseNascita);

        Object[] listaParametroAnnoNascita = {ParBio.annoNascita, getListaParametroAnnoNascita()};
        lista.add(listaParametroAnnoNascita);

        Object[] listaParametroLuogoMorte = {ParBio.luogoMorte, getListaParametroLuogoMorte()};
        lista.add(listaParametroLuogoMorte);

        Object[] listaParametroGiornoMeseMorte = {ParBio.giornoMeseMorte, getListaParametroGiornoMeseMorte()};
        lista.add(listaParametroGiornoMeseMorte);

        Object[] listaParametroAnnoMorte = {ParBio.annoMorte, getListaParametroAnnoMorte()};
        lista.add(listaParametroAnnoMorte);

        Object[] listaParametroAttivita = {ParBio.attivita, getListaParametroAttivita()};
        lista.add(listaParametroAttivita);

        Object[] listaParametroNazionalita = {ParBio.nazionalita, getListaParametroNazionalita()};
        lista.add(listaParametroNazionalita);


        return lista;
    }// end of single test


    /**
     * Lista di possibili valori per il parametro <br>
     * Nell'ordine:
     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' risultante <br>
     * 2) valore elaborato valido (minuscole, quadre, ecc.), restituibile al server non necessariamente registrabile sul mongoDB <br>
     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
     * 5) eventuale testo preesistente sul mongoDB <br>
     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>
     */
    private List<String[]> getListaParametroNome() {
        List<String[]> lista = new ArrayList<>();

        lista.add(new String[]{"mario", "mario", "Mario", "Mario", "Mario", "", "Mario"});
        lista.add(new String[]{"maRio", "maRio", "MaRio", "MaRio", "MaRio", "", "MaRio"});
        lista.add(new String[]{"[[Mario]]", "[[Mario]]", "Mario", "Mario", "Mario", "", "Mario"});
        lista.add(new String[]{"[Mario]", "[Mario]", "Mario", "Mario", "Mario", "", "Mario"});

        lista.add(new String[]{"[[Mario]]<ref>Pippoz</ref>", "[[Mario]]", "Mario", "Mario", "Mario<ref>Pippoz</ref>", "", "Mario<ref>Pippoz</ref>"});
        lista.add(new String[]{"[[Mario]]<!--Prova-->", "[[Mario]]", "Mario", "Mario", "Mario<!--Prova-->", "", "Mario<!--Prova-->"});
        lista.add(new String[]{"[[Mario]]{{forse}}", "[[Mario]]", "Mario", "Mario", "Mario{{forse}}", "", "Mario{{forse}}"});
        lista.add(new String[]{"Mario=Giovanni", "Mario", "Mario", "Mario", "Mario=Giovanni", "", "Mario=Giovanni"});
        lista.add(new String[]{"Mario circa", "Mario", "Mario", "Mario", "Mario circa", "", "Mario circa"});
        lista.add(new String[]{"Mario ecc.", "Mario", "Mario", "Mario", "Mario ecc.", "", "Mario ecc."});
        lista.add(new String[]{"Mario ?", "Mario", "Mario", "Mario", "Mario", "", "Mario"});
        lista.add(new String[]{"Mario?", "Mario", "Mario", "Mario", "Mario", "", "Mario"});

        lista.add(new String[]{"Crystle Danae?", "Crystle Danae", "Crystle Danae", "Crystle Danae", "Crystle Danae", "Pluto Paperino", "Pluto Paperino"});
        lista.add(new String[]{"[[Giovanna]]<ref>Pippoz</ref>", "[[Giovanna]]", "Giovanna", "Giovanna", "Giovanna<ref>Pippoz</ref>", "Maria", "Maria<ref>Pippoz</ref>"});

        lista.add(new String[]{"?", "", "", "", "", "", ""}); //punto interrogativo (da solo) NON ammesso. Viene cancellato dal server.
        lista.add(new String[]{"", "", "", "", "", "", ""});

        return lista;
    }// end of single test


    /**
     * Lista di possibili valori per il parametro <br>
     * Nell'ordine:
     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' <br>
     * 2) valore elaborato valido (minuscole, quadre, ecc.), restituibile al server non necessariamente registrabile sul mongoDB <br>
     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
     * 5) eventuale testo preesistente sul mongoDB <br>
     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>
     */
    private List<String[]> getListaParametroCognome() {
        List<String[]> lista = new ArrayList<>();

        lista.add(new String[]{"rossi", "rossi", "Rossi", "Rossi", "Rossi", "", "Rossi"});
        lista.add(new String[]{"roSSi", "roSSi", "RoSSi", "RoSSi", "RoSSi", "", "RoSSi"});
        lista.add(new String[]{"[[Rossi]]", "[[Rossi]]", "Rossi", "Rossi", "Rossi", "", "Rossi"});
        lista.add(new String[]{"[Rossi]", "[Rossi]", "Rossi", "Rossi", "Rossi", "", "Rossi"});

        lista.add(new String[]{"[[Rossi]]<ref>Pippoz</ref>", "[[Rossi]]", "Rossi", "Rossi", "Rossi<ref>Pippoz</ref>", "", "Rossi<ref>Pippoz</ref>"});
        lista.add(new String[]{"[[Rossi]]<!--Prova-->", "[[Rossi]]", "Rossi", "Rossi", "Rossi<!--Prova-->", "", "Rossi<!--Prova-->"});
        lista.add(new String[]{"[[Rossi]]{{forse}}", "[[Rossi]]", "Rossi", "Rossi", "Rossi{{forse}}", "", "Rossi{{forse}}"});
        lista.add(new String[]{"Rossi=Giovanni", "Rossi", "Rossi", "Rossi", "Rossi=Giovanni", "", "Rossi=Giovanni"});
        lista.add(new String[]{"Rossi?", "Rossi", "Rossi", "Rossi", "Rossi", "", "Rossi"});

        lista.add(new String[]{"[[Beretta]]<ref>Pippoz</ref>", "[[Beretta]]", "Beretta", "Beretta", "Beretta<ref>Pippoz</ref>", "Mancuso", "Mancuso<ref>Pippoz</ref>"});
        lista.add(new String[]{"Rovagnati?", "Rovagnati", "Rovagnati", "Rovagnati", "Rovagnati", "Fisichella", "Fisichella"});

        lista.add(new String[]{"?", "", "", "", "", "", ""}); //punto interrogativo NON ammesso. Viene cancellato dal server.
        lista.add(new String[]{"", "", "", "", "", "", ""});

        return lista;
    }// end of single test


    /**
     * Lista di possibili valori per il parametro <br>
     * Nell'ordine:
     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' <br>
     * 2) valore elaborato valido (minuscole, quadre, ecc.), restituibile al server non necessariamente registrabile sul mongoDB <br>
     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
     * 5) eventuale testo preesistente sul mongoDB <br>
     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>
     */
    private List<String[]> getListaParametroSesso() {
        List<String[]> lista = new ArrayList<>();

//        lista.add(new String[]{"m", "m", "M", "M", "M", "", "M"});
//        lista.add(new String[]{"m?", "m", "M", "M", "M", "", "M"});
//        lista.add(new String[]{"M?", "M", "M", "M", "M", "", "M"});
//        lista.add(new String[]{"maschio", "maschio", "M", "M", "M", "", "M"});
//        lista.add(new String[]{"Maschio", "Maschio", "M", "M", "M", "", "M"});
//        lista.add(new String[]{"uomo", "uomo", "M", "M", "M", "", "M"});
//        lista.add(new String[]{"Uomo", "Uomo", "M", "M", "M", "", "M"});
//        lista.add(new String[]{"maschio<ref>forse<ref>", "maschio", "M", "M", "M<ref>forse<ref>", "", "M<ref>forse<ref>"});
//        lista.add(new String[]{"Maschio<ref>forse<ref>", "Maschio", "M", "M", "M<ref>forse<ref>", "", "M<ref>forse<ref>"});
//
//        lista.add(new String[]{"f", "f", "F", "F", "F", "", "F"});
//        lista.add(new String[]{"f?", "f", "F", "F", "F", "", "F"});
//        lista.add(new String[]{"F?", "F", "F", "F", "F", "", "F"});
//        lista.add(new String[]{"femmina", "femmina", "F", "F", "F", "", "F"});
//        lista.add(new String[]{"Femmina", "Femmina", "F", "F", "F", "", "F"});
//        lista.add(new String[]{"donna", "donna", "F", "F", "F", "", "F"});
//        lista.add(new String[]{"Donna", "Donna", "F", "F", "F", "", "F"});

        lista.add(new String[]{"trans", "trans", "trans", "", "trans", "", "trans"});
        lista.add(new String[]{"incerto", "incerto", "incerto", "", "incerto", "", "incerto"});
        lista.add(new String[]{"dubbio", "dubbio", "dubbio", "", "dubbio", "", "dubbio"});
        lista.add(new String[]{"non si sa", "non si sa", "non si sa", "", "non si sa", "", "non si sa"});

        lista.add(new String[]{"?", "", "", "", "", "", ""}); //punto interrogativo NON ammesso. Viene cancellato dal server.
        lista.add(new String[]{"", "", "", "", "", "", ""});

        return lista;
    }// end of single test


    /**
     * Lista di possibili valori per il parametro <br>
     * Nell'ordine:
     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' <br>
     * 2) valore elaborato valido (minuscole, quadre, ecc.), restituibile al server non necessariamente registrabile sul mongoDB <br>
     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
     * 5) eventuale testo preesistente sul mongoDB <br>
     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>
     */
    private List<String[]> getListaParametroLuogoNascita() {
        List<String[]> lista = new ArrayList<>();

        lista.add(new String[]{"wilmington", "wilmington", "Wilmington", "Wilmington", "Wilmington", "", "Wilmington"});
        lista.add(new String[]{"[[Wilmington]]", "[[Wilmington]]", "Wilmington", "Wilmington", "Wilmington", "", "Wilmington"});
        lista.add(new String[]{"[Wilmington]", "[Wilmington]", "Wilmington", "Wilmington", "Wilmington", "", "Wilmington"});
        lista.add(new String[]{"[[Wilmington]]{{forse}}", "[[Wilmington]]", "Wilmington", "Wilmington", "Wilmington{{forse}}", "", "Wilmington{{forse}}"});
        lista.add(new String[]{"[[Wilmington]]?", "[[Wilmington]]", "Wilmington", "Wilmington", "Wilmington", "", "Wilmington"});

        lista.add(new String[]{"[[Wilmington]]<ref>Pippoz</ref>", "[[Wilmington]]", "Wilmington", "Wilmington", "Wilmington<ref>Pippoz</ref>", "", "Wilmington<ref>Pippoz</ref>"});
        lista.add(new String[]{"[[Wilmington]]<!--Prova-->", "[[Wilmington]]", "Wilmington", "Wilmington", "Wilmington<!--Prova-->", "", "Wilmington<!--Prova-->"});
        lista.add(new String[]{"[[Wilmington]]{{forse}}", "[[Wilmington]]", "Wilmington", "Wilmington", "Wilmington{{forse}}", "", "Wilmington{{forse}}"});
        lista.add(new String[]{"Wilmington=Giovanni", "Wilmington", "Wilmington", "Wilmington", "Wilmington=Giovanni", "", "Wilmington=Giovanni"});
        lista.add(new String[]{"Wilmington?", "Wilmington", "Wilmington", "Wilmington", "Wilmington", "", "Wilmington"});

        lista.add(new String[]{"[[Wilmington]]", "[[Wilmington]]", "Wilmington", "Wilmington", "Wilmington", "Barletta", "Barletta"});

        lista.add(new String[]{"?", "?", "?", "?", "?", "", "?"}); //punto interrogativo AMMESSO. Viene restituito al server.
        lista.add(new String[]{"", "", "", "", "", "", ""});

        return lista;
    }// end of single test


    /**
     * Lista di possibili valori per il parametro <br>
     * Nell'ordine:
     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' <br>
     * 2) valore elaborato valido (minuscole, quadre, ecc.), restituibile al server non necessariamente registrabile sul mongoDB <br>
     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
     * 5) eventuale testo preesistente sul mongoDB <br>
     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>
     */
    private List<String[]> getListaParametroGiornoMeseNascita() {
        List<String[]> lista = new ArrayList<>();

        lista.add(new String[]{"[[1 Settembre]]", "[[1 Settembre]]", "1º settembre", "1º settembre", "1º settembre", "", "1º settembre"});
        lista.add(new String[]{"[[1 Brumaio]]", "[[1 Brumaio]]", "1º brumaio", "", "1º brumaio", "", "[[1 Brumaio]]"});

        lista.add(new String[]{"[[12ottobre]]", "[[12ottobre]]", "12 ottobre", "12 ottobre", "12 ottobre", "", "12 ottobre"});
        lista.add(new String[]{"[[12   ottobre]]", "[[12   ottobre]]", "12 ottobre", "12 ottobre", "12 ottobre", "", "12 ottobre"});
        lista.add(new String[]{"[12  ottobre]", "[12  ottobre]", "12 ottobre", "12 ottobre", "12 ottobre", "", "12 ottobre"});
        lista.add(new String[]{"[[ 12 ottobre]]", "[[ 12 ottobre]]", "12 ottobre", "12 ottobre", "12 ottobre", "", "12 ottobre"});
        lista.add(new String[]{"[[12 ottobre ]]", "[[12 ottobre ]]", "12 ottobre", "12 ottobre", "12 ottobre", "", "12 ottobre"});
        lista.add(new String[]{"[[12 Ottobre]]", "[[12 Ottobre]]", "12 ottobre", "12 ottobre", "12 ottobre", "", "12 ottobre"});
        lista.add(new String[]{"[[1° Marzo]]", "[[1° Marzo]]", "1º marzo", "1º marzo", "1º marzo", "", "1º marzo"});

        lista.add(new String[]{"[[1 Brumaio]]<ref>Dal 2000</ref>", "[[1 Brumaio]]", "1º brumaio", "", "1º brumaio<ref>Dal 2000</ref>", "", "[[1 Brumaio]]<ref>Dal 2000</ref>"});

        lista.add(new String[]{"?", "?", "?", "?", "?", "", "?"}); //punto interrogativo AMMESSO. Viene restituito al server.
        lista.add(new String[]{"", "", "", "", "", "", ""});

        return lista;
    }// end of single test


    /**
     * Lista di possibili valori per il parametro <br>
     * Nell'ordine:
     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' <br>
     * 2) valore elaborato valido (minuscole, quadre, ecc.), restituibile al server non necessariamente registrabile sul mongoDB <br>
     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
     * 5) eventuale testo preesistente sul mongoDB <br>
     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>
     */
    private List<String[]> getListaParametroAnnoNascita() {
        List<String[]> lista = new ArrayList<>();

        lista.add(new String[]{"[[1981]]", "[[1981]]", "1981", "1981", "1981", "", "1981"});
        lista.add(new String[]{"[[1981]]?", "[[1981]]", "1981", "1981", "1981", "", "1981"});

        lista.add(new String[]{"?", "?", "?", "?", "?", "", "?"}); //punto interrogativo AMMESSO. Viene restituito al server.
        lista.add(new String[]{"", "", "", "", "", "", ""});

        return lista;
    }// end of single test


    /**
     * Lista di possibili valori per il parametro <br>
     * Nell'ordine:
     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' <br>
     * 2) valore elaborato valido (minuscole, quadre, ecc.), restituibile al server non necessariamente registrabile sul mongoDB <br>
     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
     * 5) eventuale testo preesistente sul mongoDB <br>
     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>
     */
    private List<String[]> getListaParametroLuogoMorte() {
        List<String[]> lista = new ArrayList<>();

        lista.add(new String[]{"wilmington", "wilmington", "Wilmington", "Wilmington", "Wilmington", "", "Wilmington"});
        lista.add(new String[]{"[[Wilmington]]", "[[Wilmington]]", "Wilmington", "Wilmington", "Wilmington", "", "Wilmington"});
        lista.add(new String[]{"[Wilmington]", "[Wilmington]", "Wilmington", "Wilmington", "Wilmington", "", "Wilmington"});
        lista.add(new String[]{"[[Wilmington]]{{forse}}", "[[Wilmington]]", "Wilmington", "Wilmington", "Wilmington{{forse}}", "", "Wilmington{{forse}}"});
        lista.add(new String[]{"[[Wilmington]]?", "[[Wilmington]]", "Wilmington", "Wilmington", "Wilmington", "", "Wilmington"});

        lista.add(new String[]{"[[Wilmington]]<ref>Pippoz</ref>", "[[Wilmington]]", "Wilmington", "Wilmington", "Wilmington<ref>Pippoz</ref>", "", "Wilmington<ref>Pippoz</ref>"});
        lista.add(new String[]{"[[Wilmington]]<!--Prova-->", "[[Wilmington]]", "Wilmington", "Wilmington", "Wilmington<!--Prova-->", "", "Wilmington<!--Prova-->"});
        lista.add(new String[]{"[[Wilmington]]{{forse}}", "[[Wilmington]]", "Wilmington", "Wilmington", "Wilmington{{forse}}", "", "Wilmington{{forse}}"});
        lista.add(new String[]{"Wilmington=Giovanni", "Wilmington", "Wilmington", "Wilmington", "Wilmington=Giovanni", "", "Wilmington=Giovanni"});
        lista.add(new String[]{"Wilmington?", "Wilmington", "Wilmington", "Wilmington", "Wilmington", "", "Wilmington"});

        lista.add(new String[]{"[[Wilmington]]", "[[Wilmington]]", "Wilmington", "Wilmington", "Wilmington", "Barletta", "Barletta"});

        lista.add(new String[]{"?", "?", "?", "?", "?", "", "?"}); //punto interrogativo AMMESSO. Viene restituito al server.
        lista.add(new String[]{"", "", "", "", "", "", ""});

        return lista;
    }// end of single test


    /**
     * Lista di possibili valori per il parametro <br>
     * Nell'ordine:
     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' <br>
     * 2) valore elaborato valido (minuscole, quadre, ecc.), restituibile al server non necessariamente registrabile sul mongoDB <br>
     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
     * 5) eventuale testo preesistente sul mongoDB <br>
     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>
     */
    private List<String[]> getListaParametroGiornoMeseMorte() {
        List<String[]> lista = new ArrayList<>();

        lista.add(new String[]{"[[1 Settembre]]", "[[1 Settembre]]", "1º settembre", "1º settembre", "1º settembre", "", "1º settembre"});
        lista.add(new String[]{"[[1 Brumaio]]", "[[1 Brumaio]]", "1º brumaio", "", "1º brumaio", "", "[[1 Brumaio]]"});

        lista.add(new String[]{"[[12ottobre]]<ref>Forse</ref>", "[[12ottobre]]", "12 ottobre", "12 ottobre", "12 ottobre<ref>Forse</ref>", "", "12 ottobre<ref>Forse</ref>"});
        lista.add(new String[]{"[[12   ottobre]]", "[[12   ottobre]]", "12 ottobre", "12 ottobre", "12 ottobre", "", "12 ottobre"});
        lista.add(new String[]{"[12  ottobre]", "[12  ottobre]", "12 ottobre", "12 ottobre", "12 ottobre", "", "12 ottobre"});
        lista.add(new String[]{"[[ 12 ottobre]]", "[[ 12 ottobre]]", "12 ottobre", "12 ottobre", "12 ottobre", "", "12 ottobre"});
        lista.add(new String[]{"[[12 ottobre ]]", "[[12 ottobre ]]", "12 ottobre", "12 ottobre", "12 ottobre", "", "12 ottobre"});
        lista.add(new String[]{"[[12 Ottobre]]", "[[12 Ottobre]]", "12 ottobre", "12 ottobre", "12 ottobre", "", "12 ottobre"});
        lista.add(new String[]{"[[1° Marzo]]", "[[1° Marzo]]", "1º marzo", "1º marzo", "1º marzo", "", "1º marzo"});

        lista.add(new String[]{"[[1 Brumaio]]<ref>Dal 2000</ref>", "[[1 Brumaio]]", "1º brumaio", "", "1º brumaio<ref>Dal 2000</ref>", "", "[[1 Brumaio]]<ref>Dal 2000</ref>"});

        lista.add(new String[]{"?", "?", "?", "?", "?", "", "?"}); //punto interrogativo AMMESSO. Viene restituito al server.
        lista.add(new String[]{"", "", "", "", "", "", ""});

        return lista;
    }// end of single test


    /**
     * Lista di possibili valori per il parametro <br>
     * Nell'ordine:
     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' <br>
     * 2) valore elaborato valido (minuscole, quadre, ecc.), restituibile al server non necessariamente registrabile sul mongoDB <br>
     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
     * 5) eventuale testo preesistente sul mongoDB <br>
     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>
     */
    private List<String[]> getListaParametroAnnoMorte() {
        List<String[]> lista = new ArrayList<>();

        lista.add(new String[]{"[[1981]]", "[[1981]]", "1981", "1981", "1981", "", "1981"});
        lista.add(new String[]{"[[1981]]?", "[[1981]]", "1981", "1981", "1981", "", "1981"});

        lista.add(new String[]{"?", "?", "?", "?", "?", "", "?"}); //punto interrogativo AMMESSO. Viene restituito al server.
        lista.add(new String[]{"", "", "", "", "", "", ""});

        return lista;
    }// end of single test


    /**
     * Lista di possibili valori per il parametro <br>
     * Nell'ordine:
     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' <br>
     * 2) valore elaborato valido (minuscole, quadre, ecc.), restituibile al server non necessariamente registrabile sul mongoDB <br>
     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
     * 5) eventuale testo preesistente sul mongoDB <br>
     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>
     */
    private List<String[]> getListaParametroAttivita() {
        List<String[]> lista = new ArrayList<>();

        lista.add(new String[]{"[[Modella]]", "[[Modella]]", "modella", "modella", "modella", "", "modella"});
        lista.add(new String[]{"Modella<ref>Dal 2000</ref>", "Modella", "modella", "modella", "modella<ref>Dal 2000</ref>", "", "modella<ref>Dal 2000</ref>"});
        lista.add(new String[]{"Paninaro<ref>Dal 2000</ref>", "Paninaro", "paninaro", "", "paninaro<ref>Dal 2000</ref>", "", "Paninaro<ref>Dal 2000</ref>"});
        lista.add(new String[]{"Paninaro<ref>Dal 2000</ref>", "Paninaro", "paninaro", "", "paninaro<ref>Dal 2000</ref>", "attore", "attore<ref>Dal 2000</ref>"});
        lista.add(new String[]{"Pittore ?", "Pittore", "pittore", "pittore", "pittore", "", "pittore"});

        lista.add(new String[]{"?", "", "", "", "", "", ""}); //punto interrogativo NON ammesso. Viene cancellato dal server.
        lista.add(new String[]{"", "", "", "", "", "", ""});

        return lista;
    }// end of single test


    /**
     * Lista di possibili valori per il parametro <br>
     * Nell'ordine:
     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' <br>
     * 2) valore elaborato valido (minuscole, quadre, ecc.), restituibile al server non necessariamente registrabile sul mongoDB <br>
     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
     * 5) eventuale testo preesistente sul mongoDB <br>
     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>
     */
    private List<String[]> getListaParametroNazionalita() {
        List<String[]> lista = new ArrayList<>();

        lista.add(new String[]{"[[Statunitense]]", "[[Statunitense]]", "statunitense", "statunitense", "statunitense", "", "statunitense"});
        lista.add(new String[]{"[[Sarmatese]]{{forse}}", "[[Sarmatese]]", "sarmatese", "", "sarmatese{{forse}}", "", "[[Sarmatese]]{{forse}}"});
        lista.add(new String[]{"[[Sarmatese]]{{forse}}", "[[Sarmatese]]", "sarmatese", "", "sarmatese{{forse}}", "polacca", "polacca{{forse}}"});

        lista.add(new String[]{"?", "", "", "", "", "", ""}); //punto interrogativo NON ammesso. Viene cancellato dal server.
        lista.add(new String[]{"", "", "", "", "", "", ""});

        return lista;
    }// end of single test


    /**
     * Restituisce un valore grezzo troncato dopo alcuni tag chiave <br>
     * <p>
     * ELIMINA gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Restituisce un valore GREZZO che deve essere ancora elaborato <br>
     * Eventuali parti terminali inutili vengono scartate ma devono essere conservate a parte per il template <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     *
     * @param valorePropertyTmplBioServer testo originale proveniente dalla property tmplBioServer della entity Bio
     *
     * @return valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) <br>
     */
    @Test
    public void estraeValoreInizialeGrezzo111() {
        ParBio parBio;
        String testoOriginale = VUOTA;
        List<String[]> lista;

//     * 0) type del parametro (enumeration) <br>
//     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
//     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' risultante <br>
//     * 2) valore elaborato valido (minuscole, quadre, ecc.), restituibile al server non necessariamente registrabile sul mongoDB <br>
//     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
//     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
//     * 5) eventuale testo preesistente sul mongoDB <br>
//     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>

        System.out.println("");
        System.out.println("******************");
        System.out.println("1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' risultante");
        System.out.println("******************");
        for (Object[] riga : getAll()) {
            if (riga.length == 2) {
                parBio = (ParBio) riga[0];
                lista = (List<String[]>) riga[1];

                if (lista != null && lista.size() > 0) {
                    System.out.println("");
                    System.out.println("Parametro " + parBio.getTag().toLowerCase());
                    for (String[] valori : lista) {
                        testoOriginale = valori[0];
                        previsto = valori[1];
                        ottenuto = parBio.estraeValoreInizialeGrezzo(testoOriginale);
                        Assert.assertEquals(previsto, ottenuto);
                        System.out.println("Testo originale: " + testoOriginale + " Valore troncato: " + ottenuto);
                    }// end of for cycle
                }// end of if cycle
            }// end of if cycle

        }// end of for cycle
    }// end of single test


    /**
     * Restituisce un valore valido troncato dopo alcuni tag chiave ed elaborato <br>
     * <p>
     * ELIMINA gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Elabora il valore grezzo (minuscole, quadre, ecc.), che diventa valido e restituibile al server <br>
     * NON controlla la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
     * Può essere sottoscritto da alcuni parametri che rispondono in modo particolare <br>
     *
     * @param valorePropertyTmplBioServer testo originale proveniente dalla property tmplBioServer della entity Bio
     *
     * @return valore valido troncato ed elaborato dopo alcuni tag chiave (<ref>, {{, ecc.) <br>
     */
    @Test
    public void regolaValoreInizialeValido222() {
        ParBio parBio;
        String testoOriginale = VUOTA;
        List<String[]> lista;

//     * 0) type del parametro (enumeration) <br>
//     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
//     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' risultante <br>
//     * 2) valore elaborato valido (minuscole, quadre, ecc.), restituibile al server non necessariamente registrabile sul mongoDB <br>
//     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
//     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
//     * 5) eventuale testo preesistente sul mongoDB <br>
//     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>

        System.out.println("");
        System.out.println("******************");
        System.out.println("2) valore elaborato valido (minuscole, quadre, ecc.), restituibile al server non necessariamente registrabile sul mongoDB");
        System.out.println("******************");
        for (Object[] riga : getAll()) {
            if (riga.length == 2) {
                parBio = (ParBio) riga[0];
                lista = (List<String[]>) riga[1];

                if (lista != null && lista.size() > 0) {
                    System.out.println("");
                    System.out.println("Parametro " + parBio.getTag().toLowerCase());
                    for (String[] valori : lista) {
                        testoOriginale = valori[0];
                        previsto = valori[2];
                        ottenuto = parBio.regolaValoreInizialeValido(testoOriginale);
                        Assert.assertEquals(previsto, ottenuto);
                        System.out.println("Testo originale: " + testoOriginale + " Valore valido: " + ottenuto);
                    }// end of for cycle
                }// end of if cycle
            }// end of if cycle

        }// end of for cycle
    }// end of single test


    /**
     * Restituisce un valore valido del parametro <br>
     * <p>
     * ELIMINA gli eventuali contenuti IN CODA che non devono essere presi in considerazione <br>
     * Elabora il valore grezzo (minuscole, quadre, ecc.)
     * CONTROLLA la corrispondenza dei parametri linkati (Giorno, Anno, Attivita, Nazionalita) <br>
     * Se manca la corrispondenza, restituisce VUOTA <br>
     * La differenza con estraeValoreInizialeValido() riguarda solo i parametri Giorno, Anno, Attivita, Nazionalita <br>
     *
     * @param valorePropertyTmplBioServer testo originale proveniente dalla property tmplBioServer della entity Bio
     *
     * @return valore valido del parametro
     */
    @Test
    public void estraeValoreParametro333() {
        ParBio parBio;
        String testoOriginale = VUOTA;
        List<String[]> lista;

//     * 0) type del parametro (enumeration) <br>
//     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
//     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' risultante <br>
//     * 2) valore elaborato valido (minuscole, quadre, ecc.), restituibile al server non necessariamente registrabile sul mongoDB <br>
//     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
//     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
//     * 5) eventuale testo preesistente sul mongoDB <br>
//     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>

        System.out.println("");
        System.out.println("******************");
        System.out.println("3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità)");
        System.out.println("******************");
        for (Object[] riga : getAll()) {
            if (riga.length == 2) {
                parBio = (ParBio) riga[0];
                lista = (List<String[]>) riga[1];

                if (lista != null && lista.size() > 0) {
                    System.out.println("");
                    System.out.println("Parametro " + parBio.getTag().toLowerCase());
                    for (String[] valori : lista) {
                        testoOriginale = valori[0];
                        previsto = valori[3];
                        ottenuto = parBio.estraeValoreParametro(testoOriginale);
                        Assert.assertEquals(previsto, ottenuto);
                        System.out.println("Testo originale: " + testoOriginale + " Parametro valido: " + ottenuto);
                    }// end of for cycle
                }// end of if cycle
            }// end of if cycle

        }// end of for cycle
    }// end of single test


    /**
     * Restituisce un valore finale per upload del valore valido elaborato e con la 'coda' <br>
     * <p>
     * MANTIENE gli eventuali contenuti IN CODA che vengono reinseriti dopo aver elaborato il valore valido del parametro <br>
     * Usato per Upload sul server
     *
     * @param valorePropertyTmplBioServer testo originale proveniente dalla property tmplBioServer della entity Bio
     *
     * @return valore finale completo col valore valido elaborato e la 'coda' dalla property di tmplBioServer
     */
    @Test
    public void elaboraParteValida444() {
        ParBio parBio;
        String testoOriginale = VUOTA;
        List<String[]> lista;

//     * 0) type del parametro (enumeration) <br>
//     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
//     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' risultante <br>
//     * 2) valore elaborato valido (minuscole, quadre, ecc.), restituibile al server non necessariamente registrabile sul mongoDB <br>
//     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
//     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
//     * 5) eventuale testo preesistente sul mongoDB <br>
//     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>

        System.out.println("");
        System.out.println("******************");
        System.out.println("4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati");
        System.out.println("******************");
        for (Object[] riga : getAll()) {
            if (riga.length == 2) {
                parBio = (ParBio) riga[0];
                lista = (List<String[]>) riga[1];

                if (lista != null && lista.size() > 0) {
                    System.out.println("");
                    System.out.println("Parametro " + parBio.getTag().toLowerCase());
                    for (String[] valori : lista) {
                        testoOriginale = valori[0];
                        previsto = valori[4];
                        ottenuto = parBio.elaboraParteValida(testoOriginale);
                        Assert.assertEquals(previsto, ottenuto);
                        System.out.println("Testo originale: " + testoOriginale + " Valore elaborato: " + ottenuto);
                    }// end of for cycle
                }// end of if cycle
            }// end of if cycle

        }// end of for cycle
    }// end of single test


    /**
     * Restituisce un valore finale per upload merged del parametro mongoDB e con la 'coda' <br>
     * <p>
     * Elabora un valore valido del parametro, utilizzando quello del mongoDB <br>
     * MANTIENE gli eventuali contenuti IN CODA che vengono reinseriti dopo aver elaborato il valore valido del parametro <br>
     * Usato per Upload sul server
     *
     * @param valorePropertyTmplBioServer testo originale proveniente dalla property tmplBioServer della entity Bio
     * @param valoreMongoDB               da sostituire al posto del valore valido dalla property di tmplBioServer
     *
     * @return valore finale completo col parametro mongoDB e la 'coda' dalla property di tmplBioServer
     */
    @Test
    public void sostituisceParteValida666() {
        ParBio parBio;
        String testoOriginale = VUOTA;
        String valoreMongoDB = VUOTA;
        List<String[]> lista;

//     * 0) type del parametro (enumeration) <br>
//     * 0) testo originale proveniente dalla property tmplBioServer della entity Bio <br>
//     * 1) valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' risultante <br>
//     * 2) valore elaborato valido (minuscole, quadre, ecc.), restituibile al server non necessariamente registrabile sul mongoDB <br>
//     * 3) valore del parametro valido per la registrazione sul mongoDB (esiste Giorno, Anno, Attività, Nazionalità) <br>
//     * 4) valore restituito per upload con la sola parte valida elaborata e la 'coda' mantenuta senza perdita di dati <br>
//     * 5) eventuale testo preesistente sul mongoDB <br>
//     * 6) valore finale per upload merged del mongoDB con la 'coda' <br>
        System.out.println("");
        System.out.println("******************");
        System.out.println("6) valore finale per upload merged del mongoDB con la 'coda'");
        System.out.println("******************");
        for (Object[] riga : getAll()) {
            if (riga.length == 2) {
                parBio = (ParBio) riga[0];
                lista = (List<String[]>) riga[1];

                if (lista != null && lista.size() > 0) {
                    System.out.println("");
                    System.out.println("Parametro " + parBio.getTag().toLowerCase());
                    for (String[] valori : lista) {
                        testoOriginale = valori[0];
                        valoreMongoDB = valori[5];
                        previsto = valori[6];
                        ottenuto = parBio.sostituisceParteValida(testoOriginale, valoreMongoDB);
                        Assert.assertEquals(previsto, ottenuto);
                        System.out.println("Testo originale: " + testoOriginale + " Valore del mongoDB: " + valoreMongoDB + " Valore per upload: " + ottenuto);
                    }// end of for cycle
                }// end of if cycle
            }// end of if cycle

        }// end of for cycle
    }// end of single test


    /**
     * Estrae una mappa chiave-valore per un fix di parametri, dal testo di una biografia <br>
     * <p>
     * E impossibile sperare in uno schema fisso
     * I parametri sono spesso scritti in ordine diverso da quello previsto
     * Occorre considerare le {{ graffe annidate, i | (pipe) annidati
     * i mancati ritorni a capo, ecc., ecc.
     * <p>
     * Uso la lista dei parametri che può riconoscere
     * (è meno flessibile, ma più sicuro)
     * Cerco il primo parametro nel testo e poi spazzolo il testo per cercare
     * il primo parametro noto e così via
     *
     * @param valorePropertyTmplBioServer del template Bio
     *
     * @return mappa dei parametri esistenti nella enumeration e presenti nel testo
     */
    @Test
    public void getMappaDownload() {
        HashMap<String, String> mappa;
        Bio entity = getBio();

        mappa = libBio.getMappaDownload(entity);

        System.out.println("*************");
        System.out.println("getMappaDownload");
        System.out.println("*************");
        System.out.println("");

        Assert.assertNotNull(mappa);
//        Assert.assertEquals(mappa.get(ParBio.nome.getTag()), "Rudolf");
//        Assert.assertEquals(mappa.get(ParBio.cognome.getTag()), "Kolisch");
//        Assert.assertEquals(mappa.get(ParBio.sesso.getTag()), "M");
//        Assert.assertEquals(mappa.get(ParBio.luogoNascita.getTag()), "Klamm am Semmering");
//        Assert.assertEquals(mappa.get(ParBio.giornoMeseNascita.getTag()), "20 luglio");
//        Assert.assertEquals(mappa.get(ParBio.annoNascita.getTag()), "1896");
//        Assert.assertEquals(mappa.get(ParBio.luogoMorte.getTag()), "Watertown");
//        Assert.assertEquals(mappa.get(ParBio.giornoMeseMorte.getTag()), "1º agosto");
//        Assert.assertEquals(mappa.get(ParBio.annoMorte.getTag()), "1978");
//        Assert.assertEquals(mappa.get(ParBio.attivita.getTag()), "Violinista");
//        Assert.assertEquals(mappa.get(ParBio.nazionalita.getTag()), "austriaco");

        stampaParametriMongo(entity);
    }// end of single test


    /**
     * Mappa chiave-valore con i valori 'troncati' <br>
     * Valore grezzo troncato dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' risultante <br>
     *
     * @param mappaDownload coi valori originali provenienti dalla property tmplBioServer della entity Bio
     *
     * @return mappa con i valori 'troncati'
     */
    @Test
    public void getMappaTroncata() {
        LinkedHashMap<String, String> mappa = null;
        Bio entity = getBio();

        mappa = libBio.getMappaDownload(entity);
        mappa = libBio.getMappaTroncata(mappa);
        System.out.println("*************");
        System.out.println("getMappaTroncata");
        System.out.println("*************");
        System.out.println("");

        Assert.assertNotNull(mappa);
        Assert.assertEquals(mappa.get(ParBio.nome.getTag()), "Rudolf");
        Assert.assertEquals(mappa.get(ParBio.cognome.getTag()), "Kolisch");
        Assert.assertEquals(mappa.get(ParBio.sesso.getTag()), "M");
        Assert.assertEquals(mappa.get(ParBio.luogoNascita.getTag()), "Klamm am Semmering");
        Assert.assertEquals(mappa.get(ParBio.giornoMeseNascita.getTag()), "20 luglio");
        Assert.assertEquals(mappa.get(ParBio.annoNascita.getTag()), "1896");
        Assert.assertEquals(mappa.get(ParBio.luogoMorte.getTag()), "Watertown");
        Assert.assertEquals(mappa.get(ParBio.giornoMeseMorte.getTag()), "1º agosto");
        Assert.assertEquals(mappa.get(ParBio.annoMorte.getTag()), "1978");
        Assert.assertEquals(mappa.get(ParBio.attivita.getTag()), "Violinista");
        Assert.assertEquals(mappa.get(ParBio.nazionalita.getTag()), "austriaco");

        stampaParametriMongo(entity);
    }// end of single test


    /**
     * Mappa chiave-valore con i valori 'elaborati' <br>
     * Valore elaborato valido (minuscole, quadre, ecc.) <br>
     *
     * @param mappaTroncata dopo alcuni tag chiave (<ref>, {{, ecc.) e senza la 'coda' risultante
     *
     * @return mappa con i valori 'elaborati'
     */
    @Test
    public void getMappaElaborata() {
        LinkedHashMap<String, String> mappa = null;
        Bio entity = getBio();

        mappa = libBio.getMappaDownload(entity);
        mappa = libBio.getMappaTroncata(mappa);
        mappa = libBio.getMappaElaborata(mappa);
        System.out.println("*************");
        System.out.println("getMappaElaborata");
        System.out.println("*************");
        System.out.println("");

        Assert.assertNotNull(mappa);
        Assert.assertEquals(mappa.get(ParBio.nome.getTag()), "Rudolf");
        Assert.assertEquals(mappa.get(ParBio.cognome.getTag()), "Kolisch");
        Assert.assertEquals(mappa.get(ParBio.sesso.getTag()), "M");
        Assert.assertEquals(mappa.get(ParBio.luogoNascita.getTag()), "Klamm am Semmering");
        Assert.assertEquals(mappa.get(ParBio.giornoMeseNascita.getTag()), "20 luglio");
        Assert.assertEquals(mappa.get(ParBio.annoNascita.getTag()), "1896");
        Assert.assertEquals(mappa.get(ParBio.luogoMorte.getTag()), "Watertown");
        Assert.assertEquals(mappa.get(ParBio.giornoMeseMorte.getTag()), "1º agosto");
        Assert.assertEquals(mappa.get(ParBio.annoMorte.getTag()), "1978");
        Assert.assertEquals(mappa.get(ParBio.attivita.getTag()), "violinista");
        Assert.assertEquals(mappa.get(ParBio.nazionalita.getTag()), "austriaco");

        stampaParametriMongo(entity);
    }// end of single test


    /**
     * Mappa chiave-valore con i valori 'validi' <br>
     * Valore elaborato valido (minuscole, quadre, ecc.) <br>
     *
     * @param mappaElaborata con i valori validi (minuscole, quadre, ecc.)
     *
     * @return mappa con i valori 'validi'
     */
    @Test
    public void getMappaValida() {
    }// end of single test


    /**
     * Elabora la singola voce biografica <br>
     * Estrae dal tmplBioServer i singoli parametri previsti nella enumeration ParBio <br>
     * Ogni parametro viene 'pulito' se presentato in maniera 'impropria' <br>
     * Quello che resta è affidabile ed utilizzabile per le liste <br>
     */
//    @Test
    public void esegue() {
        String tmplBioServer = VUOTA;
        HashMap<String, String> mappa;
        Bio entity = null;

        tmplBioServer = BIO_DUE;
        entity = bioService.newEntity(123456789, "NonRilevante", tmplBioServer);
        entity = elaboraService.esegueNoSave(entity);

        System.out.println("*************");
        System.out.println("tmplBioServer");
        System.out.println("*************");
        System.out.println(tmplBioServer);
        System.out.println("");

        Assert.assertEquals(entity.nome, "Crystle Danae");
        Assert.assertEquals(entity.cognome, "Stewart");
        Assert.assertEquals(entity.sesso, "F");
        Assert.assertEquals(entity.luogoNato, "Wilmington");
        Assert.assertEquals(entity.giornoNascita.titolo, "1º settembre");
        Assert.assertEquals(entity.annoNascita.titolo, "1981");
        Assert.assertEquals(entity.attivita.singolare, "modella");
        Assert.assertEquals(entity.nazionalita.singolare, "statunitense");

        stampaParametriMongo(entity);
    }// end of single test


    @Test
    public void provaPaginaStrana() {
        LinkedHashMap<String, String> mappa = null;
        Bio entity = getBio(TITOLO_DUE);

        mappa = libBio.getMappaDownload(entity);
//        mappa = libBio.getMappaTroncata(mappa);
//        mappa = libBio.getMappaElaborata(mappa);

        Assert.assertNotNull(mappa);
        Assert.assertEquals("Antonio", mappa.get(ParBio.nome.getTag()));
        Assert.assertEquals("Guerra", mappa.get(ParBio.cognome.getTag()));
        Assert.assertEquals("M", mappa.get(ParBio.sesso.getTag()));
        Assert.assertEquals("Afragola", mappa.get(ParBio.luogoNascita.getTag()));
        Assert.assertEquals("24 marzo", mappa.get(ParBio.giornoMeseNascita.getTag()));
        Assert.assertEquals("1824", mappa.get(ParBio.annoNascita.getTag()));
        Assert.assertEquals("Afragola", mappa.get(ParBio.luogoMorte.getTag()));
        Assert.assertEquals("20 maggio", mappa.get(ParBio.giornoMeseMorte.getTag()));
        Assert.assertEquals("1890", mappa.get(ParBio.annoMorte.getTag()));
        Assert.assertEquals("politico", mappa.get(ParBio.attivita.getTag()));
        Assert.assertEquals("italiano", mappa.get(ParBio.nazionalita.getTag()));

        stampaMappa(entity.tmplBioServer, mappa);

        String noteMorte = "<ref name=\"LAF\">{{cita notizia|autore=Domenico Corcione|url=http://www.lafragolanapoli.it/giornale/afragola-sconosciuta-le-lapidi-cittadine/|titolo=Afragola sconosciuta: le lapidi cittadine|giornale=La Fragola Napoli|data=27 marzo 2014}}</ref>";
        Assert.assertEquals(VUOTA, mappa.get(ParBio.titolo.getTag()));
        Assert.assertEquals(noteMorte, mappa.get(ParBio.noteMorte.getTag()));
    }// end of single test


    @Test
    public void provaPaginaStrana2() {
        LinkedHashMap<String, String> mappa = null;
//        Bio entity = getBio(TITOLO_TRE); //@todo QUI  SI  FERMA

//        mappa = libBio.getMappaDownload(entity);
//        mappa = libBio.getMappaTroncata(mappa);
//        mappa = libBio.getMappaElaborata(mappa);
    }// end of single test


    private void stampaParametriMongo(Bio entity) {
        System.out.println("");
        System.out.println("*************");
        System.out.println("stampa tutti i parametri" + " - sono " + ParBio.values().length);
        System.out.println("*************");

        for (ParBio par : ParBio.values()) {
            System.out.println(par.getTag() + ": " + par.getValue(entity));
        }// end of for cycle

        System.out.println("");
        System.out.println("*************");
        System.out.println("stampa i parametri presenti");
        System.out.println("*************");

        for (ParBio par : ParBio.values()) {
            if (text.isValid(par.getValue(entity))) {
                System.out.println(par.getTag() + ": " + par.getValue(entity));
            }// end of if cycle
        }// end of for cycle
    }// end of method


    private void stampaMappa(String tmplBioServer, LinkedHashMap<String, String> mappa) {
        System.out.println("");
        System.out.println("*************");
        System.out.println("stampa mappa" + " - sono " + mappa.size());
        System.out.println("*************");

        System.out.println("Template:");
        System.out.println(tmplBioServer);
        System.out.println("");

        for (String key : mappa.keySet()) {
            System.out.println(key + ": " + mappa.get(key));
        }// end of for cycle
    }// end of method

}// end of test class
