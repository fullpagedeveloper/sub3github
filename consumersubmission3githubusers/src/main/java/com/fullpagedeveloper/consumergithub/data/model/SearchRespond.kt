package com.fullpagedeveloper.consumergithub.data.model

import android.os.Parcelable
import com.fullpagedeveloper.consumergithub.data.model.GithubUser
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchRespond(
    val total_count : String,
    val incomplete_results: Boolean? = null,
    val items : List<GithubUser>
): Parcelable