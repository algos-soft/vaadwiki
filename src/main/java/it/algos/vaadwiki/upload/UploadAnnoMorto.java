package it.algos.vaadwiki.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.modules.anno.Anno;
import it.algos.vaadwiki.enumeration.EADidascalia;
import it.algos.vaadwiki.liste.ListaAnnoMorto;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

import static it.algos.vaadflow.application.FlowCost.SPAZIO;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: gio, 24-gen-2019
 * Time: 17:22
 * <p>
 * Classe specializzata per caricare (upload) le liste sul server wiki. <br>
 * <p>
 * Viene chiamato da Scheduler (standard, con frequenza giornaliera) <br>
 * Può essere invocato dal bottone 'Upload all' della classe WikiViewList <br>
 * Può essere invocato dal bottone 'Morti' della colonna 'Upload' della classe WikiViewList <br>
 * Necessita del login come bot <br>
 * Creata con appContext.getBean(UploadAnnoMorto.class, anno) <br>
 * Punto di inzio @PostConstruct inizia() nella sottoclasse <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class UploadAnnoMorto extends UploadAnni {


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public UploadAnnoMorto() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(UploadAnnoMorto.class, anno) <br>
     *
     * @param anno di cui costruire la pagina sul server wiki
     */
    public UploadAnnoMorto(Anno anno) {
        super(anno);
    }// end of constructor


    /**
     * Metodo invocato subito DOPO il costruttore
     * <p>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() del costruttore <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti, <br>
     * ma l'ordine con cui vengono chiamati (nella stessa classe) NON è garantito <br>
     * Se hanno la stessa firma, chiama prima @PostConstruct della sottoclasse <br>
     * Se hanno firme diverse, chiama prima @PostConstruct della superclasse <br>
     */
    @PostConstruct
    protected void inizia() {
        lista = appContext.getBean(ListaAnnoMorto.class, anno);
        super.inizia();
    }// end of method


    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.typeDidascalia = EADidascalia.annoMorto;
        super.titoloPagina = uploadService.getTitoloAnnoMorto(anno);
        super.tagCategoria = LibWiki.setCat("Liste di morti per anno", SPAZIO + anno.ordine);
    }// end of method


}// end of class
