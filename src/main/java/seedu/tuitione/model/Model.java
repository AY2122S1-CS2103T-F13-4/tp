package seedu.tuitione.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.tuitione.commons.core.GuiSettings;
import seedu.tuitione.model.lesson.Lesson;
import seedu.tuitione.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' tuitione book file path.
     */
    Path getTuitiOneFilePath();

    /**
     * Sets the user prefs' tuitione book file path.
     */
    void setTuitiOneFilePath(Path tuitiOneFilePath);

    /**
     * Replaces tuitione book data with the data in {@code tuitiOne}.
     */
    void setTuitiOne(ReadOnlyTuitiOne tuitiOne);

    /** Returns the TuitiOne */
    ReadOnlyTuitiOne getTuitiOne();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the tuitione book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the tuitione book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the tuitione book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the tuitione book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the tuitione book.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Returns true if a lesson with the same values as {@code lesson} exists in TuitiONE
     */
    boolean hasLesson(Lesson lesson);

    /**
     * Deletes the given lesson.
     * The lesson must exist in TuitiONE.
     */
    void deleteLesson(Lesson lesson);

    /**
     * Adds the given lesson.
     * {@code lesson} must not already exist in TuitiONE.
     */
    void addLesson(Lesson lesson);

    /**
     * Returns list of lessons.
     */
    ArrayList<Lesson> getLessonList();
}
