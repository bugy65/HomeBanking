const app = Vue.createApp({

    data() {
      return {
        message: 'Hello Vue!',
        cardNumber: 0,
        cvv: 0,
        amount: 0,
        description: "",
      }
    },
    created() {
  
    },
    methods: {
      makePayment(){
        let newPayment = {
          cardNumber: this.cardNumber,
          cvv: this.cvv,
          amount: this.amount,
          description: this.description,
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
            axios.post('http://localhost:8080/api/transactions/cardPayment', newPayment)
              .catch(function (error) {
                this.error = error.response.data
              })
              .then(
                setTimeout(function () {
                  if (this.error == "This Account does not have enough balance" || this.error == "empty card number field" || this.error == "You cannot send a negative amount" || this.error == "Invalid CVV" || this.error == "Empty description" || this.error == "The card is expired") {
                    Swal.fire({
                      title: "Error",
                      text: `${this.error}`,
                      icon: "error"
                    })
                    this.error = ""
                  } else {
                    Swal.fire({
                      title: "PayOUT",
                      text: "Transaction made",
                      icon: "success"
                    })
                    setTimeout(function () {
                      // location.reload()
                    }, 1000)
                  }
                }, 1000))
          }
        })
      }
  
    },
    computed: {
  
    },
  }).mount('#app')
  
  