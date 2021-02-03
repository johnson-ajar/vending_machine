
export var useCoinType:string = 'UK'

export interface Coin {
    name: string,
    display: string,
    value: number,
    min: number, //use to set the slider
    max: number // use to set the slider
}
export interface CoinType {
    [name: string]: {[key:string]:Coin}
};

export  var coins:CoinType = {
        UK:{
            ONE_POUND:{name:"ONE_POUND", display:"1£",min:0, max:100, value:100},
            FIFTY_PENCE:{name:"FIFTY_PENCE", display:"50p",min:0,max:100, value: 50},
            TWENTY_PENCE:{name:"TWENTY_PENCE", display:"20p",min:0, max:500, value: 20},
            TEN_PENCE:{name: "TEN_PENCE", display:"10p",min:0,max:1000, value: 10},
            FIVE_PENCE:{name: "FIVE_PENCE", display:"5p",min:0, max:2000,value: 5},
            TWO_PENCE:{name:"TWO_PENCE", display:"2p",min:0, max:2000,value: 2},
            ONE_PENCE:{name:"ONE_PENCE", display:'1p',min:0, max:2000, value: 1} 
        },
        US: {}
}

export var getCoinByCountry= (country:string):{[key:string]:Coin} => {
    return coins[country];
};