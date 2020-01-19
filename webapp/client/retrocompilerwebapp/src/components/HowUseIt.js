import React, {Component} from "react";
import '../styles/HowUseIt.css'

class HowUseIt extends Component{


    render(){
        return(

            <div className="container">
                <div className="row">
                    <div className="content">
                        <div className="header __1__" >
                            <h1>What is the retro compiler</h1>
                        </div>
                        <div className="body __1__">
                            <p>
                                The retro compiler project is an API which generate java bytecode.
                                Appeared in 1996, java is one of the most used languages.
                                In order not to be forgotten, it must be regularly updated according to
                                the expectations of users which grow each year.
                                Today in version 14, java manages to surprise us by adding new features.
                                Unfortunately, many APIs are coded in earlier versions and therefore cannot use the latest features which, it must be admitted, simplifies the code.
                                The retro compiler offers you the possibility of coding in the latest version of java
                                while generating code compatible with an earlier version. the API is indeed capable of generating code compatible with Java 5.
                            </p>
                        </div>
                    </div>
                    <div>
                    </div>
                </div>
                <div className="row-right">
                    <div>
                    </div>
                    <div className="content content-right">
                          <div className="header __2__">
                                <h1>How can I use it</h1>
                          </div>
                          <div className="body __2__">
                               <p>
                                    The retro compiler accept class file, directory or Jar. You have to upload one of them.
                                    By default the retro compiler only get the java version which is use to write your(s) file(s).
                                    So you probably should check some options. Here are the available options.
                               </p>
                               <table className="option-table">
                                    <thead>
                                        <tr>
                                            <th>Option</th>
                                            <th>Description</th>
                                            <th>Argument</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>--help</td>
                                            <td>Show help.</td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>--info</td>
                                            <td>Show all features which are detected.</td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>--target</td>
                                            <td>Generate bytecode for a version.</td>
                                            <td><p>v - the version we want generate</p></td>
                                        </tr>
                                        <tr>
                                            <td>--features</td>
                                            <td>Use only with --info help.</td>
                                            <td><p>[feature, feature, ...] - show only features in the list passed in argument</p></td>
                                        </tr>
                                    </tbody>
                               </table>
                          </div>
                    </div>
                </div>
                <div className="row">
                    <div className="content">
                        <div className="header __1__" >
                            <h1>Which features can be rewritten</h1>
                        </div>
                        <div className="body __1__">
                            <p>
                                This is a good question. Retro compiler can detected and replace 5 features.
                                Here are the features and whe version of their first apparition.
                            </p>
                            <ul>
                                <li>
                                    Try-with-resources : Java 7
                                </li>
                                <li>
                                    Lambda : Java 8
                                </li>
                                <li>
                                    Concatenation : Java 9
                                </li>
                                <li>
                                    NestMates : Java 11
                                </li>
                                <li>
                                    Record : Java 14
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div>
                    </div>
                </div>
            </div>

        );
    }

}

export default HowUseIt;