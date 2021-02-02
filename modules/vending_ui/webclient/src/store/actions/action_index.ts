import {Action as MachineAction, actionCreators as MachineActions} from '../actions/machine_action';
//import {AnyAction} from 'redux'
export const actionCreators = {
    machine: MachineActions
}
export type Action = | MachineAction;