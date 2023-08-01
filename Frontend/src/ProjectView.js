import React, {Component} from "react";
import Nav from "./Nav";
import TableHeader from "./TableHeader";
import ProjectRow from "./ProjectRow";


class ProjectView extends Component {
    state = {
        projects: [],
    }

    async componentDidMount() {
        await this.setupRefreshButton()
        await this.setupAddSampleButton()
        await this.setupDeleteAllButton()
        await this.setupAddNewProject()
    }

    async setupAddSampleButton() {
        let refreshButton = document.getElementById("refreshButton");
        let sampleButton = document.getElementById("sampleButton");
        sampleButton.onclick = async function() {
            await fetch("/debug/projects", { headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}`}});
            refreshButton.click();
        };
    }

    async setupDeleteAllButton() {
        let refreshButton = document.getElementById("refreshButton");
        let deleteAllButton = document.getElementById("deleteAllButton");
        deleteAllButton.onclick = async function() {
            await fetch("/debug/deleteAllProjects", { headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}`}});
            refreshButton.click();
        };
    }

    async setupRefreshButton() {
        let downloadLogs = async function() {
            const response = await fetch("/projects/all", { headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}`}});
            const body = await response.json();
            this.setState({ projects: body })
            return body
        }
        downloadLogs = downloadLogs.bind(this)

        await downloadLogs()

        let refreshButton = document.getElementById("refreshButton");
        refreshButton.onclick = function() {
            downloadLogs();
        };
    }

    async setupAddNewProject() {
        let textField = document.getElementById("projectNameTextfield");
        let saveButton = document.getElementById("saveProjectButton");
        let refreshButton = document.getElementById("refreshButton");

        saveButton.onclick = async function() {
            await fetch(`/projects/add/${textField.value}`, {
                method: 'POST',
                headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}`}});
            refreshButton.click()
        };
    }

    render() {
        return (
            <div className={"bg-gray-100 min-h-screen"}>
                <div className={"pt-8 px-6 py-4 space-y-6"}>
                    <h2 className={"text-4xl font-bold text-black whitespace-nowrap"}>
                        Projects
                    </h2>
                    <div>
                        <p className={"text-sm font-medium text-gray-500 whitespace-nowrap"}>
                            Organize all projects or add new ones.
                        </p>
                    </div>
                </div>
                <Nav id="logsFilters">
                    <input id="projectNameTextfield"className="mb-4shadow appearance-none border rounded-md w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline w-2/4" type="text" placeholder="Project Name"/>
                    <button id="saveProjectButton" className="shadow bg-sky-500 hover:bg-sky-500 focus:shadow-outline focus:outline-none text-white font-bold rounded-md px-3 py-2" type="button">
                        Add Project
                    </button>
                    <button id="refreshButton" className={`block px-3 py-2 rounded-md text-sky-500 font-bold`}>
                        Refresh
                    </button>
                    <div id="test" className={`block px-3 py-2 rounded-md text-black font-bold hidden`}>
                        DEMO
                    </div>
                    <button id="sampleButton" className={`block px-3 py-2 rounded-md text-sky-300 font-bold hidden`}>
                        Add Samples
                    </button>
                    <button id="deleteAllButton" className={`block px-3 py-2 rounded-md text-sky-300 font-bold hidden`}>
                        Delete All Projects
                    </button>
                </Nav>
                <div className="flex flex-col px-5">
                    <div className="overflow-x-auto sm:-mx-6 lg:-mx-8">
                        <div className="inline-block min-w-full py-2 sm:px-6 lg:px-8">
                            <div className="overflow-hidden shadow-md sm:rounded-lg">
                                <table className="table-auto min-w-full">
                                    <thead className="bg-gray-200">
                                    <tr>
                                        {["ID", "Name", "Project ID", "Creation Date", "Actions"].map(name =>
                                            <TableHeader key={name} title={name}/>
                                        )}
                                    </tr>
                                    </thead>
                                    <tbody className={"bg-white"}>
                                    {this.state.projects.map(row =>
                                        <ProjectRow key={row.id} item={row}/>
                                    )}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default ProjectView;