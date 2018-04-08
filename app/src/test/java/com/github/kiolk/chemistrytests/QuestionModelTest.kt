package com.github.kiolk.chemistrytests

import android.text.Html
import com.github.kiolk.chemistrytests.data.CloseQuestion
import com.github.kiolk.chemistrytests.data.Option
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import toHtml

@RunWith(RobolectricTestRunner::class)
class  QuestionModelTest {

    val question = CloseQuestion("Is the capital of Belarus",
            null,
            Option("Hrodna"),
            Option("Minsk"),
            Option("Vitebsk"),
            Option("Homel"),
            Option("Zhodina"),
            2)

    @Test
    fun subSupText(){
        val format : String = Html.fromHtml("H<sub>2</sub>SO<sub>4</sub>").toString()
        Assert.assertNotEquals(format, 1)
    }

    @Test
    fun checkGetAnswer(){
        Assert.assertEquals("Minsk", question.getAnswer())
    }

    @Test
    fun checkCorrectAnswer(){
        Assert.assertTrue(question.checkAnswer("Minsk"))
        Assert.assertFalse(question.checkAnswer("Hrodna"))
    }

    @Test
    fun checkToHtmlConverter(){
        val raw = "H_2__SO_4__"
        val result = toHtml(raw)
        Assert.assertEquals(result, "H<sub>2</sub>SO<sub>4</sub>")
        Assert.assertEquals(toHtml("X^2^^"), "X<sup>2</sup>")
    }

    @Test
    fun checkGetOptions(){
        Assert.assertEquals(question.getOptions().toString(), "[Hrodna, Minsk, Vitebsk, Homel]")
    }

    @Test
    fun checkGetRandomOptions(){
        Assert.assertNotEquals(question.getRandomOptions().toString(), "[Hrodna, Minsk, Vitebsk, Homel]")
        Assert.assertEquals(question.getRandomOptions().size, question.getOptions().size)
        Assert.assertTrue(question.getRandomOptions().contains(question.option1))
        Assert.assertTrue(question.getRandomOptions().contains(question.option2))
        Assert.assertTrue(question.getRandomOptions().contains(question.option3))
        Assert.assertTrue(question.getRandomOptions().contains(question.option4))
    }
}