package seedu.tuitione.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.tuitione.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.tuitione.commons.exceptions.IllegalValueException;
import seedu.tuitione.commons.util.JsonUtil;
import seedu.tuitione.model.TuitiOne;
import seedu.tuitione.testutil.TypicalPersons;

public class JsonSerializableTuitiOneTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableTuitiOneTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalPersonsTuitiOne.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidPersonTuitiOne.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonTuitiOne.json");

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializableTuitiOne dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableTuitiOne.class).get();
        TuitiOne tuitiOneFromFile = dataFromFile.toModelType();
        TuitiOne typicalPersonsTuitiOne = TypicalPersons.getTypicalTuitiOne();
        assertEquals(tuitiOneFromFile, typicalPersonsTuitiOne);
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableTuitiOne dataFromFile = JsonUtil.readJsonFile(INVALID_PERSON_FILE,
                JsonSerializableTuitiOne.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicatePersons_throwsIllegalValueException() throws Exception {
        JsonSerializableTuitiOne dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PERSON_FILE,
                JsonSerializableTuitiOne.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableTuitiOne.MESSAGE_DUPLICATE_PERSON,
                dataFromFile::toModelType);
    }

}
