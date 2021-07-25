package it.algos.vaadwiki.backend.packages.bio;

import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.wrapper.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import it.algos.vaadwiki.backend.service.*;
import it.algos.vaadwiki.wiki.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

/**
 * Project: vaadwiki <br>
 * Created by Algos <br>
 * User: gac <br>
 * First time: lun, 26-apr-2021 <br>
 * Last doc revision: mar, 18-mag-2021 alle 19:35 <br>
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
//Spring
@Service
//Spring
@Qualifier("bioService")
//Spring
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
//Algos
@AIScript(sovraScrivibile = false, type = AETypeFile.servicePackage, doc = AEWizDoc.inizioRevisione)
public class BioService extends AService {

    public static final String CATEGORIA_TEST = "Nati nel 1167";

    public static final String CATEGORIA_TEST_DUE = "Nati nel 1168";

    public static final String CATEGORIA_TEST_TRE = "Nati nel 1935";

    public static final String CATEGORIA_BIO = "BioBot";

    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AWikiBotService wikiBot;

    /**
     * Costruttore senza parametri <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public BioService() {
        super(Bio.class);
    }

    //    /**
    //     * Crea e registra una entityBean solo se non esisteva <br>
    //     * Deve esistere la keyPropertyName della collezione, in modo da poter creare una nuova entityBean <br>
    //     * solo col valore di un parametro da usare anche come keyID <br>
    //     * Controlla che non esista già una entityBean con lo stesso keyID <br>
    //     * Deve esistere il metodo newEntity(keyPropertyValue) con un solo parametro <br>
    //     *
    //     * @param keyPropertyValue obbligatorio
    //     *
    //     * @return la nuova entityBean appena creata e salvata
    //     */
    //    @Override
    //    public Bio creaIfNotExist(final String keyPropertyValue) {
    //        return (Bio) checkAndSave(newEntity(keyPropertyValue));
    //    }

    /**
     * Creazione in memoria di una nuova entityBean che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Senza properties per compatibilità con la superclasse <br>
     *
     * @return la nuova entityBean appena creata (non salvata
     */
    @Override
    public Bio newEntity() {
        return newEntity(0, VUOTA, VUOTA, null);
    }

    /**
     * Creazione in memoria di una nuova entityBean che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @param wrap per i dati base essenziali di una biografia
     *
     * @return la nuova entityBean appena creata (non salvata)
     */
    public Bio newEntity(final WrapPage wrap) {
        return newEntity(wrap.getPageid(), wrap.getTitle(), wrap.getTmpl(), wrap.getTime());
    }

    /**
     * Creazione in memoria di una nuova entityBean che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @param wrap per i dati base essenziali di una biografia
     *
     * @return la nuova entityBean appena creata (non salvata)
     */
    public Bio newEntity(final BioWrap wrap) {
        return newEntity(wrap.getPageid(), wrap.getTitle(), wrap.getTmplBio(), wrap.getLastModifica());
    }

    /**
     * Creazione in memoria di una nuova entityBean che NON viene salvata <br>
     * Usa il @Builder di Lombok <br>
     * Eventuali regolazioni iniziali delle property <br>
     *
     * @param pageId       di riferimento (obbligatorio, unico)
     * @param wikiTitle    di riferimento (obbligatorio, unico)
     * @param tmplBio      (obbligatorio, unico)
     * @param lastModifica sul server wiki (obbligatorio)
     *
     * @return la nuova entityBean appena creata (non salvata)
     */
    public Bio newEntity(final long pageId, final String wikiTitle, final String tmplBio, final LocalDateTime lastModifica) {
        Bio newEntityBean = Bio.builderBio()
                .pageId(pageId)
                .wikiTitle(text.isValid(wikiTitle) ? wikiTitle : null)
                .tmplBio(text.isValid(tmplBio) ? tmplBio : null)
                .lastServer(lastModifica != null ? lastModifica : LocalDateTime.now())
                .lastMongo(LocalDateTime.now())
                .build();

        return (Bio) fixKey(newEntityBean);
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
    public Bio findById(final String keyID) {
        Bio bio = (Bio) super.findById(keyID);
        fixTransienti(bio);
        return bio;
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
    public Bio findByKey(final String keyValue) {
        return (Bio) super.findByKey(keyValue);
    }

    public void fixTransienti(Bio bio) {
        //        bio.tmplBioClient = bio.tmplBioServer + "Pippoz";
        //
        //        Map mappa = wikiBot.getMappaDownload(bio.tmplBioServer);
        //        bio.mappaDownload = mappa;
        //        bio.mappaTroncata = mappa;
        //        bio.mappaElaborata = mappa;
    }

    /**
     * Operazioni eseguite PRIMA di save o di insert <br>
     * Regolazioni automatiche di property <br>
     * Controllo della validità delle properties obbligatorie <br>
     * Controllo per la presenza della company se FlowVar.usaCompany=true <br>
     * Controlla se la entity registra le date di creazione e modifica <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @param entityBeanBefore da regolare prima del save
     * @param operation        del dialogo (NEW, Edit)
     *
     * @return the modified entity
     */
    @Override
    public AEntity beforeSave(final AEntity entityBeanBefore, final AEOperation operation) {
        AEntity entityBean = super.beforeSave(entityBeanBefore, operation);

        ((Bio) entityBean).valido = ((Bio) entityBeanBefore).lastMongo.isAfter(((Bio) entityBeanBefore).lastServer);
        return entityBean;
    }


    /**
     * Ciclo di download <br>
     * Parte dalla lista di tutti i (long) pageIds della categoria <br>
     * Usa la lista di pageIds e si recupera una lista (stessa lunghezza) di miniWrap <br>
     * Elabora la lista di miniWrap e costruisce una lista di pageIds da leggere <br>
     *
     * @return true se l'azione è stata eseguita
     */
    public void ciclo() {
        //@todo Categoria provvisorio
        String catTitle = CATEGORIA_TEST_TRE;
        //@todo Categoria provvisorio

        List<Long> listaPageIdsCategoria = null;
        List<MiniWrap> listaMiniWrap = null;
        List<Long> listaPageIdsDaLeggere = null;
        List<WrapPage> listaWrapPage = null;

        //--Controlla quante pagine ci sono nella categoria
        //--Si collega come anonymous; non serve essere loggati <br>
        wikiBot.getTotaleCategoria(catTitle);

        //@todo login provvisorio
        //        QueryLogin loggin= appContext.getBean(AQueryLogin.class);
        //        loggin.urlRequest();
        //        loggin.getCookies();
        //        QueryBot query= appContext.getBean(QueryBot.class,loggin.getCookies());
        //        query.urlRequest();
        //        boolean collegato=query.isBot();
        //        int a=87;
        //@todo login provvisorio

        //--Parte dalla lista di tutti i (long) pageIds della categoria
        //--Deve riuscire a gestire una lista di circa 430.000 long per la category BioBot
        listaPageIdsCategoria = wikiBot.getLongCat(catTitle);

        //--Usa la lista di pageIds e si recupera una lista (stessa lunghezza) di miniWrap
        //--Deve riuscire a gestire una lista di circa 430.000 miniWrap per la category BioBot
        listaMiniWrap = wikiBot.getMiniWrap(catTitle,listaPageIdsCategoria);

        //--Elabora la lista di miniWrap e costruisce una lista di pageIds da leggere
        //--Vengono usati quelli che hanno un miniWrap.pageid senza corrispondente bio.pageid nel mongoDb
        //--Vengono usati quelli che hanno miniWrap.lastModifica maggiore di bio.lastModifica
        //--A regime deve probabilmente gestire una lista di circa 10.000 miniWrap
        //--si tratta delle voci nuove e di quelle modificate nelle ultime 24 ore
        listaPageIdsDaLeggere = wikiBot.elaboraMiniWrap(listaMiniWrap);

        //--meglio suddividere in blocchi più piccoli
        listaWrapPage = wikiBot.leggePages(listaPageIdsDaLeggere);
        if (listaWrapPage != null && listaWrapPage.size() > 0) {
            for (WrapPage wrap : listaWrapPage) {
                creaBio(wrap);
            }
        }
    }

    /**
     * Scarica una singola biografia <br>
     */
    public Bio downloadBio(String wikiTitle) {
        WrapPage wrap = null;

        if (text.isValid(wikiTitle)) {
            wrap = wikiBot.leggePage(wikiTitle);
        }

        return wrap != null ? creaBio(wrap) : null;
    }

    /**
     * Costruisce una singola biografia <br>
     */
    public Bio creaBio(WrapPage wrap) {
        Bio bio = null;

        if (wrap != null && wrap.isValida()) {
            bio = this.newEntity(wrap);
            this.save(bio, AEOperation.newEditNoLog);
            logger.info(AETypeLog.download, String.format("Download della pagina %s", wrap.getTitle()));
        }
        else {
            if (wrap == null) {
                logger.warn(AETypeLog.download, "Qualcosa non ha funzionato");
            }
            else {
                logger.info(AETypeLog.download, String.format("Su wiki non esiste la pagina %s", wrap.getTitle()));
            }
        }

        return bio;
    }

    /**
     * Creazione o ricreazione di alcuni dati iniziali standard <br>
     * Invocato in fase di 'startup' e dal bottone Reset di alcune liste <br>
     * <p>
     * 1) deve esistere lo specifico metodo sovrascritto
     * 2) deve essere valida la entityClazz
     * 3) deve esistere la collezione su mongoDB
     * 4) la collezione non deve essere vuota
     * <p>
     * I dati possono essere: <br>
     * 1) recuperati da una Enumeration interna <br>
     * 2) letti da un file CSV esterno <br>
     * 3) letti da Wikipedia <br>
     * 4) creati direttamente <br>
     * DEVE essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     *
     * @return wrapper col risultato ed eventuale messaggio di errore
     */
    public AIResult reset() {
        AIResult result = super.reset();
        int numRec = 0;

        if (result.isErrato()) {
            return result;
        }

        //--da sostituire
        String message;
        message = String.format("Nel package %s la classe %s non ha ancora sviluppato il metodo reset() ", "bio", "BioService");
        return AResult.errato(message);

        // return super.fixPostReset(AETypeReset.enumeration, numRec);
    }

}// end of singleton class