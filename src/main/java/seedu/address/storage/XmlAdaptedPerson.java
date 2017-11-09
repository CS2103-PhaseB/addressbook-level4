package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.*;
import seedu.address.model.tag.Tag;

/**
 * JAXB-friendly version of the Person.
 */
public class XmlAdaptedPerson {

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String phone;
    @XmlElement (required = true)
    //@@author siri99
    private String birthday;
    @XmlElement(required = true)
    //@@author siri99
    private String email;
    @XmlElement(required = true)
    private String address;

    @XmlElement
    private String score;
    //@@author Linus
    @XmlElement
    private String avatar;
    //@@author Linus

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedPerson.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedPerson() {}


    /**
     * Converts a given Person into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedPerson(ReadOnlyPerson source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        //@@author siri99
        birthday = source.getBirthday().value;
        //@@author siri99
        email = source.getEmail().value;
        address = source.getAddress().value;
        score = source.getScore().value;
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
        avatar = source.getAvatarPic().source;
    }

    /**
     * Converts this jaxb-friendly adapted person object into the model's Person object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }
        final Name name = new Name(this.name);
        final Phone phone = new Phone(this.phone);
        final Birthday birthday = new Birthday(this.birthday);
        final Email email = new Email(this.email);
        final Address address = new Address(this.address);
        final Score score = new Score(this.score);
        final Set<Tag> tags = new HashSet<>(personTags);

        //@@author Linus
        Avatar tempAvatar;
        try {
            tempAvatar = new Avatar(this.avatar);
        } catch (IllegalValueException e) {
            tempAvatar = new Avatar();
        }
        final Avatar avatar = tempAvatar;
        return new Person(name, phone, birthday, email, address, score, tags, avatar);
        //@@author Linus
    }
}
