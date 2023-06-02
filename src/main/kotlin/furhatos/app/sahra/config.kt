package furhatos.app.sahra

import furhatos.event.Event

val PORT = 8100 // GUI Port
val INTRODUCTION = "introduction"
val GOODBYE = "goodbye"
val SPEECH_DONE = "SpeechDone"
val SPEECH_STARTED = "SpeechStarted"
val SPEECH_STOP = "SpeechStop"
val HISTORY_CLEAR = "HistoryClear"
val NEW_LISTEN_MODE = "NewListenMode"
val LISTEN = "listen"

// Event used to pass data to GUI
class KeepAliveDelivery(
        val dateAlive : String
) : Event()
