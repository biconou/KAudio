package com.github.biconou.kaudio.player

import com.github.biconou.kaudio.channel.DataMessage

interface AudioDeviceAdapter {

    fun write(data: DataMessage)
}
