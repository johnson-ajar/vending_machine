import {AnyAction} from 'redux'
import {GET_MACHINES, HAS_MACHINE_CHANGED, SELECT_MACHINE, SUBMIT_PAYMENT} from '../action_types/machine_action_type';
import {VendingMachineState} from '../../app/components/machine/vending_machine_component';
import { MachineService } from '../../service/machine_service';
import { MachineBank } from '../../model/machine_bank';
import { VendingMachine } from '../../model/vending_machine';
import {CoinRegistry} from '../../model/coin_registry';
import {coins, useCoinType} from '../../model/coin_type';


export type Action = AnyAction 
    |{type: typeof GET_MACHINES, payload: {machineState:VendingMachineState}}
    |{type: typeof SELECT_MACHINE, payload:{selectedMachine:VendingMachine}}
    |{type: typeof SUBMIT_PAYMENT, payload:{coinChange:CoinRegistry, selectedMachine:VendingMachine}}
    |{type: typeof HAS_MACHINE_CHANGED, payload:{hasMachineChanged:boolean}};

let service:MachineService = new MachineService();

export const actionCreators = {
    getMachines:()=>({
        type: GET_MACHINES,
        payload: async () => {
            const machineState:VendingMachineState = {
                machines: await service.getMachines(),
                selectedMachine:new VendingMachine(),
                coinChange: new CoinRegistry(coins[useCoinType]),
                isMachineChanged:false
            };
            return {machineState: machineState};
        }
    }),
    selectMachine:(machine:VendingMachine)=>({
        type: SELECT_MACHINE,
        payload: async () => {
            const machineRegistry:CoinRegistry = await service.getMachineRegistry(machine.getName());
            const userRegistry:CoinRegistry = await service.getMachineUsersRegistry(machine.getName());
            const bank:MachineBank = new MachineBank();
            bank.setMachineRegistry(machineRegistry);
            bank.setUserRegistry(userRegistry);
            machine.setBank(bank);
            return {selectedMachine:machine}
        }
    }),
    submitPayment:(machine_name:string, amount:number, payment:CoinRegistry)=>({
        type: SUBMIT_PAYMENT,
        payload: async ()=> {
            const coinChange:CoinRegistry = await service.submitPayment(machine_name, amount, payment);
            const machineRegistry:CoinRegistry = await service.getMachineRegistry(machine_name);
            const userRegistry:CoinRegistry = await service.getMachineUsersRegistry(machine_name);
            const machine:VendingMachine = new VendingMachine();
            const bank:MachineBank = new MachineBank();
            bank.setMachineRegistry(machineRegistry);
            bank.setUserRegistry(userRegistry);
            machine.setBank(bank);
            machine.setName(machine_name);
           
            return {
                coinChange:coinChange,
                selectedMachine: machine
                };
        }
    }),
    setMachineChanged:(changed:boolean) =>({
        type: HAS_MACHINE_CHANGED,
        payload: {hasRegistryChanged:changed}
    })
};
