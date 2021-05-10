package it.algos.vaadflow14.wiki;


import org.json.simple.*;

import java.sql.*;
import java.util.Date;
import java.util.*;

public enum PagePar {

    //--parametro di controllo
    batchcomplete(true, false, false, false, TypePar.letturascrittura, TypeField.booleano),

    //--parametri wiki info
    touched(true, false, false, false, TypePar.sololettura, TypeField.timestamp),
    ns(true, true, true, true, TypePar.letturascrittura, TypeField.longzero) {
        @Override
        public WikiPage setWiki(WikiPage wiki, Object value) {
            if (value instanceof Long) {
                wiki.setNs((long) value);
            }// fine del blocco if
            return wiki;
        }// end of method
    },
    contentmodel(true, true, true, true, TypePar.letturascrittura, TypeField.string) {
        @Override
        public WikiPage setWiki(WikiPage wiki, Object value) {
            if (value instanceof String) {
                wiki.setContentmodel((String) value);
            }// fine del blocco if
            return wiki;
        }// end of method
    },
    pagelanguagehtmlcode(true, false, false, true, TypePar.sololettura, TypeField.string),
    length(true, false, false, false, TypePar.sololettura, TypeField.longnotzero),
    pagelanguage(true, true, true, true, TypePar.letturascrittura, TypeField.string) {
        @Override
        public WikiPage setWiki(WikiPage wiki, Object value) {
            if (value instanceof String) {
                wiki.setPagelanguage((String) value);
            }// fine del blocco if
            return wiki;
        }// end of method
    },
    pagelanguagedir(true, false, false, true, TypePar.sololettura, TypeField.string),
    pageid(true, true, true, true, TypePar.letturascrittura, TypeField.longnotzero) {
        @Override
        public WikiPage setWiki(WikiPage wiki, Object value) {
            if (value instanceof Long) {
                wiki.setPageid((long) value);
            }// fine del blocco if
            return wiki;
        }// end of method
    },
    title(true, true, true, true, TypePar.letturascrittura, TypeField.string) {
        @Override
        public WikiPage setWiki(WikiPage wiki, Object value) {
            if (value instanceof String) {
                wiki.setTitle((String) value);
            }// fine del blocco if
            return wiki;
        }// end of method
    },
    lastrevid(true, false, false, true, TypePar.sololettura, TypeField.string),


    csrftoken(false, false, false, true, TypePar.soloscrittura, TypeField.string),
    starttimestamp(false, false, false, true, TypePar.soloscrittura, TypeField.date),

    //--parametri wiki revisions
    //    slots(true, false, false, false, TypePar.letturascrittura, TypeField.JSONObject) {
    //    },
    minor(true, true, false, false, TypePar.letturascrittura, TypeField.booleano) {
        @Override
        public WikiPage setWiki(WikiPage wiki, Object value) {
            if (value instanceof Boolean) {
                wiki.setMinor((boolean) value);
            }// fine del blocco if
            return wiki;
        }// end of method
    },
    size(true, true, false, false, TypePar.letturascrittura, TypeField.longnotzero) {
        @Override
        public WikiPage setWiki(WikiPage wiki, Object value) {
            if (value instanceof Long) {
                wiki.setSize((long) value);
            }// fine del blocco if
            return wiki;
        }// end of method
    },
    revid(true, true, false, false, TypePar.letturascrittura, TypeField.longnotzero) {
        @Override
        public WikiPage setWiki(WikiPage wiki, Object value) {
            if (value instanceof Long) {
                wiki.setRevid((long) value);
            }// fine del blocco if
            return wiki;
        }// end of method
    },
    comment(true, true, false, false, TypePar.letturascrittura, TypeField.string) {
        @Override
        public WikiPage setWiki(WikiPage wiki, Object value) {
            if (value instanceof String) {
                wiki.setComment((String) value);
            }// fine del blocco if
            return wiki;
        }// end of method
    },
    user(true, true, false, false, TypePar.letturascrittura, TypeField.string) {
        @Override
        public WikiPage setWiki(WikiPage wiki, Object value) {
            if (value instanceof String) {
                wiki.setUser((String) value);
            }// fine del blocco if
            return wiki;
        }// end of method
    },
    userid(true, true, false, false, TypePar.letturascrittura, TypeField.longzero) {
        @Override
        public WikiPage setWiki(WikiPage wiki, Object value) {
            if (value instanceof Long) {
                wiki.setUserid((long) value);
            }// fine del blocco if
            return wiki;
        }// end of method
    },
    parentid(true, true, false, false, TypePar.letturascrittura, TypeField.longzero) {
        @Override
        public WikiPage setWiki(WikiPage wiki, Object value) {
            if (value instanceof Long) {
                wiki.setParentid((long) value);
            }// fine del blocco if
            return wiki;
        }// end of method
    },
    timestamp(true, true, false, false, TypePar.letturascrittura, TypeField.timestamp) {
        @Override
        public WikiPage setWiki(WikiPage wiki, Object value) {
            if (value instanceof Timestamp) {
                wiki.setTimestamp((Timestamp) value);
            }// fine del blocco if
            return wiki;
        }// end of method
    },


