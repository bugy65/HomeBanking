const app = Vue.createApp({
    data() {
        return {
            datos: [],
            clientes: [],
            account: [],
            dataCards: [],
            firstName: "",
            lastName: "",
            email: "",
            creationdate: "",
            balance: "",
            firstnameEdit: "",
            lastnameEdit: "",
            emailEdit: "",
            urlClient: "",
            transactionsDTO1: [],
            clientesSeleccionados: {},



            dataVacia: "",
            dataAccount: [],
            accNumber: [],
            dataBalance: [],
            transactions: [],
            loans1: [],
            datosTarjetas: [],
            Tarjetas: [],

            debit: [],
            credit: [],
            oneCard: "",

            typeSelector: "",
            accountsActive: [],



            accountTypeSelected: "",
            oneAccount: "",

            accountSelect: "",
            sinceDate: "",
            untilDate: "",
            pelotudofunciona:[],
        };
    },

    created() {//van a cargarse cuando carge la pestaña


        this.LoadAccounts()


        this.loadClients()


        this.loadCards()

        
        



    },

    methods: {//siempre esperan a ser llamados

       

        LoadAccounts() {
            axios.get(`/api/clients/current/accounts`).then((dataAcc) => {
                this.account = dataAcc.data;
                this.accNumber = this.accountsActive.number
                this.dataBalance = this.accountsActive.balance
                // this.transactionsDTO1 = this.account[0].transactionsDTO[0].account.transactions

                console.log(this.account)

                this.accountsActive = this.account.filter(perAccount => perAccount.activeAccount == true)
                console.log(this.accountsActive)


            });
        },
        

        loadClients() {
            axios.get(`/api/clients/current`).then((data) => {
                this.datos = data;
                this.clientes = this.datos.data;
                this.dataAccount = this.clientes.accounts;
                this.firstName = this.clientes.firstName;
                this.lastName = this.clientes.lastName;
                this.loans1 = this.clientes.loans
                this.transacciones= this.dataAccount.transactionsDTO
                
                // this.dataAccount.map(cuenta => this.pelotudofunciona.push(cuenta.transactionsDTO))
                this.transaccionesXD = this.dataAccount[0].transactionsDTO
                

                
                console.log("AAAAAAAAAAAAAAA")
                console.log(this.transaccionesXD )
                console.log(this.dataAccount)

            });
        },

    

        loadCards() {
            axios.get(`/api/clients/current`).then((dataCards) => {
                this.datosTarjetas = dataCards.data.cards;
                this.debit = this.datosTarjetas.filter(cardDebit => cardDebit.enabled == true && cardDebit.type === "DEBIT")
                this.credit = this.datosTarjetas.filter(cardDebit => cardDebit.enabled == true && cardDebit.type === "CREDIT")
                // this.Tarjetas = dataCards.data._embedded.cards.sort((a, b) => a.id - b.id);


                console.log(this.debit);
                console.log(this.credit);
                console.log(this.datosTarjetas);
                console.log(this.datosTarjetas);
            })
        },

        selectorAccountType(type) {
            this.accountTypeSelected = type
            console.log(this.accountTypeSelected)
        },

        dateTransform(dateImput) {
            const date = new Date(dateImput)
            return date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear() + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds()
        },
        dateTransformCrads(dateImput) {
            const date = new Date(dateImput)
            const year = date.getFullYear().toString()
            const yearTwo = year.substr(year.length - 2)
            return (date.getMonth() + 1) + "/" + yearTwo
        },
        logOut() {

            axios.post('/api/logout').then(response => location.href = "/web/index.html")
            console.log("soy puto")
        },

        addAccount() {
            axios.post(`/api/clients/current/accounts`, `accountNumber=${this.accountTypeSelected}`).then(response => location.href = "/web/dashboard.html")
        },


        selector(color) {
            this.typeSelector = color
            console.log(this.typeSelector)
        },
        addCreditCard() {
            axios.post('/api/clients/current/cards', `type=CREDIT&color=${this.typeSelector}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } }).then(response => console.log('registered credit card'))
        },
        addDebitCard() {
            axios.post('/api/clients/current/cards', `type=DEBIT&color=${this.typeSelector}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } }).then(response => console.log('registered debit card'))
        },

        goToTransferspage() {
            location.href = "/web/transfers.html"
        },

        // deleteCards(tarjeta){
        //     this.oneCard = tarjeta.number
        //     axios.post('http://localhost:8080/api/cards/delete', `cardNumber=${this.oneCard}`).then(response => console.log("eliminada"))

        // },



        compareDates(tarjeta) {
            //today
            let time = new Date().getTime();
            // card
            let dateCard = new Date(tarjeta).getTime();
            let difference = dateCard - time
            let toOvercome = 5270388157

            if (difference < toOvercome) {
                if (dateCard < time) {
                    return this.message = "Expired Card"
                } else {
                    return this.message = "Card about to expire"
                }
            }
            else {
                return this.message = "Valid Card"
            }
        },


        getExpiresDate(dateImput) {
            const date = new Date(dateImput)
            const dateYear = date.getFullYear()
            const getTwoDigits = dateYear.toString().substring(2)
            return (date.getMonth() + 1) + "/" + getTwoDigits
        },

        createAccount() {
            const inputOptions = new Promise((resolve) => {
                resolve({
                    'SAVINGS': 'Savings Account',
                    'CURRENT': 'Current Account',
                })
            })
            const { value: typeAccount } = Swal.fire({
                title: 'Select Type Account',
                input: 'radio',
                inputOptions: inputOptions,
                inputValidator: (value) => {
                    if (!value) {
                        return 'You need to choose something!'
                    }
                }
            })
                .then((result) => {
                    if (result.isConfirmed) {
                        axios.post(`/api/clients/current/accounts`, `accountType=${result.value}`)
                            .catch(function (error) {
                                this.error = error.response.data
                                console.log(this.error);
                            })
                            .then(response => {
                                if (this.error == "Forbidden, already has 3 accounts") {
                                    Swal.fire({
                                        title: "Error",
                                        text: `${this.error}`,
                                        icon: "error"
                                    })
                                    this.error = ""
                                } else {
                                    Swal.fire({
                                        title: "Create Account",
                                        text: "Account Created",
                                        icon: "success"
                                    })
                                    setTimeout(function () {
                                        location.reload()
                                    }, 1000)
                                }
                            })
                    }
                }

                )
        },

        deleteAccount(accountSelected) {

            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
            }).then((result) => {
                if (result.isConfirmed) {
                    this.oneAccount = accountSelected.number
                    axios.post('/api/accounts/delete', `accountNumber=${this.oneAccount}`)
                        .catch(function (error) {
                            this.error = error.response.data
                        })
                        .then(
                            setTimeout(function () {
                                if (this.error == "Account has a positive balance, it cannot be deleted" || this.error == "The client has an active loan. Accounts cannot be deleted until they are paid in full" || this.error == "Account does not belong to authenticated client") {
                                    Swal.fire({
                                        title: "Error",
                                        text: `${this.error}`,
                                        icon: "error"
                                    })
                                    this.error = ""
                                } else {
                                    Swal.fire({
                                        title: "Account deleted",
                                        text: "Account deleted",
                                        icon: "success"
                                    })
                                    setTimeout(function () {
                                        location.reload()
                                    }, 1000)
                                }
                            }, 1000))
                }
            })
        },

        deleteCard(tarjeta) {
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
            }).then((result) => {
                if (result.isConfirmed) {
                    this.oneCard = tarjeta.number
                    axios.post('/api/cards/delete', `cardNumber=${this.oneCard}`)
                        .catch(function (error) {
                            this.error = error.response.data
                        })
                        .then(
                            setTimeout(function () {
                                if (this.error == "Card does not exist" || this.error == "Card does not belong to authenticated client") {
                                    Swal.fire({
                                        title: "Error",
                                        text: `${this.error}`,
                                        icon: "error"
                                    })
                                    this.error = ""
                                } else {
                                    Swal.fire({
                                        title: "Delete card ",
                                        text: "Deleted card",
                                        icon: "success"
                                    })
                                    setTimeout(function () {
                                        location.reload()
                                    }, 1000)
                                }
                            }, 1000))
                }
            })




            // this.oneCard = card.number
            // axios.post('/api/cards/delete', `cardNumber=${this.oneCard}`)
            //     .then(response => {
            //         setTimeout(function () {
            //             location.reload()
            //         }, 1000)
            //     })

        },




        downloadPDF(account) {
            let NewPDF = {
                numberAccount: account.number,
                since: this.sinceDate,
                until: this.untilDate
            }
            console.log(this.sinceDate)
            console.log(this.untilDate)
            console.log(account.number)
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
            }).then((result) => {
                if (result.isConfirmed) {
                    this.accountSelect = account.number

                    axios.post('/api/transactions/generate', NewPDF, { 'responseType': 'blob' })
                        .then(response => {
                            let url = window.URL.createObjectURL(new Blob([response.data]))
                            let link = document.createElement("a")
                            link.href = url
                            link.setAttribute("download", `Resume.pdf`)
                            document.body.appendChild(link)
                            link.click()
                        })
                }


            })
        }
    },

    computed:  {},//siempre se ejecutan constantemente las funcione, solo se llaman una vez
}).mount("#app");//se va a montar en el elemento que tenga el id app

