package furhatos.app.sahra.flow.main

import furhatos.app.sahra.INTRODUCTION
import furhatos.app.sahra.GOODBYE
import furhatos.app.sahra.SPEECH_DONE
import furhatos.app.sahra.SPEECH_STARTED
import furhatos.app.sahra.SPEECH_STOP
import furhatos.app.sahra.HISTORY_CLEAR
import furhatos.app.sahra.NEW_LISTEN_MODE
import furhatos.app.sahra.LISTEN
import furhatos.app.sahra.flow.Parent
import furhatos.app.sahra.util.gaze.GazeAversion
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures

var listen_mode = "listenreply" // listenreply, listen, nointeraction

val Attending: State = state(Parent) {


    // ------------------- CHATTING -------------------

    onEvent(LISTEN) {
        furhat.listen()
    }

    onResponse {

        if (listen_mode == "listenreply") {

            furhat.gesture(GazeAversion(2.0))
            furhat.gesture(Gestures.Thoughtful)

            val robotResponse = call {
                getChatCompletion()
            } as String?

            furhat.ask(robotResponse ?: "Could you please repeat that")
        }

    }

    onNoResponse {

    }

    // ------------------- PRE DEFINED SPEECH -------------------

    onEvent(INTRODUCTION) {
        send(SPEECH_STARTED)
        furhat.say("Hello there, I have been looking forward to this conversation for a while")
        send(SPEECH_DONE)

        if (listen_mode == "listenreply") {
            furhat.listen()
        }
    }

    onEvent(GOODBYE) {
        send(SPEECH_STARTED)
        furhat.say("Thank you for chatting about AI with me today")
        send(SPEECH_DONE)

        if (listen_mode == "listenreply") {
            furhat.listen()
        }
    }

    // ------------------- FLOW MANAGEMENT -------------------

    onEvent(NEW_LISTEN_MODE) {
        listen_mode = it.get("data") as String

        if (listen_mode == "listenreply") {
            furhat.listen()
        }
    }

    onEvent(HISTORY_CLEAR) {
        Furhat.dialogHistory.clear()
        furhat.gesture(Gestures.ExpressSad)
    }

    onEvent(SPEECH_STOP) {
        furhat.stopSpeaking()
        send(SPEECH_DONE)
        furhat.gesture(Gestures.ExpressDisgust)

        if (listen_mode == "listenreply") {
            furhat.listen()
        }
    }

    // ------------------- ATTENTION -------------------

    // TODO - this needs more work for stage use, it is OK for web streaming when only 1 person in the room, but will get distracted by people if there are more around. Perhaps for stage use we need to attend a location
    onUserEnter() {
        furhat.attend(it)
        furhat.gesture(Gestures.BigSmile)
    }

    onUserLeave() {
        furhat.attend(users.random)
        furhat.gesture(Gestures.Smile)
    }

}
