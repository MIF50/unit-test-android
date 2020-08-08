package com.techyourchance.testdrivendevelopment.exercise8;

import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;

import java.util.ArrayList;
import java.util.List;

public class FetchContactUseCase {



    public interface Listener {
        void onContactFetched(List<Contact> contacts);
        void onFetchContactFailed();
    }

    private GetContactsHttpEndpoint mGetContactHttpEndpoint;
    private final List<Listener> mListeners = new ArrayList<>();

    public FetchContactUseCase(GetContactsHttpEndpoint mGetContactHttpEndpointMock) {
        this.mGetContactHttpEndpoint = mGetContactHttpEndpointMock;

    }

    public void fetchContact(String filterTerm) {
        mGetContactHttpEndpoint.getContacts(filterTerm, new GetContactsHttpEndpoint.Callback() {
            @Override
            public void onGetContactsSucceeded(List<ContactSchema> contactSchemas) {
                notifySucceeded(contactSchemas);
            }

            @Override
            public void onGetContactsFailed(GetContactsHttpEndpoint.FailReason failReason) {
                switch (failReason) {
                    case GENERAL_ERROR:
                    case NETWORK_ERROR:
                        notifyFailed();
                        break;
                }
            }
        });

    }

    private void notifySucceeded(List<ContactSchema> contactSchemas) {
        for (Listener listener: mListeners) {
            listener.onContactFetched(contactsFromSchemas(contactSchemas));
        }

    }

    private void notifyFailed(){
        for (Listener listener : mListeners){
            listener.onFetchContactFailed();
        }
    }

    private List<Contact> contactsFromSchemas(List<ContactSchema> contactSchemas) {
        List<Contact> contacts = new ArrayList<>();
        for (ContactSchema schema : contactSchemas) {
            contacts.add(
                    new Contact(schema.getId(),schema.getFullName(),schema.getImageUrl())
            );
        }
        return contacts;
    }

    public void registerListener(Listener mListener) {
        mListeners.add(mListener);
    }

    public void unregisterListener(Listener listener) {
        mListeners.remove(listener);

    }


}
