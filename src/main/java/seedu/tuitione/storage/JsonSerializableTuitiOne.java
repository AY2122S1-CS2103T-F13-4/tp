package seedu.tuitione.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.tuitione.commons.exceptions.IllegalValueException;
import seedu.tuitione.model.TuitiOne;
import seedu.tuitione.model.ReadOnlyTuitiOne;
import seedu.tuitione.model.person.Person;

/**
 * An Immutable TuitiOne that is serializable to JSON format.
 */
@JsonRootName(value = "tuitione")
class JsonSerializableTuitiOne {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";

    private final List<JsonAdaptedStudent> persons = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableTuitiOne} with the given persons.
     */
    @JsonCreator
    public JsonSerializableTuitiOne(@JsonProperty("persons") List<JsonAdaptedStudent> persons) {
        this.persons.addAll(persons);
    }

    /**
     * Converts a given {@code ReadOnlyTuitiOne} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableTuitiOne}.
     */
    public JsonSerializableTuitiOne(ReadOnlyTuitiOne source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedStudent::new).collect(Collectors.toList()));
    }

    /**
     * Converts this tuitione book into the model's {@code TuitiOne} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public TuitiOne toModelType() throws IllegalValueException {
        TuitiOne tuitiOne = new TuitiOne();
        for (JsonAdaptedStudent jsonAdaptedStudent : persons) {
            Person person = jsonAdaptedStudent.toModelType();
            if (tuitiOne.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            tuitiOne.addPerson(person);
        }
        return tuitiOne;
    }

}
