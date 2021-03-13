package com.example.alculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mResultView;
    private CalculusState mCalculusState;
    private HashSet<String> mOperandParts;
    private HashSet<String> mOperations;
    private static final String CALCULUS_STATE_NAMESPACE = "CALCULUS_STATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mResultView = findViewById(R.id.tvMain);
        mCalculusState = new CalculusState(mResultView);
        initializeKeyHashes();
        setListenerForButtons();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CALCULUS_STATE_NAMESPACE, mCalculusState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        mCalculusState = savedInstanceState.getParcelable(CALCULUS_STATE_NAMESPACE);
        mCalculusState.setDisplay(mResultView);
        super.onRestoreInstanceState(savedInstanceState);

    }

    private void initializeKeyHashes() {
        mOperandParts = new HashSet<String>() {{
            add(getResources().getString(R.string.button0));
            add(getResources().getString(R.string.button1));
            add(getResources().getString(R.string.button2));
            add(getResources().getString(R.string.button3));
            add(getResources().getString(R.string.button4));
            add(getResources().getString(R.string.button5));
            add(getResources().getString(R.string.button6));
            add(getResources().getString(R.string.button7));
            add(getResources().getString(R.string.button8));
            add(getResources().getString(R.string.button9));
            add(getResources().getString(R.string.buttonPt));
        }};

        mOperations = new HashSet<String>() {{
            add(getResources().getString(R.string.buttonDivision));
            add(getResources().getString(R.string.multiplication));
            add(getResources().getString(R.string.plus));
            add(getResources().getString(R.string.minus));
        }};
    }


    private void setListenerForButtons() {
        ViewGroup rootView = findViewById(android.R.id.content);
        ArrayList<Button> buttons = buttonsOfViewGroupRecursevly(rootView);
        for (Button key : buttons) {
            key.setOnClickListener(this);
        }
    }

    private ArrayList<Button> buttonsOfViewGroupRecursevly(ViewGroup view) {
        ArrayList<Button> buttons = new ArrayList<>();
        processViewGroup(view, buttons);
        return buttons;
    }

    private void processViewGroup(ViewGroup view, ArrayList<Button> buttons) {
        for (int i = 0; i < view.getChildCount(); i++) {
            View element = view.getChildAt(i);
            if (element instanceof Button) {
                buttons.add((Button) element);
            } else if (element instanceof ViewGroup) {
                processViewGroup((ViewGroup) element, buttons);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (!(v instanceof Button)) {
            throw new RuntimeException("Listener on click was set up to wrong type view. It should be Button.");
        }
        String pressedValue = ((Button) v).getText().toString();
        if (mOperandParts.contains(pressedValue)) {
            mCalculusState.appendDigit(pressedValue);
        } else if (pressedValue.equals(getResources().getString(R.string.buttonNegative))) {
            mCalculusState.changePosNeg();
        } else if (mOperations.contains(pressedValue)) {
            mCalculusState.determineOperation(pressedValue);
        } else if (pressedValue.equals(getResources().getString(R.string.stringEqually))) {
            mCalculusState.execute();
        } else if (pressedValue.equals(getResources().getString(R.string.ac))) {
            mCalculusState.clearStatemenet();
        } else if (pressedValue.equals(getResources().getString(R.string.buttonDel))) {
            mCalculusState.clearLastDigit();
//        } else if (pressedValue.equals(getResources().getString(R.string.clear_number))) {
//            mCalculusState.clearCurrentOperand();
        } else {
            throw new UnsupportedOperationException();
        }

    }
}