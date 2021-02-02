import React from 'react';
import Icons from 'svg-react-loader?name=icon!../../../assets/svg/icons.svg';

/*const Icon = ({name: string, color: string, size: number}) => (
  <svg className={`icon icon-${name}`} fill={color} width={size} height={size}>
  <use xlinkHref={`${Icons}#icon-${name}`}/>
  </svg>
);
*/

type IconType = {
  name: string,
  color: string,
  size: number
};

export class Icon extends React.Component<IconType,{}>{
  constructor(props: IconType){
    super(props);
  }

  render(){
    return(
    //  <svg className={`icon icon-${this.props.name}`} fill={this.props.color} width={this.props.size} height={this.props.size}>
    //  <use xlinkHref={`${Icons}#icon-${this.props.name}`}/>
    //</svg>
    <div>
      <Icons id="icon-arrow-down"></Icons>
    </div>
    );
  }
}
