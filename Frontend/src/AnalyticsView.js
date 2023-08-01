import React, {Component} from "react";
import NavItem from "./NavItem";
import Nav from "./Nav";
import Table from "./Table";
import DropdownButton from "./DropdownButton"

class AnalyticsView extends Component {
   state = {
      appLogs: [],
      webLogs: [],
      backendLogs: [],
      selectedLogs: "allLogs"
   }

   async getLogsFor(url, filtered) {
      const response = await fetch(url, { headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}`}});
      const body = await response.json();
      return body
   }

   async componentDidMount() {
      await this.setupRefreshButton()
      await this.setupAddSampleButton()
      this.setupFilterButtons()
      await this.setupDeleteAllButton()
   }

   setupFilterButtons() {
      let allLogsButton = document.getElementById("allLogs");
      let appLogsButton = document.getElementById("appLogs");
      let webLogsButton = document.getElementById("webLogs");
      let backendLogsButton = document.getElementById("backendLogs");

      let selectLogType = function(value) {
         this.setState({selectedLogs: value})
      };

      selectLogType = selectLogType.bind(this)

      allLogsButton.onclick = function() { selectLogType("allLogs") };
      appLogsButton.onclick = function() { selectLogType("appLogs") };
      webLogsButton.onclick = function() { selectLogType("webLogs") };
      backendLogsButton.onclick = function() { selectLogType("backendLogs") };
   }

   async setupAddSampleButton() {
      let generateSamples = async function() {
         await fetch("/debug/mobileSamples", { headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}`}});
      }
      let refreshButton = document.getElementById("refreshButton");

      let sampleButton = document.getElementById("sampleButton");
      sampleButton.onclick = async function() {
         await generateSamples();
         refreshButton.click();
      };
   }

   async setupDeleteAllButton() {
      let refreshButton = document.getElementById("refreshButton");
      let deleteAllButton = document.getElementById("deleteAllButton");
      deleteAllButton.onclick = async function() {
         await fetch("/debug/deleteAll", { headers: {
            'Content-Type': 'application/json'
            // 'Content-Type': 'application/x-www-form-urlencoded',
         }});
         refreshButton.click();
      };
   }

   async setupRefreshButton() {
      let downloadLogs = async function() {
         this.setState({ appLogs: sessionStorage.getItem("selectedProject") === null ? await this.getLogsFor("/logs/mobile/all") : await this.getLogsFor(`/logs/mobile/all/${sessionStorage.getItem("selectedProject")}`)})
         this.setState({ webLogs: sessionStorage.getItem("selectedProject") === null ? await this.getLogsFor("/logs/web/all") : await this.getLogsFor(`/logs/web/all/${sessionStorage.getItem("selectedProject")}`)})
         this.setState({ backendLogs: await this.getLogsFor("/logs/backend/all")})
      }
      downloadLogs = downloadLogs.bind(this)

      await downloadLogs()

      let refreshButton = document.getElementById("refreshButton");
      refreshButton.onclick = function() {
         downloadLogs();
      };
   }

   getLogs() {
      switch (this.state.selectedLogs) {
         case "allLogs":
            return this.state.appLogs.concat(this.state.webLogs).concat(this.state.backendLogs).sort(function(a, b) { return b.count - a.count })
         case "appLogs":
            return this.state.appLogs.sort(function(a, b) { return b.count - a.count })
         case "webLogs":
            return this.state.webLogs.sort(function(a, b) { return b.count - a.count })
         case "backendLogs":
            return this.state.backendLogs.sort(function(a, b) { return b.count - a.count })
         default:
            return []
      }
   }

   render() {
      return (
         <div className={"bg-gray-100 min-h-screen"}>
            <div className={"pt-8 px-6 py-4 space-y-6"}>
               <h2 className={"text-4xl font-bold text-black whitespace-nowrap"}>
                  Analytics
               </h2>
               <div>
                  <p className={"text-sm font-medium text-gray-500 whitespace-nowrap"}>
                     See how users are using your products.
                  </p>
               </div>
            </div>
            <Nav id="logsFilters">
               <NavItem id="allLogs" isActive={this.state.selectedLogs === "allLogs"}>
                  All ({this.state.appLogs.length + this.state.webLogs.length + this.state.backendLogs.length})
               </NavItem>
               <NavItem id={"appLogs"} isActive={this.state.selectedLogs === "appLogs"}>
                  App ({this.state.appLogs.length})
               </NavItem>
               <NavItem id={"webLogs"} isActive={this.state.selectedLogs === "webLogs"}>
                  Web ({this.state.webLogs.length})
               </NavItem>
               <NavItem id={"backendLogs"} isActive={this.state.selectedLogs === "backendLogs"}>
                  Backend ({this.state.backendLogs.length})
               </NavItem>
               <DropdownButton/>
               <button id="refreshButton" className={`block px-3 py-2 rounded-md text-sky-500 font-bold`}>
                  Refresh
               </button>
               <div id="test" className={`block px-3 py-2 rounded-md text-black font-bold hidden`}>
                  DEMO
               </div>
               <button id="sampleButton" className={`block px-3 py-2 rounded-md text-sky-300 font-bold`}>
                  Add Samples
               </button>
               <button id="deleteAllButton" className={`block px-3 py-2 rounded-md text-sky-300 font-bold hidden`}>
                  Delete All Logs
               </button>
            </Nav>
            <Table className="py-5" headers={["Event", "Description", "Occurrences", "Source", "Project", "Date", "Actions"]}
                   rows={this.getLogs()}/>
         </div>
      );
   }
}

export default AnalyticsView;