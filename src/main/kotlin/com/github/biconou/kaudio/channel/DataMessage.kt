package com.github.biconou.kaudio.channel

import java.io.Serializable

enum class DataMessageType {
    BEGIN, END, DATA
}

data class DataMessage(val audioFormat: String, val data: ByteArray, val lengthInBytes: Int, val type: DataMessageType) : Serializable