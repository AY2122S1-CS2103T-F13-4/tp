package seedu.tuitione.model.student;

import static seedu.tuitione.commons.util.AppUtil.checkArgument;
import static seedu.tuitione.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

/**
 * Represents the grade of the student
 */
public class Grade {
    public static final String GRADE_MESSAGE_CONSTRAINTS = "Prefix should be in capital letters, "
            + "and is either S (to denote secondary) "
            + "or P (to denote Primary). "
            + "\nLevel number should be from 1 to 6 for primary and 1 to 4 for secondary."
            + "Example(s): P1 S3";

    public static final String[] VALID_GRADES = {"P1", "P2", "P3", "P4", "P5", "P6", "S1", "S2", "S3", "S4"};
    public final String value;

    /**
     * Constructs a student grade
     *
     * @param value The grade of the student
     */
    public Grade(String value) {
        requireAllNonNull(value);
        checkArgument(isValidGrade(value), GRADE_MESSAGE_CONSTRAINTS);
        this.value = value;
    }

    /**
     * Returns true if given grade is valid.
     */
    public static boolean isValidGrade(String grade) {
        for (String validGrade : VALID_GRADES) {
            if (grade.equals(validGrade)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Grade // instanceof handles nulls
                && value.equals(((Grade) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
