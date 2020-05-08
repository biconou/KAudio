package com.github.biconou.kaudio.player

import com.github.biconou.kaudio.audio.format.SupportedAudioFormats
import com.github.biconou.kaudio.audio.system.findMixerByName
import com.github.biconou.kaudio.channel.DataMessage
import javax.sound.sampled.DataLine
import javax.sound.sampled.LineUnavailableException
import javax.sound.sampled.SourceDataLine

class RealAudioDeviceAdapter(mixerName: String) : AudioDeviceAdapter {

    // TODO ici dans le cas où n'est pas trouvé, ça devrait planter
    private val mixer = findMixerByName(mixerName)

    private var currentFormatKey: String = ""

    private var dataLine: SourceDataLine? = null

    @Throws(LineUnavailableException::class)
    override fun write(dataMessage: DataMessage) {
        if (currentFormatKey != dataMessage.audioFormat) {
            val audioFormat = SupportedAudioFormats.findFormat(dataMessage.audioFormat)
            dataLine?.close()
            val info = DataLine.Info(SourceDataLine::class.java, audioFormat)
            dataLine = mixer?.getLine(info) as SourceDataLine
            dataLine?.open(audioFormat)
            dataLine?.start()
        }
        this.dataLine?.write(dataMessage.data, 0, dataMessage.lengthInBytes)
    }

}