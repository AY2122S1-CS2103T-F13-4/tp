package seedu.tuitione.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.tuitione.commons.exceptions.DataConversionException;
import seedu.tuitione.model.ReadOnlyTuitiOne;
import seedu.tuitione.model.ReadOnlyUserPrefs;
import seedu.tuitione.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends TuitiOneStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getTuitiOneFilePath();

    @Override
    Optional<ReadOnlyTuitiOne> readTuitiOne() throws DataConversionException, IOException;

    @Override
    void saveTuitiOne(ReadOnlyTuitiOne tuitiOne) throws IOException;

}
