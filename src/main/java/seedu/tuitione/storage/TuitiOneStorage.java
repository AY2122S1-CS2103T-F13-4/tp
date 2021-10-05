package seedu.tuitione.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.tuitione.commons.exceptions.DataConversionException;
import seedu.tuitione.model.ReadOnlyTuitiOne;
import seedu.tuitione.model.TuitiOne;

/**
 * Represents a storage for {@link TuitiOne}.
 */
public interface TuitiOneStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getTuitiOneFilePath();

    /**
     * Returns TuitiOne data as a {@link ReadOnlyTuitiOne}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyTuitiOne> readTuitiOne() throws DataConversionException, IOException;

    /**
     * @see #getTuitiOneFilePath()
     */
    Optional<ReadOnlyTuitiOne> readTuitiOne(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyTuitiOne} to the storage.
     * @param tuitiOne cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveTuitiOne(ReadOnlyTuitiOne tuitiOne) throws IOException;

    /**
     * @see #saveTuitiOne(ReadOnlyTuitiOne)
     */
    void saveTuitiOne(ReadOnlyTuitiOne tuitiOne, Path filePath) throws IOException;

}
