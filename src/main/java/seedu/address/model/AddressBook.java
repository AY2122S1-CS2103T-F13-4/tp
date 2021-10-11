package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Student;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicateLessonException;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;

    private final List<Lesson> lessons;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        lessons = new ArrayList<>();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Student> students) {
        this.persons.setPersons(students);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setLessons(newData.getLessonList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Student student) {
        requireNonNull(student);
        return persons.contains(student);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Student p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Student target, Student editedStudent) {
        requireNonNull(editedStudent);

        persons.setPerson(target, editedStudent);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Student key) {
        persons.remove(key);
    }

    //// lesson-level operations

    /**
     * Returns true if a lesson with the same identity as {@code lesson} exists in TuitiONE.
     */
    public boolean hasLesson(Lesson lesson) {
        requireNonNull(lesson);
        for (Lesson l : lessons) {
            // weak equality check
            if (l.isSameLesson(lesson)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a lesson to TuitiONE.
     * The person must not already exist in the address book.
     */
    public void addLesson(Lesson l) {
        lessons.add(l);
    }

    /**
     * Removes a lesson from this {@code TuitiONE}.
     * lesson must exist in TuitiONE.
     */
    public void removeLesson(Lesson key) {
        lessons.remove(key);
    }

    /**
     * Replaces the contents of the lesson list with {@code lesson}.
     * {@code lesson} must not contain duplicate lessons.
     */
    public void setLessons(List<Lesson> lessons) {
        requireAllNonNull(lessons);
        if (!lessonsAreUnique(lessons)) {
            throw new DuplicateLessonException();
        }
        this.lessons.addAll(lessons);
    }

    /**
     * Returns true if {@code lessons} contains only unique lessons.
     */
    public boolean lessonsAreUnique(List<Lesson> lessons) {
        for (int i = 0; i < lessons.size() - 1; i++) {
            for (int j = i + 1; j < lessons.size(); j++) {
                if (lessons.get(i).isSameLesson(lessons.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    //// util methods

    @Override
    public String toString() {
        return "AddressBook["
                + persons.asUnmodifiableObservableList().size() + " persons, "
                + FXCollections.observableList(lessons).size() + " lessons]";
    }

    @Override
    public ObservableList<Student> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Lesson> getLessonList() {
        return FXCollections.observableList(lessons);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && persons.equals(((AddressBook) other).persons))
                && lessons.equals(((AddressBook) other).lessons);
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }

}
