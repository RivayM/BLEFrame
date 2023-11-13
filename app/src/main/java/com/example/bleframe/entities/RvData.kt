package com.example.bleframe.entities

interface RvData {

    val textTop: String
    val textBot: String
    val lottie : RvLottie?
    val image  : RvImage?
    val setting: RvSetting?

    interface RvLottie{
        val lottie: TypeLottie

        enum class TypeLottie {
            SEARCH,BUTTON
        }
    }

    interface RvImage{
        val image: Int
    }

    interface RvSetting{
        val setting: TypeSetting

        enum class TypeSetting{
            TIMEOUT,LOGIN,LOGOUT
        }
    }
}