package eu.qm.fiszki.activity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import eu.qm.fiszki.AlarmReceiver;
import eu.qm.fiszki.Alert;
import eu.qm.fiszki.MyExpandableAdapter;
import eu.qm.fiszki.R;
import eu.qm.fiszki.database.DBAdapter;
import eu.qm.fiszki.database.DBModel;
import eu.qm.fiszki.database.DBStatus;


public class MainActivity extends AppCompatActivity {

    static public SettingsActivity settings;
    static public DBAdapter myDb;
    static public DBStatus openDataBase;
    static public Alert alert;
    static public ImageView emptyDBImage;
    static public TextView emptyDBText;
    static public Context context;
    static public FloatingActionButton fab;
    static public Dialog dialog;
    static public EditText editOriginal;
    static public EditText editTranslate;
    static public EditText editCategory;
    static public Button dialogButton;
    static public int rowId;
    static public AlarmReceiver alarm;
    static public Toolbar toolbar;
    static public ImageView addNewCategories;
    static public ImageView addNewWord;
    static public FABToolbarLayout fab_all;
    static public ExpandableListView expandableList;
    static public ListView listView;
    static public View clickedView;
    static public String typeOfSelected;
    static public String typeCategory = "Category";
    static public String typeFlashcard = "Flashcard";
    List<String> child = new ArrayList<String>();
    ExpandableListCleaner elc;
    Cursor deletedRow,deletedCategory;
    private ArrayList<String> parentItems = new ArrayList<String>();
    private ArrayList<Object> childWord = new ArrayList<Object>();
    private ArrayList<Object> childTranslation = new ArrayList<Object>();
    private MyExpandableAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        settings = new SettingsActivity();
        elc = new ExpandableListCleaner();
        alarm = new AlarmReceiver();
        alert = new Alert();
        openDataBase = new DBStatus();
        myDb = new DBAdapter(this);
        context = this;
        addNewCategories = (ImageView) findViewById(R.id.add_category);
        addNewWord = (ImageView) findViewById(R.id.add_word);
        fab_all = (FABToolbarLayout) findViewById(R.id.fab_all);
        fab = (FloatingActionButton) findViewById(R.id.fabtoolbar_fab);
        emptyDBImage = (ImageView) findViewById(R.id.emptyDBImage);
        emptyDBText = (TextView) findViewById(R.id.emptyDBText);
        emptyDBImage.setImageResource(R.drawable.emptydb);
        settings.context = this;
        settings.alarmIntent = new Intent(this, AlarmReceiver.class);
        settings.pendingIntent = PendingIntent.getBroadcast(this, 0, settings.alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        settings.manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        expandableList = (ExpandableListView) findViewById(R.id.list);
        listView = (ListView) findViewById(R.id.uncategoryList);
        openDataBase.openDB(myDb);

        toolbarMainActivity();
        listViewSelect();

        if (myDb.getAllRows().getCount() > 0 || myDb.getAllCategories().getCount() > 0) {
            emptyDBImage.setVisibility(View.INVISIBLE);
            emptyDBText.setVisibility(View.INVISIBLE);
            expandableList.setVisibility(View.VISIBLE);
        } else {
            emptyDBImage.setVisibility(View.VISIBLE);
            emptyDBText.setVisibility(View.VISIBLE);
            expandableList.setVisibility(View.INVISIBLE);
        }

    }

    public void setCategories() {
        int x = 0;
        Cursor c = myDb.getAllCategories();
        if (c.getCount() > 0) {
            do {
                parentItems.add(c.getString(1));
                childWord.add(setWord(c.getInt(0)));
                childTranslation.add(setTranslation(c.getInt(0)));
                c.moveToNext();
                x++;
            } while (x != c.getCount());
        }
    }

    public List<String> setWord(int CategoryId) {
        Cursor c = myDb.getRowByCategory(CategoryId);
        List<String> child = new ArrayList<String>();
        if (c.getCount() > 0) {
            if (c.moveToFirst()) {
                do {
                    child.add(c.getString(1));
                } while (c.moveToNext());
            }
            c.close();
        } else {
            child.add("");
        }
        return child;
    }

    public List<String> setTranslation(int CategoryId) {
        Cursor c = myDb.getRowByCategory(CategoryId);
        List<String> child = new ArrayList<String>();
        if (c.getCount() > 0) {
            if (c.moveToFirst()) {
                do {
                    child.add(c.getString(2));
                } while (c.moveToNext());
            }
            c.close();
        } else {
            child.add("");
        }
        return child;
    }

