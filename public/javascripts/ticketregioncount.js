var labels = document.getElementById("barchart5").getAttribute("data-labels").split(",");
   var data = document.getElementById("barchart5").getAttribute("data-data").split(",");
   var ctx = document.getElementById("ticketregioncount").getContext('2d');
   var coloR = [];
   var dynamicColors = function() {
               var r = Math.floor(Math.random() * 255);
               var g = Math.floor(Math.random() * 255);
               var b = Math.floor(Math.random() * 255);
               return "rgb(" + r + "," + g + "," + b + ")";
            };
    for (var i in data) {
                coloR.push(dynamicColors());
             }
   var myChart = new Chart(ctx, {
      type: 'bar',
      data: {
          labels: labels,
          datasets: [{
              label: 'Tickets By Region',
              data: data,
   backgroundColor: coloR ,
              borderColor: coloR ,


              borderWidth: 3
          }]
      },
      options: {
      responsive: false,
          scales: {
              yAxes: [{
                  ticks: {
                      beginAtZero:true
                  }
              }]
          }
      }
   });