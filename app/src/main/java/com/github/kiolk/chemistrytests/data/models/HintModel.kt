package com.github.kiolk.chemistrytests.data.models

import java.io.Serializable

fun Hint.length() : Int = this.text.length

class Hint(var text : String = "", var images : List<String>? = null) : Serializable{

}
