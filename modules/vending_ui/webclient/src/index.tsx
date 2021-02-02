import * as React from 'react';
import * as ReactDOM from 'react-dom';
import {store as AppStore} from './store/store_index';
import {Provider} from 'react-redux';
import {IntlProvider} from 'react-intl';
import { ApplicationComponent} from './application';
import {localeMessages, language} from './i18nSetup';
import {createMuiTheme, MuiThemeProvider} from '@material-ui/core/styles';

const material_themes = createMuiTheme({
  palette: {
    contrastThreshold: 3,
    tonalOffset: 0.2
  }
});

ReactDOM.render(
<Provider store = {AppStore}>
  <MuiThemeProvider theme={material_themes}>
    <IntlProvider locale={language} messages={localeMessages}>
          <ApplicationComponent/>
    </IntlProvider>
  </MuiThemeProvider>
</Provider>
,
  document.getElementById('root')
);