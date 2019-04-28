package com.buel.holyhelper.model
/**
 * Created by blue7 on 2018-04-17.
 */

data class CarModel(
        @JvmField var userName: String? = null,
        @JvmField var userPhotoUri: String? = null,
        @JvmField var userEmail: String? = null,
        @JvmField var userTell: String? = null,
        @JvmField var carUid: String? = null,
        @JvmField var carName: String? = null
)