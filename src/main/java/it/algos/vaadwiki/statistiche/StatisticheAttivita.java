package it.algos.vaadwiki.statistiche;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.modules.attivita.Attivita;
import it.algos.vaadwiki.modules.attivita.AttivitaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.List;

import static it.algos.vaadflow.application.FlowCost.A_CAPO;
import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.DURATA_UPLOAD_STATISTICHE_ATTIVITA;
import static it.algos.vaadwiki.application.WikiCost.LAST_UPLOAD_STATISTICHE_ATTIVITA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: mar, 01-ott-2019
 * Time: 07:57
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class StatisticheAttivita extends StatisticheAttNaz {


    private static String TITOLO_PAGINA_WIKI = "Progetto:Biografie/Attività";

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    private AttivitaService service;


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(StatisticheAttivita.class) <br>
     */
    public StatisticheAttivita() {
    }// end of Spring constructor



    /**
     * Preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        super.fixPreferenze();
        super.titoloPagina = TITOLO_PAGINA_WIKI;
        super.codeLastUpload = LAST_UPLOAD_STATISTICHE_ATTIVITA;
        super.codeDurataUpload = DURATA_UPLOAD_STATISTICHE_ATTIVITA;
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
     * Testo descrittivo prima della tabella tabella <br>
     */
    @Override
    protected String inizioParagrafo(int numBio) {
        String testo = VUOTA;

        testo += A_CAPO;
        testo += "==Attività usate==";
        testo += A_CAPO;
        testo += "'''";
        testo += listaPlurali.size();
        testo += "'''";
        testo += " attività";
        testo += "<ref>Le attività sono quelle [[Discussioni progetto:Biografie/Attività|'''convenzionalmente''' previste]] dalla comunità ed [[Modulo:Bio/Plurale attività|inserite nell' '''elenco''']] utilizzato dal [[template:Bio|template Bio]]</ref>";
        testo += " '''effettivamente utilizzate''' nelle  '''[[:Categoria:BioBot|" + text.format(numBio) + "]]'''";
        testo += "<ref>La '''differenza''' tra le voci della categoria e quelle utilizzate è dovuta allo specifico utilizzo del [[template:Bio|template Bio]] ed in particolare all'uso del parametro Categorie=NO</ref>";
        testo += " voci biografiche che usano il [[template:Bio|template Bio]] ed i parametri '''''attività'''''.";

        return testo;
    }// fine del metodo





    @Override
    protected String colonneTabella() {
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
        testo += "'''1° att'''";
        testo += A_CAPO;

        testo += color;
        testo += "'''2° att'''";
        testo += A_CAPO;

        testo += color;
        testo += "'''3° att'''";
        testo += A_CAPO;

        testo += color;
        testo += "'''totale'''";
        testo += A_CAPO;

        return testo;
    }// fine del metodo



    @Override
    protected String rigaTabella(String attivitaPlurale, int cont) {
        String testo = "";
        String tagDx = "style=\"text-align: right;\" |";
        String nome = attivitaPlurale.toLowerCase();
        String listaTag = "[[Progetto:Biografie/Attività/";
        String categoriaTag = "[[:Categoria:";
        String sepTag = "|";
        String endTag = "]]";
        String lista;
        String categoria;
        List<Attivita> listaAttivita = service.findAllByPlurale(attivitaPlurale);
        int numAttivitaUno = 0;
        int numAttivitaDue = 0;
        int numAttivitaTre = 0;
        int numAttivitaTotali = 0;

        lista = listaTag + text.primaMaiuscola(nome) + sepTag + nome + endTag;
        categoria = categoriaTag + nome + sepTag + nome + endTag;

        for (Attivita attivita : listaAttivita) {
            numAttivitaUno += bioService.countByAttivitaPrincipale(attivita);
            numAttivitaDue += bioService.countByAttivitaDue(attivita);
            numAttivitaTre += bioService.countByAttivitaTre(attivita);
            numAttivitaTotali += bioService.countByAttivitaTotali(attivita);
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
        testo += numAttivitaUno;

        testo += " || ";
        testo += tagDx;
        testo += numAttivitaDue;

        testo += " || ";
        testo += tagDx;
        testo += numAttivitaTre;

        testo += " || ";
        testo += tagDx;
        testo += numAttivitaTotali;

        testo += A_CAPO;

        return testo;
    }// fine del metodo


}// end of class
