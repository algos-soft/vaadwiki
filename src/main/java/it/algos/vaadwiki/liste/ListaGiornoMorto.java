package it.algos.vaadwiki.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadwiki.enumeration.EADidascalia;
import it.algos.vaadwiki.modules.bio.Bio;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.List;

import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 24-gen-2019
 * Time: 08:37
 * <p>
 * Lista dei morti nel giorno <br>
 * La lista è un semplice testo (formattato secondo i possibili tipi di raggruppamento) <br>
 * Creata con appContext.getBean(ListaGiornoMorto.class, giorno) <br>
 * Punto di inzio @PostConstruct inizia() nella superclasse <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListaGiornoMorto extends Lista {


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public ListaGiornoMorto() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(ListaGiornoMorto.class, giorno) <br>
     *
     * @param giorno di cui costruire la pagina sul server wiki
     */
    public ListaGiornoMorto(Giorno giorno) {
        super.giorno = giorno;
        super.soggetto = giorno.titolo;
    }// end of constructor


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.typeDidascalia = EADidascalia.giornoMorto;
        super.usaSuddivisioneParagrafi = pref.isBool(USA_PARAGRAFI_GIORNI);
        super.usaRigheRaggruppate = pref.isBool(USA_RIGHE_RAGGRUPPATE_GIORNI);
        super.titoloParagrafoVuoto = pref.getStr(TAG_PARAGRAFO_VUOTO_GIORNI_MORTE);
        super.paragrafoVuotoInCoda = pref.isBool(IS_PARAGRAFO_VUOTO_GIORNI_IN_CODA);
        super.usaLinkParagrafo = pref.isBool(USA_LINK_PARAGRAFO_GIORNI);
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
        return bioService.findAllByGiornoMorte(giorno);
    }// fine del metodo


}// end of class
