package lyubomir.babev.countries.project.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CountryModel(
    val capitalName     : String,
    val code            : String,
    val flag            : String,
    val latLng          : List<Double>,
    val name            : String,
    val population      : Int,
    val region          : String,
    val subregion       : String
) : Parcelable