package lyubomir.babev.countries.project.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatEditText
import lyubomir.babev.countries.project.R
import lyubomir.babev.countries.project.databinding.ActivityMainBinding
import lyubomir.babev.countries.project.extensions.getAttrColor
import lyubomir.babev.countries.project.models.CountryModel
import lyubomir.babev.countries.project.utils.Constants.Companion.INTENT_COUNTRY_MODEL
import lyubomir.babev.countries.project.utils.NetworkHelper
import lyubomir.babev.countries.project.viewModels.MainViewModel
import lyubomir.babev.countries.project.views.adapters.CountriesAdapter
import lyubomir.babev.countries.project.views.custom.CustomEditText

class MainActivity : AppCompatActivity() {

    private lateinit var binding            : ActivityMainBinding
    private lateinit var networkHelper      : NetworkHelper

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    override fun onStart() {
        super.onStart()
        networkHelper.startListening()
    }

    override fun onStop() {
        super.onStop()
        networkHelper.stopListening()
    }

    private fun init() {
        binding.lifecycleOwner          = this
        binding.viewModel               = viewModel
        binding.countriesRv.adapter     = CountriesAdapter(::onCountryClicked)

        setSearchListeners()
        setNetworkHelper()
        setRefreshListener()
    }

    private fun setSearchListeners() {
        binding.searchEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onSearch(s.toString())
                binding.searchEdt.setCompoundDrawablesWithIntrinsicBounds(0,0, if (s?.isNotEmpty() == true) R.drawable.ic_clear_search else R.drawable.ic_search_icon, 0)
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.searchEdt.drawableListener = object : CustomEditText.DrawableListener {
            override fun onClick(editText: AppCompatEditText) {
                if (editText.text?.isNotEmpty() == true)
                    editText.setText("")
            }
        }
    }

    private fun setNetworkHelper() {
        networkHelper = NetworkHelper(this) {
            viewModel.onInternetAvailable()
        }
    }

    private fun onCountryClicked(countryModel: CountryModel) {
        startActivity(Intent(this, CountryDetailsActivity::class.java).putExtra(INTENT_COUNTRY_MODEL, countryModel))
    }

    private fun setRefreshListener() {
        binding.refreshLayout.setOnRefreshListener { viewModel.getCountries() }
        binding.refreshLayout.setColorSchemeColors(getAttrColor(android.R.attr.colorPrimary))
    }
}