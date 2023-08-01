class Logger {

    static log(title, description, page, parameters) {
        const object = {
            name: title,
            description: description,
            operatingSystem: "macOS 12.1",
            browser: "Safari",
            locale: "en-US",
            webVersion: "0.0.1",
            page: page,
            parameters: {},
            project: 2
        }

        fetch("/logs/web/log", {
            method: "POST",
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(object)
        }).then(response =>
            console.log(response)
        ).catch(error => {
            console.log('found error', error)
        });
    }
}

export default Logger;