package lyubomir.babev.countries.project.viewModels

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import lyubomir.babev.countries.project.communication.CountriesApi
import lyubomir.babev.countries.project.models.CountryModel

enum class CountriesStatus {SUCCESS, ERROR, EMPTY, LOADING}

class MainViewModel : ViewModel() {

    private val _statusLD = MutableLiveData<CountriesStatus>()
    val statusLD: LiveData<CountriesStatus> = _statusLD

    private val _countriesLD = MutableLiveData<List<CountryModel>>()
    val countriesLD: LiveData<List<CountryModel>> = _countriesLD

    private val _isDownloadingLD = MutableLiveData<Boolean>()
    val isDownloadingLD: LiveData<Boolean> = _isDownloadingLD

    private var countries = listOf<CountryModel>()

    init {
        getCountries()
    }

    fun getCountries() {
        viewModelScope.launch {
            _isDownloadingLD.value  = true
            _statusLD.value         = CountriesStatus.LOADING
                try {
                countries               = CountriesApi.retrofitService.getCountries().sortedByDescending { it.population }
                _countriesLD.value      = countries
                showErrorOnEmptyList()
            } catch (e: Exception) {
                _statusLD.value         = CountriesStatus.ERROR
                countries               = listOf()
                _countriesLD.value      = countries
            }
            _isDownloadingLD.value = false
        }
    }

    fun onSearch(search: String) {
        viewModelScope.launch {
            _countriesLD.value  = if (search.length < 3) countries else countries.filter { it.name.lowercase().contains(search.lowercase()) }
            showErrorOnEmptyList()
        }
    }

    fun onInternetAvailable() {
        if (countries.isEmpty() && _isDownloadingLD.value == false)
            getCountries()
    }

    private fun showErrorOnEmptyList() {
        _statusLD.value = if (_countriesLD.value?.isEmpty() == true) CountriesStatus.EMPTY else CountriesStatus.SUCCESS
    }

}