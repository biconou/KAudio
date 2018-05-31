package com.github.biconou.kaudio.channel

import java.rmi.registry.LocateRegistry

class RemoteControlChannelAdapter(host: String): ControlChannel{

    var remoteControlChannel: RemoteControlChannel

    init {
        val registry = LocateRegistry.getRegistry(host)
        remoteControlChannel = registry.lookup("controlChannel") as RemoteControlChannel
    }

    override fun push(controlMessage: ControlMessage) {
        remoteControlChannel.push(controlMessage)
    }

    override fun pause() {
        remoteControlChannel.pause()
    }

    override fun play() {
        remoteControlChannel.play()
    }

    override fun pull(): ControlMessage {
        return remoteControlChannel.pull()
    }
}