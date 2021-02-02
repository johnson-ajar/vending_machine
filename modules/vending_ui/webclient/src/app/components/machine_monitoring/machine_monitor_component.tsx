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

export interface MachineMonitorProps extends VendingMachineProps{
    
}

//const coinStyle = 
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
        if(registryType === 'Machine'){
            if(bank.getMachineRegistry() === undefined){
                return (<></>);
            }
            registryCoins = Object.assign(new CoinRegistry(), bank.getMachineRegistry()).getCoins();
        } else if (registryType == 'User'){
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
        if(registryType === 'Machine'){
            let registry:CoinRegistry = Object.assign(new CoinRegistry(), bank.getMachineRegistry());
            amount = registry.getAmount(useCoinType);
        } else if(registryType == 'User'){
            let registry:CoinRegistry = Object.assign(new CoinRegistry(), bank.getUserRegistry());
            amount = registry.getAmount(useCoinType);
        }
        return (
            <td>Registry Amount: {amount.toLocaleString(undefined, {maximumFractionDigits:2})}</td>
        );
    }
    
    private displayBankAmount(machine:VendingMachine) {
        let bank:MachineBank = Object.assign(new MachineBank(), machine.getBank());
        return(<tr>
            <td>Total Amount:</td>
            <td>{bank.getAmount(useCoinType).toLocaleString(undefined, {maximumFractionDigits:2})}</td>
            </tr>
       );
       
    }
    public render() {
        return(<div>
            <h1>Machine Monitoring</h1>
            <Table striped bordered hover>
                <tbody>
                    <tr>
                        <th>Machine Coin Registry</th>
                        <th>User Coin Registry</th>
                    </tr>
                    <tr>
                        {this.displayRegistry(this.props.machineState.selectedMachine, 'Machine')}
                        {this.displayRegistry(this.props.machineState.selectedMachine, 'User')}
                    </tr>
                    <tr>
                        {this.displayRegistryAmount(this.props.machineState.selectedMachine, 'Machine')}
                        {this.displayRegistryAmount(this.props.machineState.selectedMachine, 'User')}
                    </tr>
                    {this.displayBankAmount(this.props.machineState.selectedMachine)}
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
    setMachineChanged: actionCreators.machine.setMachineChanged
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(MachineMonitorComponent);