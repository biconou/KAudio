package com.github.biconou.kaudio.audio.format

import javax.sound.sampled.AudioFormat


fun AudioFormat.computeFormatKey(): String {
    val sb = StringBuilder()
    sb.append(encoding).append("_")
    sb.append(sampleRate.toLong()).append("_")
    sb.append(sampleSizeInBits).append("_")
    if (isBigEndian) {
        sb.append("BE")
    } else {
        sb.append("LE")
    }
    return sb.toString()
}

object SupportedAudioFormats {
    private val formats = listOf(
            AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    44100.toFloat(),
                    16,
                    2,
                    4,
                    44100.toFloat(),
                    false)
    )

    private val formatsMap = mutableMapOf<String, AudioFormat>()

    init {
        with(formatsMap) {
            formats.forEach { put(it.computeFormatKey(), it) }
        }
    }

    fun findFormat(formatKey: String): AudioFormat? {
        return formatsMap[formatKey]
    }
}

fun convertToPCMAudioFormat(sourceFormat: AudioFormat): AudioFormat {
    var targetFormat = sourceFormat
    if (sourceFormat.encoding != AudioFormat.Encoding.PCM_SIGNED) {
        val targetFrameRate = sourceFormat.sampleRate
        var targetSampleSizeInBits = sourceFormat.sampleSizeInBits
        if (targetSampleSizeInBits == -1) {
            targetSampleSizeInBits = 16
        }
        val targetChannels = sourceFormat.channels
        val targetFrameSize = targetChannels * targetSampleSizeInBits / 8

        targetFormat = AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                sourceFormat.sampleRate,
                targetSampleSizeInBits,
                targetChannels,
                targetFrameSize,
                targetFrameRate,
                false)

    }
    return targetFormat
}
