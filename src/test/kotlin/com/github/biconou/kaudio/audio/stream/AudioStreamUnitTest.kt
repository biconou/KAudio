package com.github.biconou.kaudio.audio.stream

import TestResourcesUtils
import assertk.assert
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import com.github.biconou.kaudio.audio.system.getPCMAudioInputStream
import org.junit.jupiter.api.Test

class AudioStreamUnitTest {



    @Test
    fun oneSecondAudio_WAV_16_44100() {
        val audioInputStream = getPCMAudioInputStream(TestResourcesUtils.resolveFile("/WAV/naim-test-2-wav-16-44100.wav"))
        assert(audioInputStream.bytesPerSecond).isEqualTo(176400)
    }

    @Test
    fun readPerSeconds_WAV_16_44100() {
        val audioInputStream = getPCMAudioInputStream(TestResourcesUtils.resolveFile("/WAV/naim-test-2-wav-16-44100.wav"))
        val packetSizes = ArrayList<Int>()
        val buffer = ByteArray(audioInputStream.bytesPerSecond)
        do {
            val nbRead = audioInputStream.readOneSecond(buffer)
            packetSizes.add(nbRead)
        } while (nbRead > -1)
        assert(packetSizes).containsExactly(176400,176400,176400,176400,176400,176400,176400,176400,176400,176400,176400,1940,-1)
    }

}