import React from 'react';
import './App.css';
import  Nav from './components/Nav.js';
import Footer from './components/Footer.js';
import HowUseIt from './components/HowUseIt.js';
import Form from './components/Form.js';
import {BrowserRouter as Router, Switch, Route} from 'react-router-dom';

function App() {

  return (
    <div className="App">
        <Router>
            <div className="nav-container">
                <Nav />
            </div>

            <div className="main-container">
                  <Switch>
                    <Route exact component={Form} path="/" />
                    <Route component={HowUseIt} path="/how_to_use_it" />
                    <Route path="/documentation" />
                  </Switch>
            </div>
        </Router>

        <div className="footer-container">
            <Footer />
        </div>
    </div>
  );
}

export default App;
