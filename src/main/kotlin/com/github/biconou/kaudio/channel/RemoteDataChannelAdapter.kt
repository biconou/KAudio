package com.github.biconou.kaudio.channel

import java.rmi.registry.LocateRegistry


class RemoteDataChannelAdapter(host: String) : DataChannel {

    var remoteDataChannel: RemoteDataChannel

    init {
        val registry = LocateRegistry.getRegistry(host)
        remoteDataChannel = registry.lookup("dataChannel") as RemoteDataChannel

    }

    override fun push(audioDataMessage: DataMessage) {
        remoteDataChannel.push(audioDataMessage)
    }

    override fun pull(): DataMessage {
        return remoteDataChannel.pull()
    }

    override fun purge() {
        remoteDataChannel.purge()
    }

    override fun begin(audioFormatKey: String) {
        remoteDataChannel.begin(audioFormatKey)
    }

    override fun end(audioFormatKey: String) {
        remoteDataChannel.end(audioFormatKey)
    }


}