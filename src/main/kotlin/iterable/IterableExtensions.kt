package iterable

/**
 * Iterate [count] times
 */
fun <T, C : Iterator<T>> C.next(count: Int): C {
    (1..count).forEach { next() }
    return this
}

/**
 * Remove the next [count] elements
 */
fun <T> MutableIterator<T>.remove(count: Int): MutableIterator<T> {
    (1..count).forEach { remove() }
    return this
}
