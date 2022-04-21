package com.example.chat_2022_eleves

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class Promo {
    var promo: String? = null

    @SerializedName("enseignants")
    var profs: ArrayList<Enseignant?>? = null
    override fun toString(): String {
        return "Promo{" +
                "promo='" + promo + '\'' +
                ", profs=" + profs +
                '}'
    }
}