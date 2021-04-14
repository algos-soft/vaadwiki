package it.algos.vaadflow14.ui.service;

import com.vaadin.flow.component.html.Image;
import it.algos.vaadflow14.backend.service.AAbstractService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static it.algos.vaadflow14.backend.application.FlowCost.SLASH;
import static it.algos.vaadflow14.backend.application.FlowCost.VUOTA;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mar, 22-set-2020
 * Time: 18:17
 * <p>
 * Classe di libreria; NON deve essere astratta, altrimenti SpringBoot non la costruisce <br>
 * Estende la classe astratta AAbstractService che mantiene i riferimenti agli altri services <br>
 * L'istanza può essere richiamata con: <br>
 * 1) StaticContextAccessor.getBean(AImageService.class); <br>
 * 3) @Autowired public AImageService annotation; <br>
 * <p>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AImageService extends AAbstractService {

    /**
     * versione della classe per la serializzazione
     */
    private static final long serialVersionUID = 1L;


    public Image getBandiera(String statoSigla) {
        Image image = null;
        String path = VUOTA;
        String directory = "bandiere";
        String suffix = ".png";
        String alt = VUOTA;
        String height = "4mm";
        String width = "6mm";

        path = directory;
        path += SLASH;
        path += statoSigla;
        path += suffix;

        image = new Image(path, alt);
        image.setHeight(height);
        image.setWidth(width);

        return image;
    }

}