package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EnrollCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.*;

import java.time.DayOfWeek;
import java.time.LocalTime;


import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.*;

/**
 * Parses input arguments and creates a new {@code RemarkCommand} object
 */
public class EnrollCommandParser implements Parser<EnrollCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@code RemarkCommand}
     * and returns a {@code RemarkCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public EnrollCommand parse(String userInput) throws ParseException {
        requireNonNull(userInput);

        String subject = null;
        Grade grade = null;
        DayOfWeek day = null;
        LocalTime startTime = null;
        double price;
        Index index;
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(userInput, PREFIX_SUBJECT, PREFIX_GRADE, PREFIX_DAY,PREFIX_TIME);

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        if (argMultimap.getValue(PREFIX_SUBJECT).isPresent()) {
            subject = argMultimap.getValue(PREFIX_SUBJECT).get();
        }

        if (argMultimap.getValue(PREFIX_GRADE).isPresent()) {
            grade = ParserUtil.parseGrade(argMultimap.getValue(PREFIX_GRADE).get());
        }

        if (argMultimap.getValue(PREFIX_DAY).isPresent()) {
            day = ParserUtil.parseDayOfWeek(argMultimap.getValue(PREFIX_DAY).get());
        }

        if (argMultimap.getValue(PREFIX_TIME).isPresent()) {
            startTime = ParserUtil.parseLocalTime(argMultimap.getValue(PREFIX_SUBJECT).get());
        }

        Lesson lesson = new Lesson(subject, grade, day, startTime, 0.0);
        //BY RIGHT SHOULD ADD INTO AN EXISTING LESSON

        return new EnrollCommand(lesson);
    }
}
