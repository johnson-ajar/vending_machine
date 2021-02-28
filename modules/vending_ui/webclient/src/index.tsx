import * as React from 'react';
import * as ReactDOM from 'react-dom';
import {store as AppStore} from './store/store_index';
import {Provider} from 'react-redux';
import {IntlProvider} from 'react-intl';
import { ApplicationComponent} from './application';
import {localeMessages, language} from './i18nSetup';

ReactDOM.render(
<Provider store = {AppStore}>
    <IntlProvider locale={language} messages={localeMessages}>
          <ApplicationComponent/>
    </IntlProvider>
</Provider>
,
  document.getElementById('root')
);