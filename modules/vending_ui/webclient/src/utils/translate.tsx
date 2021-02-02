import { IntlProvider } from 'react-intl';
import {localeMessages as messages, language} from '../i18nSetup';

const {intl} = new IntlProvider(
  {locale: language, messages},
  {}
).getChildContext();

const translate = (id: string, values?:{}) => {
  console.log(messages);
  console.log(language+ " " +id);
  return intl.formatMessage({id}, values)
}

export default translate;