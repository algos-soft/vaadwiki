package it.algos.vaadwiki.statistiche;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.enumeration.EALogType;
import it.algos.vaadflow.enumeration.EATempo;
import it.algos.vaadwiki.service.ABioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

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
     * Upload delle statistiche. <br>
     * Può essere sovrascritto. Ma DOPO deve invocare il metodo della superclasse <br>
     */
    public void updatePaginaGiorni() {
        long inizio = System.currentTimeMillis();
        if (checkMongo()) {
            return;
        }// end of if cycle

        appContext.getBean(StatisticheGiorni.class);

        logger.crea(EALogType.upload, "Upload delle statistiche per i giorni", inizio);
        pref.saveValue(LAST_UPLOAD_STATISTICHE_GIORNI, LocalDateTime.now());
        pref.saveValue(DURATA_UPLOAD_STATISTICHE_GIORNI, EATempo.minuti.get(inizio));
    }// end of method


    /**
     * Upload delle statistiche. <br>
     * Può essere sovrascritto. Ma DOPO deve invocare il metodo della superclasse <br>
     */
    public void updatePaginaAnni() {
        long inizio = System.currentTimeMillis();
        if (checkMongo()) {
            return;
        }// end of if cycle

        appContext.getBean(StatisticheAnni.class);

        logger.crea(EALogType.upload, "Upload delle statistiche per gli anni", inizio);
        pref.saveValue(LAST_UPLOAD_STATISTICHE_ANNI, LocalDateTime.now());
        pref.saveValue(DURATA_UPLOAD_STATISTICHE_ANNI, EATempo.minuti.get(inizio));
    }// end of method

    /**
     * Upload delle statistiche. <br>
     * Può essere sovrascritto. Ma DOPO deve invocare il metodo della superclasse <br>
     */
    public void updatePagineStatistiche() {
        long inizio = System.currentTimeMillis();
        if (checkMongo()) {
            return;
        }// end of if cycle

        appContext.getBean(StatisticheAnni.class);

        logger.crea(EALogType.upload, "Upload delle statistiche per gli anni", inizio);
        pref.saveValue(LAST_UPLOAD_STATISTICHE_ANNI, LocalDateTime.now());
        pref.saveValue(DURATA_UPLOAD_STATISTICHE_ANNI, EATempo.minuti.get(inizio));
    }// end of method

}// end of class
