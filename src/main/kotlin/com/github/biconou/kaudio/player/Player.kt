package com.github.biconou.kaudio.player

import com.github.biconou.kaudio.channel.ControlChannel
import com.github.biconou.kaudio.channel.DataChannel
import com.github.biconou.kaudio.audio.system.findMixerByName
import com.github.biconou.kaudio.audio.format.SupportedAudioFormats
import com.github.biconou.kaudio.audio.format.computeFormatKey
import com.github.biconou.kaudio.audio.format.defaultAudioFormat
import com.github.biconou.kaudio.channel.ControlMessageType
import com.github.biconou.kaudio.channel.DataMessageType
import org.slf4j.LoggerFactory
import sun.audio.AudioDevice.device
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.DataLine
import javax.sound.sampled.LineUnavailableException
import javax.sound.sampled.SourceDataLine
import kotlin.concurrent.thread

class Player(private val device: AudioDeviceAdapter) {

    companion object {
        private val logger = LoggerFactory.getLogger(Player.javaClass)
    }

    private val lock = Object()
    private var mustWait: Boolean = true

    private var controlChannel: ControlChannel? = null
    private var dataChannel: DataChannel? = null

    fun bindControlChannel(controlChannel: ControlChannel) {
        this.controlChannel = controlChannel
        thread(start = true, name = "player_control_thread") {
            while (true) {
                val controlMessage = controlChannel.pull()
                when (controlMessage.type) {
                    ControlMessageType.PLAY -> synchronized(lock) {
                        mustWait = false
                        lock.notify()
                    }
                    ControlMessageType.PAUSE -> synchronized(lock) {
                        mustWait = true
                    }
                }
            }
        }
    }

    fun bindDataChannel(dataChannel: DataChannel) {
        this.dataChannel = dataChannel
        thread(start = true, name = "player_data_thread") {
            while (true) {
                if (mustWait) {
                    synchronized(lock) {
                        lock.wait()
                    }
                }
                val dataMessage = dataChannel.pull()
                when (dataMessage.type) {
                    DataMessageType.BEGIN -> {}
                    DataMessageType.DATA -> device.write(dataMessage)
                }
            }
        }
    }
}