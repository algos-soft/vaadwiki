package it.algos.wiki.mediawiki;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 27-gen-2019
 * Time: 19:54
 * <p>
 * Legge una pagina di servizio con i dati wiki.
 * Solo lettura. Request di tipo GET. Niente Bot. Niente cookies.
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class ReadPage extends ReadWiki {

    /**
     * Tag aggiunto prima del titoloWiki (leggibile) della pagina per costruire il 'domain' completo
     */
    private final static String TAG_PAGE = TAG_QUERY + "prop=info|revisions&rvprop=content|ids|flags|timestamp|user|userid|comment|size&titles=";


    /**
     * Istanza (@Scope = 'singleton') inietta da Spring in automatico <br>
     * Metodo più semplice. Non si possono passare parametri <br>
     * Viene iniettata nel ciclo di 'init()' di questa classe ed è quindi disponibile solo DOPO il ciclo <br>
     * <p>
     * Se serve prima, sempre senza possibilità di passare parametri, occorre: <br>
     * 1) dichiararla nel costruttore  <br>
     * 2) spostare il suo uso in un metodo @PostConstruct  <br>
     * 3) dichiararla con Xxx.getInstance(), ma la classe Xxx DEVE essere un istanza esplicita di Singleton Class <br>
     * 4) dichiararla (sconsigliato per l'uso dei Test) con StaticContextAccessor.getBean(Xxx.class) <br>
     * <p>
     * Property pubblic per poterla usare nei Test <br>
     */
    @Autowired
    public ReadWiki readWiki;


    /**
     * Request di tipo GET
     * Accetta SOLO un domain (indirizzo) di pagine wiki
     *
     * @param titoloWiki
     *
     * @return contenuto completo (json) della pagina (con i metadati mediawiki)
     */
    public Page crea(String titoloWiki) {
        Page page = null;
        String rispostaContenutoGrezzo = "";

        if (text.isValid(titoloWiki)) {
            rispostaContenutoGrezzo = super.legge(titoloWiki);
            page = new Page(rispostaContenutoGrezzo);
        }// end of if cycle

        return page;
    }// end of method


    /**
     * Inserisce un tag specifico prima del titoloWiki della pagina per costruire il 'domain' completo
     */
    public String getTagIniziale() {
        return TAG_PAGE;
    } // fine del metodo

}// end of class
