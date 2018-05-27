package com.github.kiolk.chemistrytests.data.models

import java.io.Serializable

class Course(var mCourseId: Int = 0,
             var mCourseTitle: String = "Title",
             var mCourseDescription: String = "Description",
             var mCourseIcon: String = "Http",
             var mListTestParams: MutableList<Int> = mutableListOf()) : Serializable {

    var mCoursTestParams : MutableList<TestParams> = mutableListOf()
    var mCompletedTests : MutableList<Int> = mutableListOf()

    fun filterTestParams(listParams : MutableList<TestParams>) : MutableList<TestParams>{
        val tmp : MutableList<TestParams> = mutableListOf()
        mListTestParams.forEach {
            val id = it
            val test : TestParams?= listParams.find { it.testId == id }
            if(test != null){
                tmp.add(test)
            }
        }
        return tmp
    }
}

fun testCourses() : MutableList<Course>{
    val list : MutableList<Course> = mutableListOf(Course(0, "Дорога к ЦТ", "Данный курс содержит подборку тестов " +
            "направленных на максимально полную и качественнуюподготовку к ЦТ", "http://www.profi-forex.by/system/news/CT_7.jpg", mutableListOf(0, 1)),
            Course(1, "8 Классю Химия", "Последовательность тестов охватываюўая все параграфы за 8 класс. Вопросы разного уровня сложности, позволят проверить уровень усвоения школьного материала",
                    "http://multidvd.org/uploads/posts/2011-10/1318260971_himija_8_klass.jpg", mutableListOf(1, 2)),
            Course(2, "Prepare fore Jagiellonian University Medical College", "Contain list of tests that help succesfully pass in this college",
                    "http://intranet.tdmu.edu.ua/data/kafedra/internal/sus_dusct/classes_stud/ru/stomat/ptn/%D0%B8%D1%81%D1%82%D0%BE%D1%80%D0%B8%D1%8F%20%D0%BC%D0%B5%D0%B4%D0%B8%D1%86%D0%B8%D0%BD%D1%8B/1/01.%20%d0%92%d1%81%d1%82%d1%83%d0%bf%d0%bb%d0%b5%d0%bd%d0%b8%d0%b5%20%d0%b2%20%d0%b8%d1%81%d1%82%d0%be%d1%80%d0%b8%d1%8e%20%d0%bc%d0%b5%d0%b4%d0%b8%d1%86%d0%b8%d0%bd%d1%8b.files/image061.jpg", mutableListOf(0,2)))
    return list
}