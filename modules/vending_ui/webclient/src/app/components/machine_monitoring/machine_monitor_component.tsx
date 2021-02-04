import * as React from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { actionCreators } from '../../../store/actions/action_index';
import {AppState, Dispatch} from '../../../store/store_index';
import {VendingMachineProps} from '../machine/vending_machine_component';
import {Table} from 'react-bootstrap';
import {VendingMachine} from '../../../model/vending_machine';
import { MachineBank } from '../../../model/machine_bank';
import {CoinRegistry} from '../../../model/coin_registry';
import {getCoinByCountry, useCoinType} from '../../../model/coin_type';
import UpdateRegistryComponent from '../machine/update_registry_component';

export interface MachineMonitorProps extends VendingMachineProps{
    
}


export class MachineMonitorComponent extends React.Component<MachineMonitorProps> {
    coins = getCoinByCountry(useCoinType);
    constructor(props:MachineMonitorProps){
        super(props);
        this.displayRegistryAmount = this.displayRegistryAmount.bind(this);
        this.displayRegistry = this.displayRegistry.bind(this);
    }

    private displayRegistry(machine:VendingMachine, registryType:string) {
        let bank:MachineBank = Object.assign(new MachineBank(), machine.getBank());
        
        let registryCoins:{[name:string]:number} ={};
        if(registryType === 'machine'){
            if(bank.getMachineRegistry() === undefined){
                return (<></>);
            }
            registryCoins = Object.assign(new CoinRegistry(), bank.getMachineRegistry()).getCoins();
        } else if (registryType == 'user'){
            if(bank.getUserRegistry() === undefined){
                return (<></>);
            }
            registryCoins = Object.assign(new CoinRegistry(), bank.getUserRegistry()).getCoins();
        }
        return(<td>
            {Object.keys(registryCoins).map((key)=>{
                return(<b style={{marginLeft:"10px"}} key={key}>
                    {this.coins[key].display}:
                    <span style={{color:"blue", fontSize:"25px"}}>{registryCoins[key]}</span>
                    </b>)
            })}
        </td>);
    }
    
    private displayRegistryAmount(machine: VendingMachine, registryType:string) {
        let bank:MachineBank = Object.assign(new MachineBank(), machine.getBank());
        if(bank.getUserRegistry() === undefined) {
            return (<></>);
        }
       
        let amount:number = 0.0;
        if(registryType === 'machine'){
            let registry:CoinRegistry = Object.assign(new CoinRegistry(), bank.getMachineRegistry());
            amount = registry.getAmount(useCoinType);
        } else if(registryType == 'user'){
            let registry:CoinRegistry = Object.assign(new CoinRegistry(), bank.getUserRegistry());
            amount = registry.getAmount(useCoinType);
        }
        return (
            <td>Registry Amount: {amount.toLocaleString(undefined, {maximumFractionDigits:2})}</td>
        );
    }
    
    private displayBankAmount(machine:VendingMachine) {
        let bank:MachineBank = machine.getBank() as MachineBank;
        return(<tr>
            <td>Total Amount:</td>
            <td>{bank.getAmount(useCoinType).toLocaleString(undefined, {maximumFractionDigits:2})}</td>
            </tr>
       );
       
    }
    public render() {
        return(<div>
            <h1>Machine Monitoring</h1>
            <Table striped bordered hover className="text-left">
                <tbody>
                    <tr>
                        <th>Machine Coin Registry</th>
                        <th>User Coin Registry</th>
                    </tr>
                    <tr>
                        {this.displayRegistry(this.props.machineState.selectedMachine, 'machine')}
                        {this.displayRegistry(this.props.machineState.selectedMachine, 'user')}
                    </tr>
                    <tr>
                        {this.displayRegistryAmount(this.props.machineState.selectedMachine, 'machine')}
                        {this.displayRegistryAmount(this.props.machineState.selectedMachine, 'user')}
                    </tr>
                    {this.displayBankAmount(this.props.machineState.selectedMachine)}
                    <UpdateRegistryComponent/>
                </tbody>
            </Table>
            </div>);
    }
}

const mapStateToProps = (appState:AppState)=>({
    machineState:appState.machineState
});

const mapDispatchToProps = (dispatch:Dispatch)=>bindActionCreators({
    getMachines: actionCreators.machine.getMachines,
    selectMachine: actionCreators.machine.selectMachine,
    submitPayment: actionCreators.machine.submitPayment,
    setMachineChanged: actionCreators.machine.setMachineChanged,
    updateMachineRegistry: actionCreators.machine.updateMachineRegistry,
    resetChangeRegistry: actionCreators.machine.resetChangeRegistry,
    emptyErrorMessages: actionCreators.machine.emptyErrorMessages
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(MachineMonitorComponent);