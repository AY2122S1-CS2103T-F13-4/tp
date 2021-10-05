package seedu.tuitione.model;

import javafx.collections.ObservableList;
import seedu.tuitione.model.person.Person;

/**
 * Unmodifiable view of an tuitione book
 */
public interface ReadOnlyTuitiOne {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

}
