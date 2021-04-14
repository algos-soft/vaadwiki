package it.algos.vaadflow14.wizard.enumeration;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 08-apr-2021
 * Time: 21:38
 */
public enum AEWizCopy {
    nome("Nome","Nome usato nelle dir e nei path.", AECopyType.directory),
    file("File","Copia un file.", AECopyType.directory),
    fileSovrascriveSempreAncheSeEsiste("File","Copia un file esistente. Se esiste il file di destinazione, lo sovrascrive.", AECopyType.file),
    fileSoloSeNonEsiste("File","Copia un file esistente. Se esiste il file di destinazione, non fa nulla.", AECopyType.file),
    fileCheckFlagSeEsiste("File","Copia un file esistente. Se esiste il file di destinazione, controlla il flag sovraScrivibile=false.", AECopyType.file),
    source("Source","Crea da source.", AECopyType.directory),
    sourceSovrascriveSempreAncheSeEsiste("Source","Crea un file da source. Se esiste il file di destinazione, lo sovrascrive.", AECopyType.source),
    sourceSoloSeNonEsiste("Source","Crea un file da source. Se esiste il file di destinazione, non fa nulla.", AECopyType.source),
    sourceCheckFlagSeEsiste("Source","Crea un file da source. Se esiste il file di destinazione, controlla il flag sovraScrivibile=false.", AECopyType.source),
    dir("Directory","Copia una directory.", AECopyType.directory),
    dirDeletingAll("Directory","Copia una directory esistente. Cancella sempre la vecchia cartella.", AECopyType.directory),
    dirAddingOnly("Directory","Copia una directory esistente. Se non esiste, crea la cartella vuota. Aggiunge files e directories senza cancellare quelli esistenti", AECopyType.directory),
    dirSoloSeNonEsiste("Directory","Copia una directory esistente. Se esiste già, non fa nulla. Se non esiste, crea la cartella vuota. Aggiunge files e directories senza cancellare quelli esistenti", AECopyType.directory),
    path("Path","Path completo. Inizia e finisce con SLASH", AECopyType.directory),

    ;

    private String tag;
    private String descrizione;

    private AECopyType type;

    /**
     * Costruttore <br>
     */
    AEWizCopy(String tag,String descrizione, AECopyType type) {
        this.tag = tag;
        this.descrizione = descrizione;
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public AECopyType getType() {
        return type;
    }

}
