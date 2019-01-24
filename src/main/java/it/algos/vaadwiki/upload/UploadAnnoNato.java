package it.algos.vaadwiki.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.liste.ListaAnnoNato;
import it.algos.vaadwiki.liste.ListaGiornoNato;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 24-gen-2019
 * Time: 17:22
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class UploadAnnoNato extends UploadAnni {

    @Autowired
    protected ListaAnnoNato listaAnnoNato;


    /**
     * Titolo della pagina da creare/caricare su wikipedia
     * Sovrascritto
     */
    protected void elaboraTitolo() {
        if (anno != null) {
            titoloPagina = getTitoloPagina(anno, "Nati");
        }// fine del blocco if
    }// fine del metodo


    /**
     * Costruisce una lista di biografie che hanno una valore valido per la pagina specifica
     * Esegue una query
     * Sovrascritto
     */
    protected void elaboraMappaListaDidascalieBio() {
        mappaListaOrdinataDidascalie = listaAnnoNato.esegue(anno);
        super.elaboraMappaListaDidascalieBio();
    }// fine del metodo

}// end of class
