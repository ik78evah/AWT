import React, {Component} from "react";
class FeatureRow extends Component {

    componentDidMount() {
        let deleteButton = document.getElementById(`delete${this.props.item.id}`);

        let deleteFeature = async function() {
            await fetch(`/features/delete/${this.props.item.id}`, {
                method: "DELETE",
                headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}`}
            });
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
                <td
                    className="px-6 py-4 text-sm font-medium text-gray-900 whitespace-nowrap">
                    {this.props.item.id}
                </td>
                <td className="px-6 py-4 text-sm font-mono text-gray-500 whitespace-nowrap">
                    {this.props.item.path}
                </td>
                <td className="px-6 py-4 text-sm font-mono text-gray-500 whitespace-nowrap">
                    {this.props.item.uri}
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

export default FeatureRow;