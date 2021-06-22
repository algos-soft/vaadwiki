package it.algos.vaadflow14.backend.wrapper;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: mar, 25-mag-2021
 * Time: 18:25
 */
public class WrapQuattro {

    private String prima;

    private String seconda;

    private String terza;

    private boolean valido;


    public WrapQuattro(WrapTreStringhe wrapTre, boolean valido) {
        this(wrapTre.getPrima(), wrapTre.getSeconda(), wrapTre.getTerza(), valido);
    }

    public WrapQuattro(String prima, String seconda, String terza, boolean valido) {
        this.prima = prima;
        this.seconda = seconda;
        this.terza = terza;
        this.valido = valido;
    }

    public String getPrima() {
        return prima;
    }

    public String getSeconda() {
        return seconda;
    }

    public String getTerza() {
        return terza;
    }

    public boolean isValido() {
        return valido;
    }

}
