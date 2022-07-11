Vue.createApp({
    data() {
        return {
            datos: [],
            
            urlClient: "",
            transactionsDTO1: [],
            clientesSeleccionados: {},

            firstName:"",
            lastName:"",

            dataAccount: [],
            accNumber: [],
            dataBalance: [],
            transactions: [],
            loans1: [],
            datosTarjetas: [],
            Tarjetas: [],

            debit: [],
            credit: [],
            oneCard:"",

            typeSelector: "",
            accountsActive:[],



            accountTypeSelected:"",
            oneAccount:"",


            nameLoan: "",
            amountLoan: 0,
            interestLoan: 0,
            paymentsLoan: [6, 12, 18, 24, 36, 48, 60],
            paymentLoans: []
            
        };
    },

    created() {


        axios.get(`http://localhost:8080/api/clients/current/accounts`).then((dataAcc) => {
            this.account = dataAcc.data;
            this.accNumber = this.accountsActive.number
            this.dataBalance = this.accountsActive.balance
            this.transactionsDTO1 = this.account[0].transactionsDTO[0].account.transactions


            
            this.accountsActive = this.account.filter(perAccount => perAccount.activeAccount == true)
            console.log(this.account)


        });
        axios.get(`http://localhost:8080/api/clients/current`).then((data) => {
            this.datos = data;
            this.clientes = this.datos.data;
            this.dataAccount = this.clientes.accounts;
            this.firstName = this.clientes.firstName;
            this.lastName = this.clientes.lastName;
            this.loans1 = this.clientes.loans



            console.log(this.dataAccount)

        });

        axios.get(`http://localhost:8080/api/clients/current`).then((dataCards) => {
            this.datosTarjetas = dataCards.data.cards;
            this.debit = this.datosTarjetas.filter(cardDebit => cardDebit.enabled == true && cardDebit.type === "DEBIT"  )
            this.credit = this.datosTarjetas.filter(cardDebit => cardDebit.enabled == true  && cardDebit.type === "CREDIT" )
            // this.Tarjetas = dataCards.data._embedded.cards.sort((a, b) => a.id - b.id);


            console.log(this.debit);
            console.log(this.credit);
            console.log(this.datosTarjetas);
            console.log(this.datosTarjetas);
        });


    






        
    },

    methods: {


        createTypeLoan() {
            let newLoan = {
              name: this.nameLoan,
              maxAmount: this.amountLoan,
              payments: this.paymentLoans,
              interest: this.interestLoan
            }
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
                axios.post('http://localhost:8080/api/loans/createLoan', newLoan)
                  .catch(function (error) {
                    this.error = error.response.data
                  })
                  .then(
                    setTimeout(function () {
                      if (this.error == "Only admins can enter here" || this.error == "Loan already exists, enter another name" || this.error == "Missing Data") {
                        Swal.fire({
                          title: "Error",
                          text: `${this.error}`,
                          icon: "error"
                        })
                        this.error = ""
                      } else {
                        Swal.fire({
                          title: "New Loan",
                          text: "New Type Loan Created",
                          icon: "success"
                        })
                        setTimeout(function () {
                          // location.reload()
                        }, 1000)
                      }
                    }, 1000))
              }
            })
          },



    },

    computed: {},
}).mount("#app");