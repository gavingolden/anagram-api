package anagram.api.resource.words

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
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
                 @RequestParam("limit") limit: Int?)
            : AnagramsResponse {
        return AnagramsResponse(wordsService.findAnagrams(word, limit))
    }

    data class ComparisonResponse(val match: Boolean)

    @PostMapping("/anagrams/comparison.json")
    @ResponseStatus(HttpStatus.OK)
    fun comparison(@RequestBody body: WordsRequest): ComparisonResponse {
        return ComparisonResponse(wordsService.compare(body.words))
    }
}