import React, {Component} from "react";
import Nav from "./Nav";
import TableHeader from "./TableHeader";
import Row from "./Row";
import Logger from "./Logger";


class RowView extends Component {
    state = {
        todos: [],
    }

    async componentDidMount() {
        await this.setupRefreshButton()
        await this.setupAddNewTodo()
    }

    async setupRefreshButton() {
        let downloadLogs = async function() {
            const response = await fetch("/demo/all",{
                method: "GET",
                headers: {
                    'Content-Type': 'application/json',
                    'ProjectId': 2,
                    'Authorization': sessionStorage.getItem('token')
            }
            });
            const body = await response.json();
            const status = response.status
            if (status === 200) {
                this.setState({todos: body})
                Logger.log("Get all items successful", `User queried all items successfully`, "/items")
            } else {
                Logger.log("Get all items failed", `Querying for all items failed with statuscode ${status}`, "/items")
            }
            return body
        }
        downloadLogs = downloadLogs.bind(this)

        await downloadLogs()

        let refreshButton = document.getElementById("refreshButton");
        refreshButton.onclick = function() {
            downloadLogs();
        };
    }

    async setupAddNewTodo() {
        let textField = document.getElementById("todoTextField");
        let saveButton = document.getElementById("saveTodoButton");
        let refreshButton = document.getElementById("refreshButton");

        saveButton.onclick = async function() {
            await fetch(`/demo/add/${textField.value}`, {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                    'ProjectId': 2,
                    'Authorization': sessionStorage.getItem('token')
                }
            });
            Logger.log("New item added", `User added a new shopping item`, "/items")
            refreshButton.click()
        };
    }

    render() {
        console.log(this.state)
        return (
            <div className={"bg-green-100 min-h-screen"}>
                <div className={"pt-8 px-6 py-4 space-y-6"}>
                    <h2 className={"text-4xl font-bold text-black whitespace-nowrap"}>
                        Items
                    </h2>
                    <div>
                        <p className={"text-sm font-medium text-gray-500 whitespace-nowrap"}>
                            What do you want to buy today?
                        </p>
                    </div>
                </div>
                <Nav id="logsFilters">
                    <input id="todoTextField" className="mb-4shadow appearance-none border rounded-md w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline w-2/4" type="text" placeholder="Todo Title"/>
                    <button id="saveTodoButton" className="shadow bg-green-500 hover:bg-green-500 focus:shadow-outline focus:outline-none text-white font-bold rounded-md px-3 py-2" type="button">
                        Add
                    </button>
                    <button id="refreshButton" className={`block px-3 py-2 rounded-md text-green-500 font-bold`}>
                        Refresh
                    </button>
                </Nav>
                <div className="flex flex-col px-5">
                    <div className="overflow-x-auto sm:-mx-6 lg:-mx-8">
                        <div className="inline-block min-w-full py-2 sm:px-6 lg:px-8">
                            <div className="overflow-hidden shadow-md sm:rounded-lg">
                                <table className="table-auto min-w-full">
                                    <thead className="bg-green-200">
                                    <tr>
                                        {["Title", "Completed", "Action"].map(name =>
                                            <TableHeader key={name} title={name}/>
                                        )}
                                    </tr>
                                    </thead>
                                    <tbody className={"bg-white"}>
                                    {this.state.todos.map(row =>
                                        <Row key={row.id} item={row}/>
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

export default RowView;