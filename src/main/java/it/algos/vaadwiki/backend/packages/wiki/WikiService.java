package it.algos.vaadwiki.backend.packages.wiki;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.enumeration.*;
import it.algos.vaadflow14.backend.logic.*;
import it.algos.vaadwiki.backend.enumeration.*;

import java.time.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * First time: ven, 16-apr-2021
 * Last doc revision: mar, 18-mag-2021 alle 18:37 <br>
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AAbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AWikiService.class); <br>
 * 3) @Autowired public AWikiService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
public abstract class WikiService extends AService {


    public final static String PATH_MODULO = "Modulo:Bio/";

    public final static String PATH_PROGETTO = "Progetto:Biografie/";

    public final static String PATH_MODULO_PLURALE = PATH_MODULO + "Plurale ";

    public final static String PATH_MODULO_LINK = PATH_MODULO + "Link ";

    public final static String GENERE = "genere";

    public final static String ATT = "Attività";

    public final static String ATT_LOWER = ATT.toLowerCase();

    public final static String NAZ = "Nazionalità";

    public final static String NAZ_LOWER = NAZ.toLowerCase();

    public final static String PATH_MODULO_GENERE = PATH_MODULO_PLURALE + ATT_LOWER + SPAZIO + GENERE;

    public final static String PATH_MODULO_ATTIVITA = PATH_MODULO_PLURALE + ATT_LOWER;

    public final static String PATH_STATISTICHE_ATTIVITA = PATH_PROGETTO + ATT;

    public final static String PATH_MODULO_NAZIONALITA = PATH_MODULO_PLURALE + NAZ_LOWER;

    public final static String PATH_STATISTICHE_NAZIONALITA = PATH_PROGETTO + NAZ;

    public final static String PATH_MODULO_PROFESSIONE = PATH_MODULO_LINK + ATT_LOWER;

    public final static String PATH_MODULO_PRENOME = "Progetto:Antroponimi/Nomi doppi";

    protected AEWikiPreferenza prefDownload;

    /**
     * Costruttore senza parametri <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public WikiService() {
        this(null);
    }

    /**
     * Costruttore senza parametri <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public WikiService(final Class<? extends AEntity> entityClazz) {
        super(entityClazz);
    }


    protected void fixDataDownload() {

        if (prefDownload != null) {
            prefDownload.setValue(LocalDateTime.now());
        }

    }

}