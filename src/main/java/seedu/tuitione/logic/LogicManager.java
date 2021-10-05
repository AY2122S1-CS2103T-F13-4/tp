package seedu.tuitione.logic;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.tuitione.commons.core.GuiSettings;
import seedu.tuitione.commons.core.LogsCenter;
import seedu.tuitione.logic.commands.Command;
import seedu.tuitione.logic.commands.CommandResult;
import seedu.tuitione.logic.commands.exceptions.CommandException;
import seedu.tuitione.logic.parser.TuitiOneParser;
import seedu.tuitione.logic.parser.exceptions.ParseException;
import seedu.tuitione.model.Model;
import seedu.tuitione.model.ReadOnlyTuitiOne;
import seedu.tuitione.model.person.Person;
import seedu.tuitione.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final TuitiOneParser tuitiOneParser;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        tuitiOneParser = new TuitiOneParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command = tuitiOneParser.parseCommand(commandText);
        commandResult = command.execute(model);

        try {
            storage.saveTuitiOne(model.getTuitiOne());
        } catch (IOException ioe) {
            throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyTuitiOne getTuitiOne() {
        return model.getTuitiOne();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public Path getTuitiOneFilePath() {
        return model.getTuitiOneFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
