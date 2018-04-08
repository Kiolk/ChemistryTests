package com.github.kiolk.chemistrytests

import android.text.Html
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun subSupText(){
        val format : String = Html.fromHtml("H<sub>2</sub>SO<sub>4</sub>").toString()
        assertEquals(format, 1)
    }
}
