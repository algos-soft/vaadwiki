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
import static it.algos.vaadwiki.application.WikiCost.SOGLIA_NOMI_MONGO;
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
public class StatisticheNomiB extends Statistiche {

    /**
     * Costruttore base senza parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * Usa: appContext.getBean(StatisticheNomiB.class) <br>
     */
    public StatisticheNomiB() {
    }// end of Spring constructor


    /**
     * Questa classe viene tipicamente costruita con appContext.getBean(StatisticheAttivita.class) <br>
     * La injection viene fatta da SpringBoot SOLO DOPO il metodo init() <br>
     * Si usa quindi un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired di questa classe <br>
     */
    @PostConstruct
    protected void initIstanzaDopoInitDiSpringBoot() {
        super.titoloPagina = NomeService.TITOLO_PAGINA_WIKI_2;
        inizia();
    }// end of method

    /**
     * Preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        super.fixPreferenze();
        this.templateCorrelate = "AntroponimiCorrelate";
    }// fine del metodo

    /**
     * Corpo della pagina <br>
     */
    protected void elaboraBody() {
        Nome nome=null;
        StringBuilder testo = new StringBuilder(VUOTA);
        int nomi = nomeService.count();
        int biografie = bioService.count();
        List<Nome> lista = nomeService.findAllAlfabetico();
        int soglia = pref.getInt(SOGLIA_NOMI_MONGO);
        int sogliaWiki = pref.getInt(SOGLIA_NOMI_PAGINA_WIKI);
        testo.append("==Nomi==");
        testo.append(A_CAPO);

//        testo += "Elenco dei" + text.format(nomi) + " nomi che raggiungono o superano le 50 ricorrenze nelle voci biografiche ";
        testo.append("Elenco dei '''");
        testo.append(text.format(nomi));
        testo.append("''' nomi '''differenti'''  utilizzati nelle '''");
        testo.append(text.format(biografie));
        testo.append("''' voci biografiche con occorrenze maggiori di '''");
        testo.append(soglia);
        testo.append("'''.");
        testo.append(A_CAPO);
        testo.append("Per ogni nome costruita una pagina con la lista delle voci biografiche se le occorrenze sono superiori a '''");
        testo.append(text.format(sogliaWiki));
        testo.append("'''");
        testo.append(A_CAPO);

        testo.append(A_CAPO);
        testo.append("{|class=\"wikitable sortable\" style=\"background-color:#EFEFEF;\"");
        testo.append(A_CAPO);
        testo.append("\n! style=\"background-color:#CCC;\" | '''#'''");
        testo.append("\n! style=\"background-color:#CCC;\" | '''Nome'''");
        testo.append("\n! style=\"background-color:#CCC;\" | '''Voci'''");

        for (int k = 1; k <= lista.size(); k++) {
             nome= lista.get(k-1);
            if (nome.valido) {
                testo.append("\n|-");
                testo.append("\n|style=\"text-align: right;\"|");
                testo.append(k);
                testo.append("||'''[[Persone di nome ");
                testo.append(nome.nome);
                testo.append("|");
                testo.append(nome.nome);
                testo.append("]]'''||style=\"text-align: right;\"|");
                testo.append(nome.voci);
            } else {
                testo.append("\n|-");
                testo.append("\n|style=\"text-align: right;\"|");
                testo.append(k);
                testo.append("||");
                testo.append(nome.nome);
                testo.append("||style=\"text-align: right;\"|");
                testo.append(nome.voci);
            }// end of if/else cycle
        }// end of for cycle
        testo.append(A_CAPO);
        testo.append("|}");

        testoPagina += testo.toString().trim();
    }// fine del metodo

}// end of class
