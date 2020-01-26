/**
 * 
 */

import React, {Component} from 'react';
import "../styles/Features.css"

class Features extends Component {

	constructor(props) {
		super(props);
		this.state = {
				isInfoSelected: false,
				selectAll: true,
				lambda: true,
				tryWithResources: true,
				concatenation: true,
				record: true,
				nestMates: true,
		};
		this.handleInputChange = this.handleInputChange.bind(this);
		this.handleClick = this.handleClick.bind(this);
		
//		test
		this.handleInfoChange = this.handleInfoChange.bind(this);
	}
	
//	test
	handleInfoChange(event) {
		const target = event.target;
		const value = target.type === "checkbox" ? target.checked : target.value;
		const name = target.name;

		this.setState({
			[name]:value,
		});
		
//		test
		this.props.onInfoChange(event.target.checked);
	}
	
	handleInputChange(event) {
		const target = event.target;
		const value = target.type === "checkbox" ? target.checked : target.value;
		const name = target.name;
		

		this.setState({
			[name]:value,
		});
		
//		test
		this.checkAny();
//		
		const features = this.state;
//		
		this.props.onAnyFeatureChange(
				features.lambda,
				features.tryWithResources,
				features.concatenation,
				features.record,
				features.nestMates);
	}

	handleClick(event) {
		const selectAll = this.state.selectAll;
		
//		test
		this.setState({
			selectAll: !selectAll,
		});
		
		this.checkAll();
		
//		test
		this.props.onAllFeaturesChange(selectAll);
	}

	checkAll() {
		const selectAll = this.state.selectAll;

		if (!selectAll) {
			this.setState({
				any: true,
				lambda: true,
				tryWithResources: true,
				concatenation: true,
				record: true,
				nestMates: true,
			});
		}
		else {
			this.setState({
				any: false,
				lambda: false,
				tryWithResources: false,
				concatenation: false,
				record: false,
				nestMates: false,
			});
		}
	}
	
//	test
	checkAny() {
		const features = this.state;
		
		if (features.lambda ||
				features.tryWithResources ||
				features.concatenation ||
				features.record ||
				features.nestMates) {
			this.setState({
				any: true,
			});
		}
		else {
			this.setState({
				any: false,
			});
		}
	}

	render() {
		let features;
		let force;

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
                
                <div className="unSelect">
                <button type="button" onClick={this.handleInputChange} className="button-unSelect">
                    Confirm
                </button>
            </div>

			</div>;

		return(
				<div className="features">

                    <input
                        name="isInfoSelected"
                        type="checkbox"
                        id="info"
                        value="info"
                        checked={this.state.isInfoSelected}
                    
//                    test
                        onChange={this.handleInfoChange}
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