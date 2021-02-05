import * as React from 'react';
import {AppState, Dispatch} from '../../../store/store_index';
import {actionCreators} from '../../../store/actions/action_index';
import { VendingMachine } from '../../../model/vending_machine';
import {CoinRegistry} from '../../../model/coin_registry';
import { bindActionCreators } from 'redux';
import {connect} from 'react-redux';
import SelectMachineComponent from './select_machine_component';
import {Table,Button, Alert} from 'react-bootstrap';
import {coins, useCoinType, getCoinByCountry} from '../../../model/coin_type';


export interface VendingMachineState {
    machines: VendingMachine[];
    selectedMachine:VendingMachine;
    coinChange:CoinRegistry;
    isMachineChanged: boolean;
    errors:Error[];
}

export type VendingMachineProps = {
    machineState:VendingMachineState;
    getMachines: ()=>any;
    selectMachine:(machine:VendingMachine)=>any;
    submitPayment:(machine_name:string, amount:number, payment:CoinRegistry)=>any;
    setMachineChanged:(changed:boolean)=>any;
    resetChangeRegistry:()=>any;
    emptyErrorMessages:()=>any;
}

interface MachineRegistryState extends VendingMachineState {
   paymentRegistry:CoinRegistry,
   purchaseAmount:number,
   hasBalance:boolean,
   showError:boolean
}

export class VendingMachineComponent extends React.Component<VendingMachineProps, MachineRegistryState> {
    state:MachineRegistryState;
    public constructor(props:VendingMachineProps) {
        super(props);
        this.state = {
            machines:[],
            coinChange: new CoinRegistry(coins[useCoinType]),
            paymentRegistry:new CoinRegistry(coins[useCoinType]),
            selectedMachine: new VendingMachine(),
            purchaseAmount: 0.0,
            hasBalance: false,
            isMachineChanged: false,
            errors:[],
            showError:true
        };
        this.getMachines = this.getMachines.bind(this);
        this.displaySelectedMachineName = this.displaySelectedMachineName.bind(this);
        this.incrementPayment = this.incrementPayment.bind(this);
        this.makePurchase = this.makePurchase.bind(this);
        this.calculatePurchaseAmount = this.calculatePurchaseAmount.bind(this);
        this.submitPayment = this.submitPayment.bind(this);
        this.calculateBalance = this.calculateBalance.bind(this);
        this.displaySelectedMachine = this.displaySelectedMachine.bind(this);
        this.displayMachineSelectionMenu = this.displayMachineSelectionMenu.bind(this);
        this.collectChange = this.collectChange.bind(this);
        this.closeErrorMessage = this.closeErrorMessage.bind(this);
    }
    public componentDidUpdate() {
       
    }
    private getMachines(){
        this.props.getMachines();
       return null; 
    }

    private displaySelectedMachineName() {
        return(<tr>
                <td>Selected Machine: </td>
                <td>
                {this.props !== null ? <h3>{this.props.machineState.selectedMachine.getName()}</h3>:""}
                </td>
           </tr>);
    }

    private displayMachineSelectionMenu() {
        return (
            <tr>
                    <td>Select Machine Name : </td>
                    <td>
                       <SelectMachineComponent/>
                    </td>
            </tr>
        );
    }
    
    private calculatePurchaseAmount(e:React.MouseEvent<HTMLElement, MouseEvent>) {
        let min:number = 2;
        let max:number = 10;
        let value = Math.random()*(max-min);
        this.props.resetChangeRegistry();
        this.setState({
            ...this.state,
            purchaseAmount: value,
            coinChange: new CoinRegistry(getCoinByCountry(useCoinType)),
            paymentRegistry: new CoinRegistry(getCoinByCountry(useCoinType)),
            hasBalance: false,
        });
    }

    private makePurchase() {
        let purchaseValue:number = this.state !== null ? this.state.purchaseAmount : 0.0;
        return (
            <tr>
                <td>Amount Purchased</td>
                <td>
                    <Button  onClick={e => this.calculatePurchaseAmount(e)}>Make Purchase</Button>
                    <span style={{fontSize:"30px", marginLeft:"30px"}}>£{purchaseValue.toLocaleString(undefined, {maximumFractionDigits:2})}</span>
                </td>
            </tr>
        );
    }

    private incrementPayment(e:React.MouseEvent<HTMLButtonElement, MouseEvent>){
        let tmpRegistry:CoinRegistry = this.state.paymentRegistry;
        tmpRegistry.setCoin(e.currentTarget.name,tmpRegistry.getNoCoin(e.currentTarget.name)+1);
        this.setState({
            ...this.state,
            paymentRegistry:tmpRegistry,
            hasBalance: this.hasBalance()
        });
        
    }

    private submitPayment(e:React.MouseEvent<HTMLButtonElement, MouseEvent>){
        let hasBalance = this.hasBalance();
        this.setState({
            ...this.state,
            hasBalance: hasBalance
        });
       if(hasBalance) {
           //If the payment is right call payment function of the service.
           //Make sure the machine state is reset.
           this.props.submitPayment(this.props.machineState.selectedMachine.getName(), this.state.purchaseAmount, this.state.paymentRegistry);
           this.setState({
               ...this.state,
               purchaseAmount:0.0,
               paymentRegistry: new CoinRegistry(getCoinByCountry(useCoinType)),
               coinChange: new CoinRegistry(getCoinByCountry(useCoinType)), //this.props.machineState.coinChange,
               hasBalance:false,
               errors: []
           });
       } 
    }  

