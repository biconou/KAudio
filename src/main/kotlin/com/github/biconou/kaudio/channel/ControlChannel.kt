package com.github.biconou.kaudio.channel

interface ControlChannel {
    fun pull(): ControlMessage
    fun push(controlMessage: ControlMessage)
    fun pause()
    fun play()
}