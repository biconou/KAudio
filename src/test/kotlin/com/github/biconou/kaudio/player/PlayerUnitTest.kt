package com.github.biconou.kaudio.player

import com.github.biconou.kaudio.audio.format.computeFormatKey
import com.github.biconou.kaudio.audio.format.defaultAudioFormat
import com.github.biconou.kaudio.channel.*
import com.nhaarman.mockito_kotlin.*
import org.junit.Test
import org.mockito.internal.verification.Times

class PlayerUnitTest {

    @Test
    fun whenCreatePlayer_thenNoError() {
        val device = mock<AudioDeviceAdapter>()
        Player(device)
    }

    @Test
    fun whenSendOneDataMessageButNoStart_thenAudioDeviceRecievesNothing() {
        // Given
        val device = mock<AudioDeviceAdapter>()
        val dataMessage = DataMessage(defaultAudioFormat.computeFormatKey(), ByteArray(2), 2, DataMessageType.DATA)
        val dataChannel = mock<DataChannel> {
            on { pull() } doReturn dataMessage
        }
        val player = Player(device)

        // When
        player.bindDataChannel(dataChannel)
        Thread.sleep(10)

        // Then
        verify(device, times(0)).write(dataMessage)
    }

    @Test
    fun writeSendDataMessagesAndStartPlay_thenAudioDeviceRecievesMessages() {
        // Given
        val device = mock<AudioDeviceAdapter>()
        val dataMessage = DataMessage(defaultAudioFormat.computeFormatKey(), ByteArray(2), 2, DataMessageType.DATA)
        val dataChannel = mock<DataChannel> {
            var count = 0
            on { pull() } doAnswer {
                if (count < 5) {
                    count ++
                    dataMessage
                } else {
                    val lock = Object()
                    synchronized(lock) {
                        lock.wait()
                    }
                    null
                }
            }
        }

        val controlChannel = mock<ControlChannel> {
            var count = 0
            on { pull() } doAnswer {
                if (count < 1) {
                    count ++
                    ControlMessage (ControlMessageType.PLAY)
                } else {
                    val lock = Object()
                    synchronized(lock) {
                        lock.wait()
                    }
                    null
                }
            }
        }

        val player = Player(device)

        // When
        player.bindDataChannel(dataChannel)
        player.bindControlChannel(controlChannel)
        Thread.sleep(100)

        // Then
        verify(device, Times(5)).write(dataMessage)
    }

}