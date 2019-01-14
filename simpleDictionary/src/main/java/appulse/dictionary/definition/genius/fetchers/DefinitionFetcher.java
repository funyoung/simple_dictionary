package appulse.dictionary.definition.genius.fetchers;

import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import appulse.dictionary.definition.genius.objects.Definition;
import appulse.dictionary.model.DictionaryModel;

public class DefinitionFetcher extends AbstractBaseFetcher<ArrayList<Definition>> {
	@MainThread
	public DefinitionFetcher(DictionaryModel context) {
		super(context, FetcherConstant.definitions_url, FetcherConstant.apkKey);
	}

	@WorkerThread
	@Override
	protected ArrayList<Definition> onFetcherResult(String word, String line) {
		ArrayList<Definition> definitions = new ArrayList<>();

		try {
			JSONArray response = new JSONArray(line);
			for (int i = 0; i < response.length(); i++) {
				JSONObject definition = response.getJSONObject(i);
				definitions.add(new Definition(definition.getString("word"),
						definition.getString("partOfSpeech"),
						definition.getString("text")));
			}
		} catch (JSONException e) {
		}

		return definitions;
	}

	@MainThread
	@Override
	protected void onPostResult(DictionaryModel model, String queryingWord, ArrayList<Definition> definitions) {
		model.onDefinitionReady(queryingWord, definitions);
	}
}
