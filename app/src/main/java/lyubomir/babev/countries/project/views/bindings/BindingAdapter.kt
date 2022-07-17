package lyubomir.babev.countries.project.views.bindings

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import lyubomir.babev.countries.project.R
import lyubomir.babev.countries.project.extensions.loadSvg
import lyubomir.babev.countries.project.models.CountryModel
import lyubomir.babev.countries.project.viewModels.CountriesStatus
import lyubomir.babev.countries.project.views.adapters.CountriesAdapter

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<CountryModel>?) {
    val adapter = recyclerView.adapter as CountriesAdapter
    adapter.submitList(data)
    recyclerView.postDelayed( { recyclerView.scrollToPosition(0) } , recyclerView.itemAnimator?.moveDuration ?: 0)
}

@BindingAdapter("setRefreshing")
fun bindRefreshLayout(refreshLayout: SwipeRefreshLayout, isRefreshing: Boolean) {
    if (refreshLayout.isRefreshing && !isRefreshing)
        refreshLayout.isRefreshing = isRefreshing
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let { imgView.loadSvg(imgUrl) }
}

@BindingAdapter("status")
fun bindTextStatus(textView: TextView, status: CountriesStatus) {
    when (status) {
        CountriesStatus.LOADING,
        CountriesStatus.SUCCESS -> {
            textView.visibility = View.GONE
            textView.text       = ""
        }
        CountriesStatus.EMPTY   -> {
            textView.visibility = View.VISIBLE
            textView.text       = textView.context.getString(R.string.no_countries_found)
        }
        else                    -> {
            textView.visibility = View.VISIBLE
            textView.text       = textView.context.getString(R.string.a_problem_occured_please_check_your_internet_connection)
        }
    }
}