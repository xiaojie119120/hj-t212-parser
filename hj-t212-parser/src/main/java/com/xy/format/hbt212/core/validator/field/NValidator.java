package com.xy.format.hbt212.core.validator.field;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Collections;

/**
 * Created by xiaoyao9184 on 2018/1/10.
 */
public class NValidator implements ConstraintValidator<N, String> {

    private int int_len_max;
    private int fraction_len_max;
    private String format;
    private boolean optional;

    @Override
    public void initialize(N n) {
        this.int_len_max = n.integer();
        this.fraction_len_max = n.fraction();
        this.optional = n.optional();

        format = String.join("", Collections.nCopies(int_len_max,"#"));
        if(fraction_len_max > 0){
            format = format + "." + String.join("", Collections.nCopies(fraction_len_max,"#"));
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(optional && value == null){
            return true;
        }
//        return isValidFormat(format, value);

        BigDecimal decimal = new BigDecimal(value);
        int int_len = getNumLength(decimal.longValue());
        int fraction_len = decimal.scale();
        return int_len <= int_len_max &&
                fraction_len <= fraction_len_max;
    }

    @Deprecated
    public static boolean isValidFormat(String format, String value) {
        Number number = null;
        try {
            DecimalFormat sdf = new DecimalFormat(format);
            if (value != null){
                number = sdf.parse(value);
                //TODO 12345 + #### = 12345 位数不对
                if (!value.equals(sdf.format(number))) {
                    number = null;
                }
            }

        } catch (ParseException ex) {
        }
        return number != null;
    }

    private static int getNumLength(long num){
        num = num>0?num:-num;
        if (num==0) {
            return 1;
        }
        return (int) Math.log10(num)+1;
    }
}
