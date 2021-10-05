package seedu.tuitione.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.tuitione.commons.core.LogsCenter;
import seedu.tuitione.commons.exceptions.DataConversionException;
import seedu.tuitione.model.ReadOnlyTuitiOne;
import seedu.tuitione.model.ReadOnlyUserPrefs;
import seedu.tuitione.model.UserPrefs;

/**
 * Manages storage of TuitiOne data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private TuitiOneStorage tuitiOneStorage;
    private UserPrefsStorage userPrefsStorage;

    /**
     * Creates a {@code StorageManager} with the given {@code TuitiOneStorage} and {@code UserPrefStorage}.
     */
    public StorageManager(TuitiOneStorage tuitiOneStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.tuitiOneStorage = tuitiOneStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ TuitiOne methods ==============================

    @Override
    public Path getTuitiOneFilePath() {
        return tuitiOneStorage.getTuitiOneFilePath();
    }

    @Override
    public Optional<ReadOnlyTuitiOne> readTuitiOne() throws DataConversionException, IOException {
        return readTuitiOne(tuitiOneStorage.getTuitiOneFilePath());
    }

    @Override
    public Optional<ReadOnlyTuitiOne> readTuitiOne(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return tuitiOneStorage.readTuitiOne(filePath);
    }

    @Override
    public void saveTuitiOne(ReadOnlyTuitiOne tuitiOne) throws IOException {
        saveTuitiOne(tuitiOne, tuitiOneStorage.getTuitiOneFilePath());
    }

    @Override
    public void saveTuitiOne(ReadOnlyTuitiOne tuitiOne, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        tuitiOneStorage.saveTuitiOne(tuitiOne, filePath);
    }

}
