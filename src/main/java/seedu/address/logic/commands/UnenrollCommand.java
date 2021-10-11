package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LESSON;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Student;


public class UnenrollCommand extends Command {

    public static final String COMMAND_WORD = "unenroll";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Unenroll a specified student "
            + "from a given TuitiONE lesson\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "LESSONCODE\n"
            + "Example: " + "unenroll 1 " + PREFIX_LESSON + "Science-P5-Wed-1230";

    public static final String MESSAGE_UNENROLL_STUDENT_SUCCESS = "Unenrolled Student: %1$s from Lesson: %2$s";
    public static final String MESSAGE_STUDENT_NOT_IN_LESSON = "%1$s is not currently enrolled in the Lesson: %2$s";

    private final Index targetIndex;

    private final String lessonCode;

    /**
     * Creates an UnenrollCommand for a Student with a given index and a specified {@code Lesson}.
     */
    public UnenrollCommand(Index targetIndex, String lessonCode) {
        this.targetIndex = targetIndex;
        this.lessonCode = lessonCode;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Student> lastShownList = model.getFilteredPersonList();
        Lesson lesson = model.searchLessons(lessonCode);

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        if (lesson == null) {
            throw new CommandException(Messages.MESSAGE_INVALID_LESSON_CODE);
        }

        Student studentToUnenroll = lastShownList.get(targetIndex.getZeroBased());

        if (!lesson.containsStudent(studentToUnenroll)) {
            throw new CommandException(String.format(MESSAGE_STUDENT_NOT_IN_LESSON,
                    studentToUnenroll.getName(),
                    lesson));
        }
        lesson.removeStudent(studentToUnenroll);
        return new CommandResult(String.format(MESSAGE_UNENROLL_STUDENT_SUCCESS, studentToUnenroll.getName(), lesson));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UnenrollCommand // instanceof handles nulls
                && targetIndex.equals(((UnenrollCommand) other).targetIndex))
                && lessonCode.equals(((UnenrollCommand) other).lessonCode); // state check
    }

}
