var app = new Vue({
    el: '#app',
    data: {
        queryType: '',
        intervalType: '',
        interval: '',
        returned: [],
        error_console: '',
        fields: ['"userName"', '"aggregatedValue"'],
        showDismissibleAlert: false,
        emitterStatus: ''
    },
    methods: {
        controlEmitter: function (action) {
            const instance = axios.create({
                baseURL: 'http://localhost:8081',
                timeout: 1000,
                headers: {'Access-Control-Allow-Origin': 'true'}
            });
            var app = this;
            instance.get('/emitter/' + action)
                .then(function (response) {
                    // handle success
                    console.log(response);
                    if (response.data != "") {
                        app.emitterStatus = response.data;
                    } else {
                        app.controlEmitter('status')
                    }
                })
                .catch(function (error) {
                    // handle error
                    console.log(error);
                })
                .then(function () {
                    // always executed
                });
        },
        performQuery: function (queryType, intervalType, interval) {
            var base;
            var app = this;
            var query;
            if (queryType == 'aggregations') {
                base = 'http://localhost:8084';
                query = 'analytics/query/aggregate/' + interval + '/' + intervalType;
            } else if (queryType == 'transactions') {
                base = 'http://localhost:8084';
                query = 'analytics/query/all/' + interval + '/' + intervalType;
            }
            else if (queryType == 'persistence') {
                query = 'analytics/persist/aggregate/' + interval + '/' + intervalType;
                base = 'http://localhost:8084';
            } else if (queryType == 'persistedAgg') {
                query = 'analytics/query';
                base = 'http://localhost:8085';
            }
            const instance = axios.create({
                baseURL: base,
                timeout: 100000,
                headers: {'Access-Control-Allow-Origin': 'true'}
            });
            instance.get(query)
                .then(function (response) {
                    if (queryType === 'persistence') {
                        app.returned = [];
                    } else {
                        app.returned = response.data;
                    }
                    showDismissibleAlert = false;
                })
                .catch(function (error) {
                    // handle error
                    console.log(error);
                    app.returned = [];
                    app.error_console = error;
                    app.showDismissibleAlert = true;
                })
                .then(function () {
                    app.queryType = '';
                    app.intervalType = '';
                    app.interval = ''
                });
        }
    }
});
app.emitterStatus = app.controlEmitter('status');