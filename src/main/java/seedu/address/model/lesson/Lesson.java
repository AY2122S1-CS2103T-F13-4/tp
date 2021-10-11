package seedu.address.model.lesson;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import seedu.address.commons.util.StringUtil;
import seedu.address.model.person.Grade;
import seedu.address.model.person.Name;
import seedu.address.model.person.Student;

/**
 * Represents a Lesson in the tuitiONE book.
 * A lesson spans for an hour.
 * Guarantees: immutable; subject is valid as declared in {@link #isValidSubject(String)},
 * start and end times are valid as declared in {@link #isValidTime(LocalTime)}
 * and {@link #isValidPrice(double)}
 */
public class Lesson {

    public static final String SUBJECT_VALIDATION_REGEX = "\\p{Alnum}+";

    public static final String TIME_MESSAGE_CONSTRAINTS = "Lesson can only start be between 9 am to 8 pm";
    public static final String PRICE_MESSAGE_CONSTRAINT = "Price cannot be 0 or negative";
    public static final String CODE_MESSAGE_CONSTRAINT = "Lesson code should be of correct format";
    public static final String DAY_MESSAGE_CONSTRAINT = "Day specified is not legitimate";
    public static final String ENROLLMENT_MESSAGE_CONSTRAINT = "Student is unable to enroll for this lesson";
    public static final String STUDENT_NOT_ENROLLED = "%1$s is not enrolled for %2$s";
    public static final String SUBJECT_MESSAGE_CONSTRAINTS = "Subject names should be alphanumeric and"
            + "within %1$d characters";

    public static final long LESSON_PERIOD_IN_HOURS = 2;
    public static final LocalTime BOUNDED_START_TIME = LocalTime.of(9, 0);
    public static final LocalTime BOUNDED_END_TIME = LocalTime.of(21, 0).minusHours(LESSON_PERIOD_IN_HOURS);
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmm");
    public static final int MAXIMUM_SUBJECT_LENGTH = 20;

    private final String subject;
    private final Grade grade;
    private final DayOfWeek dayOfWeek;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final double price;
    private final List<Student> students;

    /**
     * Constructs a {@code Lesson}.
     *
     * @param subject A valid lesson name.
     * @param grade Grade of lesson.
     * @param dayOfWeek A day of the week.
     * @param startTime A valid start time.
     * @param price Price of lesson.
     */
    public Lesson(String subject, Grade grade, DayOfWeek dayOfWeek, LocalTime startTime, double price) {
        requireAllNonNull(subject, grade, dayOfWeek, startTime, price);

        checkArgument(isValidSubject(subject), String.format(SUBJECT_MESSAGE_CONSTRAINTS, MAXIMUM_SUBJECT_LENGTH));
        checkArgument(isValidTime(startTime), TIME_MESSAGE_CONSTRAINTS);
        checkArgument(isValidPrice(price), PRICE_MESSAGE_CONSTRAINT);

        this.subject = subject;
        this.grade = grade;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = startTime.plusHours(LESSON_PERIOD_IN_HOURS);
        this.price = price;
        this.students = new ArrayList<>();
    }

    /**
     * Constructs a {@code Lesson} using a lesson code.
     * Acts as an overloaded constructor for easy loading from Storage.
     *
     * @param lessonCode A valid lesson code.
     * @param price Price of lesson.
     */
    public static Lesson createFromCodeAndPrice(String lessonCode, double price) {
        requireAllNonNull(lessonCode, price);

        checkArgument(isValidLessonCode(lessonCode), CODE_MESSAGE_CONSTRAINT);

        String[] parameters = lessonCode.split("-");
        String subject = StringUtil.capitalize(parameters[0]);
        Grade grade = new Grade(parameters[1]);
        DayOfWeek day = parseStringToDayOfWeek(parameters[2]);
        LocalTime startTime = LocalTime.parse(parameters[3], TIME_FORMATTER);

        return new Lesson(subject, grade, day, startTime, price);
    }

    /**
     * Returns the subject of lesson.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Returns the grade of lesson.
     */
    public Grade getGrade() {
        return grade;
    }

    /**
     * Returns the day of lesson.
     */
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * Returns the start time of lesson.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Returns the end time of lesson.
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Returns the price of lesson.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns formatted lesson code string, e.g. Science-P5-Wed-1430.
     */
    public String getLessonCode() {
        return String.format(
                "%1$s-%2$s-%3$s-%4$s",
                subject,
                grade.value,
                parseDayToString(dayOfWeek),
                startTime.format(TIME_FORMATTER));
    }

    /**
     * Returns true if both lessons have the same lesson code.
     * This defines a weaker notion of equality between two lessons.
     */
    public boolean isSameLesson(Lesson otherLesson) {
        if (otherLesson == this) {
            return true;
        }
        return (otherLesson != null)
                && (otherLesson.getLessonCode().equals(getLessonCode()));
    }

    /**
     * Returns an immutable student set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    /**
     * Returns an unmodifiable set of student names for equality checks.
     */
    public List<Name> getStudentNames() {
        return students.stream().map(Student::getName).collect(Collectors.toUnmodifiableList());
    }

