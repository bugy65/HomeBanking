Vue.createApp({
    data() {
        return {

            email: "",
            password: "",
            error: false,
            error_msg: "",

            firstName: "",
            lastName:"",
            emailRegister: "",
            passwordRegister: "",
        };
    },

    created() {
        //   const urlParams = new URLSearchParams(window.location.search)
        //   const id = urlParams.get('id')

        

    },

    methods: {
        login() {

            axios.post('/api/login', `email=${this.email}&password=${this.password}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } }
            ).then(response => location.href= "/web/dashboard.html").then(response => console.log('signed in!!!'))


            .catch(function (error) {
                if (error.response) {
                  // The request was made and the server responded with a status code
                  // that falls out of the range of 2xx
                  console.log(error.response.data);
                  console.log(error.response.status);
                  console.log(error.response.headers);
                } else if (error.request) {
                  // The request was made but no response was received
                  // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
                  // http.ClientRequest in node.js
                  console.log(error.request);
                } else {
                  // Something happened in setting up the request that triggered an Error
                  console.log('Error', error.message);
                }
                console.log(error.config);
              });
        },
        Register() {

            axios.post('/api/clients',`firstName=${this.firstName}&lastName=${this.lastName}&email=${this.emailRegister}&password=${this.passwordRegister}`,{headers:{'content-type':'application/x-www-form-urlencoded'}}
            ).then(response => axios.post('/api/login', `email=${this.emailRegister}&password=${this.passwordRegister}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } }
            ).then(response => location.href= "/web/dashboard.html"))


            .catch(function (error) {
                if (error.response) {
                  // The request was made and the server responded with a status code
                  // that falls out of the range of 2xx
                  console.log(error.response.data);
                  console.log(error.response.status);
                  console.log(error.response.headers);
                } else if (error.request) {
                  // The request was made but no response was received
                  // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
                  // http.ClientRequest in node.js
                  console.log(error.request);
                } else {
                  // Something happened in setting up the request that triggered an Error
                  console.log('Error', error.message);
                }
                console.log(error.config);
              });
        },


        
        singUp(){
            document.querySelector('.img__btn').addEventListener('click', function() {
                document.querySelector('.cont').classList.toggle('s--signup');
              });
        }
    },

    computed: {},
}).mount("#app");