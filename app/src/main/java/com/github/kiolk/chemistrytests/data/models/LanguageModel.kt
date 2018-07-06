package com.github.kiolk.chemistrytests.data.models

import java.io.Serializable

class LanguageModel(var languagePrefix : String = "en",
                    var isUserSelection : Boolean = false) : Serializable
