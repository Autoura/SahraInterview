package furhatos.app.sahra.flow.main

import furhatos.app.sahra.INTRODUCTION
import furhatos.app.sahra.GOODBYE
import furhatos.app.sahra.SPEECH_DONE
import furhatos.app.sahra.SPEECH_STARTED
import furhatos.app.sahra.SPEECH_STOP
import furhatos.app.sahra.HISTORY_CLEAR
import furhatos.app.sahra.flow.Parent
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures

val Attending: State = state(Parent) {

    // TODO - this needs more work for stage use, it is OK for web streaming when only 1 person in the room, but will get distracted by people if there are more around. Perhaps for stage use we need to attend a location
    onUserEnter() {
        furhat.attend(it)
        furhat.gesture(Gestures.Smile)
    }

    onUserLeave() {
        furhat.attend(users.random)
        furhat.gesture(Gestures.Smile)
    }

    onEvent(HISTORY_CLEAR) {
        Furhat.dialogHistory.clear()
        furhat.gesture(Gestures.ExpressSad)
    }

    onEvent(SPEECH_STOP) {
        furhat.stopSpeaking()
        send(SPEECH_DONE)
        furhat.gesture(Gestures.ExpressDisgust)
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
