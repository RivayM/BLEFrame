package com.example.bleframe.entities

interface RvData {

    val textTop: String
    val textBot: String
    val lottie: RvLottie?
    val image: RvImage?
    val clickType: ClickType

    interface RvLottie {
        val lottieId: Int
    }

    interface RvImage {
        val imageId: Int
    }

    enum class ClickType {
        TIMEOUT, LOGIN, LOGOUT, DEVICE, LOG,NONE
    }
}