import React from 'react'
import {View, Text, Button, StyleSheet} from 'react-native'
import HiRNBridge from '../lib/HiRNBridge'

import HiRNImageView from '../lib/HiRNImageView'


export default class RNBridgeDemo extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            headers: '',
            message: ''
        }
    }

    async getHeader() {
        let headers = await HiRNBridge.getHeaderParams()
        this.setState({
            headers
        })
    }

    render() {

        const {headers, message} = this.state;
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

            <HiRNImageView
                style={{
                    width: 200,
                    height: 200
                }}
                src={'https://m.360buyimg.com/babel/jfs/t1/158210/3/5443/41202/6013b891Ecdf9fa43/6c2efaa3e1af72f5.png'}
                onPress={e => {
                    this.setState({
                        message: e
                    })

                    alert("收到native的事件：" + e)
                }}
            />

            <Text>收到来自native UI 的数据 ：{message}</Text>

        </View>;
    }
}