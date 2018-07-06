import android.content.Context
import android.net.ConnectivityManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.github.kiolk.chemistrytests.data.models.*
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.INPUT_CHOICE
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.MULTIPLE_CHOICE
import com.github.kiolk.chemistrytests.data.models.CloseQuestion.Question.SINGLE_CHOICE
import java.util.*

val PHOTO_TAG = "drawable"

fun toHtml(pString: String): String {
    var result = pString.replace("__", "</sub>")
    result = result.replace("_", "<sub>")
    result = result.replace("^^", "</sup>")
    result = result.replace("^", "<sup>")
    return result
}

fun isPresentDrawable(text : String) : Boolean{
   val result : Boolean =  text.findAnyOf(listOf(PHOTO_TAG), 0, false) != null
    return result
}

fun showFragment(manager: FragmentManager, container: Int, fragment: Fragment, tag : String? = null) {
    val transaction = manager.beginTransaction()
    if(tag != null){
        transaction.add(container, fragment, tag)
        transaction.addToBackStack(tag)
    }else{
        transaction.add(container, fragment)
    }
    transaction.commit()
    manager.executePendingTransactions()
}


fun closeFragment(manager: FragmentManager, fragment: Fragment, tag : String? = null) {
    val transaction = manager.beginTransaction()
    if(tag != null){
        val foundFragment = manager.findFragmentByTag(tag)
        transaction.remove(foundFragment)
    }else{
        transaction.remove(fragment)
    }
    transaction.commit()
}

fun checkConnection(pContext: Context): Boolean {
    val check = pContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return check.activeNetworkInfo != null && check.activeNetworkInfo.isConnectedOrConnecting
}

fun <T> reversSort(list: MutableList<T>): MutableList<T> {
    val tmpList: MutableList<T> = mutableListOf()
    list.forEach { tmpList.add(it) }
    var position: Int = list.size - 1
    list.forEach {
        tmpList[position] = it
//        tmpList.add(position, it)
        position -= 1
    }
    return tmpList
}

fun getTrainingTets(): Test {
    val listOfQuestions: MutableList<CloseQuestion> = mutableListOf()
    var cnt: Int = 0
    while (cnt < 10) {

        val question: CloseQuestion = CloseQuestion(cnt, "X^2^^<br> drawable <br> H_2__SO_4__$cnt?", "http://teacher-chem.ru/wp-content/uploads/2014/12/olimp-11.jpg",

                SINGLE_CHOICE, null, 1.0F, "ru", mutableListOf(Option("6 drawable", "http://images.myshared.ru/5/327874/slide_7.jpg"),
                Option("10 drawable", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b0/Aptiganel.svg/1200px-Aptiganel.svg.png"),
                Option("11 drawable", "http://images.myshared.ru/5/327874/slide_7.jpg"),
                Option("12 drawable", "http://images.myshared.ru/5/327874/slide_7.jpg")),
                listOf(4))
        val question2: CloseQuestion = CloseQuestion(cnt, "X^2^^<br> drawable <br> H_2__SO_4__$cnt?", "http://teacher-chem.ru/wp-content/uploads/2014/12/olimp-11.jpg",

                MULTIPLE_CHOICE, null, 1.0F, "ru", mutableListOf(Option("6 drawable", "http://images.myshared.ru/5/327874/slide_7.jpg"),
                Option("10 drawable", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b0/Aptiganel.svg/1200px-Aptiganel.svg.png"),
                Option("7 drawable", "http://images.myshared.ru/5/327874/slide_7.jpg"),
                Option("11 drawable", "http://images.myshared.ru/5/327874/slide_7.jpg"),
                Option("12 drawable", "http://images.myshared.ru/5/327874/slide_7.jpg")),
                listOf(3, 4))
        val question3: CloseQuestion = CloseQuestion(cnt, "What equal 2 + $cnt?", null,

                INPUT_CHOICE, null, 1.0F, "ru", mutableListOf(Option("${2 + cnt}", null)),
                listOf(1))
        cnt++
        listOfQuestions.add(question)
        listOfQuestions.add(question2)
        listOfQuestions.add(question3)
    }
    val params: TestParams = TestParams(3, RANDOM_ORDER, TRAINING_TEST, 5, true, DIRECT_TEST)
    return Test(listOfQuestions, params)
}

fun changeLocale(context: Context, lang: String?) {
    val locale = Locale(lang)
//        lang?.let { saveData(it) }
    Locale.setDefault(locale)
    val configuration = context.resources.configuration
    configuration.locale = locale
    context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
}