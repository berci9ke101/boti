package moscht.filter

import hu.kszi2.boti.filter.MachineFilter
import moscht.Machine
import moscht.MachineStatus

class StatusFilter(private val stat: MachineStatus) : MachineFilter {
    override fun accept(machine: Machine): Boolean = machine.status == stat
    override fun reportChecked(thing: Any): Boolean {
        return thing == stat.status
    }
}
