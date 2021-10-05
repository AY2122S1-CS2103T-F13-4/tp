package seedu.tuitione.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import seedu.tuitione.model.lesson.Lesson;
import seedu.tuitione.model.person.Person;
import seedu.tuitione.model.person.UniquePersonList;

/**
 * Wraps all data at the tuitione-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class TuitiOne implements ReadOnlyTuitiOne {

    private final UniquePersonList persons;

    private final ArrayList<Lesson> lessons;

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

    public TuitiOne() {}

    /**
     * Creates an TuitiOne using the Persons in the {@code toBeCopied}
     */
    public TuitiOne(ReadOnlyTuitiOne toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Resets the existing data of this {@code TuitiOne} with {@code newData}.
     */
    public void resetData(ReadOnlyTuitiOne newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the tuitione book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the tuitione book.
     * The person must not already exist in the tuitione book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the tuitione book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the tuitione book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code TuitiOne}.
     * {@code key} must exist in the tuitione book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    //// lesson-level operations

    /**
     * Returns true if a lesson with the same identity as {@code lesson} exists in TuitiONE.
     */
    public boolean hasLesson(Lesson lesson) {
        requireNonNull(lesson);
        return lessons.contains(lesson);
    }

    /**
     * Adds a lesson to TuitiONE.
     * The person must not already exist in the tuitione book.
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

    //// util methods

    @Override
    public String toString() {
        return persons.asUnmodifiableObservableList().size() + " persons";
        // TODO: refine later
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    /**
     * Returns list of lessons.
     * @return the list of lessons.
     */
    public ArrayList<Lesson> getLessonList() {
        return lessons;
    }


    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TuitiOne // instanceof handles nulls
                && persons.equals(((TuitiOne) other).persons));
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
