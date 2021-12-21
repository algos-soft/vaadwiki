package it.algos.vaadwiki.backend.packages.bio;

import it.algos.vaadflow14.backend.annotation.*;
import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.exceptions.*;
import it.algos.vaadflow14.backend.interfaces.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadflow14.backend.wrapper.*;
import it.algos.vaadflow14.wizard.enumeration.*;
import it.algos.vaadwiki.backend.application.*;
import it.algos.vaadwiki.backend.enumeration.*;
import it.algos.vaadwiki.backend.login.*;
import it.algos.vaadwiki.backend.packages.wiki.*;
import it.algos.vaadwiki.backend.service.*;
import it.algos.vaadwiki.wiki.*;
import it.algos.vaadwiki.wiki.query.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import java.io.*;
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
public class BioService extends WikiService {

    public static final String CATEGORIA_TEST = "Nati nel 1167";

    public static final String CATEGORIA_TEST_DUE = "Nati nel 1168";

    public static final String CAT_1435 = "Nati nel 1435";

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
    public WikiBotService wikiBot;

    /**
     * Istanza di una interfaccia <br>
     * Iniettata automaticamente dal framework SpringBoot con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public BotLogin login;

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public ElaboraService elaboraService;

    /**
     * Costruttore senza parametri <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public BioService() {
        super(Bio.class);
        super.prefDownload = AEWikiPreferenza.lastDownloadBiografie;
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
    public Bio newEntity(final WrapBio wrap) {
        return newEntity(wrap.getPageid(), wrap.getTitle(), wrap.getTemplBio(), wrap.getTime());
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
     *
     * @return la nuova entityBean appena creata (non salvata)
     */
    public Bio newEntity(final long pageId, final String wikiTitle, final String tmplBio) {
        return newEntity(pageId,wikiTitle,tmplBio,(LocalDateTime)null);
    }

        /**
         * Creazione in memoria di una nuova entityBean che NON viene salvata <br>
         * Usa il @Builder di Lombok <br>
         * Eventuali regolazioni iniziali delle property <br>
         *
         * @param pageId       di riferimento (obbligatorio, unico)
         * @param wikiTitle    di riferimento (obbligatorio, unico)
         * @param tmplBio      (obbligatorio, unico)
         * @param lastMongo sul server wiki (obbligatorio)
         *
         * @return la nuova entityBean appena creata (non salvata)
         */
    public Bio newEntity(final long pageId, final String wikiTitle, final String tmplBio, final LocalDateTime lastMongo) {
        Bio newEntityBean = Bio.builderBio()
                .pageId(pageId)
                .wikiTitle(text.isValid(wikiTitle) ? wikiTitle : null)
                .tmplBio(text.isValid(tmplBio) ? tmplBio : null)
                .lastServer(lastMongo != null ? lastMongo : LocalDateTime.now())
                .lastMongo(LocalDateTime.now())
                .build();

        return (Bio) fixKey(newEntityBean);
    }

