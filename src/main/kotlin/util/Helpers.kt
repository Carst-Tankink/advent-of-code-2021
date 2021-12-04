package util

class Helpers {
    companion object {
        fun <T> transpose(input: List<List<T>>): List<List<T>> {
            return if (input.any { it.isEmpty() }) emptyList() else
                listOf(input.map { it[0] }) + transpose(input.map { it.drop(1) })
        }
    }
}