    //--parametri slots/main
    contentformat(true, true, true, false, TypePar.letturascrittura, TypeField.string) {
        @Override
        public WikiPage setWiki(WikiPage wiki, Object value) {
            if (value instanceof String) {
                wiki.setContentformat((String) value);
            }// fine del blocco if
            return wiki;
        }// end of method
    },
    content(true, true, false, false, TypePar.letturascrittura, TypeField.string) {
        @Override
        public WikiPage setWiki(WikiPage wiki, Object value) {
            if (value instanceof String) {
                wiki.setContent((String) value);
            }// fine del blocco if
            return wiki;
        }// end of method
    },

    missing(true, false, false, false, TypePar.provvisorio, TypeField.booleano),
    revisions(false, false, false, false, TypePar.provvisorio, TypeField.string),
    ultimalettura(false, true, false, false, TypePar.sololettura, TypeField.timestamp) {
        @Override
        public WikiPage setWiki(WikiPage wiki, Object value) {
            if (value instanceof Timestamp) {
                wiki.setUltimalettura((Timestamp) value);
            }// fine del blocco if
            return wiki;
        }// end of method
    };

    private boolean read;

    private boolean database;

    private boolean obbligatorioDatabase;

    private boolean info;

    private TypePar typePar;

    private TypeField typeField;


    PagePar(boolean read, boolean database, boolean obbligatorioDatabase, boolean info, TypePar typePar, TypeField typeField) {
        this.read = read;
        this.database = database;
        this.obbligatorioDatabase = obbligatorioDatabase;
        this.info = info;
        this.typePar = typePar;
        this.typeField = typeField;
    }// fine del metodo costruttore


    /**
     * Restituisce una collezione di tutti gli elementi
     *
     * @return collezione
     */
    public static List<PagePar> getAll() {
        return (List<PagePar>) Arrays.asList(values());
    }// end of method


    /**
     * Restituisce il parametro, individuato dal nome
     *
     * @param key nome del parametro
     *
     * @return parametro
     */
    public static PagePar getPar(String key) {
        PagePar pagePar = null;

        for (PagePar pageParTmp : values()) {
            if (key.equals(pageParTmp.toString())) {
                pagePar = pageParTmp;
            }// fine del blocco if
        } // fine del ciclo for-each

        return pagePar;
    }// end of method


    /**
     * Restituisce il tipo di campo di un parametro, individuato dal nome
     *
     * @param key nome del parametro
     *
     * @return tipo di campo
     */
    public static TypeField getParField(String key) {
        return getPar(key).getType();
    }// end of method


    /**
     * Restituisce una collezione degli elementi di una Request Read
     *
     * @return collezione
     */
    public static List<PagePar> getRead() {
        List<PagePar> lista = new ArrayList<PagePar>();

        for (PagePar par : values()) {
            if (par.read) {
                lista.add(par);
            }
        }

        return lista;
    }


    /**
     * Restituisce una collezione limitata agli elementi permanenti col flag info valido
     *
     * @return collezione
     */
    public static List<PagePar> getInf() {
        List<PagePar> lista = new ArrayList<PagePar>();
        TypePar typeParLoc;

        for (PagePar par : values()) {
            typeParLoc = par.typePar;
            if (typeParLoc != TypePar.provvisorio) {
                if (par.info) {
                    lista.add(par);
                }// fine del blocco if
            }// fine del blocco if
        } // fine del ciclo for-each

        return lista;
    }// end of method


    /**
     * Restituisce una collezione limitata agli elementi permanenti col flag info NON valido
     *
     * @return collezione
     */
    public static List<PagePar> getRev() {
        List<PagePar> lista = new ArrayList<PagePar>();
        TypePar typeParLoc;

        for (PagePar par : values()) {
            typeParLoc = par.typePar;
            if (typeParLoc != TypePar.provvisorio) {
                if (!par.info) {
                    lista.add(par);
                }// fine del blocco if
            }// fine del blocco if
        } // fine del ciclo for-each

        return lista;
    }// end of method


