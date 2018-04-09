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
    val question : CloseQuestion = CloseQuestion("How many month in year?", null,
            Option("6", null),
            Option("2", null),
            Option("10", null),
            Option("11", null),
            Option("12", null),
            5,
            SINGLE_CHOICE)
    val listOfQuestions : MutableList<CloseQuestion> = mutableListOf()
    var cnt : Int = 0
    while (cnt < 10){
        cnt ++
        listOfQuestions.add(question)
    }
    val params: TestParams = TestParams(RANDOM_ORDER, TRAINING_TEST, 5, true)
    return Test(listOfQuestions, params)
}