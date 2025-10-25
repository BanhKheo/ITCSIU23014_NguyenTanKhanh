# 🚀 LAB 3 
# Task 1.1: Interactive Form Validator (15 Points)

> ✨ A real-time **interactive form validator** built with HTML, CSS, and JavaScript.  
> It validates user input before submission — similar to real-world signup forms used on websites like Facebook or Gmail.

## Results

![alt text](Lab3/Task1.2/img/resultExer1.png.png)
## 🧠 JavaScript Logic

### ShowError
![alt text](Task1.1/img/ShowError.png.png)

- Field_id is the name of input field + string 'Error', we can find the hidden div with 'FieldId' + 'Error' and visble is to the page 
- Add the msg to the div because it empty


### 
![alt text](/Task1.1/img/borderColorOnError.pngpng)
- This is using in the validate Form method if user input wrong add the class 'invalid' and remove 'valid' to the input tag, in constrast add the class'valid' and remove 'invalid'

# Task 1.2: Interactive Form Validator (15 Points)

## Results

![alt text](Task1.2/img/resultExer2.png.png.png)
## 🧠 JavaScript Logic

### Render Product 
![alt text](Task1.2/img/renderProduct.png.png)

- Loop through product object array to render all product with div include picture, name, price, and button add to cart
- And render at beginning of the web

### Toggle the cart

![alt text](Task1.2/img/toggleCart.png.png)

- Onclick on the cart icon and open the div cart is hidding and click again to hide

### Add to cart

![alt text](Task1.2/img/addToCart.png.png)

- If the product already in the cart increase the cart quantity by 1
- If not add product to the cart

### Render the cart
![alt text](Task1.2/img/renderCart.png.png.png)

- if cart has item create the div item content quantity, plus, subtract button and totals prices



