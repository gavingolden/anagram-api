package anagram.api.resource.words

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class WordsController(val wordsService: Corpus) {

    data class AddWordsRequest(val words: List<String>)

    @PostMapping("/words.json",
            consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE],
            produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun add(@RequestBody body: AddWordsRequest) {
        wordsService.addWords(body.words)
    }
}