package com.example.mygiphycompose.data.remote

import com.example.mygiphycompose.domain.Pagination as domainPagination
import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("count")
    val count: Int,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("total_count")
    val totalCount: Int
) {
    fun mapToDomain() = domainPagination(
        count = count ?: 0,
        offset = offset ?: 0,
        totalCount = totalCount ?: 0
    )
}