package appulse.dictionary.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appulse.dictionary.definition.genius.objects.Definition;
import appulse.dictionary.definition.genius.objects.Synonym;

public class DictionaryModel {

    public interface Refresher {
        void refreshSynonyms(String word, List<Synonym> synonymList);
        void refreshDefinition(String word, List<Definition> definitionList);
        void refreshPronounciation(String word, String pronounciation);
    }

    private final Map<String, WordModel> wordModelMap = new HashMap<>();

    private Refresher refresher;

    private WordModel ensureWordMode(@NonNull String queryingWord) {
        WordModel wm = query(queryingWord);
        if (null == wm) {
            wm = new WordModel(queryingWord);
            wordModelMap.put(queryingWord, wm);
        }
        return wm;
    }
    public void onSynonymReady(@NonNull String queryingWord, ArrayList<Synonym> sResult) {
        //mContext.sAdapter.addItems(sResult);
        ensureWordMode(queryingWord).updateSynonymList(sResult);
        if (null != refresher) {
            refresher.refreshSynonyms(queryingWord, sResult);
        }
    }

    public void onDefinitionReady(@NonNull String queryingWord, ArrayList<Definition> result) {
//        mContext.mAdapter.addItems(result);
//        mContext.setSupportProgressBarIndeterminateVisibility(false);
        ensureWordMode(queryingWord).updateDefinitionList(result);
        if (null != refresher) {
            refresher.refreshDefinition(queryingWord, result);
        }
    }

    public void onPronounciationReady(@NonNull String queryingWord, String result) {
//        TextView txt = (TextView) mContext.findViewById(R.id.top_pronounciation);
//        txt.setText(result);
        ensureWordMode(queryingWord).setPronounciation(result);
        if (null != refresher) {
            refresher.refreshPronounciation(queryingWord, result);
        }
    }

    public WordModel query(String word) {
        return wordModelMap.get(word);
    }

    public void setRefresher(Refresher refresher) {
        this.refresher = refresher;
    }

    public Collection<WordModel> getDumpList() {
        return wordModelMap.values();
    }

    public void add(WordModel wordModel) {
        if (null != wordModel && null != wordModel.getWord()) {
            wordModelMap.put(wordModel.getWord(), wordModel);
        }
    }
}
