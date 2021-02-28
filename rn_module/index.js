/**
 * @format
 */
import React from 'react'
import {Text, View, AppRegistry} from 'react-native';
import App from './App';
import {name as appName} from './app.json';
import {DemoPage} from "./js/page/DemoPage";
import RNBridgeDemo from "./js/page/RNBridgeDemo";

const PageRoute = {
    '/browsing': DemoPage,
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

AppRegistry.registerComponent(appName, () => Index);
// AppRegistry.registerComponent('demo_page', () => DemoPage);
