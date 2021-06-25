Preferenze
======================

Collezione di valori utilizzati per regolare alcuni aspetti del programma.

##Types
Diversi i tipi di dati gestiti:

- Stringa di testo: String
- Binario: boolean
- Numero intero: int
- Numero lungo: long
- Data: LocalDate
- Date e ora: LocalDateTime
- Orario: LocalTime
- Email: String con controllo di congruità
- Enum: String[] con valore selezionato tra quelli elencati
- Icona: Icon
  
##Valori
- Ogni preferenza specifica quale tipo di dato mantiene.
- Tutti i valori vengono memorizzati nel database MongoDB come dati binary.
- Viene usata una codifica e decodifica specifica per ogni tipo di dato.
- Usato sempre il charset di caratteri UTF-8
- La codifica _objectToBytes(Object obj)_ e la decodifica _<bytesToObject(byte[] bytes)_, avviene nella enumeration `AETypePref.type` della preferenza specifica.

##Company
- Le preferenze hanno un ID diverso a seconda se il programma usaCompany oppure no.
- La property usaCompany del programma può essere impostato **SOLO** alla creazione del programma e non può essere successivamente modificata.
####Usa company
- Se la property `usaCompany` del programma è _true_, la property `ID` è formata dalla property `company.code` seguita dalla property `preferenza.code`; quest'ultima con la iniziale maiuscola per visualizzare meglio la CamelCase.
- Se la property  `usaCompany` della preferenza è _false_, esiste una e una sola preferenza col medesimo valore di `code`.
- Se la property `usaCompany` della preferenza è _true_, esiste una preferenza col medesimo valore di `code` per ogni company esistente.
####Non usa company
- Se la property  `usaCompany` del programma è _false_, la property `company` della preferenza perde di significato e non viene usata.
- Se la property  `usaCompany` del programma è _false_, la property `usaCompany` della preferenza perde di significato e non viene usata.
- In questo caso la property `ID` coincide con la property `code`.
- La preferenza viene trovata indifferentemente tramite _findById(String keyID)_ oppure _findByKey(String keyValue)_ ricordando che keyID è uguale a keyValue.

##Collezione
Le preferenze memorizzate nella collezione del mongoDB sono di tre tipi:
* A - _Standard_, inserite all'avvio del programma prendendole dalla enumeration AEPreferenza <br>
* B - _Specifiche_, inserite all'avvio del programma prendendole da una enumeration specifica <br>
* C - _Aggiuntive_, inserite direttamente dall'utente (se permesso) <br>

##Fields
Ogni preferenza ha alcuni field chiave obbligatori:
* A - `id`; se usaCompany = false, id è uguale a `code` altrimenti se usaCompany = true, `id` DEVE contenere anche la sigla della company.
* B - `code` per individuare e selezionare la preferenza richiesta;  <br>
* C - `descrizione` per individuare lo scopo e il funzionamento della preferenza <br>
* D - `type` per suddividere le preferenze secondo la enumeration AETypePref <br>
* E - `value` per memorizzare il valore nel mongoDB sotto forma di byte[], convertiti secondo il `type` <br>

##Creazione
Funzionamento:
* 1 - Quando parte il programma la prima volta (quando è vuota la collection `preferenza` sul mongoDB), vengono create `TUTTE` le preferenze _standard_ e _specifiche_ provenienti dalle enumeration
* 2 - Quando il programma parte le volte successive (quando la collection preferenza sul mongoDB non è vuota), vengono create tutte e sole le preferenze (_standard_ e _specifiche_) NON presenti. Quelle presenti NON vengono modificate.
* 3 - Premendo il Bottone/menu `reset` si cancellano e si ricreano tutte e sole le preferenze (_standard_ e _specifiche_) indipendentemente dal fatto che abbiano valori personalizzati o meno.
* Le preferenze aggiuntive, create dall'utente, non vengono modificate
* 4 - Premendo il Bottone/menu `delete` si cancellano `TUTTE` le preferenze

