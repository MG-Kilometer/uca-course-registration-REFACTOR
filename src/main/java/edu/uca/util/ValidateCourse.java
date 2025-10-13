package edu.uca.util;

public class ValidateCourse extends Validate {
    private static final String CODE_MATCH = "^[A-Za-z0-9]+$";
    private static final String TITLE_MATCH = "^[A-Z\\sa-z]+$";
    private static final String CAPACITY_MATCH = "^[0-9]+$";

    public boolean Validate_Code(String code) {
        super.Validate_String(CODE_MATCH, code);

        try {
            if (!super.getResult()) {
                throw new ValidationException("Course code must be alphanumeric. Invalid: " + code);
            }
        } catch(ValidationException ignored) {}

        return(super.getResult());
    }

    public boolean Validate_Title(String title) {
        super.Validate_String(TITLE_MATCH, title);

        try {
            if (!super.getResult()) {
                throw new ValidationException("Course title must be letters. Invalid: " + title);
            }
        } catch(ValidationException ignored) {}

        return(super.getResult());
    }

    public boolean Validate_Capacity(String capacity) {
        super.Validate_String(CAPACITY_MATCH, capacity);

        try {
            if (!super.getResult()) {
                throw new ValidationException("Capacity must be numeric. Invalid: " + capacity);
            }
        } catch(ValidationException ignored) {}

        return(super.getResult());
    }
}
