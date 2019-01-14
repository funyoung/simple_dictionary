package appulse.dictionary.definition.genius.fetchers;

import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import appulse.dictionary.model.DictionaryModel;

abstract class AbstractBaseFetcher<Result> extends AsyncTask<String, Void, Result> {
    private final DictionaryModel mContext;

    private final static String base_url = "http://api.wordnik.com:80/v4/word.json/";
    private final String definitions_url;
    private final String apiKey;

    private String queryingWord;

    @MainThread
    public AbstractBaseFetcher(DictionaryModel context, String url, String apiKey) {
        mContext = context;
        this.definitions_url = url;
        this.apiKey = apiKey;
    }

    @WorkerThread
    abstract protected Result onFetcherResult(String word, String line);

    @WorkerThread
    @Override
    protected final Result doInBackground(String... strings) {
        queryingWord = strings[0];

        try {
            URL url = new URL(base_url + queryingWord + definitions_url + "&api_key=" + apiKey);
            URLConnection connection = url.openConnection();
            String line = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
            return onFetcherResult(queryingWord, line);
        } catch (IOException e) {
        }

        return onFetcherResult(queryingWord, null);
    }

    @MainThread
    @Override
    protected final void onPostExecute(Result result) {
        onPostResult(mContext, queryingWord, result);
    }

    @MainThread
    protected abstract void onPostResult(DictionaryModel model, String queryingWord, Result result);
}
