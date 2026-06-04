package laki.webide.core;

import android.content.Context;
import com.google. android.material.textfield.TextInputLayout;
import android.text.Spanned;
import java.util.ArrayList;
import java.util.regex.Pattern;

import laki.webide.R;

public class VariableNameValidator extends BaseInputValidator {
    private ArrayList<String> alreadyUsed;
    private String exception;
    private String[] keywords;
    Pattern ps = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]*");
    private String[] usedwords;

    public VariableNameValidator(Context context, TextInputLayout textInputLayout, String[] strArr, String[] strArr2, ArrayList<String> arrayList) {
        super(context, textInputLayout);
        this.keywords = strArr;
        this.usedwords = strArr2;
        this.alreadyUsed = arrayList;
    }

    public VariableNameValidator(Context context, TextInputLayout textInputLayout, String[] strArr, String[] strArr2, ArrayList<String> arrayList, String str) {
        super(context, textInputLayout);
        this.keywords = strArr;
        this.usedwords = strArr2;
        this.alreadyUsed = arrayList;
        this.exception = str;
    }

    public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
        return null;
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        if (charSequence.toString().trim().length() < 1) {
            this.textInputLayout.setErrorEnabled(true);
            this.textInputLayout.setError(String.format("Use at least %d letters", 1));
            this.valid = false;
        } else if (charSequence.toString().trim().length() > 20) {
            this.textInputLayout.setErrorEnabled(true);
            this.textInputLayout.setError(String.format("You can use maximum %d letters", 20));
            this.valid = false;
        } else if (this.exception != null && this.exception.length() > 0 && charSequence.toString().equals(this.exception)) {
            this.valid = true;
        } else if (this.alreadyUsed.indexOf(charSequence.toString()) >= 0) {
            this.textInputLayout.setErrorEnabled(true);
            this.textInputLayout.setError("Current name is unavailable");
            this.valid = false;
        } else {
            boolean z;
            for (Object equals : this.usedwords) {
                if (charSequence.toString().equals(equals)) {
                    z = true;
                    break;
                }
            }
            z = false;
            if (z) {
                this.textInputLayout.setErrorEnabled(true);
                this.textInputLayout.setError("Current name is unavailable");
                this.valid = false;
                return;
            }
            for (Object equals2 : this.keywords) {
                if (charSequence.toString().equals(equals2)) {
                    z = true;
                    break;
                }
            }
            z = false;
            if (z) {
                this.textInputLayout.setErrorEnabled(true);
                this.textInputLayout.setError("Can't use reserved keywords");
                this.valid = false;
            } else if (!Character.isLetter(charSequence.charAt(0))) {
                this.textInputLayout.setErrorEnabled(true);
                this.textInputLayout.setError("The variable name must start with a letter");
                this.valid = false;
            } else if (this.ps.matcher(charSequence.toString()).matches()) {
                this.textInputLayout.setErrorEnabled(false);
                this.valid = true;
            } else {
                this.textInputLayout.setErrorEnabled(true);
                this.textInputLayout.setError("Only use letters(a-z), numbers and Special characters(_)");
                this.valid = false;
            }
        }
    }

    public void setUsedWords(String[] strArr) {
        this.usedwords = strArr;
    }
}
