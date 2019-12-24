package it.algos.vaadwiki.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.didascalia.WrapDidascalia;
import it.algos.vaadwiki.enumeration.EADidascalia;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.nazionalita.Nazionalita;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 23-dic-2019
 * Time: 06:37
 * Lista delle persone per nazionalità <br>
 * La lista è un semplice testo (formattato secondo i possibili tipi di raggruppamento) <br>
 * Creata con appContext.getBean(ListaNazionalita.class, nome) <br>
 * Punto di inzio @PostConstruct inizia() nella superclasse <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaNazionalita extends Lista {

    //--property
    protected Nazionalita nazionalita;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public ListaNazionalita() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(ListaNazionalita.class, nome) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     *
     * @param nazionalita di cui costruire la pagina sul server wiki
     */
    public ListaNazionalita(Nazionalita nazionalita) {
        this.nazionalita = nazionalita;
        super.soggetto = nazionalita.plurale;
    }// end of constructor


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.typeDidascalia = EADidascalia.listaNazionalita;
        super.usaSuddivisioneParagrafi = true;
        super.usaRigheRaggruppate = false;
        super.titoloParagrafoVuoto = pref.getStr(TAG_PARAGRAFO_VUOTO_NAZIONALITA);
        super.paragrafoVuotoInCoda = pref.isBool(IS_PARAGRAFO_VUOTO_NAZIONALITA_IN_CODA);
        super.usaLinkParagrafo = pref.isBool(USA_LINK_PARAGRAFO_NAZIONALITA);
        super.usaParagrafoSize = pref.isBool(USA_PARAGRAFO_SIZE_NAZIONALITA);
        super.usaSottopagine = pref.isBool(USA_SOTTOPAGINE_ATT_NAZ);
        super.taglioSottoPagina = pref.getInt(TAGLIO_SOTTOPAGINA_ATT_NAZ);
    }// end of method


    /**
     * Recupera una lista (array) di records Bio che usano questa istanza di Nome nella property nome
     * Sovrascritto nella sottoclasse concreta
     *
     * @return lista delle istanze di Bio che usano questo istanza nella property appropriata
     */
    @Override
    public List<Bio> listaBio() {
        List<Bio> listaBio = null;
        List<Nazionalita> listaNazionalita = nazionalitaService.findAllByPlurale(nazionalita.plurale);

        if (listaNazionalita != null && listaNazionalita.size() > 0) {
            listaBio = new ArrayList<>();
            for (Nazionalita nazionalita : listaNazionalita) {
                listaBio.addAll(bioService.findAllByNazionalita(nazionalita));
            }// end of for cycle
        }// end of if cycle

        return listaBio;
    }// fine del metodo


    /**
     * Ordina la lista di didascalie specifiche <br>
     */
    @SuppressWarnings("all")
    public ArrayList<WrapDidascalia> ordinaListaDidascalie(ArrayList<WrapDidascalia> listaDisordinata) {
        return listaService.ordinaListaDidascalieNomi(listaDisordinata);
    }// fine del metodo


}// end of class
