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
        word.lexicalSort().let {
            wordStore.getOrPut(it) { mutableSetOf() }
                    .add(word)

        }
    }
}