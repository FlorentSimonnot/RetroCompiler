/**
 * 
 */

import React, {Component} from 'react';
import Features from './Features.js';
import Target from './Target.js';
import File from './File.js';
import '../styles/Form.css';

class Form extends Component {

	constructor(props) {
		super(props);
		this.state = {
				submit: false,	
		};
	}

	render() {

		return(

		        <div className="form">
                    <form>

                        <div>
                            <File />
                            <Features />
                            <Target />
                        </div>
                        <div className="submit-container">
                            <input type="submit" value="Submit" id="submit" />
                        </div>

                    </form>
                </div>
		);
	}
}

export default Form;