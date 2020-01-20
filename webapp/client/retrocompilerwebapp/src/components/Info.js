/**
 * 
 */

import React, {Component} from "react";

class Info extends Component{

	constructor(props) {
		super(props);
		this.state = {
				info: false,	
		};
		this.handleInputChange = this.handleInputChange.bind(this);
	}

	handleInputChange(event) {
		const target = event.target;
		const value = target.type === "checkbox" ? target.checked : target.value;
		const name = target.name;

		this.setState({
			[name]:value,
		});
	}

	render(){
		return(
				<form>

				
				</form>
		);
	}

}

export default Info;