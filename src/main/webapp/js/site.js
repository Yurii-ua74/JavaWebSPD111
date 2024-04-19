document.addEventListener( 'DOMContentLoaded', () => {
    // шукаємо кнопку реєстрації, якщо знаходимо - додаємо обробник
    const signupButton = document.getElementById("signup-button");
    if (signupButton) {
        signupButton.onclick = signupButtonClick;
    }
    // шукаємо кнопку автентифікації, якщо знаходимо - додаємо обробник
    const authButton = document.getElementById("auth-button");
    if (authButton) {
        authButton.onclick = authButtonClick;
    }
    // налаштування модальних вікон
    var elems = document.querySelectorAll('.modal');
    M.Modal.init(elems, {
        "opacity": 0.5, 	      // Opacity of the modal overlay.
        "inDuration": 250, 	      // Transition in duration in milliseconds.
        "outDuration": 250, 	  // Transition out duration in milliseconds.
        "onOpenStart": null,	  // Callback function called before modal is opened.
        "onOpenEnd": null,	      // Callback function called after modal is opened.
        "onCloseStart": null,	  // Callback function called before modal is closed.
        "onCloseEnd": null,	      // Callback function called after modal is closed.
        "preventScrolling": true, // Prevent page from scrolling while modal is open.
        "dismissible": true,	  // Allow modal to be dismissed by keyboard or overlay click.
        "startingTop": '4%',	  // Starting top offset
        "endingTop": '10%'	      // Ending top offset
    });
    checkAuth();
});

function serveCartButtons() {
    // шукаємо id користувача (з його аватарки)
    const userId = document.querySelector('[data-user-id]').getAttribute('data-user-id');
    // шукаємо всі кнопки "додати до кошику" за ознакою data-product="..."
    for( let btn of document.querySelectorAll('[data-product]') ) {
        btn.onclick = () => {
            // вилучаємо id з атрибута
            let productId = btn.getAttribute('data-product');
            // при натисненні надсилаємо запит до API
            fetch(`/${getContext()}/shop-api?user-id=${userId}&product-id=${productId}`, {
                method: 'PUT'
            })
                .then(r => r.json())
                .then(console.log);
        }
    }}

function  getContext() {
    return window.location.pathname.split('/')[1];
}

function authButtonClick(e) {
    const emailInput = document.querySelector('input[name="auth-email"]');
    if( ! emailInput ) { throw "'auth-email' not found" ; }
    const passwordInput = document.querySelector('input[name="auth-password"]');
    if( ! passwordInput ) { throw "'auth-password' not found" ; }

    // console.log( emailInput.value, passwordInput.value ) ;
    fetch(`/${getContext()}/auth?email=${emailInput.value}&password=${passwordInput.value}`, {
        method: 'GET'
    })
        .then( r => r.json() )
        .then( j => {
            if( j.data == null || typeof j.data.token == "undefined" ) {
                document.getElementById("modal-auth-message").innerText = "У вході відмовлено";
            }
            else {
                // авторизація токенами передбачає їх збереження з метою подальшого використання
                // Для того щоб токени були доступні після перезавантаження їх вміщують
                // до постійного сховища браузера - localStorage ...
                localStorage.setItem("auth-token", j.data.token);
                window.location.reload();
            }
        } ) ;
}
///////////////////////////////////   //////////////////    //////////////////////////
function checkAuth() {
    // ... при завантаженні сторінки перевіряємо наявність даних автентифікації у localStorage
    const  authToken = localStorage.getItem("auth-token");
    if (authToken) {
        //перевіряємо токен на валідність
        fetch(`/${getContext()}/auth?token=${authToken}`, {
            method: 'POST'
        })
            .then(r => r.json())
            // .then(console.log);
            .then(j => {
                if (j.meta.status === 'success') {
                    // замінити "кнопку" входу на аватарку користувача
                    document.querySelector('[data-auth="avatar"]')
                        .innerHTML = `<img data-user-id="${j.data.id}" title="${j.data.name}" class="nav-avatar" src="/${getContext()}/img/avatar/${j.data.avatar}"  alt="avatar"/>`

                    const product = document.querySelector('[data-auth="product"]');
                    if (product) {
                        fetch(`/${getContext()}/product.jsp`)
                            .then(r => r.text())
                            .then(t => {
                                product.innerHTML = t;
                                document.getElementById("add-product-button")
                                    .addEventListener('click', addProductClick);
                            });
                    }
                    serveCartButtons();
                }
            });
    }
}

// Визначення функції для відображення помилки
function displayError(elementId, message) {
    var errorSpan = document.getElementById(elementId);
    if (errorSpan) {
        errorSpan.innerHTML = message;
        errorSpan.style.display = message ? 'block' : 'none'; // Показуємо або ховаємо, залежно від наявності повідомлення
    }
}

