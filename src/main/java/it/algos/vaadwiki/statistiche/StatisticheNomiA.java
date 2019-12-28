package it.algos.vaadwiki.statistiche;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadwiki.modules.nome.Nome;
import it.algos.vaadwiki.modules.nome.NomeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.*;
import static it.algos.vaadwiki.application.WikiCost.SOGLIA_NOMI_PAGINA_WIKI;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 04-ott-2019
 * Time: 20:03
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class StatisticheNomiA extends Statistiche {


    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(StatisticheNomiA.class) <br>
     */
    public StatisticheNomiA() {
    }// end of Spring constructor


    /**
     * Questa classe viene tipicamente costruita con appContext.getBean(StatisticheNomiA.class) <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired di questa classe <br>
     */
    @PostConstruct
    protected void initIstanzaDopoInitDiSpringBoot() {
        super.titoloPagina = NomeService.TITOLO_PAGINA_WIKI;
        super.inizia();
    }// end of method


    /**
     * Preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        super.fixPreferenze();

        this.templateCorrelate = "AntroponimiCorrelate";
        super.usaTagIndice = false;
    }// fine del metodo


    /**
     * Corpo della pagina <br>
     */
    protected void elaboraBody() {
        StringBuilder testo = new StringBuilder(VUOTA);
        int nomi = nomeService.countValidi();
        List<Nome> lista = nomeService.findValidi();
        int soglia = pref.getInt(SOGLIA_NOMI_PAGINA_WIKI);
        testo.append("==Nomi==");
        testo.append(A_CAPO);

//        testo += "Elenco dei" + text.format(nomi) + " nomi che raggiungono o superano le 50 ricorrenze nelle voci biografiche ";
        testo.append("Elenco dei '''").append(text.format(nomi)).append("''' nomi che superano le '''").append(soglia).append("''' ricorrenze nelle voci biografiche.");
        testo.append(A_CAPO);
        testo.append("Per ognuno di questi nomi esiste una pagina dal titolo '''Persone di nome...''' contenente una lista di biografie suddivise per attività principale.");
        testo.append(A_CAPO);
        testo.append("Per ogni nome viene linkata la pagina con la lista ed il numero di voci biografiche che utilizzano il nome stesso come primo nome.");
        testo.append(A_CAPO);

        testo.append(A_CAPO);
        testo.append("{{Div col}}");
        testo.append(A_CAPO);
        for (Nome nome : lista) {
            testo.append("*");
            testo.append("[[Persone di nome ");
            testo.append(nome.nome);
            testo.append("|");
            testo.append(nome.nome);
            testo.append("]]");
            testo.append(SPAZIO);
            testo.append("(");
            testo.append("'''");
            testo.append(text.format(nome.voci));
            testo.append("'''");
            testo.append(")");
            testo.append(A_CAPO);
        }// end of for cycle
        testo.append("{{Div col end}}");

        testoPagina += testo.toString().trim();
    }// fine del metodo

}// end of class
