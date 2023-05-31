package furhatos.app.sahra.flow.main

import furhatos.app.sahra.ModeDelivery
import furhatos.app.sahra.QR_CODE_VERIFIED
import furhatos.app.sahra.flow.Parent
import furhatos.event.actions.ActionGaze
import furhatos.flow.kotlin.*

val Welcoming: State = state(Parent) {

    onUserEnter(instant = true) {
        if (!furhat.isAttendingUser()) {
            furhat.attend(it)
        }
    }

}

val Attending: State = state(Parent) {



}
