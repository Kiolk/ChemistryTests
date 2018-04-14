import com.github.kiolk.chemistrytests.data.models.*
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

    val question : CloseQuestion = CloseQuestion("X^2^^<br> drawable <br> H_2__SO_4__$cnt?", "http://teacher-chem.ru/wp-content/uploads/2014/12/olimp-11.jpg",
            Option("6", null),
            Option("2 drawable", "http://images.myshared.ru/5/327874/slide_7.jpg"),
            Option("10 drawable", "http://images.myshared.ru/5/327874/slide_7.jpg"),
            Option("11 drawable", "http://images.myshared.ru/5/327874/slide_7.jpg"),
            Option("12", null),
            5,
            SINGLE_CHOICE, null, 1.0F, "ru", mutableListOf( Option("6 drawable", "http://images.myshared.ru/5/327874/slide_7.jpg"),
            Option("2 drawable", "http://images.myshared.ru/5/327874/slide_7.jpg"),
            Option("10 drawable", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b0/Aptiganel.svg/1200px-Aptiganel.svg.png"),
            Option("11 drawable", "http://images.myshared.ru/5/327874/slide_7.jpg"),
            Option("12 drawable", "http://images.myshared.ru/5/327874/slide_7.jpg")))



        cnt ++
//        question.questionOptions = randomSort(question.questionOptions)
//        question.questionEn = "How many month in year?$cnt"
        listOfQuestions.add(question)
    }
    val params: TestParams = TestParams(RANDOM_ORDER, TRAINING_TEST, 5, true)
    return Test(listOfQuestions, params)
}