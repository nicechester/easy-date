package org.easydate;

import org.junit.jupiter.api.Test;

import java.text.ParseException;

class NumerizerTest {

    @Test
    void parse() throws ParseException {
        String[] numWords = {"One", "Two", "Three", "Four", "Five", "Six", "Seven",
                "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen",
                "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen", "Twenty",
                "First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh", "Eighth",
                "Ninth", "Tenth", "Eleventh", "Twelfth", "Thirteenth", "Fourteenth",
                "Fifteenth", "Sixteenth", "Seventeenth", "Eighteenth", "Nineteenth",
                "Twentieth", "Twenty First", "Twenty Second", "Twenty Third", "Twenty Fourth",
                "Twenty Fifth", "Twenty Sixth", "Twenty Seventh", "Twenty Eighth", "Twenty Ninth",
                "Thirtieth", "Thirty First"};
        for (int i = 0; i < numWords.length; ++i) {
            System.out.println(String.format("%s -> %d", numWords[i], Numerizer.parse(numWords[i].toLowerCase())));
        }

    }

    @Test
    void numerize_keys() {
        System.out.println(Numerizer.suppoerted_words());
    }

    @Test
    void numerize() {
        String source = "Do you have tickets for March tenth, June twenty first, 2025-12-25 for two adults and 1 kid?";
        String numerized = Numerizer.numerize(source);
        System.out.println(source);
        System.out.println(numerized);
    }
}