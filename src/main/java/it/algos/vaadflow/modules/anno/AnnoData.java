package it.algos.vaadflow.modules.anno;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.backend.data.AData;
import it.algos.vaadflow.enumeration.EASecolo;
import it.algos.vaadflow.modules.secolo.Secolo;
import it.algos.vaadflow.modules.secolo.SecoloService;
import it.algos.vaadflow.service.IAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadflow.application.FlowCost.TAG_ANN;
import static it.algos.vaadflow.application.FlowCost.VUOTA;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 08-ott-2018
 * Time: 15:15
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class AnnoData extends AData {


    //--usato nell'ordinamento delle categorie
    static final int ANNO_INIZIALE = 2000;

    static final int ANTE_CRISTO = 1000;
    static final int DOPO_CRISTO = 2030;

    @Autowired
    private SecoloService secoloService;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param service di collegamento per la Repository
     */
    @Autowired
    public AnnoData(@Qualifier(TAG_ANN) IAService service) {
        super(Anno.class, service);
    }// end of Spring constructor


    /**
     * Creazione della collezione
     * <p>
     * Ante cristo dal 1000
     * Dopo cristo fino al 2030
     */
    public int creaAll() {
        int num = 0;
        int progressivo;
        String titoloAnno;
        EASecolo secoloEnum;
        Secolo secolo;
        String titoloSecolo;

        //costruisce gli anni prima di cristo dal 1000
        for (int k = ANTE_CRISTO; k > 0; k--) {
            progressivo = ANNO_INIZIALE - k;
            titoloAnno = k + EASecolo.TAG_AC;
            secoloEnum = EASecolo.getSecoloAC(k);
            titoloSecolo = secoloEnum.getTitolo();
            secolo = secoloService.findByKeyUnica(titoloSecolo);
            if (progressivo != ANNO_INIZIALE) {
                ((AnnoService) service).crea(secolo, progressivo, titoloAnno);
            }// end of if cycle
        }// end of for cycle

        //costruisce gli anni dopo cristo fino al 2030
        for (int k = 1; k <= DOPO_CRISTO; k++) {
            progressivo = k + ANNO_INIZIALE;
            titoloAnno = k + VUOTA;
            secoloEnum = EASecolo.getSecoloDC(k);
            titoloSecolo = secoloEnum.getTitolo();
            secolo = secoloService.findByKeyUnica(titoloSecolo);
            if (progressivo != ANNO_INIZIALE) {
                ((AnnoService) service).crea(secolo, progressivo, titoloAnno);
            }// end of if cycle
        }// end of for cycle

        return num;
    }// end of method


}// end of class
