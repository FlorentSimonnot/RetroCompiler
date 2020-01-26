/**
 * 
 */

import React, {Component} from 'react';
import '../styles/File.css'

class File extends Component {

	constructor(props) {
		super(props);
		this.state = {
            file: true,
            directory: false,
            jar : false,
		};
		this.handleOptionChange = this.handleOptionChange.bind(this);
		this.inputFile = this.inputFile.bind(this);
		
//		test
		this.handleChange = this.handleChange.bind(this);
	}

	handleOptionChange(event, type) {
		const name = type;

		if (name === "File") {
			this.setState({
				file: true,
				directory: false,
				jar : false,
				input: "",
			});
		}
		else if(name === "Directory"){
            this.setState({
                file: false,
                directory: true,
                jar : false,
            });
		}
		else {
			this.setState({
				file: false,
				directory: false,
				jar : true,
			});
		}
	}

	inputFile(selector){
	    console.log(selector);
	}
	
//	test
	handleChange(event) {
		this.props.onInputFileChange(event.target.value);
	}

	render() {

//		test
		const inputFile = this.props.inputFile;
		
	    let f = "file";
	    if(this.state.file === true) f = "file";
	    if(this.state.directory === true) f = "directory";
	    if(this.state.jar === true) f = "jar";

	    let input;
	    if(this.state.file === true) input = <input type="file" name="files[]" id="file" multiple onChange={(e) => this.inputFile(e.target.files)}/>
	    if(this.state.directory === true) input = <input type="file" name="files[]" id="file" mozdirectory="" webkitdirectory="" directory="" onChange={(e) => this.inputFile(e.target.files)}/>
	    if(this.state.jar === true) input = <input type="file" name="files[]" id="file" onChange={(e) => this.inputFile(e.target.files)} />

	    let classname;
	    if(this.state.file === true) classname = "file-class"
	    if(this.state.directory === true) classname = "directory-class"
	    if(this.state.jar === true) classname = "jar-class"

		return(
				<div>

				    <div className="tabs">
                          <input type="radio" id="tab1" name="tab-control" checked={this.state.file} />
                          <input type="radio" id="tab2" name="tab-control" />
                          <input type="radio" id="tab3" name="tab-control" />
                          <ul>
                            <li title="File" name="File" onClick={(e) => this.handleOptionChange(e, "File")}>
                                <img src="file.svg" width="40" height="40" alt="file" />
                                <label htmlFor="tab1" role="button">
                                    <br/>
                                    <span>FILE</span>
                                </label>
                            </li>
                            <li title="Directory" name="Directory" onClick={(e) => this.handleOptionChange(e, "Directory")}>
                                <img src="folder.svg" width="40" height="40" alt="folder" />
                                <label htmlFor="tab2" role="button">
                                    <br/>
                                    <span>DIRECTORY</span>
                                </label>
                            </li>
                            <li title="Jar" name="Jar" onClick={(e) => this.handleOptionChange(e, "Jar")}>
                                <img src="jar.svg" width="40" height="40" alt="jar"/>
                                <label htmlFor="tab3" role="button">
                                    <br/>
                                    <span>JAR</span>
                                </label>
                            </li>
                          </ul>

                          <div className="slider"><div className="indicator"></div></div>

                          <div className="content">
                                <h2 className={classname}>{f}</h2>
                                <div className="FilesSelector">
                                    <div className="zone">

                                      <div id="dropZ">
                                        <div className="logo-upload">
                                            <img src="upload.svg" width="200px" height="100px" alt="cloud"/>
                                        </div>
                                        <div>Drag and drop your {f} here</div>
                                        <span>OR</span>
                                        <div className="selectFile">
                                          <label>Select {f}</label>
                                          {input}
                                        </div>
                                      </div>

                                    </div>
                                </div>
                          </div>

				    </div>

                    <label>
                    Put the full path of the file you want to parse
                    <br/>
                    <textarea value={inputFile} onChange={this.handleChange} />
                    </label>

				</div>
		);
	}
}

export default File;