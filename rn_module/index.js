/**
 * @format
 */

import {AppRegistry} from 'react-native';
import App from './App';
import {name as appName} from './app.json';
import {DemoPage} from "./DemoPage";

AppRegistry.registerComponent(appName, () => App);
AppRegistry.registerComponent('demo_page', () => DemoPage);
