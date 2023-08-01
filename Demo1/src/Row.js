import React, {Component} from "react";
import Logger from "./Logger";

class Row extends Component {

    componentDidMount() {
        let deleteButton = document.getElementById(`delete${this.props.item.id}`);
        let completeButton = document.getElementById(`complete${this.props.item.id}`);

        let deleteProject = async function() {
            await fetch(`/demo/delete/${this.props.item.id}`, {
                method: "DELETE",
                headers: {
                    'Content-Type': 'application/json',
                    'ProjectId': 1,
                    'Authorization': sessionStorage.getItem('token')
                }
            });
            Logger.log("Delete todo", `User deleted todo`, "/todos")
        }
        deleteProject = deleteProject.bind(this)

        let completeItem = async function() {
            if (this.props.item.isCompleted === false) {
                await fetch(`/demo/setCompleted/${this.props.item.id}`, {
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/json',
                        'ProjectId': 2,
                        'Authorization': sessionStorage.getItem('token')
                    }
                });
                Logger.log("Complete todo", `User completed todo`, "/todos")
            }
        }
        completeItem = completeItem.bind(this)

        let refreshButton = document.getElementById("refreshButton");

        completeButton.onclick = async function () {
            await completeItem()
            refreshButton.click()
        }

        deleteButton.onclick = async function () {
            console.log("Called")
            await deleteProject()
            refreshButton.click()
        }
    }

    render() {
        return (
            <tr  key={`row${this.props.item.id}`} className="bg-white border-b hover:bg-gray-50">
                <td
                    className={`px-6 py-4 text-sm font-medium text-gray-900 whitespace-nowrap ${this.props.item.isCompleted ? "line-through" : "no_underline"}`}>
                    {this.props.item.title}
                </td>
                <td className="px-6 py-4" id={`complete${this.props.item.id}`}>
                    <button className="text-sm font-bold text-sky-500 rounded">
                        {this.props.item.isCompleted ? "Completed" : "Complete"}
                    </button>
                </td>
                <td className="px-6 py-4" id={`delete${this.props.item.id}`}>
                    <button className="text-sm font-bold text-red-500 rounded">
                        Delete
                    </button>
                </td>
            </tr>
        );
    }
}

export default Row;