/**
 * 
 */

import React, {Component} from 'react';

class File extends Component {

	constructor(props) {
		super(props);
		this.state = {
				fileOrJar: true,
				directory: false,
		};
		this.handleOptionChange = this.handleOptionChange.bind(this);
	}

	handleOptionChange(event) {
		const name = event.target.name;

		if (name === "File/Jar") {
			this.setState({
				fileOrJar: true,
				directory: false,
			});
		}
		else {
			this.setState({
				fileOrJar: false,
				directory: true,
			});
		}
	}

	render() {
		const fileOrJar = this.state.fileOrJar;
		let chooseFile;

		if (fileOrJar) {
			chooseFile = <label>
			Choose a file/jar to parse:
				<input type="file"
					ref={this.state.file} />
			</label>;
		}
		else {
			chooseFile = <label>
			Choose a directory to parse:
				<input type="file"
					ref={this.state.file}
			webkitdirectory="" />
				</label>;
		}

		return(
				<form>

				<div>
				Input type:
					<label>
				<input 
				id="FileJar"
					name="File/Jar"
						type="radio"
							checked={this.state.fileOrJar}
				onChange={this.handleOptionChange} />
				File/Jar
				</label>

				<label>
				<input
				id="Directory"
					name="Directory"
						type="radio"
							checked={this.state.directory}
				onChange={this.handleOptionChange} />
				Directory
				</label>
				</div>

				{chooseFile}

				</form>
		);
	}
}

export default File;