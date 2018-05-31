package com.github.biconou.kaudio.channel


enum class ControlMessageType {
    PLAY,
    PAUSE
}

data class ControlMessage(val type: ControlMessageType)