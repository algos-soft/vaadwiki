package it.algos.vaadflow14.wizard.enumeration;

/**
 * Project vaadwiki14
 * Created by Algos
 * User: gac
 * Date: sab, 23-gen-2021
 * Time: 17:08
 *
 * La descrizione serve solo per ricordarmi <br>
 */
public enum AECopyWiz {
    dirDeletingAll("Cancella sempre la vecchia cartella e poi ricopia tutto.", AECopyType.directory),
    dirAddingOnly("Se non esiste, crea la cartella vuota. Aggiunge files e directories senza cancellare quelli esistenti", AECopyType.directory),
    dirSoloSeNonEsiste("Se esiste già, non fa nulla. Se non esiste, crea la cartella vuota. Aggiunge files e directories senza cancellare quelli esistenti", AECopyType.directory),
    fileSovrascriveSempreAncheSeEsiste("", AECopyType.file),
    fileSoloSeNonEsiste("", AECopyType.file),
    fileCheckFlagSeEsiste("", AECopyType.file),
    sourceSovrascriveSempreAncheSeEsiste("", AECopyType.source),
    sourceSoloSeNonEsiste("", AECopyType.source),
    sourceCheckFlagSeEsiste("", AECopyType.source),

    ;

    private String descrizione;

    private AECopyType type;

    /**
     * Costruttore <br>
     */
    AECopyWiz(String descrizione, AECopyType type) {
        this.descrizione = descrizione;
        this.type = type;
    }

    public AECopyType getType() {
        return type;
    }
}
