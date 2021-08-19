package it.algos.test;

import static it.algos.vaadflow14.backend.application.FlowCost.*;
import it.algos.vaadwiki.backend.packages.bio.*;
import it.algos.vaadwiki.wiki.*;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: lun, 16-ago-2021
 * Time: 14:45
 */
public abstract class WTest extends ATest {

    protected static final String PAGINA_UNO = "Roman Protasevič";

    protected static final String PAGINA_DUE = "Sigurd Ribbung";

    protected static final String PAGINA_TRE = "Bernart Arnaut d'Armagnac";

    protected static final String PAGINA_QUATTRO = "Francesco Maria Pignatelli";

    protected static final String PAGINA_CINQUE = "Colin Campbell (generale)";

    protected static final String PAGINA_SEI = "Edwin Hall";

    protected static final String PAGINA_SETTE = "Louis Winslow Austin";

    protected static final String PAGINA_DISAMBIGUA = "Rossi";

    protected static final String PAGINA_REDIRECT = "Regno di Napoli (1805-1815)";

    protected String didascalia;

    protected WrapBio wrap;

    protected Bio bio;

    protected void print(final Bio bio, final String nomeCognome,final String attivitaNazionalita) {
        System.out.println(VUOTA);
        System.out.println(String.format("Titolo effettivo della voce: %s", sorgente));
        System.out.println(String.format("NomeCognome: %s", nomeCognome));
        System.out.println(String.format("AttivitaNazionalita: %s", attivitaNazionalita));
    }

    protected void print(final Bio bio, final String ottenuto) {
        print(bio.wikiTitle, bio.nome, bio.cognome, ottenuto);
    }

    protected void print(final String sorgente, final String sorgente2, final String sorgente3, final String ottenuto) {
        System.out.println(VUOTA);
        System.out.println(String.format("Titolo effettivo della voce: %s", sorgente));
        System.out.println(String.format("Nome: %s", sorgente2));
        System.out.println(String.format("Cognome: %s", sorgente3));
        System.out.println(String.format("Didascalia: %s", ottenuto));
    }

    protected void printWrapBio(WrapBio wrap) {
        System.out.println("WrapBio");
        System.out.println(VUOTA);
        System.out.println("Wrap valido: " + wrap.isValido());
        System.out.println("Titolo:" + SPAZIO + wrap.getTitle());
        System.out.println("PageId:" + SPAZIO + wrap.getPageid());
        System.out.println("Type:" + SPAZIO + wrap.getType());
        System.out.println("Timestamp:" + SPAZIO + wrap.getTime());
        System.out.println("Template:" + SPAZIO + wrap.getTemplBio());
    }

}
