import React, {Component} from "react";
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
                    <a href="#">Home</a>
                </li>
                <li>
                    <a href="#">How to use it</a>
                </li>
                <li>
                    <a href="../../../../../target/site/apidocs/index.html" target="_blank">Documentation</a>
                </li>
            </ul>
        </nav>

        );
    }

}

export default Nav;