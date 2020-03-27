package com.buel.holyhelpers.model

/**
 * Created by blue7 on 2018-04-17.
 */

data class UserModel(
        @JvmField var userName: String? = null,
        @JvmField var userPhotoUri: String? = null,
        @JvmField var userEmail: String? = null,
        @JvmField var userTell: String? = null,
        @JvmField var uid: String? = null,
        @JvmField var corpsUID: String? = null,
        @JvmField var pushToken: String? = null,
        @JvmField var adminUID: String? = null,
        @JvmField var userType: String? = null,
        @JvmField var corpsName: String? = null,
        @JvmField var macAddress: String? = null,
        @JvmField var message: String? = null,
        @JvmField var permission: String? = null
)