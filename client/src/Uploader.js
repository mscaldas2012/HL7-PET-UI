import React from 'react';
import Modal from 'react-modal';
import Dropzone from 'react-dropzone';
import './Uploader.css';
import axios from 'axios';

import {SERVER_URL} from "./config";


const uploadModalStyles = {
    overlay: { backgroundColor: 'rgba(120, 120, 120, 0.75)' },
    content: {
        top: '50%',
        left: '50%',
        right: 'auto',
        bottom: 'auto',
        marginRight: '-50%',
        transform: 'translate(-50%, -50%)',
        width: '80%',
        overflow: 'visible',
        padding: 0,
    },
}; // .uploadModalStyles

Modal.setAppElement('#root');

const Uploader = props => {
    const UPLOAD_MAX_SIZE = 1000000
    const handleDrop = (acceptedFiles, rejectedFiles) => {
        // console.log('acceptedFiles:', acceptedFiles);
        // console.log('rejectedFiles:', rejectedFiles);

        if (acceptedFiles.length === 1) {
            const formData = new FormData();

            formData.append('file', acceptedFiles[0]);

            // props.setUploadState(UploadStatus.Uploading);
            axios({
                method: 'post',
                url: `${SERVER_URL}/pet/profile`,
                data: formData,
            })
                .then(resp => {
                    // console.log('UPLOAD_PATH response:', resp);
                    // set file fileID from response
                        console.log("all good!")
                })
                .catch(err => {
                    // console.log(err);
                    console.log('err.response:', err.response);
                })
        } // .if
    }; // ./handleDrop

    const closeModal = () => {console.log("closign") } //props.setUploadState(UploadStatus.Off);

    return (
        // <div className="row uploader-row">
        <Modal
            // isOpen={props.uploadState === UploadStatus.Started}
            isOpen = {true}
            onRequestClose={props.hideEditModal}
            contentLabel="Upload a new file"
            onRequestClose={closeModal}
            shouldCloseOnOverlayClick={false}
            style={uploadModalStyles}
        >
            <div className="Uploader-container">
                <div className="d-flex justify-content-end">
                    <button onClick={closeModal} className="btn">
                        Close
                    </button>
                </div>

                <div className="Uploader">
                    <Dropzone
                        onDrop={handleDrop}
                        minSize={0}
                        maxSize={UPLOAD_MAX_SIZE}
                        multiple={false}
                    >
                        {({
                              getRootProps,
                              getInputProps,
                              isDragActive,
                              isDragReject,
                              rejectedFiles,
                          }) => {
                            const isFileTooLarge =
                                !isDragActive &&
                                rejectedFiles.length > 0 &&
                                rejectedFiles[0].size > UPLOAD_MAX_SIZE;
                            let classForDropBox = 'uploader-drop-box';
                            if (isDragReject) {
                                classForDropBox += ' reject';
                            }
                            if (isDragActive) {
                                classForDropBox += ' active';
                            }

                            return (
                                // <div {...getRootProps()} className={classForDropBox}>
                                //     <input {...getInputProps()} />
                                <div>
                                    {isDragActive && !isDragReject && (
                                        <div className="text-success">Drop file ...</div>
                                    )}
                                    {isDragReject && (
                                        <div className="text-danger">Only one file</div>
                                    )}
                                    {isFileTooLarge && (
                                        <div className="text-danger">
                                            File exceds maximum size of 100MB.
                                        </div>
                                    )}

                                    <div className="instructions">
                                        <figure className="files"></figure>
                                        <h5>Drag and Drop or Browse your files</h5>
                                        <p>
                                            {
                                                'Select add file or simply drag and drop a file anywhere on this area to start uploading.'
                                            }
                                        </p>
                                        <button className="btn btn-action btn-outline-primary">
                                            Add File
                                        </button>
                                    </div>
                                </div>
                            );
                        }}
                    </Dropzone>
                </div>
            </div>
        </Modal>
        // </div>
    ); // .return
}; // .Uploader

export default Uploader;
