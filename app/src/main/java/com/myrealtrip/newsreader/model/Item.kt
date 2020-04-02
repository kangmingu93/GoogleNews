package com.myrealtrip.newsreader.model

import java.io.Serializable

data class Item(
    var link: String?,
    var url: String?,
    var title: String,
    var content: String,
    var firstKeyword: String,
    var secondKeyword: String,
    var thirdKeyword: String
) : Serializable {
    constructor():this(null, null, "", "", "", "", "")
}