
export var useCoinType:string = 'UK'

export interface Coin {
    name: string,
    display: string,
    value: number
}
export interface CoinType {
    [name: string]: {[key:string]:Coin}
};

export  var coins:CoinType = {
        UK:{
            ONE_POUND:{name:"ONE_POUND", display:"1£", value:100},
            FIFTY_PENCE:{name:"FIFTY_PENCE", display:"50p", value: 50},
            TWENTY_PENCE:{name:"TWENTY_PENCE", display:"20p", value: 20},
            TEN_PENCE:{name: "TEN_PENCE", display:"10p", value: 10},
            FIVE_PENCE:{name: "FIVE_PENCE", display:"5p",value: 5},
            TWO_PENCE:{name:"TWO_PENCE", display:"2p",value: 2},
            ONE_PENCE:{name:"ONE_PENCE", display:'1p', value: 1} 
        },
        US: {}
}

export var getCoinByCountry= (country:string):{[key:string]:Coin} => {
    return coins[country];
};