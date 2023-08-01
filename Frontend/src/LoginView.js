import React, {Component} from "react";

class LoginView extends Component {

    state = {
        isRegisterMode: false,
    }

    componentDidMount() {
        let actionButton = document.getElementById(`actionButton`);
        let alternativeButton = document.getElementById(`alternativeButton`);

        let switchMode = function() {
            this.setState(prevState => ({ isRegisterMode: !prevState.isRegisterMode }))
        };
        switchMode = switchMode.bind(this)

        alternativeButton.onclick = function () {
            switchMode()
        }

        let makeAPICall = async function() {
            let usernameField = document.getElementById("usernameTextfield");
            let passwordField = document.getElementById("passwordTextfield");
            let username = usernameField.value
            let password = passwordField.value

            if (this.state.isRegisterMode) {
                await this.register(username, password)
                this.setState({isRegisterMode: false})
            } else {
                await this.signIn(username, password)
            }
        };
        makeAPICall = makeAPICall.bind(this)

        actionButton.onclick = async function () {
            await makeAPICall()
        }
    }

    async signIn(username, password) {
        const response = await fetch("http://localhost:8080/login", {
            method: "POST",
            headers: {'Content-Type': 'application/json', 'ProjectId': 0},
            body: JSON.stringify({
                username: username,
                password: password,
            })
        })
        const status = response.status
        if (status !== 200) {
            alert("Invalid credentials. Please try again!")
            return false
        } else {
            const json = await response.json()

            if (json.role === "ROLE_ADMIN") {
                sessionStorage.setItem('token', json.token);
                window.location.href = "analytics"
            } else {
                alert("Access is restricted to Admins")
            }
        }
    }

    async register(username, password) {
        sessionStorage.removeItem('token');
        const response = await fetch("/user/add", {
            method: "POST",
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                username: username,
                password: password,
                enabled: true,
                projectid: 0,
                roles: "ROLE_ADMIN"
            })
        })
        const status = await response.status
        console.log(status)
        if (status === 200) {
            alert("Successfully registered.")
        } else {
            alert("An error occured, please try again!")
        }
    }

    render() {
        return (
            <div className="h-full w-full flex items-center justify-center py-16 px-4 sm:px-6 lg:px-8">
                <div className="max-w-md w-full space-y-8">
                    <div>
                        <h1 className="text-center text-3xl font-bold font-mono text-medium block py-2 pl-3 pr-4 text-sky-500 border-b border-gray-100 md:border-0 md:p-0">
                            mBaaS
                        </h1>
                        <h3 className="text-center font-bold text-small block py-2 pl-3 pr-4 border-b border-gray-100 md:border-0 md:p-0">
                            Admin Dashboard
                        </h3>
                        <h2 className="mt-6 text-center text-xl font-extrabold text-gray-900">
                            {this.state.isRegisterMode ? "Register" : "Sign In"}
                        </h2>
                    </div>
                        <input type="hidden" name="remember" value="true"/>
                        <div className="rounded-md shadow-sm -space-y-px">
                            <div>
                                <input id="usernameTextfield" className="relative block w-full  mb-4shadow appearance-none border rounded-t-md w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" type="text" placeholder="Username"/>
                            </div>
                            <div>
                                <input id="passwordTextfield" type="password" className="relative block mb-4shadow appearance-none border rounded-b-md w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" placeholder="Password"/>
                            </div>

                        </div>
                        <div className={"space-y-3"}>
                            <button id="actionButton" className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-sky-500 hover:bg-sky-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                                {this.state.isRegisterMode ? "Register" : "Sign In"}
                            </button>
                            <button id="alternativeButton" className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-sky-500 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                                {this.state.isRegisterMode === false ? "Register" : "Sign In"}
                            </button>
                        </div>
                </div>
            </div>
        );
    }
}

export default LoginView;