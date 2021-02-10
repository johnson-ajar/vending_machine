## Vending Machine Java API.
This module contain the following packages.

>>> `com.machine.vending.model`

>>> `com.machine.vending.model.common`

>>> `com.machine.vending.model.entity`

>>> `com.machine.vending.model.main`

>>> `com.machine.vending.utils`

### Package com.machine.vending.model
>> This package contain the abstract classes for the different components of a vending machine which includes

>> Coin registry

>>> This holds the different coin type used by the machine and number of coins available for each type

>> Bank

>>>	This holds two registry a machine registry and user registry. Machine registry is used to keep track of the coins used to provide the change. This is initialised with a default amount of £100 in different denomination. 

>> Machine

>>> The state of the machine depends on two components, the Bank (responsible for managing the money) and Inventory (responsible for managing products). In this implementation the machine product inventory state is not considered for monitoring. This can be extended to provide functionality to monitor the inventory component.

>> Monitor

>>> This component can be considered as the java api that provides the functionality to alter the machine state and also provide the information of the state. This java api is used by the REST api to manage and monitor a vending machine.