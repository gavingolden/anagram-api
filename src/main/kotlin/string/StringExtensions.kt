package string

/**
 * Lexically sort a string
 */
fun String.lexicalSort(): String {
    return toCharArray().sortedArray().joinToString(separator = "")
}