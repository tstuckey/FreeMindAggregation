package utilities;

import org.basex.core.Context;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.transform.XSLTransformException;
import org.jdom2.transform.XSLTransformer;
import org.jdom2.input.*;
import org.jdom2.output.*;

import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: thomasstuckey
 * Date: 8/8/12
 * Time: 11:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseXOperations {
    // Create database context
    final Context ctx = new Context();
    BaseXClient session = null;

    public BaseXOperations(BaseXClient t_session) {
        this.session = t_session;
    }

    /**
     * Create the Database and set the parser to XML
     *
     * @param schema database schema to use
     * @throws IOException
     */
    public void setupDB(String schema) throws IOException {
        // create empty database
        session.execute("create db " + schema);
        session.execute("set parser xml");
        //System.out.println(session.info());

    }

    /**
     * Add the list of files to the Database opened in this
     * class' instance of the BaseXClient session
     *
     * @param results The list of files to be inserted into the database
     */
    public void addFiles(List results) {
        InputStream t_stream = null;
        Iterator<File> myiterator = results.iterator();
        File t_file;
        while (myiterator.hasNext()) {
            t_file = myiterator.next();
            Document orgDoc = convertFileTojDom(t_file);
            Document expandedDoc = expandNodes(orgDoc);
            ByteArrayOutputStream os = convertDocumentToOutputStream(expandedDoc);
            ByteArrayInputStream is = convertOutputTOInput(os);

            try {
                System.out.println("file name is:" + changeDirectorytoDBpath(t_file.toString()));
                session.add(changeDirectorytoDBpath(t_file.toString()), is);
            } catch (IOException e) {
                System.err.println(session.info());
                e.printStackTrace();
                System.exit(10);
            }
        }
    }

    /**
     * Replaces the backslashes in a filename with a forwardslashes
     * and replaces colons with forwardslashes to be Windows friendly
     *
     * @param inFileName Filename with full path to be converted
     * @return converted filename
     */
    private String changeDirectorytoDBpath(String inFileName) {
        String outFileName = inFileName.replaceAll("\\\\", "\\/");
        return outFileName.replaceAll(":", "");
    }

    /**
     * Convert the file into a jDom Document
     *
     * @param p_file
     * @return Document
     */
    private Document convertFileTojDom(File p_file) {
        Document my_doc = new Document();
        SAXBuilder my_sax = new SAXBuilder();
        try {
            //use the Saxon engine to build an XML
            my_doc = my_sax.build(p_file);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return my_doc;
    }

    /**
     * Apply an XSL to to the input document and return the transformed document.
     *
     * @param p_doc
     * @return transformed document
     */
    private Document expandNodes(Document p_doc) {
        XSLTransformer myXSL;
        Document transformed_Doc = null;

        try {
            //System.out.println("Directory is "+System.getProperty("user.dir"));
            //myXSL=new XSLTransformer("FreeMindToBaseX_app/transforms/expandNode.xsl"); //for invoking within IDE
            myXSL = new XSLTransformer("transforms/expandNode.xsl");  //for invoking from JAR
            transformed_Doc = myXSL.transform(p_doc);
        } catch (XSLTransformException e) {
            e.printStackTrace();
            transformed_Doc = null;
        }
        return transformed_Doc;
    }

    private ByteArrayInputStream convertOutputTOInput(ByteArrayOutputStream t_os) {
        return new ByteArrayInputStream(t_os.toByteArray());
    }

    private ByteArrayOutputStream convertDocumentToOutputStream(Document t_doc) {
        ByteArrayOutputStream result = null;
        try {
            XMLOutputter outputDocument = new XMLOutputter();
            outputDocument.setFormat(Format.getPrettyFormat());
            result = new ByteArrayOutputStream();
            outputDocument.output(t_doc, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
