package com.github.biconou.kaudio.audio.system

import com.github.biconou.kaudio.audio.format.convertToPCMAudioFormat
import java.io.File
import java.io.FileNotFoundException
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Mixer
import javax.sound.sampled.UnsupportedAudioFileException


fun listAllMixers(): Array<Mixer.Info> {
    return AudioSystem.getMixerInfo()
}

fun findMixerByName(mixerName: String): Mixer? {
    val found = listAllMixers().filter { m -> m.name == mixerName }
    if (found.isEmpty()) {
        return null
    } else {
        return AudioSystem.getMixer(found.first())
    }
}

@Throws(FileNotFoundException::class)
fun getPCMAudioInputStream(file: File): AudioInputStream {

    val sourceAudioInputStream: AudioInputStream? = AudioSystem.getAudioInputStream(file)

    return sourceAudioInputStream?.run {
        AudioSystem.getAudioInputStream(convertToPCMAudioFormat(format), this)
    } ?: throw UnsupportedAudioFileException()
}



