package utilities;

import org.apache.commons.io.FileUtils;
import org.basex.core.Context;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.transform.XSLTransformException;
import org.jdom2.transform.XSLTransformer;
import org.jdom2.input.*;
import org.jdom2.output.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

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
        session.execute("create db " + schema);
        session.execute("set parser xml");

    }

    /**
     * Remove any offending special characters that would throw off XML encodings
     * @param t_file
     * @return File cast of string
     */
    private File fix_file(File t_file, File tmpFile){
        File textFile = t_file;
        String data = null;
        try {
            data = FileUtils.readFileToString(textFile, StandardCharsets.UTF_8);
            data = data.replace("&nbsp;", " ");
            FileUtils.writeStringToFile(tmpFile, data, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tmpFile;
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
            File tmpFile = new File("./tmp.mm");
            tmpFile = fix_file(t_file, tmpFile);
            Document orgDoc = convertFileTojDom(tmpFile);
            try{
                tmpFile.delete(); // delete the temporary file
            }catch(Exception e){
                //unable to delete tmp file
            }
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
     * Apply an XSL to the input document and return the transformed document.
     *
     * @param p_doc
     * @return transformed document
     */
    private Document expandNodes(Document p_doc) {
        XSLTransformer myXSL;
        Document transformed_Doc = null;

        try {
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
