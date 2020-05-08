package com.github.biconou.kaudio.channel

import java.rmi.server.UnicastRemoteObject

class RemoteDataChannelImpl : UnicastRemoteObject(), RemoteDataChannel {

    val localDataChannel = LocalDataChannel(10)
    private val dummyData = ByteArray(0)

    override fun push(audioDataMessage: DataMessage) {
        localDataChannel.push(audioDataMessage)
    }

    override fun pull(): DataMessage {
        return localDataChannel.pull()
    }

    override fun purge() {
        localDataChannel.purge()
    }

    override fun begin(audioFormatKey: String) {
        localDataChannel.push(DataMessage(audioFormatKey, dummyData, 0, DataMessageType.BEGIN))
    }

    override fun end(audioFormatKey: String) {
        localDataChannel.push(DataMessage(audioFormatKey, dummyData, 0, DataMessageType.END))
    }
}