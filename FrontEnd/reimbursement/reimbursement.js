let requestBlank = document.getElementById("request-form").innerHTML
const url = "http://localhost:8081/"

function submitRequest(form){
    let amount = form.amount.value;
    let description = form.description.value;

    fetch(`${url}reimbursement`, {
        method: 'POST',
        headers: {
            authorization: window.localStorage.getItem("token")
        },
        body: JSON.stringify({
            amount: amount,
            description: description
            })
        })
    .then(response => {
        console.log(response)
        console.log(response.status)
        if(response.status === 400 | response.status === 401){
            throw new Error(response.text().then(body => console.log(body)))
        }
        console.log(...response.headers)
        document.getElementById("request-form").innerHTML = `${requestBlank} <h4 id='complete'>Reimbursement Ticket Submitted.</h4>`

    })
    .catch(error  => {
        console.error(error)
        document.getElementById("request-form").innerHTML = `${requestBlank} <h4>Ticket Submission Failed. Please Try Again.</h4>`
    })
}