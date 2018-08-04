package com.example.roie.lotto;

import java.util.Locale;
import java.util.Objects;

public class LottoData {
    private final static int MAX_NUM = 40;
    private int[] numbers;
    private int extra_number;
    private int lotto_idx;

    LottoData(int [] guessedNumbers, int extra_number, int lotto_idx)
    {
        this.numbers = new int[MAX_NUM];
        for (int number : guessedNumbers) {
            this.numbers[number]++;
        }

        this.extra_number = extra_number;
        this.lotto_idx = lotto_idx;
    }

    public boolean isReleventLotto(String lottoIdxStr)
    {
        return Objects.equals(Integer.toString(this.lotto_idx), lottoIdxStr);
    }

    public String getStatusMsg(String[] results)
    {
        String data = results[1];

        int res = 0;

        if (this.extra_number == Integer.parseInt(results[8])) {
            res += 10;
        }

        for(int i = 2; i <= 7 ; i++) {
            if (this.numbers[Integer.parseInt(results[i])] > 0)
                res ++;
        }

        return String.format(Locale.US, "On Lottary: %d - %s\n Guessed: %d number%s %s",
                this.lotto_idx, data, res % 10, res % 10 == 1 ? "" : "s",
                res > 9 ? "and extra number" : "");
    }
}