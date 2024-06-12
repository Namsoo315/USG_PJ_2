package com.example.myapplication.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "root", strict = false)
data class Root @JvmOverloads constructor(
    @field:Element(name = "result")
    @param:Element(name = "result")
    val result: Result = Result()
)

@Root(name = "result", strict = false)
data class Result @JvmOverloads constructor(
    @field:ElementList(entry = "item", inline = true)
    @param:ElementList(entry = "item", inline = true)
    val bookItems: List<BookItemIsbn> = emptyList()
)

@Root(name = "item", strict = false)
@Parcelize
data class BookItemIsbn @JvmOverloads constructor(
    @field:Element(name = "title_info")
    @param:Element(name = "title_info")
    val bookName: String = "",

    @field:Element(name = "author_info")
    @param:Element(name = "author_info")
    val author: String = "",

    @field:Element(name = "pub_info")
    @param:Element(name = "pub_info")
    val publisher: String = ""
) : Parcelable


data class BookUsingIsbn(
    val bookName: String,
    val author: String,
    val publisher: String,
    val bookRealPrice: String
)

data class PrePriceResponse(
    val TOTAL_COUNT: String,
    val docs: List<Doc>,
    val PAGE_NO: String
)

data class Doc(
    val PUBLISHER: String,
    val AUTHOR: String,
    val PRE_PRICE: String,
    val TITLE: String
)


