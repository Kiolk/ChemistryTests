package com.github.kiolk.chemistrytests.data.models

import java.io.Serializable

class User (var userId : String = "UserId",
            var userName : String = "User",
            var email : String? = null,
            var avatarUrl : String? = null,
            var completedTests : MutableList<ResultInformation> = mutableListOf(),
            var userCustomTests : MutableList<TestParams> = mutableListOf(),
            var accountType : Int? = null) : Serializable

