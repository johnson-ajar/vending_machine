import {VendingMachine} from '../model/vending_machine';
import {CoinRegistry} from '../model/coin_registry';
import { MachineBank } from '../model/machine_bank';
export class MachineService {
    machineUrl:string = "";
    headers:Headers = new Headers();
    constructor(url:string = 'http://localhost:8080/api/v1/vending'){
        this.machineUrl = url;
        this.headers.append("Content-Type", 'application/json');
        this.headers.append("Accept", 'application/json');
        this.headers.append("Origin", 'http://localhost:8081');
    }

    public async getMachines():Promise<VendingMachine[]>{
        const response:Response = await fetch(this.machineUrl+"/machine/all",{
            mode:'cors',
            method: 'GET',
            headers: this.headers
        });
        const data = await response.json();
        let machines:VendingMachine[] = [];
        data.forEach((d:{bank:{machineRegistry:{}, userRegistry:{}}},i:number) =>{
            //let machine:VendingMachine = Object.assign(new VendingMachine(),d);
            let machine:VendingMachine = this.createVendingMachine(d);
            machines.push(machine);
        });
        return machines;
    }


    public async submitPayment(machine_name:string, amount:number, payment:CoinRegistry):Promise<CoinRegistry> {
        let amountstr = amount.toLocaleString(undefined, {maximumFractionDigits:2})
        let paymentUrl:string = `${this.machineUrl}/machine/${machine_name}/payment?amount=${amountstr}`;
       
        const response:Response = await fetch(paymentUrl, {
            mode:'cors',
            method: 'PUT',
            headers: this.headers,
            body: JSON.stringify(payment)
        });
        if(response.status === 404) {
            throw Error("The machine cannot be found");
        } else if(response.status == 400){
            throw Error("The parameter passed is invalid or payment amount is insufficient or not enough funds in machine registry.");
        }
        const changeObject = await response.json();
        const changePayment = Object.assign(new CoinRegistry(), changeObject);
        return changePayment;
    }


    public async getMachineRegistry(machine_name:string) : Promise<CoinRegistry> {
        let registryUrl:string = `${this.machineUrl}/machine/${machine_name}/coins`;
        const response:Response = await fetch(registryUrl, {
            mode: 'cors',
            method: 'GET',
            headers: this.headers
        });
        const registryObj = await response.json();
        const registry:CoinRegistry = Object.assign(new CoinRegistry(),registryObj);
        return registry;
    }

    public async getMachineUsersRegistry(machine_name:string) : Promise<CoinRegistry> {
        let registryUrl: string = `${this.machineUrl}/machine/${machine_name}/user/coins`;
        const response:Response = await fetch(registryUrl, {
            mode: 'cors',
            method: 'GET',
            headers: this.headers
        });

        const registryObj = await response.json();
        const registry:CoinRegistry = Object.assign(new CoinRegistry(), registryObj);
        return registry;
    }

    public async updateMachineRegistry(machine_name:string, registry_type:string, updatedRegistry:CoinRegistry):Promise<VendingMachine> {
        let registryUrl: string = `${this.machineUrl}/machine/${machine_name}/registry?type=${registry_type}`;
        const response:Response = await fetch(registryUrl, {
            mode: 'cors',
            method: 'PUT',
            headers: this.headers,
            body: JSON.stringify(updatedRegistry)
        });
        const machineObj = await response.json();
        
        return this.createVendingMachine(machineObj);
    }

    private createVendingMachine(machineObj:any) {
        const machine:VendingMachine = new VendingMachine();
        const bank:MachineBank = new MachineBank();
        
        const machineRegistry:CoinRegistry = Object.assign(new CoinRegistry, machineObj.bank.machineRegistry);
        const userRegistry:CoinRegistry = Object.assign(new CoinRegistry, machineObj.bank.userRegistry);
        bank.setMachineRegistry(machineRegistry);
        bank.setUserRegistry(userRegistry);
        machine.setName(machineObj.name);
        machine.setBank(bank);
        return machine;
    }
}