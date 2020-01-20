import React from 'react';
import './App.css';
import Nav from './components/Nav.js';
import Info from './components/Info.js';
import Target from './components/Target.js';
import Features from './components/Features.js';

function App() {
	return (
			<div className="App">
			<Nav />
			<Features />
			<Target />
			<Info />
			</div>
	);
}

export default App;
