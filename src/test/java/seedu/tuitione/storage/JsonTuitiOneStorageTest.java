package seedu.tuitione.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.tuitione.testutil.Assert.assertThrows;
import static seedu.tuitione.testutil.TypicalPersons.ALICE;
import static seedu.tuitione.testutil.TypicalPersons.HOON;
import static seedu.tuitione.testutil.TypicalPersons.IDA;
import static seedu.tuitione.testutil.TypicalPersons.getTypicalTuitiOne;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.tuitione.commons.exceptions.DataConversionException;
import seedu.tuitione.model.ReadOnlyTuitiOne;
import seedu.tuitione.model.TuitiOne;

public class JsonTuitiOneStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonTuitiOneStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readTuitiOne_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readTuitiOne(null));
    }

    private java.util.Optional<ReadOnlyTuitiOne> readTuitiOne(String filePath) throws Exception {
        return new JsonTuitiOneStorage(Paths.get(filePath)).readTuitiOne(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readTuitiOne("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataConversionException.class, () -> readTuitiOne("notJsonFormatTuitiOne.json"));
    }

    @Test
    public void readTuitiOne_invalidPersonTuitiOne_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readTuitiOne("invalidPersonTuitiOne.json"));
    }

    @Test
    public void readTuitiOne_invalidAndValidPersonTuitiOne_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readTuitiOne("invalidAndValidPersonTuitiOne.json"));
    }

    @Test
    public void readAndSaveTuitiOne_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempTuitiOne.json");
        TuitiOne original = getTypicalTuitiOne();
        JsonTuitiOneStorage jsonTuitiOneStorage = new JsonTuitiOneStorage(filePath);

        // Save in new file and read back
        jsonTuitiOneStorage.saveTuitiOne(original, filePath);
        ReadOnlyTuitiOne readBack = jsonTuitiOneStorage.readTuitiOne(filePath).get();
        assertEquals(original, new TuitiOne(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addPerson(HOON);
        original.removePerson(ALICE);
        jsonTuitiOneStorage.saveTuitiOne(original, filePath);
        readBack = jsonTuitiOneStorage.readTuitiOne(filePath).get();
        assertEquals(original, new TuitiOne(readBack));

        // Save and read without specifying file path
        original.addPerson(IDA);
        jsonTuitiOneStorage.saveTuitiOne(original); // file path not specified
        readBack = jsonTuitiOneStorage.readTuitiOne().get(); // file path not specified
        assertEquals(original, new TuitiOne(readBack));

    }

    @Test
    public void saveTuitiOne_nullTuitiOne_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveTuitiOne(null, "SomeFile.json"));
    }

    /**
     * Saves {@code TuitiOne} at the specified {@code filePath}.
     */
    private void saveTuitiOne(ReadOnlyTuitiOne tuitiOne, String filePath) {
        try {
            new JsonTuitiOneStorage(Paths.get(filePath))
                    .saveTuitiOne(tuitiOne, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveTuitiOne_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveTuitiOne(new TuitiOne(), null));
    }
}
