import com.github.kiolk.chemistrytests.data.models.*
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.INPUT_CHOICE
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.MULTIPLE_CHOICE
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.SINGLE_CHOICE

val PHOTO_TAG = "drawable"

fun toHtml(pString : String) : String{
    var result = pString.replace("__", "</sub>")
    result = result.replace("_", "<sub>")
    result = result.replace("^^", "</sup>")
    result = result.replace("^", "<sup>")
    return result
}

fun getTrainingTets() : Test{
    val listOfQuestions : MutableList<CloseQuestion> = mutableListOf()
    var cnt : Int = 0
    while (cnt < 10){

    val question : CloseQuestion = CloseQuestion(cnt, "X^2^^<br> drawable <br> H_2__SO_4__$cnt?", "http://teacher-chem.ru/wp-content/uploads/2014/12/olimp-11.jpg",

            SINGLE_CHOICE, null, 1.0F, "ru", mutableListOf( Option("6 drawable", "http://images.myshared.ru/5/327874/slide_7.jpg"),
            Option("10 drawable", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b0/Aptiganel.svg/1200px-Aptiganel.svg.png"),
            Option("11 drawable", "http://images.myshared.ru/5/327874/slide_7.jpg"),
            Option("12 drawable", "http://images.myshared.ru/5/327874/slide_7.jpg")),
            listOf(4))
        val question2 : CloseQuestion = CloseQuestion(cnt, "X^2^^<br> drawable <br> H_2__SO_4__$cnt?", "http://teacher-chem.ru/wp-content/uploads/2014/12/olimp-11.jpg",

                MULTIPLE_CHOICE, null, 1.0F, "ru", mutableListOf( Option("6 drawable", "http://images.myshared.ru/5/327874/slide_7.jpg"),
                Option("10 drawable", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b0/Aptiganel.svg/1200px-Aptiganel.svg.png"),
                Option("7 drawable", "http://images.myshared.ru/5/327874/slide_7.jpg"),
                Option("11 drawable", "http://images.myshared.ru/5/327874/slide_7.jpg"),
                Option("12 drawable", "http://images.myshared.ru/5/327874/slide_7.jpg")),
                listOf(3, 4))
        val question3 : CloseQuestion = CloseQuestion(cnt, "What equal 2 + $cnt?", null,

                INPUT_CHOICE, null, 1.0F, "ru", mutableListOf( Option("${2+cnt}", null)),
                listOf(1))
        cnt ++
        listOfQuestions.add(question)
        listOfQuestions.add(question2)
        listOfQuestions.add(question3)
    }
    val params: TestParams = TestParams(RANDOM_ORDER, TRAINING_TEST, 5, true, DIRECT_TEST)
    return Test(listOfQuestions, params)
}