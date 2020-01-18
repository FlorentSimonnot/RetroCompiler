import React, {Component} from "react";
import '../styles/Nav.css'

class Nav extends Component{

    render(){
        return(

        <nav class="Nav">
            <ul>
                <li>
                    <a href="#">Home</a>
                </li>
                <li>
                    <a href="#">RetroCompiler</a>
                </li>
                <li>
                    <a href="#">Documentation</a>
                </li>
            </ul>
        </nav>

        );
    }

}

export default Nav;