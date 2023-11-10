package com.example.bleframe.data.location

import android.content.Context
import android.location.LocationManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationDataSources @Inject constructor(@ApplicationContext appContext: Context){

    private var locationManager : LocationManager? = null

    init {
        locationManager = appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }


}