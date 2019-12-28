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
     * Upload delle statistiche delle didascalie. <br>
     */
    public void updateBiografie() {
        appContext.getBean(StatisticheBiografie.class);
    }// end of method


    /**
     * Upload delle statistiche delle didascalie. <br>
     */
    public void updateDidascalie() {
        appContext.getBean(StatisticheDidascalie.class);
    }// end of method


    /**
     * Upload delle statistiche dei giorni. <br>
     */
    public void updateGiorni() {
        long inizio = System.currentTimeMillis();
        appContext.getBean(StatisticheGiorni.class);

        logger.crea(EALogType.upload, "Upload delle statistiche per i giorni", inizio);
        pref.saveValue(LAST_UPLOAD_STATISTICHE_GIORNI, LocalDateTime.now());
        pref.saveValue(DURATA_UPLOAD_STATISTICHE_GIORNI, EATempo.minuti.get(inizio));
    }// end of method


    /**
     * Upload delle statistiche degli anni. <br>
     */
    public void updateAnni() {
        long inizio = System.currentTimeMillis();
        appContext.getBean(StatisticheAnni.class);

        logger.crea(EALogType.upload, "Upload delle statistiche per gli anni", inizio);
        pref.saveValue(LAST_UPLOAD_STATISTICHE_ANNI, LocalDateTime.now());
        pref.saveValue(DURATA_UPLOAD_STATISTICHE_ANNI, EATempo.minuti.get(inizio));
    }// end of method


    /**
     * Upload delle statistiche dei nomi. <br>
     */
    public void updateNomi() {
        long inizio = System.currentTimeMillis();
        appContext.getBean(StatisticheNomiA.class);
        appContext.getBean(StatisticheNomiB.class);

        logger.crea(EALogType.upload, "Upload delle statistiche dei nomi", inizio);
        pref.saveValue(LAST_UPLOAD_STATISTICHE_NOMI, LocalDateTime.now());
        pref.saveValue(DURATA_UPLOAD_STATISTICHE_NOMI, EATempo.minuti.get(inizio));
    }// end of method


    /**
     * Upload delle statistiche dei cognomi. <br>
     */
    public void updateCognomi() {
//        long inizio = System.currentTimeMillis();
//        appContext.getBean(StatisticheCognomi.class);
//
//        logger.crea(EALogType.upload, "Upload delle statistiche dei cognomi", inizio);
//        pref.saveValue(LAST_UPLOAD_STATISTICHE_COGNOMI, LocalDateTime.now());
//        pref.saveValue(DURATA_UPLOAD_STATISTICHE_COGNOMI, EATempo.minuti.get(inizio));
    }// end of method


    /**
     * Upload delle statistiche delle attività. <br>
     */
    public void updateAttivita() {
        long inizio = System.currentTimeMillis();
        appContext.getBean(StatisticheAttivita.class);

        logger.crea(EALogType.upload, "Upload delle statistiche delle attività", inizio);
        pref.saveValue(LAST_UPLOAD_STATISTICHE_ATTIVITA, LocalDateTime.now());
        pref.saveValue(DURATA_UPLOAD_STATISTICHE_ATTIVITA, EATempo.minuti.get(inizio));
    }// end of method


    /**
     * Upload delle statistiche delle nazionalità. <br>
     */
    public void updateNazionalita() {
        long inizio = System.currentTimeMillis();
        appContext.getBean(StatisticheNazionalita.class);

        logger.crea(EALogType.upload, "Upload delle statistiche delle nazionalità", inizio);
        pref.saveValue(LAST_UPLOAD_STATISTICHE_NAZIONALITA, LocalDateTime.now());
        pref.saveValue(DURATA_UPLOAD_STATISTICHE_NAZIONALITA, EATempo.minuti.get(inizio));
    }// end of method


    /**
     * Upload delle statistiche dei parametri. <br>
     */
    public void updateParametri() {
        appContext.getBean(StatisticheParametri.class);
    }// end of method


    /**
     * Upload di tutte le statistiche. <br>
     */
    public void updateAll() {
        if (checkMongo()) {
            return;
        }// end of if cycle

        updateBiografie();
        updateDidascalie();
        updateGiorni();
        updateAnni();
        updateNomi();
        updateCognomi();
        updateAttivita();
        updateNazionalita();
        updateParametri();
    }// end of method

}// end of class
