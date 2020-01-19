import React from 'react';
import './App.css';
import  Nav from './components/Nav.js';
import Footer from './components/Footer.js';
import HowUseIt from './components/HowUseIt.js';

function App() {
  return (
    <div className="App">
        <div className="nav-container">
            <Nav />
        </div>

        <div className="main-container">
            {/*Set the form*/}
            <HowUseIt />
        </div>

        <div className="footer-container">
            <Footer />
        </div>
    </div>
  );
}

export default App;
