package eu.qm.fiszki;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;

import eu.qm.fiszki.activity.LearningModeActivity;

public class Alert {
    public void pass(final Context context, String message, String title,
                     String nameButton) {
        final AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setButton(nameButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                ((Activity) context).finish();

            }
        });
        alertDialog.show();
    }

    public void fail(final Context context, String orginalWord, String message, String messeageAgain,
                     String title,
                     String nameButton) {
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(Html.fromHtml(message + " " + "<b>" + orginalWord + "</b>" + ". " +
                "\n" + messeageAgain));
        alertDialog.setCancelable(false);
        alertDialog.setButton(nameButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

    public void buildAlert(String title, String message, String buttonText, Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }
    public void learningModePass(final Context context, String message, String title,
                                 String nameButton) {
        final AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setButton(nameButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }
    public void learningModeFail(final Context context, String orginalWord, String message, String title,
                                 String nameButton) {
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(Html.fromHtml(message + " " + "<b>" + orginalWord + "</b>"));
        alertDialog.setButton(nameButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

}
