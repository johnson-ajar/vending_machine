import * as Actions from '../actions/machine_action';
import {VendingMachineState} from '../../app/components/machine/vending_machine_component';
import {GET_MACHINES_FULFILLED, HAS_MACHINE_CHANGED, SELECT_MACHINE_FULFILLED, SUBMIT_PAYMENT_FULFILLED} from '../action_types/machine_action_type';
import { VendingMachine } from '../../model/vending_machine';
import { CoinRegistry } from '../../model/coin_registry';
import {coins, useCoinType} from '../../model/coin_type';
import { MachineBank } from '../../model/machine_bank';

let initialState:VendingMachineState = {
   machines:[],
   selectedMachine: new VendingMachine(),
   coinChange: new CoinRegistry(coins[useCoinType]),
   isMachineChanged:false
}
export function reducers (state:VendingMachineState = initialState, action:Actions.Action){
    console.log(action);
    let partialState:Partial<VendingMachineState> | undefined;
    switch(action.type){
        case GET_MACHINES_FULFILLED:
            console.log(action.payload);
            partialState = {
                ...state,
                machines:action.payload.machineState.machines,
                selectedMachine:action.payload.machineState.selectedMachine as VendingMachine
            }
        break;
        case SUBMIT_PAYMENT_FULFILLED:
            console.log("Submit Payment reducer.....");
            console.log(action.payload);
            let bank = new MachineBank();
            bank.setMachineRegistry(action.payload.machineRegistry);
            bank.setUserRegistry(action.payload.userRegistry);
            partialState = {
                ...state,
                coinChange: action.payload.coinChange,
                selectedMachine: action.payload.selectedMachine
            }
        break;
        case SELECT_MACHINE_FULFILLED:
            console.log(action.payload);
            partialState = {
                ...state,
                isMachineChanged: !state.isMachineChanged,
                selectedMachine:action.payload.selectedMachine as VendingMachine
            }
        break;
        case HAS_MACHINE_CHANGED:
            partialState = {
                ...state,
                isMachineChanged:action.payload.hasMachineChanged
            }
        break;
    }
    return (partialState != null )? {...state, ...partialState}: state;
} 