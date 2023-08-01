import React, {Component} from "react";
import Nav from "./Nav";
import TableHeader from "./TableHeader";
import FeatureRow from "./FeatureRow";


class FeatureView extends Component {
    state = {
        features: [],
    }

    async componentDidMount() {
        await this.setupRefreshButton()
        await this.setupAddSampleButton()
        await this.setupDeleteAllButton()
        await this.setupAddNewFeature()
    }

    async setupAddSampleButton() {
        let refreshButton = document.getElementById("refreshButton");
        let sampleButton = document.getElementById("sampleButton");
        sampleButton.onclick = async function() {
            await fetch("/debug/features", { headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}`}});
            refreshButton.click();
        };
    }

    async setupDeleteAllButton() {
        let refreshButton = document.getElementById("refreshButton");
        let deleteAllButton = document.getElementById("deleteAllButton");
        deleteAllButton.onclick = async function() {
            await fetch("/debug/deleteAllFeatures", { headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}`}});
            refreshButton.click();
        };
    }

    async setupRefreshButton() {
        let downloadLogs = async function() {
            const response = await fetch("/features/all", { headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}`}});
            const body = await response.json();
            this.setState({ features: body })
            return body
        }
        downloadLogs = downloadLogs.bind(this)

        await downloadLogs()

        let refreshButton = document.getElementById("refreshButton");
        refreshButton.onclick = function() {
            downloadLogs();
        };
    }

    async setupAddNewFeature() {
        let textField = document.getElementById("featureNameTextfield");
        let uriTextField = document.getElementById("featureURITextfield")
        let saveButton = document.getElementById("saveFeatureButton");
        let refreshButton = document.getElementById("refreshButton");

        saveButton.onclick = async function() {
            if (textField.value === "" || uriTextField.value === "") {
                alert("Both fields must be filled to create a new feature")
            } else {
                await fetch("/features/add", {
                    method: "POST",
                    headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${sessionStorage.getItem('token')}`},
                    body: JSON.stringify({
                        path: textField.value,
                        uri: uriTextField.value
                    })
                });
                refreshButton.click()
            }
        };
    }

    render() {
        return (
            <div className={"bg-gray-100 min-h-screen"}>
                <div className={"pt-8 px-6 py-4 space-y-6"}>
                    <h2 className={"text-4xl font-bold text-black whitespace-nowrap"}>
                        Features
                    </h2>
                    <div>
                        <p className={"text-sm font-medium text-gray-500 whitespace-nowrap"}>
                            Organize all features or add new ones.
                        </p>
                    </div>
                </div>
                <Nav id="logsFilters">
                    <input id="featureNameTextfield"className="mb-4shadow appearance-none border rounded-md w-fit py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline w-fit" type="text" placeholder="Feature Path"/>
                    <input id="featureURITextfield"className="mb-4shadow appearance-none border rounded-md w-fit py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline w-fit" type="text" placeholder="Feature URI"/>
                    <button id="saveFeatureButton" className="shadow bg-sky-500 hover:bg-sky-500 focus:shadow-outline focus:outline-none text-white font-bold rounded-md px-3 py-2" type="button">
                        Add Feature
                    </button>
                    <button id="refreshButton" className={`block px-3 py-2 rounded-md text-sky-500 font-bold w-fit`}>
                        Refresh
                    </button>
                    <div id="test" className={`block px-3 py-2 rounded-md text-black font-bold w-fit hidden`}>
                        DEMO
                    </div>
                    <button id="sampleButton" className={`block px-3 py-2 rounded-md text-sky-300 font-bold w-fit hidden`}>
                        Add Samples
                    </button>
                    <button id="deleteAllButton" className={`block px-3 py-2 rounded-md text-sky-300 font-bold w-fit hidden`}>
                        Delete All Features
                    </button>
                </Nav>
                <div className="flex flex-col px-5">
                    <div className="overflow-x-auto sm:-mx-6 lg:-mx-8">
                        <div className="inline-block min-w-full py-2 sm:px-6 lg:px-8">
                            <div className="overflow-hidden shadow-md sm:rounded-lg">
                                <table className="table-auto min-w-full">
                                    <thead className="bg-gray-200">
                                    <tr>
                                        {["ID", "Path", "URI", "Actions"].map(name =>
                                            <TableHeader key={name} title={name}/>
                                        )}
                                    </tr>
                                    </thead>
                                    <tbody className={"bg-white"}>
                                    {this.state.features.map(row =>
                                        <FeatureRow key={row.id} item={row}/>
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

export default FeatureView;