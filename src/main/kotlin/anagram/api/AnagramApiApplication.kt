package anagram.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AnagramApiApplication

fun main(args: Array<String>) {
    runApplication<AnagramApiApplication>(*args)
}
