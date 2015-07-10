package com.sevak_avet.scheduler;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.english.EnglishLuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Set;

/**
 * Created by savetisyan on 10.07.15.
 */
@Component
public class MorphologyHelper {
    @Autowired
    private RussianLuceneMorphology russianLuceneMorphology;

    @Autowired
    private EnglishLuceneMorphology englishLuceneMorphology;

    public boolean checkBadWord(String s, Set<String> badWords) throws IOException {
        Tokenizer tokenizer = new StandardTokenizer();
        tokenizer.setReader(new StringReader(s.toLowerCase()));
        CharTermAttribute termAttribute = tokenizer.getAttribute(CharTermAttribute.class);
        tokenizer.reset();

        LuceneMorphology morphology = null;
        if(isCyrillicWord(s)) {
            morphology = russianLuceneMorphology;
        } else if(isLatinWord(s)) {
            morphology = englishLuceneMorphology;
        }

        while (tokenizer.incrementToken()) {
            System.out.println(termAttribute);

            if(morphology != null) {
                List<String> normalForms = morphology.getNormalForms(termAttribute.toString());
                System.out.println(normalForms);

                for (String normalForm : normalForms) {
                    if (badWords.contains(normalForm)) {
                        return true;
                    }
                }
            } else {
                if(badWords.contains(termAttribute.toString())) {
                    return true;
                }
            }
        }

        tokenizer.end();
        tokenizer.close();

        return false;
    }

    public static boolean isCyrillicWord(String s) {
        return s.matches("[А-Яа-яЁё]+");
    }

    public static boolean isLatinWord(String s) {
        return s.matches("[A-Za-z]+");
    }

}
