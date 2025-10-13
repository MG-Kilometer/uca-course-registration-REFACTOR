package edu.uca.util;

/*
    Validate student fields
 */

public class ValidateStudent extends Validate {
    private static final String BANNERID_MATCH = "^[Bb]\\d+"; // match any B0...
    private static final String NAME_MATCH = "^[a-zA-Z]+\\s*[a-zA-Z]+"; // match any J...
    private static final String EMAIL_MATCH = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public boolean ValidateID(String bannerID) {
        super.Validate_String(BANNERID_MATCH, bannerID);

        try {
            if (!super.getResult()) {
                throw new ValidationException("BannerID must start with B and be followed by one or more numbers. Invalid: " + bannerID);
            }
        } catch(ValidationException ignored) {}

        return(super.getResult());
    }

    public boolean ValidateName(String name) {
        super.Validate_String(NAME_MATCH, name);

        try {
            if (!super.getResult()) {
                throw new ValidationException("Name must only contain characters and be at least 2 characters long. Invalid: " + name);
            }
        } catch(ValidationException ignored) {}
        return(super.getResult());
    }
    public boolean ValidateEmail(String email) {
        super.Validate_String(EMAIL_MATCH, email);

        try {
            if (!super.getResult()) {
                throw new ValidationException("Email must start with letters, have an @, and end with . characters. Invalid: " + email);
            }
        } catch(ValidationException ignored) {}
        return(super.getResult());
    }
}
