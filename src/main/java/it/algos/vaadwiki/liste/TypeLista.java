package it.algos.vaadwiki.liste;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 13-dic-2019
 * Time: 11:26
 */
public class TypeLista {

    public boolean usaRigheRaggruppate;

    public boolean paragrafoVuotoInCoda;

    public boolean usaSuddivisioneParagrafi;

    public boolean usaLinkParagrafo;

    public boolean usaParagrafoSize;

    public boolean usaSottopagine;


    public TypeLista(boolean usaSuddivisioneParagrafi) {
        this.usaSuddivisioneParagrafi = usaSuddivisioneParagrafi;
        if (usaSuddivisioneParagrafi) {
            this.usaRigheRaggruppate = false;
            this.paragrafoVuotoInCoda = true;
            this.usaLinkParagrafo = true;
            this.usaParagrafoSize = true;
            this.usaSottopagine = true;
        } else {
            this.usaRigheRaggruppate = true;
            this.paragrafoVuotoInCoda = false;
            this.usaLinkParagrafo = false;
            this.usaParagrafoSize = false;
            this.usaSottopagine = false;
        }// end of if/else cycle
    }// end of constructor


    public TypeLista(
            boolean usaSuddivisioneParagrafi,
            boolean usaRigheRaggruppate,
            boolean paragrafoVuotoInCoda,
            boolean usaLinkParagrafo,
            boolean usaParagrafoSize,
            boolean usaSottopagine) {
        this.usaSuddivisioneParagrafi = usaSuddivisioneParagrafi;
        this.usaRigheRaggruppate = usaRigheRaggruppate;
        this.paragrafoVuotoInCoda = paragrafoVuotoInCoda;
        this.usaLinkParagrafo = usaLinkParagrafo;
        this.usaParagrafoSize = usaParagrafoSize;
        this.usaSottopagine = usaSottopagine;
    }// end of constructor

}// end of class
