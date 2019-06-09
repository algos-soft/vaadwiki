package it.algos.vaadwiki.liste;

import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Route;
import it.algos.vaadwiki.modules.nome.Nome;
import it.algos.vaadwiki.modules.nome.NomeService;
import it.algos.vaadwiki.upload.UploadNomi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static it.algos.vaadwiki.application.WikiCost.ROUTE_VIEW_NOMI;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: Fri, 07-Jun-2019
 * Time: 19:00
 * <p>
 * Classe per la visualizzazione di una lista di prova di biografie di un particolare giorno <br>
 * Viene invocata da NomeViewList <br>
 * Eliminato header e footer della pagina definitiva su wiki <br>
 * Lista delle biografie di un Nome <br>
 */
@Route(value = ROUTE_VIEW_NOMI)
public class ViewNome extends ViewListe {

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected NomeService nomeService;

    @Autowired
    protected ApplicationContext appContext;

    protected Nome nome;

//    @Autowired
    private UploadNomi uploadNomi;


    @Override
    public void setParameter(BeforeEvent event, String nomeIdKey) {
        super.idKey = nomeIdKey;
        this.inizia();
    }// end of method


    public void inizia() {
        nome = nomeService.findById(idKey);
        uploadNomi = appContext.getBean(UploadNomi.class,nome);
        testo = uploadNomi.getTesto();

        super.inizia();
    }// end of method

}// end of class
