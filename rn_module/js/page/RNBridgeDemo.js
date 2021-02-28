import React from 'react'
import {View, Text, Button, StyleSheet} from 'react-native'
import HiRNBridge from '../lib/HiRNBridge'


export default class RNBridgeDemo extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            headers: ''
        }
    }

    async getHeader() {
        let headers = await HiRNBridge.getHeaderParams()
        this.setState({
            headers
        })
    }

    render() {

        const {headers} = this.state;
        return <View>
            <Button
                title={'onBack'}
                onPress={() => {
                    HiRNBridge.onBack(null)
                }}
            />

            <Button
                title={'goToNative'}
                onPress={() => {
                    HiRNBridge.goToNative({'goodsId': '1580374239317'})
                }}
            />

            <Button
                title={'getHeaderParams'}
                onPress={() => {
                    this.getHeader();
                }}
            />
            <Text>获取的header：{JSON.stringify(headers)}</Text>
        </View>;
    }
}