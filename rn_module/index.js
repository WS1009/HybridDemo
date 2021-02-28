/**
 * @format
 */
import React from 'react'
import {Text, View, AppRegistry, DeviceEventEmitter} from 'react-native';
import App from './App';
import {name as appName} from './app.json';
import {BrowsingPage} from "./js/page/BrowsingPage";
import RNBridgeDemo from "./js/page/RNBridgeDemo";

const PageRoute = {
    '/browsing': BrowsingPage,
    '/bridgeDemo': RNBridgeDemo
}

class Index extends React.Component {
    getPageComponent() {
        const {routeTo} = this.props;
        const page = PageRoute[routeTo]
        return page
    }

    render() {
        const Page = this.getPageComponent()
        return <Page/>;
    }
}

//适合单RN实例的情况
// AppRegistry.registerComponent(appName, () => Index);

//适合多RN实例的情况
AppRegistry.registerComponent('rn_module_bridgeDemo', () => RNBridgeDemo);
AppRegistry.registerComponent('rn_module_browsing', () => BrowsingPage);
