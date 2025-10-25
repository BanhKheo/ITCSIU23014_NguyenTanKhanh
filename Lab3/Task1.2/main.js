// Product data
const products = [
    { id: 1, name: 'Laptop', price: 999.99, image: '💻' },
    { id: 2, name: 'Smartphone', price: 699.99, image: '📱' },
    { id: 3, name: 'Headphones', price: 199.99, image: '🎧' },
    { id: 4, name: 'Smartwatch', price: 299.99, image: '⌚' }
];

// Cart array
let cart = [];

// TODO: Implement functions

function addToCart(productId) {
    const product = products.find(p => p.id === productId);
    if (product) {
        const cartItem = cart.find(item => item.id === productId);
        if (cartItem) {
            cartItem.quantity += 1;
        } else {
            cart.push({ ...product, quantity: 1 });
        }
        renderCart();
    }


}

function removeFromCart(itemId) {
    cart = cart.filter(item => item.id !== itemId);
    renderCart();
}

function updateQuantity(productId, change) {
    for (let item of cart) {
        if (item.id === productId) {
            item.quantity += change;
            if (item.quantity <= 0) {
                cart = cart.filter(i => i.id !== productId);
            }
            break;
        }
    }
    renderCart();
}   

function calculateTotal() {
    return cart.reduce((total, item) => total + item.price * item.quantity, 0);
}

function renderProducts() {
    const productList = document.getElementById('productsGrid');
    products.forEach(product => {
        const productDiv = document.createElement('div');
        productDiv.className = 'product';
        productDiv.innerHTML = `
            <div class="product-image">${product.image}</div>
            <div class="product-name">${product.name}</div>
            <div class="product-price">$${product.price.toFixed(2)}</div>
            <button onclick="addToCart(${product.id})  ">Add to Cart</button>
        `;
        productList.appendChild(productDiv);
    });
}

function renderCart() {
    const cartList = document.getElementById('cartItems');
    cartList.innerHTML = '';
    cart.forEach(item => {
        const cartItemDiv = document.createElement('div');
        cartItemDiv.className = 'cart-item';
        cartItemDiv.innerHTML = `
            <div>${item.name} (x${item.quantity}) - $${(item.price * item.quantity).toFixed(2)}</div>
            <button onclick="updateQuantity(${item.id}, 1)">+</button>
            <button onclick="updateQuantity(${item.id}, -1)">-</button>
            <button onclick="removeFromCart(${item.id})">Remove</button>
        `;
        cartList.appendChild(cartItemDiv);
    });
    const totalPrice = calculateTotal();
    document.getElementById('cartTotal').innerText = `Total: $${totalPrice.toFixed(2)}`;
    
}

function toggleCart() {
    const cartSection = document.getElementById('cartSection');
    if (cartSection.style.display === 'none' || cartSection.style.display === '') {
        cartSection.style.display = 'block';
    } else {
        cartSection.style.display = 'none';
    }
}

// Initialize
renderProducts();