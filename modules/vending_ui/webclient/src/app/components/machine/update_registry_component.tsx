import * as React from 'react';

//import {VendingMachine} from '../../../model/vending_machine';
import {connect} from 'react-redux';
import {actionCreators} from '../../../store/actions/action_index';
import {bindActionCreators} from 'redux';
import {CoinRegistry} from '../../../model/coin_registry';
import {AppState, Dispatch} from '../../../store/store_index';
import { InputGroup} from 'react-bootstrap';
import {coins, Coin, useCoinType} from '../../../model/coin_type';
import RangeSlider from 'react-bootstrap-range-slider';

interface UpdateRegistryState{
   selectedRegistry:CoinRegistry;
   registryType:string;
   isRegistryChanged: boolean;
}

interface UpdateRegistryProps {
    machineName:string;
    userRegistry:CoinRegistry;
    machineRegistry:CoinRegistry;
    updateMachineRegistry:(macine_name:string, registry_type:string, registry:CoinRegistry)=>any;
}

export class UpdateRegistryComponent extends React.Component<UpdateRegistryProps, UpdateRegistryState>{
    state:UpdateRegistryState;
    constructor(props: UpdateRegistryProps) {
        super(props);
        this.setSelectRegistry = this.setSelectRegistry.bind(this);
        this.updateCoinRegistry = this.updateCoinRegistry.bind(this);
        this.addRangeSlider = this.addRangeSlider.bind(this);
        this.updateCoin = this.updateCoin.bind(this);
        this.submitUpdatedRegistry = this.submitUpdatedRegistry.bind(this);
        this.resetRegistry= this.resetRegistry.bind(this);
        this.state = {
            registryType: 'machine',
            selectedRegistry: new CoinRegistry(),
            isRegistryChanged: false
        }
    }
    
    private setRegistryState(index:number) {
        this.setState({
            ...this.state,
            selectedRegistry: Number(index) == 2 ?
                 this.props.userRegistry :
                this.props.machineRegistry,
            registryType: (Number(index) == 2 ? 'user': 'machine')
        });
    }
    private setSelectRegistry(event:React.SyntheticEvent<HTMLSelectElement,Event>){
       this.setRegistryState(Number(event.currentTarget.value));
    }
    
    private registrySelection() {
        return(
            <div className="input-group mb-3">
                <div className="input-group-prepend">
                    <label className="input-group-text" htmlFor="registrySelection"> Select Registry Before Updating: </label>
                </div>
                <select className="custom-select" defaultValue="1" id="registrySelection" onChange={(e)=>this.setSelectRegistry(e)}>
                    <option value="1">Machine</option>
                    <option value="2">User</option>
                </select>
            </div>
        );
    }
    private addRangeSlider(coin:Coin, coinValue:number) {
        return(<RangeSlider  min={coin.min} max={coin.max} size="sm" tooltip='auto' value={coinValue}
        onChange={(e:any)=> this.updateCoin(e, coin)}></RangeSlider>);
    }

    private updateCoin(e:any, coin:Coin) {
        let coinCount = e.target.value;
        //If registry is not changed use the registry from properties
        //If the registry is changed used it from state.
       let registry:CoinRegistry = this.getCoinRegistryToUpdate(); 
       registry.setCoin(coin.name, coinCount);
       this.setState({
           ...this.state,
           selectedRegistry: registry,
           isRegistryChanged: true
       });
       
    }

    private getCoinRegistryToUpdate() {
        let registry:CoinRegistry = !this.state.isRegistryChanged ? 
            (this.state.registryType === 'user'? this.props.userRegistry:this.props.machineRegistry)
            : this.state.selectedRegistry;
          
        let nRegistry:CoinRegistry = new CoinRegistry();
        Object.keys(registry.getCoins()).forEach((k)=>{
            nRegistry.setCoin(k, registry.getNoCoin(k));
        });
        return nRegistry;
    }

    private updateCoinRegistry() {
        //If registry is not changed use the this.props.
        //If registry is changed use the state.
        let registryCoins = this.getCoinRegistryToUpdate().getCoins();
        return(<td>
            {this.refreshRegistry()}
            Update Registry 
            <InputGroup></InputGroup>
            {Object.keys(registryCoins).map((k)=>{
            return(
                <InputGroup key={k}>
               
                <InputGroup.Prepend key={k} className="mb-3">
                    <InputGroup.Text id="basic-addon1">{coins[useCoinType][k].display}</InputGroup.Text>
                    {this.addRangeSlider(coins[useCoinType][k], registryCoins[k])}
                    <span style={{marginLeft:"30px"}}>£{(registryCoins[k]*coins[useCoinType][k].value/100.0).toFixed(2)}</span>
                </InputGroup.Prepend>
                </InputGroup>
           )
        })}  </td>);
    }

    private submitUpdatedRegistry(e:React.MouseEvent<HTMLElement, MouseEvent>) {
       let registry:CoinRegistry = this.getCoinRegistryToUpdate();
        this.props.updateMachineRegistry(
            this.props.machineName,
            this.state.registryType,
            registry
        );
        this.setState({
            ...this.state,
            selectedRegistry: registry,
            isRegistryChanged: false
        });
         
    }


    private resetRegistry(e:any) { 
       let registry:CoinRegistry =  this.state.registryType == 'user'? this.props.userRegistry : this.props.machineRegistry;
       let nRegistry:CoinRegistry = new CoinRegistry();
        Object.keys(registry.getCoins()).forEach((k)=>{
            nRegistry.setCoin(k, registry.getNoCoin(k));
        });
       this.setState({
           ...this.state,
           selectedRegistry:  nRegistry,
           isRegistryChanged: false
       });
    }

    private refreshRegistry() {
        return(<div className="input-group mb-3">
            <div className="input-group-prepend">
                    <label style={{marginRight:"100px"}} className="input-group-text" htmlFor="updateRegistry">Refresh Registry: </label>
            </div>
            <button style={{marginLeft:"100px"}}id="updateRegistry" className="btn btn-outline-secondary" type="button" onClick={(e)=>this.resetRegistry(e)}> Click Here....</button>
        </div>);
    }
    

    private updateRegistry() {
        return(<div className="input-group mb-3">
            <div className="input-group-prepend">
                    <label style={{marginRight:"100px"}} className="input-group-text" htmlFor="updateRegistry">To Update Registry: </label>
            </div>
            <button style={{marginLeft:"100px"}}id="updateRegistry" className="btn btn-outline-secondary" type="button" onClick={(e)=>this.submitUpdatedRegistry(e)}> Click Here....</button>
        </div>);
    }

    public render(){
        return(<tr>
            <td>
                {this.registrySelection()}
                <span style={{marginLeft:"20px"}}>{this.state.registryType}</span>
                {this.updateRegistry()}
            </td>
            {this.updateCoinRegistry()}
        </tr>)
    };
}

const mapStateToProps = (state:AppState)=>({
   machine: state.machineState.selectedMachine,
   machineName: state.machineState.selectedMachine.getName(),
   machineRegistry: state.machineState.selectedMachine.getBank().getMachineRegistry(),
   userRegistry: state.machineState.selectedMachine.getBank().getUserRegistry(),
   isMachineChanged: state.machineState.isMachineChanged
});

const mapDispatchToProps = (dispatch: Dispatch) => bindActionCreators({
    updateMachineRegistry: actionCreators.machine.updateMachineRegistry
},dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(UpdateRegistryComponent);