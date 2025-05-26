package com.paintology.lite.trace.drawing.photoeditor.textviewBubble;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paintology.lite.trace.drawing.R;
import com.paintology.lite.trace.drawing.photoeditor.DovCharney.PatrickCox;

public class BubbleInputDialog extends Dialog {
    private final String defaultStr;
    private EditText et_bubble_input;
    private TextView tv_show_count;
    private TextView tv_action_done;
    private static final int MAX_COUNT = 33; //Word Count maximum limit of 33
    private Context mContext;
    private TextView textView;

    public BubbleInputDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        mContext = context;
        defaultStr = context.getResources().getString(R.string.double_click_input_text);
        initView();
    }

    public BubbleInputDialog(Context context, TextView view) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        mContext = context;
        defaultStr = context.getResources().getString(R.string.double_click_input_text);
        textView = view;
        initView();
    }

    public void setBubbleTextView(TextView textView) {
        this.textView = textView;
        if (defaultStr.equals(textView.getText())) {
            et_bubble_input.setText("");
        } else {
            et_bubble_input.setText(textView.getText());
            et_bubble_input.setSelection(textView.getText().length());
        }
    }


    private void initView() {
        setContentView(R.layout.view_input_dialog);
        tv_action_done = (TextView) findViewById(R.id.tv_action_done);
        et_bubble_input = (EditText) findViewById(R.id.et_bubble_input);
        tv_show_count = (TextView) findViewById(R.id.tv_show_count);
        et_bubble_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                long textLength = PatrickCox.calculateLength(s);
                tv_show_count.setText(String.valueOf(MAX_COUNT - textLength));
                if (textLength > MAX_COUNT) {
                    tv_show_count.setTextColor(mContext.getResources().getColor(R.color.red_e73a3d));
                } else {
                    tv_show_count.setTextColor(mContext.getResources().getColor(R.color.grey_8b8b8b));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_bubble_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    done();
                    return true;
                }
                return false;
            }
        });
        tv_action_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });
    }


    @Override
    public void show() {
        super.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager) et_bubble_input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                m.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        }, 500);

    }

    @Override
    public void dismiss() {
        super.dismiss();
        InputMethodManager m = (InputMethodManager) et_bubble_input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        m.hideSoftInputFromWindow(et_bubble_input.getWindowToken(), 0);
    }

    public interface CompleteCallBack {
        void onComplete(View bubbleTextView, String str);
    }

    private CompleteCallBack mCompleteCallBack;

    public void setCompleteCallBack(CompleteCallBack completeCallBack) {
        this.mCompleteCallBack = completeCallBack;
    }

    private void done() {
        if (Integer.valueOf(tv_show_count.getText().toString()) < 0) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.over_text_limit), Toast.LENGTH_SHORT).show();
            return;
        }
        dismiss();
        if (mCompleteCallBack != null) {
            String str;
            if (TextUtils.isEmpty(et_bubble_input.getText())) {
                str = "";
            } else {
                str = et_bubble_input.getText().toString();
            }
            mCompleteCallBack.onComplete(textView, str);
        }
    }
}
