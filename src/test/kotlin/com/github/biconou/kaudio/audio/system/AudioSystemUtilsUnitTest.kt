package com.github.biconou.kaudio.audio.system

import TestResourcesUtils
import assertk.assert
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.FileNotFoundException
import javax.sound.sampled.AudioFormat


class AudioSystemUtilsUnitTest {

    @Test
    @DisplayName("Test retrieve PCM audio input stream from a WAV file")
    fun pcmAudioInputStreamFromWAVFile() {
        val audioInputStream = getPCMAudioInputStream(TestResourcesUtils.resolveFile("/WAV/naim-test-2-wav-16-44100.wav"))
        assert(audioInputStream.format.channels).isEqualTo(2)
        assert(audioInputStream.format.sampleRate).isEqualTo(44100f)
        assert(audioInputStream.format.encoding).isEqualTo(AudioFormat.Encoding.PCM_SIGNED)
        assert(audioInputStream.format.frameRate).isEqualTo(44100.0f)
        assert(audioInputStream.format.frameSize).isEqualTo(4)
        assert(audioInputStream.format.isBigEndian).isEqualTo(false)
        assert(audioInputStream.format.sampleSizeInBits).isEqualTo(16)
    }

    @Test
    @DisplayName("Test retrieve PCM audio input stream from non exiting file")
    fun pcmAudioInputStreamFromNonExistingFile() {
        assertThrows(FileNotFoundException::class.java) {
            getPCMAudioInputStream(TestResourcesUtils.resolveFile("FOO"))
        }
    }
}