package com.lordnash.stormy.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.lordnash.stormy.R;

/**
 * Created by Bacha on 7/21/2015.
 */
public class AlertDialogFragment extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context =getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
        .setTitle(context.getString(R.string.error_Title))
                .setMessage(context.getString(R.string.error_Message))
                .setPositiveButton(context.getString(R.string.error_Button), null);
        AlertDialog dialog = builder.create();
        return dialog;

    }

}
