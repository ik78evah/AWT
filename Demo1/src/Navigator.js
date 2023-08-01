import React, {Component} from "react";
import {BrowserRouter as Router, Link, Route, Routes} from "react-router-dom";
import MenuItem from "./MenuItem";
import RowView from "./RowView";
import LoginView from "./LoginView";

class Navigator extends Component {

   state = {
      path: window.location.href,
      isLoggedIn: sessionStorage.getItem('token') !== null,
      hideNavigationBar: window.location.href !== "http://localhost:3001/"
   }

   componentDidMount() {
      console.log(sessionStorage.getItem("token"))
      if (this.state.isLoggedIn) {
         if (window.location.href !== "http://localhost:3001/todos") {
            window.location.href = "todos"
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

      let todoButton = document.getElementById("todoButton");
      todoButton.onclick = function() { setPath("todos")}
   }

   isActive(field) {
      return this.state.path === `http://localhost:3000/${field}`
   }

   render() {
      return (
         <Router>
            <div className={"bg-red-100 min-h-screen"}>
               <nav className="bg-red-50 border-red-200  py-2.5 rounded shadow min-h-fit">
                  <div className="container flex flex-wrap items-center justify-between mx-8 my-3 divide-y">
                     <div>
                        <ul className="flex flex-col mt-4 md:flex-row md:space-x-8 md:mt-0 md:text-sm md:font-medium">
                           <Link to={"/"}>
                              <h1 className=" font-bold font-mono text-medium block py-2 pl-3 pr-4 text-red-500 border-b border-gray-100 md:border-0 md:p-0">
                                 ToDo List
                              </h1>
                           </Link>
                           <Link to={"/todos"} id={"todoButton"} className={`${this.state.hideNavigationBar ? 'visible' : 'invisible'}`}>
                              <MenuItem title="All" isActive={true} />
                           </Link>
                           <Link to={"/"} id={"signOutButton"} className={`${this.state.hideNavigationBar ? 'visible' : 'invisible'} gap-20`}>
                              <h1 className={`block px-50 py-2 pl-3 pr-4 text-black-500 font-bold border-b text-medium border-gray-100 hover:bg-gray-50 md:hover:bg-transparent md:border-0 md:hover:text-red-500 md:py-0`}>
                                 Sign Out
                              </h1>
                           </Link>
                        </ul>
                     </div>
                  </div>
               </nav>
               <Routes>
                  <Route path="/" element={<LoginView />}/>
                  <Route path="/todos" element={<RowView/>}/>
               </Routes>
            </div>
         </Router>
      );
   }
}

export default Navigator;
