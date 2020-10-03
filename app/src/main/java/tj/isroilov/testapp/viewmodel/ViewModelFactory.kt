package tj.isroilov.testapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tj.isroilov.testapp.model.repository.EntityRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val entityRepository: EntityRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(entityRepository) as T
    }
}