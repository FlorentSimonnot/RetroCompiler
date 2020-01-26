/**
 * 
 */

import React, {Component} from "react";

class Force extends Component{

	constructor(props) {
		super(props);
		this.handleInputChange = this.handleInputChange.bind(this);
	}

	handleInputChange(event) {
		const target = event.target;
		const value = target.type === "checkbox" ? target.checked : target.value;
		
//		test
		this.props.onForceChange(value);
	}

	render(){
		
//		test
		const force = this.props.force;
		
		return(
				<div>

                    <input
                        name="force"
                        type="checkbox"
                        id="force"
                        checked={force}
                         onChange={this.handleInputChange} />

                         <label htmlFor="force">
                            Force the retro compilation
                         </label>

				</div>
		);
	}

}

export default Force;