export default {

    getArticles() {
        return fetch("/api/discount/articles")
            .then(response => {
                if (response.status >= 400) {
                    throw response
                }
                return response.json();
            })
    }

}