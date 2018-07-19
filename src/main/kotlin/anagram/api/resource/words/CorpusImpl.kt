package anagram.api.resource.words

import anagram.api.extensions.lexicalSort
import anagram.api.resource.language.Language
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import string.lazyStr

@Service
class CorpusImpl(val language: Language) : Corpus {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(CorpusImpl::class.java)
    }

    /**
     * Stores the current state of the words service. The lexically
     * sorted value of the word serves as it's key in the map for
     * efficient anagram lookup
     */
    val wordStore = hashMapOf<String, MutableCollection<String>>()

    override fun addWords(words: Collection<String>) {
        words.filterNot(::valid).let {
            if (it.isEmpty()) {
                logger.info("""Adding ${words.size} words to the corpus""")
                words.forEach(this::addWordToStore)
            } else {
                throw IllegalArgumentException("""The words [${it.joinToString()}] are invalid""")
            }
        }
    }

    override fun deleteWord(word: String) {
        logger.debug("Deleting word [{}] from corpus", word)
        findBucket(word)?.remove(word)
    }

    override fun clearCorpus() {
        logger.info("Clearing all words from the corpus")
        wordStore.clear()
    }

    override fun findAnagrams(word: String, limit: Int?, excludeProperNouns: Boolean?): Collection<String> {
        if (limit != null && limit < 0) throw IllegalArgumentException("limit must be positive")

        return findBucket(word).orEmpty()
                .minus(word)
                .also { logger.info("""Found ${it.size} anagrams for [$word]""") }
                .let {
                    if (excludeProperNouns == true) {
                        logger.debug("Excluding proper nouns from results")
                        it.filter { it[0].isLowerCase() }
                    } else it
                }.let {
                    if (limit != null) {
                        logger.debug("""Taking only $limit of ${it.size} anagrams for [$word]""")
                        it.take(limit)
                    } else it
                }.also { logger.info("""Returning ${it.size} anagrams for [$word]""") }
    }

    override fun compare(words: Collection<String>): Boolean {
        return words.first()
                .let(::findBucket)
                ?.containsAll(words) ?: false
                .also { isMatch ->
                    logger.debug("The words [{}] are{} mutual anagrams",
                            lazyStr { words.joinToString() },
                            if (isMatch) "" else " not")
                }
    }

    override fun deleteAnagrams(word: String) {
        val key = buildKey(word)
        wordStore[key].let {
            if (it != null && it.contains(word)) {
                wordStore.remove(key)
                logger.info("""Removed [$word] and its ${it.size - 1} anagrams""")
            } else {
                logger.debug("Did not remove [{}] because it is not in the corpus", word)
            }
        }
    }

    /**
     * Determine if the word is valid
     * @return true if valid, else false
     */
    private fun valid(word: String): Boolean {
        val isValid = word.isNotBlank() && language.words().contains(word)
        if (!isValid) {
            logger.debug("[{}] is not a valid word", word)
        }
        return isValid
    }

    /**
     * Add a word to the data store
     * @param word to add
     */
    private fun addWordToStore(word: String) {
        logger.debug("Adding [{}] to corpus", word)
        findOrCreateBucket(word).add(word)
    }

    /**
     * Find the anagram bucket for the given word
     * if it exists, else create it
     * @return the existing or new bucket
     */
    private fun findOrCreateBucket(word: String): MutableCollection<String> {
        return buildKey(word)
                .let { wordStore.getOrPut(it, ::buildNewBucket) }
    }

    /**
     * Find the anagram bucket for the given word
     * @return the bucket in which the word resides if one exists, else null
     */
    private fun findBucket(word: String): MutableCollection<String>? {
        return buildKey(word)
                .let(wordStore::get)
                .takeIf { it != null && it.contains(word) }
                .also { bucket ->
                    if (bucket == null) logger.debug("Did not find bucket for [{}]", word)
                    else logger.debug("Found bucket of size {} for [{}]", bucket.size, word)
                }
    }

    /**
     * Build the key for the anagram bucket in which
     * the word is stored
     */
    private fun buildKey(word: String): String {
        return word.lexicalSort()
                .also { logger.debug("Built bucket key [{}] for [{}]", it, word) }
    }

    /**
     * Bucket implementation. This will affect the performance and
     * behavior of various methods due to ordering, insertion/removal
     * speed, etc
     */
    private fun buildNewBucket(): MutableCollection<String> {
        return sortedSetOf()
    }
}