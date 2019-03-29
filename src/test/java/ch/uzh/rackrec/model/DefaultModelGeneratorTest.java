package ch.uzh.rackrec.model;

import ch.uzh.rackrec.data.KaveDataSet;
import ch.uzh.rackrec.model.gen.DefaultModelGenerator;
import ch.uzh.rackrec.model.gen.ModelGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Properties;

import static org.mockito.Mockito.mock;

public class DefaultModelGeneratorTest {
    @Before
    public void initialize(){

    }

    @Test
    public void setPropertiesOfDependencies(){
        KaveDataSet dataset = mock(KaveDataSet.class);
        DefaultModelGenerator sut = new DefaultModelGenerator(dataset);

        Properties props = new Properties();
        sut.setProperties(props);

        Mockito.verify(dataset).setProperties(props);
    }
}
