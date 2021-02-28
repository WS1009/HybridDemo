import React from 'react';
import {
    SafeAreaView,
    StyleSheet,
    ScrollView,
    View,
    Text,
    StatusBar,
    DeviceEventEmitter
} from 'react-native';

export class DemoPage extends React.Component {
    componentDidMount() {
        this.eventListener = DeviceEventEmitter.addListener("HI_RN_EVENT", (params) => {
            const {method} = params || {}
            if (method === 'onStart') {
                console.log(method)
            }
        })
    }

    componentWillUnmount() {
        this.eventListener && this.eventListener.remove()
    }

    render() {
        return <View
            style={{
                flex: 1,
                justifyContent: 'center',
                alignItems: 'center'
            }}
        >
            <Text
                style={{
                    fontSize: 30,
                    color: '#0f0'
                }}
            >
                browsing page
            </Text>
        </View>
    }
}
