package seedu.address.model.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.lesson.Lesson.MAXIMUM_SUBJECT_LENGTH;
import static seedu.address.model.lesson.Lesson.createFromCodeAndPrice;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.LessonBuilder.DEFAULT_LESSON_CODE;
import static seedu.address.testutil.LessonBuilder.DEFAULT_PRICE;

import java.time.DayOfWeek;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.person.Student;
import seedu.address.testutil.LessonBuilder;
import seedu.address.testutil.PersonBuilder;

public class LessonTest {

    private static final String[] DAY_SHORT_FORMS = new String[] {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    private static Lesson defaultLesson = LessonBuilder.getDefault();

    @BeforeEach
    public void setUp() {
        // reset defaultLesson
        defaultLesson = LessonBuilder.getDefault();
    }

    //// constructor

    @Test
    public void constructor_null_throwsNullPointerException() {
        // basic constructor
        assertThrows(NullPointerException.class, () -> new Lesson(null, null, null, null, 0.0));
        // constructor based on lesson code
        assertThrows(NullPointerException.class, () -> createFromCodeAndPrice(null, 0.0));
    }

    @Test
    public void createLessonFromCode() {
        Lesson createdLesson = Lesson.createFromCodeAndPrice(DEFAULT_LESSON_CODE, DEFAULT_PRICE);
        assertEquals(defaultLesson, createdLesson);
    }

    //// local methods

    @Test
    public void getLessonCode() {
        assertEquals(DEFAULT_LESSON_CODE, defaultLesson.getLessonCode());
    }

    @Test
    public void isSameLesson() {
        // this -> true
        assertTrue(defaultLesson.isSameLesson(defaultLesson));

        // null -> false
        assertFalse(defaultLesson.isSameLesson(null));

        // new instance with same (default) lesson details -> true
        Lesson otherLesson = new LessonBuilder().build();
        assertTrue(defaultLesson.isSameLesson(otherLesson));

        // different lesson code -> false
        Lesson differentLesson = new LessonBuilder().withSubject("Different").build();
        assertFalse(defaultLesson.isSameLesson(differentLesson));
    }

    @Test
    public void isAbleToEnroll() {
        // null -> false
        assertFalse(defaultLesson.isAbleToEnroll(null));

        // eligible student -> true
        Student eligibleStudent = new PersonBuilder().build();
        assertTrue(defaultLesson.isAbleToEnroll(eligibleStudent));

        // different grade student -> false
        Student differentGradeStudent = new PersonBuilder().withGrade("P5").build();
        assertFalse(defaultLesson.isAbleToEnroll(differentGradeStudent));

        // student with clashing timing -> false
        Student busyStudent = new PersonBuilder().build();
        Lesson clashingLesson = LessonBuilder.getDefault();
        clashingLesson.addStudent(busyStudent);
        assertFalse(defaultLesson.isAbleToEnroll(busyStudent));

        // student already enrolled -> false
        Student enrolledStudent = new PersonBuilder().build();
        defaultLesson.addStudent(enrolledStudent);
        assertFalse(defaultLesson.isAbleToEnroll(enrolledStudent));
    }

    @Test
    public void hasOverlappedTiming() {
        // exact same timing -> true
        Lesson clashingLesson = LessonBuilder.getDefault();
        assertTrue(defaultLesson.hasOverlappedTiming(clashingLesson));

        // different day -> false
        Lesson otherDayLesson = new LessonBuilder().withDayOfWeek(DayOfWeek.MONDAY).build();
        assertFalse(defaultLesson.hasOverlappedTiming(otherDayLesson));

        // start timing clashes -> true
        Lesson startTimeClashLesson = new LessonBuilder().withStartTime(16, 0).build();
        assertTrue(defaultLesson.hasOverlappedTiming(startTimeClashLesson));

        // end timing clashes -> true
        Lesson endTimeClashLesson = new LessonBuilder().withStartTime(14, 0).build();
        assertTrue(defaultLesson.hasOverlappedTiming(endTimeClashLesson));

        // same day and no clash in timing -> false
        Lesson differentTimingLesson = new LessonBuilder().withStartTime(13, 0).build();
        assertFalse(defaultLesson.hasOverlappedTiming(differentTimingLesson));
    }

    @Test
    public void containsStudent() {
        Student enrolledStudent = new PersonBuilder().build();

        // student not present
        assertFalse(defaultLesson.containsStudent(enrolledStudent));

        // student present
        defaultLesson.addStudent(enrolledStudent);
        assertTrue(defaultLesson.containsStudent(enrolledStudent));
    }

    @Test
    public void removeStudent() {
        Student toRemove = new PersonBuilder().build();
        // student not present
        String notPresentMessage = String.format(Lesson.STUDENT_NOT_ENROLLED, toRemove, defaultLesson);
        assertThrows(IllegalArgumentException.class, notPresentMessage, () -> defaultLesson.removeStudent(toRemove));
        assertEquals(0, defaultLesson.getLessonSize());

        // student present and to remove
        defaultLesson.addStudent(toRemove);
        assertEquals(1, defaultLesson.getLessonSize());
        assertEquals(1, toRemove.getLessons().size());

        defaultLesson.removeStudent(toRemove);
        assertEquals(0, defaultLesson.getLessonSize());
    }

    //// static methods

    @Test
    public void isValidSubject() {
        // valid string
        String valid = "Maths";
        assertTrue(Lesson.isValidSubject(valid));

        // non-alphanumeric string
        String nonAlphanumeric = "~12P";
        assertFalse(Lesson.isValidSubject(nonAlphanumeric));

        // empty string
        String empty = "";
        assertFalse(Lesson.isValidSubject(empty));

        // lengthy string
        StringBuilder lengthyBuilder = new StringBuilder();
        IntStream.range(0, MAXIMUM_SUBJECT_LENGTH + 1)
                .forEach(num -> lengthyBuilder.append('a'));
        assertFalse(Lesson.isValidSubject(lengthyBuilder.toString()));

        // null string
        assertFalse(Lesson.isValidSubject(null));
    }

    @Test
    public void isValidPrice() {
        // negative price
        assertFalse(Lesson.isValidPrice(-0.9));
        // valid (non-negative) price
        assertTrue(Lesson.isValidPrice(0.9));
        // free lesson
        assertTrue(Lesson.isValidPrice(0));
    }

    @Test
    public void isValidLessonCode() {
        // null code
        assertFalse(Lesson.isValidLessonCode(null));

        // valid code
        assertTrue(Lesson.isValidLessonCode(DEFAULT_LESSON_CODE));

        // invalid code
        String[] invalidCodes = new String[] {
            "",
            "S1-Wed-1500",
            "Science-S1-Wed-15:00",
            "Science-S1-Wednesday-15:00"
        };
        for (String code : invalidCodes) {
            assertFalse(Lesson.isValidLessonCode(code));
        }
    }

    @Test
    public void parseStringToDayOfWeek() {
        // null
        assertThrows(NullPointerException.class, () -> Lesson.parseStringToDayOfWeek(null));

        // invalid
        String invalid = "";
        assertThrows(IllegalArgumentException.class, () -> Lesson.parseStringToDayOfWeek(invalid));

        // valid
        for (int idx = 0; idx < DAY_SHORT_FORMS.length; idx++) {
            assertEquals(DayOfWeek.of(idx + 1), Lesson.parseStringToDayOfWeek(DAY_SHORT_FORMS[idx]));
        }
    }

    @Test
    public void parseDayToString() {
        // null
        assertThrows(NullPointerException.class, () -> Lesson.parseDayToString(null));

        // valid
        for (int idx = 0; idx < DAY_SHORT_FORMS.length; idx++) {
            assertEquals(DAY_SHORT_FORMS[idx], Lesson.parseDayToString(DayOfWeek.of(idx + 1)));
        }
    }

    @Test
    public void equals() {
        // this
        assertEquals(defaultLesson, defaultLesson);

        // same lesson but different instance
        Lesson sameLesson = LessonBuilder.getDefault();
        assertEquals(defaultLesson, sameLesson);

        // null
        assertNotEquals(defaultLesson, null);

        // instance of but different params
        Lesson otherLesson = new LessonBuilder().withGrade("S3").build();
        assertNotEquals(defaultLesson, otherLesson);

        // not Lesson
        assertNotEquals(defaultLesson, new Object());
    }
}
