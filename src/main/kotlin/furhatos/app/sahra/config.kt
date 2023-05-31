package furhatos.app.sahra

import furhatos.event.Event

val PORT = 8100 // GUI Port
val HELLO = "hello"
val GOODBYE = "goodbye"

// Event used to pass data to GUI
class EventDelivery(
        val event : String
) : Event()

