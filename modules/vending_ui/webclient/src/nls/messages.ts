 import messages_en from './messages_en.json';

// In order to import the messages in the json as a module.
// resolveJsonModule property has to set in tsconfig.json. 
// Make sure the import doesn't use * from. 
const locales: {[key: string]: {}} = {
  en: messages_en
};

export default locales;