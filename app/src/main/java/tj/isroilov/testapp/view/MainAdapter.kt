package tj.isroilov.testapp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.glide.slider.library.SliderLayout
import com.glide.slider.library.slidertypes.BaseSliderView
import com.glide.slider.library.slidertypes.TextSliderView
import com.glide.slider.library.tricks.ViewPagerEx
import kotlinx.android.synthetic.main.row_main.view.*
import tj.isroilov.testapp.R
import tj.isroilov.testapp.model.entity.BaseEntity


class MainAdapter(
    private val context: Context
) : RecyclerView.Adapter<MainAdapter.AdapterViewHolder>(), BaseSliderView.OnSliderClickListener,
    ViewPagerEx.OnPageChangeListener {

    var data = ArrayList<BaseEntity>()

    fun loadData(loadingData: List<BaseEntity>) {
        data = ArrayList(loadingData)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AdapterViewHolder {
        return AdapterViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_main, p0, false)
        )
    }

    override fun getItemCount(): Int {
        Log.d("Adapter Size ", data.size.toString())
        return data.size
    }

    override fun onBindViewHolder(p0: AdapterViewHolder, p1: Int) {
        p0.bindItems(data[p1])
    }

    inner class AdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(article: BaseEntity) {
            val entityType = itemView.row_type
            val price = itemView.row_price
            val address = itemView.row_address
            val distance = itemView.row_distance
            val station = itemView.row_station
            val slider = itemView.row_image_slider

            val share = itemView.row_share

            share.setOnClickListener {
                val shareString = "Тип - ${article.type} \n " +
                        "Цена - ${article.price}  \n" +
                        "Адрес - ${article.address} \n" +
                        "Станция - ${article.station}  \n" +
                        "До станции - ${article.stationDistance} \n" +
                        "Фото - ${article.images?.get(0)}"

                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareString)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                context.startActivity(shareIntent)
            }

            entityType.text = article.type
            price.text = article.price
            address.text = article.address
            distance.text = article.stationDistance
            station.text = article.station

            loadImages(slider, article.images)

        }
    }


    private fun loadImages(sliderLayout: SliderLayout, images: List<String>?) {
        sliderLayout.removeAllSliders()
        if (images != null) {
            for (image in images) {
                val textSliderView = TextSliderView(context)
                textSliderView
                    .image(image)
                textSliderView.bundle(Bundle())
                textSliderView.bundle
                    .putString("extra", image)
                sliderLayout.addSlider(textSliderView)
            }
        }

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default)
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
        sliderLayout.addOnPageChangeListener(this)
        sliderLayout.stopAutoCycle()

    }

    override fun onSliderClick(slider: BaseSliderView?) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

}