package com.example.gptapi.aop;

import java.util.HashMap;
import java.util.Map;

public class Translit {

    final static int UPPER = 1;

    final static int LOWER = 2;

    final static Map<String, String> map = makeTranslitMap();

    private static Map<String, String> makeTranslitMap() {
        Map<String, String> map = new HashMap<>();
        map.put("a", "а");
        map.put("b", "б");
        map.put("v", "в");
        map.put("g", "г");
        map.put("d", "д");
        map.put("e", "е");
        map.put("yo", "ё");
        map.put("zh", "ж");
        map.put("z", "з");
        map.put("i", "и");
        map.put("j", "й");
        map.put("k", "к");
        map.put("l", "л");
        map.put("m", "м");
        map.put("n", "н");
        map.put("o", "о");
        map.put("p", "п");
        map.put("r", "р");
        map.put("s", "с");
        map.put("t", "т");
        map.put("u", "у");
        map.put("f", "ф");
        map.put("h", "х");
        map.put("ts", "ц");
        map.put("ch", "ч");
        map.put("sh", "ш");
        map.put("`", "ъ");
        map.put("y", "у");
        map.put("'", "ь");
        map.put("yu", "ю");
        map.put("ya", "я");
        map.put("x", "кс");
        map.put("w", "в");
        map.put("q", "к");
        map.put("iy", "ий");
        return map;
    }

    private static int charClass(char c) {
        return Character.isUpperCase(c) ? UPPER : LOWER;
    }

    private static String get(String s) {
        int charClass = charClass(s.charAt(0));
        String result = map.get(s.toLowerCase());
        return result == null ? "" : (charClass == UPPER ? (result.charAt(0) + "").toUpperCase() +
                (result.length() > 1 ? result.substring(1) : "") : result);
    }

    public static String translit(String text) {
        int len = text.length();
        if (len == 0) {
            return text;
        }
        if (len == 1) {
            return get(text);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; ) {
            // get next 2 symbols
            String toTranslate = text.substring(i, i <= len - 2 ? i + 2 : i + 1);
            // trying to translate
            String translated = get(toTranslate);
            // if these 2 symbols are not connected try to translate one by one
            if (translated.isEmpty()) {
                translated = get(toTranslate.charAt(0) + "");
                sb.append(translated.isEmpty() ? toTranslate.charAt(0) : translated);
                i++;
            } else {
                sb.append(translated.isEmpty() ? toTranslate : translated);
                i += 2;
            }
        }
        return sb.toString();
    }
}