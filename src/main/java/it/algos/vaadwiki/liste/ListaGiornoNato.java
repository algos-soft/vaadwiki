package it.algos.vaadwiki.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadwiki.didascalia.EADidascalia;
import it.algos.vaadwiki.didascalia.WrapDidascalia;
import it.algos.vaadwiki.modules.bio.Bio;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 18-gen-2019
 * Time: 07:56
 * <p>
 * Lista dei Nati nel giorno <br>
 * La lista è un semplice testo (formattato secondo i possibili tipi di raggruppamento) <br>
 * Creata con appContext.getBean(ListaGiornoNato.class, giorno) <br>
 * Punto di inzio @PostConstruct inizia() nella superclasse <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaGiornoNato extends Lista {


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public ListaGiornoNato() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(ListaGiornoNato.class, giorno) <br>
     *
     * @param giorno di cui costruire la pagina sul server wiki
     */
    public ListaGiornoNato(Giorno giorno) {
        super.giorno = giorno;
    }// end of constructor


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.typeDidascalia = EADidascalia.giornoNato;
        super.usaSuddivisioneParagrafi = pref.isBool(USA_PARAGRAFI_GIORNI);
        super.usaRigheRaggruppate = pref.isBool(USA_RIGHE_RAGGRUPPATE_GIORNI);
        super.titoloParagrafoVuoto = pref.getStr(TAG_PARAGRAFO_VUOTO_GIORNI_NASCITA);
        super.paragrafoVuotoInCoda = pref.isBool(IS_PARAGRAFO_VUOTO_GIORNI_IN_CODA);
        super.usaParagrafoSize = pref.isBool(USA_PARAGRAFO_SIZE_GIORNI);
    }// end of method


    /**
     * Recupera una lista (array) di records Bio che usano questa istanza di giorno/anno nella property nato/morto
     * Sovrascritto nella sottoclasse concreta
     *
     * @return lista delle istanze di Bio che usano questo istanza nella property appropriata
     */
    @Override
    public List<Bio> listaBio() {
        return bioService.findAllByGiornoNascita(giorno);
    }// fine del metodo


}// end of class
