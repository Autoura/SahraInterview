package furhatos.app.sahra

import furhatos.app.sahra.flow.NoGUI
import furhatos.skills.Skill
import furhatos.flow.kotlin.*

class InterviewSkill : Skill() {
    override fun start() {
        Flow().run(NoGUI)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}
