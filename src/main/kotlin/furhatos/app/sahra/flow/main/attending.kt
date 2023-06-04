package furhatos.app.sahra.flow.main

import furhatos.app.sahra.*
import furhatos.app.sahra.flow.Parent
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures

var listen_mode = "nointeraction" // listenreply, listen, nointeraction

val Attending: State = state(Parent) {

    // ------------------- CHATTING -------------------

    onEvent("listen") {
        send(LISTENING_STARTED)
        furhat.listen()
    }

    onResponse {

        send(LISTENING_ENDED)

        if (listen_mode == "listenreply") {

            furhat.gesture(Gestures.Thoughtful(duration = 10.0)) // Will stop before 10
            send(THINKING_STARTED)
            val robotResponse = call {
                getChatCompletion()
            } as String?
            send(THINKING_ENDED)
            furhat.stopGestures();

            random(
                furhat.gesture(Gestures.BigSmile(duration = 2.0)),
                furhat.gesture(Gestures.Smile(duration = 2.0)),
                furhat.gesture(Gestures.BigSmile(duration = 1.0)),
                furhat.gesture(Gestures.Smile(duration = 1.0))
            )

            send(SPEECH_STARTED)
            furhat.say(removeEmojis(robotResponse as String) ?: "Could you please repeat that")
            send(SPEECH_DONE)

            send(LISTENING_STARTED)
            furhat.listen()
        }

    }

    onNoResponse {
        send(LISTENING_ENDED)
    }

    // ------------------- PRE DEFINED SPEECH -------------------

    onEvent("introduction") {
        send(LISTENING_ENDED)

        send(SPEECH_STARTED)
        furhat.say("Hello there, I have been looking forward to this conversation for a while")
        send(SPEECH_DONE)

        if (listen_mode == "listenreply") {
            send(LISTENING_STARTED)
            furhat.listen()
        }
    }

    onEvent("goodbye") {
        send(LISTENING_ENDED)

        send(SPEECH_STARTED)
        furhat.say("Thank you for chatting about AI with me today")
        send(SPEECH_DONE)

        // Don't listen at this point otherwise the conversation will continue
        // IF we do bring back listening here, need to change the OpenAI prompt so Sahra knows the conversation is finished rather than trying to restart chatting
    }

    onEvent("SayThis") {
        send(LISTENING_ENDED)

        send(SPEECH_STARTED)
        furhat.say(it.get("data") as String)
        send(SPEECH_DONE)

        // Say & Listen
        if (it.get("new_listen_mode") as String != "") {
            listen_mode = it.get("new_listen_mode") as String
        }

        if (listen_mode == "listenreply") {
            send(LISTENING_STARTED)
            furhat.listen()
        }
    }

    // ------------------- FLOW MANAGEMENT -------------------

    onEvent("NewListenMode") {
        // Blocking - so everything will stop (except for speech, if saying something)
        send(LISTENING_ENDED)
        send(THINKING_ENDED)
        send(SPEECH_DONE) // The speech will continue until complete, however. If the controller wants to stop speech, they can hit the stop speech button also

        listen_mode = it.get("data") as String

        if (listen_mode == "listenreply") {
            send(LISTENING_STARTED)
            furhat.listen()
        }

        if (listen_mode == "nointeraction") {
            furhat.stopListening()
        }

    }

    onEvent("HistoryClear", instant = true) {
        Furhat.dialogHistory.clear()
        furhat.gesture(Gestures.ExpressSad)
    }

    onEvent("SpeechStop", instant = true) {
        furhat.stopSpeaking()
        send(SPEECH_DONE)
        furhat.gesture(Gestures.ExpressDisgust)
    }

    onEvent("ListenStop", instant = true) {
        furhat.stopListening()
        send(LISTENING_ENDED)
        furhat.gesture(Gestures.ExpressDisgust)
    }

    // ------------------- HEAD -------------------

    onEvent("smile", instant = true) {
        furhat.gesture(Gestures.BigSmile(duration = 2.0))
    }

    onEvent("disgust", instant = true) {
        furhat.gesture(Gestures.ExpressDisgust)
    }

    onEvent("nod", instant = true) {
        furhat.gesture(Gestures.Nod)
    }

    onEvent("shake", instant = true) {
        furhat.gesture(Gestures.Shake)
    }

    // ------------------- ATTENTION -------------------

    onEvent("unattend", instant = true) {
        furhat.attendNobody()
    }

    // TODO - this needs more work for stage use, it is OK for web streaming when only 1 person in the room, but will get distracted by people if there are more around. Perhaps for stage use we need to attend a location
    onUserEnter(instant = true) {
        furhat.attend(it)
        furhat.gesture(Gestures.BigSmile)
    }

    onUserLeave(instant = true) {
        furhat.attend(users.random)
        furhat.gesture(Gestures.Smile)
    }

}
