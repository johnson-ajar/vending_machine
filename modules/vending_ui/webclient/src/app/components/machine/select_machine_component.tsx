import * as React from 'react';

import {VendingMachine} from '../../../model/vending_machine';
import {VendingMachineProps} from './vending_machine_component';
import {AppState, Dispatch} from '../../../store/store_index';
import {actionCreators} from '../../../store/actions/action_index';
import {bindActionCreators} from 'redux';
import { connect } from 'react-redux';
import {Dropdown, DropdownButton} from 'react-bootstrap';

interface SelectedMachineState {
    selectedMachine:VendingMachine;    
}

export class SelectMachineComponent extends React.Component<VendingMachineProps, SelectedMachineState>{
    
    constructor(props:VendingMachineProps) {
        super(props);
        this.setSelectedMachine = this.setSelectedMachine.bind(this);
    }

    private setSelectedMachine(index:string | null, event:React.SyntheticEvent) {
        this.setState(
            {
                selectedMachine:this.props.machineState.machines[Number(index)]
            }
        );
        this.props.selectMachine(this.props.machineState.machines[Number(index)]);
        this.props.setMachineChanged(!this.props.machineState.isMachineChanged);
    }

    private machineMenuItems() {
        return(<>
                 {this.props.machineState.machines.map((m, index:number)=>{
                        return <Dropdown.Item key={index} eventKey={index.toString()} title={m.getName()}>{m.getName()}</Dropdown.Item>
                    })}
        </>);
    }

    public render(){
        return(
            <div>
                <DropdownButton id="dropdown-basic-button" title={"Machines"} onSelect={this.setSelectedMachine}>
                   {this.machineMenuItems()}
                </DropdownButton>
            </div>
        );
    } 
}

const mapStateToProps = (state:AppState)=>({
    machineState: state.machineState
});

const mapDispatchToProps = (dispatch: Dispatch) => bindActionCreators({
    getMachines:actionCreators.machine.getMachines,
    selectMachine:actionCreators.machine.selectMachine,
    submitPayment:actionCreators.machine.submitPayment,
    setMachineChanged: actionCreators.machine.setMachineChanged,
    updateMachineRegistry: actionCreators.machine.updateMachineRegistry,
    resetChangeRegistry: actionCreators.machine.resetChangeRegistry,
    emptyErrorMessages: actionCreators.machine.emptyErrorMessages
},dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(SelectMachineComponent);