package ua.alisasira.validation.validator;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class BirthdayValidator implements ConstraintValidator<Birthday, LocalDate> {

    @Value("${min.age:18}")
    private Integer minAge;

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        if (date == null) {
            return true;
        }

        LocalDate now = LocalDate.now();
        LocalDate value = date.plusYears(minAge);
        return now.isAfter(value);
    }

    @Override
    public void initialize(Birthday constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
