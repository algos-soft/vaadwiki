package it.algos.vaadwiki.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadwiki.modules.bio.Bio;
import it.algos.vaadwiki.service.ABioService;
import it.algos.wiki.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadflow.application.FlowCost.VUOTA;

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
     *
     * @param wikiTitle della pagina wiki (obbligatorio, unico)
     */
    public void esegue(String wikiTitle) {
        Bio entity = bioService.findByKeyUnica(wikiTitle);
    }// end of method


    /**
     * Legg
     * Crea un nuovo template dai dati dellla entity
     */
    public String creaTemplate(Bio entity) {
        String newTemplate = "";
//        Bio entity = bioService.findByKeyUnica(pageid);

        return newTemplate;
    }// end of method


    /**
     * Controlla che esistano modifiche sostanziali (non solo la data)
     *
     * @param titoloVoce eventualmente da modificare
     * @param testoNew   della modifica
     * @param tagIni     inizio del testo iniziale (incipit) da considerare NON sostanziale
     * @param tagEnd     fine del testo iniziale (incipit) da considerare NON sostanziale
     *
     * @return la modifica va effettuata
     */
    public boolean checkModificaSostanziale(String titoloVoce, String testoNew, String tagIni, String tagEnd) {
        boolean status = false;
        String testoOldSignificativo = VUOTA;
        String testoNewSignificativo = VUOTA;
        String testoOld = Api.leggeVoce(titoloVoce);
        int pos1 = 0;
        int pos2 = 0;

        if (text.isEmpty(testoOld)) {
            return true;
        }// end of if cycle

        if (text.isValid(testoOld) && text.isValid(testoNew)) {
            pos1 = testoOld.indexOf(tagIni);
            pos2 = testoOld.indexOf(tagEnd, pos1);
            try { // prova ad eseguire il codice
                testoOldSignificativo = testoOld.substring(pos2);
            } catch (Exception unErrore) { // intercetta l'errore
                int a = 87; //todo per ora niente (spedire mail)
            }// fine del blocco try-catch

            pos1 = testoNew.indexOf(tagIni);
            pos2 = testoNew.indexOf(tagEnd, pos1);
            try { // prova ad eseguire il codice
                testoNewSignificativo = testoNew.substring(pos2);
            } catch (Exception unErrore) { // intercetta l'errore
                int a = 87; //todo per ora niente (spedire mail)
            }// fine del blocco try-catch
        }// fine del blocco if

        if (text.isValid(testoOldSignificativo) && text.isValid(testoNewSignificativo)) {
            if (!testoNewSignificativo.equals(testoOldSignificativo)) {
                status = true;
            }// fine del blocco if
        }// fine del blocco if

        return status;
    } // fine del metodo


    public UploadGiornoNato uploadGiornoNato(Giorno giorno) {
       return appContext.getBean(UploadGiornoNato.class, giorno);
    }// end of method

}// end of class
