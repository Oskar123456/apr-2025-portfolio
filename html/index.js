let records = []
let recordsToDownload = []

function reflectionFinalSubmit(e) {
  e.preventDefault();

  console.log("SENDING: " + JSON.stringify(records))

  fetch("http://localhost:9999/records", {
    method: "POST",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/json",
    },
    body: JSON.stringify(records),
  })
    .then((res) => {
      if (res.status === 200) return res.json();
      else throw new Error("Something went wrong");
    })
    .then((json) => {
      console.log(JSON.stringify(json));
      records = []

      const submissionsList = document.getElementById("submissions-list");
      const submissionsListToDL = document.getElementById("submissions-list-to-dl");
      submissionsListToDL.innerHTML = submissionsList.innerHTML;
      submissionsList.innerHTML = ""
    });
}

function reflectionFormSubmit(e) {
  e.preventDefault();

  const formData = new FormData(e.target);

  let kvs = new Map();

  for (let e of formData.entries()) {
    addPropToClass(kvs, e[0], e[1])
  }

  console.log("Submitting: " + JSON.stringify(kvs))

  records.push(kvs)

  const submissionsList = document.getElementById("submissions-list");
  let li = document.createElement("li")
  li.innerHTML = JSON.stringify(kvs)
  submissionsList.appendChild(li)
}

function addPropToClass(map, name, value) {
  let nameParts = name.split(".");

  console.log("adding " + name)

  if (nameParts.length == 1) {
    map[name] = value;
    return;
  }

  let obj = map[nameParts[0]];
  if (!obj) {
    map[nameParts[0]] = {}
    console.log("map does not have " + nameParts[0])
    obj = map[nameParts[0]];
  }

  for (let i = 1; i < nameParts.length; ++i) {
    let namePart = nameParts[i];

    if (i + 1 == nameParts.length) {
      obj[namePart] = value;
      return;
    }

    if (!(namePart in obj)) {
      obj[namePart] = {}
      console.log(obj + " does not have " + namePart)
    } else {
      console.log(obj + " does have " + namePart)
    }

    obj = obj[namePart];
  }
}
