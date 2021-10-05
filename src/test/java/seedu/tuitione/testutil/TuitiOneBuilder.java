package seedu.tuitione.testutil;

import seedu.tuitione.model.TuitiOne;
import seedu.tuitione.model.person.Person;

/**
 * A utility class to help with building TuitiOne objects.
 * Example usage: <br>
 *     {@code TuitiOne ab = new TuitiOneBuilder().withPerson("John", "Doe").build();}
 */
public class TuitiOneBuilder {

    private TuitiOne tuitiOne;

    public TuitiOneBuilder() {
        tuitiOne = new TuitiOne();
    }

    public TuitiOneBuilder(TuitiOne tuitiOne) {
        this.tuitiOne = tuitiOne;
    }

    /**
     * Adds a new {@code Person} to the {@code TuitiOne} that we are building.
     */
    public TuitiOneBuilder withPerson(Person person) {
        tuitiOne.addPerson(person);
        return this;
    }

    public TuitiOne build() {
        return tuitiOne;
    }
}
