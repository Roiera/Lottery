package com.example.roie.lotto;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback{
    private LottoData guessedData;
    private static final int MAX_NUMBER = 40;
    private static final int MAX_EXTRA_NUMBER = 7;
    private static final int MIN_NUMBER = 1;
    private static final int NUMBER_OF_NUMS = 6;
    private static final int REQUEST_APP_PERMISSIONS = 0;
    private static final String TAG="Lotto";

    private static final String[] PERMISSIONS_APP = {};

//    private int requestPermissions() {
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
//                != PackageManager.PERMISSION_GRANTED {
//            ActivityCompat.requestPermissions(this, PERMISSIONS_APP, REQUEST_APP_PERMISSIONS);
//            return -1;
//        }
//
//        return 0;
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//
//        if (requestCode == REQUEST_APP_PERMISSIONS) {
//            Log.i(TAG, "Received response for application permissions request.");
//
//            // We have requested multiple permissions for contacts, so all of them need to be
//            // checked.
//            if (PermissionUtil.verifyPermissions(grantResults)) {
//                processMessages();
//            } else {
//                Log.i(TAG, "Application permissions were NOT granted.");
//                Snackbar.make(findViewById(R.id.MainLayout),
//                        "Application permissions not granted", Snackbar.LENGTH_LONG).show();
//            }
//        } else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }

    private void showErrSnackBar(String message) {
        Snackbar snack = Snackbar.make(findViewById(R.id.coordinatorLayout), message,
                Snackbar.LENGTH_INDEFINITE);
        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snack.show();
        }

    private boolean getNumbers()
    {

        int[] guessedNumbers = new int[NUMBER_OF_NUMS];
        int extraNumber, lottaryIdx;

        try {
            EditText editText = findViewById(R.id.guessedNum1);
            guessedNumbers[0] = Integer.parseInt(editText.getText().toString());

            editText = findViewById(R.id.guessedNum2);
            guessedNumbers[1] = Integer.parseInt(editText.getText().toString());

            editText = findViewById(R.id.guessedNum3);
            guessedNumbers[2] = Integer.parseInt(editText.getText().toString());

            editText = findViewById(R.id.guessedNum4);
            guessedNumbers[3] = Integer.parseInt(editText.getText().toString());

            editText = findViewById(R.id.guessedNum5);
            guessedNumbers[4] = Integer.parseInt(editText.getText().toString());

            editText = findViewById(R.id.guessedNum6);
            guessedNumbers[5] = Integer.parseInt(editText.getText().toString());

            editText = findViewById(R.id.additionalNum);
            extraNumber = Integer.parseInt(editText.getText().toString());

            editText = findViewById(R.id.lotteryNumber);
            lottaryIdx = Integer.parseInt(editText.getText().toString());
        }
        catch (NumberFormatException ex) {
            showErrSnackBar("Invalid Input");
            return false;
        }
//        int[] guessedNumbers = new int[] {1, 2, 3, 4, 5, 6};
//        int lottaryIdx = 2945;
//        int extraNumber = 4;

        if (validate_numbers(guessedNumbers, extraNumber)) {
              guessedData = new LottoData(guessedNumbers, extraNumber, lottaryIdx);
        }
        else {
            showErrSnackBar("Invalid Input");
            return false;
        }

        return true;
    }

    private boolean validate_numbers(int [] guessedNumbers, int extraNumber)
    {
        Arrays.sort(guessedNumbers);

        for (int i = 0; i < NUMBER_OF_NUMS; i++) {
            if (guessedNumbers[i] > MAX_NUMBER ||
                    guessedNumbers[i] < MIN_NUMBER ||
                    (i > 0 && guessedNumbers[i] == guessedNumbers[i-1])) {
                return false;
            }
        }
// Add log message
        if (extraNumber > MAX_EXTRA_NUMBER || extraNumber < MIN_NUMBER) {
            return false;
        }
        return true;
    }

    private void getResults() throws IOException
    {
        String urlStr = "http://www.pais.co.il/Lotto/Pages/last_Results.aspx?download=1";
        URL url = new URL(urlStr);
        String line;
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count = 0;

        while((count = bis.read(buffer, 0, 1024)) != -1)
        {
            baos.write(buffer, 0, count);
        }

        String csvfile = baos.toString();
        BufferedReader br = new BufferedReader(new StringReader(csvfile));

        while((line = br.readLine()) != null) {
            String[] numbers = line.split(",");
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = findViewById(R.id.checkResults);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getNumbers()) {
                    return;
                }

                GetLottoResults getLottoResults = new GetLottoResults(findViewById(R.id.coordinatorLayout));
                getLottoResults.execute(guessedData);

            }
        });

        final Button button2 = findViewById(R.id.clearGuess);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText)(findViewById(R.id.guessedNum1))).setText("");
                ((EditText)(findViewById(R.id.guessedNum2))).setText("");
                ((EditText)(findViewById(R.id.guessedNum3))).setText("");
                ((EditText)(findViewById(R.id.guessedNum4))).setText("");
                ((EditText)(findViewById(R.id.guessedNum5))).setText("");
                ((EditText)(findViewById(R.id.guessedNum6))).setText("");
                ((EditText)(findViewById(R.id.additionalNum))).setText("");
                ((EditText)(findViewById(R.id.lotteryNumber))).setText("");
            }
        });
    }
}
