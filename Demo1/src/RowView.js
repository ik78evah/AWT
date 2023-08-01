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
                    'ProjectId': 1,
                    'Authorization': sessionStorage.getItem('token')
            }
            });
            const body = await response.json();
            const status = response.status
            if (status === 200) {
                this.setState({todos: body})
                Logger.log("Get all todos successful", `User queried all todos successfully`, "/todos")
            } else {
                Logger.log("Get all todos failed", `Querying for all todos failed with statuscode ${status}`, "/todos")
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
                    'ProjectId': 1,
                    'Authorization': sessionStorage.getItem('token')
                }
            });
            Logger.log("New todo added", `User added a new todo`, "/todos")
            refreshButton.click()
        };
    }

    render() {
        return (
            <div className={"bg-red-100 min-h-screen"}>
                <div className={"pt-8 px-6 py-4 space-y-6"}>
                    <h2 className={"text-4xl font-bold text-black whitespace-nowrap"}>
                        ToDos
                    </h2>
                    <div>
                        <p className={"text-sm font-medium text-gray-500 whitespace-nowrap"}>
                            Try to complete your todos for today.
                        </p>
                    </div>
                </div>
                <Nav id="logsFilters">
                    <input id="todoTextField" className="mb-4shadow appearance-none border rounded-md w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline w-2/4" type="text" placeholder="Todo Title"/>
                    <button id="saveTodoButton" className="shadow bg-red-500 hover:bg-red-500 focus:shadow-outline focus:outline-none text-white font-bold rounded-md px-3 py-2" type="button">
                        Add
                    </button>
                    <button id="refreshButton" className={`block px-3 py-2 rounded-md text-red-500 font-bold`}>
                        Refresh
                    </button>
                </Nav>
                <div className="flex flex-col px-5">
                    <div className="overflow-x-auto sm:-mx-6 lg:-mx-8">
                        <div className="inline-block min-w-full py-2 sm:px-6 lg:px-8">
                            <div className="overflow-hidden shadow-md sm:rounded-lg">
                                <table className="table-auto min-w-full">
                                    <thead className="bg-red-200">
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