package com.example.android.materialdesigncodelab.ui.components;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.materialdesigncodelab.R;

/**
 * Created by lixindong on 11/14/16.
 */

public class CustomLocationDialog {

    public interface OnDialogClickedListener {
        void onOk(View v, String latitude, String longitude);
        void onCancel(View v);
    }

    public static void showDialog(Activity activity, final OnDialogClickedListener listener) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_custom_location);

        Button ok = (Button) dialog.findViewById(R.id.ok);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);

        final EditText latitude = (EditText) dialog.findViewById(R.id.latitude);
        final EditText longitude = (EditText) dialog.findViewById(R.id.longitude);

        if (listener != null) {
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onOk(v, latitude.getText().toString(), longitude.getText().toString());
                    dialog.dismiss();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCancel(v);
                    dialog.dismiss();
                }
            });
        }

        dialog.show();
    }
}
