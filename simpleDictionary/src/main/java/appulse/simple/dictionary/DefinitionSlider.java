package appulse.simple.dictionary;

import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DefinitionSlider extends DefinitionList {
    private static final String TAG = DefinitionSlider.class.getSimpleName();

    private static final String FILE_NAME = "textbook_origin.txt";

    private final List<String> wordList = new ArrayList<>();
    private int current = -1;


    @Override
    protected void onStart() {
        super.onStart();

        doLoadWord();
    }

    @Override
    protected void onStop() {
        super.onStop();

        saveTheWord();
    }


    private String getWord(boolean next) {
        if (wordList.isEmpty()) {
            return null;
        }

        current += next ? 1 : -1;
        if (current < 0) {
            return null;
        } else if (current >= wordList.size()) {
            return null;
        } else {
            return wordList.get(current);
        }
    }

    // todo: load with async task.
    private void doLoadWord() {
        try(InputStream in = getResources().getAssets().open(FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(inputStreamReader)) {
            while (true) {
                String line = reader.readLine();
                if (null == line) {
                    break;
                }

                line = line.trim();
                if (!TextUtils.isEmpty(line)) {
                    for (String word : line.split(" ")) {
                        wordList.add(word);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        current = wordList.indexOf(the_word);
        if (current == -1) {
            current = wordList.indexOf(getTheWord());
        }

        if (!wordList.isEmpty()) {
            if (current == -1) {
                refreshWord(true);
            } else {
                setWord(wordList.get(current));
            }
        }
    }

    @Override
    protected void refreshWord(boolean next) {
        String word = getWord(next);
        setWord(word);
    }

    @Override
    protected boolean isHomeAsUp() {
        return false;
    }

    private static final String LAST_WORD = "LAST_WORD";
    private String getTheWord() {
        if (TextUtils.isEmpty(the_word)) {
            SharedPreferences preferences = getSharedPreferences(TAG, MODE_PRIVATE);
            return preferences.getString(LAST_WORD, null);
        }
        return null;
    }

    private void saveTheWord() {
        if (null == the_word) {
            return;
        }

        SharedPreferences preferences = getSharedPreferences(TAG, MODE_PRIVATE);
        preferences.edit().putString(LAST_WORD, the_word).apply();
    }

}
