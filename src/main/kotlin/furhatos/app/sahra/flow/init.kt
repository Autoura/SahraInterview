package furhatos.app.sahra.flow

import furhatos.app.sahra.*
import furhatos.app.sahra.flow.main.Attending
import furhatos.event.senses.SenseSkillGUIConnected
import furhatos.flow.kotlin.*
import furhatos.skills.HostedGUI
import furhatos.flow.kotlin.voice.AzureVoice
import furhatos.flow.kotlin.voice.PollyVoice

import furhatos.autobehavior.enableSmileBack
import java.awt.Color

import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

// Our GUI declaration
val GUI = HostedGUI("MonitorGUI", "assets/gui/dist", PORT)
val timer = Timer()

// Starting state, before our GUI has connected.
val NoGUI: State = state(null) {
    onEvent<SenseSkillGUIConnected> {
        goto(GUIConnected)
    }
}

val GUIConnected = state(NoGUI) {

    init {
        furhat.voice = if (AzureVoice("SoniaNeural").isAvailable) AzureVoice("SoniaNeural") else PollyVoice.Matthew()
        furhat.setCharacter("Jane")
        furhat.ledStrip.solid(Color(0, 0, 0)) // Turn LED off
        furhat.enableSmileBack = true

        // Keep sending events to the GUI app, so we can see there is a connection between the two
        val interval = 3000 // 3 seconds

        furhat.param.noSpeechTimeout = 20000
        furhat.param.maxSpeechTimeout = 20000 // 20 seconds is max on Azure

        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {

                val currentDateTime = LocalDateTime.now(ZoneOffset.UTC)
                val dateString = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

                println("KeepAlive timer called at $dateString")
                send(KeepAliveDelivery(dateAlive = dateString))
            }
        }, 0, interval.toLong())

        goto(Attending)
    }

}