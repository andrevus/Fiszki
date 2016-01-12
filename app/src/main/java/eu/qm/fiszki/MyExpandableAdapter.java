package eu.qm.fiszki;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import eu.qm.fiszki.activity.*;
import eu.qm.fiszki.activity.ItemAdapter;

/**
 * Created by Siusiacz on 29.12.2015.
 */
public class MyExpandableAdapter extends BaseExpandableListAdapter {

    private Activity activity;
    private ArrayList<Object> childtems1, childtems2;
    private LayoutInflater inflater;
    private ArrayList<String> parentItems, child1, child2;
    String selectedItem;

    public MyExpandableAdapter(ArrayList<String> parents, ArrayList<Object> childern1, ArrayList<Object> childern2) {
        this.parentItems = parents;
        this.childtems1 = childern1;
        this.childtems2 = childern2;
    }

    public void setInflater(LayoutInflater inflater, Activity activity) {
        this.inflater = inflater;
        this.activity = activity;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        child1 = (ArrayList<String>) childtems1.get(groupPosition);
        child2 = (ArrayList<String>) childtems2.get(groupPosition);
        TextView textWord = null;
        TextView textTranslation = null;
        if (child1.get(childPosition).equals("")) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.group, null);
            }
            textTranslation = (TextView) convertView.findViewById(R.id.translation);
            textWord = (TextView) convertView.findViewById(R.id.word);
            textWord.setGravity(Gravity.CENTER);
            textWord.setTextSize(25f);
            textWord.setText(R.string.main_activity_empty_group);
            textTranslation.setText("");
        } else {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.group, null);
            }
            textTranslation = (TextView) convertView.findViewById(R.id.translation);
            textWord = (TextView) convertView.findViewById(R.id.word);
            textWord.setGravity(Gravity.START);
            textWord.setTextSize(20f);
            textTranslation.setText(child2.get(childPosition));
            textWord.setText(child1.get(childPosition));

            MainActivity.dialog = new Dialog(MainActivity.context);
            MainActivity.dialog.setContentView(R.layout.layout_dialog_edit);
            MainActivity.dialog.setTitle(R.string.main_activity_dialog_edit_item);

            MainActivity.editOriginal = (EditText) MainActivity.dialog.findViewById(R.id.editOrginal);
            MainActivity.editTranslate = (EditText) MainActivity.dialog.findViewById(R.id.editTranslate);
            MainActivity.dialogButton = (Button) MainActivity.dialog.findViewById(R.id.editButton);

            MainActivity.emptyDBImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MainActivity.fab_all.isShown()) {
                        MainActivity.fab_all.hide();
                    }
                }
            });

            convertView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MainActivity.fab_all.isShown()) {
                        MainActivity.fab_all.hide();
                    }
                    return false;
                }
            });

            final View finalConvertView = convertView;
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (MainActivity.fab_all.isToolbar()) {
                        MainActivity.fab_all.hide();
                    }
                    selectedItem = child1.get(childPosition);
                    if (selectedItem.equals(child1.get(childPosition))){
                        finalConvertView.setBackgroundColor(activity.getResources().getColor(R.color.pressed_color));
                    }
                    return false;
                }
            });
        }
        return convertView;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row, null);
        }
        ((CheckedTextView) convertView).setText(parentItems.get(groupPosition));
        ((CheckedTextView) convertView).setChecked(isExpanded);
        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return ((ArrayList<String>) childtems1.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return parentItems.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }



    @Override

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
