#Dal server wiki ad una Page (nuova o modificata)
##CicloUpdate

    1. newService.esegue(result);
    2. updateService.esegue(result);

##Creazione nuova Page
    newService.esegue(result);
        NewService.esegue(DownloadResult result) {
        pageService.downloadPagine(result);
            PageService.downloadPagine(DownloadResult result) {
            downloadSingoloBlocco(result, bloccoPage);
            downloadSingoloBlocco(DownloadResult result, ArrayList<Long> arrayPageid) {
            singoloBlocco(result, arrayPageid, EACicloType.download);
            singoloBlocco(DownloadResult result, ArrayList<Long> arrayPageid, EACicloType type) {
            creaBio(page, checkUpload);
            creaBio(Page page, boolean checkUpload) {
                    
##Aggiornamento Page di una pagina wiki esistente
    updateService.esegue(result);
        UpdateService.esegue(DownloadResult result) {
        esegueCiclo(result);
        esegueCiclo(DownloadResult result) {
        esegueSingoloBlocco(mappa, result);
        esegueSingoloBlocco(LinkedHashMap<Long, Timestamp> mappa, DownloadResult result) {
        pageService.updateSingoloBlocco(result, vociModificateDaRileggere);
            PageService.updateSingoloBlocco(DownloadResult result, ArrayList<Long> arrayPageid) {
            singoloBlocco(result, arrayPageid, EACicloType.update);
            singoloBlocco(DownloadResult result, ArrayList<Long> arrayPageid, EACicloType type) {       
            creaBio(page, checkUpload);
            creaBio(Page page, boolean checkUpload) {
    

#Da una Page (nuova o modificata) alla Entity
            PageService.creaBio(Page page, boolean checkUpload) {
            template = api.estraeTmplBio(page);
            entity = bioService.newEntity(pageid, wikiTitle, template, lastWikiModifica);
            entity = elaboraService.esegueNoSave(entity);
                ElaboraService.esegueNoSave(Bio bio) {
                esegue(bio, false);
                esegue(Bio bio, boolean registra) {
                mappa = getMappaBio(bio);
                    HashMap<String, String> getMappaBio(Bio bio) {
                    bio.getTmplBioServer();
                    libBio.getMappaBio(tmplBioServer);
                setValue(bio, mappa, registra);
1. Dalla Page estrae il tmpl
2. Costruisce una entity Bio, con le informazioni base
3. Crea una mappa HashMap<String, String> estraendola dal tmpl interno (originale) della entity Bio
4. Utilizza i valori della mappa per regolare le singole property della entity Bio