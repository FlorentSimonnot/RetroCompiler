/**
 * 
 */

import React, {Component} from 'react';
import Features from './Features.js';
import Target from './Target.js';
import File from './File.js';

class Form extends Component {

	constructor(props) {
		super(props);
		this.state = {
				submit: false,	
		};
	}

	render() {

		return(
				<form>

				<div>
				<File />
				<Features />
				<Target />
				</div>

				<input type="submit" value="Submit" />

					</form>
		);
	}
}

export default Form;