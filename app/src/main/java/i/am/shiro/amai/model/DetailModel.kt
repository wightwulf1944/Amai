package i.am.shiro.amai.model

class DetailModel(
    val title: String,
    val pageCount: Int,
    val tags: Map<String, List<String>>,
    val thumbnails: List<Thumbnail>
)
