package seedu.tuitione.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.tuitione.model.TuitiOne;
import seedu.tuitione.model.Model;

/**
 * Clears the tuitione book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setTuitiOne(new TuitiOne());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
