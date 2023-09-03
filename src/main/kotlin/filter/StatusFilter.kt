package hu.kszi2.boti.filter

import hu.kszi2.boti.Machine
import hu.kszi2.boti.MachineStatus

class StatusFilter(private val stat: MachineStatus) : MachineFilter {
    override fun accept(machine: Machine): Boolean = machine.status == stat
    override fun reportChecked(thing: Any): Boolean {
        return thing == stat.status
    }
}
