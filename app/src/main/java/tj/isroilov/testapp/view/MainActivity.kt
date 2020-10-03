package tj.isroilov.testapp.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import tj.isroilov.testapp.R
import tj.isroilov.testapp.model.database.AppRoomDatabase
import tj.isroilov.testapp.model.network.AppNetworkAPI
import tj.isroilov.testapp.model.repository.EntityRepository
import tj.isroilov.testapp.utils.snackBar
import tj.isroilov.testapp.viewmodel.MainViewModel
import tj.isroilov.testapp.viewmodel.ViewModelFactory


class MainActivity : AppCompatActivity(), MainListener {


    private lateinit var appRoomDatabase: AppRoomDatabase
    private lateinit var networkAPI: AppNetworkAPI
    private lateinit var entityRepository: EntityRepository
    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var mainViewModel: MainViewModel


    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        appRoomDatabase = AppRoomDatabase.invoke(this)
        networkAPI = AppNetworkAPI()
        entityRepository = EntityRepository(networkAPI, appRoomDatabase, this)

        viewModelFactory = ViewModelFactory(entityRepository)
        mainViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        mainAdapter = MainAdapter(this)
        main_recycler.adapter = mainAdapter
        main_recycler.layoutManager = LinearLayoutManager(this)

        container.setOnRefreshListener {
            Handler(Looper.myLooper()!!).postDelayed({
                startLoadingData()
                container.isRefreshing = false
            }, 500)

        }
        startLoadingData()
    }


    private fun startLoadingData(){
        mainViewModel.getAllData(101, 0).observe(this, {
            Log.d("RESULT", "coming ${it.size}")
            // imageLoader.loadImage(it)
            mainAdapter.loadData(it)
        })

        for (i in 1..6) {
            mainViewModel.getAllData(101, i)
        }
    }

    override fun startRequest() {
        main_progress.visibility = View.VISIBLE
    }

    override fun successRequest() {
        main_progress.visibility = View.GONE
    }

    override fun errorRequest(message: String?) {
        main_progress.visibility = View.GONE
        container.snackBar(message)
    }
}
