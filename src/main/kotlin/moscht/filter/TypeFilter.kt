package moscht.filter

import hu.kszi2.boti.filter.MachineFilter
import moscht.Machine
import moscht.MachineType

class TypeFilter(private val type: MachineType) : MachineFilter {
    override fun accept(machine: Machine): Boolean =
        machine.type == type

    override fun reportChecked(thing: Any): Boolean =
        thing == type
}