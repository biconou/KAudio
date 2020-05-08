package com.github.biconou.kaudio

import com.github.biconou.kaudio.channel.DataMessage
import com.github.biconou.kaudio.channel.LocalControlChannel
import com.github.biconou.kaudio.channel.LocalDataChannel
import com.github.biconou.kaudio.player.AudioDeviceAdapter
import com.github.biconou.kaudio.player.Player
import com.github.biconou.kaudio.playqueue.PlayQueue
import com.nhaarman.mockito_kotlin.mock
import org.junit.Test
import assertk.assert
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
import com.github.biconou.kaudio.audio.stream.bytesPerSecond
import com.github.biconou.kaudio.audio.stream.readOneSecond
import com.github.biconou.kaudio.audio.system.getPCMAudioInputStream
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer


class SocialTest {

    @Test
    fun test() {
        val packetSizes = ArrayList<Int>()
        val device = mock<AudioDeviceAdapter> {
            on { write(any()) } doAnswer  { invocationOnMock ->
                val dataMessage = invocationOnMock.getArgument<DataMessage>(0)
                packetSizes.add(dataMessage.lengthInBytes)
                null
            }
        }
        val playQueue = PlayQueue()
        val player = Player(device)
        val controlChannel = LocalControlChannel()
        val dataChannel = LocalDataChannel(3)

        playQueue.bindControlChannel(controlChannel)
        playQueue.bindDataChannel(dataChannel)

        player.bindControlChannel(controlChannel)
        player.bindDataChannel(dataChannel)

        val file = TestResourcesUtils.resolveFile("/WAV/naim-test-2-wav-16-44100.wav")
        assert(file.exists()).isTrue()
        playQueue.add(file.toPath())
        playQueue.play()
        Thread.sleep(10000)
        assert(packetSizes).containsExactly(176400,176400,176400,176400,176400,176400,176400,176400,176400,176400,176400,1940)
    }
}