import {Coin, coins, useCoinType} from './coin_type';
export class CoinRegistry {

    private coinRegistry: {[name: string]: number} ={};
    private amount:number = 0.0;

    constructor(inCoins:{[key:string]:Coin}={}) {
        this.amount =0.0;
        this.initialize(inCoins);
    }

   public initialize(inCoins:{[key:string]:Coin}) {
        Object.keys(inCoins).map((key)=>{
            this.coinRegistry[inCoins[key].name]=0;
        });
   }

   public setCoin(name:string, amount:number) {
        this.coinRegistry[name] = amount
   }

   public getNoCoin(name:string):number {
        return this.coinRegistry[name];
   }
   //Specify the country coin type.
   public getAmount(type: string):number {
       this.amount = 0.0;
       Object.keys(this.coinRegistry).forEach(name=>{
            this.amount += this.coinRegistry[name]*coins[useCoinType][name].value
       });
       return this.amount/100.0;
   }

   public getCoins() {
       return this.coinRegistry;
   }

   public setCoins(inCoins:{[name:string]:number}) {
       this.coinRegistry = inCoins;
   }
}