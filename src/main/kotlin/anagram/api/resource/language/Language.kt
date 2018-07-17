package anagram.api.resource.language

interface Language {
    /**
     * Get all the words in the language
     */
    fun words(): Collection<String>
}