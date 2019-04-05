package com.miage.altea.tp.battle_api.utils;

import org.springframework.context.i18n.LocaleContextHolder;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class BattleUtilities {

    private static float rounded(float value) {
        System.out.println(value);
        DecimalFormat df = new DecimalFormat("#", new DecimalFormatSymbols(LocaleContextHolder.getLocale()));
        return Float.valueOf(df.format(value));
    }

    public static float pokemonStat(float baseStat, float level) {
        return rounded(5 + (baseStat * (level/50)));
    }

    public static float pokemonLife(float hp, float level) {
        return rounded(10 + level + (hp * (level/50)));
    }

    public static float attack(float hp, float level, float baseStat, float opponentDefense) {
        float value = hp - rounded((((2*level) / 5) + (2 * (baseStat/opponentDefense))) + 2);
        return (value <= 0)? 0 : value;
    }

}
