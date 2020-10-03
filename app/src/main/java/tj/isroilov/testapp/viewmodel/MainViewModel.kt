package tj.isroilov.testapp.viewmodel

import androidx.lifecycle.ViewModel
import tj.isroilov.testapp.model.repository.EntityRepository

class MainViewModel(
    private val repository: EntityRepository
) : ViewModel() {
    fun getAllData(id: Int, type: Int) = repository.getData(id, type)
}