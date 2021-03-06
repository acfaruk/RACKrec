/**
 * Copied from example project
 * https://github.com/kave-cc/java-cc-kave-examples/blob/7bbafadbddb2b221eeafce3af75a4e9cf40c3e5e/src/main/java/examples/IoHelper.java
 *
 */

package ch.uzh.rackrec.data;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.collect.Lists;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.utils.io.Directory;
import cc.kave.commons.utils.io.IReadingArchive;
import cc.kave.commons.utils.io.ReadingArchive;

/**
 * this class explains how contexts can be read from the file system
 */
public class IoHelper {

    private static final Logger logger = Logger.getLogger(IoHelper.class.getName());

    public static List<Context> read(String zipFile) {
        LinkedList<Context> res = Lists.newLinkedList();

        IReadingArchive ra = null;
        try {
            ra = new ReadingArchive(new File(zipFile));

            while (ra.hasNext()) {
                res.add(ra.getNext(Context.class));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not open reading archive. ", e);
        } finally {
            if (ra != null){
                ra.close();
            }
        }
        return res;
    }

    /*
     * will recursively search for all .zip files in the "dir". The paths that are
     * returned are relative to "dir".
     */
    public static Set<String> findAllZips(String dir) {
        return new Directory(dir).findFiles(s -> s.endsWith(".zip"));
    }
}