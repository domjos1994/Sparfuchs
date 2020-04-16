package de.domjos.sparfuchs.utils.validator;

import javafx.scene.control.Control;
import org.controlsfx.validation.ValidationMessage;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.Validator;

import java.math.BigInteger;
import java.util.ResourceBundle;

public class IBanValidator implements Validator<String> {
    private ResourceBundle bundle;

    public IBanValidator(ResourceBundle bundle) {
        super();

        this.bundle = bundle;
    }

    @Override
    public ValidationResult apply(Control control, String s) {
        ValidationResult validationResult = new ValidationResult();
        if(s.trim().length() != 22) {
            validationResult.add(ValidationMessage.error(control, this.bundle.getString("validation.iban.length")));
            return validationResult;
        }
        if(!checkCheckNumber(s)) {
            validationResult.add(ValidationMessage.error(control, this.bundle.getString("validation.iban.check")));
        }
        return validationResult;
    }

    private boolean checkCheckNumber(String s) {
        String tempNumber = "";
        // step 1
        tempNumber = s.substring(4) + s.substring(0, 2) + "00";

        // step 2
        StringBuilder tempStep2 = new StringBuilder();
        for(int i = 0; i<=tempNumber.length()-1; i++) {
            if(this.isNumber(tempNumber.toCharArray()[i]) || String.valueOf(tempNumber.toCharArray()[i]).equals(" ")) {
                tempStep2.append(tempNumber.toCharArray()[i]);
            } else {
                tempStep2.append(this.findPosition(tempNumber.toCharArray()[i]));
            }
        }
        tempNumber = tempStep2.toString().replace(" ", "");

        // step 3
        BigInteger value = new BigInteger(tempNumber).mod(new BigInteger("97"));

        // step 4
        BigInteger checkNumber = new BigInteger("98").subtract(value);

        return checkNumber.toString().equals(s.substring(2, 4));
    }

    private boolean isNumber(char input) {
        try {
            Integer.parseInt(String.valueOf(input));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public int findPosition(char inputLetter) {
        char inputLetterToLowerCase= Character.toLowerCase(inputLetter);
        return ((int) inputLetterToLowerCase - 96) + 9;
    }
}
