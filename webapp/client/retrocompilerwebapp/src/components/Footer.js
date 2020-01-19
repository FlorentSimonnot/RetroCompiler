import React, {Component} from "react";
import '../styles/Footer.css'

class Footer extends Component{

    render(){
        return(
            <div className="Footer">
                <div className="top">
                    <div className="left-container">
                        <table>
                            <tbody>
                                    <tr>
                                        <td>Jonathan Chu  -  jchu@etud.u-pem.fr</td>
                                    </tr>
                                    <tr>
                                        <td>Florent Simonnot  -  fsimonno@etud.u-pem.fr</td>
                                    </tr>
                            </tbody>
                        </table>
                    </div>

                    <div className="right-container">
                        <img alt="logo_upem" src="eiffel-logo.jpg" width="auto" height="80px"/>
                    </div>
                </div>

                 <div className="bottom">
                        <span>© - copyright all reserved</span>
                 </div>
            </div>
        );
    }

}

export default Footer;