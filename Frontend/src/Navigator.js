import React, {Component} from "react";
import {BrowserRouter as Router, Link, Route, Routes} from "react-router-dom";
import MenuItem from "./MenuItem";
import AnalyticsView from "./AnalyticsView";
import ProjectView from "./ProjectView";
import FeatureView from "./FeatureView";
import UserView from "./UserView";
import LoginView from "./LoginView";
import MappingView from "./MappingView";

class Navigator extends Component {

   state = {
      path: window.location.href,
      isLoggedIn: sessionStorage.getItem('token') !== null,
      hideNavigationBar: window.location.href !== "http://localhost:3000/"
   }

   componentDidMount() {
      if (this.state.isLoggedIn) {
         if (window.location.href !== "http://localhost:3000/analytics") {
            window.location.href = "analytics"
         }
      }

      const signOutButton = document.getElementById("signOutButton");

      let deleteToken = function() {
         sessionStorage.clear()
      }
      deleteToken = deleteToken.bind(this)

      signOutButton.onclick = function () {
         deleteToken()
         window.location.href = "/"
      }

      let setPath = function(path)
      {
         this.setState({path: `http://localhost:3000/${path}`})
      };
      setPath = setPath.bind(this)

      let analyticsButton = document.getElementById("analyticsButton");
      let userButton = document.getElementById("userButton");
      let projectButton = document.getElementById("projectButton");
      let featureButton = document.getElementById("featureButton");
      let mappingsButton = document.getElementById("mappingsButton");

      analyticsButton.onclick = function() { setPath("analytics")}
      userButton.onclick = function() { setPath("users")}
      projectButton.onclick = function() { setPath("projects")}
      featureButton.onclick = function() { setPath("features")}
      mappingsButton.onclick = function() { setPath("mappings")}
   }

   isActive(field) {
      return this.state.path === `http://localhost:3000/${field}`
   }

   render() {
      return (
         <Router>
            <div className={"bg-gray-100 min-h-screen"}>
               <nav className="bg-gray-50 border-gray-200  py-2.5 rounded shadow min-h-fit">
                  <div className="container flex flex-wrap items-center justify-between mx-8 my-3 divide-y">
                     <div>
                        <ul className="flex flex-col mt-4 md:flex-row md:space-x-8 md:mt-0 md:text-sm md:font-medium">
                           <Link to={"/"}>
                              <h1 className=" font-bold font-mono text-medium block py-2 pl-3 pr-4 text-sky-500 border-b border-gray-100 md:border-0 md:p-0">
                                 mBaaS
                              </h1>
                           </Link>
                           <Link to={"/analytics"} id={"analyticsButton"} className={`${this.state.hideNavigationBar ? 'visible' : 'invisible'}`}>
                              <MenuItem title="Analytics" isActive={this.isActive("analytics")} />
                           </Link>

                           <Link to={"/users"} id={"userButton"} className={`${this.state.hideNavigationBar ? 'visible' : 'invisible'}`}>
                              <MenuItem title="Users" isActive={this.isActive("users")}/>
                           </Link>

                           <Link to={"/projects"} id={"projectButton"} className={`${this.state.hideNavigationBar ? 'visible' : 'invisible'}`}>
                              <MenuItem title="Projects" isActive={this.isActive("projects")}/>
                           </Link>

                           <Link to={"/features"} id={"featureButton"} className={`${this.state.hideNavigationBar ? 'visible' : 'invisible'}`}>
                              <MenuItem title="Features" isActive={this.isActive("features")}/>
                           </Link>
                           <Link to={"/mappings"} id={"mappingsButton"} className={`${this.state.hideNavigationBar ? 'visible' : 'invisible'}`}>
                              <MenuItem title="Mappings" isActive={this.isActive("mappings")}/>
                           </Link>
                           <Link to={"/"} id={"signOutButton"} className={`${this.state.hideNavigationBar ? 'visible' : 'invisible'} gap-20`}>
                              <h1 className={`block px-50 py-2 pl-3 pr-4 text-red-500 font-bold border-b text-medium border-gray-100 hover:bg-gray-50 md:hover:bg-transparent md:border-0 md:hover:text-sky-500 md:py-0`}>
                                 Sign Out
                              </h1>
                           </Link>
                        </ul>
                     </div>
                  </div>
               </nav>
               <Routes>
                  <Route path="/" element={<LoginView />}/>
                  <Route path="/analytics" element={<AnalyticsView/>}/>
                  <Route path="/users" element={<UserView />}/>
                  <Route path="/projects" element={<ProjectView/>}/>
                  <Route path="/features" element={<FeatureView/>}/>
                  <Route path="/mappings" element={<MappingView/>}/>
               </Routes>
            </div>
         </Router>
      );
   }
}

export default Navigator;
