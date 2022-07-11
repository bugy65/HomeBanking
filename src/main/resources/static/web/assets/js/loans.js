Vue.createApp({
    data() {
        return {

            firstName: "",
            lastName: "",
            datos: "",
            clientes: "",
            dataAccount: "",
            firstName: "",
            loansAccount: "",

            payments1: "",
            loans1: "",

            cuentaDestino: "",
            idloan: 0,
            error: "",

            dues: [],
            due: [],
            duesPayments: [],
            dueSeleccionado: 0,
            amount: 0,
            changer1: true,
            changer2: false,
            plus:0,
        };
    },

    created() {
        axios.get(`https://skyhomebanking.herokuapp.com/api/clients/current`).then((data) => {
            this.datos = data;
            this.clientes = this.datos.data;
            this.dataAccount = this.clientes.accounts;
            this.firstName = this.clientes.firstName;
            this.lastName = this.clientes.lastName;
            this.loansAccount = this.clientes.loans






            console.log(this.dataAccount)


        });
        axios.get(`/api/loans`).then((dataloan) => {
            this.loans1 = dataloan.data;

            this.dues = this.loans1.filter((item) => item.name == "Hipotecario")

            console.log(this.dues)
            console.log(this.loans1)
            console.log(this.duesPayments)
        });


    },

    methods: {
        logOut() {

            axios.post('/api/logout').then(response => location.href = "https://skyhomebanking.herokuapp.com/web/index.html")
            console.log("soy puto")
        },

        filter() {
            this.dues = this.loans1.filter((item) => item.id == this.idloan)
            this.duesPayments = this.dues[0].payments
            this.cuotaAgregada = this.amount / dueSeleccionado * 1.2
        },

        solicitarLoan() {
            Swal.fire({
                title: `estas seguro de solicitar $${this.amount}?`,
                text: "no se puede deshacer los cambios!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Confirm!'
            }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire({
                        title: `Espera un momento se esta procesando...`,
                        icon: 'question',
                        showConfirmButton: false,
                    })
                    let applyForLoan = {
                        loanId: this.idloan,
                        amount: this.amount,
                        payments: this.dueSeleccionado,
                        accountNumber: this.cuentaDestino
                    }
                    axios.post("/api/loans", applyForLoan)
                        .then((data)=> {console.log(data)})
                        .catch(function (error) {
                            this.error = error.response.data
                            console.log(this.error)
                        })
                        .then(
                            setTimeout(function () {
                                if (this.error == "Hay campos sin completar" || this.error =="El tipo Prestamo no existe" || this.error =="excede el maximo permitido" || this.error =="las cuotas agregadas no estan disponibles" || this.error =="La cuenta no existe" || this.error =="La cuenta no es tuya" || this.error =="Ya hay un tipo prestamo solicitado") {
                                    Swal.fire({
                                        title: `${this.error}`,
                                        icon: 'error',
                                    })
                                } else {
                                    Swal.fire({
                                        title: 'Se confirmo la solicitud del prestamo correctamente',
                                        icon: 'success',
                                        showConfirmButton: false,
                                    }
                                    )
                                    setTimeout(function(){
                                        location.reload()
                                    }, 2500)
                                }
                            }, 2500)
                        )
                }
            })
        }
    },

    computed: {
        Plus(){
            if(this.amount !=0 && this.dueSeleccionado !=0){
                this.plus= ((this.amount / this.dueSeleccionado) *1.2).toFixed(2)
            }
            return this.plus
        }
    },
}).mount("#app");