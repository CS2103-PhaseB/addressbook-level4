package seedu.address.logic.commands;

//@@author Linus
import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_IMAGE_URL;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Avatar;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

    /**
     * Updates the avatar picture of an existing person in the address book.
     */

    public class AddAvatarCommand extends Command {
        public static final String COMMAND_WORD = "avatar";

        public static final String MESSAGE_USAGE = COMMAND_WORD + ": Updates the avatar picture of the person identified "
                + "by the index number. "
                + "Existing avatar picture will be replaced by the new picture.\n"
                + "Parameters: INDEX of person (positive integer) "
                + "[u/image URL]\n"
                + "Example of using online image: " + COMMAND_WORD + " 1 "
                + PREFIX_IMAGE_URL
                + "https://i.pinimg.com/564x/c1/23/83/c1238302de2a7cc3c905830681814e79.jpg\n"
                + "Example of using local image: " + COMMAND_WORD + " 1 "
                + PREFIX_IMAGE_URL + "https://www.gravatar.com/avatar/null\n";

        public static final String MESSAGE_UPDATE_AVATAR_PIC_SUCCESS = "Update avatar picture for Person: %1$s";
        public static final String MESSAGE_NOT_UPDATED = "Please enter a valid image URL.";
        public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

        private final Index index;
        private final Avatar avatar;

        private boolean isOldFileDeleted = true;

    /**
     *
     * @param index of the person in the filtered person list to update avatar picture
     * @param avatar of the image to be used as the Avatar picture
     */
    public AddAvatarCommand(Index index, Avatar avatar) {
        requireNonNull(index);
        requireNonNull(avatar);

        this.index = index;
        this.avatar = avatar;
    }

    @Override
    public CommandResult execute() throws CommandException {
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyPerson personToUpdateAvatarPic = lastShownList.get(index.getZeroBased());
        Person updatedAvatarPicPerson = new Person(personToUpdateAvatarPic);
        Avatar newAvatar;

        if (avatar.toString().compareTo(Avatar.DEFAULT_URL) == 0) {
            String oldFile = personToUpdateAvatarPic.getAvatarPic().toString();
            if (oldFile.compareTo(Avatar.DEFAULT_URL) != 0) {
                oldFile = urlToPath(oldFile);
                try {
                    Files.delete(Paths.get(oldFile));
                } catch (IOException ioe) {
                    isOldFileDeleted = false;
                }
            }
            newAvatar = avatar;
        } else {
            String newFile;
            if (!Files.isDirectory(Paths.get("avatars"))) {
                try {
                    Files.createDirectory(Paths.get("avatars"));
                } catch (IOException ioe) {
                    throw new CommandException("avatars directory failed to be created");
                }
            }
            if (personToUpdateAvatarPic.getAvatarPic().toString().compareTo(Avatar.DEFAULT_URL) == 0) {

                /*
                *  Validates avatar image
                * */
                String imgExtension = "";

                int i = avatar.toString().lastIndexOf('.');
                if (i > 0) {
                    imgExtension = avatar.toString().substring(i + 1);
                }

                List validExtension = Arrays.asList("jpg", "jpeg", "png", "gif", "JPG", "JPEG", "PNG", "GIF");
                if (validExtension.contains(imgExtension)) {

                    newFile = "avatars/" + new Date().getTime() + '.' + imgExtension;

                } else {

                    newFile = "avatars/" + new Date().getTime() + ".png";
                }

            } else {
                newFile = personToUpdateAvatarPic.getAvatarPic().toString();
                newFile = urlToPath(newFile);
            }
            if (!Files.exists(Paths.get(newFile))) {
                try {
                    Files.createFile(Paths.get(newFile));
                } catch (IOException ioe) {
                    throw new CommandException("New file failed to be created");
                }
            }
            try {
                URL url = new URL(avatar.toString());
                InputStream in = url.openStream();
                Files.copy(in, Paths.get(newFile), StandardCopyOption.REPLACE_EXISTING);
                in.close();
                newAvatar = new Avatar("file://" + Paths.get(newFile).toAbsolutePath().toUri().getPath());
            } catch (IOException ioe) {
                throw new CommandException("Image failed to download");
            } catch (IllegalValueException ive) {
                throw new CommandException(ive.getMessage());
            }
        }
        updatedAvatarPicPerson.setAvatarPic(newAvatar);

        /*
        *  Updates the avatar for the person based on its index
        * */
        EventsCenter.getInstance().post(new JumpToListRequestEvent(this.index));

        try {
            model.updatePerson(personToUpdateAvatarPic, updatedAvatarPicPerson);

        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PersonNotFoundException e) {
            throw new AssertionError("The target person cannot be missing");
        }

        if (avatar.toString().compareTo(Avatar.DEFAULT_URL) != 0) {
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        }

        String resultMessage = String.format(MESSAGE_UPDATE_AVATAR_PIC_SUCCESS, personToUpdateAvatarPic.getName());

        if (isOldFileDeleted) {
            return new CommandResult(resultMessage);
        } else {
            return new CommandResult(String.join("\n", resultMessage, "Old image not deleted"));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddAvatarCommand // instanceof handles nulls
                && this.index.equals(((AddAvatarCommand) other).index)
                && this.avatar.equals(((AddAvatarCommand) other).avatar)); // state check
    }

    private String urlToPath(String url) {
        return url.substring(url.indexOf("avatars"));
    }
}
