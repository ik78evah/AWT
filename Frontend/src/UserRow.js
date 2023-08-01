import React, {Component} from "react";

class UserRow extends Component {

    componentDidMount() {
        let deleteButton = document.getElementById(`delete${this.props.item.id}`);
        let refreshButton = document.getElementById("refreshButton");

        let deleteProject = async function() {
            await fetch(`/user/deleteId/${this.props.item.id}`, { headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}`}});
        }
        deleteProject = deleteProject.bind(this)

        deleteButton.onclick = async function () {
            await deleteProject()
            refreshButton.click();
        }
    }

    render() {
        return (
            <tr  key={`row${this.props.id}`} className="bg-white border-b hover:bg-gray-50">
                <td className="px-6 py-4 text-sm font-mono text-gray-500 whitespace-nowrap">
                    {this.props.item.username}
                </td>
                <td className="px-6 py-4 text-sm font-mono text-gray-500 whitespace-nowrap">
                    {this.props.item.role.replace("ROLE_", "")}
                </td>
                <td
                    className="px-6 py-4 text-sm font-medium text-gray-900 whitespace-nowrap">
                    {this.props.item.projectid}
                </td>
                <td className="px-6 py-4" id={`delete${this.props.item.id}`}>
                    <button className="text-sm font-bold text-red-500 rounded">
                        Revoke Access
                    </button>
                </td>
            </tr>
        );
    }
}

export default UserRow;