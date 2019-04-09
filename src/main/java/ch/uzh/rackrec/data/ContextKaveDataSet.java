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
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContextKaveDataSet extends KaveDataSet {

    private String basePath;
    private String relativeContextPath;
    private String contextPath;
    private List<Context> contexts;

    @Inject
    public ContextKaveDataSet(Properties properties, Logger logger){
        super(properties, logger);
        loadProperties();
        checkFolder();
        loadDataSet();
    }

    @Override
    public List<Context> getContextData() {
        return contexts;
    }

    private void loadProperties(){
        basePath = properties.getProperty("base-path");
        relativeContextPath = properties.getProperty("context-path");
        contextPath = basePath + relativeContextPath;
    }

    private void checkFolder() {
        if (relativeContextPath == null){
            String message = "The context path was not found in the properties object. The data can't be loaded!";
            logger.log(Level.SEVERE, message);
            throw new RuntimeException(message);
        }

        Path path = Paths.get(URI.create("file://" + basePath + relativeContextPath));

        if (Files.isDirectory(path) == false){
            String message = "The context data folder was not found in: " + path + " please update the folder name.";
            logger.log(Level.SEVERE, message);
            throw new RuntimeException(message);
        };
    }

    private void loadDataSet(){
        Set<String> slnZips = IoHelper.findAllZips(contextPath);

        for (String slnZip : slnZips) {
            logger.log(Level.INFO, "\n#### processing solution zip: %s #####\n", slnZip);
            processSlnZip(slnZip);
        }
    }

    private void processSlnZip(String slnZip) {
        // open the .zip file ...
        try (IReadingArchive ra = new ReadingArchive(new File(contextPath, slnZip))) {
            logger.log(Level.INFO, "Reading contexts from ");
            contexts = ra.getAll(Context.class);
        }
    }

}
