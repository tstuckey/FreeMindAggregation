import java.util.Enumeration;

/**
 * Created with IntelliJ IDEA.
 * User: thomasstuckey
 * Date: 8/21/12
 * Time: 7:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class OutputProperties {
    public static void main(String[] args) {
        Enumeration keys = System.getProperties().keys();
        while (keys.hasMoreElements()){
            String key = (String)keys.nextElement();
            String value = (String)System.getProperties().get(key);
            System.out.println(key+": "+value);
        }
    }

}
