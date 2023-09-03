package hu.kszi2.boti.filter

import hu.kszi2.boti.Machine
import hu.kszi2.boti.MachineType

class TypeFilter(private val type: MachineType) : MachineFilter {
    override fun accept(machine: Machine): Boolean =
        machine.type == type

    override fun reportChecked(thing: Any): Boolean =
        thing == type
}