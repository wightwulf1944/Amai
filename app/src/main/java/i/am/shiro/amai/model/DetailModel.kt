package i.am.shiro.amai.model

import i.am.shiro.amai.data.entity.BookEntity
import i.am.shiro.amai.data.view.ThumbnailView

class DetailModel(
    val book: BookEntity,
    val artistTags: List<String>,
    val groupTags: List<String>,
    val parodyTags: List<String>,
    val characterTags: List<String>,
    val languageTags: List<String>,
    val categoryTags: List<String>,
    val generalTags: List<String>,
    val pageImages: List<ThumbnailView>
)