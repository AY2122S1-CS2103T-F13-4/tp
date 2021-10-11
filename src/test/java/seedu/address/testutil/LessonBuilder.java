package seedu.address.testutil;

import static seedu.address.model.lesson.Lesson.LESSON_PERIOD_IN_HOURS;
import static seedu.address.model.person.Grade.isValidGrade;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Grade;
import seedu.address.model.person.Student;

/**
 * A utility class to help with building Lesson objects.
 */
public class LessonBuilder {

    // default constants for test lessons
    public static final String DEFAULT_SUBJECT = "Science";
    public static final Grade DEFAULT_GRADE = new Grade("S1");
    public static final DayOfWeek DEFAULT_DAY_OF_WEEK = DayOfWeek.WEDNESDAY;
    public static final LocalTime DEFAULT_START_TIME = LocalTime.of(15, 0);
    public static final double DEFAULT_PRICE = 9.9;
    public static final LocalTime DEFAULT_END_TIME = DEFAULT_START_TIME.plusHours(LESSON_PERIOD_IN_HOURS);
    public static final String DEFAULT_LESSON_CODE = "Science-S1-Wed-1500";

    private String subject;
    private Grade grade;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private double price;
    private final List<Student> students;

    /**
     * Creates a {@code LessonBuilder} with the default details.
     */
    public LessonBuilder() {
        this.subject = DEFAULT_SUBJECT;
        this.grade = DEFAULT_GRADE;
        this.dayOfWeek = DEFAULT_DAY_OF_WEEK;
        this.startTime = DEFAULT_START_TIME;
        this.price = DEFAULT_PRICE;
        this.students = new ArrayList<>();
    }

    /**
     * Sets the {@code subject} of the {@code Lesson} that we are building.
     */
    public LessonBuilder withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    /**
     * Sets the {@code grade} of the {@code Lesson} that we are building.
     */
    public LessonBuilder withGrade(Grade grade) {
        this.grade = grade;
        return this;
    }

    /**
     * Sets the {@code grade} of the {@code Lesson} that we are building.
     * An overloaded method that takes the string grade value instead as parameter.
     */
    public LessonBuilder withGrade(String grade) {
        assert (isValidGrade(grade)) : "Grade to mock is invalid";
        this.grade = new Grade(grade);
        return this;
    }

    /**
     * Sets the {@code dayOfWeek} of the {@code Lesson} that we are building.
     */
    public LessonBuilder withDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        return this;
    }

    /**
     * Sets the {@code startTime} of the {@code Lesson} that we are building.
     */
    public LessonBuilder withStartTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    /**
     * Sets the {@code startTime} of the {@code Lesson} that we are building.
     * An overloaded method that takes the int hour and minutes instead as parameters.
     */
    public LessonBuilder withStartTime(int hour, int minutes) {
        assert (0 <= hour && hour < 24) && (0 <= minutes && minutes < 60) : "Start time to mock is invalid";
        this.startTime = LocalTime.of(hour, minutes);
        return this;
    }

    /**
     * Sets the {@code price} of the {@code Lesson} that we are building.
     */
    public LessonBuilder withPrice(double price) {
        this.price = price;
        return this;
    }

    /**
     * Sets the {@code students} of the {@code Lesson} that we are building.
     */
    public LessonBuilder withStudents(List<Student> students) {
        this.students.clear();
        this.students.addAll(students);
        return this;
    }

    /**
     * Creates a {@code Lesson} with the specified details.
     */
    public Lesson build() {
        Lesson lesson = new Lesson(subject, grade, dayOfWeek, startTime, price);
        for (Student student : students) {
            lesson.addStudent(student);
        }
        return lesson;
    }

    /**
     * Creates a {@code Lesson} with the default details.
     */
    public static Lesson getDefault() {
        return new LessonBuilder().build();
    }
}
