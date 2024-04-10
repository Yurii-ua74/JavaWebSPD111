<%@ page contentType="text/html;charset=UTF-8" %>

<h1>Додайте новий товар</h1>
<div class="row">
    <form class="col s12" method="post">
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">cases</i>
                <input id="icon_prefix" type="text" name="user-name"
                       class="" >
                <label for="icon_prefix">Назва товару</label>
                <span class="helper-text"
                      data-error="Це необхідне поле"
                      data-success="Правильно">Назва товару</span>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">mail</i>
                <input  id="icon_email" type="tel"  name="user-email">
                <label for="icon_email">E-mail продавця</label>
                <span class="helper-text"
                      data-error="Це необхідне поле"
                      data-success="Правильно">Адреса електронної пошти</span>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">payments</i>
                <input  id="icon_password" type="text" name="user-password" >
                <label for="icon_password">Ціна роздрібна</label>
                <span class="helper-text"
                      data-error="Це необхідне поле"
                      data-success="Припустимо">Ціна роздрібна</span>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">price_change</i>
                <input id="icon_repeat" type="text"  name="user-repeat" inputmode="tel">
                <label for="icon_repeat">Ціна оптова</label>
                <span class="helper-text"
                      data-error="Це необхідне поле"
                      data-success="Правильно">Ціна оптова</span>
            </div>
        </div>
        <div class="row">
            <div class="file-field input-field col s6">
                <div class="btn brown">
                    <i class="material-icons">photo</i>
                    <input type="file" name="user-avatar">
                </div>
                <div class="file-path-wrapper">
                    <label>
                        <input class="file-path validate" type="text" placeholder="Аватарка">
                    </label>
                </div>
            </div>
            <div class="input-field col s6">
                <button type="button" id="signup-button" class="btn brown right"><i class="material-icons left">task_alt</i>Додати</button>
            </div>
        </div>
    </form>
</div>
