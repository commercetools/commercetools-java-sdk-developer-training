function handleResponse(response) {
    if (response.status === 501) {
        return response.json().then(error => {
            throw new Error('The function is not implemented on the server.');
        });
    }
    if (response.status === 404) {
                throw new Error('Not found.');
        }
    if (!response.ok) {
        return response.json().then(error => {
            throw new Error(error.message);
        });
    }
    return response.json();
}

function getStores() {
    const url = '/api/stores';
    fetch(url)
            .then(handleResponse)
            .then(stores => {
                if (stores.results && stores.results.length > 0) {
                    const storesKeyValue = stores.results.reduce((storesInfo, store) => {
                      storesInfo[store.key] = store.name['en-US'];
                      return storesInfo;
                    }, {});
                    localStorage.setItem('storesList', JSON.stringify(storesKeyValue));

                } else {
                    localStorage.removeItem('storesList');
                }
            })
            .catch((error) => {
                console.error('Error:', error);
            });
}