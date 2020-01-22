import React, {Component} from "react";
import {Link} from 'react-router-dom';
import '../styles/Nav.css'

class Nav extends Component{

    showMenu(){
        let menu = document.getElementById("menu");
        let style = window.getComputedStyle(menu, null);
        if(style.getPropertyValue("display") === "none"){
            menu.style.display = "block";
        }
        else{
            menu.style.display = "none";
        }
    }

    render(){
        return(

        <nav className="Nav">
            <div className="hamburger-menu">
                <button id="hamburger-button" onClick={this.showMenu}>
                    <img src="menu.svg" width="40px" height="40px" alt="menu"/>
                </button>
            </div>
            <ul id="menu">
                <li>
                    <Link to="/">Home</Link>
                </li>
                <li>
                    <Link to="/how_to_use_it">How to use it</Link>
                </li>
                <li>
                    <Link to="/documentation">Documentation</Link>
                </li>
            </ul>
        </nav>

        );
    }

}

export default Nav;