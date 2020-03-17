package com.jarroyo.sharedcode.base.exception

import java.lang.Exception

/**
 * KMP_MVVM
 * Created by jake on 2020-03-17, 4:17 PM
 */
class NotAuthenticatedException(message: String? = "Not Authenticated"): Exception(message)