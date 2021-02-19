package com.hasawi.sears_outlet.ui.main.view.signin.login;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.hasawi.sears_outlet.R;
import com.hasawi.sears_outlet.databinding.FragmentOtpBinding;
import com.hasawi.sears_outlet.ui.base.BaseFragment;
import com.hasawi.sears_outlet.ui.main.view.signin.SigninActivity;

public class VerifyOtpFragment extends BaseFragment {

    FragmentOtpBinding fragmentOtpBinding;
    SigninActivity signinActivity;
    CountDownTimer cTimer = null;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_otp;
    }

    @Override
    protected void setup() {
        fragmentOtpBinding = (FragmentOtpBinding) viewDataBinding;
        signinActivity = (SigninActivity) getActivity();
        //GenericTextWatcher here works only for moving to next EditText when a number is entered
//first parameter is the current EditText and second parameter is next EditText
        fragmentOtpBinding.edtOtp1.addTextChangedListener(new GenericTextWatcher(fragmentOtpBinding.edtOtp1, fragmentOtpBinding.edtOtp2));
        fragmentOtpBinding.edtOtp2.addTextChangedListener(new GenericTextWatcher(fragmentOtpBinding.edtOtp2, fragmentOtpBinding.edtOtp3));
        fragmentOtpBinding.edtOtp3.addTextChangedListener(new GenericTextWatcher(fragmentOtpBinding.edtOtp3, fragmentOtpBinding.edtOtp4));
        fragmentOtpBinding.edtOtp4.addTextChangedListener(new GenericTextWatcher(fragmentOtpBinding.edtOtp4, null));

//GenericKeyEvent here works for deleting the element and to switch back to previous EditText
//first parameter is the current EditText and second parameter is previous EditText
        fragmentOtpBinding.edtOtp1.setOnKeyListener(new GenericKeyEvent(fragmentOtpBinding.edtOtp1, null));
        fragmentOtpBinding.edtOtp2.setOnKeyListener(new GenericKeyEvent(fragmentOtpBinding.edtOtp2, fragmentOtpBinding.edtOtp1));
        fragmentOtpBinding.edtOtp3.setOnKeyListener(new GenericKeyEvent(fragmentOtpBinding.edtOtp3, fragmentOtpBinding.edtOtp2));
        fragmentOtpBinding.edtOtp4.setOnKeyListener(new GenericKeyEvent(fragmentOtpBinding.edtOtp4, fragmentOtpBinding.edtOtp3));
    }

    public void startTimer() {
        cTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                fragmentOtpBinding.tvTimer.setText("(0:" + millisUntilFinished / 1000 + ")");
            }

            public void onFinish() {
                // Todo Call Otp Resend
                fragmentOtpBinding.tvTimer.setVisibility(View.GONE);
            }
        };
        cTimer.start();
    }

    public void cancelTimer() {
        if (cTimer != null)
            cTimer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelTimer();
    }

    public class GenericKeyEvent implements View.OnKeyListener {
        private EditText currentView;
        private EditText previousView;

        public GenericKeyEvent(EditText currentView, EditText previousView) {
            this.currentView = currentView;
            this.previousView = previousView;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.getId() != R.id.edt_otp_1 && currentView.getText().toString().isEmpty()) {
                //If current is empty then previous EditText's number will also be deleted
                previousView.setText(null);
                previousView.requestFocus();
                return true;
            }
            return false;
        }
    }


    public class GenericTextWatcher implements TextWatcher {
        View currentView, nextView;

        public GenericTextWatcher(View currentView, View nextView) {
            this.currentView = currentView;
            this.nextView = nextView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            switch (currentView.getId()) {
                case R.id.edt_otp_1:
                case R.id.edt_otp_2:
                case R.id.edt_otp_3:
                    if (text.length() == 1)
                        nextView.requestFocus();
                    break;

            }
        }


    }


}
