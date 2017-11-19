# LinusMelb
###### /java/systemtests/ClearCommandSystemTest.java
``` java
        assertStatusBarUnchangedExceptSyncStatus();
```
###### /java/systemtests/AddressBookSystemTest.java
``` java
        assertEquals(expectedSyncStatus, handle.getSyncStatus().split(", ")[1]);
```
###### /java/seedu/address/ui/BrowserPanelTest.java
``` java
    @Test
    public void display() throws Exception {

        URL expectedDefaultPageUrl = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE);
        assertEquals(expectedDefaultPageUrl, browserPanelHandle.getLoadedUrl());

        postNow(selectionChangedEventStub);

        URL expectedPersonUrl = MainApp.class.getResource(FXML_FILE_FOLDER + BROWSER_PAGE);;

        waitUntilBrowserLoaded(browserPanelHandle);

        assertEquals(expectedPersonUrl, browserPanelHandle.getLoadedUrl());
    }
```
###### /java/seedu/address/logic/commands/HomeCommandTest.java
``` java
public class HomeCommandTest {

    private Model model;
    private Model expectedModel;
    private HomeCommand homeCommand;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        homeCommand = new HomeCommand();
        homeCommand.setData(model, new CommandHistory(), new UndoRedoStack());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(homeCommand, model, HomeCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showFirstPersonOnly(model);
        assertCommandSuccess(homeCommand, model, HomeCommand.MESSAGE_SUCCESS, expectedModel);
    }
}

```
###### /java/seedu/address/logic/commands/CommandTestUtil.java
``` java
    public static final String INVALID_WEB_IMAGE_URL_A =
            "INVALID_IMAGE_URL";
    public static final String INVALID_WEB_IMAGE_URL_B =
            "http://invalid.com/invalid.jpg";
    public static final String VALID_WEB_IMAGE_URL_A =
            "http://188.166.212.235/storage/avatars/default-M.png";
    public static final String VALID_WEB_IMAGE_URL_B =
            "http://188.166.212.235/storage/avatars/default-F.png";
```
###### /java/seedu/address/model/person/AvatarTest.java
``` java

public class AvatarTest {
    @Test
    public void isValidUrl() {
        // 0 stands for valid image URL
        // -1 stands for invalid image URL
        // -2 stands for expired image URL

        // invalid URL
        assertFalse(Avatar.isValidUrl("") == 0); // empty string
        assertFalse(Avatar.isValidUrl("images/nonExist.png") == 0);
        assertFalse(Avatar.isValidUrl(INVALID_WEB_IMAGE_URL_A) == 0);
        assertFalse(Avatar.isValidUrl(INVALID_WEB_IMAGE_URL_B) == 0);

        // valid URL
        assertTrue(Avatar.isValidUrl(VALID_WEB_IMAGE_URL_A) == 0);
        assertTrue(Avatar.isValidUrl(VALID_WEB_IMAGE_URL_B) == 0);
    }
}



```
