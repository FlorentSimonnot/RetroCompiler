/**
 * 
 */

import React, {Component} from 'react';
import Force from './Force.js';
import "../styles/Features.css"

class Features extends Component {

	constructor(props) {
		super(props);
		this.state = {
				info: false,
				selectAll: true,
				lambda: true,
				tryWithResources: true,
				concatenation: true,
				record: true,
				nestMates: true,
		};
		this.handleInputChange = this.handleInputChange.bind(this);
		this.handleClick = this.handleClick.bind(this);
	}

	handleInputChange(event) {
		const target = event.target;
		const value = target.type === "checkbox" ? target.checked : target.value;
		const name = target.name;

		this.setState({
			[name]:value,
		});
	}

	handleClick(event) {
		const selectAll = this.state.selectAll;
		
		this.setState({
			selectAll: !selectAll,
		});
		
		this.checkAll();
	}

	checkAll() {
		const selectAll = this.state.selectAll;

		if (!selectAll) {
			this.setState({
				lambda: true,
				tryWithResources: true,
				concatenation: true,
				record: true,
				nestMates: true,
			});
		}
		else {
			this.setState({
				lambda: false,
				tryWithResources: false,
				concatenation: false,
				record: false,
				nestMates: false,
			});
		}
	}

	render() {
		const info = this.state.info;
		let features;
		let force;

		if (info) {
			features = 
            <div className="features-details">
                <div className="features-checkbox">
                    <label>
                        Choose feature(s) you want to detect :
                    </label>
                    <br/>

                    <div className="row-features-checkbox-1">
                        <div>
                            <input
                                name="lambda"
                                type="checkbox"
                                id="lambda"
                                checked={this.state.lambda}
                                onChange={this.handleInputChange} />
                            <label htmlFor="lambda">
                            Lambda
                            </label>
                        </div>

                        <div>
                            <input
                                name="record"
                                type="checkbox"
                                id="record"
                                checked={this.state.record}
                                onChange={this.handleInputChange} />
                            <label htmlFor="record">
                                Record
                            </label>
                        </div>

                        <div>
                        <input
                            name="concatenation"
                            type="checkbox"
                            id="concatenation"
                            checked={this.state.concatenation}
                            onChange={this.handleInputChange} />
                        <label htmlFor="concatenation">
                            Concatenation
                        </label>
                        </div>

                    </div>

                    <div className="row-features-checkbox-2">

                        <div>
                        <input
                            name="tryWithResources"
                            type="checkbox"
                            id="tryWithResources"
                            checked={this.state.tryWithResources}
                            onChange={this.handleInputChange} />
                        <label htmlFor="tryWithResources">
                        Try With Resources
                        </label>
                        </div>

                        <div>
                        <input
                            name="nestMates"
                            type="checkbox"
                            id="nestMates"
                            checked={this.state.nestMates}
                            onChange={this.handleInputChange} />

                        <label htmlFor="nestMates">
                            Nest Mates
                        </label>
                        </div>
                    </div>
                </div>

                <div className="unSelect">
                    <button type="button" onClick={this.handleClick} className="button-unSelect">
                        {this.state.selectAll ? "Unselect all" : "Select all"}
                    </button>
                </div>

			</div>;
		}

		if (this.state.any) {
			force = <Force />
		}

		return(
				<div className="features">

                    <input
                        name="info"
                        type="checkbox"
                        id="info"
                        checked={this.state.info}
                        onChange={this.handleInputChange}
                    />

                    <label htmlFor="info" className="checkbox-main">
                        Show me info about features detection
                    </label>

                    {features}

                    {force}

				</div>
		);
	}
}
export default Features;