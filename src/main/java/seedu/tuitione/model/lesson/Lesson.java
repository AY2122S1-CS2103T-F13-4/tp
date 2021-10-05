package seedu.tuitione.model.lesson;

import static seedu.tuitione.commons.util.AppUtil.checkArgument;
import static seedu.tuitione.commons.util.CollectionUtil.requireAllNonNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import seedu.tuitione.model.person.Grade;
import seedu.tuitione.model.person.Person;

/**
 * Represents a Lesson in the tuitiONE book.
 * A lesson spans for an hour.
 * Guarantees: immutable; subject is valid as declared in {@link #isValidLessonName(String)},
 * start and end times are valid as declared in {@link #isValidTime(LocalTime)}
 * and {@link #isValidPrice(double)}
 */
public class Lesson {

    public static final String SUBJECT_VALIDATION_REGEX = "\\p{Alnum}+";
    public static final String SUBJECT_MESSAGE_CONSTRAINTS = "Subject names should be alphanumeric";
    public static final String TIME_MESSAGE_CONSTRAINTS = "Lesson can only start be between 9 am to 8 pm";
    public static final String PRICE_MESSAGE_CONSTRAINT = "Price cannot be 0 or negative";
    public static final String CODE_MESSAGE_CONSTRAINT = "Lesson code should be of correct format";

    public static final LocalTime BOUNDED_START_TIME = LocalTime.parse("09:00");
    public static final LocalTime BOUNDED_END_TIME = LocalTime.parse("20:00");

    private final String subject;
    private final Grade grade;
    private final DayOfWeek day;
    private final LocalTime startTime;
    private final double price;
    private final Set<Person> students; // todo consider linking students to classes

    /**
     * Constructs a {@code Lesson}.
     *
     * @param subject A valid lesson name.
     * @param grade Grade of lesson.
     * @param day A day of the week.
     * @param startTime A valid start time.
     * @param price Price of lesson.
     */
    public Lesson(String subject, Grade grade, DayOfWeek day, LocalTime startTime, double price) {
        requireAllNonNull(subject, grade, day, startTime, price);

        checkArgument(isValidLessonName(subject), SUBJECT_MESSAGE_CONSTRAINTS);
        checkArgument(isValidTime(startTime), TIME_MESSAGE_CONSTRAINTS);
        checkArgument(isValidPrice(price), PRICE_MESSAGE_CONSTRAINT);

        this.subject = subject;
        this.grade = grade;
        this.day = day;
        this.startTime = startTime;
        this.price = price;
        this.students = new HashSet<>();
    }

    public String getSubject() {
        return subject;
    }

    public Grade getGrade() {
        return grade;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public double getPrice() {
        return price;
    }

    /**
     * Returns an immutable student set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Person> getStudents() {
        return Collections.unmodifiableSet(students);
    }

    /**
     * Returns true if a given string is a valid lesson name.
     */
    public static boolean isValidLessonName(String test) {
        return test.matches(SUBJECT_VALIDATION_REGEX);
    }

    /**
     * Returns true if a given timings are within bounded limits.
     */
    public static boolean isValidTime(LocalTime testStart) {
        return testStart.equals(BOUNDED_START_TIME)
                || testStart.equals(BOUNDED_END_TIME)
                || (testStart.isAfter(BOUNDED_START_TIME) && testStart.isBefore(BOUNDED_END_TIME));
    }

    /**
     * Returns true if a given price is at least 0.
     */
    public static boolean isValidPrice(double testPrice) {
        return (testPrice >= 0.0);
    }

    /**
     * Returns true if a given lesson code is follows the correct format.
     */
    public static boolean isValidLessonCode(String testCode) {
        if (testCode == null) {
            return false;
        }

        // check number of parameters in lesson code
        String[] testLessonParams = testCode.split("-");
        if (testLessonParams.length == 4 || testLessonParams[1].length() == 2) {
            return false;
        }

        try {
            // attempt to parse
            Integer.parseInt("" + testLessonParams[1].charAt(1));
            DayOfWeek.valueOf(testLessonParams[2].toUpperCase(Locale.ROOT));
            LocalTime.parse(testLessonParams[3]);

        } catch (IllegalArgumentException | DateTimeParseException e) {
            return false;
        }
        return true;
    }

    /**
     * Returns formatted lesson code string.
     */
    public String getLessonCode() {
        return String.format("%s-%s-%s-%s", subject, grade, day, startTime);
    }

    /**
     * Returns true if both lessons have the same lesson code.
     * This defines a weaker notion of equality between two lessons.
     */
    public boolean isSameLesson(Lesson otherLesson) {
        if (otherLesson == this) {
            return true;
        }
        return otherLesson != null
                && otherLesson.getLessonCode().equals(getLessonCode());
    }

    /**
     * Returns true if instance lesson code and requested lesson code are the same.
     */
    public boolean isSameLessonUsingCode(String otherLessonCode) {
        checkArgument(isValidLessonCode(otherLessonCode), CODE_MESSAGE_CONSTRAINT);
        return getLessonCode().equals(otherLessonCode);
    }

    /**
     * Returns a weak form of lesson from a given lesson code
     * for weak equality check @link #isSameLesson(Lesson).
     */
    public static Lesson getWeakLessonFromCode(String code) {
        checkArgument(isValidLessonCode(code), CODE_MESSAGE_CONSTRAINT);
        String[] lessonParams = code.split("-");

        // extract and parse relevant fields for a lesson instance
        String subject = lessonParams[0];
        Grade grade = new Grade("" + lessonParams[1].charAt(0),
                Integer.parseInt("" + lessonParams[1].charAt(1)));
        DayOfWeek day = DayOfWeek.valueOf(lessonParams[2].toUpperCase(Locale.ROOT));
        LocalTime startTime = LocalTime.parse(lessonParams[3]);
        double price = 0.0; // mock value

        return new Lesson(subject, grade, day, startTime, price);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof Lesson)) {
            return false;
        }
        // state check
        Lesson otherLesson = (Lesson) other;
        return subject.equals(otherLesson.subject)
                && grade.equals(otherLesson.grade)
                && day.equals(otherLesson.day)
                && startTime.equals(otherLesson.startTime)
                && (price == otherLesson.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, grade, day, startTime, price);
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + getLessonCode() + ']';
    }

}
