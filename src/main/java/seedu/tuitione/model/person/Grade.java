package seedu.tuitione.model.person;

import static seedu.tuitione.commons.util.AppUtil.checkArgument;
import static seedu.tuitione.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

/**
 * Represents the grade of the student
 */
public class Grade {
    public static final String MESSAGE_CONSTRAINTS = "Prefix should either be S (to denote secondary) "
            + "or P (to denote Primary)."
            + "Level number should be from 1 to 6 for primary and 1 to 4 for secondary.";

    private String prefix;
    private int levelNumber;

    /**
     * Constructs a student grade
     *
     * @param prefix A valid prefix.
     * @param levelNumber A valid levelNumber.
     */
    public Grade(String prefix, int levelNumber) {
        requireAllNonNull(prefix, levelNumber);
        checkArgument(isValidInput(prefix, levelNumber), MESSAGE_CONSTRAINTS);
        this.prefix = prefix;
        this.levelNumber = levelNumber;
    }

    /**
     * Returns true if given prefix and levelNumber are valid
     * @param prefix The inputted grade prefix.
     * @param levelNumber The inputted grade level number.
     * @return Whether the prefix and levelNumber are valid inputs.
     */
    private boolean isValidInput(String prefix, int levelNumber) {
        switch (prefix) {
        case "S":
            return levelNumber >= 1 && levelNumber <= 4;
        case "P":
            return levelNumber >= 1 && levelNumber <= 6;
        default:
            return false;
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) { // short circuit if same object
            return true;
        } else if (other instanceof Grade) {
            String otherPrefix = ((Grade) other).getPrefix();
            int otherLevelNumber = ((Grade) other).getLevelNumber();
            return prefix.equals(otherPrefix) && (levelNumber == otherLevelNumber); // state check
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(prefix, levelNumber);
    }

    @Override
    public String toString() {
        return "[" + prefix + levelNumber + "]";
    }
}