    private calculateBalance():number {
        if(this.state === null){
            return 0.0;
        }
        let purchase: number = this.state.purchaseAmount;
        let payment: number = this.state.paymentRegistry.getAmount(useCoinType);
        let balance: number = payment-purchase;
        return parseFloat(balance.toFixed(3));
    }

    private hasBalance():boolean {
        let balance:number = this.calculateBalance();
        //Check for zero balance
        if(parseFloat(Math.abs(balance).toFixed(2))==0.00){
            return true;
        }
        if(balance >0.00){
            return true;
        }
        return false;
    }

    private noCoins(name: string) {
            if(this.state === null){
                return 0;
            }
            return this.state.paymentRegistry.getNoCoin(name);
    }
    private paymentAmount() {
        if(this.state === null) {
            return 0.0;
        }
        return this.state.paymentRegistry.getAmount(useCoinType);
    }
    private makePayment(country:string) {
        let coins = getCoinByCountry(country);
        let disableIncrementPayment:boolean = this.state !== null && this.state.purchaseAmount > 0.0  ? false : true;
        let balance:number = this.calculateBalance();
        let disablePaymentSubmit:boolean = !this.hasBalance();//balance < 0 ? true: false;
       
        return(
            <tr>
                <td>Make Payment (Press Coin)</td>
                <td>
                    <div>
                    {Object.keys(coins).map(key=>{
                        let coin_name = coins[key].name;
                        let coin_display = coins[key].display;
                        return(<Button 
                            style={{marginRight:"10px"}} 
                            name={coin_name} 
                            key={coin_name} 
                            disabled={disableIncrementPayment}
                            onClick={this.incrementPayment}>{coin_display+" : "+this.noCoins(coin_name)}
                        </Button>);
                    })}
                    </div>
                    <div>
                    <span style={{marginTop:"40px"}}>Provided Payment Amount: <b>£{this.paymentAmount()}</b>
                        <div>
                            {<b style={{color:(this.hasBalance()?"green":"red"), marginRight:"70px" }}>
                                Balance : £{(this.hasBalance()?Math.abs(balance):balance).toLocaleString(undefined, {maximumFractionDigits:2})}
                                </b>}
                        </div>
                    </span>
                    <Button 
                    style={{marginTop:"20px"}}
                    disabled={disablePaymentSubmit}
                     onClick={this.submitPayment}>
                         Submit Payment
                    </Button>
                    
                    </div>
                   
                </td>
            </tr>
        );
    }

    private collectChange() {
        
        let changeCoins = this.props.machineState.coinChange.getCoins();
        let cCoins = getCoinByCountry(useCoinType);
        return(
            <tr>
                <td>Collect Change : £{this.props.machineState.coinChange.getAmount(useCoinType)}</td>
                
                <td>
                {Object.keys(changeCoins).map(key=>{
                        return(<b key={key}>
                            {cCoins[key].display} :
                            <span style={{color:"blue", fontSize:"25px"}}>{changeCoins[key]} </span> 
                            </b>);
                    })}
                </td>
            </tr>
        );
    }

    private displaySelectedMachine() {
        let isMachineSelected = this.state !== null && this.props.machineState.selectedMachine.getName() !== "" ? true: false;
        if(isMachineSelected){
            return(
                <>
                    {this.displaySelectedMachineName()}
                    {this.makePurchase()}
                    {this.makePayment(useCoinType)}
                    {this.collectChange()}
                </>
            );
        }
        return(<></>)
    }

    private closeErrorMessage(e:any) {
       this.props.emptyErrorMessages();
        this.setState({
            ...this.state,
           showError:false
        });
    }

    private popErrors() {
        if(!this.state.showError){
            return(<></>)
        }
        return this.props.machineState.errors.map((e:Error, index:number)=>{
            return(<Alert key={index} variant="danger" onClose={(e)=>{this.closeErrorMessage(e)}} dismissible>
                <Alert.Heading>Error</Alert.Heading>
                <p>{e.message}</p>
            </Alert>)
         });
    }

    public render() {
        return(
            <div>
                {this.popErrors()}
                <Table striped bordered hover className="text-left">
                    <tbody>
                    <tr>
                        <th>Load Vending Machine : </th>
                        <th><button onClick = {()=>this.getMachines()}>Load Machines</button></th>
                    </tr>
                    {this.displayMachineSelectionMenu()}
                    {this.displaySelectedMachine()}
                    </tbody>
                </Table>
            </div>
        );
    }
}

//assign only state type.
const mapStateToProps = (appState: AppState) => ({
    machineState: appState.machineState,
});

const mapDispatchToProps = (dispatch:Dispatch) => bindActionCreators({
    getMachines: actionCreators.machine.getMachines,
    selectMachine: actionCreators.machine.selectMachine,
    submitPayment: actionCreators.machine.submitPayment,
    setMachineChanged: actionCreators.machine.setMachineChanged,
    updateMachineRegistry: actionCreators.machine.updateMachineRegistry,
    resetChangeRegistry: actionCreators.machine.resetChangeRegistry,
    emptyErrorMessages: actionCreators.machine.emptyErrorMessages
}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(VendingMachineComponent);