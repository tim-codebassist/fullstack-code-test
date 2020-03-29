const listContainer = document.querySelector('#service-list');
let servicesRequest = new Request('/service');
fetch(servicesRequest)
.then(function(response) { return response.json(); })
.then(function(serviceList) {
  serviceList.forEach(service => {
    var li = document.createElement("li");
    li.appendChild(document.createTextNode(service.url + ' ' + service.name + ': ' + service.status));
    var button = document.createElement('BUTTON')
    button.appendChild(document.createTextNode('Delete'))
    addDeleteOnClick(button, service.url)
    li.appendChild(button)
    listContainer.appendChild(li);
  });
});

const saveButton = document.querySelector('#post-service');
saveButton.onclick = evt => {
    let url = document.querySelector('#url').value;
    let name = document.querySelector('#name').value;
    fetch('/service', {
    method: 'post',
    headers: {
      'Accept': 'application/json, text/plain, */*',
      'Content-Type': 'application/json'
    },
  body: JSON.stringify({url:url,name:name})
}).then(res=> location.reload());
}

function addDeleteOnClick(button, url) {
    button.onclick = evt => {
        fetch('/service', {
          method: 'delete',
          headers: {
            'Accept': 'application/json, text/plain, */*',
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({url:url})
        }).then(res=> location.reload());
      }
}