    /**
     * Returns true if a student is eligible to enroll for the lesson.
     * A student must be of the same grade, must be available for the time slot,
     * and must not be already enrolled to the lesson.
     */
    public boolean isAbleToEnroll(Student student) {
        if (student == null) {
            return false;
        }
        if (containsStudent(student)) {
            return false;
        }
        if (!student.getGrade().equals(grade)) {
            return false;
        }
        for (Lesson otherLesson : student.getLessons()) {
            if (hasOverlappedTiming(otherLesson)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if a student is eligible to unenroll from the lesson.
     * A student must be already enrolled to the lesson.
     * @param student the student to unenroll from this lesson.
     * @return a boolean to represent whether the student can be unenrolled from the lesson.
     */
    public boolean isAbleToUnenroll(Student student) {
        requireNonNull(student);
        if (containsStudent(student)) {
            return true;
        }
        return false;
    }


    /**
     * Returns true if the lessons overlap in timing and day.
     */
    public boolean hasOverlappedTiming(Lesson otherLesson) {
        LocalTime otherStartTime = otherLesson.getStartTime();
        LocalTime otherEndTime = otherLesson.getEndTime();

        boolean isClashingStartTime = (otherStartTime.isAfter(startTime) && otherStartTime.isBefore(endTime));
        boolean isClashingEndTime = (otherEndTime.isAfter(startTime) && otherEndTime.isBefore(endTime));
        boolean isSameTiming = (otherStartTime.equals(startTime)); // end time implied as same due to fixed timing
        boolean isOnSameDay = otherLesson.getDayOfWeek().equals(dayOfWeek);

        if (!isOnSameDay) {
            return false;
        }
        return (isClashingStartTime || isClashingEndTime || isSameTiming);
    }

    /**
     * Checks if Student is enrolled in this Lesson
     */
    public boolean containsStudent(Student student) {
        return getStudentNames().contains(student.getName());
    }

    /**
     * Returns the number of students attending the lesson.
     */
    public int getLessonSize() {
        return students.size();
    }

    /**
     * Add student to the lesson instance.
     */
    public void addStudent(Student student) {
        requireNonNull(student);
        checkArgument(isAbleToEnroll(student), ENROLLMENT_MESSAGE_CONSTRAINT);
        students.add(student);
        student.enrollForLesson(this);
    }

    /**
     * Removes student from the lesson instance.
     */
    public void removeStudent(Student student) {
        requireNonNull(student);
        checkArgument(isAbleToUnenroll(student), String.format(STUDENT_NOT_ENROLLED, student, this));
        students.remove(student);
        student.unenrollFromLesson(this);
    }

    /**
     * Returns true if a given string is a valid subject name for a lesson.
     */
    public static boolean isValidSubject(String testSubject) {
        if (testSubject == null) {
            return false;
        }
        return testSubject.matches(SUBJECT_VALIDATION_REGEX)
                && (testSubject.length() <= MAXIMUM_SUBJECT_LENGTH)
                && (!testSubject.isEmpty());
    }

    /**
     * Returns true if a given timings are within bounded limits.
     */
    public static boolean isValidTime(LocalTime testStart) {
        if (testStart == null) {
            return false;
        }
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
        if (testLessonParams.length != 4) {
            return false;
        }
        if (!isValidSubject(testLessonParams[0]) || !Grade.isValidGrade(testLessonParams[1])) {
            return false;
        }
        try {
            parseStringToDayOfWeek(testLessonParams[2]);
            LocalTime.parse(testLessonParams[3], TIME_FORMATTER);
        } catch (IllegalArgumentException | DateTimeParseException e) {
            return false;
        }
        return true;
    }

    /**
     * Parses a {@code String day} into {@code DayOfWeek}.
     */
    public static DayOfWeek parseStringToDayOfWeek(String day) {
        requireNonNull(day);
        switch (day) {
        case "Mon":
            return DayOfWeek.MONDAY;
        case "Tue":
            return DayOfWeek.TUESDAY;
        case "Wed":
            return DayOfWeek.WEDNESDAY;
        case "Thu":
            return DayOfWeek.THURSDAY;
        case "Fri":
            return DayOfWeek.FRIDAY;
        case "Sat":
            return DayOfWeek.SATURDAY;
        case "Sun":
            return DayOfWeek.SUNDAY;
        default:
            throw new IllegalArgumentException(DAY_MESSAGE_CONSTRAINT);
        }
    }

    /**
     * Returns String with corresponding to DayOfWeek.
     */
    public static String parseDayToString(DayOfWeek dayOfWeek) {
        requireNonNull(dayOfWeek);
        return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault());
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
                && dayOfWeek.equals(otherLesson.dayOfWeek)
                && startTime.equals(otherLesson.startTime)
                && (price == otherLesson.price)
                && getStudentNames().equals(otherLesson.getStudentNames());
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, grade, dayOfWeek, startTime, price, getStudentNames());
    }

    @Override
    public String toString() {
        return getLessonCode();
    }
}