    @Override
    public void onResume() {
        super.onResume();
        listViewPopulate();
        toolbarMainActivity();
        if (myDb.getAllRows().getCount() > 0 || myDb.getAllCategories().getCount() > 0) {
            emptyDBImage.setVisibility(View.INVISIBLE);
            emptyDBText.setVisibility(View.INVISIBLE);
            expandableList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_selected_mainactivity, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            fab.show();
            clickedView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            toolbarMainActivity();
        }
        return true;
    }

    public void listViewPopulate() {
        parentItems.clear();
        childWord.clear();
        childTranslation.clear();
        child.clear();
        setCategories();
        adapter = new MyExpandableAdapter(parentItems, childWord, childTranslation);
        adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        expandableList.setDividerHeight(2);
        expandableList.setGroupIndicator(null);
        expandableList.setClickable(true);
        expandableList.setEmptyView(null);
        expandableList.setAdapter(adapter);
        if (myDb.getAllRows().getCount() > 0) {
            ItemAdapter flashCardList = new ItemAdapter(this, myDb.getAllRows(), myDb, this);
            listView.setAdapter(flashCardList);
        }

    }

    public void listViewSelect() {
        dialog = new Dialog(context);

        emptyDBImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fab_all.isShown()) {
                    fab_all.hide();
                }
            }
        });

        expandableList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (fab_all.isShown()) {
                    fab_all.hide();
                }
                return false;
            }
        });

        expandableList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                                      @Override
                                                      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                                          if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                                                              int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                                                              int childPosition = ExpandableListView.getPackedPositionChild(id);

                                                              dialog.setContentView(R.layout.layout_dialog_edit_flashcard);
                                                              dialog.setTitle(R.string.main_activity_dialog_edit_item);

                                                              editOriginal = (EditText) dialog.findViewById(R.id.editOrginal);
                                                              editTranslate = (EditText) dialog.findViewById(R.id.editTranslate);
                                                              dialogButton = (Button) dialog.findViewById(R.id.editButton);

                                                              typeOfSelected = typeFlashcard;
                                                              toolbarSelected();
                                                              editOriginal.setText(adapter.getChildWord(groupPosition, childPosition));
                                                              editTranslate.setText(adapter.getChildTranslate(groupPosition, childPosition));
                                                              rowId = myDb.getRowIdByWord(editOriginal.getText().toString());

                                                              if (rowId > -1) {
                                                                  if (fab_all.isToolbar()) {
                                                                      fab_all.hide();
                                                                  }

                                                                  if (clickedView != null) {
                                                                      clickedView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                                                                  }
                                                                  clickedView = view;
                                                                  clickedView.setBackgroundColor(getResources().getColor(R.color.pressed_color));
                                                                  fab.hide();
                                                              }
                                                          }
                                                          if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                                                              int groupPosition = ExpandableListView.getPackedPositionGroup(id);

                                                              dialog.setContentView(R.layout.layout_dialog_edit_category);
                                                              dialog.setTitle(R.string.main_activity_dialog_edit_category);

                                                              editCategory = (EditText) dialog.findViewById(R.id.editCategory);
                                                              dialogButton = (Button) dialog.findViewById(R.id.editButton);

                                                              typeOfSelected = typeCategory;
                                                              toolbarSelected();
                                                              editCategory.setText(adapter.getGroupName(groupPosition));
                                                              rowId = myDb.getCategoryId(editCategory.getText().toString()).getInt(0);
                                                              if (clickedView != null) {
                                                                  clickedView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                                                              }
                                                              clickedView = view;
                                                              clickedView.setBackgroundColor(getResources().getColor(R.color.pressed_color));
                                                              fab.hide();

                                                          }
                                                          return true;
                                                      }
                                                  }

        );

        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()

        {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition,
                                        long id) {
                if (clickedView != null) {
                    fab.show();
                    clickedView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    toolbarMainActivity();
                }
                return false;
            }
        });
    }

    public void toolbarMainActivity() {
        toolbar.getMenu().clear();
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setBackgroundResource(R.color.ColorPrimary);
        toolbar.setNavigationIcon(null);
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.settings) {
                            Intent goSettings = new Intent(MainActivity.this, SettingsActivity.class);
                            startActivity(goSettings);
                            finish();
                        } else if (id == R.id.examMode) {
                            if (myDb.getAllRows().getCount() > 0) {
                                Intent goExamMode = new Intent(MainActivity.this, ExamModeActivity.class);
                                startActivity(goExamMode);
                            } else {
                                alert.buildAlert(getString(R.string.alert_title_fail), getString(R.string.alert_learningmode_emptybase), getString(R.string.button_action_ok), MainActivity.this);
                            }
                        } else if (id == R.id.learningMode) {
                            if (myDb.getAllRows().getCount() > 0) {
                                Intent goLearningMode = new Intent(MainActivity.this, LearningModeActivity.class);
                                startActivity(goLearningMode);
                            } else {
                                alert.buildAlert(getString(R.string.alert_title_fail), getString(R.string.alert_learningmode_emptybase), getString(R.string.button_action_ok), MainActivity.this);
                            }
                        }
                        return true;
                    }
                });
        toolbar.dismissPopupMenus();
    }

    public void toolbarSelected() {


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getMenu().clear();
        toolbar.setTitle(getString(R.string.main_activity_title_seleced_record));
        toolbar.setBackgroundResource(R.color.seleced_Adapter);
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.editRecord) {
                            if (typeOfSelected.equals(typeFlashcard)) {
                                listViewEditFlashcard();
                            }
                            if (typeOfSelected.equals(typeCategory)) {
                                listViewEditCategory();
                            }
                        } else if (id == R.id.deleteRecord) {
                            if (typeOfSelected.equals(typeFlashcard)) {
                                listViewDeleteFlashcard();
                            }
                            if (typeOfSelected.equals(typeCategory)) {
                                listViewDeleteCategory();
                            }
                        } else if (id == android.R.id.home) {
                            clickedView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                            fab.show();
                            toolbarMainActivity();
                        }
                        return true;
                    }
                });
        toolbar.dismissPopupMenus();
    }

    public void listViewEditFlashcard() {
        editOriginal.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(editOriginal, 0);
            }
        }, 50);
        editOriginal.setSelection(editOriginal.getText().length());

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editOriginal.getText().toString().isEmpty() || editTranslate.getText().toString().isEmpty()) {
                    alert.buildAlert(getString(R.string.alert_title), getString(R.string.alert_message_onEmptyFields), getString(R.string.button_action_ok), MainActivity.this);
                } else {
                    if (myDb.getRow(rowId).getString(1).equals(editOriginal.getText().toString())) {
                        myDb.updateAdapter(rowId, editOriginal.getText().toString(), editTranslate.getText().toString());
                        fab.show();
                        listViewPopulate();
                        dialog.dismiss();
                        toolbarMainActivity();
                    } else {
                        if (myDb.getRowValue(DBModel.KEY_WORD, editOriginal.getText().toString())) {
                            alert.buildAlert(getString(R.string.alert_title), getString(R.string.alert_message_onRecordExist), getString(R.string.button_action_ok), MainActivity.this);
                            editOriginal.requestFocus();
                        } else {
                            myDb.updateAdapter(rowId, editOriginal.getText().toString(), editTranslate.getText().toString());
                            fab.show();
                            listViewPopulate();
                            dialog.dismiss();
                            toolbarMainActivity();
                        }
                    }
                }
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    public void listViewEditCategory() {
        editCategory.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(editCategory, 0);
            }
        }, 50);
        editCategory.setSelection(editCategory.getText().length());

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editCategory.getText().toString().isEmpty()) {
                    alert.buildAlert(getString(R.string.alert_title), getString(R.string.alert_message_onEmptyFields), getString(R.string.button_action_ok), MainActivity.this);
                } else {
                    if (myDb.getCategories(rowId).getString(1).equals(editCategory.getText().toString())) {
                        myDb.updateCategory(rowId, editCategory.getText().toString());
                        fab.show();
                        listViewPopulate();
                        dialog.dismiss();
                        toolbarMainActivity();
                    } else {
                        if (myDb.getCategoryValue(DBModel.CATEGORY_NAME, editCategory.getText().toString())) {
                            alert.buildAlert(getString(R.string.alert_title), getString(R.string.alert_message_onRecordExist), getString(R.string.button_action_ok), MainActivity.this);
                            editCategory.requestFocus();
                        } else {
                            myDb.updateCategory(rowId, editCategory.getText().toString());
                            fab.show();
                            listViewPopulate();
                            dialog.dismiss();
                            toolbarMainActivity();
                        }
                    }
                }
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    public void listViewDeleteFlashcard() {
        final AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(getString(R.string.alert_title));
        alertDialog.setCancelable(false);
        alertDialog.setMessage(Html.fromHtml(getString(R.string.alert_delete_record)));
        alertDialog.setButton(getString(R.string.button_action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletedRow = myDb.getRow(rowId);
                myDb.deleteFlashcardById(rowId);
                if (myDb.getAllRows().getCount() > 0) {
                    listViewPopulate();
                    toolbarMainActivity();
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), getString(R.string.snackbar_returnword_message), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.snackbar_returnword_button), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDb.insertRowWithId(deletedRow.getInt(0), deletedRow.getString(1),
                                            deletedRow.getString(2), deletedRow.getInt(3), deletedRow.getInt(4));
                                    listViewPopulate();
                                }
                            });
                    snackbar.show();
                    fab.setVisibility(View.VISIBLE);
                } else {
                    emptyDBImage.setVisibility(View.VISIBLE);
                    emptyDBText.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.INVISIBLE);
                    fab.show();
                    myDb.updateRow(settings.notificationStatus, 0);
                    myDb.updateRow(settings.notificationPosition, 0);
                    alarm.close(settings.manager, context, settings.pendingIntent);
                    toolbarMainActivity();
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), getString(R.string.snackbar_returnword_message), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.snackbar_returnword_button), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDb.insertRowWithId(deletedRow.getInt(0), deletedRow.getString(1),
                                            deletedRow.getString(2), deletedRow.getInt(3), deletedRow.getInt(4));
                                    emptyDBImage.setVisibility(View.INVISIBLE);
                                    emptyDBText.setVisibility(View.INVISIBLE);
                                    listView.setVisibility(View.VISIBLE);
                                    listViewPopulate();
                                }
                            });
                    snackbar.show();
                    fab.setVisibility(View.VISIBLE);
                }
            }
        });
        alertDialog.setButton2(getString(R.string.button_action_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }

    public void listViewDeleteCategory() {
        final AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(getString(R.string.alert_title));
        alertDialog.setCancelable(false);
        alertDialog.setMessage(Html.fromHtml(getString(R.string.alert_delete_record)));
        alertDialog.setButton(getString(R.string.button_action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletedCategory = myDb.getCategories(rowId);
                deletedRow = myDb.getRowByCategory(rowId);
                myDb.deleteCategory(rowId);
                myDb.deleteFlashcardByCategoryId(rowId);
                if (myDb.getAllRows().getCount() > 0) {
                    listViewPopulate();
                    toolbarMainActivity();
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), getString(R.string.snackbar_returnword_message), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.snackbar_returnword_button), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDb.insertCategoryWithId(deletedCategory.getInt(0), deletedCategory.getString(1));
                                    deletedRow.moveToFirst();
                                    do{
                                        myDb.insertRowWithId(deletedRow.getInt(0),deletedRow.getString(1),deletedRow.getString(2),deletedRow.getInt(3), deletedRow.getInt(4));
                                    }while(deletedRow.moveToNext());
                                    listViewPopulate();
                                }
                            });
                    snackbar.show();
                    fab.setVisibility(View.VISIBLE);
                } else {
                    emptyDBImage.setVisibility(View.VISIBLE);
                    emptyDBText.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.INVISIBLE);
                    fab.show();
                    myDb.updateRow(settings.notificationStatus, 0);
                    myDb.updateRow(settings.notificationPosition, 0);
                    alarm.close(settings.manager, context, settings.pendingIntent);
                    toolbarMainActivity();
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), getString(R.string.snackbar_returnword_message), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.snackbar_returnword_button), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDb.insertRowWithId(deletedRow.getInt(0), deletedRow.getString(1),
                                            deletedRow.getString(2), deletedRow.getInt(3), deletedRow.getInt(4));
                                    emptyDBImage.setVisibility(View.INVISIBLE);
                                    emptyDBText.setVisibility(View.INVISIBLE);
                                    listView.setVisibility(View.VISIBLE);
                                    listViewPopulate();
                                }
                            });
                    snackbar.show();
                    fab.setVisibility(View.VISIBLE);
                }
            }
        });
        alertDialog.setButton2(getString(R.string.button_action_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }

    public void add_new_categories(View view) {
        fab_all.hide();
        Intent addNewCategory = new Intent(this, AddCategoryActivity.class);
        startActivity(addNewCategory);
    }

    public void add_new_word(View view) {
        fab_all.hide();
        Intent addNewWord = new Intent(this, AddWordActivity.class);
        startActivity(addNewWord);
    }
}

