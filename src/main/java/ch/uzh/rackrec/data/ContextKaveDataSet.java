/**
 * Some parts copied from example project
 * https://github.com/kave-cc/java-cc-kave-examples/blob/7bbafadbddb2b221eeafce3af75a4e9cf40c3e5e/src/main/java/examples/IoHelper.java
 *
 */

package ch.uzh.rackrec.data;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.utils.io.IReadingArchive;
import cc.kave.commons.utils.io.ReadingArchive;
import com.google.inject.Inject;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContextKaveDataSet extends KaveDataSet {

    private String contextPath;
    private List<Context> contexts;

    @Inject
    public ContextKaveDataSet(Properties properties, Logger logger){
        super(properties, logger);
        contextPath = properties.getProperty("context-path");
        checkFolder();
    }

    @Override
    public Iterable<Context> getContextData() {
        return new Iterable<Context>() {
            @Override
            public Iterator<Context> iterator() {
                return new ContextIterator(contextPath);
            }
        };
    }

    private void checkFolder() {
        if (contextPath == null && Boolean.parseBoolean(properties.getProperty("generate-model")) == true){
            String message = "The context path was not found in the properties object. The data can't be loaded!";
            logger.log(Level.SEVERE, message);
            throw new RuntimeException(message);
        }

        Path path = Paths.get(URI.create("file://" + contextPath));


        if (Files.isDirectory(path) == false && Boolean.parseBoolean(properties.getProperty("generate-model")) == true){
            String message = "The context data folder was not found in: " + path + " please update the folder name.";
            logger.log(Level.SEVERE, message);
            throw new RuntimeException(message);
        };
    }

}
