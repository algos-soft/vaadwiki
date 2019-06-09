package it.algos.vaadwiki.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadwiki.didascalia.EADidascalia;
import it.algos.vaadwiki.didascalia.WrapDidascalia;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.modules.nome.Nome;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Fri, 07-Jun-2019
 * Time: 20:32
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class ListaNomi extends Lista {

    protected Nome nome;

    public ListaNomi(Nome nome) {
        this.nome = nome;
    }// end of Spring constructor

    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     * Se ci sono superclassi e sottoclassi, chiama prima @PostConstruct della superclasse <br>
     */
    @PostConstruct
    protected void inizia() {
        super.esegue();
    }// end of method

//    /**
//     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
//     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
//     * Ogni chiave della mappa è una dei giorni/anni in cui suddividere la pagina <br>
//     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
//     *
//     * @param nome di riferimento per la lista
//     *
//     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
//     */
//    public LinkedHashMap<String, ArrayList<String>> esegue(Nome nome) {
//        this.nome = nome;
//        return super.esegue();
//    }// fine del metodo


    /**
     * Recupera una lista (array) di records Bio che usano questa istanza di Giorno nella property giornoNato
     * oppure
     * Recupera una lista (array) di records Bio che usano questa istanza di Giorno nella property giornoMorto
     * oppure
     * Recupera una lista (array) di records Bio che usano questa istanza di Anno nella property annoNato
     * oppure
     * Recupera una lista (array) di records Bio che usano questa istanza di Anno nella property annoMorto
     * <p>
     * Sovrascritto nella sottoclasse concreta
     *
     * @return lista delle istanze di Bio che usano questo istanza nella property appropriata
     */
    @Override
    public ArrayList<Bio> listaBio() {
        return bioService.findAllByNome(nome.nome);
    }// fine del metodo


    /**
     * Costruisce una lista di didascalie (Wrap) che hanno una valore valido per la pagina specifica <br>
     * La lista NON è ordinata <br>
     * Sovrascritto nella sottoclasse concreta <br>
     *
     * @param listaGrezzaBio di persone che hanno una valore valido per la pagina specifica
     *
     * @return lista NON ORDINATA di didascalie (Wrap)
     */
    @Override
    public ArrayList<WrapDidascalia> creaListaDidascalie(ArrayList<Bio> listaGrezzaBio) {
        ArrayList<WrapDidascalia> lista = new ArrayList<WrapDidascalia>();
        WrapDidascalia wrap;

        for (Bio bio : listaGrezzaBio) {
            wrap = appContext.getBean(WrapDidascalia.class, bio, EADidascalia.liste);
            lista.add(wrap);
        }// end of for cycle

        return lista;
    }// fine del metodo

}// end of class
