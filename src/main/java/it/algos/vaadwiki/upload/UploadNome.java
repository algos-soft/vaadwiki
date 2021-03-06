package it.algos.vaadwiki.upload;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.enumeration.EADidascalia;
import it.algos.vaadwiki.liste.ListaNomi;
import it.algos.vaadwiki.modules.nome.Nome;
import it.algos.wiki.LibWiki;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadwiki.application.WikiCost.USA_FORCETOC_NOMI;
import static it.algos.vaadwiki.service.LibBio.PIPE;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Fri, 07-Jun-2019
 * Time: 20:28
 * <p>
 * Classe specializzata per caricare (upload) le liste sul server wiki. <br>
 * <p>
 * Viene chiamato da Scheduler (standard, con frequenza settimanale) <br>
 * Può essere invocato dal bottone 'Upload all' della classe NomeList <br>
 * Può essere invocato dal bottone della colonna 'Upload' della classe ViewNome <br>
 * Necessita del login come bot <br>
 * Creata con appContext.getBean(UploadNome.class, nome) <br>
 * Punto di inzio @PostConstruct inizia() nella sottoclasse <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class UploadNome extends Upload {


    //--property
    protected Nome nome;


    /**
     * Costruttore base senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso il parametro Bio del costruttore usato <br>
     */
    public UploadNome() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(UploadNome.class, nome) <br>
     *
     * @param nome di cui costruire la pagina sul server wiki
     */
    public UploadNome(Nome nome) {
        this.nome = nome;
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
        lista = appContext.getBean(ListaNomi.class, nome);
        super.soggetto = nome.nome;
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

        super.typeDidascalia = EADidascalia.listaNomi;
        super.usaSuddivisioneParagrafi = true;
        super.usaRigheRaggruppate = false;
        super.titoloPagina = uploadService.getTitoloNome(nome);
        super.usaHeadTocIndice = pref.isBool(USA_FORCETOC_NOMI);
        super.usaHeadIncipit = true;
        super.usaBodyDoppiaColonna = false;
        super.tagCategoria = LibWiki.setCat("Liste di persone per nome", nome.nome);
        super.usaBodySottopagine = true;
    }// end of method


    /**
     * Costruisce la frase di incipit iniziale
     * <p>
     * Sovrascrivibile <br>
     * Parametrizzato (nelle sottoclassi) l'utilizzo e la formulazione <br>
     */
    protected String elaboraIncipitSpecifico() {
        String testo = VUOTA;

        testo += "incipit lista nomi";
        testo += PIPE;
        testo += "nome=";
        testo += nome.nome;
        testo = LibWiki.setGraffe(testo);

        return testo;
    }// fine del metodo

}// end of class
