package anagram.api.resource.words

import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.Positive

@RestController
@Validated
class WordsController(val wordsService: Corpus) {

    data class WordsRequest(val words: Collection<String>)

    @PostMapping("/words.json")
    @ResponseStatus(HttpStatus.CREATED)
    fun addWord(@RequestBody body: WordsRequest) {
        wordsService.addWords(body.words)
    }

    @DeleteMapping("/words.json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteWords() {
        wordsService.clearCorpus()
    }

    @DeleteMapping("/words/{word}.json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteWord(@PathVariable("word") word: String) {
        wordsService.deleteWord(word)
    }


    data class AnagramsResponse(val anagrams: Collection<String>)

    @GetMapping("/anagrams/{word}.json")
    @ResponseStatus(HttpStatus.OK)
    fun anagrams(@PathVariable("word") word: String,
                 @Positive @RequestParam("limit") limit: Int?,
                 @RequestParam("exclude_proper_nouns") excludeProperNouns: Boolean?)
            : AnagramsResponse {
        return AnagramsResponse(wordsService.findAnagrams(word, limit, excludeProperNouns))
    }

    @DeleteMapping("/anagrams/{word}.json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun anagrams(@PathVariable("word") word: String) {
        wordsService.deleteAnagrams(word)
    }

    data class ComparisonResponse(val match: Boolean)

    @PostMapping("/anagrams/comparison.json")
    @ResponseStatus(HttpStatus.OK)
    fun comparison(@RequestBody body: WordsRequest): ComparisonResponse {
        return ComparisonResponse(wordsService.compare(body.words))
    }
}