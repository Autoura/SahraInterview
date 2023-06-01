package furhatos.app.sahra.flow.main

import furhatos.app.sahra.EventDelivery
import furhatos.app.sahra.INTRODUCTION
import furhatos.app.sahra.GOODBYE
import furhatos.app.sahra.SPEECH_DONE
import furhatos.app.sahra.SPEECH_STARTED
import furhatos.app.sahra.SPEECH_STOP
import furhatos.app.sahra.HISTORY_CLEAR
import furhatos.app.sahra.flow.Parent
import furhatos.event.actions.ActionGaze
import furhatos.flow.kotlin.*

val Welcoming: State = state(Parent) {

    onUserEnter(instant = true) {
        if (!furhat.isAttendingUser()) {
            furhat.attend(it)
            goto(Attending)
        }
    }

}

val Attending: State = state(Parent) {

    onEvent(HISTORY_CLEAR) {
        Furhat.dialogHistory.clear()
    }

    onEvent(SPEECH_STOP) {
        furhat.stopSpeaking()
        send(SPEECH_DONE)
    }

    onEvent(INTRODUCTION) {
        send(SPEECH_STARTED)
        furhat.say("Hello there, I have been looking forward to this conversation for a while")
        send(SPEECH_DONE)
    }

    onEvent(GOODBYE) {
        send(SPEECH_STARTED)
        furhat.say("Thank you for chatting about AI with me today")
        send(SPEECH_DONE)
    }

}
