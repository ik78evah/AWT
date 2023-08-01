import React, {Component} from "react";
import Nav from "./Nav";
import TableHeader from "./TableHeader";
import MappingRow from "./MappingRow";
import ProjectFeatureSelector from "./ProjectFeatureSelector"
import FeatureSelector from "./FeatureSelector"

class MappingView extends Component {
    state = {
        features: [],
        projects: [],
        mappings: []
    }

    async componentDidMount() {
        await this.setupRefreshButton()
        await this.setupAddNewFeature()
    }

    async setupRefreshButton() {
        let downloadFeatures = async function() {
            const response = await fetch("/features/all", { headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}`}});
            const body = await response.json();
            this.setState({ features: body })
            return body
        }
        downloadFeatures = downloadFeatures.bind(this)

        await downloadFeatures()

        let downloadProjects = async function() {
            const response = await fetch("/projects/all", { headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}`}});
            const body = await response.json();
            this.setState({ projects: body })
            return body
        }
        downloadProjects = downloadProjects.bind(this)

        await downloadProjects()

        let downloadMappings = async function() {
            const response = await fetch("/f2p/all", { headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}`}});
            const body = await response.json();
            this.setState({ mappings: body })
            return body
        }
        downloadMappings = downloadMappings.bind(this)

        await downloadMappings()
        
        let refreshButton = document.getElementById("refreshButton");
        refreshButton.onclick = async function() {
            await downloadFeatures();
            await downloadProjects();
            await downloadMappings();
        };
    }

    async setupAddNewFeature() {
        let saveButton = document.getElementById("saveMappingButton");
        let refreshButton = document.getElementById("refreshButton");

        saveButton.onclick = async function() {
            if (sessionStorage.getItem("selectedProjectMapping") === null || sessionStorage.getItem("selectedFeature") === null) {
                alert("You need to select a project and a feature to add a mapping")
            } else {
                let selectedFeature = sessionStorage.getItem("selectedFeature")
                let selectedProject = sessionStorage.getItem("selectedProjectMapping")
                await fetch(`/f2p/add/${selectedFeature}/${selectedProject}`, {
                    method: "POST",
                    headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}`}
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
                        Mappings
                    </h2>
                    <div>
                        <p className={"text-sm font-medium text-gray-500 whitespace-nowrap"}>
                            Enable or disable specific features for your projects.
                        </p>
                    </div>
                </div>
                <Nav id="logsFilters">
                    <h1 id="refreshButton" className={`block px-3 py-2 rounded-md text-sky-500 font-bold w-fit`}>
                        Map Project
                    </h1>
                    <ProjectFeatureSelector />
                    <h1 id="refreshButton" className={`block px-3 py-2 rounded-md text-sky-500 font-bold w-fit`}>
                        to Feature
                    </h1>
                    <FeatureSelector />
                    <button id="saveMappingButton" className="shadow bg-sky-500 hover:bg-sky-500 focus:shadow-outline focus:outline-none text-white font-bold rounded-md px-3 py-2" type="button">
                        Add Mapping
                    </button>
                    <button id="refreshButton" className={`block px-3 py-2 rounded-md text-sky-500 font-bold w-fit`}>
                        Refresh
                    </button>
                </Nav>
                <div className="flex flex-col px-5">
                    <div className="overflow-x-auto sm:-mx-6 lg:-mx-8">
                        <div className="inline-block min-w-full py-2 sm:px-6 lg:px-8">
                            <div className="overflow-hidden shadow-md sm:rounded-lg">
                                <table className="table-auto min-w-full">
                                    <thead className="bg-gray-200">
                                    <tr>
                                        {["Project", "Feature", "Actions"].map(name =>
                                            <TableHeader key={name} title={name}/>
                                        )}
                                    </tr>
                                    </thead>
                                    <tbody className={"bg-white"}>
                                    {this.state.mappings.map(row =>
                                        <MappingRow key={row.id} item={{
                                            "project": this.state.projects.filter(proj => proj.id === row.projectid),
                                            "feature": this.state.features.filter(feat => feat.id === row.featureid)
                                        }}/>
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

export default MappingView;