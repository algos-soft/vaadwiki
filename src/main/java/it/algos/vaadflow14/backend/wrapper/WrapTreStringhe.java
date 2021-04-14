package it.algos.vaadflow14.backend.wrapper;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: lun, 06-apr-2020
 * Time: 21:37
 */
public class WrapTreStringhe {

    private String prima;

    private String seconda;

    private String terza;


    public WrapTreStringhe(String prima, String seconda, String terza) {
        this.prima = prima;
        this.seconda = seconda;
        this.terza = terza;
    }


    public String getPrima() {
        return prima;
    }


    public void setPrima(String prima) {
        this.prima = prima;
    }


    public String getSeconda() {
        return seconda;
    }


    public void setSeconda(String seconda) {
        this.seconda = seconda;
    }


    public String getTerza() {
        return terza;
    }


    public void setTerza(String terza) {
        this.terza = terza;
    }

}
