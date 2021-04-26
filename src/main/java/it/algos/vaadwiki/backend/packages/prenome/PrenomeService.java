package it.algos.vaadwiki.backend.packages.prenome;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow14.backend.annotation.AIScript;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.enumeration.AEOperation;
import it.algos.vaadflow14.backend.logic.AService;
import it.algos.vaadflow14.backend.enumeration.AETypeReset;
import it.algos.vaadflow14.backend.interfaces.AIResult;
import it.algos.vaadflow14.backend.wrapper.AResult;
import it.algos.vaadwiki.backend.enumeration.*;
import it.algos.vaadwiki.backend.logic.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * Fix date: dom, 18-apr-2021 <br>
 * Fix time: 7:42 <br>
 * <p>
 * Classe (facoltativa) di un package con personalizzazioni <br>
 * Se manca, usa la classe EntityService <br>
 * Layer di collegamento tra il 'backend' e mongoDB <br>
 * Mantiene lo 'stato' della classe AEntity ma non mantiene lo stato di un'istanza entityBean <br>
 * L' istanza (SINGLETON) viene creata alla partenza del programma <br>
 * <p>
 * Annotated with @Service (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per iniettare questo singleton nel costruttore di xxxLogicList <br>
 * Annotated with @Scope (obbligatorio con SCOPE_SINGLETON) <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@Service
@Qualifier("prenomeService")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@AIScript(sovraScrivibile = false)
public class PrenomeService extends WikiService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * Costruttore senza parametri <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public PrenomeService() {
        super(Prenome.class);
        super.prefDownload = AEWikiPreferenza.lastDownloadPrenome;
    }

    /**
     * Crea e registra una entityBean <br>
     *
     * @param code di riferimento (obbligatorio, unico)
     *
     * @return la nuova entityBean appena creata e salvata
     */
    public Prenome crea(final String code) {
        return (Prenome) mongo.insert(newEntity(code));
    }


    /**
     * Creazione in memoria di una nuova entityBean che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @param code di riferimento (obbligatorio, unico)
     *
     * @return la nuova entityBean appena creata (non salvata)
     */
    public Prenome newEntity(final String code) {
        Prenome newEntityBean = Prenome.builderPrenome()
                .code(text.isValid(code) ? code : null)
                .build();

        return (Prenome) fixKey(newEntityBean);
    }

    /**
     * Retrieves an entity by its id.
     *
     * @param keyID must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    @Override
    public Prenome findById(final String keyID) {
        return (Prenome) super.findById(keyID);
    }


    /**
     * Retrieves an entity by its keyProperty.
     *
     * @param keyValue must not be {@literal null}.
     *
     * @return the entity with the given id or {@literal null} if none found
     *
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     */
    @Override
    public Prenome findByKey(final String keyValue) {
        return (Prenome) super.findByKey(keyValue);
    }


    /**
     * Esegue un azione di download, specifica del programma/package in corso <br>
     * Deve essere sovrascritto <br>
     *
     * @return true se l'azione è stata eseguita
     */
    public boolean download() {
        return downloadModulo(PATH_MODULO_PRENOME);
    }

    /**
     * Legge la mappa di valori dalla pagina wiki <br>
     * Cancella la (eventuale) precedente lista di prenome <br>
     * Elabora la lista di prenome <br>
     * Crea le entities e le integra da altro modulo <br>
     *
     * @param wikiTitle della pagina su wikipedia
     *
     * @return true se l'azione è stata eseguita
     */
    public boolean downloadModulo(String wikiTitle) {
        boolean status = false;
        String tag = A_CAPO + "\\*";
        String[] righe = null;
        String nome;
        String testoPagina = wiki.legge(wikiTitle);

        if (text.isValid(testoPagina)) {
            righe = testoPagina.split(tag);
        }

        if (array.isAllValid(righe)) {
            this.deleteAll();

            //--il primo va eliminato (non pertinente)
            for (int k = 1; k < righe.length; k++) {
                nome = righe[k];

                //--l'ultimo va troncato
                if (k == righe.length - 1) {
                    nome = nome.substring(0, nome.indexOf("\n\n"));
                }

                this.crea(nome);
            }
        } else {
            logger.error( "downloadModulo - Qualcosa non ha funzionato");
        }

        super.fixDataDownload();
        return status;
    }

}// end of singleton class