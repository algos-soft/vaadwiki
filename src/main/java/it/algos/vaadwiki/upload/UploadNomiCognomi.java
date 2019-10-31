package it.algos.vaadwiki.upload;

/**
 * Project vaadwiki
 * Created by Algos
 * User: gac
 * Date: ven, 27-set-2019
 * Time: 07:56
 */
public abstract class UploadNomiCognomi extends Upload {

    /**
     * Le preferenze specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.usaBodySottopagine = true;
    }// end of method

}// end of class
