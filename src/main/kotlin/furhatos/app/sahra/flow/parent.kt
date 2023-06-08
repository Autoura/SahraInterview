package furhatos.app.sahra.flow

import furhatos.app.sahra.LISTENING_ENDED
import furhatos.app.sahra.LISTENING_STARTED
import furhatos.app.sahra.THINKING_ENDED
import furhatos.app.sahra.THINKING_STARTED
import furhatos.flow.kotlin.*
import java.awt.Color

val Parent: State = state {

    // --- Listening (So you know you can speak)

    onEvent(LISTENING_ENDED, instant = true) {
        furhat.ledStrip.solid(Color(0, 0, 0)) // Off
    }

    onEvent(LISTENING_STARTED, instant = true) {
        furhat.ledStrip.solid(Color.GREEN)
    }

    // --- Thinking (So you know not to speak)

    onEvent(THINKING_ENDED, instant = true) {
        furhat.ledStrip.solid(Color(0, 0, 0)) // Off
    }

    onEvent(THINKING_STARTED, instant = true) {
        furhat.ledStrip.solid(Color.RED)
    }

}