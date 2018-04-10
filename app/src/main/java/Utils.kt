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

    val question : CloseQuestion = CloseQuestion("How many month in year$cnt?", null,
            Option("6", null),
            Option("2", null),
            Option("10", null),
            Option("11", null),
            Option("12", null),
            5,
            SINGLE_CHOICE, null, 1.0F, "ru", mutableListOf( Option("6", null),
            Option("2", null),
            Option("10", null),
            Option("11", null),
            Option("12", null)))



        cnt ++
//        question.questionOptions = randomSort(question.questionOptions)
//        question.questionEn = "How many month in year?$cnt"
        listOfQuestions.add(question)
    }
    val params: TestParams = TestParams(RANDOM_ORDER, TRAINING_TEST, 5, true)
    return Test(listOfQuestions, params)
}