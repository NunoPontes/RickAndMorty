package com.nunop.rickandmorty

import android.app.Application
import com.facebook.stetho.Stetho
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application(){

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
            Timber.plant(Timber.DebugTree())
        }
    }
}

//Things to do:
//- Add error states
//- Add styles
//- Clean unused variables and other comments
//- Process dead (SavedStateHandle) no need to use, because the way we built nothing is lost if
// process gets killed
//- Dependency injection
//- Search option
//- Filter (that applies on the list and on search)
//- Location details
//- Episode details
//- Redirect to locations and episodes, from characters
//- Redirect to characters from locations and episodes (if it makes sense)
//- Dark theme
//- Works on various size screens
//- UI
//- Unit Tests, maximum coverage
//- UI tests (with usability tests)
//- Dependency injection Hilt?
//- UI Compose <--------------------------------------------------
//- R8/Proguard <-------------------------------------------------
