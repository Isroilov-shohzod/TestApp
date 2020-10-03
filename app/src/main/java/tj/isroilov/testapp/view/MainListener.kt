package tj.isroilov.testapp.view

interface MainListener {
    fun startRequest()
    fun successRequest()
    fun errorRequest(message: String?)
}