const url = "http://localhost:8080/"

async function viewUsers(){

    try {
    const response = await fetch(`${url}user`,{
        method: "GET",
        headers: {
            authorization: window.localStorage.getItem("token")
        }
    });

    const users = await response.json();

    console.log(users);
    let html_code = "<tr>"
    users.map(element =>{
        for(const key in element){
            html_code += `<td>${element[key]}</td>`
        }
        html_code += "</tr>"
        console.log(html_code)
    })

    document.getElementById("userTable").innerHTML = html_code
} catch(error){
    console.error(error);
}
}