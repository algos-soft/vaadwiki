package it.algos.vaadflow14.wizard.enumeration;

import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow14.backend.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import javax.annotation.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: dom, 18-lug-2021
 * Time: 08:58
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Component
public  class WizCostServiceInjector {

    @Autowired
    public FileService file;

    @Autowired
    public TextService text;

    @Autowired
    public ALogService logger;


    @PostConstruct
    public void postConstruct() {
        for (AEWizCost aeWizCost : AEWizCost.values()) {
            aeWizCost.setFile(file);
            aeWizCost.setText(text);
            aeWizCost.setLogger(logger);
        }
    }

}

