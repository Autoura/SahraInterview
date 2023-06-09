package furhatos.app.sahra.flow

import furhatos.app.sahra.*
import furhatos.app.sahra.util.getChatCompletion
import furhatos.app.sahra.util.removeEmojis
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
            if (robotResponse?.isNotEmpty() == true) {
                // If we ONLY get an emoji response, when you remove the emoji you can end up with nothing, so we have to say something. Ideally we don't have only emoji responses. Emojis are removed generally because the text to speech AI mostly doesn't really do a good job at saying an emoji
                furhat.say(removeEmojis(robotResponse).takeIf { it.isNotEmpty() } ?: "Oh")
            } else {
                // Likely an OpenAI issue
                furhat.say("Sorry I didn't understand, please say that again")
            }
            send(SPEECH_ENDED)

            send(LISTENING_STARTED)
            furhat.listen()
        }

    }

    onNoResponse {
        send(LISTENING_ENDED)
    }

    // ------------------- SPEECH DIRECTED BY UI -------------------

    onEvent("introduction") {
        send(LISTENING_ENDED)

        send(SPEECH_STARTED)
        furhat.say("Hello there, I have been looking forward to this conversation for a while")
        send(SPEECH_ENDED)

        if (listen_mode == "listenreply") {
            send(LISTENING_STARTED)
            furhat.listen()
        }
    }

    onEvent("goodbye") {
        send(LISTENING_ENDED)

        send(SPEECH_STARTED)
        furhat.say("Thank you for chatting about AI with me today")
        send(SPEECH_ENDED)

        // Don't listen at this point otherwise the conversation will continue
        // IF we do bring back listening here, need to change the OpenAI prompt so Sahra knows the conversation is finished rather than trying to restart chatting
    }

    onEvent("SayThis") {
        send(LISTENING_ENDED)

        send(SPEECH_STARTED)
        furhat.say(it.get("data") as String)
        send(SPEECH_ENDED)

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
        send(SPEECH_ENDED) // The speech will continue until complete, however. If the controller wants to stop speech, they can hit the stop speech button also

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

    onEvent("SpeechStop") {
        // Has to block, otherwise might start listening, which will be unexpected
        if (furhat.isSpeaking) {
            furhat.stopSpeaking()
            send(SPEECH_ENDED)
            furhat.gesture(Gestures.ExpressDisgust)
        } else {
            // As not speaking, no need to stop speaking. Ensure the UI is in sync with skill
            send(SPEECH_ENDED)
        }
    }

    onEvent("ListenStop") {
        // Has to block, otherwise doesn't actually stop listening
        if (furhat.isListening) {
            furhat.stopListening()
            send(LISTENING_ENDED)
            furhat.gesture(Gestures.ExpressDisgust)
        } else {
            // As not listening, no need to stop listening. Ensure the UI is in sync with skill
            send(LISTENING_ENDED)
        }
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

    // TODO - this needs more work. We don't attend, as for streaming use, we need to keep looking at the camera
    onUserEnter(instant = true) {
        furhat.glance(it, async = true)
        furhat.gesture(Gestures.BigSmile)
    }

}
