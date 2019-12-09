package it.algos.vaadwiki.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.didascalia.EADidascalia;
import it.algos.vaadwiki.didascalia.WrapDidascalia;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.nome.Nome;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Fri, 07-Jun-2019
 * Time: 20:32
 * <p>
 * Lista delle persone per nome <br>
 * La lista è un semplice testo (formattato secondo i possibili tipi di raggruppamento) <br>
 * Creata con appContext.getBean(ListaNomi.class, nome) <br>
 * Punto di inzio @PostConstruct inizia() nella superclasse <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaNomi extends Lista{


    //--property
    protected Nome nome;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public ListaNomi() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(ListaNomi.class, nome) <br>
     * Non rimanda al costruttore della superclasse. Regola qui solo alcune property. <br>
     * La superclasse usa poi il metodo @PostConstruct inizia() per proseguire dopo l'init del costruttore <br>
     *
     * @param nome di cui costruire la pagina sul server wiki
     */
    public ListaNomi(Nome nome) {
        this.nome = nome;
    }// end of constructor


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.typeDidascalia = EADidascalia.listaNomi;
        super.usaSuddivisioneParagrafi = true;
        super.usaRigheRaggruppate = false;
        super.titoloParagrafoVuoto = pref.getStr(TAG_PARAGRAFO_VUOTO_NOMI_COGNOMI);
        super.paragrafoVuotoInCoda = pref.isBool(IS_PARAGRAFO_VUOTO_NOMI_IN_CODA);
        super.usaLinkParagrafo = pref.isBool(USA_LINK_PARAGRAFO_NOMI);
        super.usaParagrafoSize = pref.isBool(USA_PARAGRAFO_SIZE_NOMI);
        super.titoloSottoPaginaVuota = pref.getStr(TAG_SOTTOPAGINA_VUOTA_NOMI_COGNOMI);
        super.usaBodySottopagine = pref.isBool(USA_SOTTOPAGINE_NOMI_COGNOMI);
    }// end of method


    /**
     * Recupera una lista (array) di records Bio che usano questa istanza di Nome nella property nome
     * Sovrascritto nella sottoclasse concreta
     *
     * @return lista delle istanze di Bio che usano questo istanza nella property appropriata
     */
    @Override
    public List<Bio> listaBio() {
        return bioService.findAllByNome(nome);
    }// fine del metodo


    /**
     * Ordina la lista di didascalie specifiche <br>
     */
    @SuppressWarnings("all")
    public ArrayList<WrapDidascalia> ordinaListaDidascalie(ArrayList<WrapDidascalia> listaDisordinata) {
        return listaService.ordinaListaDidascalieNomi(listaDisordinata);
    }// fine del metodo


    public ListaSottopagina getSottopagina() {
        return listaService.sottopagina(mappa, pref.getInt(SOGLIA_SOTTOPAGINA_NOMI_COGNOMI), "Persone di nome " + nome.nome, titoloParagrafoVuoto, titoloSottoPaginaVuota);
    }// fine del metodo

}// end of class
