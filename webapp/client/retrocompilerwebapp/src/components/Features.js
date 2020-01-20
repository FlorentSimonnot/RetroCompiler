/**
 * 
 */

import React, {Component} from 'react';
import Force from './Force.js';

class Features extends Component {

	constructor(props) {
		super(props);
		this.state = {
				info: false,
				lambda: false,
				tryWithResources: false,
				concatenation: false,
				record: false,
				nestMates: false,
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
		this.checkAny();
	}

	checkAny() {
		this.setState({
			any: this.state.lambda ||
			this.state.tryWithResources ||
			this.state.concatenation ||
			this.state.record ||
			this.state.nestMates,
		});
	}

	render() {
		const info = this.state.info;
		let features;
		let force;

		if (info) {
			features = 
				<form>
				<label>
			Features:
				</label>

			<br/>	
				
			<input
			name="lambda"
				type="checkbox"
					checked={this.state.lambda}
			onChange={this.handleInputChange} />
			<label>
			Lambda
			</label>

			<br/>

			<input
			name="tryWithResources"
				type="checkbox"
					checked={this.state.tryWithResources}
			onChange={this.handleInputChange} />
			<label>
			Try With Resources
			</label>

			<br/>

			<input
			name="concatenation"
				type="checkbox"
					checked={this.state.concatenation}
			onChange={this.handleInputChange} />
			<label>
			Concatenation
			</label>

			<br/>

			<input
			name="record"
				type="checkbox"
					checked={this.state.record}
			onChange={this.handleInputChange} />
			<label>
			Record
			</label>

			<br/>

			<input
			name="nestMates"
				type="checkbox"
					checked={this.state.nestMates}
			onChange={this.handleInputChange} />
			<label>
			Nest Mates
			</label>
			</form>;
		}

		if (this.state.any) {
			force = <Force />
		}

		return(
				<form>

				<label>
				Info:
					</label>

				<input
				name="info"
					type="checkbox"
						checked={this.state.info}
				onChange={this.handleInputChange} />

				{features}

				{force}

				</form>
		);
	}
}

export default Features;