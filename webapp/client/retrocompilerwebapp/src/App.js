import React from 'react';
import './App.css';
//import Info from './components/Info.js';
//import Target from './components/Target.js';
//import Features from './components/Features.js';
import  Nav from './components/Nav.js';
import Footer from './components/Footer.js';
import HowUseIt from './components/HowUseIt.js';
import Form from './components/Form.js';

function App() {

  return (
    <div className="App">
        <div className="nav-container">
            <Nav />
        </div>

        <div className="main-container">
            <Form />
        </div>

        <div className="footer-container">
            <Footer />
        </div>
    </div>
  );
}

export default App;
