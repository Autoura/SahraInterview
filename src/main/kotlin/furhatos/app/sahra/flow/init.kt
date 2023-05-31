package furhatos.app.sahra.flow

import furhatos.app.sahra.*
import furhatos.app.sahra.flow.main.Welcoming
import furhatos.event.senses.SenseSkillGUIConnected
import furhatos.flow.kotlin.*
import furhatos.skills.HostedGUI
import furhatos.flow.kotlin.voice.AzureVoice
import furhatos.flow.kotlin.voice.PollyVoice

import furhatos.skills.SingleUserEngagementPolicy
import furhatos.autobehavior.enableSmileBack
import java.awt.Color

// Our GUI declaration
val GUI = HostedGUI("MonitorGUI", "assets/gui/dist", PORT)

// Starting state, before our GUI has connected.
val NoGUI: State = state(null) {
    onEvent<SenseSkillGUIConnected> {
        goto(GUIConnected)
    }
}

/*
    Here we know our GUI has connected. Since the user might close down the GUI and then reopen
    again, we inherit our handler from the NoGUI state. An edge case might be that a second GUI
    is opened, but this is not accounted for here.

 */
val GUIConnected = state(NoGUI) {

    init {
        furhat.voice = if (AzureVoice("SoniaNeural").isAvailable) AzureVoice("SoniaNeural") else PollyVoice.Matthew()
        furhat.setCharacter("Jane")
        furhat.ledStrip.solid(Color(0, 0, 0)) // Turn LED off
        furhat.enableSmileBack = true

        goto(Welcoming)
    }

}