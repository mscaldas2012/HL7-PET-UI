import React, { Component } from 'react';
import {SERVER_URL} from "./config";

class ProfileDD extends Component {

    state = {
        profiles: [],
        selectedProfile: 'default'
    };

    // function = getProfile() {
    //     return ({this.state.selectedProfile})
    // }
    componentDidMount() {
        fetch(`${SERVER_URL}/pet/profiles`)
            .then((response) => {
                return response.json();
            })
            .then(data => {
                let teamsFromApi = data.map(p => {
                    return {value: p, display: p}
                });
                this.setState({
                    profiles: teamsFromApi
                });
            }).catch(error => {
            console.log(error);
        });
    }

    updateState = e => {
        this.setState({selectedProfile : e.target.value});
        this.props.changed ( e.target.value);
    }


    render() {
        return (
            <div>
                Profile: <select value={this.state.selectedProfile} onChange={this.updateState}>
                    {this.state.profiles.map((p) => <option key={p.value} value={p.value}>{p.display}</option>)}
                </select>
            </div>
        );
    }
}

export default ProfileDD;