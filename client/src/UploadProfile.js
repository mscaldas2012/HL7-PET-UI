import React, { Component } from 'react';

import {SERVER_URL} from "./config";


import ReactUploadFile from 'react-upload-file';


class ChooseButton extends Component {

    submit =  e => {
        fetch(`${SERVER_URL}/pet/profile`, {
            method: 'post',
            header: {
                'Content-Type': 'multipart/form-data'
            },
            body: e.target.value

        })
            .then(r => console.error("coming back!"))
            . catch( e => console.error("error: " + e))
    }

    render() {
        return (
        <div>
            {/*<form id="test">*/}
                <input type="file" id="docpicker" />
                <input type="submit" onClick={this.submit}/>
            {/*</form>*/}
        </div>
        )
    }
}

class UploadProfile extends Component {

    render() {
        /* set properties */
        const options = {
            baseUrl: `${SERVER_URL}/pet/profile`
        };
        /* Use ReactUploadFile with options */
        /* Custom your buttons */
        return (
            <ReactUploadFile options={options} chooseFileButton={<ChooseButton />} />
        );
    }
}

export default UploadProfile