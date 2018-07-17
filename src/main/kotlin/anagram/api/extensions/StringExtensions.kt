package anagram.api.extensions

/**
 * Lexically sort a string
 */
fun String.lexicalSort(): String {
    return toCharArray().sortedArray().contentToString()
}