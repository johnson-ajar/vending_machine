import {AnyAction} from 'redux'
import {VendingMachineState} from '../../app/components/machine/vending_machine_component';
import { MachineService } from '../../service/machine_service';
import { MachineBank } from '../../model/machine_bank';
import { VendingMachine } from '../../model/vending_machine';
import {CoinRegistry} from '../../model/coin_registry';
import {coins, getCoinByCountry, useCoinType} from '../../model/coin_type';
import {GET_MACHINES,
       HAS_MACHINE_CHANGED,
        SELECT_MACHINE,
        SUBMIT_PAYMENT,
        EMPTY_MACHINE_ERRORS,
        RESET_CHANGE_REGISTRY,
        UPDATE_MACHINE_REGISTRY} from '../action_types/machine_action_type';



export type Action = AnyAction 
    |{type: typeof GET_MACHINES, payload: {machineState:VendingMachineState}}
    |{type: typeof SELECT_MACHINE, payload:{selectedMachine:VendingMachine}}
    |{type: typeof SUBMIT_PAYMENT, payload:{coinChange:CoinRegistry, isRegistryChanged:boolean, selectedMachine:VendingMachine, errors:Error[]}}
    |{type: typeof HAS_MACHINE_CHANGED, payload:{hasMachineChanged:boolean}}
    |{type: typeof UPDATE_MACHINE_REGISTRY, payload:{updatedMachine:VendingMachine}}
    |{type: typeof EMPTY_MACHINE_ERRORS, payload:{errors:Error[]}}
    |{type: typeof RESET_CHANGE_REGISTRY, payload:{changeRegistry:CoinRegistry}};

let service:MachineService = new MachineService();

export const actionCreators = {
    getMachines:()=>({
        type: GET_MACHINES,
        payload: async () => {
            const machineState:VendingMachineState = {
                machines: await service.getMachines(),
                selectedMachine:new VendingMachine(),
                coinChange: new CoinRegistry(coins[useCoinType]),
                isMachineChanged:false,
                errors:[]
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
            let coinChange:CoinRegistry = new CoinRegistry();
            let errors = [];
            try {
                coinChange= await service.submitPayment(machine_name, amount, payment);
            } catch(e) {
                coinChange = payment;
                errors.push(e);
            }
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
                selectedMachine: machine,
                errors:errors
                };
        }
    }),
    setMachineChanged:(changed:boolean) =>({
        type: HAS_MACHINE_CHANGED,
        payload: {hasRegistryChanged:changed}
    }),

    updateMachineRegistry:(machine_name:string, registry_type:string, registry:CoinRegistry)=>({
        type: UPDATE_MACHINE_REGISTRY,
        payload: async()=>{
            const machine:VendingMachine = await service.updateMachineRegistry(machine_name,registry_type,registry);
            return {updatedMachine: machine};
        }
    }),
    resetChangeRegistry:()=>({
        type: RESET_CHANGE_REGISTRY,
        payload: {coinChange: new CoinRegistry(getCoinByCountry(useCoinType))}
    }),
    emptyErrorMessages:()=>({
        type: EMPTY_MACHINE_ERRORS,
        payload: {errors:[]}
    })
};
