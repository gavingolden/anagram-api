package anagram.api.resource.language.english

import anagram.api.resource.language.Language
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

@Component
class EnglishLanguage(@Value("classpath:english-dictionary.txt") wordsResource: Resource)
    : Language {

    private val words: Set<String> = Files.lines(Paths.get(wordsResource.uri))
            .collect(Collectors.toSet())

    override fun words(): Set<String> {
        return words
    }
}