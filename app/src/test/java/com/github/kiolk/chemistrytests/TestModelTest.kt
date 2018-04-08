package com.github.kiolk.chemistrytests

import com.github.kiolk.chemistrytests.data.CloseQuestion
import com.github.kiolk.chemistrytests.data.Option
import com.github.kiolk.chemistrytests.data.Test.TestParams.DIRECT_ORDER
import com.github.kiolk.chemistrytests.data.Test.TestParams.RANDOM_ORDER
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TestModelTest {

    val question1 = CloseQuestion("Is the capital of Belarus?",
            null,
            Option("Hrodna"),
            Option("Minsk"),
            Option("Vitebsk"),
            Option("Homel"),
            Option("Zhodina"),
            2)

    val question2 = CloseQuestion("Is shortest month in year?",
            null,
            Option("Hrodna"),
            Option("Minsk"),
            Option("Vitebsk"),
            Option("Homel"),
            Option("Zhodina"),
            2)

    val question3 = CloseQuestion("Is biggest planet in solar system?",
            null,
            Option("Hrodna"),
            Option("Minsk"),
            Option("Vitebsk"),
            Option("Homel"),
            Option("Zhodina"),
            2)
    val test = com.github.kiolk.chemistrytests.data.Test(listOf(question1,
            question2, question3),RANDOM_ORDER)
    val test2 = com.github.kiolk.chemistrytests.data.Test(listOf(question1,
            question2, question3))

    @Test
    fun checkGetNextDirect(){
        var question = test2.getNext()
        Assert.assertEquals(question, question1)
        question = test2.getNext()
        Assert.assertEquals(question, question2)
        question = test2.getNext()
        Assert.assertEquals(question, question3)
    }

    @Test
    fun checkGetNextRandom(){
        val questions = listOf(question1, question2, question3)
       Assert.assertNotEquals(questions.toString(), test.questions.toString())
        Assert.assertTrue(test.questions.contains(question1))
        Assert.assertTrue(test.questions.contains(question2))
        Assert.assertTrue(test.questions.contains(question3))
    }

    @Test
    fun checkHasNext(){
        var cnt = 0
        while(test.hasNext() && cnt<= test.questions.size){
            ++cnt
            test.getNext()
        }
        Assert.assertEquals(cnt, test.questions.size)
    }

    @Test
    fun checkHasNext2(){
        Assert.assertTrue(test.hasNext())
        test.getNext()
        Assert.assertTrue(test.hasNext())
        test.getNext()
        Assert.assertTrue(test.hasNext())
        test.getNext()
        Assert.assertFalse(test.hasNext())
    }

    @Test
    fun checkAnswers(){
        while (test2.hasNext()){
            test2.getNext()
            test2.checkAnswer("Minsk")
        }
        Assert.assertEquals(test2.resultingScore, 1F)
    }

    @Test
    fun checkGetCorrectAnswer(){
        while (test.hasNext()){
            test.getNext()
            if(!test.checkAnswer("Minsk")){
                Assert.assertEquals(test.getCorrectAnswer(), "Jupiter")
            }
        }
    }
}