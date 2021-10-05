package seedu.tuitione.model;

import static java.util.Objects.requireNonNull;
import static seedu.tuitione.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.tuitione.commons.core.GuiSettings;
import seedu.tuitione.commons.core.LogsCenter;
import seedu.tuitione.model.lesson.Lesson;
import seedu.tuitione.model.person.Person;

/**
 * Represents the in-memory model of the tuitione book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final TuitiOne tuitiOne;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;

    /**
     * Initializes a ModelManager with the given tuitiOne and userPrefs.
     */
    public ModelManager(ReadOnlyTuitiOne tuitiOne, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(tuitiOne, userPrefs);

        logger.fine("Initializing with tuitione book: " + tuitiOne + " and user prefs " + userPrefs);

        this.tuitiOne = new TuitiOne(tuitiOne);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.tuitiOne.getPersonList());
    }

    public ModelManager() {
        this(new TuitiOne(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getTuitiOneFilePath() {
        return userPrefs.getTuitiOneFilePath();
    }

    @Override
    public void setTuitiOneFilePath(Path tuitiOneFilePath) {
        requireNonNull(tuitiOneFilePath);
        userPrefs.setTuitiOneFilePath(tuitiOneFilePath);
    }

    //=========== TuitiOne ================================================================================

    @Override
    public void setTuitiOne(ReadOnlyTuitiOne tuitiOne) {
        this.tuitiOne.resetData(tuitiOne);
    }

    @Override
    public ReadOnlyTuitiOne getTuitiOne() {
        return tuitiOne;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return tuitiOne.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        tuitiOne.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        tuitiOne.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        tuitiOne.setPerson(target, editedPerson);
    }

    @Override
    public boolean hasLesson(Lesson lesson) {
        requireNonNull(lesson);
        return tuitiOne.hasLesson(lesson);
    }

    @Override
    public void deleteLesson(Lesson target) {
        tuitiOne.removeLesson(target);
    }

    @Override
    public void addLesson(Lesson lesson) {
        tuitiOne.addLesson(lesson);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedTuitiOne}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public ArrayList<Lesson> getLessonList() {
        return tuitiOne.getLessonList();
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return tuitiOne.equals(other.tuitiOne)
                && userPrefs.equals(other.userPrefs)
                && filteredPersons.equals(other.filteredPersons);
    }

}
