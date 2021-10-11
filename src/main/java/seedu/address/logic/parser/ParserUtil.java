package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LESSON;
import static seedu.address.model.lesson.Lesson.TIME_FORMATTER;
import static seedu.address.model.lesson.Lesson.parseStringToDayOfWeek;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.EnrollCommand;
import seedu.address.logic.commands.UnenrollCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Grade;
import seedu.address.model.person.Name;
import seedu.address.model.person.ParentContact;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_INVALID_TIME = "Time formatting is invalid.";
    public static final String MESSAGE_INVALID_DAY = "Day formatting is invalid.";
    public static final String MESSAGE_INVALID_COST_NOT_NUMBER = "Cost formating is invalid, it is not a number.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static ParentContact parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!ParentContact.isValidPhone(trimmedPhone)) {
            throw new ParseException(ParentContact.MESSAGE_CONSTRAINTS);
        }
        return new ParentContact(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String grade} into an {@code Grade}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code grade} is invalid.
     */
    public static Grade parseGrade(String grade) throws ParseException {
        requireNonNull(grade);
        String trimmedGrade = grade.trim();
        if (!Grade.isValidGrade(grade)) {
            throw new ParseException(Grade.MESSAGE_CONSTRAINTS);
        }
        return new Grade(trimmedGrade);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String time} into a {@code LocalTime}.
     */
    public static LocalTime parseLocalTime(String time) throws ParseException {
        requireNonNull(time);
        String trimmedTime = time.trim();
        try {
            return LocalTime.parse(trimmedTime, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ParseException(MESSAGE_INVALID_TIME);
        }
    }

    /**
     * Parses a {@code String day} into {@code DayOfWeek}.
     */
    public static DayOfWeek parseDayOfWeek(String day) throws ParseException {
        requireNonNull(day);
        String cleanedDay = StringUtil.capitalize(day.trim());
        try {
            return parseStringToDayOfWeek(cleanedDay);
        } catch (IllegalArgumentException e) {
            throw new ParseException(MESSAGE_INVALID_DAY);
        }
    }

    /**
     * Parses a {@code String lesson Code} into a {@code UnenrollCommand}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static UnenrollCommand parseUnenrollArgs(String args) throws ParseException {
        requireNonNull(args);
        Index index;
        String lessonCode = null;
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_LESSON);

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnenrollCommand.MESSAGE_USAGE, pe));
        }

        if (argMultimap.getValue(PREFIX_LESSON).isPresent()) {
            lessonCode = argMultimap.getValue(PREFIX_LESSON).get().trim();
        }
        return new UnenrollCommand(index, lessonCode);
    }

    /**
     * Parses a {@code String lesson Code} into a {@code EnrollCommand}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static EnrollCommand parseEnrollArgs(String args) throws ParseException {
        requireNonNull(args);
        Index index;
        String lessonCode = null;
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_LESSON);

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EnrollCommand.MESSAGE_USAGE), pe);
        }

        if (argMultimap.getValue(PREFIX_LESSON).isPresent()) {
            lessonCode = argMultimap.getValue(PREFIX_LESSON).get().trim();
        }
        return new EnrollCommand(index, lessonCode);
    }

    /**
     * Parses a {@code String subject} into a {@code String subject}
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param args Subject
     * @throws ParseException if the given {@code subject} is invalid
     */
    public static String parseSubjectArgs(String args) throws ParseException {
        requireNonNull(args);
        String trimmedSubject = args.trim();

        if (!Lesson.isValidSubject(trimmedSubject)) {
            throw new ParseException(Lesson.SUBJECT_MESSAGE_CONSTRAINTS);
        }

        return trimmedSubject;
    }

    /**
     * Parses a {@code String cost} into a {@code double cost}
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code cost} is invalid
     */
    public static double parseCostArgs(String args) throws ParseException {
        requireNonNull(args);
        String trimmedCost = args.trim();
        double cost;

        try {
            cost = Double.parseDouble(args);
        } catch (NumberFormatException e) {
            throw new ParseException(MESSAGE_INVALID_COST_NOT_NUMBER);
        }

        if (!Lesson.isValidPrice(cost)) {
            throw new ParseException(Lesson.PRICE_MESSAGE_CONSTRAINT);
        }

        return cost;
    }
}
