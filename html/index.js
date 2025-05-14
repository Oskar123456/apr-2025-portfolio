
function reflectionFormSubmit(e) {
    e.preventDefault()

    const formData = new FormData(e.target)

    let kvs = new Map();

    for (let e of formData.entries()) {
        kvs[e[0]] = e[1]
    }

    fetch('http://localhost:9999/records', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(kvs)
    }).then(res => {
        if (res.status === 200)
            return res.json()
        else
            throw new Error("Something went wrong")
    }).then(json => {
        console.log("succesfully created object: " + kvs["className"] + " = " + JSON.stringify(json))
    })
}
