import * as React from 'react';

//import {VendingMachine} from '../../../model/vending_machine';
import {connect} from 'react-redux';
import {actionCreators} from '../../../store/actions/action_index';
import {bindActionCreators} from 'redux';
import {CoinRegistry} from '../../../model/coin_registry';
import {AppState, Dispatch} from '../../../store/store_index';
import { DropdownButton, Dropdown, InputGroup} from 'react-bootstrap';
import {coins, Coin, useCoinType} from '../../../model/coin_type';
import RangeSlider from 'react-bootstrap-range-slider';

interface UpdateRegistryState{
   selectedRegistry:CoinRegistry;
   registryType:string;
}

interface UpdateRegistryProps {
    machineName:string,
    userRegistry:CoinRegistry;
    machineRegistry:CoinRegistry;
    updateMachineRegistry:(macine_name:string, registry_type:string, registry:CoinRegistry)=>any;
}

export class UpdateRegistryComponent extends React.Component<UpdateRegistryProps, UpdateRegistryState>{
    state:UpdateRegistryState;
    constructor(props: UpdateRegistryProps) {
        super(props);
        this.setSelectRegistry = this.setSelectRegistry.bind(this);
        this.updateRegistry = this.updateRegistry.bind(this);
        this.updateCoin = this.updateCoin.bind(this);
        this.addRangeSlider = this.addRangeSlider.bind(this);
        this.submitUpdatedRegistry = this.submitUpdatedRegistry.bind(this);
        this.state = {
            registryType: 'machine',
            selectedRegistry: this.props.machineRegistry
        }
    }
   public componentDidMount() {
       this.setState({
           registryType: 'machine',
           selectedRegistry: this.props.machineRegistry
       })
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
    private setSelectRegistry(index:string , event:React.SyntheticEvent){
       this.setRegistryState(Number(index));
    }

    private registrySelection(){
        return (
        <div>
        Select Registry Before updating :  <DropdownButton style={{marginLeft:"20px"}} id="dropdown-basic-button" title={"Choose Registry"} onSelect={this.setSelectRegistry}>
            <Dropdown.Item key={1} eventKey="1" title="Machine">Machine</Dropdown.Item>
            <Dropdown.Item key={2} eventKey="2" title="User"> User</Dropdown.Item>
        </DropdownButton>
        </div>);
    };

    private addRangeSlider(coin:Coin, coinValue:number) {
       
        return(<RangeSlider  min={coin.min} max={coin.max} size="sm" tooltip='auto' value={coinValue}
        onChange={(e:any)=> this.updateCoin(e, coin)}></RangeSlider>);
    }

    private updateCoin(e:any, coin:Coin) {
        let coinCount = e.target.value;
        let registry:CoinRegistry = this.state.selectedRegistry;
        registry.setCoin(coin.name, coinCount);
       this.setState({
           ...this.state,
           selectedRegistry: registry
       });
    }

    private updateRegistry() {
        let registryCoins = this.state.registryType == 'user'? this.props.userRegistry.getCoins(): this.props.machineRegistry.getCoins();
        return(<td> Update Registry 
            {Object.keys(registryCoins).map((k)=>{
            return(
                <InputGroup key={k}>
                <InputGroup.Prepend key={k} className="mb-3">
                    <InputGroup.Text id="basic-addon1">{coins[useCoinType][k].display}</InputGroup.Text>
                    {this.addRangeSlider(coins[useCoinType][k], registryCoins[k])}
                </InputGroup.Prepend>
                </InputGroup>
           )
        })}  </td>);
    }

    private submitUpdatedRegistry(e:React.MouseEvent<HTMLElement, MouseEvent>) {
        console.log(this.state.selectedRegistry);
        let registry:CoinRegistry = this.state.registryType == 'machine'? this.props.machineRegistry : this.props.userRegistry;
       
        this.props.updateMachineRegistry(
            this.props.machineName,
            this.state.registryType,
            registry
        );
        this.setState({
            ...this.state,
            selectedRegistry: registry
        });
         
    }

    public render(){
        return(<tr>
            <td>
                {this.registrySelection()}
                <span style={{marginLeft:"20px"}}>{this.state.registryType}</span>
               {<div className="input-group mb-3">
                   <div className="input-group-prepend">
                       To Update Registry:
                       <button className="btn btn-outline-secondary" type="button" onClick={(e)=>this.submitUpdatedRegistry(e)}> Click Here....</button>
                   </div>
            </div>}
            </td>
            {this.updateRegistry()}
        </tr>)
    };
}

const mapStateToProps = (state:AppState)=>({
   machineName: state.machineState.selectedMachine.getName(),
   machineRegistry: state.machineState.selectedMachine.getBank().getMachineRegistry(),
   userRegistry: state.machineState.selectedMachine.getBank().getUserRegistry()
});

const mapDispatchToProps = (dispatch: Dispatch) => bindActionCreators({
    updateMachineRegistry: actionCreators.machine.updateMachineRegistry
},dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(UpdateRegistryComponent);