package seedu.tuitione.logic.commands;

import static seedu.tuitione.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.tuitione.testutil.TypicalPersons.getTypicalTuitiOne;

import org.junit.jupiter.api.Test;

import seedu.tuitione.model.TuitiOne;
import seedu.tuitione.model.Model;
import seedu.tuitione.model.ModelManager;
import seedu.tuitione.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyTuitiOne_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyTuitiOne_success() {
        Model model = new ModelManager(getTypicalTuitiOne(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalTuitiOne(), new UserPrefs());
        expectedModel.setTuitiOne(new TuitiOne());

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
