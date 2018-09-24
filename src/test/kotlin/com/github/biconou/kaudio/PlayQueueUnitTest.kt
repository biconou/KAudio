package com.github.biconou.kaudio

import TestResourcesUtils
import assertk.assert
import assertk.assertions.isEqualTo
import com.github.biconou.kaudio.channel.DataChannel
import com.nhaarman.mockito_kotlin.*
import org.junit.jupiter.api.Test

class PlayQueueUnitTest {

    @Test
    fun whenNothingAdded_thenNoDataSentInChannel() {
        // Given
        val mockDataChannel = mock<DataChannel>()
        val playQueue = PlayQueue()
        playQueue.bindDataChannel(mockDataChannel)

        // When
        // Nothing is done with the play queue

        // Then
        verifyZeroInteractions(mockDataChannel)
    }

    @Test
    fun whenAudioFileAdded_thenDataSentInChannel() {
        // Given
        val collectedMessages = ArrayList<Any>()
        val mockDataChannel = mock<DataChannel> {
            on { push(any()) }.then { collectedMessages.add(it.getArgument(0)) }
            on { begin(any()) }.then { collectedMessages.add(it.getArgument(0)) }
            on { end(any()) }.then { collectedMessages.add(it.getArgument(0)) }
        }
        val playQueue = PlayQueue()
        playQueue.bindDataChannel(mockDataChannel)

        // When
        playQueue.add(TestResourcesUtils.resolveFile("/WAV/naim-test-2-wav-16-44100.wav").toPath())

        // TODO find something else to wait till the thread is finished
        Thread.sleep(2000)

        // Then
        verify(mockDataChannel, times(12)).push(any())
        verify(mockDataChannel, times(1)).begin("PCM_SIGNED_44100_16_LE")
        verify(mockDataChannel, times(1)).end("PCM_SIGNED_44100_16_LE")
        assert(collectedMessages.first()).isEqualTo("PCM_SIGNED_44100_16_LE")
        assert(collectedMessages.last()).isEqualTo("PCM_SIGNED_44100_16_LE")
    }
}