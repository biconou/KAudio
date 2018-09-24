package com.github.biconou.kaudio

import com.github.biconou.kaudio.channel.*


class PlayerBuilder(playerName: String) {

    var dataChannel: DataChannel
    var controlChannel: ControlChannel
    var player: Player? = null

    init {
        if (playerName.contains(char = '@')) {
            val splitted = playerName.split("@")
            val host = splitted.component2()
            controlChannel = RemoteControlChannelAdapter(host)
            dataChannel = RemoteDataChannelAdapter(host)
        } else {
            dataChannel = LocalDataChannel(10)
            controlChannel = LocalControlChannel()
            player = Player(playerName)
            /* player.bindControlChannel(controlChannel)
            player.bindDataChannel(dataChannel) */
        }
    }
}