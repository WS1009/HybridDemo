import {requireNativeComponent} from 'react-native'
import React from 'react'

const ImageView = requireNativeComponent('HiRNImageView')

export default class HiRNImageView extends React.Component {

    _onJSClickEvent = (event) => {
        if (this.props.onPress) {
            this.props.onPress(event.nativeEvent.message)
        }
    }

    render() {
        return <ImageView
            {...this.props}
            onJSClick={this._onJSClickEvent}
        />;
    }
}
