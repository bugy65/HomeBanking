package com.mindhub.homebanking.Utils;

public class Utils {

    public static int RandomNumberGenerate(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);

    }
}