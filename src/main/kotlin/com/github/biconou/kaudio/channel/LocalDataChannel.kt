package com.github.biconou.kaudio.channel

import java.util.concurrent.LinkedBlockingQueue

class LocalDataChannel(capacity: Int) : DataChannel {

    private val queue = LinkedBlockingQueue<DataMessage>(capacity)

    private val dummyData = ByteArray(0)

    override fun push(audioDataMessage: DataMessage) {
        queue.put(audioDataMessage)
    }

    override fun pull(): DataMessage {
        return queue.take()
    }

    override fun purge() {
        queue.clear()
    }

    override fun begin(audioFormatKey: String) {
        push(DataMessage(audioFormatKey, dummyData, 0, DataMessageType.BEGIN))
    }

    override fun end(audioFormatKey: String) {
        push(DataMessage(audioFormatKey, dummyData, 0, DataMessageType.END))
    }
}