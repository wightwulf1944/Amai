package i.am.shiro.amai.repository

import i.am.shiro.amai.data.AmaiDatabase
import i.am.shiro.amai.data.entity.ImageEntity

class ReadRepository(
    private val database: AmaiDatabase
) {
    // TODO: add image prefetching somehow

    suspend fun getPages(bookId: Int): List<ImageEntity> {
        return database.imageDao.findByBookId(bookId)
    }
}
