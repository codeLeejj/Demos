package com.annis.mydemos.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.annis.mydemos.R;

public class CarNumberSelectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_number_selector);
        EditText editText = findViewById(R.id.et);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                handler(s.toString());
            }
        });

        textView = findViewById(R.id.tv);

    }

    TextView textView;

    private void handler(String string) {
        String[] split = string.split("-");
        int[] numbers = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            try {
                int carNumber = Integer.parseInt(split[i]);
                numbers[i] = carNumber;
            } catch (NumberFormatException e) {
                continue;
            }
        }
        textView.setText(select(numbers));
    }

    final static int[] goodNumber = new int[]{1, 3, 5, 6, 7, 8, 11, 13, 15, 16, 17, 18, 21, 23, 24, 25,
            29, 31, 32, 33, 35, 37, 39, 41, 45, 47, 48, 52, 63, 65, 67, 73, 81};
    final static int[] goodWithBad = new int[]{27, 30, 40, 42, 43, 50, 51, 55, 61, 71, 75, 77, 80};
    final static int[] badWithGood = new int[]{38, 57, 58,};

    private static String select(int[] numbers) {
        StringBuffer buffer = new StringBuffer();
        for (int number : numbers) {
            String judge = judge(number);
            buffer.append(String.format("%5d : %s %s", number, dayOfWeekBelLimit(number), judge));
            buffer.append("\n");
        }
        return buffer.toString();
    }

    /**
     * 一周限号时间
     *
     * @param number
     */
    private static String dayOfWeekBelLimit(int number) {
        int value = number % 10 % 5;
        value = value == 0 ? 5 : value;
        return "周" + value + "限号";
    }

    /**
     * 判断凶吉
     *
     * @param number
     * @return
     */
    private static String judge(int number) {
        int value = (int) ((number / 80.0f % 1) * 80);
        for (int item : goodNumber) {
            if (item == value) {
                return "吉";
            }
        }

        for (int item : goodWithBad) {
            if (item == value) {
                return "吉带凶";
            }
        }

        for (int item : badWithGood) {
            if (item == value) {
                return "凶带吉";
            }
        }
        return "凶";
    }
}