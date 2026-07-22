package i.am.shiro.amai.data.repository

import i.am.shiro.amai.data.local.AmaiDatabase
import i.am.shiro.amai.data.local.entity.ImageEntity
import i.am.shiro.amai.model.Page

class ReadRepository(
    private val database: AmaiDatabase
) {

    suspend fun getPages(bookId: Int) = database.imageDao.findByBookId(bookId)
        .map { it.toPage() }

    private fun ImageEntity.toPage() = Page(
        bookId = bookId,
        pageIndex = pageIndex,
        path = path,
        aspectRatio = width.toFloat() / height.toFloat(),
        thumbnailPath = thumbnailPath,
        thumbnailAspectRatio = thumbnailWidth.toFloat() / thumbnailHeight.toFloat()
    )
}