    /**
     * Restituisce una collezione degli elementi permanenti (per il database)
     *
     * @return collezione
     */
    public static List<PagePar> getDB() {
        List<PagePar> lista = new ArrayList<PagePar>();

        for (PagePar par : values()) {
            if (par.database) {
                lista.add(par);
            }// fine del blocco if
        } // fine del ciclo for-each

        return lista;
    }// end of method


    /**
     * Restituisce una collezione degli elementi obbligatori (per la respons della request)
     *
     * @return collezione
     */
    public static List<PagePar> getObbReq() {
        List<PagePar> lista = new ArrayList<PagePar>();

        for (PagePar par : values()) {
            //            if (par.obbligatorioRequest) {
            //                lista.add(par);
            //            }// fine del blocco if
        } // fine del ciclo for-each

        return lista;
    }// end of method


    /**
     * Restituisce una collezione degli elementi obbligatori (per il save del database)
     *
     * @return collezione
     */
    public static List<PagePar> getObbDb() {
        List<PagePar> lista = new ArrayList<PagePar>();

        for (PagePar par : values()) {
            if (par.obbligatorioDatabase) {
                lista.add(par);
            }// fine del blocco if
        } // fine del ciclo for-each

        return lista;
    }// end of method


    /**
     * Restituisce una collezione degli elementi da restituire in lettura e scrittura
     *
     * @return collezione
     */
    public static List<PagePar> getWrite() {
        List<PagePar> lista = new ArrayList<PagePar>();

        for (PagePar par : values()) {
            if (par.typePar == TypePar.letturascrittura || par.typePar == TypePar.soloscrittura) {
                lista.add(par);
            }// fine del blocco if
        } // fine del ciclo for-each

        return lista;
    }// end of method


    /**
     * Controlla che tutti i parametri abbiano un valore valido (ai fini della lettura della Request Read)
     *
     * @param mappa dei valori
     *
     * @return true se tutti sono validi
     */
    public static boolean isParValidiRead(HashMap mappa) {
        boolean status = true;
        String key;
        Object value = null;

        for (PagePar par : getRead()) {
            key = par.toString();
            value = mappa.get(key);
            if (!isParValido(par, value)) {
                status = false;
            }// fine del blocco if
        } // fine del ciclo for-each

        return status;
    }// end of method


    /**
     * Controlla che il parametro abbia un valore valido
     *
     * @param par   parametro
     * @param value del parametro
     *
     * @return true se il valore è valido
     */
    private static boolean isParValido(PagePar par, Object value) {
        boolean status = false;
        TypeField type = par.getType();

        if (type == TypeField.string) {
            if (value != null && value instanceof String) {
                status = true;
            }// fine del blocco if
        }// fine del blocco if

        if (type == TypeField.longzero) {
            if (value instanceof Long) {
                status = true;
            }// fine del blocco if
        }// fine del blocco if

        if (type == TypeField.longnotzero) {
            if (value instanceof Long && (Long) value > 0) {
                status = true;
            }// fine del blocco if
        }// fine del blocco if

        if (type == TypeField.date) {
            if (value != null && value instanceof Date) {
                status = true;
            }// fine del blocco if
        }// fine del blocco if

        if (type == TypeField.timestamp) {
            if (value != null && value instanceof Timestamp) {
                status = true;
            }// fine del blocco if
        }// fine del blocco if

        if (type == TypeField.booleano) {
            if (value != null && value instanceof Boolean) {
                status = true;
            }// fine del blocco if
        }// fine del blocco if

        if (type == TypeField.JSONObject) {
            if (value != null && value instanceof JSONObject) {
                status = true;
            }// fine del blocco if
        }// fine del blocco if

        return status;
    }// end of method


    public static boolean isDatabase(String key) {
        return getPar(key).database;
    }// end of method


    /**
     * Inserisce nell'istanza il valore passato come parametro
     * La property dell'istanza ha lo stesso nome della enumeration
     * DEVE essere sovrascritto
     *
     * @param wiki  istanza da regolare
     * @param value valore da inserire
     *
     * @return istanza regolata
     */
    public WikiPage setWiki(WikiPage wiki, Object value) {
        return wiki;
    }// end of method


    public TypeField getType() {
        return typeField;
    }// end of method


    /**
     * Enumeration di tipologie dei campi
     */
    public static enum TypePar {
        sololettura, letturascrittura, soloscrittura, provvisorio
    }// fine della Enumeration interna


    /**
     * Enumeration di tipologie dei campi
     */
    public static enum TypeField {
        string, longzero, longnotzero, date, timestamp, booleano, JSONObject
    }// fine della Enumeration interna

} // fine della Enumeration
