    // Get a reference to the canvas element
    document.addEventListener("DOMContentLoaded", function() {
// JavaScript code for updating the chart


var ctx = document.getElementById('stockChart').getContext('2d');

// Initial data for the chart
var initialData = {
    labels: [],
    datasets: [{
        label: 'Stock Price',
        data: [],
        borderColor: 'blue',
        borderWidth: 1,
        fill: false,
        lineTension: 0, // Set lineTension to 0 for a linear line
    }]
};

var stockChart = new Chart(ctx, {
    type: 'line',
    data: initialData,
    options: {
        scales: {
            x: {
                type: 'time',
                time: {
                    unit: 'second',
                },
                title: {
                    display: true,
                    text: 'Time',
                },
            },
            y: {
                title: {
                    display: true,
                    text: 'Price',
                },
            },
        },
    },
});

function formatTimestamp(timestamp) {
    const date = new Date(timestamp);
    const hours = date.getHours();
    const minutes = date.getMinutes();
    const seconds = date.getSeconds();
    return `${hours}:${minutes}:${seconds}`;
}

function updateChart(currentPrice) {
    // Format the timestamp and add it to the labels
    const formattedTimestamp = formatTimestamp(new Date());
    stockChart.data.labels.push(formattedTimestamp);
    stockChart.data.datasets[0].data.push(currentPrice);

    // Limit the number of data points to display (e.g., show the last 10 points)
    var maxDataPoints = 20;
    if (stockChart.data.labels.length > maxDataPoints) {
        stockChart.data.labels.shift();
        stockChart.data.datasets[0].data.shift();
    }

    // Update the chart
    stockChart.update();
}


// Function to update the current price
function updateCurrentPrice() {
    $.get("/getCurrentPrice", function(data) {
        // Update the current price on the page
        $("#currentPrice").text("Current Stock Price: " + data.currentPrice);

        // Log the type of currentPrice
        //console.log("Type of currentPrice: " + typeof data.currentPrice);

        // Add the new stock price to the chart
        updateChart(data.currentPrice);
    });
}



 // Handle adding new agents
const addAgentButton = document.getElementById("addAgentButton");
addAgentButton.addEventListener("click", function () {
    const newAgentBalance = parseFloat(document.getElementById("newAgentBalance").value);
    const newAgentBuyingPriceOffer = parseFloat(document.getElementById("newAgentBuyingPriceOffer").value);


    console.log("New Agent Balance:", newAgentBalance);
    console.log("New Agent Buying Price Offer:", newAgentBuyingPriceOffer);
    // Create an object to hold the agent data
    const newAgentData = {
        newAgentBalance: newAgentBalance,
        newAgentBuyingPriceOffer: newAgentBuyingPriceOffer,
    };

    // Send a POST request to add the new agent (you need to implement this logic)
    fetch("/addAgent", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(newAgentData),
    })
    .then(response => {
        if (response.ok) {
            // Agent added successfully, you can update the UI or perform any other actions
            console.log("Agent addition successful");
        } else {
            // Handle errors or display an error message
            console.error("Agent addition failed");
        }
    })
    .catch(error => {
        // Handle network errors
        console.error("Error:", error);
    });
});


    // Handle submitting the initialize data form
    const initializeDataForm = document.getElementById("initializeDataForm");
    initializeDataForm.addEventListener("submit", function (event) {
        event.preventDefault();

        const stocks = parseInt(document.getElementById("stocks").value);
        const initialPrice = parseFloat(document.getElementById("initialPrice").value);
        const balance = parseFloat(document.getElementById("balance").value);
        const buyingPriceOffer = parseFloat(document.getElementById("buyingPriceOffer").value);

        console.log("Form Data:", stocks, initialPrice, balance, buyingPriceOffer);

        // Submit the form (you need to implement this logic)
        // For example, you can use an AJAX request to send the data to the server and handle it in your controller
            // Create an object to hold the data
            const requestData = {
                stocks: stocks,
                initialPrice: initialPrice,
                balance: balance,
                buyingPriceOffer: buyingPriceOffer
            };

            // Send an AJAX POST request to the server to handle the form submission
            fetch("/initializeData", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(requestData)
            })
            .then(response => {
                if (response.ok) {
                    // Data initialization successful, you can update the UI or take other actions
                    console.log("Data initialization successful");



                    fetch("/simulate", {
                        method: "POST", // Use POST or GET based on your server implementation
                        headers: {
                            "Content-Type": "application/json",
                        },
                    })
                    .then(response => {
                        if (response.ok) {
                            // Simulation started successfully
                            console.log("Simulation started successfully");
                        } else {
                            // Handle errors or display an error message
                            console.error("Simulation failed to start");
                        }
                    })
                    .catch(error => {
                        // Handle network errors
                        console.error("Error:", error);
                    });




                    setInterval(updateCurrentPrice, 1000); // Adjust the interval (in milliseconds) as needed
                    document.getElementById("chartContainer").style.display = "block";
                    document.getElementById("addAgentContainer").style.display = "block";
                    document.getElementById("currentPrice").style.display = "block";
                    document.getElementById("initializationFormContainer").style.display = "none";

                } else {
                    // Data initialization failed, handle the error

                    console.error("Data initialization failed");
                }
            })
            .catch(error => {
                // Handle any network or other errors
                console.error("Error:", error);
            });
    });
    });
