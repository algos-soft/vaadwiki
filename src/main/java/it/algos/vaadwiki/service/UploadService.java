package it.algos.vaadwiki.service;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.modules.bio.Bio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadbio2
 * Created by Algos
 * User: gac
 * Date: sab, 11-ago-2018
 * Time: 15:44
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class UploadService extends ABioService {

    /**
     * Carica sul servere wiki la entity indicata
     */
    public void esegue(long pageid) {
        Bio entity = bioService.findByKeyUnica(pageid);
    }// end of method

    /**
     * Legg
     * Crea un nuovo template dai dati dellla entity
     */
    public String creaTemplate(Bio entity) {
        String newTemplate="";
//        Bio entity = bioService.findByKeyUnica(pageid);

        return newTemplate;
    }// end of method

}// end of class
