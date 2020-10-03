package tj.isroilov.testapp.model.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tj.isroilov.testapp.model.database.AppRoomDatabase
import tj.isroilov.testapp.model.entity.BaseEntity
import tj.isroilov.testapp.model.network.AppNetworkAPI
import tj.isroilov.testapp.view.MainListener

class EntityRepository(
    private val api: AppNetworkAPI,
    private val dataBase: AppRoomDatabase,
    private val mainListener: MainListener

) {

    /*
    * data names to parse
    */
    private val dataNames =
        arrayOf(
            "object",
            "id",
            "price",
            "address",
            "station",
            "stationhowget",
            "image",
            "path-medium"
        )

    /*
    * request types
    */
    private val requestTypes = arrayOf(
        "Квартира",
        "Комната",
        "Дом",
        "Коммерческая",
        "Новостройка",
        "Аренда",
        "Зарубежная",
    )
    private val entity = MutableLiveData<List<BaseEntity>>()

    init {
        entity.observeForever { saveFlatsToDatabase(it) }
    }

    private fun updateData(id: Int, type: Int) {
        mainListener.startRequest()
        val call: Call<JsonObject> = when (type) {
            0 -> api.getFlats(id)
            1 -> api.getRooms(id)
            2 -> api.getCountry(id)
            3 -> api.getCommercial(id)
            4 -> api.getNew(id)
            5 -> api.getRent(id)
            6 -> api.getForeign(id)
            else -> {
                api.getFlats(id)
            }
        }

        call.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(
                call: Call<JsonObject?>?,
                response: Response<JsonObject?>
            ) {
                /*
                * parsing part
                * warning! maybe hard-codes
                */
                val obj = response.body()
                val keys = obj?.keySet()
                val entitiesFromServer = ArrayList<BaseEntity>()
                if (keys != null) {
                    for (key in keys) {
                        if (obj.get(key).isJsonObject) {
                            val innerObject: JsonObject =
                                obj.getAsJsonObject(key).getAsJsonObject(dataNames[0])
                            val innerObjectKeys = innerObject.keySet()

                            /*
                            * vars for data class
                            */

                            var itemId = ""
                            val images = ArrayList<String>()
                            var price: String? = ""
                            var address: String? = ""
                            var station: String? = ""
                            var stationDistance: String? = ""

                            for (innerObjectKey in innerObjectKeys!!) {
                                if (innerObject.get(innerObjectKey).isJsonObject) {
                                    images.add(
                                        innerObject.get(innerObjectKey)
                                            .asJsonObject.get(dataNames[6])
                                            .asJsonObject.get(dataNames[7])
                                            .toString().replace("\"", "")
                                    )
                                } else {
                                    itemId =
                                        innerObject.getAsJsonPrimitive(dataNames[1])
                                            .toString()
                                    price =
                                        innerObject.getAsJsonPrimitive(dataNames[2])
                                            .toString()
                                    address =
                                        innerObject.getAsJsonPrimitive(dataNames[3])
                                            .toString()

                                    if (innerObjectKeys.contains(dataNames[4])) {
                                        station = innerObject.getAsJsonPrimitive(dataNames[4])
                                            .toString()
                                    }
                                    if (innerObjectKeys.contains(dataNames[5]))
                                        stationDistance =
                                            innerObject.getAsJsonPrimitive(dataNames[5])
                                                .toString()
                                }

                            }

                            val entity = BaseEntity(
                                itemId,
                                images,
                                requestTypes[type],
                                price,
                                address,
                                station,
                                stationDistance
                            )
                            entitiesFromServer.add(entity)
                        }
                    }
                    entity.postValue(entitiesFromServer)
                    mainListener.successRequest()
                }

            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                Log.d("TAG", "onFailure: ${t.message}")
                mainListener.errorRequest(t.message)
            }
        })


    }

    private fun saveFlatsToDatabase(entities: List<BaseEntity>) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("EntityRepository", "saveFlatsToDatabase: ${entities.size}")
            dataBase.getBaseDao().saveEntity(entities)
        }
    }

    fun getData(id: Int, type: Int): LiveData<List<BaseEntity>> {
        updateData(id, type)
        return dataBase.getBaseDao().readEntity()
    }



}