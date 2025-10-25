

//Only letters, numbers, and underscores are allowed
function validateUsername(username) {
    for (let char of username) {
        if (!((char >= 'a' && char <= 'z') || (char >= 'A' && char <= 'Z') || (char >= '0' && char <= '9') || char === '_') || username.length < 4 || username.length > 20) {
            return false;
        }
    }
    return true;
}

//Basic email format validation
function validateEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

//Password must be at least 8 characters, contain uppercase, number, 
function validatePassword(password) {
    const minLength = 8;
    const hasUppercase = /[A-Z]/.test(password);
    const hasNumber = /[0-9]/.test(password);
    return password.length >= minLength && hasUppercase && hasNumber;
}


function showError(fieldId, message) {
    // 1. Construct the ID of the error div
    const errorElementId = fieldId + 'Error';
    
    // 2. Find the error element on the page
    const errorElement = document.getElementById(errorElementId);

    // 3. If it exists, set its text and show it
    if (errorElement) {
        errorElement.textContent = message;
        errorElement.style.display = 'block';
    }
}

function clearError(fieldId) {
    const errorElementId = fieldId + 'Error';
    const errorElement = document.getElementById(errorElementId);
    if (errorElement) {
        errorElement.textContent = '';
        errorElement.style.display = 'none'; // Hide error
    }
}

function borderColorOnError(fieldId, hasError){
    const inputElement = document.getElementById(fieldId);
    if (inputElement) {
        if (hasError) {
            inputElement.classList.add('valid');
            inputElement.classList.remove('invalid');
        } else {
            inputElement.classList.add('invalid');
            inputElement.classList.add('valid');
        }
    }  
}
function validateForm() {
    let isFormValid = true;

    const usernameInput = document.getElementById('username');
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const confirmPasswordInput = document.getElementById('confirmPassword');
    const submitButton = document.getElementById('submitBtn');

    // Clear previous errors
    clearError('username');
    clearError('email');
    clearError('password');
    clearError('confirmPassword');

    //Check username
    if (validateUsername(usernameInput.value)) {
        borderColorOnError('username', true); // It's valid
    } else {
        showError('username', 'Invalid username. Only letters, numbers, and underscores are allowed.');
        borderColorOnError('username', false); // It's invalid
        isFormValid = false;
    }
    
    //Check email
    if (validateEmail(emailInput.value)) {
        borderColorOnError('email', true); // It's valid
    } else {
        showError('email', 'Invalid email format.');
        borderColorOnError('email', false); // It's invalid
        isFormValid = false;
    }

    //Check password
    if (validatePassword(passwordInput.value)) {
        borderColorOnError('password', true); // It's valid
    } else {
        showError('password', 'Password must be at least 8 characters long, contain an uppercase letter and a number.');
        borderColorOnError('password', false); // It's invalid
        isFormValid = false;
    }
    
    //Check password match
    // This check depends on the password field, so we must check password *first*
    if (validatePassword(passwordInput.value) && passwordInput.value === confirmPasswordInput.value) {
        borderColorOnError('confirmPassword', true); // It's valid
    } else {
        // Only show "Passwords do not match" if the password itself is valid but the confirmation isn't
        if(validatePassword(passwordInput.value)) {
            showError('confirmPassword', 'Passwords do not match.');
        }
        borderColorOnError('confirmPassword', false); // It's invalid
        isFormValid = false;
    }

    submitButton.disabled = !isFormValid;
    
    return isFormValid;
}
// TODO: Add event listeners for real-time validation

const inputs = ['username', 'email', 'password', 'confirmPassword'];
inputs.forEach(id => {
    document.getElementById(id).addEventListener('input', validateForm);
});


document.getElementById('signupForm').addEventListener('submit', function(e) {
    e.preventDefault();
    // TODO: Handle form submission

    if (validateForm()) {
        alert('Form submitted successfully!');
    }
   
    
});