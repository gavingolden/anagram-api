package anagram.api.resource.words

import anagram.api.extensions.lexicalSort
import anagram.api.resource.language.Language
import org.springframework.stereotype.Service

@Service
class CorpusImpl(val language: Language) : Corpus {

    /**
     * Stores the current state of the words service. The lexically
     * sorted value of the word serves as it's key in the map for
     * efficient anagram lookup
     */
    val wordStore = hashMapOf<String, MutableCollection<String>>()

    override fun addWords(words: Collection<String>) {

        words.filterNot(::valid)
                .let {
                    if (it.isEmpty()) {
                        words.forEach(this::addWordToStore)
                    } else {
                        throw IllegalArgumentException("""The words [${it.joinToString()}] are invalid""")
                    }
                }
    }

    override fun deleteWord(word: String) {
        findBucket(word).remove(word)

    }

    override fun findAnagrams(word: String): Collection<String> {
        val anagrams = findOrCreateBucket(word)
        return when {
            anagrams.contains(word) -> anagrams.minus(word)
            else -> emptySet()
        }

    }

    /**
     * Determine if the word is valid
     * @return true if valid, else false
     */
    private fun valid(word: String): Boolean {
        return word.isNotBlank() && language.words().contains(word)
    }

    /**
     * Add a word to the data store
     * @param word to add
     */
    private fun addWordToStore(word: String) {
        findOrCreateBucket(word).add(word)
    }

    /**
     * Find the bucket of anagrams for the given word
     * if it exists, else create it
     * @return the existing or new bucket
     */
    private fun findOrCreateBucket(word: String): MutableCollection<String> {
        val key = buildKey(word)
        return wordStore.getOrPut(key) { mutableSetOf() }
    }

    /**
     * Find the bucket of anagrams for the given word.
     * This does not create the bucket if it does not
     * already exist
     */
    private fun findBucket(word: String): MutableCollection<String> {
        val key = buildKey(word)
        return wordStore.getOrElse(key) { mutableSetOf() }
    }

    /**
     * Build the key for the anagram bucket in which
     * the word is stored
     */
    private fun buildKey(word: String): String {
        return word.lexicalSort()
    }
}