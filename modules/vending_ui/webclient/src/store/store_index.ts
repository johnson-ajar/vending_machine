import {Action} from './actions/action_index';
import {reducers as MachineReducer} from './reducers/machine_reducer';

import {VendingMachineState} from '../app/components/machine/vending_machine_component';
import thunk from 'redux-thunk';
import { Dispatch as ReduxDispatch, compose, applyMiddleware, combineReducers, Store, createStore } from 'redux';
import promiseMiddleware from 'redux-promise-middleware';


export interface AppState {
    machineState: VendingMachineState
}

const rootReducers = combineReducers<AppState>({
    machineState: MachineReducer
});

/*
//Creating the root reducers.
const rootReducer = (state:AppState, action:Action) => {
    if(action.type === 'RESET_ALL') {
        console.log("Resetting all state TODO: reset properly");
    }
    return appReducers(state, action);
}
*/

export type Dispatch = ReduxDispatch<Action>;
const composeEnhancers = window['__REDUX_DEVTOOLS_EXTENSION_COMPOSE__']||compose;

export const store:Store = createStore(
    rootReducers,
    composeEnhancers(applyMiddleware(promiseMiddleware(), thunk))
);





