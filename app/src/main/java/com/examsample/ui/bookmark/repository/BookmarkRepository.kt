package com.examsample.ui.bookmark.repository

import android.content.Context
import com.examsample.ui.bookmark.BookmarkSortType
import com.examsample.ui.bookmark.local.BookmarkDatabase
import com.examsample.ui.bookmark.model.BookmarkModel
import com.examsample.ui.home.model.ProductModel
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class BookmarkRepository {

    fun selectAll(
        context: Context,
        sort: BookmarkSortType,
        onSuccess: (list: List<BookmarkModel>) -> Unit,
        onFail: (error: String) -> Unit
    ): Disposable = when (sort) {
        BookmarkSortType.RegDateDesc -> {
            BookmarkDatabase.getInstance(context).bookmarkDao().selectAllRegDateDesc()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    onSuccess(it)
                }) {
                    onFail(it.toString())
                }
        }
        BookmarkSortType.RegDateAsc -> {
            BookmarkDatabase.getInstance(context).bookmarkDao().selectAllRegDateAsc()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    onSuccess(it)
                }) {
                    onFail(it.toString())
                }
        }
        BookmarkSortType.ReviewRatingDesc -> {
            BookmarkDatabase.getInstance(context).bookmarkDao().selectAllReviewDesc()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    onSuccess(it)
                }) {
                    onFail(it.toString())
                }
        }
        BookmarkSortType.ReviewRatingAsc -> {
            BookmarkDatabase.getInstance(context).bookmarkDao().selectAllReviewAsc()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    onSuccess(it)
                }) {
                    onFail(it.toString())
                }
        }
    }

    fun selectExists(
        context: Context,
        productModel: ProductModel,
        result: (exists: Boolean) -> Unit
    ): Disposable =
        BookmarkDatabase.getInstance(context).bookmarkDao().selectProductExists(productModel.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ exists ->
                Logger.d("BookmarkRepository : selectDBProductExists exists >>> $exists")
                when (exists) {
                    1 -> result(true)
                    else -> result(false)
                }
            }, { error ->
                Logger.d("BookmarkRepository : selectDBProductExists error Log >>> $error")
            })

    fun insertBookMark(context: Context, model: ProductModel): Disposable =
        BookmarkDatabase.getInstance(context).bookmarkDao().insert(convertToBookMarkModel(model))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Logger.d("BookmarkRepository : insertBookMark isSuccess")
            }, {
                Logger.d("BookmarkRepository : insertBookMark exists fail >>> ${it.message}")
            })

    fun deleteBookMark(context: Context, model: ProductModel): Disposable =
        BookmarkDatabase.getInstance(context).bookmarkDao().delete(convertToBookMarkModel(model))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ isSuccess ->
                Logger.d("BookmarkRepository : deleteBookMark isSuccess >>> $isSuccess")
            }, { error ->
                Logger.d("BookmarkRepository : deleteBookMark isSuccess >>> $error")
            })

    fun deleteBookMark(context: Context, model: BookmarkModel): Disposable =
        BookmarkDatabase.getInstance(context).bookmarkDao().delete(model)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ isSuccess ->
                Logger.d("BookmarkRepository : deleteBookMark isSuccess >>> $isSuccess")
            }, { error ->
                Logger.d("BookmarkRepository : deleteBookMark isSuccess >>> $error")
            })

    private fun convertToBookMarkModel(model: ProductModel): BookmarkModel {
        return BookmarkModel(
            id = model.id,
            name = model.name,
            thumbnail = model.thumbnail,
            imagePath = model.descriptionModel.imagePath,
            subject = model.descriptionModel.subject,
            price = model.descriptionModel.price,
            rate = model.rate,
            regTimeStamp = System.currentTimeMillis()
        )
    }

}