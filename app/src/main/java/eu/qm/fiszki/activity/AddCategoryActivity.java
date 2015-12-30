package eu.qm.fiszki.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import eu.qm.fiszki.Alert;
import eu.qm.fiszki.R;
import eu.qm.fiszki.database.DBAdapter;
import eu.qm.fiszki.database.DBModel;
import eu.qm.fiszki.database.DBStatus;

public class AddCategoryActivity extends AppCompatActivity {

    EditText addCategoryEditText;
    DBAdapter myDb = new DBAdapter(this);
    DBStatus OpenDataBase = new DBStatus();
    Alert alert;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        addCategoryEditText = (EditText) findViewById(R.id.addCategoryEditText);
        alert = new Alert();
        context = this;
        OpenDataBase.openDB(myDb);
        clickDone();

        
    }

    @Override
    public void onResume() {
    super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addcategory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.add_new_category_title){
            if(addCategoryEditText.getText().toString().isEmpty()) {
                alert.buildAlert(getString(R.string.alert_title),
                        getString(R.string.alert_message_onEmptyField),
                        getString(R.string.button_action_ok), this);
            /* }else if (myDb.getCategoryValue(DBModel.CATEGORY_NAME,addCategoryEditText.getText().toString())){
                alert.buildAlert(getString(R.string.alert_title),
                        getString(R.string.alert_message_onEmptyField),
                        getString(R.string.button_action_ok), this); */
            }else{
                myDb.insertCategory(addCategoryEditText.getText().toString());
                addCategoryEditText.setText("");
                Toast.makeText(this,getString(R.string.add_new_category_toast),
                        Toast.LENGTH_SHORT).show();
            }


        }
        return true;
    }

    public void clickDone(){
        addCategoryEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    if(addCategoryEditText.getText().toString().isEmpty()) {
                        alert.buildAlert(getString(R.string.alert_title),
                                getString(R.string.alert_message_onEmptyField),
                                getString(R.string.button_action_ok), AddCategoryActivity.this);
            /* }else if (myDb.getCategoryValue(DBModel.CATEGORY_NAME,addCategoryEditText.getText().toString())){
                alert.buildAlert(getString(R.string.alert_title),
                        getString(R.string.alert_message_onEmptyField),
                        getString(R.string.button_action_ok), this); */
                    }else{
                        myDb.insertCategory(addCategoryEditText.getText().toString());
                        addCategoryEditText.setText("");
                        Toast.makeText(context,getString(R.string.add_new_category_toast),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
    }

}
