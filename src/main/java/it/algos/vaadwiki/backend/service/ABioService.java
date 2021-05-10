package it.algos.vaadwiki.backend.service;

import it.algos.vaadflow14.backend.service.*;
import it.algos.vaadwiki.wiki.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: sab, 08-mag-2021
 * Time: 10:56
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AAbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(ABioService.class); <br>
 * 3) @Autowired public ABioService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ABioService extends AAbstractService {


    /**
     * Legge il testo del template Bio da una voce <br>
     * Esamina solo il PRIMO template BIO che trova <br>
     * Gli estremi sono COMPRESI <br>
     * <p>
     * Recupera il tag iniziale con o senza ''Template''
     * Recupera il tag iniziale con o senza primo carattere maiuscolo
     * Recupera il tag finale di chiusura con o senza ritorno a capo precedente
     * Controlla che non esistano doppie graffe dispari all'interno del template
     *
     * @param wikiTitle della pagina wiki
     *
     * @return template completo di doppie graffe iniziali e finali
     */
    public String leggeTmpl(final String wikiTitle) {
        return wikiUser.leggeTmpl(wikiTitle, "Bio");
    }

    /**
     * Legge una singola pagina da wiki <br>
     * Non serve essere loggato come Bot <br>
     *
     * @param wikiTitle della pagina wiki
     *
     * @return pagina wiki
     */
    public Pagina leggePagina(final String wikiTitle) {
        Pagina pagina = null;

        return pagina;
    }

}