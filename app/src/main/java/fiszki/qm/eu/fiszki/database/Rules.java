package fiszki.qm.eu.fiszki.database;

import android.app.Activity;
import android.widget.EditText;

import fiszki.qm.eu.fiszki.Alert;
import fiszki.qm.eu.fiszki.Flashcard;
import fiszki.qm.eu.fiszki.FlashcardsManagment;
import fiszki.qm.eu.fiszki.R;

/**
 * Created by mBoiler on 31.01.2016.
 */
public class Rules {

    Alert alert = new Alert();
    FlashcardsManagment flashcardsManagment;

    public boolean addNewWord(EditText editText, EditText editText2, Activity activity) {
        flashcardsManagment = new FlashcardsManagment(activity.getBaseContext());
        if (editText.getText().toString().isEmpty() || editText2.getText().toString().isEmpty()) {
            alert.buildAlert(activity.getString(R.string.alert_title),
                    activity.getString(R.string.alert_message_onEmptyFields),
                    activity.getString(R.string.button_action_ok), activity);
            return false;
        }
        if (!flashcardsManagment.existence(editText.getText().toString())) {
            alert.buildAlert(activity.getString(R.string.alert_title),
                    activity.getString(R.string.alert_message_onRecordExist),
                    activity.getString(R.string.button_action_ok), activity);
            editText.setText(null);
            editText2.setText(null);
            editText.requestFocus();
            return false;
        }
        return true;
    }


}
