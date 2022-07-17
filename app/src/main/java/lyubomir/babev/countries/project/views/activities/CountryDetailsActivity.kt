package lyubomir.babev.countries.project.views.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import lyubomir.babev.countries.project.R
import lyubomir.babev.countries.project.databinding.ActivityCountryDetailsBinding
import lyubomir.babev.countries.project.extensions.loadSvg
import lyubomir.babev.countries.project.models.CountryModel
import lyubomir.babev.countries.project.utils.Constants.Companion.INTENT_COUNTRY_MODEL
import lyubomir.babev.countries.project.utils.Constants.Companion.LATITUDE_POSITION
import lyubomir.babev.countries.project.utils.Constants.Companion.LONGITUDE_POSITION

class CountryDetailsActivity: AppCompatActivity() {

    companion object {
        private const val MAP_ZOOM_FLOAT = 7.0f
    }

    private lateinit var binding: ActivityCountryDetailsBinding

    private var googleMap: GoogleMap? = null

    @SuppressLint("MissingPermission")
    private val locationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted)
                googleMap?.isMyLocationEnabled = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.backBtn.setOnClickListener { finish() }
        setCountryData()
    }

    private fun setCountryData() {
        val countryModel = intent.getParcelableExtra<CountryModel>(INTENT_COUNTRY_MODEL)
        countryModel?.let {
            with (countryModel) {
                setGoogleMap(this)

                binding.nameTxt.text            = name
                binding.regionTxt.text          = region
                binding.populationTxt.text      = population.toString()
                binding.capitalTxt.text         = capitalName

                binding.flagImg.loadSvg(flag)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setGoogleMap(countryModel: CountryModel) {
        lifecycleScope.launchWhenCreated {
            val mapFragment     = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
            val capitalLatLng   = LatLng(countryModel.latLng[LATITUDE_POSITION], countryModel.latLng[LONGITUDE_POSITION])
            googleMap           = mapFragment?.awaitMap()

            googleMap?.addMarker {
                position(capitalLatLng)
                title(countryModel.capitalName)
            }

            googleMap?.uiSettings?.isRotateGesturesEnabled      = true
            googleMap?.uiSettings?.isZoomControlsEnabled        = true
            googleMap?.uiSettings?.isZoomGesturesEnabled        = true
            googleMap?.uiSettings?.isCompassEnabled             = true

            val cameraPosition = CameraPosition.Builder().target(capitalLatLng).zoom(MAP_ZOOM_FLOAT).build()
            googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            if (ContextCompat.checkSelfPermission(this@CountryDetailsActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                googleMap?.isMyLocationEnabled = true
            else
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }
}