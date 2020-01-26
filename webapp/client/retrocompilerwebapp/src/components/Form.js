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
				info: "TEST INFO ",
				allFeatures: true,
				feature: "TEST FEATURE ",
				features: "TEST FEATURES ",
				force: "TEST FORCE ",
				version: "TEST VERSION ",
				inputFile: "TEST FILE ",
				target: "TEST TARGET ",
		};
	
//		test
		this.handleInfoChange = this.handleInfoChange.bind(this);
		this.handleAnyFeatureChange = this.handleAnyFeatureChange.bind(this);
		this.handleAllFeaturesChange = this.handleAllFeaturesChange.bind(this);
		this.handleForceOptionChange = this.handleForceOptionChange.bind(this);
		this.handleTargetOptionChange = this.handleTargetOptionChange.bind(this);
		this.handleTargetVersionChange = this.handleTargetVersionChange.bind(this);
		this.handleInputFileChange = this.handleInputFileChange.bind(this);
	}
	
//	test
	handleInfoChange(info) {
		if (info) {
			this.setState({
				info: "--info ",
			});
		}
		else {
			this.setState({
				info: " ",
			});
		}
	}
	
//	test
	handleAnyFeatureChange(
			lambda,
			twr,
			concat,
			rec,
			nestMates) {
		const lambdaTxt = lambda ? "lambda" : null;
		const twrTxt = twr ? "try-with-resources" : null;
		const concatTxt = concat ? "concatenation" : null;
		const recTxt = rec ? "record" : null;
		const nestMatesTxt = nestMates ? "nest-mates" : null;

		if (lambda || twr || concat || rec || nestMates) {
			this.setState({
				feature: "--features ",
				features: [lambdaTxt, twrTxt, concatTxt, recTxt, nestMatesTxt]
			.filter(f => f != null)
			.join(", ") + " ",
			});
		}
		else {
			this.setState({
				feature: " ",
				features: " ",
			});
		}
	}
	
//	test
	handleAllFeaturesChange(all) {
		if (all) {
			this.setState({
				feature: " ",
				feautures: " ",
			});
		}
		else {
			this.setState({
				feature: " ",
				features: " ",
			});
		}
	}
	
//	test
	handleForceOptionChange(forceOption) {
		if (forceOption) {
			this.setState({
				force: "--force ",
			});
		}
		else {
			this.setState({
				force: " ",
			});
		}
	}
	
//	test
	handleTargetOptionChange(targetOption) {
		if (targetOption) {
			this.setState({
				target: "--target ",
				version: "5 ",
			});
		}
		else {
			this.setState({
				target: " ",
				version: " ",
				force: " ",
			});
		}
	}
	
//	test
	handleTargetVersionChange(targetVersion) {
		this.setState({
			version: targetVersion,
		});
	}
	
//	test
	handleInputFileChange(file) {
		this.setState({
			inputFile: file,
		});
	}
	
	render() {
		const inputFile = this.state.inputFile;
		const info = this.state.info;
		const feature = this.state.feature;
		const features = this.state.features;
		const force = this.state.force;
		const target = this.state.target;
		const version = this.state.version;
		
		return(

		        <div className="form">
                    <form>

                        <div>
                            <File 
                            	onInputFileChange={this.handleInputFileChange}
                            />
                            <Features
                            	onInfoChange={this.handleInfoChange}
	                            onAllFeaturesChange={this.handleAllFeaturesChange}
                            	onAnyFeatureChange={this.handleAnyFeatureChange}
                            />
                            <Target 
                            	onForceOptionChange={this.handleForceOptionChange}
                            	onTargetOptionChange={this.handleTargetOptionChange}
                            	onTargetVersionChange={this.handleTargetVersionChange}
                            />
                        </div>
                        <div className="submit-container">
                            <input type="submit" value="Submit" id="submit" />
                        </div>
                            {inputFile}
                            <br/>
                            {info}
                            <br/>
                            {feature}
                            {features}
                            <br/>
                            {target}
                            <br/>
                            {version}
                            <br/>
                            {force}
                    </form>
                </div>
		);
	}
}

export default Form;