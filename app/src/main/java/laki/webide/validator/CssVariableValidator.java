package laki.webide.validator;

import android.content.Context;
import android.text.Spanned;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;
import java.util.regex.Pattern;
import a.a.a.MB;
import laki.webide.R;

public class CssVariableValidator extends MB {

    private static final Pattern VALID_CSS_NAME = Pattern.compile("^[a-zA-Z0-9_-]+$");
    private final ArrayList<String> excludedNames;

    public CssVariableValidator(Context context, TextInputLayout textInputLayout, ArrayList<String> excludedNames) {
        super(context, textInputLayout);
        this.excludedNames = excludedNames;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        return null; // No special filtering needed
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String input = s.toString().trim();
        
        if (input.isEmpty()) {
            b.setErrorEnabled(true);
            b.setError(a.getString(R.string.invalid_value_min_lenth, 1));
            d = false;
            return;
        }

        // We check against excluded names both as-is and with -- prefix
        if (excludedNames.contains(input) || excludedNames.contains("--" + input)) {
            b.setErrorEnabled(true);
            b.setError(a.getString(R.string.common_message_name_unavailable));
            d = false;
            return;
        }

        if (VALID_CSS_NAME.matcher(input).matches()) {
            b.setError(null);
            b.setErrorEnabled(false);
            d = true;
        } else {
            b.setErrorEnabled(true);
            b.setError("Invalid name (only letters, numbers, _ and - allowed)");
            d = false;
        }
    }
}
