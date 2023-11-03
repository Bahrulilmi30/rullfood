package com.catnip.rullfood.data.local.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import com.catnip.rullfood.utils.PreferenceDataStoreHelper
import kotlinx.coroutines.flow.Flow

interface UserPreferenceDataSource{
    fun getUserListViewModePrefFlow(): Flow<Boolean>
    suspend fun setUserListViewModePreference(isLinear: Boolean)
    suspend fun getUserListViewModePreference(): Boolean
}
class UserPreferenceDataSourceImpl(
    private val dataStorHelper : PreferenceDataStoreHelper
): UserPreferenceDataSource{
    override fun getUserListViewModePrefFlow(): Flow<Boolean> {
        return dataStorHelper.getPreference(PREF_USER_LIST_VIEW, false)
    }

    override suspend fun setUserListViewModePreference(isLinear: Boolean) {
        dataStorHelper.putPreference(PREF_USER_LIST_VIEW, isLinear)
    }

    override suspend fun getUserListViewModePreference(): Boolean {
        return dataStorHelper.getFirstPreference(PREF_USER_LIST_VIEW, false)
    }

    companion object{
        val PREF_USER_LIST_VIEW = booleanPreferencesKey("PREF_USER_LIST_VIEW")
    }

}