
import * as React from 'react';
import './styles/app.scss';
import './styles/app.css';
import VendingMachineComponent from './app/components/machine/vending_machine_component';
import MachineMonitorComponent from './app/components/machine_monitoring/machine_monitor_component';
//import { SampleComponent } from './app/components/samples/sample_component';


export class ApplicationComponent extends React.Component<{},{}> {

    
    public render() { 
      return (
        <div className='content'>
            {/*<SampleComponent/>*/}
           <VendingMachineComponent/>
           <MachineMonitorComponent/>
        </div>
      )
    }
}

/*
export const ApplicationComponent = () => {
  return (
    <div className='content' id = 'Application'>
        <SampleComponent/>
      <VendingMachineComponent/>
       <MachineMonitorComponent/>
    </div>
  )
}*/
