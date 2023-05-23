package com.sudhirtheindian4.newinstagramclone.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.sudhirtheindian4.newinstagramclone.R;

public class ConfirmPasswordDialog extends DialogFragment {

    public static final String TAG = "ConfirmPasswordDialog";

    public interface OnConfirmPasswordListener {
        public void onConfirmPassword(String password);
    }

    OnConfirmPasswordListener mOnConfirmPasswordListener;

    //    variables
    TextView mPassword;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm_password, container, false);
        Log.d(TAG, "oncreateview: oncreated started");

        mPassword = view.findViewById(R.id.confirm_password);

        TextView confirmDialog = view.findViewById(R.id.dialogConfirm);
        confirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onclick: captured password and confirming");

                String password = mPassword.getText().toString();
                if (!password.equals("")) {
                    mOnConfirmPasswordListener.onConfirmPassword(password);
                    getDialog().dismiss();
                } else {
                    Toast.makeText(getActivity(), "you must enter password", Toast.LENGTH_SHORT).show();
                }
            }
        });


        TextView cancelDialog = view.findViewById(R.id.dialogCancel);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onclick: cloasing the dialog");
                getDialog().dismiss();
            }
        });

        return view;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOnConfirmPasswordListener = (OnConfirmPasswordListener) getTargetFragment();

        } catch (ClassCastException e) {
            Log.d(TAG, "onAttach: ClassCastException " + e.getMessage());

        }
    }

}
