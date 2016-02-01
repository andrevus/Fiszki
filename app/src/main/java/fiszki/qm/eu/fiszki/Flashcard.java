package fiszki.qm.eu.fiszki;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import fiszki.qm.eu.fiszki.database.DBAdapter;
import fiszki.qm.eu.fiszki.database.DBModel;
import fiszki.qm.eu.fiszki.database.DBStatus;

/**
 * Created by mBoiler on 30.01.2016.
 */
public class Flashcard {


    public int id;
    public String orginalWord;
    public String translationWord;
    public int priority;
    public String category;
    public boolean selected = false;
    public Context context;


    public Flashcard() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOrginalWord() {
        return orginalWord;
    }

    public void setOrginalWord(String orginalWord) {
        this.orginalWord = orginalWord;
    }

    public String getTranslationWord() {
        return translationWord;
    }

    public void setTranslationWord(String translationWord) {
        this.translationWord = translationWord;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
