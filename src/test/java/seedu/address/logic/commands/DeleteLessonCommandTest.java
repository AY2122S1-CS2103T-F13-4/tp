package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_LESSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_LESSON;
import static seedu.address.testutil.TypicalLessons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.lesson.Lesson;

public class DeleteLessonCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Lesson lessonToDelete = model.getFilteredLessonList().get(INDEX_FIRST_LESSON.getZeroBased());
        DeleteLessonCommand deleteLessonCommand = new DeleteLessonCommand(INDEX_FIRST_LESSON);

        String expectedMessage = String.format(DeleteLessonCommand.MESSAGE_DELETE_LESSON_SUCCESS, lessonToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteLesson(lessonToDelete);

        assertCommandSuccess(deleteLessonCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredLessonList().size() + 1);
        DeleteLessonCommand deleteLessonCommand = new DeleteLessonCommand(outOfBoundIndex);

        assertCommandFailure(deleteLessonCommand, model, Messages.MESSAGE_INVALID_LESSON_DISPLAYED_INDEX);
    }

    // Wait till v1.3 when find lesson feature implemented, to implement execute_validIndexFilteredList_success() test


    // Wait till v1.3 when find lesson feature implemented, to implement
    // execute_invalidIndexFilteredList_throwsCommandException() test


    @Test
    public void equals() {
        DeleteLessonCommand deleteLessonFirstCommand = new DeleteLessonCommand(INDEX_FIRST_LESSON);
        DeleteLessonCommand deleteLessonSecondCommand = new DeleteLessonCommand(INDEX_SECOND_LESSON);

        // same object -> returns true
        assertTrue(deleteLessonFirstCommand.equals(deleteLessonFirstCommand));

        // same values -> returns true
        DeleteLessonCommand deleteLessonFirstCommandCopy = new DeleteLessonCommand(INDEX_FIRST_LESSON);
        assertTrue(deleteLessonFirstCommand.equals(deleteLessonFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteLessonFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteLessonFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteLessonFirstCommand.equals(deleteLessonSecondCommand));
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoLesson(Model model) {
        model.updateFilteredLessonList(p -> false);
        assertTrue(model.getFilteredLessonList().isEmpty());
    }
}
