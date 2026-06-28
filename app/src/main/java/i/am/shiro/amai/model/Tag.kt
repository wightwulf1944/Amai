package i.am.shiro.amai.model

// TODO type is not used anywhere but here. Construct query elswehere and remove type
data class Tag(
    val type: String,
    val name: String
) {
    val query: String
        get() {
            val searchTag = if (name.any(Char::isWhitespace)) "\"$name\"" else name
            return "$type:$searchTag"
        }
}
