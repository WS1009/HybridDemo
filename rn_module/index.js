/**
 * @format
 */

import {AppRegistry} from 'react-native';
import App from './App';
import {name as appName} from './app.json';
import {DemoPage} from "./DemoPage";
import RNBridgeDemo from "./js/RNBridgeDemo";

AppRegistry.registerComponent('rn_module', () => RNBridgeDemo);
// AppRegistry.registerComponent('demo_page', () => DemoPage);
