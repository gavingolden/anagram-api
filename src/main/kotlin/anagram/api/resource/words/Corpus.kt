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
     * Get anagrams of the given word that are currently
     * in the corpus
     * @param word with which to search
     * @return anagrams, excluding the input word
     */
    fun findAnagrams(word: String): Collection<String>
}