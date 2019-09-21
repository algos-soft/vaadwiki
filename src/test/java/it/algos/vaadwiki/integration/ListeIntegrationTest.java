package it.algos.vaadwiki.integration;

import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadflow.modules.anno.AnnoService;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadflow.modules.giorno.GiornoService;
import it.algos.vaadwiki.ATest;
import it.algos.vaadwiki.didascalia.EADidascalia;
import it.algos.vaadwiki.didascalia.WrapDidascalia;
import it.algos.vaadwiki.liste.*;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.bio.BioService;
import it.algos.vaadwiki.modules.nome.Nome;
import it.algos.vaadwiki.modules.nome.NomeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 15-set-2019
 * Time: 08:19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ListeIntegrationTest extends ATest {

    public LinkedHashMap<String, ArrayList<String>> mappaSemplice;

    public LinkedHashMap<String, LinkedHashMap<String, ArrayList<String>>> mappaComplessa;

    protected List<Bio> listaBio;

    protected List<WrapDidascalia> listaWrap;

    protected List<String> listaDidascalieText;

    @Autowired
    protected BioService bioService;

    @Autowired
    protected NomeService nomeService;

    @Autowired
    protected GiornoService giornoService;

    @Autowired
    protected AnnoService annoService;

    @Autowired
    protected ListaService listaService;

    protected Anno annoEntity;

    protected Giorno giornoEntity;

    protected Nome nomeEntity;

    protected Lista listaNome;

    protected ListaGiornoNato listaGiorno;

    protected ListaAnnoNato listaAnno;

    protected String testo;

    @Autowired
    private ApplicationContext appContext;

    private String annoText = "2005";

    private String giornoText = "3 marzo";

    private String nomeText = "Violeta";

    private int posIni = 35;

    private int posEnd = 45;


    @Before
    public void setUpIniziale() {
        Assert.assertNotNull(text);
        Assert.assertNotNull(giornoService);
        Assert.assertNotNull(nomeService);
    }// end of method


    public void testUnico() {
        annoEntity = annoService.findByKeyUnica(annoText);
        Assert.assertNotNull(annoEntity);
        listaAnno = appContext.getBean(ListaAnnoNato.class, annoEntity);
        Assert.assertNotNull(listaAnno);

        giornoEntity = giornoService.findByKeyUnica(giornoText);
        Assert.assertNotNull(giornoEntity);
        listaGiorno = appContext.getBean(ListaGiornoNato.class, giornoEntity);
        Assert.assertNotNull(listaGiorno);

        nomeEntity = nomeService.findByKeyUnica(nomeText);
        Assert.assertNotNull(nomeEntity);
        listaNome = appContext.getBean(ListaNomi.class, nomeEntity);
        Assert.assertNotNull(listaNome);

//        anno();
//        giorno();
        nome();
    }// end of single test


    public void anno() {
        //--costruisco qui la mappa semplice perché listaAnno ha la preferenza usaSuddivisioneParagrafi=true
        //--se cambio la preferenza nel mongoDb, devo cambiare anche qui
        ArrayList<Bio> listaGrezzaBio = bioService.findAllByAnnoNascita(annoEntity);
        ArrayList<WrapDidascalia> listaDidascalie = listaService.creaListaDidascalie(listaGrezzaBio, EADidascalia.annoNato);
        mappaSemplice = listaService.creaMappa(listaDidascalie);
        //--end
        Assert.assertNotNull(mappaSemplice);
        mappaComplessa = listaAnno.mappaComplessa;
        Assert.assertNotNull(mappaComplessa);

        System.out.println("*************");
        System.out.println("Mappa semplice anno - Righe semplici (escluso per gli anni)");
        System.out.println("*************");
        testo = listaService.righeSemplici(mappaSemplice);
        System.out.println(testo);
        System.out.println("");

        System.out.println("*************");
        System.out.println("Mappa semplice anno - Righe raggruppate (opzione valida per gli anni)");
        System.out.println("*************");
        testo = listaService.righeRaggruppate(mappaSemplice);
        System.out.println(testo);
        System.out.println("");

        System.out.println("*************");
        System.out.println("Mappa complessa anno - Paragrafi e righe raggruppate (opzione valida per gli anni)");
        System.out.println("*************");
        testo = listaService.paragrafoConRigheRaggruppate(mappaComplessa);
        System.out.println(testo);
        System.out.println("");
    }// end of single test


    public void giorno() {
        mappaSemplice = listaGiorno.mappaSemplice;
        Assert.assertNotNull(mappaSemplice);
        mappaComplessa = listaGiorno.mappaComplessa;
        Assert.assertNotNull(mappaComplessa);

        System.out.println("*************");
        System.out.println("Mappa semplice giorno - Righe semplici (escluso per i giorni)");
        System.out.println("*************");
        testo = listaService.righeSemplici(mappaSemplice);
        System.out.println(testo);
        System.out.println("");

        System.out.println("*************");
        System.out.println("Mappa semplice giorno - Righe raggruppate (opzione valida per i giorni)");
        System.out.println("*************");
        testo = listaService.righeRaggruppate(mappaSemplice);
        System.out.println(testo);
        System.out.println("");

        System.out.println("*************");
        System.out.println("Mappa complessa giorno (solo righe raggruppate) - Paragrafo con righe raggruppate (opzione valida per i giorni)");
        System.out.println("*************");
        testo = listaService.righeParagrafo(mappaComplessa);
        System.out.println(testo);
        System.out.println("");
    }// end of single test


    @Test
    public void nome() {
        nomeEntity = nomeService.findByKeyUnica(nomeText);
        Assert.assertNotNull(nomeEntity);
        listaNome = appContext.getBean(ListaNomi.class, nomeEntity);
        Assert.assertNotNull(listaNome);

        //--costruisco qui la mappa semplice perché listaAnno ha la preferenza usaSuddivisioneParagrafi=true
        //--se cambio la preferenza nel mongoDb, devo cambiare anche qui
        ArrayList<Bio> listaGrezzaBio = bioService.findAllByNome(nomeText);
        ArrayList<WrapDidascalia> listaDidascalie = listaService.creaListaDidascalie(listaGrezzaBio, EADidascalia.listaNomi);
        mappaSemplice = listaService.creaMappa(listaDidascalie);
        //--end
        mappaComplessa = listaService.getMappaNome(nomeText);
        Assert.assertNotNull(mappaSemplice);
        Assert.assertNotNull(mappaComplessa);

        String key;
        List<String> value;
        int k = 0;

        System.out.println("*************");
        System.out.println("Controllo dimensioni");
        System.out.println("*************");
        previstoIntero = 10;
        ottenutoIntero = listaNome.size;
        Assert.assertEquals(previstoIntero, ottenutoIntero);
        System.out.println("Le biografie che hanno il nome " + nomeText + " sono " + ottenutoIntero);
        System.out.println("");

        System.out.println("*************");
        System.out.println("Mappa semplice - Righe semplici (escluso per i nomi)");
        System.out.println("*************");
        testo = listaService.righeSemplici(mappaSemplice);
        System.out.println(testo);
        System.out.println("");

        System.out.println("*************");
        System.out.println("Mappa semplice - Righe raggruppate (opzione poco probabile per i nomi)");
        System.out.println("*************");
        testo = listaService.righeRaggruppate(mappaSemplice);
        System.out.println(testo);
        System.out.println("");

        System.out.println("*************");
        System.out.println("Mappa complessa - Paragrafo con righe semplici (opzione valida per i nomi)");
        System.out.println("deprecated");
        System.out.println("*************");
        testo = listaService.righeParagrafo(mappaComplessa);
        System.out.println(testo);
        System.out.println("");

        System.out.println("*************");
        System.out.println("Mappa complessa - Paragrafo con righe semplici (altra ipotesi)");
        System.out.println("valido");
        System.out.println("*************");
        testo = listaService.paragrafoAttivita(mappaComplessa);
        System.out.println(testo);
        System.out.println("");


        System.out.println("*************");
        System.out.println("Mappa complessa - Paragrafo con righe semplici e sottopagina");
        System.out.println("valido");
        System.out.println("*************");
        testo = listaService.paragrafoSottopaginato(mappaComplessa, "Persone di nome Violeta", "Attrici", 2);
        System.out.println(testo);
        System.out.println("");

    }// end of single test

}// end of class
