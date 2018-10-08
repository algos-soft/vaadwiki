package it.algos.vaadwiki.modules.bio;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.service.AService;
import it.algos.vaadwiki.service.ElaboraService;
import it.algos.wiki.Api;
import it.algos.wiki.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadwiki.application.VaadwikiCost.TAG_BIO;


/**
 * Project vaadbio2 <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Date: 11-ago-2018 17.19.29 <br>
 * <br>
 * Estende la classe astratta AService. Layer di collegamento per la Repository. <br>
 * <br>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Service (ridondante) <br>
 * Annotated with @Scope (obbligatorio = 'singleton') <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 */
@SpringComponent
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_BIO)
@Slf4j
@AIScript(sovrascrivibile = false)
public class BioService extends AService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private Api api;


    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    private ElaboraService elabora;

    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (come previsto dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    private BioRepository repository;


    /**
     * Costruttore <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public BioService(@Qualifier(TAG_BIO) MongoRepository repository) {
        super(repository);
        super.entityClass = Bio.class;
        this.repository = (BioRepository) repository;
    }// end of Spring constructor

    /**
     * Crea una entity e la registra <br>
     *
     * @param pageId        della pagina wiki (obbligatorio, unico)
     * @param wikiTitle     della pagina wiki (obbligatorio, unico)
     * @param tmplBioServer template effettivamente presente sul server (obligatorio, unico)
     *
     * @return la entity appena creata
     */
    public Bio crea(long pageId, String wikiTitle, String tmplBioServer) {
        return (Bio) save(newEntity(pageId, wikiTitle, tmplBioServer));
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Bio newEntity() {
        return newEntity(0, "");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Properties <br>
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok) <br>
     *
     * @param pageId    della pagina wiki (obbligatorio, unico)
     * @param wikiTitle della pagina wiki (obbligatorio, unico)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Bio newEntity(long pageId, String wikiTitle) {
        return newEntity(pageId, wikiTitle, "");
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Properties <br>
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok) <br>
     *
     * @param pageId        della pagina wiki (obbligatorio, unico)
     * @param wikiTitle     della pagina wiki (obbligatorio, unico)
     * @param tmplBioServer template effettivamente presente sul server (obligatorio, unico)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Bio newEntity(long pageId, String wikiTitle, String tmplBioServer) {
        return newEntity(pageId, wikiTitle, tmplBioServer, (LocalDateTime) null);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Gli argomenti (parametri) della new Entity DEVONO essere ordinati come nella Entity (costruttore lombok) <br>
     *
     * @param pageId                    della pagina wiki (obbligatorio, unico)
     * @param wikiTitle                 della pagina wiki (obbligatorio, unico)
     * @param tmplBioServer             template effettivamente presente sul server (obligatorio, unico)
     * @param lastLettura/aggiornamento della voce effettuata dal programma
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Bio newEntity(long pageId, String wikiTitle, String tmplBioServer, LocalDateTime lastLettura) {
        Bio entity = null;

        entity = findByKeyUnica(pageId);
        if (entity != null) {
            return findByKeyUnica(pageId);
        }// end of if cycle

        entity = Bio.builderBio()
                .pageId(pageId)
                .wikiTitle(wikiTitle.equals("") ? null : wikiTitle)
                .tmplBioServer(tmplBioServer.equals("") ? null : tmplBioServer)
                .lastModifica(LocalDateTime.of(2000, 1, 1, 0, 0))
                .lastLettura(lastLettura != null ? lastLettura : LocalDateTime.now())
                .sporca(false)
                .build();
        entity.id = entity.getPageId() + "";

        return entity;
    }// end of method


    /**
     * Returns all entities of the type <br>
     * <p>
     * Ordinate secondo l'ordinamento previsto
     *
     * @param sort ordinamento previsto
     *
     * @return all ordered entities
     */
    protected List<? extends AEntity> findAll(Sort sort) {
        List<? extends AEntity> lista = null;

        try { // prova ad eseguire il codice
            if (sort != null) {
                lista = repository.findAll(sort);
            } else {
                lista = repository.findTop50ByOrderByWikiTitleAsc();
            }// end of if/else cycle
        } catch (Exception unErrore) { // intercetta l'errore
            log.error(unErrore.toString());
        }// fine del blocco try-catch

        return lista;
    }// end of method

    /**
     * Returns all entities of the type <br>
     * <p>
     *
     * @return all ordered entities
     */
    public ArrayList<Long> findPageids() {
        ArrayList<Long> lista = new ArrayList<>();
        List<Bio> listaBio = null;

        listaBio = repository.findAllByOrderByWikiTitleAsc();

        for (Bio bio : listaBio) {
            lista.add(bio.pageId);
        }// end of for cycle

        return lista;
    }// end of method

    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param pageId di riferimento (obbligatorio)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Bio findByKeyUnica(long pageId) {
        return repository.findByPageId(pageId);
    }// end of method

    /**
     * Property unica (se esiste) <br>
     */
    @Override
    public String getPropertyUnica(AEntity entityBean) {
        return ((Bio) entityBean).getPageId() + "";
    }// end of method


    /**
     * Tutte le bio col flag 'sporca' = vero <br>
     */
    public ArrayList<Long> findSporcaPageId() {
        ArrayList lista = null;
        List<Bio> listaBio = repository.findAllBySporcaTrue();

        if (array.isValid(listaBio)) {
            lista = new ArrayList();
            for (Bio bio : listaBio) {
                lista.add(bio.pageId);
            }// end of for cycle
        }// end of if cycle

        return lista;
    }// end of method

    public void downloadBio(String wikiTitle) {
        Page pagina = api.leggePage(wikiTitle);
        if (pagina != null) {
            long pageId = pagina.getPageid();
            String templateText = api.estraeTmplBio(pagina);
            Bio bio = crea(pageId, wikiTitle, templateText);
            elabora.esegue(bio, true);
        }// end of if cycle
    }// end of method

}// end of class