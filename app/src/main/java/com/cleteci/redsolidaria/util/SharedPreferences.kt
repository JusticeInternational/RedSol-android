package com.cleteci.redsolidaria.util

import android.content.Context
import android.content.SharedPreferences
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.GetOrganizationByUserIdQuery
import com.cleteci.redsolidaria.fragment.OrganizationDetails
import com.cleteci.redsolidaria.models.Organization

class SharedPreferences(context: Context) {

    val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        SHARED_PREFERENCES_FILE_NAME,
        Context.MODE_PRIVATE
    )

    var isFirstTime: Boolean
        get() = sharedPreferences.getBoolean(SHARED_NAME, true)
        set(value) = sharedPreferences.edit().putBoolean(SHARED_NAME, value).apply()

    var loginLater: Boolean
        get() = sharedPreferences.getBoolean(LOGIN_LATER, false)
        set(value) = sharedPreferences.edit().putBoolean(LOGIN_LATER, value).apply()

    var isProviderService: Boolean
        get() = sharedPreferences.getBoolean(TYPE_USER, false)
        set(value) = sharedPreferences.edit().putBoolean(TYPE_USER, value).apply()

    var userSaved: String?
        get() = sharedPreferences.getString(USER_SAVED, null)
        set(value) = sharedPreferences.edit().putString(USER_SAVED, value).apply()

    var userInfoToDisplay: String?
        get() = sharedPreferences.getString(USER_INFO_TO_DISPLAY, null)
        set(value) = sharedPreferences.edit().putString(USER_INFO_TO_DISPLAY, value).apply()

    var currentOrganizationId: String?
        get() = sharedPreferences.getString(CURRENT_ORG, null)
        set(value) = sharedPreferences.edit().putString(CURRENT_ORG, value).apply()

    var currentOrganizationName: String?
        get() = sharedPreferences.getString(Organization.Attribute.NAME.name, null)
        set(value) = sharedPreferences.edit().putString(Organization.Attribute.NAME.name, value).apply()

    var token: String?
        get() = sharedPreferences.getString(TOKEN, null)
        set(value) = sharedPreferences.edit().putString(TOKEN, value).apply()

    fun logout() {
        loginLater = false
        userSaved = null
        userInfoToDisplay = null
    }

    companion object {
        private const val TEST_DATA_PREFERENCE = "test_data_preference"
        private const val SHARED_NAME = "shared_first_time"
        private const val LOGIN_LATER = "shared_login_later"
        private const val USER_SAVED = "shared_user_saved"
        private const val USER_INFO_TO_DISPLAY = "shared_user_info_to_display"
        private const val TYPE_USER = "shared_user_type"
        private const val TOKEN = "token"
        const val SHARED_PREFERENCES_FILE_NAME = "com.cleteci.redsolidaria.SHARED_PREFERENCES_FILE"
        const val CURRENT_ORG = "shared_current_org"

        private fun getSharedPreferencesStorage(): SharedPreferences {
            return BaseApp.getContext().getSharedPreferences(
                SHARED_PREFERENCES_FILE_NAME,
                Context.MODE_PRIVATE
            )
        }

        private fun getSharedPreferencesEditor(): SharedPreferences.Editor {
            return getSharedPreferencesStorage().edit()
        }

        private fun clearAllSharedPreferences() {
            val editor = getSharedPreferencesEditor()
            editor.clear()
            editor.commit()
        }

        fun removeOrganizationSharedPreferences() {
            val editor = getSharedPreferencesEditor()
            editor.remove(CURRENT_ORG)
            editor.remove(Organization.Attribute.NAME.name)
            editor.remove(Organization.Attribute.SCHEDULE.name)
            editor.remove(Organization.Attribute.LOCATION.name)
            editor.remove(Organization.Attribute.PAGE.name)
            editor.remove(Organization.Attribute.PHONE.name)
            editor.remove(Organization.Attribute.EMAIL.name)
            editor.remove(Organization.Attribute.LAT.name)
            editor.remove(Organization.Attribute.LNG.name)
            editor.commit()
        }

        fun putTestDataPreference(show: Boolean) {
            val editor = getSharedPreferencesEditor()
            editor.putBoolean(TEST_DATA_PREFERENCE, show)
            editor.commit()
        }

        fun getTestDataPreference(): Boolean {
            val sharedPreferences = getSharedPreferencesStorage()
            return sharedPreferences.getBoolean(TEST_DATA_PREFERENCE, false)
        }

        fun getStringObj(key: String): String? {
            val sharedPreferences = getSharedPreferencesStorage()
            return sharedPreferences.getString(key, "")
        }

        fun putOrganizationAttributes(organization: Organization) {
            val editor = getSharedPreferencesEditor()
            editor.putString(Organization.Attribute.NAME.name, organization.name)
            editor.putString(Organization.Attribute.SCHEDULE.name, organization.schedule)
            editor.putString(Organization.Attribute.LOCATION.name, organization.location)
            editor.putString(Organization.Attribute.PAGE.name, organization.webPage)
            editor.putString(Organization.Attribute.PHONE.name, organization.phone)
            editor.putString(Organization.Attribute.EMAIL.name, organization.user.email)
            editor.putString(Organization.Attribute.LAT.name, organization.lat.toString())
            editor.putString(Organization.Attribute.LNG.name, organization.lng.toString())
            editor.commit()
        }

        fun putOrganizationAttributes(userEmail: String, organization: OrganizationDetails) {
            val editor = getSharedPreferencesEditor()
            editor.putString(Organization.Attribute.NAME.name, organization.name())
            editor.putString(Organization.Attribute.SCHEDULE.name, organization.hourHand())
            editor.putString(Organization.Attribute.LOCATION.name, organization.location()?.name())
            editor.putString(Organization.Attribute.PAGE.name, organization.webPage())
            editor.putString(Organization.Attribute.PHONE.name, organization.phone())
            editor.putString(Organization.Attribute.EMAIL.name, userEmail)
            editor.putString(Organization.Attribute.LAT.name, organization.location()?.lat().toString())
            editor.putString(Organization.Attribute.LNG.name, organization.location()?.lng().toString())
            editor.commit()
        }

        fun getOrganizationAttributes(): HashMap<String, String?> {
            val sharedPreferences = getSharedPreferencesStorage()
            val name = Organization.Attribute.NAME.name
            val schedule = Organization.Attribute.SCHEDULE.name
            val location = Organization.Attribute.LOCATION.name
            val page = Organization.Attribute.PAGE.name
            val phone = Organization.Attribute.PHONE.name
            val email = Organization.Attribute.EMAIL.name
            val lat = Organization.Attribute.LAT.name
            val lng = Organization.Attribute.LNG.name
            return hashMapOf(
                name to sharedPreferences.getString(name, ""),
                schedule to sharedPreferences.getString(schedule, ""),
                location to sharedPreferences.getString(location, ""),
                page to sharedPreferences.getString(page, ""),
                phone to sharedPreferences.getString(phone, ""),
                email to sharedPreferences.getString(email, ""),
                lat to sharedPreferences.getString(lat, ""),
                lng to sharedPreferences.getString(lng, "")
            )
        }

    }

}