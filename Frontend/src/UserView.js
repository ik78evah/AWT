import React, {Component} from "react";
import NavItem from "./NavItem";
import Nav from "./Nav";
import Table from "./Table";
import TableHeader from "./TableHeader";
import UserRow from "./UserRow"
import DropdownButton from "./DropdownButton"


class UserView extends Component {
    state = {
        users: [],
    }

    async componentDidMount() {
        await this.setupRefreshButton()
        await this.setupAddSampleButton()
        await this.setupDeleteAllButton()
    }

    async setupAddSampleButton() {
        let refreshButton = document.getElementById("refreshButton");
        let generateSamples = async function() {
            await fetch("/debug/users", { headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}`}});
        }

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
            await fetch("/debug/deleteAllUsers", {headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}`}});
            refreshButton.click();
        };
    }

    async setupRefreshButton() {
        let downloadLogs = async function() {
            const url = sessionStorage.getItem("selectedProject") === null ? "/user/getAll" : `/user/getAll/${sessionStorage.getItem("selectedProject")}`
            const response = await fetch(url, { headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}`}});
            const body = await response.json();
            this.setState({ users: body })
            return body
        }
        downloadLogs = downloadLogs.bind(this)

        await downloadLogs()

        let refreshButton = document.getElementById("refreshButton");
        refreshButton.onclick = function() {
            downloadLogs();
        };
    }

    render() {
        return (
            <div className={"bg-gray-100 min-h-screen"}>
                <div className={"pt-8 px-6 py-4 space-y-6"}>
                    <h2 className={"text-4xl font-bold text-black whitespace-nowrap"}>
                        Users
                    </h2>
                    <div>
                        <p className={"text-sm font-medium text-gray-500 whitespace-nowrap"}>
                            Manage your users.
                        </p>
                    </div>
                </div>
                <Nav id="logsFilters">
                    <DropdownButton/>
                    <div id="test" className={`block px-3 py-2 rounded-md text-black font-bold hidden`}>
                        DEMO
                    </div>
                    <button id="refreshButton" className={`block px-3 py-2 rounded-md text-sky-500 font-bold hidden`}>
                        Refresh
                    </button>
                    <button id="sampleButton" className={`block px-3 py-2 rounded-md text-sky-300 font-bold hidden`}>
                        Add Samples
                    </button>
                    <button id="deleteAllButton" className={`block px-3 py-2 rounded-md text-sky-300 font-bold hidden`}>
                        Delete All Users
                    </button>
                </Nav>
                <div className="flex flex-col px-5">
                    <div className="overflow-x-auto sm:-mx-6 lg:-mx-8">
                        <div className="inline-block min-w-full py-2 sm:px-6 lg:px-8">
                            <div className="overflow-hidden shadow-md sm:rounded-lg">
                                <table className="table-auto min-w-full">
                                    <thead className="bg-gray-200">
                                    <tr>
                                        {["Username", "Role", "Project", "Actions"].map(name =>
                                            <TableHeader key={name} title={name}/>
                                        )}
                                    </tr>
                                    </thead>
                                    <tbody className={"bg-white"}>
                                    {this.state.users.map(row =>
                                        <UserRow key={row.id} item={row}/>
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

export default UserView;