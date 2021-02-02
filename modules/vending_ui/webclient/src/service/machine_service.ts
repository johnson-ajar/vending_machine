import {VendingMachine} from '../model/vending_machine';
import {CoinRegistry} from '../model/coin_registry';
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
            let machine:VendingMachine = Object.assign(new VendingMachine(),d);
            machines.push(machine);
        });
        console.log(machines);
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
        console.log(response);
        const changeObject = await response.json();
        console.log(changeObject);
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
}