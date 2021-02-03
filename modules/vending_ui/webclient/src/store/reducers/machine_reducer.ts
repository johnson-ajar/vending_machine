import * as Actions from '../actions/machine_action';
import {VendingMachineState} from '../../app/components/machine/vending_machine_component';
import { VendingMachine } from '../../model/vending_machine';
import { CoinRegistry } from '../../model/coin_registry';
import {coins, getCoinByCountry, useCoinType} from '../../model/coin_type';
import {EMPTY_MACHINE_ERRORS, GET_MACHINES_FULFILLED, HAS_MACHINE_CHANGED,
    SELECT_MACHINE_FULFILLED,
    RESET_CHANGE_REGISTRY,
    SUBMIT_PAYMENT_FULFILLED,
    SUBMIT_PAYMENT_REJECTED,
    UPDATE_MACHINE_REGISTRY_FULFILLED} from '../action_types/machine_action_type';


let initialState:VendingMachineState = {
   machines:[],
   selectedMachine: new VendingMachine(),
   coinChange: new CoinRegistry(coins[useCoinType]),
   isMachineChanged:false,
   errors:[]
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
            partialState = {
                ...state,
                coinChange: action.payload.coinChange,
                selectedMachine: action.payload.selectedMachine,
                errors: action.payload.errors
            }
        break;
        case SUBMIT_PAYMENT_REJECTED:
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
        case UPDATE_MACHINE_REGISTRY_FULFILLED:
            console.log(action.payload)
            partialState = {
                ...state,
                selectedMachine:action.payload.updatedMachine as VendingMachine
            }
        break;
        case HAS_MACHINE_CHANGED:
            partialState = {
                ...state,
                isMachineChanged:action.payload.hasMachineChanged
            }
        break;
        case RESET_CHANGE_REGISTRY:
            partialState = {
                ...state,
                coinChange:new CoinRegistry(getCoinByCountry(useCoinType))
            }
        break;
        case EMPTY_MACHINE_ERRORS:
            partialState = {
                ...state,
                errors:action.payload.error
            }
        break;
    }
    return (partialState != null )? {...state, ...partialState}: state;
} 