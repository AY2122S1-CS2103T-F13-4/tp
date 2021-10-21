package seedu.tuitione.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.tuitione.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.tuitione.logic.parser.CliSyntax.PREFIX_LESSON;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.tuitione.commons.core.Messages;
import seedu.tuitione.commons.core.index.Index;
import seedu.tuitione.logic.commands.exceptions.CommandException;
import seedu.tuitione.model.Model;
import seedu.tuitione.model.lesson.Lesson;
import seedu.tuitione.model.student.Student;

public class EnrollCommand extends Command {

    public static final String COMMAND_WORD = "enroll";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Enrolls a specified student "
            + "from a given TuitiONE lesson\n"
            + "Parameters: STUDENT_INDEX (must be a positive integer) "
            + "l/LESSON_INDEX\n"
            + "Example: " + "enroll 1 " + PREFIX_LESSON + "1";

    public static final String MESSAGE_STUDENT_IN_LESSON = "%1$s is already enrolled in the existing %2$s";
    public static final String MESSAGE_UNABLE_TO_ENROLL = "%1$s cannot be enrolled into %2$s";
    public static final String MESSAGE_SUCCESS = "%1$s enrolled into lesson: %2$s";

    private final Index indexStudent;
    private final Index indexLesson;


    /**
     * Creates an EnrollCommand for a Student with a given index to a specified {@code Lesson}.
     */
    public EnrollCommand(Index indexStudent, Index indexLesson) {
        requireAllNonNull(indexStudent, indexLesson);

        this.indexStudent = indexStudent;
        this.indexLesson = indexLesson;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Student> lastShownList = model.getFilteredStudentList();
        ObservableList<Lesson> lessonList = model.getFilteredLessonList();

        if (indexStudent.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }
        Student studentToEnroll = lastShownList.get(indexStudent.getZeroBased());

        if (indexLesson.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_LESSON_DISPLAYED_INDEX);
        }
        Lesson lesson = lessonList.get(indexLesson.getZeroBased());

        if (lesson.containsStudent(studentToEnroll)) {
            throw new CommandException(String.format(MESSAGE_STUDENT_IN_LESSON, studentToEnroll.getName(), lesson));
        }
        if (!lesson.isAbleToEnroll(studentToEnroll)) {
            throw new CommandException(String.format(MESSAGE_UNABLE_TO_ENROLL, studentToEnroll.getName(), lesson));
        }

        lesson.enrollStudent(studentToEnroll);
        model.setLesson(lesson, lesson);
        model.setStudent(studentToEnroll, studentToEnroll);

        return new CommandResult(String.format(MESSAGE_SUCCESS, studentToEnroll.getName(), lesson));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof EnrollCommand
                && indexStudent.equals(((EnrollCommand) other).indexStudent)
                && indexLesson.equals(((EnrollCommand) other).indexLesson));
    }
}
