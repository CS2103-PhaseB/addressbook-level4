package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_WEB_IMAGE_URL_A;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_WEB_IMAGE_URL_B;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WEB_IMAGE_URL_A;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WEB_IMAGE_URL_B;

import org.junit.Test;

//@@author LinusMelb

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



