package anagram.api.resource.words

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class WordsController(val wordsService: Corpus) {

    data class AddWordsRequest(val words: Collection<String>)

    @PostMapping("/words.json")
    @ResponseStatus(HttpStatus.CREATED)
    fun add(@RequestBody body: AddWordsRequest) {
        wordsService.addWords(body.words)
    }


    data class AnagramsResponse(val anagrams: Collection<String>)

    @GetMapping("/anagrams/{word}.json")
    @ResponseStatus(HttpStatus.OK)
    fun anagrams(@PathVariable("word") word: String): AnagramsResponse {
        return AnagramsResponse(wordsService.findAnagrams(word))
    }
}