    /**
     * Fetches all entities of the type <br>
     * <p>
     * Ordinate secondo l'annotation @AIView(sortProperty) della entityClazz <br>
     * Ordinate secondo la property 'ordine', se esiste <br>
     * Ordinate secondo la property 'code', se esiste <br>
     * Ordinate secondo la property 'descrizione', se esiste <br>
     * Altrimenti, ordinate in ordine di inserimento nel DB mongo <br>
     * Può essere sovrascritto, SENZA invocare il metodo della superclasse <br>
     *
     * @return all ordered entities
     */
    @Override
    public List<Bio> fetch() {
        return (List<Bio>) super.fetch();
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
    public Bio findById(final String keyID) throws AlgosException {
        return (Bio) super.findById(keyID);
    }


    /**
     * Retrieves an entity by a keyProperty.
     * Cerca una singola entity con una query. <br>
     * Restituisce un valore valido SOLO se ne esiste una sola <br>
     *
     * @param propertyName  per costruire la query
     * @param propertyValue must not be {@literal null}
     *
     * @return the founded entity unique or {@literal null} if none found
     */
    @Override
    public Bio findByProperty(String propertyName, Serializable propertyValue) throws AlgosException {
        return (Bio) super.findByProperty(propertyName, propertyValue);
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
    public Bio findByKey(final Serializable keyValue) throws AlgosException {
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
     * Crea e registra una entityBean <br>
     * A livello UI i fields sono già stati verificati <br>
     * Prevede un punto di controllo PRIMA della registrazione,
     * per eventuale congruità dei parametri o per valori particolari in base alla BusinessLogic <br>
     * Esegue la registrazione sul database mongoDB con un controllo finale di congruità <br>
     * Prevede un punto di controllo DOPO la registrazione,
     * per eventuali side effects su altre collections collegate o dipendenti <br>
     *
     * @param entityBeanDaRegistrare (nuova o esistente)
     * @param operation              del dialogo (new o modifica)
     *
     * @return la entityBean appena registrata, null se non registrata
     *
     * @throws
     */
    @Override
    public Bio save(AEntity entityBeanDaRegistrare, AEOperation operation) throws AlgosException {
        return (Bio) super.save(entityBeanDaRegistrare, operation);
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
        String catTitle = WikiVar.categoriaBio;;

        List<Long> listaPageIds = null;
        List<MiniWrap> listaMiniWrap = null;
        List<Long> listaPageIdsDaLeggere = null;
        List<WrapPage> listaWrapPage = null;
        List<WrapBio> listaWrapBio = null;
        String bio="bio";

        //--Controlla quante pagine ci sono nella categoria
        //--Si collega come anonymous; non serve essere loggati <br>
        wikiBot.getTotaleCategoria(catTitle);

        //--Controlla il collegamento come bot
        if (login == null || login.nonCollegato()) {
            logger.warn("Bot non collegato");
            return;
        }

        //--Parte dalla lista di tutti i (long) pageIds della categoria
        //--Deve riuscire a gestire una lista di circa 435.000 long per la category BioBot
        //--Tempo medio previsto = circa 1 minuto (come bot la categoria legge 5.000 pagine per volta)
        listaPageIds = appContext.getBean(QueryCat.class).urlRequest(catTitle).getLista();
        //--Nella listaPageIds possono esserci anche voci SENZA il tmpl BIO, che verranno scartate dopo

        //--Usa la lista di pageIds e recupera una lista (stessa lunghezza) di miniWrap
        //--Deve riuscire a gestire una lista di circa 435.000 long per la category BioBot
        //--Tempo medio previsto = circa 20 minuti  (come bot la query legge 500 pagine per volta
        listaMiniWrap = appContext.getBean(QueryTimestamp.class).urlRequest(listaPageIds).getLista();
        //--Nella listaMiniWrap possono esserci anche voci SENZA il tmpl BIO, che verranno scartate dopo

        //--Elabora la lista di miniWrap e costruisce una lista di pageIds da leggere
        //--Vengono usati quelli che hanno un miniWrap.pageid senza corrispondente bio.pageid nel mongoDb
        //--Vengono usati quelli che hanno miniWrap.lastModifica maggiore di bio.lastModifica
        //--A regime deve probabilmente gestire una lista di circa 10.000 miniWrap
        //--si tratta delle voci nuove e di quelle modificate nelle ultime 24 ore
        try {
            listaPageIdsDaLeggere = mongo.isExistsCollection(bio)?wikiBot.elaboraMiniWrap(listaMiniWrap):listaPageIds;
        } catch (AlgosException unErrore) {
            logger.info(String.format("Manca la collection %s nel database MongoDB",bio));
        }
        //--Nella listaPageIdsDaLeggere possono esserci anche voci SENZA il tmpl BIO, che verranno scartate dopo

        //--Legge tutte le pagine
        //--Recupera i contenuti di tutte le voci biografiche da creare/modificare
        //--Controlla che esiste il tmpl BIO <br>
        listaWrapBio = appContext.getBean(QueryPages.class).urlRequest(listaPageIdsDaLeggere).getLista();
        //--Nella listaWrapBio possono ci sono solo voci CON il tmpl BIO valido

        //--Crea/aggiorna le voci biografiche
        //--Salva le entities Bio su mongoDB
        //--Elabora (e salva) le entities Bio
        creaElaboraListaBio(listaWrapBio);

        super.fixDataDownload();
    }


    /**
     * Crea/aggiorna una serie di voci biografiche <br>
     */
    public void creaElaboraListaBio(List<WrapBio> listaWrapBio) {
        String message;
        int modificate = 0;
        long inizio = System.currentTimeMillis();

        for (WrapBio wrap : listaWrapBio) {
            modificate = creaElaboraBio(wrap) ? modificate + 1 : modificate;
        }
        message = String.format("Create o aggiornate %s biografie in %s", text.format(modificate), date.deltaText(inizio));
        logger.info(AETypeLog.bio, message);
    }

    /**
     * Crea/aggiorna una singola entity <br>
     */
    public boolean creaElaboraBio(WrapBio wrap) {
        Bio bio = null;

        if (wrap != null && wrap.isValido()) {
            bio = this.newEntity(wrap);
            bio = elaboraService.esegue(bio);
            bio.setLastMongo(LocalDateTime.now());
        }

        try {
            save(bio, AEOperation.newEditNoLog);
        } catch (AlgosException unErrore) {
            logger.log(AETypeLog.mongo, text.setQuadre(unErrore.getEntityBean().toString()) + SPAZIO + unErrore.getMessage());
        }

        return true;
    }


    /**
     * Scarica una singola biografia <br>
     */
    public Bio downloadBio(String wikiTitle) {
        WrapBio wrap = null;

        if (text.isValid(wikiTitle)) {
            wrap = appContext.getBean(QueryBio.class).urlRequest(wikiTitle).getWrap();
        }

        return fixBio(wrap);
    }


    /**
     * Scarica una singola biografia <br>
     */
    public Bio downloadBioSave(String wikiTitle) {
        Bio bio = downloadBio(wikiTitle);

        if (bio != null) {
            try {
                bio = save(bio, null);
            } catch (AlgosException unErrore) {
                logger.error(unErrore, this.getClass(), "downloadBioSave");
            }
        }

        return bio;
    }

    /**
     * Costruisce una singola biografia <br>
     */
    public Bio fixBio(WrapBio wrap) {
        Bio bio = null;

        if (wrap != null && wrap.isValido()) {
            bio = this.newEntity(wrap);
            bio = elaboraService.esegue(bio);
            bio.setLastMongo(LocalDateTime.now());
        }

        return bio;
    }

    /**
     * Costruisce una singola biografia <br>
     * Aggiorna la singola biografia se già esistente <br>
     */
    public Bio fixBioOld(WrapBio wrap) {
        Bio bio = null;

        if (wrap != null && wrap.isValido()) {
            try {
                bio = findById(Long.toString(wrap.getPageid()));
            } catch (AlgosException unErrore) {
                logger.warn(unErrore, this.getClass(), "fixBioOld");
            }
            ((MongoService)mongo).delete(bio); //@todo sistemare
            bio = this.newEntity(wrap);
            elaboraService.esegue(bio);
            try {
                save(bio, null);
            } catch (Exception unErrore) {
                logger.error(unErrore, this.getClass(), "fixBioOld");
            }

            //            if (isEsiste(String.valueOf(wrap.getPageid()))) {
            //                modificaBio(wrap);
            //            }
            //            else {
            //                nuovaBio(wrap);
            //            }
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
     * Aggiorna una biografia già esistente <br>
     */
    public Bio modificaBio(WrapBio wrap) {
        Bio bio = null;
        return bio;
    }


    /**
     * Costruisce una nuova biografia <br>
     */
    public Bio nuovaBio(WrapBio wrap) {
        Bio bio = null;

        if (wrap != null && wrap.isValido()) {
            bio = this.newEntity(wrap);
            try {
                save(bio, AEOperation.newEditNoLog);
            } catch (AlgosException unErrore) {
            }
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
     * Costruisce una singola biografia <br>
     */
    public Bio creaBio(WrapPage wrap) {
        Bio bio = null;

        if (wrap != null && wrap.isValida()) {
            bio = this.newEntity(wrap);
            try {
                save(bio, AEOperation.newEditNoLog);
            } catch (AlgosException unErrore) {
            }
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