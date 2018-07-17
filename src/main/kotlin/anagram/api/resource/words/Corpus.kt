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
}