function addProductClick(e) {
    // Збираємо дані з форми додавання продукту
    const form = e.target.closest('form');
    const name = form.querySelector("#product-name").value.trim();
    const price = Number(form.querySelector("#product-price").value);
    const description = form.querySelector("#product-description").value.trim();
    const fileInput = form.querySelector("#product-img");
    // Проводимо валідацію

    var isValid = true; // Перемикач валідності форми

    // Очищення повідомлень про помилки
    ['product-name-error', 'product-price-error', 'product-description-error', 'product-img-error'].forEach(function(id) {
        displayError(id, '');
    });

    // Перевірка поля "Назва товару"
    if (name === '') {
        displayError('product-name-error', 'Назва товару не може бути порожньою');
        isValid = false;
    } else if (!/^[a-zA-Zа-яА-Я\s]+$/.test(name)) {
        displayError('product-name-error', 'Назва товару може містити лише літери та пробіли');
        isValid = false;
    }

    // Перевірка поля "Ціна товару"
    if (isNaN(price) || price <= 0) {
        displayError('product-price-error', 'Ціна товару має бути більше нуля');
        isValid = false;
    }

    // Перевірка поля "Опис товару"
    if (description.length < 10 || description.length > 100) {
        displayError('product-description-error', 'Опис товару має містити від 10 до 100 символів');
        isValid = false;
    }

    // Перевірка поля "Зображення товару"
    if (fileInput.value.trim() !== '' && !/(\.jpg|\.jpeg|\.png)$/i.exec(fileInput.value)) {
        displayError('product-img-error', 'Файл зображення має бути у форматі JPG або PNG');
        isValid = false;
    }

    // Якщо форма валідна, відправляємо її
    if (isValid) {
        const formData = new FormData();
        formData.append("name", name);
        formData.append("price", price);
        formData.append("description", description);
        formData.append("image", fileInput.files[0]);
        formData.append("token", localStorage.getItem("auth-token"));

        fetch(`/${getContext()}/shop-api`, {
            method: 'POST',
            body: formData
        })
            .then( r => r.json() )
            .then( console.log );
    }
}

function signupButtonClick(e) {
    // шукаємо форму - батьківській елемент кнопки (e.target)
    const signupForm = e.target.closest('form') ;
    if( ! signupForm ) {
        throw "Signup form not found" ;
    }
    // всередині форми signupForm знаходимо елементи
    const nameInput = signupForm.querySelector('input[name="user-name"]');
    if( ! nameInput ) { throw "nameInput not found" ; }
    const emailInput = signupForm.querySelector('input[name="user-email"]');
    if( ! emailInput ) { throw "emailInput not found" ; }
    const passwordInput = signupForm.querySelector('input[name="user-password"]');
    if( ! passwordInput ) { throw "passwordInput not found" ; }
    const repeatInput = signupForm.querySelector('input[name="user-repeat"]');
    if( ! repeatInput ) { throw "repeatInput not found" ; }
    const avatarInput = signupForm.querySelector('input[name="user-avatar"]');
    if( ! avatarInput ) { throw "avatarInput not found" ; }

    /// Валідація даних
    let isFormValid = true ;

    if( nameInput.value === "" ) {
        nameInput.classList.remove("valid");
        nameInput.classList.add("invalid");
        isFormValid = false ;
    }
    else {
        nameInput.classList.remove("invalid");
        nameInput.classList.add("valid");
    }

    if( ! isFormValid ) return ;
    /// кінець валідації

    // формуємо дані для передачі на бекенд
    const formData = new FormData() ;
    formData.append( "user-name", nameInput.value ) ;
    formData.append( "user-email", emailInput.value ) ;
    formData.append( "user-password", passwordInput.value ) ;
    if( avatarInput.files.length > 0 ) {
        formData.append( "user-avatar", avatarInput.files[0] ) ;
    }

    // передаємо - формуємо запит
    fetch( window.location.href, { method: 'POST', body: formData } )
        .then( r => r.json() )
        .then( j => {
            //console.log(j);

            //if( j.status == 1 ) {  // реєстрація успішна
            //    alert( 'реєстрація успішна' ) ;
            //    window.location = '/' ;  // переходимо на головну сторінку
            //}
            //else {  // помилка реєстрації (повідомлення - у полі message)
            //    /* alert( j.data.message ) ; */
            //    alert( 'Помилка реєстрації' ) ;
            //    window.location = '/home' ;
            //}
            if(j.meta.status === "success"){// реєстрація успішна
                alert("Вітаємо, Ви успішно зареєстровані");
                // Перенаправлення на домашню сторінку
                // window.location = '/home';
                window.location.href = window.location.href.replace("/signup", "");
            }
            else { // помилка реєстрації
                // window.location.reload();
                alert( "Помилка реєстрації: " + j.meta.message ) ;
                // Очищення полів пароля
                passwordInput.value = '';
                repeatInput.value = '';
            }
        } ) ;
}
