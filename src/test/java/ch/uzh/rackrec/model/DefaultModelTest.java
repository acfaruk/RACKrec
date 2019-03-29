package ch.uzh.rackrec.model;

import ch.uzh.rackrec.model.gen.ModelGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Properties;

import static org.mockito.Mockito.mock;

public class DefaultModelTest {
    @Before
    public void initialize(){

    }

    @Test
    public void setPropertiesOfDependencies(){
        ModelGenerator gen = mock(ModelGenerator.class);
        DefaultModel sut = new DefaultModel(gen);

        Properties props = new Properties();
        sut.setProperties(props);

        Mockito.verify(gen).setProperties(props);
    }
}
