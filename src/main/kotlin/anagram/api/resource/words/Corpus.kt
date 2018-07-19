package anagram.api.resource.words

/**
 * Mutable collection of words
 */
interface Corpus {

    /**
     * Add words to the corpus
     * @param words to add
     */
    fun addWords(words: Collection<String>)

    /**
     * Delete a word from the corpus
     * @param word to be deleted
     */
    fun deleteWord(word: String)

    /**
     * Delete all words from the corpus
     */
    fun clearCorpus()

    /**
     * Get anagrams of the given word that are currently
     * in the corpus
     * @param word with which to search
     * @param limit max number of results
     * @param excludeProperNouns flag to exclude proper nouns
     * @return anagrams, excluding the input word
     */
    fun findAnagrams(word: String, limit: Int?, excludeProperNouns: Boolean?): Collection<String>

    /**
     * Determine if words are anagrams of one another
     * @return true if all are anagrams, else false
     */
    fun compare(words: Collection<String>): Boolean
}