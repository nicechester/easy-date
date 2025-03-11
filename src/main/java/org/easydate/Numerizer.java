package org.easydate;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class Numerizer {
    private static final LinkedHashMap<String, Integer> NUMERIC_WORDS;

    static {
        NUMERIC_WORDS = new LinkedHashMap<>();
        Properties prop = new Properties();
        try (InputStream input = Numerizer.class.getClassLoader().getResourceAsStream("text-to-number.properties")) {
            prop.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String textMap = prop.getProperty("text_map");
        for (String kv : textMap.split(",")) {
            String[] keyValue = kv.split(":");
            NUMERIC_WORDS.put(keyValue[0].replace("_", " "), Integer.parseInt(keyValue[1]));
        }
    }

    public static Collection<String> suppoerted_words() {
        return NUMERIC_WORDS.keySet();
    }

    public static int parse(String numericWord) throws ParseException {
        if (!NUMERIC_WORDS.containsKey(numericWord)) {
            throw new ParseException(String.format("'%s' isn't a number word!", numericWord), 0);
        }
        return NUMERIC_WORDS.get(numericWord);
    }

    private static Optional<String> reverseMatch(String text) {
        List<String> keyList = new ArrayList<>(NUMERIC_WORDS.keySet());
        Collections.reverse(keyList);
        for (String key : keyList) {
            if (text.toLowerCase().startsWith(key)) {
                return Optional.of(String.format("%d,%s", NUMERIC_WORDS.get(key), key));
            }
        }
        return Optional.empty();
    }

    public static String numerize(String text) {
        int begin = 0;
        List<String> result = new ArrayList<>();
        int end = 0;
        while (begin < text.length()) {
            end = text.indexOf(" ", begin);
            if (end == -1) {
                end = text.length();
            }
            Optional<String> numCandid = reverseMatch(text.substring(begin));
            if (numCandid.isPresent()) {
                String[] candidPair = numCandid.get().split(",");
                String candid = candidPair[0];
                end = begin + candidPair[1].length();
                while (end < text.length() && text.charAt(end) != ' ') {
                    candid += text.charAt(end);
                    end++;
                }
                result.add(candid);
            } else {
                result.add(text.substring(begin, end));
            }
            begin = end + 1;
        }
        return String.join(" ", result);
    }
}
