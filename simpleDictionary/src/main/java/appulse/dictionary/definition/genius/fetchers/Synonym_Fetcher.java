package appulse.dictionary.definition.genius.fetchers;

import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import appulse.dictionary.definition.genius.objects.Synonym;
import appulse.dictionary.model.DictionaryModel;

public class Synonym_Fetcher extends AbstractBaseFetcher<ArrayList<Synonym>> {
	@MainThread
	public Synonym_Fetcher(DictionaryModel context) {
		super(context, FetcherConstant.synonym_url, FetcherConstant.apkKey);
	}

	@WorkerThread
	@Override
	protected ArrayList<Synonym> onFetcherResult(String queryWord, String line) {
		ArrayList<Synonym> synonyms = new ArrayList<Synonym>(0);
		try {
			JSONArray response = new JSONArray(line);
			for (int i = 0; i < response.length(); i++) {
				JSONObject synonym = response.getJSONObject(i);
				JSONArray words = synonym.getJSONArray("words");
				for (int word = 0; word < words.length(); ++word) {
					synonyms.add(new Synonym(words.getString(word)));
				}
			}
		} catch (JSONException e) {
		}

		return synonyms;
	}

	@MainThread
	@Override
	protected void onPostResult(DictionaryModel model, String queryingWord, ArrayList<Synonym> synonyms) {
		model.onSynonymReady(queryingWord, synonyms);
	}
}
