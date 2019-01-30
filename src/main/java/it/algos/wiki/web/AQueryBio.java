package it.algos.wiki.web;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 28-gen-2019
 * Time: 14:37
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component("AQueryBio")
@Slf4j
public class AQueryBio  {

//    private String titoloWiki;
//
//
//    /**
//     * Costruttore base senza parametri <br>
//     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
//     * Può essere usato anche per creare l'istanza come SCOPE_PROTOTYPE <br>
//     * Usa: appContext.getBean(AQueryxxx.class) <br>
//     */
//    public AQueryBio() {
//        super();
//    }// end of constructor
//
//
//    /**
//     * Costruttore con parametri <br>
//     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
//     * Usa: appContext.getBean(AQueryxxx.class, urlRequest) <br>
//     * Usa: appContext.getBean(AQueryxxx.class, urlRequest).urlResponse() <br>
//     *
//     * @param urlDomain indirizzo web usato nella urlRequest
//     */
//    public AQueryBio(String urlDomain) {
//        super(urlDomain);
//    }// end of constructor
//
//
//    /**
//     * Request principale
//     * Quella base usa solo il GET
//     * In alcune request (non tutte) si aggiunge anche il POST
//     *
//     * @return urlResponse
//     */
//    public String urlResponse() {
//        return urlRequest(titoloWiki);
//    }// end of method
//
//
//    /**
//     * Request principale
//     * Quella base usa solo il GET
//     * In alcune request (non tutte) si aggiunge anche il POST
//     *
//     * @param titoloWiki della pagina
//     */
//    @Override
//    public String urlRequest(String titoloWiki) {
//        String templateBio = "";
//        String contenutoVoce = super.urlRequest(titoloWiki);
//
//        if (text.isValid(contenutoVoce)) {
//            templateBio = LibWiki.estraeTmplBioCompresi(contenutoVoce);
//        }// end of if cycle
//
//        return templateBio;
//    }// end of method


}// end of class
