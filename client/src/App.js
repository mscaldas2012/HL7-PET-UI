import React, { Component } from 'react';
import './App.css';
import ProfileDD from './Profiles';
import './UploadProfile';

import {SERVER_URL} from "./config";
import UploadProfile from "./UploadProfile";


class App extends Component {

  //<1>
  state = {
        message: '',
        path: '',
        results: [],
        token: '',
        errorMessage: '',
        selectedProfile: 'default'
  };


  resetMessage = e => {
    e.preventDefault();
    fetch(`${SERVER_URL}/pet/message?profile=${this.state.selectedProfile}`, {
        method: 'post',
        header: {
           'Accept': 'application/json',
           'Content-Type': 'text/plain'
        },
        body: this.state.message
    })
        .then(r =>  r.json())
        .then(data => {
            if (data.token) { //Everythng went ok...
                this.setState({token: data.token, errorMessage: ''});
                return (<Warning showWarning={false}/>)
            } else { // Error happened
                this.setState({errorMessage: data.description, results: []})
            }
        })
        .catch(e => {
            this.setState({errorMessage: e.message, results: []})
        })
  };

    handleMessageChange = e => {
        this.setState({message: e.target.value});
         return (<Warning showWarning={true} />)
    };



    handlePathChange = e => {
        this.setState( {path: e.target.value})
    };

  extract = e => {
      e.preventDefault();
      fetch(`${SERVER_URL}/pet/extract?path=${this.state.path}&token=${this.state.token}`)
          .then(r => {
              if (r.status === 410) {
                  this.setState({errorMessage: r.statusText, results: []})
              } else if (r.status === 200) {
                  return r.json()
            }})
          .then( data => {
              if (data)
                this.setState({ results: data, errorMessage: '' });
          })
          .catch(e => console.error(e));
  };

   profileChanged = newValue => {
      this.setState({selectedProfile : newValue})
  };

  render() {
    return (
      <div className="App">
          <form id="newMessage">
            <h1>Enter HL7 Message below</h1><br/>
            <ProfileDD changed={this.profileChanged}/>
            <UploadProfile />
            <textarea  id="nowrap" placeholder="Copy your HL7 message here!" cols="160" rows="15" value={this.state.message} readOnly={false} onChange={this.handleMessageChange}  />
            <button onClick={this.resetMessage}>Refresh</button>
            <div className="Warning">
                <Warning/>
            </div>
          </form>

          <form id="pathExtraction">
              <div>
                <h4>Path:
                <input type="text" value={this.state.path} onBlur={this.extract} readOnly={false} onChange={this.handlePathChange} size="50"/><button onClick={this.extract}>Go</button>
                </h4>
              </div>
              <div>
                <h1>Results:</h1>
              </div>
              <div id="results">
                      {this.state.results.map(s => {
                           return <div className="alert alert-secondary" role="alert"><ul>{s.map(item =>  <li>{item}</li>)}</ul></div>
                          //return (<div>{s.map(item =>  <div className="alert alert-secondary" role="alert">{item}</div>)}</div>)

                      })}
              </div>
              <div id ="errors">
                  <div className="alert alert-danger" role="alert">{this.state.errorMessage}</div>
              </div>
          </form>
      </div>
    );
  }
}


class Warning extends Component {
    render() {
        return ( this.props.showWarning ?
                <div className="alert alert-primary" role="alert">CLick Refresh before trying to retrieve information from this message</div>: null
        )
    }
}

//const Warning = ({ showWarning }) => {showWarning ?}  <div className="alert alert-primary" role="alert">CLick Refresh before trying to retrieve information from this message</div> :  null}

export default App;
