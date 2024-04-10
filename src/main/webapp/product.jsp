<h1>Додати новий товар</h1>

<form method="post" id="add-product-form">
    <div class="row">
        <div class="col s12">
            <div class="row">

                <div class="input-field col s12 m6">
                    <input id="product-name" name="product-name" type="text" class="validate" required>
                    <label for="product-name">Назва товару</label>
                    <span class="helper-text" data-error="wrong" data-success="right">Використовувайте лише літери</span>
                    <span id="product-name-error" class="error-message" style="color: red;"></span>
                </div>

                <div class="input-field col s12 m6">
                    <input id="product-price" name="product-price" type="number" class="validate" min="0" required>
                    <label for="product-price">Ціна товару</label>
                    <span class="helper-text" data-error="wrong" data-success="right">Використовувайте лише цифри</span>
                    <span id="product-price-error" class="error-message" style="color: red;"></span>
                </div>

                <div class="input-field col s12 m12">
                    <textarea id="product-description" name="product-description" class="materialize-textarea" pattern=".{10,50}" title="Поле має містити більше 10 та менше 50 символів" required></textarea>
                    <label for="product-description">Опис товару</label>
                    <span id="product-description-error" class="error-message" style="color: red;"></span>
                </div>

                <div class="file-field input-field col s12 m6">
                    <div class="btn brown">
                        <span>Зображення товару</span>
                        <input id="product-img" name="product-img" type="file" accept="image/png, image/jpeg, image/jpg">
                    </div>
                    <div class="file-path-wrapper">
                        <input  id="product-img-path" class="file-path validate" type="text">
                        <span id="product-img-error" class="error-message" style="color: red;"></span>
                    </div>
                </div>

            </div>

        </div>

    </div>
    <div class="row">
        <div class="col s12 m12 center">
            <button type="button" class="btn brown" id="add-product-button">Зберегти</button>
            <div id="add-product-result"></div>
        </div>
    </div>
    <div id="error-container"></div>
</form>