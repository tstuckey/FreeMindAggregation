package core;

import utilities.BaseXClient;
import utilities.BaseXOperations;
import utilities.GeneralParsers;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: thomasstuckey
 * Date: 6/25/12
 * Time: 9:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParseAndLoad {

    public static void main(String[] args) {
        GeneralParsers gp = new GeneralParsers();
        //if there more than zero command line arguments, use the the first one, else use the string specified
        String xmlFileName=args.length > 0? args[0]:"initDB.xml";
        HashMap myHash =  gp.getDbParams(xmlFileName);
        ParseDirectory fileHits=new ParseDirectory(myHash.get("topDirectory").toString());

        try {
            BaseXClient session = new BaseXClient(myHash);
            BaseXOperations dbOps = new BaseXOperations(session);
            dbOps.setupDB(myHash.get("db").toString());
            dbOps.addFiles(fileHits.results);
            session.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }


}
