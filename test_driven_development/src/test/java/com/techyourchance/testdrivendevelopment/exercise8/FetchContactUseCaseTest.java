import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentCaptor.*;
import static org.junit.Assert.*;


@RunWith(MockitoJUnitRunner.class)
public class FetchContactUseCaseTest {

    // region constants ----------------------------------------------------

    // end region constants -------------------------------------------------


    // region helper fields -----------------------------------------------------

    // end region helper fields --------------------------------------------------


    com.techyourchance.testdrivendevelopment.exercise8.FetchContactUseCase SUT;

    @Before
    public void setup() throws Exception {
        SUT = new com.techyourchance.testdrivendevelopment.exercise8.FetchContactUseCase();

    }


    // region helper methods ---------------------------------------------------

    // end region helper methods -------------------------------------------------

    // region helper classes ------------------------------------------------------

    // end region helper classes ----------------------------------------------------


}