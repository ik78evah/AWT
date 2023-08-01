import React, {Component} from "react";

class FeatureSelector extends Component {
    state = {
        isVisible: false,
        features: []
    }

    async componentDidMount() {
        this.setupButton()
        await this.getFeatures()

        let setFeature = function(id) {
            sessionStorage.setItem("selectedFeature", id)
            this.setState({isVisible: false})
        };
        setFeature = setFeature.bind(this)
        this.state.features.map(row => {
            let button = document.getElementById(`feature${row.id}`)
            button.onclick = function() {
                setFeature(row.id)
                let refreshButton = document.getElementById("refreshButton")
                refreshButton.click()
            }
        })
    }

    setupButton() {
        let sampleButton = document.getElementById(`dropdownFeatureButton`);
        let toggleVisibility = function() {
            this.setState(prevState => ({ isVisible: !prevState.isVisible }))};
        toggleVisibility = toggleVisibility.bind(this)

        sampleButton.onclick = async function() {
            toggleVisibility();
        };
    }

    async getFeatures() {
        let downloadLogs = async function() {
            const response = await fetch("/features/all", { headers: { 'Authorization': `Bearer ${sessionStorage.getItem('token')}`}});
            const body = await response.json();
            this.setState({ features: body })
            return body
        }
        downloadLogs = downloadLogs.bind(this)

        await downloadLogs()
    }

    render() {
        let selectedFeature = this.state.features.find(element => element.id == sessionStorage.getItem("selectedFeature"))
        let name = (selectedFeature !== undefined) ? selectedFeature.path : "None"

        return (
            <div className="relative inline-block text-left">
                <div>
                    <button id={`dropdownFeatureButton`} className="inline-flex justify-center w-full rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-100 focus:ring-indigo-500" aria-expanded="true" aria-haspopup="true">
                        {name}
                        <svg className="-mr-1 ml-2 h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"
                             fill="currentColor" aria-hidden="true">
                            <path fillRule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clipRule="evenodd"/>
                        </svg>
                    </button>
                </div>

                <div
                    className={`origin-top-right ${this.state.isVisible ? 'visible' : 'invisible'} absolute left-0 mt-2 w-56 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none`} role="menu" aria-orientation="vertical" aria-labelledby="menu-button" tabIndex="-1">
                    <div className="py-1" role="none">
                        {this.state.features.map(row =>
                            <a key={row.id} href="#" className={`${sessionStorage.getItem("selectedFeature") == row.id ? 'font-bold' : 'font-normal'} text-gray-700 block px-4 py-2 text-sm hover:bg-gray-50`} role="menuitem" tabIndex="-1" id={`feature${row.id}`}>{row.path}</a>
                        )}
                    </div>
                </div>
            </div>
        )
    }
}

export default FeatureSelector;