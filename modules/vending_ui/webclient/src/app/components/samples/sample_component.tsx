import * as React from 'react';
import translate from '../../../utils/translate';
// import {Icon} from './app/components/common/icon';
import * as icons from '../../../assets'
import { coins, useCoinType } from '../../../model/coin_type';
export class SampleComponent extends React.Component<{}> {
    
    private getCoins() {
        return (
        <div>
        {
            Object.keys(coins[useCoinType]).map((key)=>{
                return coins[useCoinType].name;
        })}
        </div>);
    }
    
    public render() {
        return(<div>
                Sample Component
          <div className='content-scss'>
            Hello scss is used for styling
          </div>
          <div>
           {translate('check.message1')}
           {translate('check.message2')}
           {this.getCoins()}
           {console.log(icons.svgIcons)}
           <icons.resistor/>
           <icons.resistor/>
           <img src={icons.ground24}/>
           </div>
        </div>);
    }
}