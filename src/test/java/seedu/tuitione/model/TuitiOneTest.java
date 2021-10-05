package seedu.tuitione.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.tuitione.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.tuitione.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.tuitione.testutil.Assert.assertThrows;
import static seedu.tuitione.testutil.TypicalPersons.ALICE;
import static seedu.tuitione.testutil.TypicalPersons.getTypicalTuitiOne;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.tuitione.model.person.Person;
import seedu.tuitione.model.person.exceptions.DuplicatePersonException;
import seedu.tuitione.testutil.PersonBuilder;

public class TuitiOneTest {

    private final TuitiOne tuitiOne = new TuitiOne();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), tuitiOne.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> tuitiOne.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyTuitiOne_replacesData() {
        TuitiOne newData = getTypicalTuitiOne();
        tuitiOne.resetData(newData);
        assertEquals(newData, tuitiOne);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        TuitiOneStub newData = new TuitiOneStub(newPersons);

        assertThrows(DuplicatePersonException.class, () -> tuitiOne.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> tuitiOne.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInTuitiOne_returnsFalse() {
        assertFalse(tuitiOne.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInTuitiOne_returnsTrue() {
        tuitiOne.addPerson(ALICE);
        assertTrue(tuitiOne.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInTuitiOne_returnsTrue() {
        tuitiOne.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(tuitiOne.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> tuitiOne.getPersonList().remove(0));
    }

    /**
     * A stub ReadOnlyTuitiOne whose persons list can violate interface constraints.
     */
    private static class TuitiOneStub implements ReadOnlyTuitiOne {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();

        TuitiOneStub(Collection<Person> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }
    }

}
