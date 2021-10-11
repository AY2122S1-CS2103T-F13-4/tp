package seedu.address.testutil;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Grade;

public class TypicalLessons {
    // Test Lessons
    public static final Lesson SCIENCE_P6 = new Lesson("Science", new Grade("P6"),
            DayOfWeek.FRIDAY, LocalTime.NOON, 10.5);
    public static final Lesson MATH_S4 = new Lesson("Math", new Grade("S4"),
            DayOfWeek.THURSDAY, LocalTime.NOON, 13.0);
    public static final Lesson ENGLISH_S2 = new Lesson("English", new Grade("S2"),
            DayOfWeek.TUESDAY, LocalTime.NOON, 11.5);

    private TypicalLessons() {} //prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical lessons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Lesson lesson : getTypicalLessons()) {
            ab.addLesson(lesson);
        }
        return ab;
    }

    private static List<Lesson> getTypicalLessons() {
        return new ArrayList<>(Arrays.asList(SCIENCE_P6, MATH_S4, ENGLISH_S2));
    }
}
