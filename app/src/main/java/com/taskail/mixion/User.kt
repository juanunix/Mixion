package com.taskail.mixion

/**
 *Created by ed on 2/4/18.
 */
object User {

    private var userName: String? = null
    private var userKey: String? = null

    var forceLockScreenFlag = false
    var userIsLoggedIn = false

    fun getUserName() = userName
    fun getUserKey() = userKey

    fun storeUser(username: String, key: String){
        userName = username
        userKey = key
    }

    fun performLogout() {
        userIsLoggedIn = false
        userName = null
        userKey = null
    }
}