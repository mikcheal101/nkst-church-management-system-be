package com.mikkytrionze.nkst.member.api.request;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;
import java.util.Objects;

public class BaptismRecordValidator implements ConstraintValidator<ValidateBaptismRecord, MemberRequest> {

    @Override
    public boolean isValid(MemberRequest memberRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(memberRequest)) {
            return true;
        }

        // if the member is not baptized then no need to validate
        if (Boolean.FALSE.equals(memberRequest.getIsBaptised())) {
            return true;
        }

        boolean isValid = true;
        constraintValidatorContext.disableDefaultConstraintViolation();

        if (memberRequest.getSerialNumber() == null) {
            addViolation(constraintValidatorContext,
                    "serialNumber",
                    "Serial Number is required!");
            isValid = false;
        } else if (memberRequest.getSerialNumber() <= 0) {
            addViolation(constraintValidatorContext,
                    "serialNumber",
                    "Serial Number cannot be less than 1!");
            isValid = false;
        }

        if (memberRequest.getDateOfBaptism() == null) {
            addViolation(constraintValidatorContext,
                    "dateOfBaptism",
                    "Date of baptism is required!");
            isValid = false;
        } else if (memberRequest.getDateOfBaptism().isAfter(Instant.now())) {
            addViolation(constraintValidatorContext,
                    "dateOfBaptism",
                    "Date of baptism cannot be a date in the future");
            isValid = false;
        }

        if (isBlank(memberRequest.getWorshipCenter())) {
            addViolation(constraintValidatorContext,
                    "worshipCenter",
                    "Worship Center is required!");
            isValid = false;
        }

        if (isBlank(memberRequest.getBibleVerse())) {
            addViolation(constraintValidatorContext,
                    "bibleVerse",
                    "Bible Verse is required!");
            isValid = false;
        }

        if (isBlank(memberRequest.getBaptisedBy())) {
            addViolation(constraintValidatorContext,
                    "baptizedBy",
                    "Baptised By is required!");
            isValid = false;
        }

        if (isBlank(memberRequest.getAddress())) {
            addViolation(constraintValidatorContext,
                    "address",
                    "Address is required!");
            isValid = false;
        }

        return isValid;
    }

    private boolean isBlank(String value) {
        return Objects.isNull(value) || value.trim().isEmpty() || value.trim().isBlank();
    }

    private void addViolation(
            ConstraintValidatorContext constraintValidatorContext,
            String fieldName,
            String message) {
        constraintValidatorContext
                .buildConstraintViolationWithTemplate(message)
                .addPropertyNode(fieldName)
                .addConstraintViolation();
    }
}
