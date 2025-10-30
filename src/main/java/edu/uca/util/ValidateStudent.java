package edu.uca.util;

/*
    Validate student fields
 */

public class ValidateStudent extends Validate {
    private static final String BANNERID_MATCH = "^[Bb]\\d+$";
    private static final String NAME_MATCH = "^[A-Za-z]+(?:\\s[A-Za-z]+)*$";
    private static final String EMAIL_MATCH = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";


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
