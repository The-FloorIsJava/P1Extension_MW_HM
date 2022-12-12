const url = "http://localhost:8081/"

async function viewReimbursements(){

    try {
    const response = await fetch(`${url}reimbursements`,{
        method: "GET",
        headers: {
            authorization: window.localStorage.getItem("token")
        }
    });

    const users = await response.json();

    console.log(reimbursemnts);
    let html_code = "<tr>"
    users.map(element =>{
        for(const key in element){
            html_code += `<td>${element[key]}</td>`
        }
        html_code += "</tr>"
        console.log(html_code)
    })

    document.getElementById("reibursementTable").innerHTML = html_code
} catch(error){
    console.error(error);
}
}