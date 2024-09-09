package com.elfennani.aniwatch.ui.screens.downloads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfennani.aniwatch.data.repository.DownloadRepository
import com.elfennani.aniwatch.models.Download
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadsViewModel @Inject constructor(
    private val downloadRepository: DownloadRepository,
) : ViewModel() {

    private val _downloads = MutableStateFlow(emptyList<Download>())
    val downloads = _downloads.asStateFlow()

    init {
        getDownloads()
    }

    private fun getDownloads() {
        viewModelScope.launch(Dispatchers.IO) {
            val downloads = downloadRepository.getDownloads()

            downloads.collect { current ->
                _downloads.update { current }
            }
        }
    }

    fun forceStartDownload(){
        downloadRepository.startWorking()
    }

    fun clearDownloads(){
        viewModelScope.launch(Dispatchers.IO) {
            downloadRepository.clearDownloads()
        }
    }
}