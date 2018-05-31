package com.github.biconou.kaudio

import com.github.biconou.kaudio.channel.RemoteControlChannelImpl
import com.github.biconou.kaudio.channel.RemoteDataChannelImpl
import java.rmi.registry.LocateRegistry


fun main(args: Array<String>) {

    val dataChannel = RemoteDataChannelImpl()
    val controlChannel = RemoteControlChannelImpl()

    val registry = LocateRegistry.createRegistry(1099);

    registry.bind("dataChannel", dataChannel)
    registry.bind("controlChannel", controlChannel)
    registry.list().forEach { println(it) }

    val player = Player("default [default]")
    player.bindControlChannel(controlChannel.localControlChannel)
    player.bindDataChannel(dataChannel.localDataChannel)

    println("Serveur lancé")
}