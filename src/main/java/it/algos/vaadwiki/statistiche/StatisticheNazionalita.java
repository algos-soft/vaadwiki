package it.algos.vaadwiki.statistiche;

import it.algos.vaadwiki.modules.attivita.Attivita;
import it.algos.vaadwiki.modules.attivita.AttivitaService;
import it.algos.vaadwiki.modules.nazionalita.Nazionalita;
import it.algos.vaadwiki.modules.nazionalita.NazionalitaService;
import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.List;

import static it.algos.vaadflow.application.FlowCost.A_CAPO;
import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 18-nov-2019
 * Time: 19:54
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class StatisticheNazionalita extends StatisticheAttNaz {

    private static String TITOLO_PAGINA_WIKI = "Progetto:Biografie/Nazionalità";

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private NazionalitaService service;


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(StatisticheNazionalita.class) <br>
     */
    public StatisticheNazionalita() {
    }// end of Spring constructor


    /**
     * Preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        super.fixPreferenze();
        super.titoloPagina = TITOLO_PAGINA_WIKI;
        super.codeLastUpload = LAST_UPLOAD_STATISTICHE_NAZIONALITA;
        super.codeDurataUpload = DURATA_UPLOAD_STATISTICHE_NAZIONALITA;
    }// fine del metodo


    /**
     * Costruisce la pagina <br>
     * Registra la pagina sul server wiki <br>
     */

    @Override
    protected void creaLista() {
        listaPlurali = service.findAllPlurali();
        listaPlurali = listaPlurali.subList(0, 4);
    }// end of method


    /**
     * Prima tabella <br>
     */
    protected String tabellaUsate(int numBio) {
        String testo = VUOTA;

        testo += A_CAPO;
        testo += "==Nazionalità usate==";
        testo += A_CAPO;
        testo += "'''";
        testo += listaPlurali.size();
        testo += "'''";
        testo += " nazionalità";
        testo += "<ref>Le nazionalità sono quelle [[Discussioni progetto:Biografie/Nazionalità|'''convenzionalmente''' previste]] dalla comunità ed [[Modulo:Bio/Plurale nazionalità|inserite nell' '''elenco''']] utilizzato dal [[template:Bio|template Bio]]</ref>";
        testo += " '''effettivamente utilizzate''' nelle  '''[[:Categoria:BioBot|" + text.format(numBio) + "]]'''";
        testo += "<ref>La '''differenza''' tra le voci della categoria e quelle utilizzate è dovuta allo specifico utilizzo del [[template:Bio|template Bio]] ed in particolare all'uso del parametro Categorie=NO</ref>";
        testo += " voci biografiche che usano il [[template:Bio|template Bio]] ed il parametro '''''nazionalità'''''.";

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
        testo += "'''lista";
        testo += "<ref>Nelle liste le biografie sono suddivise per attività rilevanti della persona. Se il numero di voci di un paragrafo diventa rilevante, vengono create delle sottopagine specifiche di quella attività. Le sottopagine sono suddivise a loro volta in paragrafi alfabetici secondo l'iniziale del cognome.</ref>'''";
        testo += A_CAPO;

        testo += color;
        testo += "'''categoria";
        testo += "<ref>Le categorie possono avere sottocategorie e suddivisioni diversamente articolate e possono avere anche voci che hanno implementato la categoria stessa al di fuori del [[template:Bio|template Bio]].</ref>'''";
        testo += A_CAPO;

        testo += color;
        testo += "'''voci'''";
        testo += A_CAPO;

        return testo;
    }// fine del metodo


    @Override
    protected String rigaPrimaTabella(String nazionalitaPlurale, int cont) {
        String testo = "";
        String tagDx = "style=\"text-align: right;\" |";
        String nome = nazionalitaPlurale.toLowerCase();
        String listaTag = "[[Progetto:Biografie/Nazionalità/";
        String categoriaTag = "[[:Categoria:";
        String sepTag = "|";
        String endTag = "]]";
        String lista;
        String categoria;
        List<Nazionalita> listaNazionalita = service.findAllByPlurale(nazionalitaPlurale);
        int numNazionalita = 0;

        lista = listaTag + text.primaMaiuscola(nome) + sepTag + nome + endTag;
        categoria = categoriaTag + nome + sepTag + nome + endTag;

        for (Nazionalita nazionalita : listaNazionalita) {
            numNazionalita += bioService.countByNazionalita(nazionalita);
        }// end of for cycle

        testo += "|-";
        testo += A_CAPO;
        testo += "|";

        testo += tagDx;
        testo += cont;

        testo += " || ";
        testo += lista;

        testo += " || ";
        testo += categoria;

        testo += " || ";
        testo += tagDx;
        testo += numNazionalita;

        testo += A_CAPO;

        return testo;
    }// fine del metodo


}// end of class
