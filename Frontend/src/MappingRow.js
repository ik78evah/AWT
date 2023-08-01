import React, {Component} from "react";

class MappingRow extends Component {

    componentDidMount() {
        let deleteButton = document.getElementById(`delete${this.props.item.id}`);

        let deleteFeature = async function() {
            const response = await fetch(`/f2p/delete/${this.props.item.feature[0].id}/${this.props.item.project[0].id}`, {
                method: "DELETE",
                headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}`}
            });
            // const body = await response.json();
        }
        deleteFeature = deleteFeature.bind(this)
        let refreshButton = document.getElementById("refreshButton");

        deleteButton.onclick = async function () {
            await deleteFeature()
            refreshButton.click()
        }
    }

    render() {
        return (
            <tr  key={`row${this.props.item.id}`} className="bg-white border-b hover:bg-gray-50">
                <td className="px-6 py-4 text-sm font-medium text-gray-900 whitespace-nowrap">
                    {this.props.item.project[0] === undefined ? "Hall" : this.props.item.project[0].name}
                </td>
                <td className="px-6 py-4 text-sm font-mono text-gray-500 whitespace-nowrap">
                    {this.props.item.feature[0] === undefined ? "Hall" : this.props.item.feature[0].path}
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

export default MappingRow;