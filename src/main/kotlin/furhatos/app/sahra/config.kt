package furhatos.app.sahra

import furhatos.event.Event

val PORT = 8100 // GUI Port
val QR_CODE_VERIFIED = "QR_code_verified"

// Event used to pass data to GUI
class ModeDelivery(
        val display_mode : String
) : Event()

class KeepAliveDelivery(
        val dateAlive : String
) : Event()


