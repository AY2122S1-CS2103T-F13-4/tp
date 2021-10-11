package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Grade;
import seedu.address.model.person.Student;

public class EnrollCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final Lesson testLessonOne = new Lesson("Science",
            new Grade("P2"),
            DayOfWeek.WEDNESDAY,
            LocalTime.of(12, 30),
            0.0);
    private final Lesson testLessonTwo = new Lesson("Science",
            new Grade("P2"),
            DayOfWeek.WEDNESDAY,
            LocalTime.of(12, 30),
            0.0);

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validEnrollment_success() throws CommandException {
        Student studentToEnroll = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EnrollCommand enrollCommand = new EnrollCommand(INDEX_FIRST_PERSON, "Science-P2-Wed-1230");

        String expectedMessage = String.format(EnrollCommand.MESSAGE_SUCCESS, studentToEnroll.getName(), testLessonOne);

        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        testLessonTwo.addStudent(studentToEnroll);

        assertCommandSuccess(enrollCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        EnrollCommand enrollFirstCommand = new EnrollCommand(INDEX_FIRST_PERSON, "l/Science-P5-Wed-1230");
        EnrollCommand enrollSecondCommand = new EnrollCommand(INDEX_SECOND_PERSON, "l/Science-P5-Wed-1230");

        // same object -> returns true
        assertTrue(enrollFirstCommand.equals(enrollFirstCommand));

        // same values -> returns true
        EnrollCommand enrollFirstCommandCopy = new EnrollCommand(INDEX_FIRST_PERSON, "l/Science-P5-Wed-1230");
        assertTrue(enrollFirstCommand.equals(enrollFirstCommandCopy));

        // different types -> returns false
        assertFalse(enrollFirstCommand.equals(1));

        // null -> returns false
        assertFalse(enrollFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(enrollFirstCommand.equals(enrollSecondCommand));
    }
}
