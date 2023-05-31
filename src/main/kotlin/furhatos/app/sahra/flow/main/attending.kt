package furhatos.app.sahra.flow.main

import furhatos.app.sahra.EventDelivery
import furhatos.app.sahra.HELLO
import furhatos.app.sahra.GOODBYE
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

    onEvent(HELLO) {
        furhat.say("Hello there, I have been looking forward to this conversation for a while")
    }

    onEvent(GOODBYE) {
        furhat.say("Thank you for chatting about AI with me today")
    }

}
