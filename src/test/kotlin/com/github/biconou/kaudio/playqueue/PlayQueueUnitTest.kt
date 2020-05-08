package com.github.biconou.kaudio.playqueue

import TestResourcesUtils
import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.github.biconou.kaudio.channel.ControlChannel
import com.github.biconou.kaudio.channel.DataChannel
import com.nhaarman.mockito_kotlin.*
import org.junit.jupiter.api.Test

class PlayQueueUnitTest {

    val collectedMessages = ArrayList<Any>()
    val mockDataChannel = mock<DataChannel> {
        on { push(any()) }.then { collectedMessages.add(it.getArgument(0)) }
        on { begin(any()) }.then { collectedMessages.add(it.getArgument(0)) }
        on { end(any()) }.then { collectedMessages.add(it.getArgument(0)) }
    }
    val mockControlChannel = mock<ControlChannel> {}
    val playQueue = PlayQueue()

    @Test
    fun whenNoItemAddedToPlayQueue_thenNoDataSentInChannel() {
        // Given
        playQueue.bindDataChannel(mockDataChannel)

        // When
        // Nothing is done with the play queue

        // Then
        verifyZeroInteractions(mockDataChannel)
    }

    @Test
    fun whenAudioFileAddedToPlayQueue_thenDataSentInChannel() {
        // Given
        playQueue.bindDataChannel(mockDataChannel)
        val waitListener = WaitPlayQueueListener()
        playQueue.registerListener(waitListener)

        // When
        playQueue.add(TestResourcesUtils.resolveFile("/WAV/naim-test-2-wav-16-44100.wav").toPath())
        playQueue.close()
        waitListener.waitEndQueue()

        // Then
        verify(mockDataChannel, times(12)).push(any())
        verify(mockDataChannel, times(1)).begin("PCM_SIGNED_44100_16_LE")
        verify(mockDataChannel, times(1)).end("PCM_SIGNED_44100_16_LE")
        assert(collectedMessages.first()).isEqualTo("PCM_SIGNED_44100_16_LE")
        assert(collectedMessages.last()).isEqualTo("PCM_SIGNED_44100_16_LE")
    }

    @Test
    fun whenPlayQueueEnds_thenListenerNotified() {
        // Given
        playQueue.bindDataChannel(mockDataChannel)
        val waitListener = WaitPlayQueueListener()
        playQueue.registerListener(waitListener)
        val mockListener1 = mock<PlayQueueListener>()
        val mockListener2 = mock<PlayQueueListener>()
        playQueue.registerListener(mockListener1)
        playQueue.registerListener(mockListener2)

        // When
        playQueue.add(TestResourcesUtils.resolveFile("/WAV/naim-test-2-wav-16-44100.wav").toPath())
        playQueue.close()
        waitListener.waitEndQueue()

        // Then
        verify(mockListener1).endQueue()
        verify(mockListener2).endQueue()
    }

    @Test
    fun whenPlayQueueIsClosed_thenAddItemFails() {
        // Given
        playQueue.bindDataChannel(mockDataChannel)

        // When
        playQueue.add(TestResourcesUtils.resolveFile("/WAV/naim-test-2-wav-16-44100.wav").toPath())
        playQueue.close()
        assert {
            playQueue.add(TestResourcesUtils.resolveFile("/WAV/naim-test-2-wav-16-44100.wav").toPath())
        }.thrownError {
            isInstanceOf(RuntimeException::class)
        }
    }

    @Test
    fun whenPlay_thenControlChannelRecievesPlay() {
        // Given
        playQueue.bindControlChannel(mockControlChannel)

        // When
        playQueue.play()

        // Then
        verify(mockControlChannel).play()
    }

    @Test
    fun whenPause_thenControlChannelRecievesPause() {
        // Given
        playQueue.bindControlChannel(mockControlChannel)

        // When
        playQueue.pause()

        // Then
        verify(mockControlChannel).pause()
    }
}