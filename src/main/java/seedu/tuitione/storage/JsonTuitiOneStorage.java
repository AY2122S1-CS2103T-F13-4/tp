package seedu.tuitione.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.tuitione.commons.core.LogsCenter;
import seedu.tuitione.commons.exceptions.DataConversionException;
import seedu.tuitione.commons.exceptions.IllegalValueException;
import seedu.tuitione.commons.util.FileUtil;
import seedu.tuitione.commons.util.JsonUtil;
import seedu.tuitione.model.ReadOnlyTuitiOne;

/**
 * A class to access TuitiOne data stored as a json file on the hard disk.
 */
public class JsonTuitiOneStorage implements TuitiOneStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonTuitiOneStorage.class);

    private Path filePath;

    public JsonTuitiOneStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getTuitiOneFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyTuitiOne> readTuitiOne() throws DataConversionException {
        return readTuitiOne(filePath);
    }

    /**
     * Similar to {@link #readTuitiOne()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyTuitiOne> readTuitiOne(Path filePath) throws DataConversionException {
        requireNonNull(filePath);

        Optional<JsonSerializableTuitiOne> jsonTuitiOne = JsonUtil.readJsonFile(
                filePath, JsonSerializableTuitiOne.class);
        if (!jsonTuitiOne.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonTuitiOne.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveTuitiOne(ReadOnlyTuitiOne tuitiOne) throws IOException {
        saveTuitiOne(tuitiOne, filePath);
    }

    /**
     * Similar to {@link #saveTuitiOne(ReadOnlyTuitiOne)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveTuitiOne(ReadOnlyTuitiOne tuitiOne, Path filePath) throws IOException {
        requireNonNull(tuitiOne);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableTuitiOne(tuitiOne), filePath);
    }

}
