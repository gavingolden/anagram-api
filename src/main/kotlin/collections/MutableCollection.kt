package collections

import iterable.next
import iterable.remove

/**
 * Remove all but the first [count] elements
 */
fun <T> MutableCollection<T>.keepFirst(count: Int) {
    if (size > count) {
        iterator().next(count)
                .remove(size - count)
    }
}
