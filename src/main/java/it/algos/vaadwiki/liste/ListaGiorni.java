package it.algos.vaadwiki.liste;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadwiki.didascalia.EADidascalia;
import it.algos.vaadwiki.didascalia.WrapDidascalia;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;

/**
 * Project vaadwiki
 * <p>
 * Created by Algos
 * User: gac
 * Date: gio, 24-gen-2019
 * Time: 08:43
 * <p>
 * Crea le liste dei nati o dei morti nel giorno
 */
@SpringComponent
@Slf4j
public abstract class ListaGiorni extends Lista {


    protected Giorno giorno;



    /**
     * Costruisce una mappa di liste di didascalie che hanno una valore valido per la pagina specifica <br>
     * La mappa è composta da una chiave (ordinata) e da un ArrayList di didascalie (testo) <br>
     * Ogni chiave della mappa è una dei giorni/anni in cui suddividere la pagina <br>
     * Ogni elemento della mappa contiene un ArrayList di didascalie ordinate per cognome <br>
     *
     * @param giorno di riferimento per la lista
     *
     * @return mappa ordinata delle didascalie ordinate per giorno/anno (key) e poi per cognome (value)
     */
    public LinkedHashMap<String, ArrayList<String>> esegue(Giorno giorno) {
        this.giorno = giorno;
        return super.esegue();
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
        return null;
    }// fine del metodo


}// end of class
