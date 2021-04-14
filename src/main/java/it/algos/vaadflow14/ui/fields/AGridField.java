package it.algos.vaadflow14.ui.fields;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow14.backend.entity.AEntity;
import it.algos.vaadflow14.ui.service.AColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static it.algos.vaadflow14.backend.application.FlowCost.FIELD_INDEX;
import static it.algos.vaadflow14.backend.application.FlowCost.VUOTA;

/**
 * Project vaadflow15
 * Created by Algos
 * User: gac
 * Date: lun, 27-lug-2020
 * Time: 07:25
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AGridField<T> extends AField<Object> {

    /**
     * Istanza unica di una classe @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) di servizio <br>
     * Iniettata automaticamente dal framework SpringBoot/Vaadin con l'Annotation @Autowired <br>
     * Disponibile DOPO il ciclo init() del costruttore di questa classe <br>
     */
    @Autowired
    public AColumnService column;


    private Grid innerField;

    private List<String> listaProperties;

    private Class entityClazz;


    public AGridField() {
    }


    public AGridField(final Class<? extends AEntity> entityClazz, List<String> listaProperties, List items) {
        innerField = new Grid(entityClazz, false);
        this.entityClazz = entityClazz;
        this.listaProperties = listaProperties;
        this.setItem(items);
        add(innerField);
    }


    /**
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     * Se viene implementata una sottoclasse, passa di qui per ogni sottoclasse oltre che per questa istanza <br>
     * Se esistono delle sottoclassi, passa di qui per ognuna di esse (oltre a questa classe madre) <br>
     */
    @PostConstruct
    protected void postConstruct() {
        addColumnsGrid(listaProperties);
    }


    /**
     * Aggiunge in automatico le colonne previste in listaProperties <br>
     */
    public void addColumnsGrid(String stringaDiProperties) {
        addColumnsGrid((array.fromStringa(stringaDiProperties)));
    }


    /**
     * Aggiunge in automatico le colonne previste in listaProperties <br>
     */
    public void addColumnsGrid(List<String> listaProperties) {
        innerField.removeAllColumns();
        innerField.addColumn(item -> VUOTA).setKey(FIELD_INDEX).setHeader("#").setWidth("2.8em").setFlexGrow(0);
        if (array.isAllValid(listaProperties)) {
            for (String propertyName : listaProperties) {
                column.add(innerField, entityClazz, propertyName);
            }
        }
        innerField.addAttachListener(event -> {
            innerField.getColumnByKey(FIELD_INDEX).getElement().executeJs("this.renderer = function(root, column, rowData) {root.textContent = rowData.index + 1}");
        });
    }


    @Override
    public void setItem(Collection collection) {
        innerField.setItems(collection);
    }


    @Override
    protected Object generateModelValue() {
        return new ArrayList<>();
    }


    @Override
    protected void setPresentationValue(Object value) {
    }

}
