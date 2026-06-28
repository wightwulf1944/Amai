package i.am.shiro.amai.data.repository

import i.am.shiro.amai.data.local.AmaiDatabase
import i.am.shiro.amai.model.Page

class ReadRepository(
    private val database: AmaiDatabase
) {
    // TODO: add image prefetching somehow

    suspend fun getPages(bookId: Int): List<Page> {
        return database.imageDao.findByBookId(bookId).map {
            Page(
                bookId = it.bookId,
                pageIndex = it.pageIndex,
                path = it.path,
                aspectRatio = it.width.toFloat() / it.height.toFloat(),
                thumbnailPath = it.thumbnailPath,
                thumbnailAspectRatio = it.thumbnailWidth.toFloat() / it.thumbnailHeight.toFloat()
            )
        }
    }
}
