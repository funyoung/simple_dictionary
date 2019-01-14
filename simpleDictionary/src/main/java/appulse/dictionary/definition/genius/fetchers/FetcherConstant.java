package appulse.dictionary.definition.genius.fetchers;

public class FetcherConstant {
    final static String definitions_url = "/definitions?limit=12&includeRelated=true&sourceDictionaries=wiktionary,webster,century,wordnet,ahd&useCanonical=true&includeTags=false";
    final static String pronounciation_url = "/pronunciations?useCanonical=false&typeFormat=ahd&limit=1";
    final static String synonym_url = "/relatedWords?useCanonical=true&relationshipTypes=synonym&limitPerRelationshipType=8";

    final static String apkKey = "de46aea2a06a6bd33572d005afc01f025e0a2875bc6a089e8";
    final static String pronounciation_key = "a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5";
}
