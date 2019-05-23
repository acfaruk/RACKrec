package ch.uzh.rackrec.data;


import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.utils.io.IReadingArchive;
import cc.kave.commons.utils.io.ReadingArchive;

import java.io.File;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContextIterator implements Iterator<Context> {

    private final static Logger logger = Logger.getLogger(ContextIterator.class.getName());
    String contextPath;
    Iterator<String> slnZips;
    Iterator<Context> currentContexts;

    public ContextIterator(String contextPath){
        this.contextPath = contextPath;
        slnZips = IoHelper.findAllZips(contextPath).iterator();

        if(slnZips.hasNext()){
            updateCurrentContexts();
        }
    }

    @Override
    public boolean hasNext() {

        if (currentContexts != null && currentContexts.hasNext()){
            return true;
        }else if(slnZips.hasNext()){
            updateCurrentContexts();

            return true;
        }
        return false;
    }

    @Override
    public Context next() {
        return currentContexts.next();
    }

    private void updateCurrentContexts() {
        while(currentContexts == null || currentContexts.hasNext() == false){
            if (slnZips.hasNext() == false){
                break;
            }

            String slnZip = slnZips.next();
            logger.log(Level.INFO, "Looking at sln zip: " + slnZip);
            IReadingArchive ra = null;
            try{
                ra = new ReadingArchive(new File(contextPath, slnZip));
                currentContexts = ra.getAll(Context.class).iterator();
            }finally{
                if(ra!= null) {
                    ra.close();
                }
            }

        }
    }
}
