package com.example.roie.lotto;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;
import android.support.design.widget.Snackbar;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetLottoResults extends AsyncTask<LottoData, Void, String> {
    private View view;
    private static final String urlStr = "http://www.pais.co.il/Lotto/Pages/last_Results.aspx?download=1";

    GetLottoResults(View view) {
        this.view = view;
    }

    @Override
    protected String doInBackground(LottoData... data) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            LottoData guessData = data[0];

            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

            String inputString;
            bufferedReader.readLine();  // Ignore the first line
            while ((inputString = bufferedReader.readLine()) != null) {
                inputString = inputString.replace("\"", "");
                String[] currentData = inputString.split(",");
                if (guessData.isReleventLotto(currentData[0])) {
                    return guessData.getStatusMsg(currentData);
                }
            }

            urlConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "No relevant lotto was found :(";
    }

    @Override
    protected void onPostExecute(String message) {
        Snackbar.make(this.view, message, Snackbar.LENGTH_INDEFINITE).show();
    }
}