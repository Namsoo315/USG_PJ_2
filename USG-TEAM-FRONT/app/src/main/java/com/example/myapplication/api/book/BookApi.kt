package com.example.myapplication.api.book

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.BookDetailResponse
import com.example.myapplication.data.BookItem
import com.example.myapplication.view.auth.TokenManager
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import android.content.ContentResolver
import android.provider.MediaStore
import java.io.FileInputStream
import java.io.InputStream
import android.content.Context


class BookApi() {

    val apiService: BookInterface by lazy {
        RetrofitObj.retrofit.build().create(BookInterface::class.java)
    }
    fun getBookDetail(bookid: Int): LiveData<BookDetailResponse> {
        val data = MutableLiveData<BookDetailResponse>()
        apiService.getBookItem(bookid).enqueue(object : Callback<BookDetailResponse> {
            override fun onResponse(call: Call<BookDetailResponse>, response: Response<BookDetailResponse>) {
                data.value = response.body()
                Log.d("bookDetail", response.body().toString())
            }

            override fun onFailure(call: Call<BookDetailResponse>, t: Throwable) {
                Log.d("getBookDetail", "error"+t.message)
            }
        })
        return data
    }

    fun registerBook(context: Context, bookName: String, bookComment: String, author: String, bookPostName: String, bookPrice: Int, isbn: String, bookRealPrice: Int, publisher: String, imageUris: List<Uri> ) {
        val bookNamePart = RequestBody.create("text/plain".toMediaTypeOrNull(), bookName)
        val bookCommentPart = RequestBody.create("text/plain".toMediaTypeOrNull(), bookComment)
        val authorPart = RequestBody.create("text/plain".toMediaTypeOrNull(), author)
        val bookPostNamePart = RequestBody.create("text/plain".toMediaTypeOrNull(), bookPostName)
        val bookPricePart = RequestBody.create("text/plain".toMediaTypeOrNull(), bookPrice.toString())
        val isbnPart = RequestBody.create("text/plain".toMediaTypeOrNull(), isbn)
        val bookRealPricePart = RequestBody.create("text/plain".toMediaTypeOrNull(), bookRealPrice.toString())
        val publisherPart = RequestBody.create("text/plain".toMediaTypeOrNull(), publisher)

        val imageParts: MutableList<MultipartBody.Part> = mutableListOf()
        for((index, uri) in imageUris.withIndex()) {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val requestFile = createRequestBodyFromInputStream(inputStream, "image/*")
                val fieldName = "images[$index]"
                val imagePart = MultipartBody.Part.createFormData(fieldName, uri.lastPathSegment ?: "", requestFile)
                imageParts.add(imagePart)
            }
        }

        val token = TokenManager.getToken()
        if (token != null) {
            Log.d("registerBook", "token: $token")
            Log.d("registerBook", "bookNamePart: ${bookNamePart.toString()}")
            Log.d("registerBook", "bookCommentPart: ${bookCommentPart.toString()}")
            Log.d("registerBook", "authorPart: ${authorPart.toString()}")
            Log.d("registerBook", "bookPostNamePart: ${bookPostNamePart.toString()}")
            Log.d("registerBook", "bookPricePart: ${bookPricePart.toString()}")
            Log.d("registerBook", "isbnPart: ${isbnPart.toString()}")
            Log.d("registerBook", "bookRealPricePart: ${bookRealPricePart.toString()}")
            Log.d("registerBook", "publisherPart: ${publisherPart.toString()}")
            for((index, imagePart) in imageParts.withIndex()){
                Log.d("registerBook", "imageParts[$index]: ${imagePart.toString()}")
            }

            apiService.uploadBook(token, bookNamePart, bookCommentPart, authorPart, bookPostNamePart, bookPricePart, isbnPart, bookRealPricePart, publisherPart, imageParts)
                .enqueue(object: Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        Log.d("registerBook", response.body().toString())
                        response.errorBody()?.string()?.let { Log.d("registerBook", it) }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // 실패 처리
                        Log.d("registerBook", "실패")
                    }
                })
        }
    }

    fun modifyBook(bookId: Int, bookPostName: String, bookComment: String, bookPrice: Int, imageUris: List<Uri>) {
        val bookCommentPart = RequestBody.create("text/plain".toMediaTypeOrNull(), bookComment)
        val bookPostNamePart = RequestBody.create("text/plain".toMediaTypeOrNull(), bookPostName)
        val bookPricePart = RequestBody.create("text/plain".toMediaTypeOrNull(), bookPrice.toString())

        val imageParts: MutableList<MultipartBody.Part> = mutableListOf()
        for(uri in imageUris) {
            val file = File(uri.path) // Uri에서 File을 얻습니다.
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file.readBytes())
            val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
            imageParts.add(imagePart)
        }

        apiService.modifyBook(bookComment=bookCommentPart,
            bookPostName = bookPostNamePart, bookPrice = bookPricePart, images = imageParts, bookId = bookId
        ).enqueue(object: Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.d("modifyBook", "성공")
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // 실패 처리
                    Log.d("modifyBook", "실패")
                }
            })
    }

    // Uri에서 실제 파일 경로를 얻는 함수
    fun getPathFromUri(contentResolver: ContentResolver, uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        return cursor?.getString(columnIndex!!) ?: ""
    }

    // InputStream에서 RequestBody를 생성하는 함수
    fun createRequestBodyFromInputStream(inputStream: InputStream, mimeType: String): RequestBody {
        val fileBytes = inputStream.readBytes()
        return RequestBody.create(mimeType.toMediaTypeOrNull(), fileBytes)
    }

    fun deleteBook(bookId: Int) {
        val token = TokenManager.getToken()
        if (token != null) {
            apiService.deleteBook(token, bookId).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Log.d("deleteBook", "성공")
                    } else {
                        Log.d("deleteBook", "실패: ${response.errorBody()?.string()}")
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("deleteBook", "에러: ${t.message}")
                }
            })
        }
    }
}