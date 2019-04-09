package ch.uzh.rackrec.data;

import cc.kave.commons.model.events.completionevents.Context;
import org.junit.Test;

import java.net.URL;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class IoHelperTest {

    @Test
    public void readZipFileTest(){
        URL resource = IoHelperTest.class.getResource("/context/test.zip");
        List<Context> contexts = IoHelper.read(resource.getPath());

        assertEquals(29, contexts.size());
    }

    @Test
    public void findAllZipsTest(){
        URL resource = IoHelperTest.class.getResource("/context");
        Set<String> zipDirs = IoHelper.findAllZips(resource.getPath());

        assertEquals(1, zipDirs.size());
        assertEquals(zipDirs.iterator().next(), "test.zip");
    }
}
