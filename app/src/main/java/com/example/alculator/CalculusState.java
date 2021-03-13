package com.example.alculator;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.TextView;

import com.example.alculator.operations.Addition;
import com.example.alculator.operations.Divison;
import com.example.alculator.operations.Multiplication;
import com.example.alculator.operations.Operation;
import com.example.alculator.operations.Subtraction;

import java.text.DecimalFormat;
import java.util.HashMap;

public class CalculusState implements Parcelable {
    private Operation mCurrentOperation;
    private String mFirstOperand;
    private String mSecondOperand;
    private String mResult;
    private boolean mWorkWithResult;
    private int mCurrentElement;
    private HashMap<String, Operation> mAvailableOperaions;
    private TextView mDisplay;
    private static final DecimalFormat RESULT_FORMAT = new DecimalFormat("#.##############################");

    public static final Creator<CalculusState> CREATOR = new Creator<CalculusState>() {
        @Override
        public CalculusState createFromParcel(Parcel in) {
            return new CalculusState(in);
        }

        @Override
        public CalculusState[] newArray(int size) {
            return new CalculusState[size];
        }
    };

    public CalculusState(TextView display) {
        mFirstOperand = "";
        mSecondOperand = "";
        mDisplay = display;
        refreshDisplay();
    }

    protected CalculusState(Parcel in) {
        mFirstOperand = in.readString();
        mSecondOperand = in.readString();
        mResult = in.readString();
        mWorkWithResult = in.readByte() != 0;
        mCurrentElement = in.readInt();
        if (mAvailableOperaions == null) {
            mAvailableOperaions = new HashMap<String, Operation>() {{
                put("/", new Divison());
                put("+", new Addition());
                put("*", new Multiplication());
                put("-", new Subtraction());
            }};
        }
        mCurrentOperation = mAvailableOperaions.get(in.readString());
    }

    public void setDisplay(TextView newView) {
        mDisplay = newView;
        refreshDisplay();
    }

    private void refreshDisplay() {
        mDisplay.setText(
                (mFirstOperand +
                        (mCurrentOperation != null ? mCurrentOperation.toString() : "") +
                        (mCurrentOperation == null ? "" : mSecondOperand) +
                        (mResult != null ? "=" + mResult : "")));
    }

    public void execute() {
        if (mCurrentOperation == null || mSecondOperand.isEmpty()) {
            return;
        }

        Double aOperand;
        Double bOperand;
        try {
            aOperand = Double.parseDouble(mFirstOperand);
        } catch (NumberFormatException e) {
            aOperand = 0d;
        }
        try {
            bOperand = Double.parseDouble(mSecondOperand);
        } catch (NumberFormatException e) {
            bOperand = 0d;
        }

        if (mCurrentOperation.validateOperation(aOperand, bOperand) == false) {
            mResult = mCurrentOperation.getResultForNonValidatedOperation();
        } else {
            Double result = mCurrentOperation.execute(aOperand, bOperand);
            mResult = RESULT_FORMAT.format(result);
        }
        refreshDisplay();
        resetVariables();
    }

    private void resetVariables() {
        mWorkWithResult = true;
        mSecondOperand = "";
        mCurrentElement = 0;
        mCurrentOperation = null;
        try {
            Double resultDouble = Double.parseDouble(mResult);
        } catch (NumberFormatException e) {
            mFirstOperand = "";
            mWorkWithResult = false;
            mResult = null;
            return;
        }
        mFirstOperand = mResult;
        mResult = null;
        mWorkWithResult = true;
    }

    public void determineOperation(String operation) {
        if (mAvailableOperaions == null) {
            mAvailableOperaions = new HashMap<String, Operation>() {{
                put("/", new Divison());
                put("+", new Addition());
                put("*", new Multiplication());
                put("-", new Subtraction());
            }};
        }
        if (mWorkWithResult) {
            mWorkWithResult = false;
        }
        if (mFirstOperand.isEmpty()) {
            return;
        }
        mCurrentOperation = mAvailableOperaions.get(operation);
        mCurrentElement = 1;
        refreshDisplay();
    }

    public void appendDigit(String digit) {
        if (mCurrentElement == 0) {

            if (mWorkWithResult) {
                mFirstOperand = "";
                mWorkWithResult = false;
            }

            if (mFirstOperand.equals("")) {
                if (digit.equals(".")){
                    mFirstOperand = mFirstOperand.concat(digit);
                } else {
                    mFirstOperand = digit;
                }
            } else {
                mFirstOperand = mFirstOperand.concat(digit);
            }
        }
        else if (mCurrentElement == 1) {
            if (mSecondOperand.equals("")) {
                if (digit.equals(".")){
                    mSecondOperand = mSecondOperand.concat(digit);
                } else {
                    mSecondOperand = digit;
                }
            } else {
                mSecondOperand = mSecondOperand.concat(digit);
            }
        }
        refreshDisplay();
    }

    public void changePosNeg() {
        if (mCurrentElement == 0) {
            if (mWorkWithResult) {
                mWorkWithResult = false;
            }
            if (mFirstOperand.equals("")) {
                return;
            } else if (mFirstOperand.contains("-")) {
                mFirstOperand = mFirstOperand.replace("-", "");
            } else {
                mFirstOperand = "-".concat(mFirstOperand);
            }
        } else  if (mCurrentElement == 1) {
            if (mSecondOperand.equals("")) {
                return;
            } else if (mSecondOperand.contains("-")) {
                mSecondOperand = mSecondOperand.replace("-", "");
            } else {
                mSecondOperand = "-".concat(mSecondOperand);
            }
        }
        refreshDisplay();
    }

    public void clearStatemenet() {
        mFirstOperand = "";
        mSecondOperand = "";
        mResult = null;
        mCurrentOperation = null;
        mCurrentElement = 0;
        mWorkWithResult = false;
        refreshDisplay();
    }

    public void clearLastDigit() {
        if (mCurrentElement == 0) {
            if (mFirstOperand.equals("")) {
                return;
            } else if (mFirstOperand.length() == 1) {
                mFirstOperand = "";
            } else {
                mFirstOperand = mFirstOperand.substring(0, mFirstOperand.length() - 1);
            }
        } else if (mCurrentElement == 1) {
            if (mSecondOperand.equals("")){
                mCurrentElement = 0;
                mCurrentOperation = null;
            } else if (mSecondOperand.length() == 1) {
                mSecondOperand = "";
            }
            else {
                mSecondOperand = mSecondOperand.substring(0, mSecondOperand.length()-1);
            }
        }

        refreshDisplay();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFirstOperand);
        dest.writeString(mSecondOperand);
        dest.writeString(mResult);
        dest.writeByte((byte) (mWorkWithResult ? 1 : 0));
        dest.writeInt(mCurrentElement);
        dest.writeString(mCurrentOperation.toString());
    }
}


