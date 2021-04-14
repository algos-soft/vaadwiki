package it.algos.vaadflow14.backend.logic;

import it.algos.vaadflow14.backend.entity.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: lun, 21-dic-2020
 * Time: 12:06
 * <p>
 * Classe di servizio per la Entity di un package <br>
 * Layer di collegamento tra il 'backend' e mongoDB <br>
 * Mantiene lo stato di una (final) entityClazz <br>
 * Di tipo SCOPE_PROTOTYPE ne viene creata una per ogni
 * package che non implementa la classe specifica xxxService <br>
 * I metodi esistono ma occorre un cast <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EntityService extends AService {


    /**
     * Costruttore con parametro <br>
     * Regola la entityClazz (final) associata a questo service <br>
     */
    public EntityService(final Class<? extends AEntity> entityClazz) {
        super(entityClazz);
    }


    /**
     * Creazione in memoria di una nuova entityBean che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * Senza properties per compatibilità con la superclasse <br>
     *
     * @return la nuova entityBean appena creata (non salvata)
     */
    @Override
    public AEntity newEntity() {
        AEntity newEntity = null;
        try {
            newEntity = entityClazz.newInstance();
        } catch (Exception unErrore) {
            logger.error(unErrore, this.getClass(), "newEntity");
        }

        return newEntity;
    }

}
