package com.example.alculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static Button btnMinus;
    private static Button btnPlus;
    private static Button btnDiv;
    private static Button btnEquals;
    private static Button btnMultiplication;
    private static Button btnAC;
    private static Button btnPT;
    private static ImageButton btnDel;
    private static Button btnPerсent;
    private static TextView tvResult;
    private static EditText etNumber;
    private static Double operand = null;
    private static String lastOperation = "=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        btnMinus = findViewById(R.id.minus);
        btnDiv = findViewById(R.id.division);
        btnPlus = findViewById(R.id.plus);
        btnEquals = findViewById(R.id.buttonEqually);
        btnMultiplication = findViewById(R.id.multiply);
        btnPerсent = findViewById(R.id.buttonPercent);
        btnPT = findViewById(R.id.buttonPt);
        btnAC = findViewById(R.id.buttonAC);
        btnDel = findViewById(R.id.buttonDel);
        tvResult = findViewById(R.id.tvMain);
        etNumber = findViewById(R.id.etMain);
    }

    // обработка нажатия на числовую кнопку
    public void onNumberClick(View view){

        Button button = (Button)view;
        etNumber.append(button.getText());

        if(lastOperation.equals("=") && operand!=null){
            operand = null;
        }
    }

    // обработка нажатия на кнопку операции
    public void onOperationClick(View view){

        Button button = (Button)view;
        String op = null;
        if (button.getText().toString().equals("x")) {
            op = "*";
        }

        if (button.getText().toString().equals("/")) {
            op = "/";
        }

        if (button.getText().toString().equals("+")) {
            op = "+";
        }

        if (button.getText().toString().equals("-")) {
            op = "-";
        }

        if (button.getText().toString().equals("=")) {
            op = "=";
        }
        String number = etNumber.getText().toString();
        // если введенно что-нибудь
        if(number.length()>0){
            number = number.replace(',', '.');
            try{
                performOperation(Double.valueOf(number), op);
            }catch (NumberFormatException ex){
                tvResult.setText("");
            }
        }
        lastOperation = op;
        tvResult.setText(lastOperation);
    }

    private void performOperation(Double number, String operation){

        // если операнд ранее не был установлен (при вводе самой первой операции)
        if(operand ==null){
            operand = number;
        }
        else{
            if(lastOperation.equals("=")){
                lastOperation = operation;
            }
            switch(lastOperation){
                case "=":
                    operand =number;
                    break;
                case "/":
                    if(number==0){
                        operand =0.0;
                    }
                    else{
                        operand /=number;
                    }
                    break;
                case "*":
                    operand *=number;
                    break;
                case "+":
                    operand +=number;
                    break;
                case "-":
                    operand -=number;
                    break;
            }
        }
        tvResult.setText(operand.toString().replace('.', ','));
        tvResult.setText("");
    }

    // сохранение состояния
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("OPERATION", lastOperation);
        if(operand!=null)
            outState.putDouble("OPERAND", operand);
        super.onSaveInstanceState(outState);
    }

    // получение ранее сохраненного состояния
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastOperation = savedInstanceState.getString("OPERATION");
        operand= savedInstanceState.getDouble("OPERAND");
        tvResult.setText(operand.toString()+lastOperation);
    }
}