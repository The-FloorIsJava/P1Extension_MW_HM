let loginBlank = document.getElementById("login-form").innerHTML
const url = "http://localhost:8081/"

function userLogin(form){
    let username = form.username.value;
    let password = form.password.value;

    fetch(`${url}login`, {
        method: 'POST',
        body: JSON.stringify({
            userName: username,
            password: password
        })
    })
    .then(response => {
        console.log(response)
        console.log(response.status)
        if(response.status === 401){
            throw new Error(response.text().then(body => console.log(body)))
        }
        console.log(...response.headers)
        document.getElementById("login-form").innerHTML = 
        "<h4 id='welcome'>Welcome to the User Reimbursement System, " + username + "</h4>" +
        "<a href='../allUsers/allUsers.html'>Users</a><br />" +
        "<a href='../reimbursement/reimbursement.html'>New Reimbursement Request</a>"
        console.log(password)
        logoutButton()
        window.localStorage.setItem("token", response.headers.get("authorization"))
    })
    .catch(error => {
        console.error(error)
        document.getElementById("login-form").innerHTML = `${loginBlank} <h4>Login Failed. Please try again.</h4>`
    })
}

function logoutButton(){
    let button = document.createElement("button")
    let node = document.createTextNode("Logout")

    button.appendChild(node)
    button.setAttribute("onclick", "logout()")
    const welcomeH1 = document.getElementById("welcome")
    welcomeH1.appendChild(button)
}

function logout(){
    document.getElementById("login-form").innerHTML = loginBlank;
}