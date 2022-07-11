Vue.createApp({
    data() {
        return {

            firstName: "",
            lastName: "",
            datos: "",
            clientes: "",
            dataAccount: "",
            firstName: "",

            cuentaOrigen: "",
            cuentaDestino: "",

            cuentaDeTerceros: "",
            descripcion: "",
            balance: 0,
            amount: 0,
            changer1: true,
            changer2: false,
        };
    },

    created() {
        axios.get(`https://skyhomebanking.herokuapp.com/api/clients/current`).then((data) => {
            this.datos = data;
            this.clientes = this.datos.data;
            this.dataAccount = this.clientes.accounts;
            this.firstName = this.clientes.firstName;
            this.lastName = this.clientes.lastName;




            console.log(this.dataAccount)

        });


    },

    methods: {
        logOut() {

            axios.post('/api/logout').then(response => location.href = "https://skyhomebanking.herokuapp.com/web/index.html")
            console.log("soy puto")
        },

        own() {
            this.changer1 = true;
            this.changer2 = false;
        },
        another() {
            this.changer1 = false;
            this.changer2 = true;
            console.log(this.changer1)
            console.log(this.changer2)
        },
        transfer() {
            Swal.fire({
                title: `Estas seguro de trasnferir dinero a ${this.cuentaDestino}?`,
                text: "no se puede deshacer los cambios!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Confirm!'
            }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire(
                        'Transferencia enviada!',
                        `Se enviaron $${this.amount}`
                    )


                    axios.post("/api/transactions", `amount=${this.amount}&description=${this.descripcion}&rootAccount=${this.cuentaOrigen}&destinationAccount=${this.cuentaDestino}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } }
                    ).then(setTimeout(function(){
                        window.location.reload(1);
                     }, 1000))

                }
            })
        },
        transferCT() {
            Swal.fire({
                title: `Estas seguro de trasnferir dinero a ${this.cuentaDeTerceros}?`,
                text: "no se puede deshacer los cambios!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Confirm!'
            }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire(
                        'Transferencia enviada!',
                        `Se enviaron $${this.amount}`
                    )


                    axios.post("/api/transactions", `amount=${this.amount}&description=${this.descripcion}&rootAccount=${this.cuentaOrigen}&destinationAccount=VIN-${this.cuentaDeTerceros}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } }
                    ).then(setTimeout(function(){
                        window.location.reload(1);
                     }, 1000))

                }
            })
        },
    },

    computed: {},
}).mount("#app");