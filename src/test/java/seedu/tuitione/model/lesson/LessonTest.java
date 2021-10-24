package seedu.tuitione.model.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.tuitione.testutil.Assert.assertThrows;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.tuitione.model.student.Student;
import seedu.tuitione.testutil.LessonBuilder;
import seedu.tuitione.testutil.StudentBuilder;

public class LessonTest {

    private Lesson defaultLesson;

    @BeforeEach
    public void setUp() {
        defaultLesson = LessonBuilder.getDefault(); // reset defaultLesson
    }

    @Test
    public void constructor_validSubject_returnsSubject() {
        Lesson sameLesson = LessonBuilder.getDefault();
        assertEquals(defaultLesson, sameLesson);
    }

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Lesson(null, null, null, null));
    }

    @Test
    public void isSameLesson() {
        // this -> true
        assertTrue(defaultLesson.isSameLesson(defaultLesson));

        // new instance with same (default) lesson details -> true
        Lesson otherLesson = new LessonBuilder().build();
        assertTrue(defaultLesson.isSameLesson(otherLesson));

        // different lesson code -> false
        Lesson differentLesson = new LessonBuilder().withSubject("Different").build(); // change subject to change code
        assertFalse(defaultLesson.isSameLesson(differentLesson));
    }

    @Test
    public void isAbleToEnroll() {
        // null -> false
        assertFalse(defaultLesson.isAbleToEnroll(null));

        // eligible student -> true
        Student eligibleStudent = new StudentBuilder().build();
        assertTrue(defaultLesson.isAbleToEnroll(eligibleStudent));

        // different grade student -> false
        Student differentGradeStudent = new StudentBuilder().withGrade("P5").build();
        assertFalse(defaultLesson.isAbleToEnroll(differentGradeStudent));

        // student with clashing timing -> false
        Student busyStudent = new StudentBuilder().build();
        Lesson clashingLesson = LessonBuilder.getDefault();
        clashingLesson.enrollStudent(busyStudent);
        assertFalse(defaultLesson.isAbleToEnroll(busyStudent));

        // student already enrolled -> false
        Student enrolledStudent = new StudentBuilder().build();
        defaultLesson.enrollStudent(enrolledStudent);
        assertFalse(defaultLesson.isAbleToEnroll(enrolledStudent));
    }

    @Test
    public void isAbleToUnenroll() {
        Student student = new StudentBuilder().withGrade(defaultLesson.getGrade().value).build();
        // student is not enrolled
        assertFalse(defaultLesson.isAbleToUnenroll(student));

        // enroll and test
        defaultLesson.enrollStudent(student);
        assertTrue(defaultLesson.isAbleToUnenroll(student));
    }

    @Test
    public void containsStudent() {
        Student enrolledStudent = new StudentBuilder().build();

        // student not present
        assertFalse(defaultLesson.containsStudent(enrolledStudent));

        // student present
        defaultLesson.enrollStudent(enrolledStudent);
        assertTrue(defaultLesson.containsStudent(enrolledStudent));
    }

    @Test
    public void addStudent() {
        Student student = new StudentBuilder().withGrade(defaultLesson.getGrade().value).build();
        defaultLesson.enrollStudent(student);

        assertEquals(1, defaultLesson.getStudents().size());
        assertTrue(defaultLesson.containsStudent(student));

        assertEquals(1, student.getLessons().size());
        assertTrue(student.getLessons().contains(defaultLesson));
    }

    @Test
    public void updateStudent() {
        StudentBuilder sb = new StudentBuilder().withGrade(defaultLesson.getGrade().value);
        Student student = sb.build();
        defaultLesson.enrollStudent(student);

        // mock we edit a student details (i.e. name)
        Student editedStudent = sb.withName("Edited Name Here").build();
        defaultLesson.updateStudent(student, editedStudent);
        assertTrue(editedStudent.containsLesson(defaultLesson));
        assertFalse(student.containsLesson(defaultLesson));

        assertEquals(1, defaultLesson.getStudents().size());
        assertEquals(defaultLesson.getStudents().get(0), editedStudent);
    }

    @Test
    public void removeStudent() {
        Student toRemove = new StudentBuilder().build();
        // student not present
        String notPresentMessage = String.format(Lesson.STUDENT_NOT_ENROLLED, toRemove.getName(), defaultLesson);
        assertThrows(IllegalArgumentException.class, notPresentMessage, () -> defaultLesson.unenrollStudent(toRemove));
        assertEquals(0, defaultLesson.getLessonSize());

        // student present and to remove
        defaultLesson.enrollStudent(toRemove);
        assertEquals(1, defaultLesson.getLessonSize());

        defaultLesson.unenrollStudent(toRemove);
        assertEquals(0, defaultLesson.getLessonSize());
    }

    @Test
    public void equals() {
        // this -> true
        assertEquals(defaultLesson, defaultLesson);

        // null -> false
        assertNotEquals(defaultLesson, null);

        // different instance -> false
        assertNotEquals(defaultLesson, 5);

        // instance of but different params -> false
        Lesson differentSubjectLesson = new LessonBuilder().withSubject("English").build();
        assertNotEquals(defaultLesson, differentSubjectLesson);

        Lesson differentGradeLesson = new LessonBuilder().withGrade("S3").build();
        assertNotEquals(defaultLesson, differentGradeLesson);

        Lesson differentLessonTimeLesson = new LessonBuilder()
                .withLessonTime(new LessonTime(DayOfWeek.MONDAY, LocalTime.NOON)).build();
        assertNotEquals(defaultLesson, differentLessonTimeLesson);

        Lesson differentPriceLesson = new LessonBuilder().withPrice(13.9).build();
        assertNotEquals(defaultLesson, differentPriceLesson);

        Lesson lessonWithDifferentStudents = new LessonBuilder().build();
        lessonWithDifferentStudents.enrollStudent(
            new StudentBuilder().withGrade(defaultLesson.getGrade().value).build()
        );
        assertNotEquals(defaultLesson, lessonWithDifferentStudents);

        // same lesson but different instance
        Lesson sameLesson = LessonBuilder.getDefault();
        assertEquals(defaultLesson, sameLesson);
    }
}
