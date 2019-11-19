package i.am.shiro.amai.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import i.am.shiro.amai.R
import i.am.shiro.amai.model.Book
import i.am.shiro.amai.network.Nhentai
import i.am.shiro.amai.transformer.BookTransformer
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.subscribeBy
import io.realm.Realm
import io.realm.kotlin.where
import timber.log.Timber

class LoadingFragment : Fragment(R.layout.fragment_loading) {

    init {
        retainInstance = true
    }

    private var disposable = Disposables.disposed()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bookId = requireActivity().intent.data!!.pathSegments[1].toInt()

        disposable = Nhentai.API.getBook(bookId)
            .map(BookTransformer::transform)
            .subscribeBy(
                onSuccess = ::updateBook,
                onError = Timber::e
            )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    private fun updateBook(newBook: Book) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val localBook = it.where<Book>()
                .equalTo("id", newBook.id)
                .findFirst()

            if (localBook != null && localBook.isDownloaded) {
                newBook.setDownloaded(true)
                    .setLocalCoverImage(localBook.coverImage)
                    .setLocalCoverThumbnailImage(localBook.coverThumbnailImage)
                    .setLocalPageImages(localBook.pageImages)
                    .setLocalPageThumbnailImages(localBook.pageThumbnailImages)
            }

            it.insertOrUpdate(newBook)
        }
        realm.close()

        val fragment = DetailFragment(newBook.id)
        requireFragmentManager().commit {
            replace(android.R.id.content, fragment)
        }
    }

}