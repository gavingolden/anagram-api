package string

/**
 * Lazily evaluated string. The default [SynchronizedLazyImpl]
 * implementation does not compute the lazy value when [toString]
 * is called.
 *
 * This can be helpful when computing expensive [String]s
 * in debug logging.
 */
class LazyString (initializer: () -> String) {
    val value by lazy(initializer)

    override fun toString(): String {
        return value // delegate will initialize on first access
    }
}

fun lazyStr(initializer: () -> String): LazyString {
    return LazyString(initializer)
}