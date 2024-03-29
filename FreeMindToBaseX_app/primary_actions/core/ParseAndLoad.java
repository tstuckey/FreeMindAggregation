package core;

import utilities.BaseXClient;
import utilities.BaseXOperations;
import utilities.GeneralParsers;
import java.io.IOException;
import java.util.HashMap;

public class ParseAndLoad {

    class AuthenticationStructure {
        String host;
        int port;
        String user;
        String clear_password;
    }

    public static void main(String[] args) {
        GeneralParsers gp = new GeneralParsers();
        String xmlFileName = args.length > 0 ? args[0] : "initDB.xml";
        HashMap myHash = gp.getDbParams(xmlFileName);
        ParseDirectory fileHits = new ParseDirectory(myHash.get("topDirectory").toString());

        try {
            BaseXClient session = new BaseXClient(myHash.get("host").toString()
                    , Integer.parseInt(myHash.get("port").toString())
                    , myHash.get("usr").toString()
                    , myHash.get("passwd").toString());
            BaseXOperations dbOps = new BaseXOperations(session);
            dbOps.setupDB(myHash.get("db").toString());
            dbOps.addFiles(fileHits.results);
            session.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
