package seedu.tuitione.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.tuitione.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.tuitione.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.tuitione.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.tuitione.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.tuitione.logic.commands.CommandTestUtil.VALID_REMARK_HUSBAND;
import static seedu.tuitione.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.tuitione.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.tuitione.logic.commands.CommandTestUtil.showStudentAtIndex;
import static seedu.tuitione.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static seedu.tuitione.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static seedu.tuitione.testutil.TypicalTuition.MATH_S2;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.tuitione.commons.core.Messages;
import seedu.tuitione.commons.core.index.Index;
import seedu.tuitione.logic.commands.EditCommand.EditStudentDescriptor;
import seedu.tuitione.model.Model;
import seedu.tuitione.model.ModelManager;
import seedu.tuitione.model.Tuitione;
import seedu.tuitione.model.UserPrefs;
import seedu.tuitione.model.student.Student;
import seedu.tuitione.testutil.EditStudentDescriptorBuilder;
import seedu.tuitione.testutil.StudentBuilder;
import seedu.tuitione.testutil.TypicalStudents;
import seedu.tuitione.testutil.TypicalTuition;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    // for unit testing
    private Model modelWithOnlyStudents = new ModelManager(TypicalStudents.getTypicalTuitione(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Student editedStudent = new StudentBuilder().build();
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder(editedStudent).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_STUDENT, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_STUDENT_SUCCESS, editedStudent);

        Model expectedModel = new ModelManager(new Tuitione(modelWithOnlyStudents.getTuitione()), new UserPrefs());
        expectedModel.setStudent(modelWithOnlyStudents.getFilteredStudentList().get(0), editedStudent);

        assertCommandSuccess(editCommand, modelWithOnlyStudents, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastStudent = Index.fromOneBased(modelWithOnlyStudents.getFilteredStudentList().size());
        Student lastStudent = modelWithOnlyStudents.getFilteredStudentList().get(indexLastStudent.getZeroBased());

        StudentBuilder studentInList = new StudentBuilder(lastStudent);
        Student editedStudent = studentInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withRemarks(VALID_REMARK_HUSBAND).build();

        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withRemarks(VALID_REMARK_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastStudent, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_STUDENT_SUCCESS, editedStudent);

        Model expectedModel = new ModelManager(new Tuitione(modelWithOnlyStudents.getTuitione()), new UserPrefs());
        expectedModel.setStudent(lastStudent, editedStudent);

        assertCommandSuccess(editCommand, modelWithOnlyStudents, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_STUDENT, new EditStudentDescriptor());
        Student editedStudent = modelWithOnlyStudents.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_STUDENT_SUCCESS, editedStudent);

        Model expectedModel = new ModelManager(new Tuitione(modelWithOnlyStudents.getTuitione()), new UserPrefs());

        assertCommandSuccess(editCommand, modelWithOnlyStudents, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        Student studentInFilteredList = modelWithOnlyStudents.getFilteredStudentList()
                .get(INDEX_FIRST_STUDENT.getZeroBased());
        Student editedStudent = new StudentBuilder(studentInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_STUDENT,
                new EditStudentDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_STUDENT_SUCCESS, editedStudent);

        Model expectedModel = new ModelManager(new Tuitione(modelWithOnlyStudents.getTuitione()), new UserPrefs());
        expectedModel.setStudent(modelWithOnlyStudents.getFilteredStudentList().get(0), editedStudent);

        assertCommandSuccess(editCommand, modelWithOnlyStudents, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateStudentUnfilteredList_failure() {
        Student firstStudent = modelWithOnlyStudents.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder(firstStudent).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_STUDENT, descriptor);

        assertCommandFailure(editCommand, modelWithOnlyStudents, EditCommand.MESSAGE_DUPLICATE_STUDENT);
    }

    @Test
    public void execute_duplicateStudentFilteredList_failure() {
        showStudentAtIndex(modelWithOnlyStudents, INDEX_FIRST_STUDENT);

        // edit student in filtered list into a duplicate in tuitione book
        Student studentInList = modelWithOnlyStudents.getTuitione().getStudentList()
                .get(INDEX_SECOND_STUDENT.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_STUDENT,
                new EditStudentDescriptorBuilder(studentInList).build());

        assertCommandFailure(editCommand, modelWithOnlyStudents, EditCommand.MESSAGE_DUPLICATE_STUDENT);
    }

    @Test
    public void execute_invalidStudentIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(modelWithOnlyStudents.getFilteredStudentList().size() + 1);
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, modelWithOnlyStudents, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of tuitione book
     */
    @Test
    public void execute_invalidStudentIndexFilteredList_failure() {
        showStudentAtIndex(modelWithOnlyStudents, INDEX_FIRST_STUDENT);
        Index outOfBoundIndex = INDEX_SECOND_STUDENT;
        // ensures that outOfBoundIndex is still in bounds of tuitione book list
        assertTrue(outOfBoundIndex.getZeroBased() < modelWithOnlyStudents.getTuitione().getStudentList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditStudentDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, modelWithOnlyStudents, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_STUDENT, DESC_AMY);

        // same values -> returns true
        EditStudentDescriptor copyDescriptor = new EditStudentDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_STUDENT, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_STUDENT, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_STUDENT, DESC_BOB)));
    }

    @Test
    public void execute_gradeFieldChangedStudentUnenrolledFromLesson_success() {
        // for integration testing
        Model modelWithStudentsAndLessons = new ModelManager(TypicalTuition.getTypicalTuitione(), new UserPrefs());

        // get student BENSON
        Student secondStudent = modelWithStudentsAndLessons
                .getFilteredStudentList().get(INDEX_SECOND_STUDENT.getZeroBased());

        StudentBuilder studentInList = new StudentBuilder(secondStudent);
        Student editedStudent = studentInList.withGrade("S3").build();

        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder(secondStudent).withGrade("S3").build();
        descriptor.setGradeIsEdited(true);
        EditCommand editCommand = new EditCommand(INDEX_SECOND_STUDENT, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_STUDENT_SUCCESS, editedStudent);

        Model expectedModel =
                new ModelManager(new Tuitione(modelWithStudentsAndLessons.getTuitione()), new UserPrefs());
        expectedModel.setStudent(secondStudent, editedStudent);

        assertCommandSuccess(editCommand, modelWithStudentsAndLessons, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), modelWithStudentsAndLessons.getFilteredLessonList().get(1).getStudents());
        assertEquals(Collections.emptyList(), editedStudent.getLessons());

    }

    @Test
    public void execute_gradeFieldNotChangedStudentStillEnrolledForLesson_success() {
        // for integration testing
        Model modelWithStudentsAndLessons = new ModelManager(TypicalTuition.getTypicalTuitione(), new UserPrefs());

        // get student BENSON
        Student secondStudent = modelWithStudentsAndLessons
                .getFilteredStudentList().get(INDEX_SECOND_STUDENT.getZeroBased());

        StudentBuilder studentInList = new StudentBuilder(secondStudent);
        Student editedStudent = studentInList.withName("Ben").build();
        editedStudent.addLesson(MATH_S2);

        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder(secondStudent).withName("Ben").build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_STUDENT, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_STUDENT_SUCCESS, editedStudent);

        Model expectedModel =
                new ModelManager(new Tuitione(modelWithStudentsAndLessons.getTuitione()), new UserPrefs());
        expectedModel.setStudent(secondStudent, editedStudent);


        assertCommandSuccess(editCommand, modelWithStudentsAndLessons, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(editedStudent),
                modelWithStudentsAndLessons.getFilteredLessonList().get(1).getStudents());
        assertEquals(Arrays.asList(MATH_S2), editedStudent.getLessons());

    }



}
