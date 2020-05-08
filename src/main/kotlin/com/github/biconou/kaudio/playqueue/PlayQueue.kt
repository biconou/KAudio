package com.github.biconou.kaudio.playqueue

import com.github.biconou.kaudio.Logging
import com.github.biconou.kaudio.audio.stream.bytesPerSecond
import com.github.biconou.kaudio.audio.stream.readOneSecond
import com.github.biconou.kaudio.channel.ControlChannel
import com.github.biconou.kaudio.channel.DataChannel
import com.github.biconou.kaudio.audio.format.computeFormatKey
import com.github.biconou.kaudio.audio.system.getPCMAudioInputStream
import com.github.biconou.kaudio.channel.DataMessage
import com.github.biconou.kaudio.channel.DataMessageType
import org.slf4j.LoggerFactory
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread


class PlayQueue : Logging {

    companion object {
        private val logger = LoggerFactory.getLogger(PlayQueue.javaClass)
        private val endPath = Paths.get("dummy")
    }


    private var dataChannel: DataChannel? = null
    private var controlChannel: ControlChannel? = null

    private var dataProductionThread: Thread? = null

    private var closed = false

    // Attention cette déclaration d'objet doit être avant le init. Ce n'est pas très sécure.
   /* private val items = object {
        var currentIndex: Int = 0
        val list: CopyOnWriteArrayList<Path> = CopyOnWriteArrayList()

        fun add(filePath: Path) {
            list.add(filePath)
        }

        fun current(): Path? {
            return try {
                list[currentIndex]
            } catch (e: IndexOutOfBoundsException) {
                null
            }
        }

        fun next(): Path? {
            return try {
                currentIndex++
                list[currentIndex]
            } catch (e: IndexOutOfBoundsException) {
                currentIndex--
                null
            }
        }
    } */

    private val items = LinkedBlockingQueue<Path>()

    private val listeners: MutableList<PlayQueueListener> = ArrayList()

    fun registerListener(listener: PlayQueueListener) {
        listeners.add(listener)
    }
    
    fun bindControlChannel(controlChannel: ControlChannel) {
        this.controlChannel = controlChannel
    }

    fun bindDataChannel(dataChannel: DataChannel) {
        this.dataChannel = dataChannel
        dataProductionThread = thread(start = true, name = "PlayQueue_producerThread") {
            var terminate = false
            while (!terminate) {
                items.take().apply {
                    if (this == endPath) {
                        terminate = true
                    } else {
                        sendData(this)
                    }
                }
            }
            listeners.forEach { it.endQueue() }
        }
    }

    private fun sendData(item: Path) {
        logger.debug("PlayQueue : start sending DATA for {}", this)
        val audioStream = getPCMAudioInputStream(item.toFile())

        dataChannel?.begin(audioStream.format.computeFormatKey())

        // TODO faire de buffer un extension property
        val buffer = ByteArray(audioStream.bytesPerSecond)
        do {
            val bytesActuallyRead = audioStream.readOneSecond(buffer)
            if (bytesActuallyRead > 0) dataChannel?.push(DataMessage(audioStream.format.computeFormatKey(), buffer.copyOf(), bytesActuallyRead, DataMessageType.DATA))
        } while (bytesActuallyRead > 0)

        // End of item AudioStream as been reached
        dataChannel?.end(audioStream.format.computeFormatKey())

        logger.debug("PlayQueue : end sending for {}", this)
    }

    fun add(filePath: Path) {
        if (closed) throw RuntimeException()
        logger.debug("Add audio file {} to playqueue",filePath.toString())
        items.add(filePath)
    }

    fun close() {
        add(endPath)
        closed = true
    }

    fun play() {
        controlChannel?.play()
    }

    fun pause() {
        controlChannel?.pause()
    }
}