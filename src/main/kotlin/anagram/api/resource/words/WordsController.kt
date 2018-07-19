package anagram.api.resource.words

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.Positive

@RestController
@Validated
class WordsController(val wordsService: Corpus) {

    data class WordsRequest(val words: Collection<String>)

    /**
     * Add words to the corpus
     */
    @PostMapping("/words.json")
    @ResponseStatus(HttpStatus.CREATED)
    fun addWord(@RequestBody body: WordsRequest) {
        wordsService.addWords(body.words)
    }

    /**
     * Remove words from the corpus
     */
    @DeleteMapping("/words.json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteWords() {
        wordsService.clearCorpus()
    }

    /**
     * Delete a word from the corpus
     */
    @DeleteMapping("/words/{word}.json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteWord(@PathVariable("word") word: String) {
        wordsService.deleteWord(word)
    }


    data class AnagramsResponse(val anagrams: Collection<String>)

    /**
     * Get the anagrams of the given word
     */
    @GetMapping("/anagrams/{word}.json")
    @ResponseStatus(HttpStatus.OK)
    fun anagrams(@PathVariable("word") word: String,
                 @Positive @RequestParam("limit") limit: Int?,
                 @RequestParam("exclude_proper_nouns") excludeProperNouns: Boolean?)
            : AnagramsResponse {
        return AnagramsResponse(wordsService.findAnagrams(word, limit, excludeProperNouns))
    }

    /**
     * Delete the word and its anagrams
     */
    @DeleteMapping("/anagrams/{word}.json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun anagrams(@PathVariable("word") word: String) {
        wordsService.deleteAnagrams(word)
    }

    data class ComparisonResponse(val match: Boolean)

    /**
     * Determine if the given words are anagrams of one another
     */
    @PostMapping("/anagrams/comparison.json")
    @ResponseStatus(HttpStatus.OK)
    fun comparison(@RequestBody body: WordsRequest): ComparisonResponse {
        return ComparisonResponse(wordsService.compare(body.words))
    }
}