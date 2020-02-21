import React, { Component } from 'react';

import {SERVER_URL} from "./config";

import axios from 'axios';


// import ReactUploadFile from 'react-upload-file';


class UploadProfile extends Component {

    submit =  e => {
        let files = e.target.files;
        let reader = new FileReader();
        reader.readAsDataURL(files[0]); //readAsText?
        reader.onload=(e1) => {
            const formData = new FormData();
            formData.append("file", files[0]);

            axios ({
                method: 'post',
                url: `${SERVER_URL}/pet/profile`,
                data: formData
            })
        }
    };




    render() {
        return (
        <div >
            Or upload a new Profile: <input type="file" id="docpicker" name="file" onChange={this.submit}/>
        </div>
        )
    }
}

// class UploadProfile extends Component {
//
//     render() {
//         /* set properties */
//         const options = {
//             baseUrl: `${SERVER_URL}/pet/profile`
//         };
//         /* Use ReactUploadFile with options */
//         /* Custom your buttons */
//         return (
//             <ReactUploadFile options={options} chooseFileButton={<ChooseButton />} />
//         );
//     }
// }

export default UploadProfile