package com.github.biconou.kaudio.channel

import java.rmi.server.UnicastRemoteObject

class RemoteControlChannelImpl : UnicastRemoteObject(), RemoteControlChannel {


    val localControlChannel = LocalControlChannel()

    override fun pull(): ControlMessage {
        return localControlChannel.pull()
    }

    override fun push(controlMessage: ControlMessage) {
        localControlChannel.push(controlMessage)
    }

    override fun pause() {
        localControlChannel.pause()
    }

    override fun play() {
        localControlChannel.play()
    }
}