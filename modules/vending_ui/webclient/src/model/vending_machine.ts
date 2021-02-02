import { MachineBank } from "./machine_bank";

export class VendingMachine {
    private name:string = "";
    private bank:MachineBank;
    constructor() {
        this.bank = new MachineBank();
    }

    public getName():string {
        return this.name;
    }

    public setName(name:string) {
        this.name = name;
    }

    public setBank(inBank:MachineBank) {
        this.bank = inBank;
    }

    public getBank():MachineBank {
        return this.bank;
    } 
}