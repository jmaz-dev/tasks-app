package com.devmasterteam.tasks.service.constants

/**
 * Constantes usadas na aplicação
 */
class TaskConstants private constructor() {

    // SharedPreferences
    object SHARED {
        const val TOKEN_KEY = "tokenkey"
        const val PERSON_KEY = "personkey"
        const val PERSON_NAME = "personname"
        const val PERSON_EMAIL = "personemail"
        const val BIOMETRIC = "false"
    }

    // Requisições API
    object HEADER {
        const val TOKEN_KEY = "token"
        const val PERSON_KEY = "personkey"
    }

    object HTTP {
        const val SUCCESS = 200
        const val AUTH_ERROR = "Não autorizado."
        const val BASE_URL = "http://devmasterteam.com/CursoAndroidAPI/"
    }

    object BUNDLE {
        const val TASKID = "taskid"
        const val TASKFILTER = "taskfilter"
    }

    object FILTER {
        const val ALL = 0
        const val NEXT = 1
        const val EXPIRED = 2
    }

}