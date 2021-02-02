import {addLocaleData} from 'react-intl';
import * as en from 'react-intl/locale-data/en';

import locales from './nls/messages';

addLocaleData([...en]);

const locale = navigator.language || (navigator.languages && navigator.languages[0]) || 'en';
let localeWithoutRegionCode: string = locale.toLocaleLowerCase().split('/[_-]+/')[0];
localeWithoutRegionCode = locale[localeWithoutRegionCode] ? localeWithoutRegionCode : 'en';

export const localeMessages = locales[localeWithoutRegionCode];
export const language = localeWithoutRegionCode;

console.log(`Broswer Language : ${localeWithoutRegionCode}`);