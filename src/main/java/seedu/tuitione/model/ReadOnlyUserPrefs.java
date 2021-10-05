package seedu.tuitione.model;

import java.nio.file.Path;

import seedu.tuitione.commons.core.GuiSettings;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    Path getTuitiOneFilePath();

}
