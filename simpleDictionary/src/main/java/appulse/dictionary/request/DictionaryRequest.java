package appulse.dictionary.request;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.ref.WeakReference;
import java.util.List;

import appulse.dictionary.definition.genius.fetchers.DefinitionFetcher;
import appulse.dictionary.definition.genius.fetchers.Synonym_Fetcher;
import appulse.dictionary.definition.genius.fetchers.pronounciation_fetcher;
import appulse.dictionary.definition.genius.objects.Definition;
import appulse.dictionary.definition.genius.objects.Synonym;
import appulse.dictionary.model.DictionaryModel;
import appulse.dictionary.model.WordModel;
import appulse.simple.dictionary.DefinitionList;
import appulse.simple.dictionary.R;

public class DictionaryRequest implements DictionaryModel.Refresher {
    private static final String TAG = DictionaryRequest.class.getSimpleName();

    private static DictionaryRequest _instance;
    private final DictionaryModel dictionaryModel = new DictionaryModel();

    public static DictionaryRequest getInstance() {
        if (null == _instance) {
            _instance = new DictionaryRequest();
        }
        return _instance;
    }

    private DictionaryRequest() {
        dictionaryModel.setRefresher(this);
    }

    private WeakReference<DefinitionList> currentRef;
    private String currentWord;
    public void request(DefinitionList definitionList, String the_word) {
        if (TextUtils.equals(currentWord, the_word)) {
            // ongoing query
            return;
        }

        currentWord = the_word;
        currentRef = new WeakReference<>(definitionList);

        WordModel wordModel = dictionaryModel.query(currentWord);
        if (null == wordModel) {
            getPronounciation();
            getDefinition();
            getSynonyms();
        } else {
            checkToRefreshPronounciation(wordModel.getPronounciation());
            checkToRefreshDefinition(wordModel.getDefinitionList());
            checkToRefreshSynonyms(wordModel.getSynonymList());
        }

        readModel();
    }

    private void checkToRefreshSynonyms(@NonNull List<Synonym> synonymList) {
        if (synonymList.isEmpty()) {
            getSynonyms();
        } else {
            refreshSynonyms(currentWord, synonymList);
        }
    }

    @Override
    public void refreshSynonyms(String word, List<Synonym> synonymList) {
        if (!TextUtils.equals(currentWord, word)) return;
        DefinitionList mContext = currentRef.get();
        if (null == mContext) return;
        mContext.sAdapter.addItems(synonymList);
    }

    private void checkToRefreshDefinition(@NonNull List<Definition> definitionList) {
        if (definitionList.isEmpty()) {
            getDefinition();
        } else {
            refreshDefinition(currentWord, definitionList);
        }
    }

    @Override
    public void refreshDefinition(String word, List<Definition> definitionList) {
        if (!TextUtils.equals(currentWord, word)) return;
        DefinitionList mContext = currentRef.get();
        if (null == mContext) return;
        mContext.mAdapter.addItems(definitionList);
        mContext.setSupportProgressBarIndeterminateVisibility(false);
    }

    private void checkToRefreshPronounciation(String pronounciation) {
        if (TextUtils.isEmpty(pronounciation)) {
            getPronounciation();
        } else {
            refreshPronounciation(currentWord, pronounciation);
        }
    }

    @Override
    public void refreshPronounciation(String word, String pronounciation) {
        if (!TextUtils.equals(currentWord, word)) return;
        DefinitionList mContext = currentRef.get();
        if (null == mContext) return;
        TextView txt = (TextView) mContext.findViewById(R.id.top_pronounciation);
        txt.setText(pronounciation);
    }

    public void getDefinition() {
        new DefinitionFetcher(dictionaryModel).execute(currentWord);
    }

    public void getPronounciation() {
        new pronounciation_fetcher(dictionaryModel).execute(currentWord);
    }

    public void getSynonyms() {
        new Synonym_Fetcher(dictionaryModel).execute(currentWord);
    }

    private boolean hasRead = false;
    private final Gson gson = new Gson();
    private void readModel() {
        if (hasRead) return;
        File folder = getModelFolder();
        if (null != folder) {
            String[] fileList = folder.list();
            if (null != fileList) {
                for (String name : fileList) {
                    File file = new File(folder, name);
                    if (file.exists() && file.isFile()) {
                        Log.i(TAG, "readModel, file " + file.getAbsolutePath());
                        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
                            WordModel wordModel = gson.fromJson(reader, WordModel.class);
                            if (null != wordModel) {
                                dictionaryModel.add(wordModel);
                            }
                            hasRead = true;
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    // dumpModel dirty word model into files
    public void dumpModel() {
        File folder = getModelFolder();
        if (null != folder) {
            for (WordModel wordModel : dictionaryModel.getDumpList()) {
                File file = new File(folder, wordModel.getWord());
                Log.i(TAG, "dumpModel, writing file " + file.getAbsolutePath());
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    String text = gson.toJson(wordModel);
                    writer.write(text);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private File getModelFolder () {
        DefinitionList mContext = currentRef.get();
        if (null == mContext) return null;
        File path = mContext.getExternalCacheDir();
        if (!path.exists() || !path.isDirectory()) {
            return null;
        }

        File f = new File(path, "dict_model");
        if (f.exists() && f.isDirectory()) {
            return f;
        } else if (f.exists()) {
            f.delete();
        }

        f.mkdir();
        return f;
    }
}
