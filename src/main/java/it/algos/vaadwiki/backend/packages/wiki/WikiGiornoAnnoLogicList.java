package it.algos.vaadwiki.backend.packages.wiki;

import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.data.renderer.*;
import it.algos.vaadflow14.backend.entity.*;
import it.algos.vaadflow14.backend.service.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: dom, 26-dic-2021
 * Time: 10:41
 */
public abstract class WikiGiornoAnnoLogicList extends WikiLogicList {


    /**
     * Costruttore con parametri <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Nel costruttore della sottoclasse l'annotation @Autowired potrebbe essere omessa perché c'è un solo costruttore <br>
     * Nel costruttore della sottoclasse usa un @Qualifier perché la classe AService è astratta ed ha diverse sottoclassi concrete <br>
     * Riceve e regola la entityClazz (final) associata a questa logicView <br>
     *
     * @param entityService (obbligatorio) riferimento al service specifico correlato a questa istanza (prototype) di LogicList
     * @param entityClazz   (obbligatorio)  the class of type AEntity
     */
    public WikiGiornoAnnoLogicList(final AIService entityService, final Class<? extends AEntity> entityClazz) {
        super(entityService, entityClazz);
    }// end of Vaadin/@Route constructor


    /**
     * Preferenze usate da questa 'logica' <br>
     * Primo metodo chiamato dopo init() (implicito del costruttore) e postConstruct() (facoltativo) <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.usaBottoneDownload = false;
        this.usaBottoneModulo = false;

        this.usaColonnaWiki = true;
        this.usaColonnaTest = true;
        this.usaColonnaUpload = true;
    }

    /**
     * Regolazioni finali della Grid <br>
     * <p>
     * Eventuali colonna 'ad-hoc' <br>
     * Eventuali 'listener' specifici <br>
     * Può essere sovrascritto, invocando PRIMA il metodo della superclasse <br>
     */
    protected void fixGrid() {
        Grid realGrid;
        ComponentRenderer renderer;
        Grid.Column colonna;
        String lar = "5em";

        if (bodyPlaceHolder != null && grid != null) {
            realGrid = grid.getGrid();

            if (usaColonnaWiki) {
                renderer = new ComponentRenderer<>(this::createWikiNatiButton);
                colonna = realGrid.addColumn(renderer);
                colonna.setHeader("Nati");
                colonna.setWidth(lar);
                colonna.setFlexGrow(0);
            }
            if (usaColonnaTest) {
                renderer = new ComponentRenderer<>(this::createTestNatiButton);
                colonna = realGrid.addColumn(renderer);
                colonna.setHeader("Nati");
                colonna.setWidth(lar);
                colonna.setFlexGrow(0);
            }
            if (usaColonnaUpload) {
                renderer = new ComponentRenderer<>(this::createUploadNatiButton);
                colonna = realGrid.addColumn(renderer);
                colonna.setHeader("Nati");
                colonna.setWidth(lar);
                colonna.setFlexGrow(0);
            }

            if (usaColonnaWiki) {
                renderer = new ComponentRenderer<>(this::createWikiMortiButton);
                colonna = realGrid.addColumn(renderer);
                colonna.setHeader("Morti");
                colonna.setWidth(lar);
                colonna.setFlexGrow(0);
            }
            if (usaColonnaTest) {
                renderer = new ComponentRenderer<>(this::createTestMortiButton);
                colonna = realGrid.addColumn(renderer);
                colonna.setHeader("Morti");
                colonna.setWidth(lar);
                colonna.setFlexGrow(0);
            }
            if (usaColonnaUpload) {
                renderer = new ComponentRenderer<>(this::createUploadMortiButton);
                colonna = realGrid.addColumn(renderer);
                colonna.setHeader("Morti");
                colonna.setWidth(lar);
                colonna.setFlexGrow(0);
            }
        }
    }

    protected Button createWikiNatiButton(AEntity entityBean) {
        Button wikiButton = new Button(new Icon(VaadinIcon.LIST));
        wikiButton.getElement().setAttribute("theme", "secondary");
        wikiButton.addClickListener(e -> wikiNatiPage(entityBean));

        return wikiButton;
    }


    protected Button createTestNatiButton(AEntity entityBean) {
        Button viewButton = new Button(new Icon(VaadinIcon.SERVER));
        viewButton.getElement().setAttribute("theme", "secondary");
        viewButton.addClickListener(e -> testNatiWiki(entityBean));

        return viewButton;
    }


    protected Button createUploadNatiButton(AEntity entityBean) {
        Button uploadButton = new Button(new Icon(VaadinIcon.UPLOAD));
        uploadButton.getElement().setAttribute("theme", "error");
        //        uploadButton.addClickListener(e -> uploadService.uploadAttivita(entityBean));

        return uploadButton;
    }

    protected Button createWikiMortiButton(AEntity entityBean) {
        Button wikiButton = new Button(new Icon(VaadinIcon.LIST));
        wikiButton.getElement().setAttribute("theme", "secondary");
        wikiButton.addClickListener(e -> wikiMortiPage(entityBean));

        return wikiButton;
    }


    protected Button createTestMortiButton(AEntity entityBean) {
        Button viewButton = new Button(new Icon(VaadinIcon.SERVER));
        viewButton.getElement().setAttribute("theme", "secondary");
        viewButton.addClickListener(e -> testMortiWiki(entityBean));

        return viewButton;
    }


    protected Button createUploadMortiButton(AEntity entityBean) {
        Button uploadButton = new Button(new Icon(VaadinIcon.UPLOAD));
        uploadButton.getElement().setAttribute("theme", "error");
        //        uploadButton.addClickListener(e -> uploadService.uploadAttivita(entityBean));

        return uploadButton;
    }

    protected void wikiNatiPage(AEntity entityBean) {
    }

    protected void wikiMortiPage(AEntity entityBean) {
    }


    protected void testNatiWiki(AEntity entityBean) {
    }

    protected void testMortiWiki(AEntity entityBean) {
    }

}
