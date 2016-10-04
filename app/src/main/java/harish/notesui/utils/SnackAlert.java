package harish.notesui.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackAlert {
    public static void showAlert(View v, String message) {
        Snackbar.make(v, message, Snackbar.LENGTH_SHORT).show();
    }
}
