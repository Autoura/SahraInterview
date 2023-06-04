package furhatos.app.sahra

import furhatos.event.Event

val PORT = 8100 // GUI Port

val SPEECH_DONE = "SpeechDone"
val SPEECH_STARTED = "SpeechStarted"
val LISTENING_STARTED = "ListeningStarted"
val LISTENING_ENDED = "ListeningEnded"
val THINKING_STARTED = "ThinkingStarted"
val THINKING_ENDED = "ThinkingEnded"

// Event used to pass data to GUI
class KeepAliveDelivery(
        val dateAlive : String
) : Event()
