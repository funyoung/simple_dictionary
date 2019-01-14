package appulse.dictionary.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import appulse.dictionary.definition.genius.objects.Definition;
import appulse.dictionary.definition.genius.objects.Synonym;

public class WordModel {
    private final String word;

    private final List<Definition> definitionList = new ArrayList<>();
    private final List<Synonym> synonymList = new ArrayList<>();
    private String pronounciation;

    public WordModel(String word) {
        this.word = word;
    }

    public void updateDefinitionList(@NonNull List<Definition> definitionList) {
        this.definitionList.clear();
        this.definitionList.addAll(definitionList);
    }

    public void updateSynonymList(@NonNull List<Synonym> synonymList) {
        this.synonymList.clear();
        this.synonymList.addAll(synonymList);
    }

    public String getWord() {
        return word;
    }

    public List<Definition> getDefinitionList() {
        return definitionList;
    }

    public List<Synonym> getSynonymList() {
        return synonymList;
    }

    public String getPronounciation() {
        return pronounciation;
    }

    public void setPronounciation(String pronounciation) {
        this.pronounciation = pronounciation;
    }
}
