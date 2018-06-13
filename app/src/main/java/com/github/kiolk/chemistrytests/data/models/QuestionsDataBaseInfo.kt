package com.github.kiolk.chemistrytests.data.models

import java.io.Serializable

class QuestionsDataBaseInfo(var version : Int = 1,
                            var avaIlableTests : Int = 3,
                            var availableQuestions : Int = 10,
                            var availableCourses : Int = 3,
                            var availableTheory : Int = 0): Serializable
