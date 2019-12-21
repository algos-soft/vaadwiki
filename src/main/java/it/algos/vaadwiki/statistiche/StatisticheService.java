package it.algos.vaadwiki.statistiche;

import it.algos.vaadflow.enumeration.EALogType;
import it.algos.vaadflow.enumeration.EATempo;
import it.algos.vaadflow.modules.giorno.Giorno;
import it.algos.vaadwiki.service.ABioService;
import lombok.extern.slf4j.Slf4j;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.time.LocalDateTime;

import static it.algos.vaadwiki.application.WikiCost.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 21-dic-2019
 * Time: 20:23
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class StatisticheService extends ABioService {

    /**
     * Upload standard delle statistiche. <br>
     * Può essere sovrascritto. Ma DOPO deve invocare il metodo della superclasse <br>
     */
    public void updatePaginaGiorni() {
        long inizio = System.currentTimeMillis();
        if (checkMongo()) {
            return;
        }// end of if cycle

        appContext.getBean(StatisticheGiorni.class);
        logger.crea(EALogType.upload, "Upload delle statistiche per i giorni", inizio);
        setLastUploadStatisticheGiorni(inizio);
    }// end of method


    /**
     * Registra nelle preferenze la data dell'ultimo upload statistiche effettuato <br>
     * Registra nelle preferenze la durata dell'ultimo upload statistiche effettuato <br>
     */
    protected void setLastUploadStatisticheGiorni(long inizio) {
        pref.saveValue(LAST_UPLOAD_STATISTICHE_GIORNI, LocalDateTime.now());
        pref.saveValue(DURATA_UPLOAD_STATISTICHE_GIORNI, EATempo.minuti.get(inizio));
    }// end of method

}// end of class
