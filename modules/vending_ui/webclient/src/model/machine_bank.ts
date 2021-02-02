import { CoinRegistry } from "./coin_registry";
import {coins, useCoinType} from './coin_type';

export class MachineBank {
    private machineRegistry:CoinRegistry;
    private userRegistry:CoinRegistry;

    public constructor() {
        this.machineRegistry = new CoinRegistry(coins[useCoinType]);
        this.userRegistry = new CoinRegistry(coins[useCoinType]);
    }

    public getMachineRegistry():CoinRegistry {
        return this.machineRegistry;
    }

    public getUserRegistry():CoinRegistry {
        return this.userRegistry;
    }

    public setMachineRegistry(registry: CoinRegistry) {
        this.machineRegistry = registry;
    }

    public setUserRegistry(registry: CoinRegistry) {
        this.userRegistry = registry;
    }

    public getAmount(type:string):number {
        return this.machineRegistry.getAmount(type)+this.userRegistry.getAmount(type);
    }
}