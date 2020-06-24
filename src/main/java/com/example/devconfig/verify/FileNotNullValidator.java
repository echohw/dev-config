package com.example.devconfig.verify;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by AMe on 2020-06-20 01:10.
 */
public class FileNotNullValidator implements ConstraintValidator<FileNotNull, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            String defaultMessage = context.getDefaultConstraintMessageTemplate();
            if (defaultMessage != null && defaultMessage.trim().length() > 0) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(defaultMessage).addConstraintViolation();
            }
            return false;
        }
        return true;
    }
}
