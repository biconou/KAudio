package com.github.biconou.kaudio.channel

interface DataChannel {
    fun push(audioDataMessage: DataMessage)
    fun pull(): DataMessage
    fun purge()
    fun begin(audioFormatKey: String)
    fun end(audioFormatKey: String)
}