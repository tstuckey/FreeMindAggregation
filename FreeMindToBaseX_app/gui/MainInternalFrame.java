import javax.swing.*;
import java.awt.*;

/* Used by Setup_Desktop.java. */
public class MainInternalFrame extends JInternalFrame {
    static int openFrameCount = 0;
    static final int xOffset = 40, yOffset = 40;


    public MainInternalFrame(JDesktopPane desktop) {
        super("",   //blank title
                true, //resizable
                true, //closable
                true, //maximizable
                true);//iconifiable


        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

        JScrollPane areaScrollPane = new JScrollPane(tabbedPane);
        areaScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        this.add(areaScrollPane);
        this.pack();
        this.setTitle("");
        //Set the window's location.
        setLocation(xOffset * openFrameCount, yOffset * openFrameCount);
    }

    public void setCursorWait(Boolean state) {
        //We had to make a private method to get the cursor to render correctly
        //for the JInternalFrame; using the setCursorWait in The_Desktop class
        //wasn't getting the results we needed
        if (state) {
            RootPaneContainer root = (RootPaneContainer) this.getTopLevelAncestor();
            root.getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            root.getGlassPane().setVisible(true);

        } else {
            RootPaneContainer root = (RootPaneContainer) this.getTopLevelAncestor();
            root.getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            root.getGlassPane().setVisible(true);
        }
    }
}
