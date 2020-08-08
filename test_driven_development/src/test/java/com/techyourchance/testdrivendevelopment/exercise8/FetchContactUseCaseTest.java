package com.techyourchance.testdrivendevelopment.exercise8;

import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint.Callback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.internal.invocation.finder.VerifiableInvocationsFinder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;


//import static org.hamcrest.CoreMatchers.*;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;


@RunWith(MockitoJUnitRunner.class)
public class FetchContactUseCaseTest {

    // region constants ----------------------------------------------------
    private static final String FILTER_TERM = "filterTerm";
    private static final String ID = "id";
    private static final String FULLNAME = "fullName";
    private static final String IMAGE_ULR = "imageUrl";
    private static final String FULLPHONE = "fullphone";
    private static final double AGE = 1.0;

    // end region constants -------------------------------------------------


    // region helper fields -----------------------------------------------------
    @Mock GetContactsHttpEndpoint mGetContactHttpEndpointMock;
    @Mock FetchContactUseCase.Listener mListenerMock1;
    @Mock FetchContactUseCase.Listener mListenerMock2;
    @Captor ArgumentCaptor<List<Contact>> mAcListContact;

    // end region helper fields --------------------------------------------------


    private FetchContactUseCase SUT;

    @Before
    public void setup() throws Exception {
        SUT = new FetchContactUseCase(mGetContactHttpEndpointMock);
        success();

    }



    // fetch contact correct filter term passed to endpoint

    @Test
    public void fetchContact_correctFilter_passedToEndpoint() {
        // Arrange
        ArgumentCaptor<String> actString = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.fetchContact(FILTER_TERM);
        // Assert
        verify(mGetContactHttpEndpointMock).getContacts(actString.capture(),any(Callback.class));
        assertThat(actString.getValue(),is(FILTER_TERM));
    }

    // fetch contact correct success observers notified with correct data

    @Test
    public void fetchContact_success_observersNotifiedWithCorrectData() {
        // Arrange
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        // Act
        SUT.fetchContact(FILTER_TERM);
        // Assert
        verify(mListenerMock1).onContactFetched(mAcListContact.capture());
        verify(mListenerMock2).onContactFetched(mAcListContact.capture());
        List<List<Contact>> captures = mAcListContact.getAllValues();

        List<Contact> capture1 = captures.get(0);
        List<Contact> capture2 = captures.get(1);

        assertThat(capture1, is(getContacts()));
        assertThat(capture2,is(getContacts()));
    }



    // fetch contact success unsubscribed not notified

    @Test
    public void fetchContact_success_unsubscribedObserversNotNotified() {
        // Arrange
        // Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.unregisterListener(mListenerMock2);
        SUT.fetchContact(FILTER_TERM);
        // Assert
        verify(mListenerMock1).onContactFetched(any(List.class));
        verifyNoMoreInteractions(mListenerMock2);
    }



    // fetch contact general error observers notified failure
    @Test
    public void fetchContact_generalError_observersNotifiedOfFailure() {
        // Arrange
        generalError();
        // Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.fetchContact(FILTER_TERM);
        // Assert
        verify(mListenerMock1).onFetchContactFailed();
        verify(mListenerMock2).onFetchContactFailed();
    }

    // fetch contact network error observers notified failure


    @Test
    public void fetchContact_networkError_observersNotifiedOfFailure() {
        // Arrange
        networkError();
        // Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.fetchContact(FILTER_TERM);
        // Assert
        verify(mListenerMock1).onFetchContactFailed();
        verify(mListenerMock2).onFetchContactFailed();
    }

    // region helper methods ---------------------------------------------------
    private List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(ID, FULLNAME, IMAGE_ULR));
        return contacts;
    }

    private List<ContactSchema> getContactsSchemas(){
        List<ContactSchema> schemas = new ArrayList<>();
        schemas.add(new ContactSchema(ID,FULLNAME,FULLPHONE, IMAGE_ULR,AGE));
        return schemas;
    }

    // end region helper methods -------------------------------------------------
    private void success() {
        doAnswer(new Answer(){

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Callback callback = (Callback) args[1];
                callback.onGetContactsSucceeded(getContactsSchemas());
                return null;
            }
        }).when(mGetContactHttpEndpointMock).getContacts(anyString(),any(Callback.class));
    }

    private void generalError(){
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Callback callback = (Callback) args[1];
                callback.onGetContactsFailed(GetContactsHttpEndpoint.FailReason.GENERAL_ERROR);
                return null;
            }
        }).when(mGetContactHttpEndpointMock).getContacts(anyString(),any(Callback.class));
    }

    private void networkError(){
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Callback callback = (Callback) args[1];
                callback.onGetContactsFailed(GetContactsHttpEndpoint.FailReason.NETWORK_ERROR);
                return null;
            }
        }).when(mGetContactHttpEndpointMock).getContacts(anyString(),any(Callback.class));
    }

    // region helper classes ------------------------------------------------------


    // end region helper classes ----------------------------------------------------


}