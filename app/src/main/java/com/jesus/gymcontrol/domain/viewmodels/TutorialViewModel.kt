package com.jesus.gymcontrol.domain.viewmodels

import androidx.lifecycle.ViewModel
import com.jesus.gymcontrol.data.sharedPreferences.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject



@HiltViewModel
class TutorialViewModel @Inject constructor(
	private val preferencesManager: PreferencesManager
) : ViewModel() {
	
	fun markTutorialAsShown() {
		preferencesManager.setTutorialShown(true)
	}
	
	fun isTutorialAlreadyShown(): Boolean {
		return preferencesManager.isTutorialShown()
	}
}
