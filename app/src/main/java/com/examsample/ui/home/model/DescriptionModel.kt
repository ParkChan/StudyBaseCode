package com.examsample.ui.home.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DescriptionModel(

    @SerializedName("imagePath")
    val imagePath: String?,

    @SerializedName("subject")
    val subject: String?,

    @SerializedName("price")
    val price: Int

) : Serializable
