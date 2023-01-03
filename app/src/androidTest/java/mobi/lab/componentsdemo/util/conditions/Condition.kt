package com.thefitsphere.android.util.conditions

/**
 * [Condition] interface is used to implement conditions for generic idle resource
 * where they get collected in Queue and conditions are checked
 *
 * *[chec]
 *
 * *[getName]
 */
interface Condition {

    /**
     *  [check] function must contain logic for condition that needs to be met
     *  for resource to be idle and test can be continued
     *
     * * Belongs to [Condition] interface
     *
     *  @return Boolean - result of checking the condition
     */
    fun check(): Boolean

    /**
     * [getName] Contains the name of the condition, this name will be used
     * in logs when the condition will be checked
     *
     * * Belongs to [Condition] interface
     *
     *@return String - condition name
     */
    fun getName(): String
}
