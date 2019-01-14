package appulse.dictionary.definition.genius.fetchers;

import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import org.json.JSONArray;
import org.json.JSONException;

import appulse.dictionary.model.DictionaryModel;

public class pronounciation_fetcher extends AbstractBaseFetcher<String> {
	@MainThread
	public pronounciation_fetcher(DictionaryModel context) {
		super(context, FetcherConstant.pronounciation_url, FetcherConstant.pronounciation_key);
	}

	@WorkerThread
	@Override
	protected String onFetcherResult(String word, String line) {
		try {
            JSONArray response = new JSONArray(line);
            if (response.length() != 0) {
                return response.getJSONObject(0).getString("raw");
            }
		} catch (JSONException e) {
        } catch (Exception ex) {
		}

		return null;
	}

	@MainThread
	@Override
	protected void onPostResult(DictionaryModel model, String queryingWord, String result) {
		model.onPronounciationReady(queryingWord, result);
	}
}

