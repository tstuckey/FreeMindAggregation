package utilities;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.filefilter.IOFileFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: thomasstuckey
 */
public class MydirectoryWalker extends DirectoryWalker {

    public MydirectoryWalker(IOFileFilter dirFilter, IOFileFilter fileFilter) {
        super(dirFilter, fileFilter, -1);
    }
    /**
     * Parse the directory by invoking the walk method that searches for file
     * names that match the
     *
     * @param startDirectory
     * @return
     */
    public List parseDirectory(File startDirectory) {
        List results = new ArrayList();
        try {
            this.walk(startDirectory, results);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * Adds the file to the results.
     *
     * @param file    to be added to the results
     * @param depth
     * @param results the list of files that meet the criteria.
     */
    protected void handleFile(File file, int depth, Collection results) {
        results.add(file);
    }
}
