package com.fullpagedeveloper.submission3githubusers.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchRespond(
    val total_count : String,
    val incomplete_results: Boolean? = null,
    val items : List<GithubUser>
): Parcelable